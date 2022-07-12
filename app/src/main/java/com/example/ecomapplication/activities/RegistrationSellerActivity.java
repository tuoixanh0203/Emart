package com.example.ecomapplication.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class RegistrationSellerActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button signUpButton;
    private EditText nameSignUp, emailSignUp, phoneSignUp, addressSignUp;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_seller);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        signUpButton = findViewById(R.id.signUpButton);
        nameSignUp = findViewById(R.id.nameSignUp);
        emailSignUp = findViewById(R.id.emailSignUp);
        phoneSignUp = findViewById(R.id.phoneSignUp);
        addressSignUp = findViewById(R.id.addressSignUp);
        getUserInfoFromFireBase();
        signUpButton.setOnClickListener(view -> {
            Log.v("TAGGGGG" , auth.getUid());
            if (TextUtils.isEmpty(nameSignUp.getText())) {
                Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(emailSignUp.getText())) {
                Toast.makeText(this, "Please enter your email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(phoneSignUp.getText())) {
                Toast.makeText(this, "Please enter your phone!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(addressSignUp.getText())) {
                Toast.makeText(this, "Please enter your address!", Toast.LENGTH_SHORT).show();
                return;
            }

            addSellerInfo();
////            auth.createUserWithEmailAndPassword(emailSignUp.getText().toString(), passwordSignUp.getText().toString())
////                    .addOnCompleteListener( getActivity(), task -> {
////                        if (task.isSuccessful()) {
////                            Toast.makeText(getContext(), "Sign Up Successfully", Toast.LENGTH_SHORT).show();
////                            addUserInfo();
////                        } else {
////                            Toast.makeText(getContext(), "Sign Up Failed " + task.getException(), Toast.LENGTH_SHORT).show();
////                        }
////                    });
        });
    }
    public void getUserInfoFromFireBase(){
        db.collection("UserInfo").document(auth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            UserInfo userInfo = task.getResult().toObject(UserInfo.class);
                            Log.v("Stranger", userInfo.getFirstName());
                            emailSignUp.setText(userInfo.getEmail());
                            phoneSignUp.setText(userInfo.getPhone());
                        }
                        catch (Exception e ) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
    public void addSellerInfo(){
        Map<String, Object> doc = new HashMap<>();
        doc.put("address", addressSignUp.getText().toString());
        doc.put("email", emailSignUp.getText().toString());
        doc.put("id", auth.getUid());
        doc.put("phone", phoneSignUp.getText().toString());
        doc.put("shopName", nameSignUp.getText().toString());

        db.collection("UserInfo").document(auth.getUid())
                .update("is_seller", true);

        db.collection("SellerInfo").document(auth.getUid()).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(RegistrationSellerActivity.this, SellerActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}