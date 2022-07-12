package com.example.ecomapplication.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.databinding.ActivityRegistrationBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding registrationBinding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registrationBinding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(registrationBinding.getRoot());

        auth = FirebaseAuth.getInstance();

//        if (auth.getCurrentUser() != null) {
//            Log.v("fsfds", auth.getCurrentUser().toString());
//            startActivity(new Intent(this, MainActivity.class));
//        }

        Objects.requireNonNull(getSupportActionBar()).hide();

        registrationBinding.signUpButton.setOnClickListener(view -> {
            String nameSignUp = registrationBinding.nameSignUp.getText().toString();
            String emailSignUp = registrationBinding.emailSignUp.getText().toString();
            String passwordSignUp = registrationBinding.passwordSignUp.getText().toString();

            if (TextUtils.isEmpty(nameSignUp)) {
                Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(emailSignUp)) {
                Toast.makeText(this, "Please enter your email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(passwordSignUp)) {
                Toast.makeText(this, "Please enter your password!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (passwordSignUp.length() < 8) {
                Toast.makeText(this, "Password length must be greater than 8", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(emailSignUp, passwordSignUp)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Sign Up Failed " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        registrationBinding.linkToSignIn.setOnClickListener(view -> startActivity(new Intent(this, LoginActivity.class)));
    }
}