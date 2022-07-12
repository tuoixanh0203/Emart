package com.example.ecomapplication.activities;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.CommentAdapter;
import com.example.ecomapplication.adapters.OrderAdapter;
import com.example.ecomapplication.adapters.ProductSellerAdapter;
import com.example.ecomapplication.models.Category;
import com.example.ecomapplication.models.Comment;
import com.example.ecomapplication.models.Product;
import com.example.ecomapplication.models.SellerInfo;
import com.example.ecomapplication.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class SellerOrderDetailActivity extends AppCompatActivity {
    ImageView detailedImg;
    TextView detailedName, detailedDesc, detailedPrice, ratingValue, quantityInStock;
    RatingBar detailedRating;
    Button editProduct, deleteProduct, addComment;
    Product product;
    FirebaseStorage storage;
    CircularImageView userCommentImg;
    EditText postDetailComment;
    RecyclerView RvComment;
    String PostKey;
    private FirebaseAuth auth;

    FirebaseFirestore db;
    FirebaseDatabase firebaseDatabase;
    List<Comment> list;
    CommentAdapter commentAdapter;

    List<Product> productList;
    ProductSellerAdapter productSellerAdapter;

    String productId, product_category, product_size, emailSeller;

    private void binding() {
        storage = FirebaseStorage.getInstance();
        setContentView(R.layout.activity_seller_oder_detail);
        RvComment = findViewById(R.id.rv_comment);
        detailedImg = findViewById(R.id.detailed_img);
        detailedName = findViewById(R.id.detailed_name);
        detailedRating = findViewById(R.id.my_rating);
        ratingValue = findViewById(R.id.rating_value);
        detailedDesc = findViewById(R.id.detailed_desc);
        detailedPrice = findViewById(R.id.detailed_price);
        quantityInStock = findViewById(R.id.seller_quantity);
        editProduct = findViewById(R.id.edit_product);
        deleteProduct = findViewById(R.id.delete_product);
        userCommentImg = findViewById(R.id.user_comment_img);
        addComment = findViewById(R.id.add_comment_btn);
        postDetailComment = findViewById(R.id.post_detail_comment);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_seller_oder_detail);
        binding();

        final Object obj = getIntent().getSerializableExtra("productSellerDetail");
        if (obj instanceof Product) {
            product = (Product) obj;
            productId = product.getId();

            Log.v("Result", "Get product ID: " + productId);
        }

        if (product != null) {
            detailedName.setText(product.getName());
            detailedDesc.setText(product.getDescription());
            detailedPrice.setText(String.valueOf(product.getPrice()));
            ratingValue.setText(product.getRating());
            quantityInStock.setText(String.valueOf(product.getQuantity()));
            detailedRating.setRating(Float.parseFloat(product.getRating()));
            product_category = product.getId_category();
            product_size = product.getSize();

            StorageReference storageReference = storage.getReferenceFromUrl(product.getImg_url());

//             Dat anh lay tu Firebase cho item
            storageReference.getDownloadUrl()
                    .addOnSuccessListener(uri -> Picasso.get()
                            .load(uri.toString())
                            .fit().centerInside()
                            .into(detailedImg))
                    .addOnFailureListener(e -> Log.v("Error", "Error when get the images: " + e));
        }
        iniRvComment(productId);
        db.collection("SellerInfo").document(auth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            SellerInfo sellerInfo = task.getResult().toObject(SellerInfo.class);
                            Log.v("Le Thanh Huyen", sellerInfo.getEmail());
                            emailSeller = sellerInfo.getEmail();
                        }
                        catch (Exception e ) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

        RvComment.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(getApplicationContext(), list);
        RvComment.setAdapter(commentAdapter);

//        RvComment.setLayoutManager(new LinearLayoutManager(this));
//        commentAdapter = new CommentAdapter(getApplicationContext(), list);
//        RvComment.setAdapter(commentAdapter);



        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Chocopie", product.getName());
                new AlertDialog.Builder(SellerOrderDetailActivity.this)
                        .setTitle("Xóa sản phẩm")
                        .setMessage("Bạn muốn xóa sản phẩm " + product.getName() + " ?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
//                                db.collection("Product").document(product.getDocumentId()).delete();
                                db.collection("Product").get().addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(document.getId().equals(product.getDocumentId())){
                                                Log.v("id_product", product.getDocumentId());
                                                db.collection("Product").document(product.getDocumentId()).delete();
                                                startActivity(new Intent(SellerOrderDetailActivity.this, SellerActivity.class));
                                            }
                                        }
                                    } else {
                                        Log.w(TAG, "Error getting documents.", task.getException());
                                    }
                                });
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniRvComment(productId);
                commentAdapter = new CommentAdapter(getApplicationContext(), list);
                RvComment.setAdapter(commentAdapter);
                addComment.setVisibility(View.INVISIBLE);
                String _imgUrl = "https://firebasestorage.googleapis.com/v0/b/ecommerce-de4aa.appspot.com/o/274736835_677983293549819_1786662699780048436_n.jpg?alt=media&token=23b9dcff-f1ce-436b-af77-101af401075f".trim();
                String _id = productId.trim();
                String _comment = postDetailComment.getText().toString().trim();
                //String _user = "dfdfsfdsf".trim();
                Object date = ServerValue.TIMESTAMP;
                AddCommentToFireBase(_comment, new Date(), _id, emailSeller, _imgUrl);
            }
        });
    }
    public void AddCommentToFireBase(String content, Object date, String id_product, String id_user, String user_img){
        String docId = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("content", content);
        doc.put("date", date);
        doc.put("id_product", id_product);
        doc.put("id_user", id_user);
        doc.put("user_img", user_img);

        db.collection("Comment").document(docId).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        postDetailComment.setText("");
                        showMessage("Comment Successed");
                        addComment.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Comment Failed");
                    }
                });
    }

    public void iniRvComment(String prod_id){
        list = new ArrayList<>();

        db.collection("Comment").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.v("comment" , "document.getId()");

                try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Comment comment = document.toObject(Comment.class);
                        Log.v("name", comment.getContent());
                        if(comment.getId_product().equals(prod_id)){
                            list.add(comment);
                            commentAdapter.notifyDataSetChanged();
                        }
                    }

                }
                catch (Exception e ) {
                    e.printStackTrace();
                }
            } else {
                Log.w("TAG", "Error getting documents.", task.getException());
            }
        });
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProductActivity.class);

//                String product_name_up = detailedName.getText().toString();
//                String product_desc_up = detailedDesc.getText().toString();
//                String price_up = detailedPrice.getText().toString();
//                String quantity_up = quantityInStock.getText().toString();
//                String rating_up = String.valueOf(detailedRating.getRating());
//
//
//                db.collection("Product").get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Product prod = document.toObject(Product.class);
////                            Log.v("Chocomint", prod.getDocumentId());
//                            if(prod.getId().equals(product.getId())){
//                                product.setDocumentId(document.getId());
//                                Log.v("Chupachup", product.getDocumentId());
//                                documentId = product.getDocumentId();
//                                intent.putExtra("Document ID", documentId);
//                            }
//                        }
//                    } else {
//                        Log.w(TAG, "Error getting documents.", task.getException());
//                    }
//                });

                intent.putExtra("ProductToEdit", product);
//                intent.putExtra("Description", product_desc_up);
//                intent.putExtra("Category", product_category);
//                intent.putExtra("Price", price_up);
//                intent.putExtra("Quantity", quantity_up);
//                intent.putExtra("Size", product_size);
//                intent.putExtra("Rating", rating_up);
//                intent.putExtra("ID", productId);

                // start the Intent
                startActivity(intent);

            }
        });
//        buyNow.setOnClickListener(view -> {
//            Product productCart = new Product(
//                    product.getName(),
//                    product.getImg_url(),
//                    product.getId_category(),
//                    product.getPrice(),
//                    product.getSize(),
//                    quantity,
//                    product.getDescription()
//            );
//
//            productCart.setProductId(productId);
//
//            addProductToFirebaseCart(view, productCart);
//
//            Intent intent = new Intent(view.getContext(), CheckoutActitvity.class);
//            startActivity(intent);
//        });
    }
}