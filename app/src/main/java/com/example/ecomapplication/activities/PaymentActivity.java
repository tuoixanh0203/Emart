package com.example.ecomapplication.activities;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomapplication.Helper.BackAction;
import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.CheckoutAdapter;
import com.example.ecomapplication.adapters.PaymentAdapter;
import com.example.ecomapplication.models.MyCartModel;
import com.example.ecomapplication.models.OrderPlace;
import com.example.ecomapplication.models.Payment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {
    private RecyclerView recyclerListPayment;
    List<Payment> paymentList;
    PaymentAdapter paymentAdapter;
    FirebaseFirestore db;
    private FirebaseAuth auth;
    List<MyCartModel> cartModelList;
    CheckoutAdapter cartAdapter;

    Button makePayment,cancelOrder, zaloPay, momo, cod, paypal;
    TextView newPaypalText, newGooglePay, newMasterCard ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_test);
        auth = FirebaseAuth.getInstance();
//        Bundle orderAddressBunder = getIntent().getExtras();
//        String orderAddress = orderAddressBunder.getString("orderAddress");
//        Log.v("TAG---", orderAddress);

        Intent intent = getIntent();
        String orderAddress = intent.getExtras().getString("orderAddress");
        Log.v("address" , orderAddress);
        String total = intent.getExtras().getString("total") != null ? intent.getStringExtra("total") : "120500";
        Log.v("address" , total);




        paymentList = new ArrayList<>();
        mapping();
        recyclerListPayment = findViewById(R.id.list_payment_rec);
        recyclerListPayment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        paymentAdapter = new PaymentAdapter(this, paymentList);
        recyclerListPayment.setAdapter(paymentAdapter);
        db = FirebaseFirestore.getInstance();
        getPaymentFireBase();

        makePayment.setOnClickListener(view -> {
            Payment selectedPayment = paymentList.get(paymentAdapter.getSelected_position());
            OrderPlace cdd = new OrderPlace(PaymentActivity.this, orderAddress, Integer.valueOf(total), auth.getUid(),new Date() , new Date(), selectedPayment);
            cdd.show();
        });

        zaloPay.setOnClickListener(view -> {
            Log.v("processs" , "fsfdsfs");
            Intent intent12 = new Intent(PaymentActivity.this, ZaloPayActivity.class);
            startActivity(intent12);
        });
        paypal.setOnClickListener(view -> {
            Intent intent12 = new Intent(PaymentActivity.this, Paypal.class);
            Payment selectedPayment = paymentList.get(paymentAdapter.getSelected_position());
//            OrderWithPaypal cdd = new OrderWithPaypal(PaymentActivity.this, orderAddress, Integer.valueOf(total), auth.getUid(),new Date() , new Date(), selectedPayment);
            intent12.putExtra("amount" , total);
            intent12.putExtra("orderAddress" , orderAddress);
            intent12.putExtra("auth" , auth.getUid());
            startActivity(intent12);
        });
        momo.setOnClickListener(view -> {
            Log.v("processs" , "fsfdsfs");

            Intent intent1 = new Intent(PaymentActivity.this, MomoActivity.class);
            startActivity(intent1);
        });

        cancelOrder.setOnClickListener(view -> {
            Intent intent13 = new Intent(PaymentActivity.this, MainActivity.class);
            startActivity(intent13);
        });
        newMasterCard.setOnClickListener(view -> newPaymentMethod("Master Card"));
//        newGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                newPaymentMethod("Google Pay");
//            }
//        });
    }
    public void back(){
        super.onBackPressed();
    }

    @Override
    public void onBackPressed()
    {
        BackAction appdialog = new BackAction();
        appdialog.Confirm(this, "Xác nhận hủy bỏ", "Bạn có muốn hủy thanh toán không ?",
                "Không", "Có", aproc(), bproc());

    }
    public Runnable aproc(){
        return () -> startActivity(new Intent(PaymentActivity.this, MainActivity.class));
    }

    public Runnable bproc(){
        return () -> startActivity(new Intent(PaymentActivity.this, MainActivity.class));

    }
    public void newPaymentMethod(String payment_type){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //you should edit this to fit your needs
        builder.setTitle("Nhập thông tin thẻ");

        final EditText one = new EditText(this);
//        one.setHint("Account Number");//optional
        one.setHint("Số thẻ(Card number");
        final EditText two = new EditText(this);
//        two.setHint("Expiry Date");//optional
        two.setHint("Ngày hết hạn(Expiry Date)");
        final EditText three = new EditText(this);
//        three.setHint("Name");//optional
        three.setHint("CVV/CVC2(mã số bảo mật)");
        //in my example i use TYPE_CLASS_NUMBER for input only numbers
        one.setInputType(InputType.TYPE_CLASS_TEXT);
        two.setInputType(InputType.TYPE_CLASS_DATETIME);
        three.setInputType(InputType.TYPE_CLASS_TEXT);

        LinearLayout lay = new LinearLayout(this);
        lay.setOrientation(LinearLayout.VERTICAL);
        lay.addView(one);
        lay.addView(two);
        lay.addView(three);

        builder.setView(lay);

        // Set up the buttons
        builder.setPositiveButton("Ok", (dialog, whichButton) -> {
            //get the two inputs
            String number = one.getText().toString();
            Log.v("payment", String.valueOf(two.getText()));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date d  = Calendar.getInstance().getTime();
            try {
                 d = dateFormat.parse(String.valueOf(two.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String provider = three.getText().toString();
            addDataToFireBase(number, d, provider, payment_type);

        });

        builder.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.cancel());
        builder.show();
    }

    public void mapping(){
        newPaypalText = findViewById(R.id.paypal);
        makePayment = findViewById(R.id.makePayment);
        cancelOrder = findViewById(R.id.cancel_order);
        paypal = findViewById(R.id.paypalpay);
        zaloPay = findViewById(R.id.zalopay_payment);
        momo = findViewById(R.id.momo_payment);
        cod = findViewById(R.id.cos_payment);
        newGooglePay = findViewById(R.id.googlepay);
        newMasterCard = findViewById(R.id.master_card);
    }

    public void addDataToFireBase (String number, Date date, String provider, String payment_type){
        // Create a new user with a first and last name
        Map<String, Object> payment = new HashMap<>();
        payment.put("account_nb", number);
        payment.put("expired", date);
        payment.put("id", "1815");
        payment.put("id_user", auth.getUid());
        payment.put("payment_type", payment_type);
        payment.put("provider", provider);


// Add a new document with a generated ID
        db.collection("Payment")
                .add(payment)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
        getPaymentFireBase();
    }

    public void getPaymentFireBase(){
        paymentList.removeAll(paymentList);
        db.collection("Payment")
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        try{
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Payment payment = document.toObject(Payment.class);
                                if(payment.getId_user().trim().equals(auth.getUid())){
                                    Log.v("user_id_if" , payment.getId_user());
                                    paymentList.add(payment);
                                }
                                paymentAdapter.notifyDataSetChanged();
                            }
                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

}