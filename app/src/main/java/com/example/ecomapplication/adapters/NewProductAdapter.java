package com.example.ecomapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecomapplication.R;
import com.example.ecomapplication.models.Product;

import java.io.Serializable;
import java.util.List;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;

    public NewProductAdapter(Context context, List<Product> newProductList) {
        this.context = context;
        this.productList = newProductList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_products,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.newName.setText(productList.get(position).getName());
        holder.newPrice.setText(productList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView newImg;
        TextView newName, newPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newImg = itemView.findViewById(R.id.new_img);
            newPrice = itemView.findViewById(R.id.new_price);
            newName = itemView.findViewById(R.id.new_product_name);
        }
    }
}
