package com.example.ecomapplication.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.PopularProductAdapter;
import com.example.ecomapplication.models.Product;
import com.example.ecomapplication.models.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class ShowAllProductsActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    PopularProductAdapter popularProductAdapter;
    private RecyclerView productAllView ;
    List<Product> productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        firestore = FirebaseFirestore.getInstance();
        productAllView = findViewById(R.id.productList);

        getProductFromFireBase();
        popularProductAdapter = new PopularProductAdapter(this, productList);
        productAllView.setLayoutManager(new GridLayoutManager(this,2));
        productAllView.setAdapter(popularProductAdapter);
    }

    private void getProductFromFireBase() {
        productList = new ArrayList<>();

        firestore.collection("Product").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Product product = document.toObject(Product.class);
                    product.setDocumentId(document.getId());
                    productList.add(product);
                    popularProductAdapter.notifyDataSetChanged();
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }
}