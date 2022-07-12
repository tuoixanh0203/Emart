package com.example.ecomapplication.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.models.UserInfo;
import com.example.ecomapplication.ui.profile.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EditProfileActivity extends AppCompatActivity {

    TextInputLayout firstName, lastName, userName, cityEt, emailEt, phoneEt;
    TextView saveProfile;

    String _firstName, _lastName, _userName, _city, _email, _phone;

    DatabaseReference reference;
    FirebaseFirestore db;
    UserInfo userInfo;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        firstName = findViewById(R.id.first_name_update);
        cityEt = findViewById(R.id.city_update);
        emailEt = findViewById(R.id.email_update);
        phoneEt = findViewById(R.id.phone_update);

        saveProfile = findViewById(R.id.update_profile);

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                String nameCheck = firstName.getEditText().getText().toString();
                String cityCheck = cityEt.getEditText().getText().toString();
                String emailCheck = emailEt.getEditText().getText().toString();
                String phoneCheck = phoneEt.getEditText().getText().toString();
                boolean check = validateUserInfo(nameCheck, cityCheck, emailCheck, phoneCheck);
                if(check == true){
                    updateProfile();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        _firstName = getIntent().getExtras().getString("First Name");
        _city = getIntent().getExtras().getString("City");
        _phone = getIntent().getExtras().getString("Phone");
        _email = getIntent().getExtras().getString("Email");

        // Get data from user activity
        firstName.getEditText().setText(_firstName);
        cityEt.getEditText().setText(_city);
        emailEt.getEditText().setText(_email);
        phoneEt.getEditText().setText(_phone);
    }

    public Boolean validateUserInfo(String name, String city, String email, String phone){
        if(name.length() == 0){
            firstName.requestFocus();
            firstName.setError("Không được để trống!");
            return false;
        }
        else if (!name.matches("\\D+")){
            firstName.requestFocus();
            firstName.setError("Họ tên không hợp lệ");
            return false;
        }
        else if (city.length() == 0){
            cityEt.requestFocus();
            cityEt.setError("Không được để trống!");
            return false;
        } else if (!city.matches("^([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*$")){
            cityEt.requestFocus();
            cityEt.setError("Tên thành phố không hợp lệ!");
            return false;
        }
        else if (email.length() == 0){
            emailEt.requestFocus();
            emailEt.setError("Không được để trống!");
            return false;
        } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            emailEt.requestFocus();
            emailEt.setError("Email không hợp lệ!");
            return false;
        }else if (phone.length() == 0){
            phoneEt.requestFocus();
            phoneEt.setError("Không được để trống!");
            return false;
        }
        else if (!phone.matches("^[+]?[0-9]{10,11}$")){
            phoneEt.requestFocus();
            phoneEt.setError("Số điện thoại không hợp lệ!");
            return false;
        }
        else{
            return true;
        }
    }


    public void updateProfile(){
        if (!isFirstNameChanged() && !isCityChanged() && !isEmailChanged() && !isPhoneChanged()){
            Toast.makeText(this, "Nothing changed", Toast.LENGTH_SHORT).show();
        }
        else {
            db.collection("UserInfo").document(auth.getUid())
                    .update("firstName", firstName.getEditText().getText().toString(),
                            "city", cityEt.getEditText().getText().toString(),
                            "email", emailEt.getEditText().getText().toString(),
                            "phone", phoneEt.getEditText().getText().toString());
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isFirstNameChanged(){
        if (!_firstName.equals(firstName.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isCityChanged(){
        if (!_city.equals(cityEt.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isEmailChanged(){
        if (!_email.equals(emailEt.getEditText().getText().toString())){
            return true;
        }
        return false;
    }

    public boolean isPhoneChanged(){
        if (!_phone.equals(phoneEt.getEditText().getText().toString())){
            return true;
        }
        return false;
    }
}