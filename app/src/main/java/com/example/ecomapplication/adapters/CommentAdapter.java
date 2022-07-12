package com.example.ecomapplication.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecomapplication.R;
import com.example.ecomapplication.models.Comment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;
    private List<Comment> mData;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment, parent, false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Date ordered = mData.get(position).getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ordered);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String order_date = sdf.format(calendar.getTime());
        Glide.with(mContext).load(mData.get(position).getUser_img()).into(holder.img_user);
        holder.tv_name.setText(mData.get(position).getId_user());
        holder.tv_content.setText(mData.get(position).getContent());
        holder.tv_date.setText(order_date);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView img_user;
        TextView tv_name, tv_content, tv_date;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            img_user = itemView.findViewById(R.id.comment_user_ava);
            tv_name = itemView.findViewById(R.id.comment_username);
            tv_content = itemView.findViewById(R.id.comment_content);
            tv_date = itemView.findViewById(R.id.comment_date);
        }
    }
    private String timestampToString(Long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("hh:mm", calendar).toString();
        return date;
    }
}
