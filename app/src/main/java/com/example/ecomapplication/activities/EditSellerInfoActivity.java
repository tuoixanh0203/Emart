package com.example.ecomapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditSellerInfoActivity extends AppCompatActivity {

    TextInputLayout shopName, shopPhone, shopAddress, shopEmail;
    TextView saveProfile;
    FirebaseFirestore db;
    String shop_name, shop_phone, shop_address, shop_email;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_seller_info);
        auth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        shopName = findViewById(R.id.shop_name_update);
        shopPhone = findViewById(R.id.shop_phone_update);
        shopAddress = findViewById(R.id.shop_address_update);
        shopEmail = findViewById(R.id.shop_email_update);
        saveProfile = findViewById(R.id.update_profile);

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SellerActivity.class);
                String nameShopCheck = shopName.getEditText().getText().toString();
                String cityShopCheck = shopAddress.getEditText().getText().toString();
                String emailShopCheck = shopEmail.getEditText().getText().toString();
                String phoneShopCheck = shopPhone.getEditText().getText().toString();
                boolean check = validateSellerInfo(nameShopCheck, cityShopCheck, emailShopCheck, phoneShopCheck);
                if(check == true){
                    updateSellerInfo();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        shop_name = getIntent().getExtras().getString("Shop Name");
        shop_phone = getIntent().getExtras().getString("Shop Phone");
        shop_address = getIntent().getExtras().getString("Shop Address");
        shop_email = getIntent().getExtras().getString("Shop Email");

        // Get data from user activity
        shopName.getEditText().setText(shop_name);
        shopPhone.getEditText().setText(shop_phone);
        shopAddress.getEditText().setText(shop_address);
        shopEmail.getEditText().setText(shop_email);
    }

    public Boolean validateSellerInfo(String _shopName, String _shopAddress, String _shopEmail, String _shopPhone){
        if(_shopName.length() == 0){
            shopName.requestFocus();
            shopName.setError("Không được để trống!");
            return false;
        } else if (!_shopName.matches("\\D+")){
            shopName.requestFocus();
            shopName.setError("Tên shop không hợp lệ");
            return false;
        }
        else if (_shopAddress.length() == 0){
            shopAddress.requestFocus();
            shopAddress.setError("Không được để trống!");
            return false;
        }
        else if (_shopEmail.length() == 0){
            shopEmail.requestFocus();
            shopEmail.setError("Không được để trống!");
            return false;
        } else if (!_shopEmail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            shopEmail.requestFocus();
            shopEmail.setError("Email không hợp lệ!");
            return false;
        }else if (_shopPhone.length() == 0){
            shopPhone.requestFocus();
            shopPhone.setError("Không được để trống!");
            return false;
        } else if (!_shopPhone.matches("^[+]?[0-9]{10,11}$")){
            shopPhone.requestFocus();
            shopPhone.setError("Số điện thoại không hợp lệ!");
            return false;
        }
        else{
            return true;
        }
    }

    public void updateSellerInfo(){
        if (!isShopNameChanged() && !isshopPhoneChanged() && !isshopAddressChanged() &&
                !isshopEmailChanged()){
            Toast.makeText(this, "Nothing Changed", Toast.LENGTH_SHORT).show();
        }
        else {
            db.collection("SellerInfo").document(auth.getUid())
                    .update("address", shopAddress.getEditText().getText().toString(),
                            "email", shopEmail.getEditText().getText().toString(),
                            "phone", shopPhone.getEditText().getText().toString(),
                            "shopName", shopName.getEditText().getText().toString());
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isShopNameChanged(){
        if (!shop_name.equals(shopName.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isshopPhoneChanged(){
        if (!shop_phone.equals(shopPhone.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isshopAddressChanged(){
        if (!shop_address.equals(shopAddress.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isshopEmailChanged(){
        if (!shop_email.equals(shopEmail.getEditText().getText().toString())){
            return true;
        }
        return false;
    }
}