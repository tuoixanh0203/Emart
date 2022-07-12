package com.example.ecomapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.CategoryAdapter;
import com.example.ecomapplication.adapters.PopularProductAdapter;
import com.example.ecomapplication.adapters.ShowAllAdapter;
import com.example.ecomapplication.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ShowProductCategory extends AppCompatActivity {

    RecyclerView recyclerView;
    PopularProductAdapter popularProductAdapter;
    CategoryAdapter categoryAdapter;
    List<Product> list;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_cate);
        String type = getIntent().getStringExtra("type");
        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.show_all_cate);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        list = new ArrayList<>();
        popularProductAdapter = new PopularProductAdapter(this, list);
        recyclerView.setAdapter(popularProductAdapter);


        if(type == null && type.isEmpty()){
            firestore.collection("Product")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (DocumentSnapshot doc :task.getResult().getDocuments()){
                                    // show all
                                    Product product = doc.toObject(Product.class);
                                    product.setDocumentId(doc.getId());
                                    list.add(product);
                                    popularProductAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }else if(type != null){
            Log.v("typeddd" , type);

            firestore.collection("Product").whereEqualTo("id_category", type)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (DocumentSnapshot doc :task.getResult().getDocuments()){
                                    Product product = doc.toObject(Product.class);
                                    product.setDocumentId(doc.getId());
                                    list.add(product);
                                    popularProductAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }
//        if(type != null && type.equalsIgnoreCase("1")){
//            firestore.collection("Product").whereEqualTo("id_category", "1")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()){
//                                for (DocumentSnapshot doc :task.getResult().getDocuments()){
//                                    Product product = doc.toObject(Product.class);
//                                    list.add(product);
//                                    popularProductAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        }
//                    });
//        }
//
//        if(type != null && type.equalsIgnoreCase("2")){
//            firestore.collection("Product").whereEqualTo("id_category", "2")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()){
//                                for (DocumentSnapshot doc :task.getResult().getDocuments()){
//                                    Product product = doc.toObject(Product.class);
//                                    list.add(product);
//                                    popularProductAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        }
//                    });
//        }
//
//        if(type != null && type.equalsIgnoreCase("3")){
//            firestore.collection("Product").whereEqualTo("id_category", "3")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()){
//                                for (DocumentSnapshot doc :task.getResult().getDocuments()){
//                                    Product product = doc.toObject(Product.class);
//                                    list.add(product);
//                                    popularProductAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        }
//                    });
//        }
//
//        if(type != null && type.equalsIgnoreCase("4")){
//            firestore.collection("Product").whereEqualTo("id_category", "4")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()){
//                                for (DocumentSnapshot doc :task.getResult().getDocuments()){
//                                    Product product = doc.toObject(Product.class);
//                                    list.add(product);
//                                    popularProductAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        }
//                    });
//        }
//
//        if(type != null && type.equalsIgnoreCase("5")){
//            firestore.collection("Product").whereEqualTo("id_category", "5")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()){
//                                for (DocumentSnapshot doc :task.getResult().getDocuments()){
//                                    Product product = doc.toObject(Product.class);
//                                    list.add(product);
//                                    popularProductAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        }
//                    });
//        }
//
//        if(type != null && type.equalsIgnoreCase("6")){
//            firestore.collection("Product").whereEqualTo("id_category", "6")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()){
//                                for (DocumentSnapshot doc :task.getResult().getDocuments()){
//                                    Product product = doc.toObject(Product.class);
//                                    list.add(product);
//                                    popularProductAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        }
//                    });
//        }
//
//        if(type != null && type.equalsIgnoreCase("7")){
//            firestore.collection("Product").whereEqualTo("id_category", "7")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()){
//                                for (DocumentSnapshot doc :task.getResult().getDocuments()){
//                                    Product product = doc.toObject(Product.class);
//                                    list.add(product);
//                                    popularProductAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        }
//                    });
//        }
//
//        if(type != null && type.equalsIgnoreCase("8")){
//            firestore.collection("Product").whereEqualTo("id_category", "8")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()){
//                                for (DocumentSnapshot doc :task.getResult().getDocuments()){
//                                    Product product = doc.toObject(Product.class);
//                                    list.add(product);
//                                    popularProductAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        }
//                    });
//        }
//
//        if(type != null && type.equalsIgnoreCase("9")){
//            firestore.collection("Product").whereEqualTo("id_category", "9")
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if(task.isSuccessful()){
//                                for (DocumentSnapshot doc :task.getResult().getDocuments()){
//                                    Product product = doc.toObject(Product.class);
//                                    list.add(product);
//                                    popularProductAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        }
//                    });
//        }

    }
}