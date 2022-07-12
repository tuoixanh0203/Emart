package com.example.ecomapplication.ui.profile;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.activities.EditProfileActivity;
import com.example.ecomapplication.models.Product;
import com.example.ecomapplication.models.SellerInfo;
import com.example.ecomapplication.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;

    TextView fullName, firstName, lastName, userName, city, password, email, phone, editProfile;
    FirebaseFirestore db;

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        fullName = root.findViewById(R.id.user_full_name);
        firstName =  root.findViewById(R.id.first_name);
        userName =  root.findViewById(R.id.username);
        city =  root.findViewById(R.id.city);
        email =  root.findViewById(R.id.email);
        phone =  root.findViewById(R.id.phone);

        editProfile =  root.findViewById(R.id.edit_profile);

        db = FirebaseFirestore.getInstance();
        getUserInfoFromFireBase();
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);

                String first_name_up = firstName.getText().toString();
                String city_up = city.getText().toString();
                String email_up = email.getText().toString();
                String phone_up = phone.getText().toString();

                intent.putExtra("First Name", first_name_up);
                intent.putExtra("City", city_up);
                intent.putExtra("Email", email_up);
                intent.putExtra("Phone", phone_up);

                // start the Intent
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setActionBarTitle("My Profile");
        }
    }

    public void getUserInfoFromFireBase(){
        db.collection("UserInfo").document(auth.getUid())
                .get()
                .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                try {
                    UserInfo userInfo = task.getResult().toObject(UserInfo.class);
                    Log.v("Stranger", userInfo.getFirstName());
                    fullName.setText(userInfo.getFirstName());
                    firstName.setText(userInfo.getFirstName());
                    city.setText(userInfo.getCity());
                    email.setText(userInfo.getEmail());
                    phone.setText(userInfo.getPhone());

                }
                catch (Exception e ) {
                    e.printStackTrace();
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }
}