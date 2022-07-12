package com.example.ecomapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding loginBinding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        auth = FirebaseAuth.getInstance();

//        if (auth.getCurrentUser() != null) {
//            startActivity(new Intent(this, MainActivity.class));
//        }


        loginBinding.signInButton.setOnClickListener(view -> {
            String emailSignIn = loginBinding.emailSignIn.getText().toString();
            String passwordSignIn = loginBinding.passwordSignIn.getText().toString();

            if (TextUtils.isEmpty(emailSignIn)) {
                Toast.makeText(this, "Please enter your email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(passwordSignIn)) {
                Toast.makeText(this, "Please enter your password!", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(emailSignIn, passwordSignIn)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Sign In Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Email or Password Wrong " + task.getException() + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        loginBinding.linkToSignUp.setOnClickListener(view -> startActivity(new Intent(this, RegistrationActivity.class)));
    }
}