package com.example.ecomapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistrationFragment extends Fragment {

    private FirebaseAuth auth;
    private Button signUpButton;
    private EditText nameSignUp, emailSignUp, passwordSignUp;
    private TextView linktoSignIn;
    FirebaseFirestore db;

    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_registration, container, false);
        auth = FirebaseAuth.getInstance();
        signUpButton = root.findViewById(R.id.signUpButton);
        nameSignUp = root.findViewById(R.id.nameSignUp);
        emailSignUp = root.findViewById(R.id.emailSignUp);
        passwordSignUp = root.findViewById(R.id.passwordSignUp);
        linktoSignIn = root.findViewById(R.id.linkToSignIn);

        signUpButton.setOnClickListener(view -> {
            Log.v("TAGGGG" , "FDFDS");
            if (TextUtils.isEmpty(nameSignUp.getText())) {
                Toast.makeText(getContext(), "Please enter your name!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(emailSignUp.getText())) {
                Toast.makeText(getContext(), "Please enter your email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(passwordSignUp.getText())) {
                Toast.makeText(getContext(), "Please enter your password!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (passwordSignUp.length() < 8) {
                Toast.makeText(getContext(), "Password length must be greater than 8", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(emailSignUp.getText().toString(), passwordSignUp.getText().toString())
                    .addOnCompleteListener( getActivity(), task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(nameSignUp.getText().toString()).build();
                            user.updateProfile(profileChangeRequest);

                            Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(RegistrationFragment.this)
                                    .navigate(R.id.action_nav_signup_to_nav_login);
                            addUserInfo();
                        } else {
                            Toast.makeText(getContext(), "Đăng ký thất bại " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        linktoSignIn.setOnClickListener(view -> {
            NavHostFragment.findNavController(RegistrationFragment.this)
                    .navigate(R.id.action_nav_signup_to_nav_login);
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setActionBarTitle("Sign Up");
        }
    }

    public void addUserInfo(){
            Map<String, Object> doc = new HashMap<>();
            doc.put("city", "");
            doc.put("date", new Date());
            doc.put("address", Arrays.asList("120 Nguyễn Trãi, Thanh Xuân, Hà Nội"));
            doc.put("email", emailSignUp.getText().toString());
            doc.put("firstName", nameSignUp.getText().toString());
            doc.put("id", "");
            doc.put("lastName", "");
            doc.put("phone", "");
            doc.put("is_seller", false);
            db.collection("UserInfo").document(auth.getUid()).set(doc)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
//                            initCart();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
    }
    public void initCart(){
        Map<String, Object> prodt = new HashMap<>();

        prodt.put("description", "description");
        prodt.put("productId", null);
        prodt.put("id_category", "1");
        prodt.put("img_url", "gs://ecommerce-de4aa.appspot.com/chien binh cau vong.jpg");
        prodt.put("name", "init");
        prodt.put("price", 13000);
        prodt.put("quantity", 1);
        prodt.put("rating", "4");
        prodt.put("size", "GG");
        db.collection("Cart")
                .document(auth.getUid())
                .collection("Products")
                .add(prodt).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(getContext(), MainActivity.class));
                Log.w(TAG, "Thanh cong");

            } else {
                Log.w(TAG, "that bai");
            }
        });
    }
}