package com.example.ecomapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ecomapplication.R;
import com.example.ecomapplication.models.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends ArrayAdapter<Product> {
    private final Context context;
    private final List<Product> search;
    private final FirebaseStorage storage;

    public SearchAdapter(@NonNull Context context, List<Product> search) {
        super(context, R.layout.search_item, search);
        this.context = context;
        this.search = search;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.search_item, parent, false);

        Product product = search.get(position);

        ImageView imageView = view.findViewById(R.id.search_img);
        TextView textView = view.findViewById(R.id.search_name);

        if (!product.getImg_url().equals("")) {
            Log.v("Result", product.getImg_url());
            StorageReference storageReference = storage.getReferenceFromUrl(product.getImg_url());

            // Dat anh lay tu Firebase cho item
            storageReference.getDownloadUrl()
                    .addOnSuccessListener(uri -> Picasso.get().load(uri.toString()).into(imageView))
                    .addOnFailureListener(e -> Log.v("Error", "Error when get the images: " + e));
        }
        textView.setText(product.getName());

        return view;
    }

    @Override
    public int getCount() {
        return search.size();
    }
}
