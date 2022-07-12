package com.example.ecomapplication.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomapplication.Helper.NotificationApi;
import com.example.ecomapplication.R;
import com.example.ecomapplication.models.FCMNotification;
import com.example.ecomapplication.models.SellerOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class OrderAdapterSeller extends RecyclerView.Adapter<OrderAdapterSeller.ViewHolder> {
    Context context;
    List<SellerOrder> list;
    FirebaseFirestore db;
    private FirebaseAuth auth;

    public OrderAdapterSeller(Context context, List<SellerOrder> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        return new OrderAdapterSeller.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_seller, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SellerOrder orderModel = list.get(position);

        Date ordered = list.get(position).getOrderDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ordered);
        SimpleDateFormat sdf = new SimpleDateFormat("E yyyy-MM-dd hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String order_date = sdf.format(calendar.getTime());
        Log.v("datea" , order_date);
        holder.productName.setText(list.get(position).getId_product());
        holder.orderDate.setText(String.valueOf(order_date));
        holder.quantity.setText(String.valueOf(list.get(position).getQuantity()));
        holder.userInfo.setText(String.valueOf(list.get(position).getUser_name()));
        if (list.get(position).getStatus().equals("confirm")) {
            holder.buttonConfirm.setText("Đã xác nhận");
            holder.buttonConfirm.setEnabled(false);
            holder.buttonCancel.setEnabled(false);
            holder.buttonCancel.setVisibility(View.INVISIBLE);
        }else if (list.get(position).getStatus().equals("cancelled")) {
            holder.buttonConfirm.setEnabled(false);
            holder.buttonCancel.setEnabled(false);
            holder.buttonCancel.setText("Đã hủy bỏ");
            holder.buttonConfirm.setVisibility(View.INVISIBLE);

        } else {
            holder.buttonConfirm.setText("Xác nhận");
        }
        int i = position;
        holder.buttonCancel.setOnClickListener(view -> {
            Log.v("tagggggg", "huy");
            Toast.makeText(context, "Đã hủy đơn đặt hàng!", Toast.LENGTH_SHORT).show();
            holder.buttonCancel.setEnabled(false);
            holder.buttonCancel.setText("Đã hủy bỏ");
            holder.buttonConfirm.setVisibility(View.INVISIBLE);

            cancelOrder(i);
        });

        holder.buttonConfirm.setOnClickListener(view -> {
            Toast.makeText(context, "Đã xác nhận đơn đặt hàng!", Toast.LENGTH_SHORT).show();
            ConfirmOrder(i);
            holder.buttonConfirm.setEnabled(false);
            holder.buttonCancel.setVisibility(View.INVISIBLE);
            holder.buttonConfirm.setText("Đã xác nhận");
            holder.buttonCancel.setEnabled(false);
        });
    }
    public void cancelOrder(int position ){
        db.collection("SellerOrder").document(list.get(position).getId_seller()).collection("Orders").document(list.get(position).getIdDocument()).update(
                "status", "cancelled");
        checkOrderStatus("cancelled",list.get(position).getId_order(), list.get(position).getId_user());

        db.collection("UserInfo").document(list.get(position).getId_user())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> registrationIds = new ArrayList<>();
                        DocumentSnapshot documentSnapshot = task.getResult();
                        String deviceToken = documentSnapshot.getString("deviceToken");
                        Log.v("Test", "Receiver device token: " + deviceToken);
                        registrationIds.add(deviceToken);

                        FCMNotification.Notification notification = FCMNotification.createNotification(
                                FCMNotification.getCancelOrderTitle(),
                                FCMNotification.getCancelOrderBody(list.get(position).getIdDocument())
                        );

                        FCMNotification.Data data = FCMNotification.createData(
                                FCMNotification.getCancelOrderTitle(),
                                FCMNotification.getCancelOrderBody(list.get(position).getIdDocument())
                        );

                        FCMNotification FcmNotification = new FCMNotification(notification, data, registrationIds);
                        new PushNotification(context).execute(FcmNotification);
                    }
                });
    }

    public void checkOrderStatus(String status, String id_order, String id_user){
        String newOderStatus = status.equals("confirm") ? "processed" : "cancelled";
        db.collection("Order").document(id_user).collection("Orders").document(id_order).update(
                "status", newOderStatus);
    }

    public void ConfirmOrder(int position) {
        db.collection("SellerOrder").document(list.get(position).getId_seller()).collection("Orders").document(list.get(position).getIdDocument()).update(
                "status", "confirm");
        checkOrderStatus("confirm",list.get(position).getId_order(), list.get(position).getId_user());

        db.collection("UserInfo").document(list.get(position).getId_user())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> registrationIds = new ArrayList<>();
                        DocumentSnapshot documentSnapshot = task.getResult();
                        String deviceToken = documentSnapshot.getString("deviceToken");
                        Log.v("Test", "Receiver device token: " + deviceToken);
                        registrationIds.add(deviceToken);

                        FCMNotification.Notification notification = FCMNotification.createNotification(
                                FCMNotification.getConfirmOrderTitle(),
                                FCMNotification.getConfirmOrderBody(list.get(position).getIdDocument())
                        );

                        FCMNotification.Data data = FCMNotification.createData(
                                FCMNotification.getConfirmOrderTitle(),
                                FCMNotification.getConfirmOrderBody(list.get(position).getIdDocument())
                        );

                        FCMNotification FcmNotification = new FCMNotification(notification, data, registrationIds);
                        new PushNotification(context).execute(FcmNotification);
                    }
                });
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
        TextView userInfo, orderDate, productName, quantity;
        Button buttonCancel, buttonConfirm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.order_date);
            productName = itemView.findViewById(R.id.product_name);
            quantity = itemView.findViewById(R.id.quantity);
            userInfo = itemView.findViewById(R.id.user_info);
            buttonCancel = itemView.findViewById(R.id.btn_cancel);
            buttonConfirm = itemView.findViewById(R.id.btn_confirm);
        }
    }
}
