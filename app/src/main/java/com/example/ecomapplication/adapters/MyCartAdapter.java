package com.example.ecomapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomapplication.R;
import com.example.ecomapplication.models.MyCartModel;
import com.example.ecomapplication.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

//public class MyCartAdapter extends FirestoreRecyclerAdapter.Adapter<MyCartAdapter.ViewHolder> {
public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {
    Context context;
    List<Product> list;
    private final FirebaseStorage storage;
    int totalAmount = 0;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    public MyCartAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
        this.storage = FirebaseStorage.getInstance();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_test, parent, false));
    }

    private void updateTotal() {
        totalAmount = 0 ;
        int temp_total = 0;
        for(int i = 0 ; i < list.size(); i++){
            temp_total += list.get(i).getPrice() * list.get(i).getQuantity();
        }
        totalAmount = temp_total;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        Product cartModel = list.get(position);
        String id_document = list.get(position).getDocumentId();
        Log.v("fsdfdsf", String.valueOf(totalAmount));
        Intent intent = new Intent("MyTotalAmount");
        updateTotal();
        holder.buttonMinus.setOnClickListener(view -> {
            if (cartModel.getQuantity() <= 1) {
                return;
            } else {
                    firestore.collection("Cart").document(auth.getUid())
                        .collection("Products").document(id_document)
                        .update("quantity", cartModel.getQuantity() - 1);
                    list.get(position).setQuantity(cartModel.getQuantity() - 1);
                    holder.quantity.setText(String.valueOf(list.get(position).getQuantity()) );

                //reload??
//                    cartAdapter.notifyDataSetChanged();
//                    listener.refreshActivity();
            }
            totalAmount = totalAmount - list.get(position).getPrice();
            holder.price.setText(NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(list.get(position).getPrice()));
            intent.putExtra("totalAmount", totalAmount);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        });

        holder.buttonPlus.setOnClickListener(view -> {
            firestore.collection("Cart").document(auth.getUid())
                    .collection("Products").document(id_document)
                    .update("quantity", cartModel.getQuantity() + 1);
            list.get(position).setQuantity(cartModel.getQuantity() + 1);
            holder.quantity.setText(String.valueOf(list.get(position).getQuantity()) );
            totalAmount = totalAmount + list.get(position).getPrice();
            holder.price.setText(NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(list.get(position).getPrice()));
            intent.putExtra("totalAmount", totalAmount);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        });

        holder.deleteFromCart.setOnClickListener(view -> {
            firestore.collection("Cart").document(auth.getUid())
                    .collection("Products")
                    .document(id_document).delete().addOnSuccessListener(task -> {
                        list.remove(list.get(position));
                        updateTotal();
                        intent.putExtra("totalAmount", totalAmount);
                        notifyDataSetChanged();
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    });

        });
        intent.putExtra("totalAmount", totalAmount);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


        holder.name.setText(list.get(position).getName());
        holder.quantity.setText(String.valueOf(list.get(position).getQuantity()));
        holder.price.setText(NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(list.get(position).getPrice()));

//        Load IMG
        final long ONE_MEGABYTE = 1024 * 1024;
        StorageReference storageReference = storage.getReferenceFromUrl(list.get(position).getImg_url());

        // Dat anh lay tu Firebase cho item
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            DisplayMetrics displayMetrics = new DisplayMetrics();

            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            }

            holder.img_url.setImageBitmap(bitmap);
        }).addOnFailureListener(exception -> {
            // Handle any errors
            Log.v("Error", "Error when get the images");
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_url;
        TextView name, price, quantity;
        LinearLayout layoutItem;
        Button buttonMinus, buttonPlus, deleteFromCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            img_url = itemView.findViewById(R.id.img_url);
            quantity = itemView.findViewById(R.id.quantity);
            layoutItem = itemView.findViewById(R.id.cart_item);
            buttonMinus = itemView.findViewById(R.id.button_minus);
            buttonPlus = itemView.findViewById(R.id.button_plus);
            deleteFromCart = itemView.findViewById(R.id.deleteFromCart);
        }
    }
}
