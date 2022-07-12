package com.example.ecomapplication.adapters;


import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomapplication.R;
import com.example.ecomapplication.models.Payment;

import java.util.List;

public class PaymentAdapter  extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    int selected_position = 0;
    private Context context;
    private List<Payment> payments;

    public PaymentAdapter(Context context, List<Payment> payments) {
        this.context = context;
        this.payments = payments;
        Log.v("taggg", String.valueOf(payments.size()));
    }
    public int getSelected_position(){
        return  selected_position;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_item , parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.v("fsfds", String.valueOf(selected_position));
        //        Glide.with(context).load(popularProductAdapterList.get(position).getImg_url()).into(holder.imageView);
        holder.number_account.setText(payments.get(position).getAccount_nb());
        holder.provider.setText(payments.get(position).getProvider());
        holder.itemView.setBackgroundColor(selected_position == position ? Color.RED : Color.TRANSPARENT);
        holder.itemView.findViewById(R.id.content_card_payment).setBackgroundColor(selected_position == position ? Color.GRAY : Color.CYAN);
//        holder.expired.setText(payments.get(position).getExpired());
//        holder.price.setText((Integer) products.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return payments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        ImageView imageView;
        ConstraintLayout card ;
        TextView number_account, expired, provider;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.payment_image);
            number_account = itemView.findViewById(R.id.payment_account_nb);
            expired = itemView.findViewById(R.id.payment_expired);
            card = itemView.findViewById(R.id.content_card_payment);
            provider = itemView.findViewById(R.id.payment_provider);

        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            // Updating old as well as new positions
            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);
            Intent intent = new Intent("selected_item");
            intent.putExtra("selected", String.valueOf(selected_position));
        }
    }
}
