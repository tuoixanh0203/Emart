package com.example.ecomapplication.adapters;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ecomapplication.R;

import java.util.List;

public class AddressAdapter  extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private Context context;
    private List<String> address;

    public AddressAdapter(Context context, List<String> address) {
        this.context = context;
        this.address = address;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item , parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        Glide.with(context).load(popularProductAdapterList.get(position).getImg_url()).into(holder.imageView);
        holder.address_name.setText(address.get(position));
//        holder.price.setText((Integer) products.get(position).getPrice());
        holder.address_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("TAG", address.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return address.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView address_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            address_name = itemView.findViewById(R.id.address_names);
        }
    }
}
