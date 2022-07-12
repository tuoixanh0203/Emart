package com.example.ecomapplication.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.CommentAdapter;
import com.example.ecomapplication.models.Comment;
import com.example.ecomapplication.models.Product;
import com.example.ecomapplication.models.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class DetailActivity extends AppCompatActivity {
    ImageView detailedImg;
    TextView detailedName, detailedDesc, detailedPrice, quantityOrder, ratingValue, orderWarning;
    RatingBar detailedRating;
    Button buyNow, addToCart, addItem, removeItem,addComment;
    Product product;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    CircularImageView userCommentImg;
    EditText postDetailComment;
    RecyclerView RvComment;
    private FirebaseAuth auth;

    FirebaseFirestore db;
    FirebaseDatabase firebaseDatabase;
    List<Comment> list;
    CommentAdapter commentAdapter;
    private  String productIdIntent;
    int quantity = 1;
    String productId, emailUser;

    private void binding() {
        storage = FirebaseStorage.getInstance();
        firestore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_detail);
        RvComment = findViewById(R.id.rv_comment);
        detailedImg = findViewById(R.id.detailed_img);
        detailedName = findViewById(R.id.detailed_name);
        detailedRating = findViewById(R.id.my_rating);
        ratingValue = findViewById(R.id.rating_value);
        detailedDesc = findViewById(R.id.detailed_desc);
        detailedPrice = findViewById(R.id.detailed_price);
        quantityOrder = findViewById(R.id.quantity);
        buyNow = findViewById(R.id.buy_now);
        addToCart = findViewById(R.id.add_to_cart);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);
        userCommentImg = findViewById(R.id.user_comment_img);
        addComment = findViewById(R.id.add_comment_btn);
        postDetailComment = findViewById(R.id.post_detail_comment);
        orderWarning = findViewById(R.id.order_warning);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_detail);
        binding();

        final Object productIdIntent = getIntent().getSerializableExtra("id_product");
        productId = (String) productIdIntent;
        if(productIdIntent != null){
             getProductInfo((String) productIdIntent);
        }

        checkHasBoughtProduct();
        db.collection("UserInfo").document(auth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            UserInfo userInfo = task.getResult().toObject(UserInfo.class);
                            Log.v("Le Thanh Huyen", userInfo.getEmail());
                            emailUser = userInfo.getEmail();
                        }
                        catch (Exception e ) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
        iniRvComment(productId);

        RvComment.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(getApplicationContext(), list);
        RvComment.setAdapter(commentAdapter);

//        RvComment.setLayoutManager(new LinearLayoutManager(this));
//        commentAdapter = new CommentAdapter(getApplicationContext(), list);
//        RvComment.setAdapter(commentAdapter);

        addComment.setOnClickListener(view -> {
            iniRvComment(productId);
            commentAdapter = new CommentAdapter(getApplicationContext(), list);
            RvComment.setAdapter(commentAdapter);
            addComment.setVisibility(View.INVISIBLE);
            String _imgUrl = "https://firebasestorage.googleapis.com/v0/b/ecommerce-de4aa.appspot.com/o/274736835_677983293549819_1786662699780048436_n.jpg?alt=media&token=23b9dcff-f1ce-436b-af77-101af401075f".trim();
            String _id = productId.trim();
            String _comment = postDetailComment.getText().toString().trim();
  //          String _user = "dfdfsfdsf".trim();
            Object date = ServerValue.TIMESTAMP;

            AddCommentToFireBase(_comment, new Date(), _id, emailUser, _imgUrl);
        });
    }

    public void getProductInfo(String productId){
        firestore.collection("Product").document(productId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                try {
                    Product res = task.getResult().toObject(Product.class);
                   product = res;
                    detailedName.setText(product.getName());
                    detailedDesc.setText(product.getDescription());
                    detailedPrice.setText(NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(product.getPrice()));
                    ratingValue.setText(product.getRating());
                    detailedRating.setRating(Float.parseFloat(product.getRating()));

                    StorageReference storageReference = storage.getReferenceFromUrl(product.getImg_url());

                    // Dat anh lay tu Firebase cho item
                    storageReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> Picasso.get()
                                    .load(uri.toString())
                                    .fit().centerInside()
                                    .into(detailedImg))
                            .addOnFailureListener(e -> Log.v("Result", "Error when get the images: " + e));
                }
                catch (Exception e ) {
                    e.printStackTrace();
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }
    public void AddCommentToFireBase(String content, Date date, String id_product, String id_user, String user_img){
        String docId = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("content", content);
        doc.put("date", date);
        doc.put("id_product", id_product);
        doc.put("id_user", id_user);
        doc.put("user_img", user_img);

        db.collection("Comment").document(docId).set(doc)
                .addOnCompleteListener(task -> {
                    postDetailComment.setText("");
                    showMessage("Đã thêm bình luận");
                    addComment.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> showMessage("Thêm bình luận thất bại"));
    }

    private void addProductToFirebaseCart(View view, Product newProduct, String productId) {
        Map<String, Object> product_to_add = new HashMap<>();
        product_to_add.put("description", newProduct.getDescription());
        product_to_add.put("id_category", newProduct.getId_category());
        product_to_add.put("id_seller", newProduct.getId_seller());
        product_to_add.put("img_url", newProduct.getImg_url());
        product_to_add.put("quantity", newProduct.getQuantity());
        product_to_add.put("price", newProduct.getPrice());
        product_to_add.put("name", newProduct.getName());
        product_to_add.put("rating", newProduct.getRating());
        product_to_add.put("size", newProduct.getSize());
        firestore.collection("Cart").document(auth.getUid())
                .collection("Products").document(productId)
                .set(product_to_add).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
//                        Toast.makeText(
//                                view.getContext(),
//                                "Added product ID " + newProduct.getDocumentId()
//                                        + " of " + newProduct.getQuantity() + " products to cart",
//                                Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(
                                view.getContext(),
                                "Fail to add product to cart",
                                Toast.LENGTH_SHORT).show();
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

    private void checkHasBoughtProduct() {
        String userId = FirebaseAuth.getInstance().getUid();
        Log.v("Result", "User: " + userId);

        firestore.collection("Order").document(userId)
                .collection("Orders").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> orders = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                            orders.add(documentSnapshot.getId());
                        }

                        for (String order: orders) {
                            Log.v("fdsfs_order", order);
                            Log.v("fdsfs__", productId);
                            firestore.collection("OrderDetail").document(order)
                                    .collection("Products").get().addOnSuccessListener(queryDocumentSnapshots -> {
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            Product product = document.toObject(Product.class);

                                            if (product.getDocumentId() != null
                                                    && product.getDocumentId().equals(productId)) {
                                                Log.v("fdsfs____",product.getDocumentId());
                                                Log.v("fdsfs____order",order);
                                                userCommentImg.setVisibility(View.VISIBLE);
                                                postDetailComment.setVisibility(View.VISIBLE);
                                                addComment.setVisibility(View.VISIBLE);
                                                orderWarning.setVisibility(View.INVISIBLE);
                                                return;
                                            }

                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        addItem.setOnClickListener(view -> {
            quantity = Integer.parseInt((String) quantityOrder.getText());
            quantity = quantity + 1;
            quantityOrder.setText(String.valueOf(quantity));
        });

        removeItem.setOnClickListener(view -> {
            quantity = Integer.parseInt((String) quantityOrder.getText());
            if (quantity > 1) {
                quantity = quantity - 1;
                quantityOrder.setText(String.valueOf(quantity));
            }
        });

        addToCart.setOnClickListener(view -> {
            Log.v("addtocart", "dfsfds");
            Product productCart = new Product(
                    product.getName(),
                    product.getImg_url(),
                    product.getId_category(),
                    product.getPrice(),
                    product.getSize(),
                    quantity,
                    product.getDescription(),
                    product.getRating(),
                    product.getId_seller()
            );


            addProductToFirebaseCart(view, productCart , productId);
        });

        buyNow.setOnClickListener(view -> {
            Product productCart = new Product(
                    product.getName(),
                    product.getImg_url(),
                    product.getId_category(),
                    product.getPrice(),
                    product.getSize(),
                    quantity,
                    product.getDescription()
            );


            addProductToFirebaseCart(view, productCart, productId);

            Intent intent = new Intent(view.getContext(), CheckoutActitvity.class);
            startActivity(intent);
        });
    }


}