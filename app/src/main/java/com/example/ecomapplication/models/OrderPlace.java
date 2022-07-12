package com.example.ecomapplication.models;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ecomapplication.Helper.NotificationApi;
import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.CheckoutAdapter;
import com.example.ecomapplication.adapters.OrderAdapterSeller;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderPlace extends Dialog implements
        android.view.View.OnClickListener {

    public Activity activity;
    public Dialog d;
    public Button yes, no;
    private TextView addressText;
    private TextView totalText;
    FirebaseFirestore db;
    private String orderAddress;
    private  String id_user;
    private Date orderDate;
    CheckoutAdapter cartAdapter;
    List<Product> cartModelList;
    private Date shippedDate;
    private int number;
    private String id;
    ProgressDialog progressDialog;
    private FirebaseAuth auth;
    UserInfo user ;
    private  Payment selectedPayment;
    private int total;

    public OrderPlace(Activity activity , String orderAddress, int total ,String id_user  , Date orderDate , Date shippedDate , Payment selectedPayment) {
        super(activity);
        this.activity = activity;
        this.orderAddress = orderAddress;
        this.total = total;
        this.id_user = id_user;
        this.orderDate = orderDate;
        this.shippedDate = shippedDate;
        this.selectedPayment = selectedPayment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.orderplace_dialog);
        db = FirebaseFirestore.getInstance();
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        totalText = findViewById(R.id.total_order);
        addressText = findViewById(R.id.address_order);
        totalText.setText(String.valueOf(total));
        addressText.setText(orderAddress);
        auth = FirebaseAuth.getInstance();
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        getProductToPayment();
        getInfoUser(auth.getUid());

    }

    public void getInfoUser(String id_user){
        db.collection("UserInfo").document(auth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            user = task.getResult().toObject(UserInfo.class);
                        }
                        catch (Exception e ) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void addPaymentOfOrder(String id_order){

        Map<String, Object> payment = new HashMap<>();
        payment.put("account_nb", selectedPayment.getAccount_nb());
        payment.put("expired", new Date());
        payment.put("id_order", id_order);
        payment.put("id_user", auth.getUid());
        payment.put("payment_type", selectedPayment.getPayment_type());
        payment.put("provider", selectedPayment.getProvider());
        db.collection("Payment")
                .add(payment)
                .addOnSuccessListener(documentReference -> {
                    Log.v(TAG, "ADD order detail thanh cong");
                })
                .addOnFailureListener(e -> Log.w(TAG, "Them order detail that bai", e));
    }

    public void orderPlace() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date d  = Calendar.getInstance().getTime();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Đang thanh toán");
        progressDialog.setMessage("Vui lòng chờ...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        for(int i = 0 ; i < cartModelList.size(); i++ ){
            Product product = cartModelList.get(i);
            Map<String, Object> order = new HashMap<>();
            id = getAlphaNumericString(5);
            order.put("orderAddress", orderAddress);
            order.put("orderDate", orderDate);
            order.put("shippedDate", shippedDate);
            order.put("status", "pending");
            order.put("total", cartModelList.get(i).getQuantity() * cartModelList.get(i).getPrice());
            // Add a new document with a generated ID
            db.collection("Order")
                    .document(auth.getUid())
                    .collection("Orders")
                    .add(order).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String id_order = task.getResult().getId();
                            AddProductListToOrderDetail(id_order, product);
                            addPaymentOfOrder(id_order);
                        } else {
                            Log.w(TAG, "Them order that bai");
                        }
                    });
        }
//
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                //Do something here
//                progressDialog.dismiss();
//            }
//        }, 1500);
    }

    public void getProductToPayment(){
        cartModelList = new ArrayList<>();
        db.collection("Cart").document(auth.getUid())
                .collection("Products").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc :task.getResult().getDocuments()) {
                            Log.v("dfsfsf", doc.getId());
                            Product myCartModel = doc.toObject(Product.class);
                            myCartModel.setDocumentId(doc.getId());
                            cartModelList.add(myCartModel);
                        }
                    }
                });
    }

    public void AddProductListToOrderDetail(String id_order, Product product){
         boolean[] check = {false};
            db.collection("OrderDetail").document(id_order)
                    .collection("Products").document(product.getDocumentId())
                    .set(product)
                    .addOnSuccessListener(documentReference -> {
                        check[0] = true;
                        Log.v("Test", "ADD order detail thanh cong");
                    })
                    .addOnFailureListener(e -> Log.v("Result", "Them order detail that bai", e));
            FireOrderToSeller(id_order, product);
    }

    public void FireOrderToSeller(String id_order, Product product){
        String user_name = "";
        if (user != null){
            user_name = user.getFirstName() + user.getLastName();
        }

        SellerOrder sellerOrder = new SellerOrder(id_order, product.getName(), auth.getUid(), user_name, new Date(), new Date(), product.getQuantity(), "pending", product.getId_seller());
        db.collection("SellerOrder").document(product.getId_seller())
                .collection("Orders")
                .add(sellerOrder)
                .addOnSuccessListener(documentReference -> Log.v("Test", "Add order seller thanh cong"))
                .addOnFailureListener(e -> Log.v("Test", "Them order detail that bai", e));
        deleteProductsInCartOfUser(product);

        db.collection("UserInfo").document(product.getId_seller())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> registrationIds = new ArrayList<>();
                        DocumentSnapshot documentSnapshot = task.getResult();
                        String deviceToken = documentSnapshot.getString("deviceToken");
                        Log.v("Test", "Receiver device token: " + deviceToken);
                        registrationIds.add(deviceToken);

                        FCMNotification.Notification notification = FCMNotification.createNotification(
                                FCMNotification.getOrderTitle(),
                                FCMNotification.getOrderBody(auth.getUid())
                        );

                        FCMNotification.Data data = FCMNotification.createData(
                                FCMNotification.getOrderTitle(),
                                FCMNotification.getOrderBody(auth.getUid())
                        );

                        FCMNotification FcmNotification = new FCMNotification(notification, data, registrationIds);
                        new PushNotification(getContext()).execute(FcmNotification);
                    }
                });
    }

    public void deleteProductsInCartOfUser(Product product) {
        Log.v("ID_user" , auth.getUid());
        db.collection("Cart").document(auth.getUid()).collection("Products").document(product.getDocumentId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    activity.startActivity(new Intent(getContext(), MainActivity.class));
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting document", e));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:{
                orderPlace();
            }
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    public String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    public static class PushNotification extends AsyncTask<Object, Void, String> {
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
}