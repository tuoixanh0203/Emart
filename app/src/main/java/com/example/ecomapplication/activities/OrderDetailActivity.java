package com.example.ecomapplication.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.ProductAdapter;
import com.example.ecomapplication.adapters.ProductOrderAdapter;
import com.example.ecomapplication.models.OrderModel;
import com.example.ecomapplication.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class OrderDetailActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    GridView productView;
    ProductOrderAdapter productAdapter;
    TextView total;
    Button feedback;
    List<Product> productList;
    String id;
    int totalPr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        feedback = findViewById(R.id.button_feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Intent intent = new Intent(OrderDetailActivity.this, DetailActivity.class);
                        intent.putExtra("id_product",  productList.get(0).getDocumentId());
                        startActivity(intent);
                    }
                });
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        OrderModel orderModel = (OrderModel) bundle.get("object_order");
        id = orderModel.getId();
        firestore = FirebaseFirestore.getInstance();
        productView = findViewById(R.id.productList);
        total = findViewById(R.id.all_total_price_order);

        productList = new ArrayList<>();
        firestore.collection("OrderDetail").document(id)
                .collection("Products").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Product product = document.toObject(Product.class);
                    if(!product.getName().equals("init")){
                        productList.add(product);
                        productAdapter.notifyDataSetChanged();
                        totalPr += product.getQuantity() * product.getPrice();
                        Log.v("Testtt", String.valueOf(totalPr));
                        total.setText(totalPr + " VNĐ");
                    }

                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
        Log.v("Testtt", String.valueOf(totalPr));
        productAdapter = new ProductOrderAdapter(this, productList);
        productView.setAdapter(productAdapter);
    }
}
