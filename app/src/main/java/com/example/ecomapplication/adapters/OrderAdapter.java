package com.example.ecomapplication.adapters;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomapplication.Helper.NotificationApi;
import com.example.ecomapplication.R;
import com.example.ecomapplication.activities.OrderDetailActivity;
import com.example.ecomapplication.models.FCMNotification;
import com.example.ecomapplication.models.OrderModel;
import com.example.ecomapplication.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context context;
    List<OrderModel> list;
//    private FirebaseStorage storage;
    FirebaseFirestore db;
    private FirebaseAuth auth;

    public OrderAdapter(Context context, List<OrderModel> list) {
        this.context = context;
        this.list = list;
//        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new OrderAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel orderModel = list.get(position);
        holder.layoutItem.setOnClickListener(view -> onClickGoToDetail(orderModel));
        Date ordered = list.get(position).getOrderDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ordered);
        SimpleDateFormat sdf = new SimpleDateFormat("E yyyy-MM-dd hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String order_date = sdf.format(calendar.getTime());

//Will print in UTC
        holder.orderAddress.setText(list.get(position).getOrderAddress());
        holder.orderDate.setText(order_date);
        holder.status_order.setText(list.get(position).getStatus());
        holder.total.setText(String.valueOf(list.get(position).getTotal()));
        switch (list.get(position).getStatus()) {
            case "pending":
                holder.buttonReceived.setEnabled(false);
                holder.status_order.setText("Đang chờ xử lí đơn hàng");
                holder.buttonReceived.setText("Đang chờ xử lí đơn hàng");
                break;
            case "processed":
                holder.buttonReceived.setEnabled(true);
                holder.status_order.setText("Đã xử lí đơn hàng");
                holder.buttonReceived.setText("Nhận đơn hàng");
                break;
            case "cancelled":
                holder.buttonReceived.setEnabled(false);
                holder.status_order.setText("Đơn hàng bị hủy bỏ");
                holder.buttonReceived.setText("Đặt hàng không thành công");
                holder.buttonReceived.setBackgroundColor(0xFFF38E99);
                break;
            case "received":
                holder.buttonReceived.setEnabled(false);
                holder.status_order.setText("Đã nhận đơn hàng");
                holder.buttonReceived.setBackgroundColor(0xFF9EE639);
                holder.buttonReceived.setText("Đã nhận hàng");
                break;
        }
        int id = position;
        holder.buttonReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.buttonReceived.setText("Đã nhận hàng");
                holder.status_order.setText("Đã nhận đơn hàng");
                holder.buttonReceived.setBackgroundColor(0xFF9EE639);
                holder.buttonReceived.setEnabled(false);
                ReceiveOrder(id);
            }
        });
    }
    public void sendReceiveNotification ( String id_order, String id_user){
        Log.v("tag_seller___order_", id_order);
        Log.v("tag_seller___user_", id_user);
        db.collection("OrderDetail").document(id_order).collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.v("tag_seller", "tassk");
                            for (DocumentSnapshot doc :task.getResult().getDocuments()) {
                                Product product = doc.toObject(Product.class);
                                String idSeller = product.getId_seller();
                                Log.v("id_sed", idSeller);
                                db.collection("UserInfo").document(idSeller)
                                        .get().addOnCompleteListener(tasks -> {
                                            if (tasks.isSuccessful()) {
                                                List<String> registrationIds = new ArrayList<>();
                                                DocumentSnapshot documentSnapshot = tasks.getResult();
                                                String deviceToken = documentSnapshot.getString("deviceToken");
                                                Log.v("Test", "Receiver device token: " + deviceToken);
                                                Toast.makeText(context.getApplicationContext(), "Bạn đã xác nhận hàng thành công!", Toast.LENGTH_SHORT).show();
                                                registrationIds.add(deviceToken);

                                                FCMNotification.Notification notification = FCMNotification.createNotification(
                                                        FCMNotification.getReceivedOrderTitle(),
                                                        FCMNotification.getReceivedOrderBody(id_user)
                                                );

                                                FCMNotification.Data data = FCMNotification.createData(
                                                        FCMNotification.getReceivedOrderTitle(),
                                                        FCMNotification.getReceivedOrderBody(id_user)
                                                );

                                                FCMNotification FcmNotification = new FCMNotification(notification, data, registrationIds);
                                                new PushNotification(context).execute(FcmNotification);
                                            }
                                        });
                            }
                        } else {
                            Log.w("Error", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void ReceiveOrder(int position){
        db.collection("Order").document(auth.getUid()).collection("Orders").document(list.get(position).getId()).update(
                "status", "received");
        Log.v("fdsfdsf", auth.getUid());
        sendReceiveNotification(list.get(position).getId(),auth.getUid());

    }
    private void onClickGoToDetail(OrderModel orderModel) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_order", orderModel);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PushNotification extends AsyncTask<Object, Void, String> {
        protected ProgressDialog progressDialog;
        protected Context context;

        public PushNotification(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(context, 1);
            this.progressDialog.setMessage("Creating Order...");
            this.progressDialog.show();
        }

        @Override
        protected String doInBackground(Object... objects) {
            String response = null;

            try {
                Log.v("Test", "Sending notification...");
                response = NotificationApi.pushNotification((FCMNotification) objects[0]);
            } catch (Exception e) {
                Log.v("Test", "POST Error: " + e.getMessage());
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderAddress, orderDate, shippedDate, total, status_order;
        RelativeLayout layoutItem;
        Button buttonReceived;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderAddress = itemView.findViewById(R.id.order_address);
            orderDate = itemView.findViewById(R.id.order_date);
            total = itemView.findViewById(R.id.total);
            status_order = itemView.findViewById(R.id.status_order);
            layoutItem = itemView.findViewById(R.id.order_item);
            buttonReceived = itemView.findViewById(R.id.btn_received);
        }
    }
}
