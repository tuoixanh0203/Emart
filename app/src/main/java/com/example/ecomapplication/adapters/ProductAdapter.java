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

public class ProductAdapter extends ArrayAdapter<Product> {
    private final Context context;
    private final List<Product> list;
    private final FirebaseStorage storage;

    public ProductAdapter(Context context, List<Product> list) {
        super(context, R.layout.product_items, list);
        this.context = context;
        this.list = list;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.product_items, parent, false);

        Product product = list.get(position);

        ImageView imageView = view.findViewById(R.id.all_img);
        TextView namePro = view.findViewById(R.id.all_product_name);
        TextView pricePro = view.findViewById(R.id.all_price);
        TextView ratingValue = view.findViewById(R.id.rating_value);
        RatingBar ratingStar = view.findViewById(R.id.rating_star);

        if (!product.getImg_url().equals("")) {
            StorageReference storageReference = storage.getReferenceFromUrl(product.getImg_url());

            // Dat anh lay tu Firebase cho item
            storageReference.getDownloadUrl()
                    .addOnSuccessListener(uri -> Picasso.get().load(uri.toString()).into(imageView))
                    .addOnFailureListener(e -> Log.v("Error", "Error when get the images: " + e));
        }
        namePro.setText(product.getName());
        pricePro.setText(String.valueOf(product.getPrice()));
        ratingValue.setText(product.getRating());
        ratingStar.setRating(Float.parseFloat(product.getRating()));
        Log.v("Test", product.getName() + " " + product.getPrice());
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
