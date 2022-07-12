package com.example.ecomapplication.ui.search;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.example.ecomapplication.activities.DetailActivity;
import com.example.ecomapplication.adapters.CategoryAdapter;
import com.example.ecomapplication.adapters.HistorySearchAdapter;
import com.example.ecomapplication.adapters.SearchAdapter;
import com.example.ecomapplication.models.Category;
import com.example.ecomapplication.models.Product;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {

    GridView categoryView;
    ListView historyView;
    ListView resultView;
    EditText searchView;
    CategoryAdapter categoryAdapter;
    HistorySearchAdapter historyAdapter;
    SearchAdapter searchAdapter;
    FirebaseFirestore firestore;

    List<Product> searchedProduct = new ArrayList<>();
    List<Category> categories;
    List<String> history;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_search, container, false);

        firestore = FirebaseFirestore.getInstance();
        categoryView = root.findViewById(R.id.categoryList);
        historyView = root.findViewById(R.id.searchHistory);
        searchView = root.findViewById(R.id.searchBox);
        resultView = root.findViewById(R.id.searchResult);

        searchAdapter = new SearchAdapter(getContext(), searchedProduct);
        resultView.setAdapter(searchAdapter);

        getCategoriesFromFireBase();
        categoryAdapter = new CategoryAdapter(getContext(), categories);
        categoryView.setAdapter(categoryAdapter);

        getHistoryFromFireBase();
        historyAdapter = new HistorySearchAdapter(getContext(), history);
        historyView.setAdapter(historyAdapter);

        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            ((MainActivity) getActivity()).setActionBarTitle("Search");
        }

        searchView.setOnTouchListener((view, motionEvent) -> {
            final int DRAWABLE_LEFT = 0;

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                if (motionEvent.getRawX() <= (searchView.getLeft() + searchView.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                    NavHostFragment.findNavController(SearchFragment.this)
                            .navigate(R.id.search_back_to_main);
                    return true;
                }
            }
            return false;
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // searchedProduct.clear();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                history.clear();
                historyAdapter.notifyDataSetChanged();

                if (charSequence.length() != 0) {
                    searchForProduct(String.valueOf(charSequence));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchAdapter.notifyDataSetChanged();
            }
        });

        historyView.setOnItemClickListener((adapterView, view, i, l) -> {
            String historyItem = historyView.getItemAtPosition(i).toString();

            searchView.setText(historyItem);
            searchForProduct(historyItem);
            history.clear();
            historyAdapter.notifyDataSetChanged();
        });

        resultView.setOnItemClickListener(((adapterView, view, i, l) -> {
            String searchText = searchView.getText().toString();
            Map<String, String> search = new HashMap<>();
            search.put("id_user", "1");
            search.put("text", searchText);
            firestore.collection("SearchHistory")
                    .add(search).addOnSuccessListener(documentReference -> {
                        Log.v("Result", "Added new search history");
                    });

            Product product = (Product) adapterView.getItemAtPosition(i);

            Intent intent = new Intent (view.getContext(), DetailActivity.class);
            intent.putExtra("id_product", product.getDocumentId());

            view.getContext().startActivity(intent);
        }));
    }
    private void getCategoriesFromFireBase() {
        categories = new ArrayList<>();

        firestore.collection("Category").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Category category = document.toObject(Category.class);
                    categories.add(category);
                    categoryAdapter.notifyDataSetChanged();
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    private void getHistoryFromFireBase() {
        history = new ArrayList<>();

        firestore.collection("SearchHistory")
                .whereEqualTo("id_user", "1").limit(5)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            history.add(document.getString("text"));
                            historyAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.v("Result", "Error getting documents.", task.getException());
                    }
        });
    }

    private void searchForProduct(String productName) {
        firestore.collection("Product")
                .whereGreaterThanOrEqualTo("name", productName)
                .whereLessThanOrEqualTo("name", productName + "\uF7FF")
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                searchedProduct.clear();
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    Product product = snapshot.toObject(Product.class);
                    product.setDocumentId(snapshot.getId());
                    searchedProduct.add(product);
                }

                searchAdapter.notifyDataSetChanged();
            } else {
                Log.v("Result", "Error getting documents.", task.getException());
            }
        });
    }
}