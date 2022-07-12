package com.example.ecomapplication.ui.order;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.OrderAdapter;
import com.example.ecomapplication.models.OrderModel;
import com.example.ecomapplication.ui.newproduct.NewProductFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
    public class OrderFragment extends Fragment {
        Toolbar toolbar;
        RecyclerView recyclerView;
        List<OrderModel> orderModels;
        OrderAdapter orderAdapter;
        private FirebaseAuth auth;
        private FirebaseFirestore firestore;


        public static OrderFragment newInstance(String param1, String param2) {
            OrderFragment fragment = new OrderFragment();
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
            View root =  inflater.inflate(R.layout.fragment_my_order, container, false);
            auth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();

            toolbar = root.findViewById(R.id.order_toolbar);

            recyclerView = root.findViewById(R.id.list_order);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            orderModels = new ArrayList<>();
            orderAdapter = new OrderAdapter(getContext(), orderModels);
            recyclerView.setAdapter(orderAdapter);
            Log.v("loggg" , auth.getUid());
            firestore.collection("Order").document(auth.getUid()).collection("Orders")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot doc :task.getResult().getDocuments()) {
                                    OrderModel orderModel = doc.toObject(OrderModel.class);
                                    orderModel.setId(doc.getId());
                                    Log.v("local", orderModel.getStatus());
                                    orderModels.add(orderModel);
                                    orderModel.setId(doc.getId());
                                    orderAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Log.w("Error", "Error getting documents.", task.getException());
                            }
                        }
                    });
            return root;
        }

        @Override
        public void onResume() {
            super.onResume();
            if (getActivity() != null) {
                ((MainActivity) getActivity()).setActionBarTitle("My Order");
            }
        }
    }