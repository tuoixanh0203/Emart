package com.example.ecomapplication.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecomapplication.R;
import com.example.ecomapplication.models.Comment;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;
    private List<Comment> mData;
    private FirebaseStorage storage;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.storage = FirebaseStorage.getInstance();
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
//        Glide.with(mContext).load(mData.get(position).getUser_img()).into(holder.img_user);
        holder.tv_name.setText(mData.get(position).getId_user());
        holder.tv_content.setText(mData.get(position).getContent());
        holder.tv_date.setText(order_date);

        StorageReference storageReference = storage.getReferenceFromUrl(mData.get(position).getComment_img());
        storageReference.getDownloadUrl()
                .addOnSuccessListener(uri -> Picasso.get().load(uri.toString()).into(holder.img_comment))
                .addOnFailureListener(e -> Log.v("Error", "Error when get the images: " + e));


        holder.rating_user.setRating(mData.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        ImageView img_user, img_comment;
        TextView tv_name, tv_content, tv_date;
        RatingBar rating_user;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            rating_user = itemView.findViewById(R.id.user_rating);
            img_comment = itemView.findViewById(R.id.comment_img);
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
