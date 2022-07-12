package com.example.ecomapplication.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomapplication.Helper.BackAction;
import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.OrderAdapterSeller;
import com.example.ecomapplication.adapters.ProductSellerAdapter;
import com.example.ecomapplication.models.Product;
import com.example.ecomapplication.models.SellerInfo;
import com.example.ecomapplication.models.SellerOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class SellerActivity extends AppCompatActivity {

    ImageButton logOut, editProfile, addProduct;
    TextView shopPhone, shopName, email, productTab, orderTab;
    RelativeLayout productRl, orderRl;
    FirebaseFirestore db;
    List<Product> myProduct;
    private RecyclerView  myProductRecyclerview ;
    private  ProductSellerAdapter myProductAdapter;
    List<SellerOrder> myOrder;
    private RecyclerView  myOrderRecyclerview ;
    private OrderAdapterSeller myOrderAdapterSeller;
    String shopAddress;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_seller_home);
        auth = FirebaseAuth.getInstance();

        logOut = findViewById(R.id.logout_btn);
        editProfile = findViewById(R.id.edit_profile_btn);
        addProduct = findViewById(R.id.add_product_btn);
        shopName = findViewById(R.id.username_seller);
        shopPhone = findViewById(R.id.shop_phone);
        email = findViewById(R.id.shop_email);
        productTab = findViewById(R.id.product_tab);
        orderTab = findViewById(R.id.order_tab);
        productRl = findViewById(R.id.productRl);
        myProductRecyclerview = findViewById(R.id.my_product_view);
        orderRl = findViewById(R.id.orderRl);
        myOrderRecyclerview = findViewById(R.id.my_order_view);
        db = FirebaseFirestore.getInstance();

        showProductUi();
        getSellerInfo();
        addProduct.setOnClickListener(view -> startActivity(new Intent(SellerActivity.this , NewProductActivity.class)));
        productTab.setOnClickListener(view -> showProductUi());

        orderTab.setOnClickListener(view -> showOrderUi());

        editProfile.setOnClickListener(view -> {
            Intent intent = new Intent(SellerActivity.this, EditSellerInfoActivity.class);

            String shop_name_up = shopName.getText().toString();
            String shop_phone_up = shopPhone.getText().toString();
            String shop_email_up = email.getText().toString();

            intent.putExtra("Shop Name", shop_name_up);
            intent.putExtra("Shop Phone", shop_phone_up);
            intent.putExtra("Shop Email", shop_email_up);
            intent.putExtra("Shop Address", shopAddress);

            // start the Intent
            startActivity(intent);
        });
    }

    public void getSellerInfo(){

        db.collection("SellerInfo").document(auth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            SellerInfo sellerInfo = task.getResult().toObject(SellerInfo.class);
                            String shop_name = sellerInfo.getShopName();
                            String shop_phone = sellerInfo.getPhone();
                            String shop_email = sellerInfo.getEmail();
                            shopAddress = sellerInfo.getAddress();
                            shopName.setText(shop_name);
                            shopPhone.setText(shop_phone);
                            email.setText(shop_email);
                        }
                        catch (Exception e ) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void showProductUi(){

        //show products and hide orders

        productRl.setVisibility(View.VISIBLE);
        orderRl.setVisibility(View.GONE);

        getMyProduct();
        myProductRecyclerview.setLayoutManager(new GridLayoutManager(this,2));
        myProductAdapter = new ProductSellerAdapter(this, myProduct);
        myProductRecyclerview.setAdapter(myProductAdapter);

        productTab.setTextColor(getResources().getColor(R.color.black));
        productTab.setBackgroundResource(R.drawable.shape_rect02);

        orderTab.setTextColor(getResources().getColor(R.color.white));
        orderTab.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    public void showOrderUi(){
        //show orders and hide products
        productRl.setVisibility(View.GONE);
        orderRl.setVisibility(View.VISIBLE);

        getMyOrder();
        myOrderRecyclerview.setLayoutManager(new GridLayoutManager(this,1));
        myOrderAdapterSeller = new OrderAdapterSeller(this, myOrder);
        myOrderRecyclerview.setAdapter(myOrderAdapterSeller);

        productTab.setTextColor(getResources().getColor(R.color.white));
        productTab.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        orderTab.setTextColor(getResources().getColor(R.color.black));
        orderTab.setBackgroundResource(R.drawable.shape_rect02);
    }

    public  void getMyProduct(){
        myProduct = new ArrayList<>();
        db.collection("Product").whereEqualTo("id_seller", auth.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc :task.getResult().getDocuments()) {
//                        Log.v("Test", auth.getCurrentUser().getUid());
                    Product product = doc.toObject(Product.class);
                    product.setDocumentId(doc.getId());
                    myProduct.add(product);
                    myProductAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    public void back(){
        super.onBackPressed();
    }
    @Override
    public void onBackPressed()
    {
        Log.v("dsfdsf", "fdfsd");
        startActivity(new Intent(SellerActivity.this, MainActivity.class));

    }

    public  void getMyOrder(){
        myOrder = new ArrayList<>();
        db.collection("SellerOrder").document(auth.getUid())
                .collection("Orders").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc :task.getResult().getDocuments()) {
                            SellerOrder sellerOrder = doc.toObject(SellerOrder.class);
                            sellerOrder.setIdDocument(doc.getId());
                            myOrder.add(sellerOrder);
                            myOrderAdapterSeller.notifyDataSetChanged();
                        }
                    }
                });
    }
}
