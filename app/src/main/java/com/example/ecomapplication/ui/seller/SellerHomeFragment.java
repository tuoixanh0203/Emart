//package com.example.ecomapplication.ui.seller;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.example.ecomapplication.R;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link SellerHomeFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class SellerHomeFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    ImageButton logOut, editProfile, addProduct;
//    TextView userName, shopName, email, productTab, orderTab;
//    RelativeLayout productRl, orderRl;
//
//    public SellerHomeFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment SellerHomeFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static SellerHomeFragment newInstance(String param1, String param2) {
//        SellerHomeFragment fragment = new SellerHomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//        showProductUi();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.fragment_seller_home, container, false);
//
//        logOut = root.findViewById(R.id.logout_btn);
//        editProfile = root.findViewById(R.id.edit_profile_btn);
//        addProduct = root.findViewById(R.id.add_product_btn);
//        userName = root.findViewById(R.id.username_seller);
//        shopName = root.findViewById(R.id.shop_name);
//        email = root.findViewById(R.id.shop_email);
//        productTab = root.findViewById(R.id.product_tab);
//        orderTab = root.findViewById(R.id.order_tab);
//        productRl = root.findViewById(R.id.productRl);
//        orderRl = root.findViewById(R.id.orderRl);
//
//        productRl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showProductUi();
//            }
//        });
//
//        orderRl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showOrderUi();
//            }
//        });
//
//        // Inflate the layout for this fragment
//        return root;
//    }
//
//    public void showProductUi(){
//        //show products and hide orders
//        productRl.setVisibility(View.VISIBLE);
//        orderRl.setVisibility(View.GONE);
//
//        productTab.setTextColor(getResources().getColor(R.color.black));
//        productTab.setBackgroundResource(R.drawable.shape_rect02);
//
//        orderTab.setTextColor(getResources().getColor(R.color.white));
//        orderTab.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//    }
//
//    public void showOrderUi(){
//        //show orders and hide products
//        orderRl.setVisibility(View.VISIBLE);
//        productRl.setVisibility(View.GONE);
//
//        productTab.setTextColor(getResources().getColor(R.color.white));
//        productTab.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//
//        orderTab.setTextColor(getResources().getColor(R.color.black));
//        orderTab.setBackgroundResource(R.drawable.shape_rect02);
//    }
//}