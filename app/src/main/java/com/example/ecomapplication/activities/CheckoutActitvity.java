package com.example.ecomapplication.activities;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ecomapplication.Helper.BackAction;
import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;

import com.example.ecomapplication.adapters.AddressAdapter;
import com.example.ecomapplication.adapters.CheckoutAdapter;
import com.example.ecomapplication.models.MyCartModel;
import com.example.ecomapplication.models.Product;
import com.example.ecomapplication.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckoutActitvity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    TextView totalAmount;
    List<String> listAddress;
    AddressAdapter addressAdapter;
    RecyclerView addressListView;
    private Button new_address_button;
    private Button click_to_payment;
        List<Product> cartModelList;
    CheckoutAdapter cartAdapter;
    RecyclerView productsCheckoutRecyclerView;
    FirebaseFirestore db;
    private FirebaseAuth auth;
    Spinner listAddressSpinner;
    TextView totalCheckout ;
    private String choosedAddress ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_actitvity);
        db = FirebaseFirestore.getInstance();
        new_address_button = findViewById(R.id.new_address_button);
        click_to_payment = findViewById(R.id.click_to_payment);
        productsCheckoutRecyclerView = findViewById(R.id.orderList);
        listAddressSpinner = findViewById(R.id.list_drop_address);
        totalAmount = findViewById(R.id.totalLabel2);
        auth = FirebaseAuth.getInstance();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));

        choosedAddress = "";
        listAddress = new ArrayList<>();
        Intent getIntent = getIntent();
        totalAmount.setText(String.valueOf(getIntent.getIntExtra("totalOrder" , 0)));
        getListAddress();

        //spinner
        Log.v("thanhcong ____" , String.valueOf(listAddress.size()));
        String[] arrayAddress = new String[listAddress.size()];
        for (int i = 0; i < listAddress.size(); i++) {
            arrayAddress[i] = listAddress.get(i);
        }
        Spinner s = (Spinner) findViewById(R.id.list_drop_address);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayAddress   );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("ChoosedString" , String.valueOf(adapterView.getItemAtPosition(i)));
                choosedAddress = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        click_to_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle addressBunder= new Bundle();
                if(choosedAddress == ""){
                    choosedAddress = "123 Nguyễn Trai, Thanh Xuan, Hà Nội";
                }
                addressBunder.putString("orderAddress", choosedAddress);
                Intent intent = new Intent(CheckoutActitvity.this, PaymentActivity.class);
                intent.putExtra("orderAddress", choosedAddress);
                Log.v("addressđsfdf" ,choosedAddress);
                Log.v("addressđsfdf" ,String.valueOf(getIntent.getIntExtra("totalOrder" , 120000)));

                intent.putExtra("total", String.valueOf(getIntent.getIntExtra("totalOrder" , 120000)));
                startActivity(intent);
            }
        });
        new_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
//                Intent intent = new Intent(view.getContext(), TestMapDis.class);
//                startActivity(intent);
            }
        });


        //get product
        getProductCheckout();
        productsCheckoutRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cartAdapter = new CheckoutAdapter(this, cartModelList);
        productsCheckoutRecyclerView.setAdapter(cartAdapter);

    }
    public void back(){
        super.onBackPressed();
    }
    @Override
    public void onBackPressed()
    {
        BackAction appdialog = new BackAction();
        appdialog.Confirm(this, "Xác nhận hủy bỏ", "Bạn có hủy thanh toán",
                "Không", "Có", aproc(), bproc());

    }
    public Runnable aproc(){
        return new Runnable() {
            public void run() {
                startActivity(new Intent(CheckoutActitvity.this, MainActivity.class));
            }
        };
    }

    public Runnable bproc(){
        return new Runnable() {
            public void run() {
                startActivity(new Intent(CheckoutActitvity.this, MainActivity.class));
            }
        };
    }
    public void getProductCheckout () {
        cartModelList = new ArrayList<>();
        db.collection("Cart").document(auth.getUid())
//        firestore.collection("Cart").document(auth.getCurrentUser().getUid())
                .collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc :task.getResult().getDocuments()) {
//                        Log.v("Test", auth.getCurrentUser().getUid());
                        Product product = doc.toObject(Product.class);
                        product.setDocumentId(doc.getId());
                        if(!product.getName().equals("init")){
                            cartModelList.add(product);
                            cartAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }



    public void showDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Thêm địa chỉ nhận hàng");
        alert.setMessage("Nhập địa chỉ nhận hàng mới");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                Log.v("address_fire", value);
                db.collection("UserInfo").document(auth.getUid()).update(
                        "address", FieldValue.arrayUnion(value));
                getListAddress();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
    public void getListAddress(){
        ArrayList<String> list = new ArrayList<>();
        db.collection("UserInfo")
                .document(auth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.v("id_document", document.getId());
                                UserInfo user = document.toObject(UserInfo.class);
                                for(int i = 0; i < user.getAddress().size() ; i++){
                                    list.add(user.getAddress().get(i));
                                }
                                renderListAddress(list);
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }

                    }
                });
        Log.v("thanhcong__" , String.valueOf(listAddress.size()));

    }
    public void renderListAddress(ArrayList<String> listAddress){
        String[] arrayAddress = new String[listAddress.size()];
        for (int i = 0; i < listAddress.size(); i++) {
            arrayAddress[i] = listAddress.get(i);
        }
        Spinner s = (Spinner) findViewById(R.id.list_drop_address);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayAddress   );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("ChoosedString" , String.valueOf(adapterView.getItemAtPosition(i)));
                choosedAddress = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int totalBill = intent.getIntExtra("totalAmount", 0);
            totalAmount.setText(totalBill + "vnd");
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}