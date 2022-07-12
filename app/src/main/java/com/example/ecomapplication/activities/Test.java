package com.example.ecomapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.CategoryAdapterSpinner;
import com.example.ecomapplication.models.Category;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Test extends AppCompatActivity {
    Spinner spnCategory;
    CategoryAdapterSpinner categoryAdapterSpinner;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        spnCategory = findViewById(R.id.spn_category);
        categoryAdapterSpinner = new CategoryAdapterSpinner(this, R.layout.item_category_selected_spinner, getListCategory());
        spnCategory.setAdapter(categoryAdapterSpinner);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(Test.this, categoryAdapterSpinner.getItem(i).getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private List<Category> getListCategory() {
        List<Category> list = new ArrayList<>();
        list.add(new Category("gs://ecommerce-de4aa.appspot.com/macbook.jpg", "Laptop", "Do dien tu", "123"));
        list.add(new Category("gs://ecommerce-de4aa.appspot.com/macbook.jpg", "dien thoai Oppo", "Do dien tu", "123"));
        list.add(new Category("gs://ecommerce-de4aa.appspot.com/macbook.jpg", "Tai nghe", "Do dien tu", "123"));
        return list;
    }
}
