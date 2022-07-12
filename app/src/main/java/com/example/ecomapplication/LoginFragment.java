package com.example.ecomapplication;

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

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ecomapplication.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginFragment extends Fragment {
    private FirebaseAuth auth;
    private Button signInButton;
    private TextView linkToSignUp;
    private EditText emailSignIn,passwordSignIn;
    private FirebaseFirestore firestore;

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();

        return fragment;
    }

    public void updateAccountToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.v("Test", "Fetching FCM registration token failed", task.getException());
                return;
            }

            String deviceToken = task.getResult();
            Log.v("Test", "Sign in device token: " + deviceToken);

            firestore.collection("UserInfo").document(auth.getUid())
                    .update("deviceToken", deviceToken);
            Log.v("Test", "Update device token successfully");
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_login, container, false);

        auth = FirebaseAuth.getInstance();


        signInButton = root.findViewById(R.id.signInButton);
        linkToSignUp = root.findViewById(R.id.linkToSignUp);
        emailSignIn = root.findViewById(R.id.emailSignIn);
        passwordSignIn = root.findViewById(R.id.passwordSignIn);

        signInButton.setOnClickListener(view -> {
            if (TextUtils.isEmpty(emailSignIn.getText())) {
                Toast.makeText(getContext(), "Please enter your email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(passwordSignIn.getText())) {
                Toast.makeText(getContext(), "Please enter your password!", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(emailSignIn.getText().toString(), passwordSignIn.getText().toString())
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            updateAccountToken();

                            Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getContext(), "Email hoặc mật khẩu không chính xác ", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        linkToSignUp.setOnClickListener(view -> {
            NavHostFragment.findNavController(LoginFragment.this)
                    .navigate(R.id.action_nav_login_to_nav_signup);
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setActionBarTitle("Sign In");
        }
    }
}