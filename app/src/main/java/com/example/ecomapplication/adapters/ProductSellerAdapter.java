package com.example.ecomapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomapplication.R;
import com.example.ecomapplication.activities.SellerOrderDetailActivity;
import com.example.ecomapplication.models.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductSellerAdapter extends RecyclerView.Adapter<ProductSellerAdapter.ViewHolder> implements ListAdapter {

    private Context context;
    private FirebaseStorage storage;

    private List<Product> products;

    public ProductSellerAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_items , parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.productId = products.get(position).getId();
        holder.name.setText(products.get(position).getName());
        holder.price.setText(String.valueOf(products.get(position).getPrice()));

        StorageReference storageReference = storage.getReferenceFromUrl(products.get(position).getImg_url());

        // Dat anh lay tu Firebase cho item
        storageReference.getDownloadUrl()
                .addOnSuccessListener(uri -> Picasso.get().load(uri.toString()).into(holder.imageView))
                .addOnFailureListener(e -> Log.v("Error", "Error when get the images: " + e));

        holder.setItemClickListener((view, clickPosition, isLongClick) -> {
            if (isLongClick) {
                Toast.makeText(context, "Long click on product ID: " + holder.productId, Toast.LENGTH_SHORT).show();
                Log.v("Mouse", products.get(position).getDocumentId());
            } else {
                Intent intent = new Intent(context, SellerOrderDetailActivity.class);

                intent.putExtra("productSellerDetail", products.get(position));
                Log.v("Mouse1", products.get(position).getDocumentId());
              //  Log.v("CowBaby", products.get(position).getName().toString());
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView imageView;
        public TextView name, price;
        public String productId;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.all_img);
            name = itemView.findViewById(R.id.all_product_name);
            price = itemView.findViewById(R.id.all_price);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);

            return true;
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position,boolean isLongClick);
    }
}
