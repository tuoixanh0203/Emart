package com.example.ecomapplication.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.CommentAdapter;
import com.example.ecomapplication.models.Order;
import com.example.ecomapplication.models.OrderModel;
import com.example.ecomapplication.models.Product;
import com.example.ecomapplication.models.UserInfo;
import com.example.ecomapplication.ui.home.HomeFragment;
import com.example.ecomapplication.ui.order.OrderFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


public class FeedbackAcitivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore firestore;
    RatingBar ratingProduct;
    TextView productNameFb, save;
    TextView btnAddImgFb;
    ImageView productPhoto, thumbnail;
    EditText content;
    String id_order, id_product, emailUser, ratingPro;
    int ratingNumPro;
    FirebaseFirestore db;
    private FirebaseAuth auth;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_acitivity);
        storage = FirebaseStorage.getInstance();

        ratingProduct = findViewById(R.id.rating_product);
        productNameFb = findViewById(R.id.product_name_fb);
        btnAddImgFb = findViewById(R.id.button_add_img_fb);
        save = findViewById(R.id.save);
        productPhoto = findViewById(R.id.product_photo);
        thumbnail = findViewById(R.id.thumbnail);
        content = findViewById(R.id.content_fb);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        OrderModel orderModel = (OrderModel) bundle.get("object_order");
        id_order = orderModel.getId();
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("OrderDetail").document(id_order)
                .collection("Products").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Product product = document.toObject(Product.class);
                        id_product = product.getDocumentId();

                        productNameFb.setText(product.getName());

                        StorageReference storageReference = storage.getReferenceFromUrl(product.getImg_url());

                        // Dat anh lay tu Firebase cho item
                        storageReference.getDownloadUrl()
                                .addOnSuccessListener(uri -> Picasso.get()
                                        .load(uri.toString())
                                        .fit().centerInside()
                                        .into(productPhoto))
                                .addOnFailureListener(e -> Log.v("Result", "Error when get the images: " + e));
                    }
                });

        db.collection("UserInfo").document(auth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        try {
                            UserInfo userInfo = task.getResult().toObject(UserInfo.class);
                            emailUser = userInfo.getEmail();
                        }
                        catch (Exception e ) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

        save.setOnClickListener(view -> {
            int numStar = (int) ratingProduct.getRating();
            String _comment = content.getText().toString().trim();
//            Log.v("Test1111", _comment);
            String _imgUrl = "https://firebasestorage.googleapis.com/v0/b/ecommerce-de4aa.appspot.com/o/images%2Fic_launcher-playstore.png?alt=media&token=34190616-be4e-4db8-83f0-858487ef6c01".trim();
            String _id = id_product.trim();
            String imgUrlComment = "gs://ecommerce-de4aa.appspot.com/images/" + uploadImgToFirebase();
            Object date = ServerValue.TIMESTAMP;

            AddCommentToFireBase(_comment, new Date(), _id, emailUser, _imgUrl, numStar, imgUrlComment);
            updateRatingProduct(numStar);
//            startActivity(new Intent(FeedbackAcitivity.this , HomeFragment.class));
        });

        btnAddImgFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImgComment();
            }
        });
    }

    private void updateRatingProduct(int numStar) {
        FirebaseFirestore.getInstance().collection("Product").document(id_product)
                .get().addOnSuccessListener(documentSnapshot -> {
                    ratingPro = (String) documentSnapshot.get("rating");
                    ratingNumPro = Math.toIntExact(documentSnapshot.getLong("rating_number"));
                    float f_ratingPro = Float.parseFloat(ratingPro);
                    float f = (float)(f_ratingPro * ratingNumPro + numStar)/(ratingNumPro + 1);
                    float ratingValue = (float) Math.round(f * 10) / 10;
                    db.collection("Product").document(id_product)
                            .update("rating", String.valueOf(ratingValue), "rating_number", ratingNumPro + 1);
                    Intent intent = new Intent(FeedbackAcitivity.this, MainActivity.class);
                    intent.putExtra("id_product",  id_product);
                    startActivity(intent);
                });
    }

    public void selectImgComment(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    public String uploadImgToFirebase(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss" , Locale.CHINESE);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);


        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        thumbnail.setImageURI(imageUri);
                    }

                });
        return fileName;
    }

    public void AddCommentToFireBase(String content, Date date, String id_product, String id_user, String user_img, int rating, String comment_Img){
        String docId = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("content", content);
        doc.put("date", date);
        doc.put("id_product", id_product);
        doc.put("id_user", id_user);
        doc.put("user_img", user_img);
        doc.put("rating", rating);
        doc.put("comment_img", comment_Img);

        db.collection("Comment").document(docId).set(doc)
                .addOnCompleteListener(task -> {
                    showMessage("Đã thêm bình luận");
//                    addComment.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> showMessage("Thêm bình luận thất bại"));
    }

    public void showMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            thumbnail.setImageURI(imageUri);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}