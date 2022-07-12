package com.example.ecomapplication.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.CategoryAdapter;
import com.example.ecomapplication.models.Category;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class ShowAllCategoryActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    GridView categoryView;
    CategoryAdapter categoryAdapter;

    List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        firestore = FirebaseFirestore.getInstance();
        categoryView = findViewById(R.id.categoryList);

        getCategoriesFromFireBase();
        categoryAdapter = new CategoryAdapter(this, categories);
        categoryView.setAdapter(categoryAdapter);
    }

    private void getCategoriesFromFireBase() {
        categories = new ArrayList<>();

        firestore.collection("Category").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Category category = document.toObject(Category.class);
                    categories.add(category);
                    categoryAdapter.notifyDataSetChanged();
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }
}