package com.example.ecomapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ecomapplication.activities.RegistrationSellerActivity;
import com.example.ecomapplication.activities.SellerActivity;
import com.example.ecomapplication.activities.ShowAllCategoryActivity;
import com.example.ecomapplication.activities.ShowAllProductsActivity;
import com.example.ecomapplication.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login, R.id.nav_signup, R.id.nav_notification,
                R.id.nav_order)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (auth.getCurrentUser() != null) {
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_signup).setVisible(false);
            navigationView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                    navigationView.removeOnLayoutChangeListener(this);
                    TextView headerEmail = navigationView.findViewById(R.id.nav_header_email);
                    TextView headerName = navigationView.findViewById(R.id.nav_header_name);
                    headerEmail.setText(auth.getCurrentUser().getEmail());
                    headerName.setText(auth.getCurrentUser().getDisplayName());
                }
            });
        } else {
            navigationView.getMenu().findItem(R.id.nav_cart).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_order).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_signout).setVisible(false);
        }

        navigationView.getMenu().findItem(R.id.nav_signout).setOnMenuItemClickListener(menuItem -> {
            firestore.collection("UserInfo").document(auth.getUid())
                            .update("deviceToken", null);
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(getIntent());
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_merchant) {
            final boolean[] check = {false};
            FirebaseFirestore.getInstance().collection("UserInfo").document(auth.getUid())
                    .get().addOnSuccessListener(documentSnapshot -> {
                        check[0] = documentSnapshot.getBoolean("is_seller");
                        if (check[0]) {
                            startActivity(new Intent(getBaseContext(), SellerActivity.class));
                        } else {
                            startActivity(new Intent(getBaseContext(), RegistrationSellerActivity.class));
                        }
                    });

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void showAllProduct(View view) {
        Intent intent = new Intent(this, ShowAllProductsActivity.class);
        startActivity(intent);
    }

    public void showAllCategory(View view) {
        Intent intent = new Intent(this, ShowAllCategoryActivity.class);
//        Intent intent = new Intent(this, Test.class);
//        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }

    public void setActionBarTitle(String title) {
        binding.appBarMain.toolbar.setTitle(title);
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_01", name, importance);
            channel.setDescription(description);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            Log.v("Test", "Created a notification channel");
        }
    }
}