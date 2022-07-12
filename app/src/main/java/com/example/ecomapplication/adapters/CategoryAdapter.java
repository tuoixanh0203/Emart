package com.example.ecomapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ecomapplication.R;
import com.example.ecomapplication.activities.ShowProductCategory;
import com.example.ecomapplication.models.Category;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private final Context context;
    private final List<Category> list;
    private final FirebaseStorage storage;

    public CategoryAdapter(Context context, List<Category> list) {
        super(context, R.layout.category_item, list);
        this.context = context;
        this.list = list;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.category_item, parent, false);

        Category category = list.get(position);

        ImageView imageView = view.findViewById(R.id.cate_img);
        TextView textView = view.findViewById(R.id.cate_name);
        ConstraintLayout item = view.findViewById(R.id.item_btn);

        if (!category.getImg_url().equals("")) {
            StorageReference storageReference = storage.getReferenceFromUrl(category.getImg_url());

            // Dat anh lay tu Firebase cho item
            storageReference.getDownloadUrl()
                    .addOnSuccessListener(uri -> Picasso.get().load(uri.toString()).into(imageView))
                    .addOnFailureListener(e -> Log.v("Error", "Error when get   the images: " + e));
        }
        textView.setText(category.getName());
        item.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, ShowProductCategory.class);
            intent.putExtra("type", list.get(position).getId_category());
            context.startActivity(intent);
        });

        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
