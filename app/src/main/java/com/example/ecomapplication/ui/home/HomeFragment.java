package com.example.ecomapplication.ui.home;


import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ecomapplication.R;
import com.example.ecomapplication.adapters.CategoryAdapter;
import com.example.ecomapplication.adapters.NewProductAdapter;
import com.example.ecomapplication.adapters.PopularProductAdapter;
import com.example.ecomapplication.adapters.SliderAdapter;
import com.example.ecomapplication.models.Category;
import com.example.ecomapplication.models.Product;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private ViewPager2 viewPager2;
    private List<Category> categoryList;
    private RecyclerView recyclerViewCategory, newProductRecyclerview, popularRecyclerview ;
    CategoryAdapter categoryAdapter;
    GridView categoryView;
    ProgressDialog progressDialog;
    FirebaseFirestore firestore;
    private static int TIME_OUT = 1200 ;
    RecyclerView categoryRecyclerView;
    ListView viewCategory;

    //new product recycler;
    NewProductAdapter newProductAdapter;
    List<Product> newProductList;

    //popular products
    PopularProductAdapter popularProductAdapter;
    List<Product> popularProductsList;

    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    CollectionReference applicationsRef;
    ArrayList<Product> sliderDataArrayList;
    SliderAdapter SlideraAapter;

    //slider demo
    String url1 = "https://www.geeksforgeeks.org/wp-content/uploads/gfg_200X200-1.png";
    String url2 = "https://qphs.fs.quoracdn.net/main-qimg-8e203d34a6a56345f86f1a92570557ba.webp";
    String url3 = "https://bizzbucket.co/wp-content/uploads/2020/08/Life-in-The-Metro-Blog-Title-22.png";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        firestore = FirebaseFirestore.getInstance();

        //        ImageSlider imageSlider = root.findViewById((R.id.image_slider));
        //        List<SlideModel> slideModels = new ArrayList<>();
        //        slideModels.add(new SlideModel(R.drawable.banner1, "Discount On Shoes Items", ImageView.ScaleType.CENTER_CROP));
        //        slideModels.add(new SlideModel(R.drawable.banner2, "Discount On Perfume", ImageView.ScaleType.CENTER_CROP));
        //
        //        slideModels.add(new SlideModel(R.drawable.banner3, "70% OFF", ImageView.ScaleType.CENTER_CROP));
        //        imageSlider.setImageList(slideModels);
        //        recyclerViewCategory = root.findViewById(R.id.rec_category);
        //        newProductRecyclerview = root.findViewById(R.id.new_product_rec);

        //binding
        categoryView = root.findViewById(R.id.rec_category);
        popularRecyclerview = root.findViewById(R.id.new_product_rec);

        // create slider

        SliderView sliderView = root.findViewById(R.id.slider);
        getProductForSlider();

//        sliderDataArrayList.add(new SliderData(url1));
//        sliderDataArrayList.add(new SliderData(url2));
//        sliderDataArrayList.add(new SliderData(url3));
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(SlideraAapter);
        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(2
        );
        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);
        // to start autocycle below method is used.
        sliderView.startAutoCycle();

        // progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Welcome to my website");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //Do something here
                progressDialog.dismiss();
            }
        }, TIME_OUT);
        // load Category
        getCategoryDataFromFirebase();

        //load product
        getProductDataFromFirebase();
//        popularRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        popularRecyclerview.setLayoutManager(new GridLayoutManager(getContext(),2));
        popularProductAdapter = new PopularProductAdapter(getContext(), popularProductsList);
        popularRecyclerview.setAdapter(popularProductAdapter);

        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        categoryView.setAdapter(categoryAdapter);

        return root;
    }

    public void getProductForSlider(){
        sliderDataArrayList = new ArrayList<>();
        SlideraAapter = new SliderAdapter(getContext(), sliderDataArrayList);
        firestore.collection("Product").limit(5).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.v("TAGG", "trueeeee");
                try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Product product = document.toObject(Product.class);
                        product.setDocumentId(document.getId());
                        sliderDataArrayList.add(product);
                        SlideraAapter.notifyDataSetChanged();

                    }
                }
                catch (Exception e ) {
                    e.printStackTrace();
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }
    public void getProductDataFromFirebase(){
        popularProductsList = new ArrayList<>();
        firestore.collection("Product").limit(6).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                try {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Product product = document.toObject(Product.class);
                        product.setDocumentId(document.getId());

                        popularProductsList.add(product);
                        popularProductAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e ) {
                    e.printStackTrace();
                }   
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    public void getCategoryDataFromFirebase(){
        categoryList = new ArrayList<>();
        int MAX_ITEM = 4;

        firestore.collection("Category").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int item_num = 0;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (item_num < MAX_ITEM) {
                        Category category = document.toObject(Category.class);
                        categoryList.add(category);
                        categoryAdapter.notifyDataSetChanged();
                        item_num++;
                    } else {
                        break;
                    }
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

}