package com.example.ecomapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.models.Product;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextInputLayout product_name, product_desc, product_category, product_price, product_quantity, product_size, product_rating;
    String product_name_, product_desc_, product_category_, product_price_, product_quantity_, product_size_, product_rating_, document_;
    TextView update_product;
    String id_product;
    String cate_gory, text;
    FirebaseFirestore db;
    Spinner product_gate_gory;
    Product product;
    ImageView back;
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        db = FirebaseFirestore.getInstance();


        product_name = findViewById(R.id.product_name_update);
        product_desc = findViewById(R.id.product_desc_update);
        product_gate_gory = findViewById(R.id.product_category_update);
        back = findViewById(R.id.back_action);
        product_price = findViewById(R.id.price_update);
        product_quantity = findViewById(R.id.quantity_update);
        product_size = findViewById(R.id.size_update);
        product_rating = findViewById(R.id.rating_update);
        update_product = findViewById(R.id.update_product);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final Object obj = getIntent().getSerializableExtra("ProductToEdit");
        if (obj instanceof Product) {
            product = (Product) obj;
            Log.v("Hiiii", product.getName());

            product_name.getEditText().setText(product.getName());
            product_desc.getEditText().setText(product.getDescription());
         //   product_category.getEditText().setText(product.getId_category());
            product_price.getEditText().setText(String.valueOf(product.getPrice()));
            product_quantity.getEditText().setText(String.valueOf(product.getQuantity()));
            product_size.getEditText().setText(product.getSize());
            product_rating.getEditText().setText(product.getRating());

            product_name_ = product.getName();
            product_desc_ = product.getDescription();
            product_category_ = product.getId_category();
            product_price_ = String.valueOf( product.getPrice());
            product_quantity_ = String.valueOf(product.getQuantity());
            product_rating_ = product.getRating();
            product_size_ = product.getSize();
        }

        //product_gate_gory.setSelection(Integer.valueOf(product_category_) - 1);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product_gate_gory.setAdapter(adapter);

        product_gate_gory.setSelection(Integer.valueOf(product_category_) - 1);

        product_gate_gory.setOnItemSelectedListener(this);

        update_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SellerActivity.class);
                String nameProdCheck = product_name.getEditText().getText().toString();
                String descProdCheck = product_desc.getEditText().getText().toString();
                String priceProdCheck = product_price.getEditText().getText().toString();
                String quantityProdCheck = product_quantity.getEditText().getText().toString();
                String ratingProdCheck = product_rating.getEditText().getText().toString();
                boolean check = validateProductInfo(nameProdCheck, descProdCheck, priceProdCheck, quantityProdCheck, ratingProdCheck);
                if(check == true){
                    updateProduct();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateProduct(){
        if (!isProductNameChanged() && !isProductDescChanged() && !isProductCateChanged() &&
                !isProductPriceChanged() && !isProductQuantityChanged() && !isProductSizeChanged() && !isProductRateChanged()){
            Log.v("Cat", product_name.getEditText().getText().toString());
            Toast.makeText(this, "Nothing changed", Toast.LENGTH_SHORT).show();

        }
        else {
            Log.v("Cat1", product_name.getEditText().getText().toString());
            Log.v("Cat2", cate_gory);
            db.collection("Product").document(product.getDocumentId())
                    .update("name", product_name.getEditText().getText().toString(),
                            "description", product_desc.getEditText().getText().toString(),
                            "id_category", cate_gory,
                            "price",  Integer.valueOf(product_price.getEditText().getText().toString()),
                            "quantity",  Integer.valueOf(product_quantity.getEditText().getText().toString()),
                            "rating", product_rating.getEditText().getText().toString(),
                            "size", product_size.getEditText().getText().toString());
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        }
    }

    public Boolean validateProductInfo(String _prodName, String _prodDesc, String _prodPrice, String _prodQuantity, String _prodRating){
        if(_prodName.length() == 0){
            product_name.requestFocus();
            product_name.setError("Không được để trống!");
            return false;
        } else if (!_prodName.matches("\\D+")){
            product_name.requestFocus();
            product_name.setError("Tên sản phẩm không hợp lệ");
            return false;
        }
        else if (_prodDesc.length() == 0){
            product_desc.requestFocus();
            product_desc.setError("Không được để trống!");
            return false;
        }
        else if (_prodPrice.length() == 0){
            product_price.requestFocus();
            product_price.setError("Không được để trống!");
            return false;
        }
        else if (!_prodPrice.matches("^[1-9]+[0-9]+")){
            product_price.requestFocus();
            product_price.setError("Giá sản phẩm không hợp lệ!");
            return false;
        }
        else if (_prodQuantity.length() == 0){
            product_quantity.requestFocus();
            product_quantity.setError("Không được để trống!");
            return false;
        } else if (!_prodQuantity.matches("^[1-9]+[0-9]+")){
            product_quantity.requestFocus();
            product_quantity.setError("Số lượng kho hàng không hợp lệ!");
            return false;
        }
        else if (_prodRating.length() == 0){
            product_rating.requestFocus();
            product_rating.setError("Không được để trống!");
            return false;
        } else if (Double.valueOf(_prodRating) > 5){
            product_rating.requestFocus();
            product_rating.setError("Dữ liệu đánh giá không hợp lệ!");
            return false;
        }
        else{
            return true;
        }
    }

    public boolean isProductNameChanged(){
        if (!product_name_.equals(product_name.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isProductDescChanged(){
        if (!product_desc_.equals(product_desc.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isProductCateChanged(){
        if (!product_category_.equals(cate_gory)){
            return true;
        }
        return false;
    }

    public boolean isProductPriceChanged(){
        if (!product_price_.equals(product_price.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isProductQuantityChanged(){
        if (!product_quantity_.equals(product_quantity.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isProductSizeChanged(){
        if (!product_size_.equals(product_size.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isProductRateChanged(){
        if (!product_rating_.equals(product_rating.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        text = adapterView.getItemAtPosition(i).toString();
        String he = String.valueOf(i);
        cate_gory = String.valueOf(i + 1);
        Log.v("tiengAnh", he);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}