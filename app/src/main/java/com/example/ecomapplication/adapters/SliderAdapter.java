package com.example.ecomapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.activities.DetailActivity;
import com.example.ecomapplication.models.Product;
import com.example.ecomapplication.models.SliderData;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder>{

    // list for storing urls of images.
    private final List<Product> mSliderItems;
    private Context context;
    private FirebaseStorage storage;

    // Constructor
    public SliderAdapter(Context context, ArrayList<Product> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();

    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                Log.v("fdfsd", mSliderItems.get(position).getName());
                intent.putExtra("id_product",  mSliderItems.get(position).getDocumentId());
                context.startActivity(intent);
            }
        });
        final Product sliderItem = mSliderItems.get(position);
        Log.v("IMg" , sliderItem.getImg_url());
        // Glide is use to load image
        // from url in your imageview.
        StorageReference storageReference = storage.getReferenceFromUrl(sliderItem.getImg_url());

        // Dat anh lay tu Firebase cho item
        storageReference.getDownloadUrl()
                .addOnSuccessListener(uri -> Picasso.get().load(uri.toString()).into(viewHolder.imageViewBackground))
                .addOnFailureListener(e -> Log.v("Error", "Error when get the images: " + e));
//        Glide.with(viewHolder.itemView)
//                .load(sliderItem.getImg_url())
//                .fitCenter()
//                .into(viewHolder.imageViewBackground);
    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            this.itemView = itemView;
        }
    }
}