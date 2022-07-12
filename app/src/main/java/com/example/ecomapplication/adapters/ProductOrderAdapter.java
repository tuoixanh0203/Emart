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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecomapplication.R;
import com.example.ecomapplication.models.Category;
import com.example.ecomapplication.models.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductOrderAdapter extends ArrayAdapter<Product> {
    private final Context context;
    private final List<Product> list;
    private final FirebaseStorage storage;

    public ProductOrderAdapter(Context context, List<Product> list) {
        super(context, R.layout.product_order_item, list);
        this.context = context;
        this.list = list;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.product_order_item, parent, false);

        Product product = list.get(position);

        ImageView imageView = view.findViewById(R.id.img_product_order);
        TextView namePro = view.findViewById(R.id.name_product_order);
        TextView pricePro = view.findViewById(R.id.total_price_order);
        TextView quantity = view.findViewById(R.id.quantity_product_order);
        TextView all = view.findViewById(R.id.all_total_price_order);


        if (!product.getImg_url().equals("")) {
            StorageReference storageReference = storage.getReferenceFromUrl(product.getImg_url());

            // Dat anh lay tu Firebase cho item
            storageReference.getDownloadUrl()
                    .addOnSuccessListener(uri -> Picasso.get().load(uri.toString()).into(imageView))
                    .addOnFailureListener(e -> Log.v("Error", "Error when get the images: " + e));
        }
        int pri = product.getPrice() * product.getQuantity();
        Log.v("Test", String.valueOf(pri));

        namePro.setText(product.getName());
        pricePro.setText(String.valueOf(pri));
        quantity.setText("x" + product.getQuantity());
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
