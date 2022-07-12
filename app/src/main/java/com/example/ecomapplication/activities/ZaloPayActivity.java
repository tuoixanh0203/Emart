package com.example.ecomapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecomapplication.Helper.CreateOrder;
import com.example.ecomapplication.Helper.AppInfo;
import com.example.ecomapplication.R;


import org.json.JSONObject;


import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class ZaloPayActivity extends AppCompatActivity {
    Button btnPay;
    String amount = "10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zalo_pay);

        btnPay = findViewById(R.id.btnPay);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ZaloPaySDK.init(AppInfo.APP_ID, Environment.SANDBOX);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateOrder orderApi = new CreateOrder();
                try {
                    JSONObject data = orderApi.createOrder(amount);
                    String code = data.getString("returncode");
                    Log.v("TAGGG" , code);
                    if (code.equals("1")) {

                        String token = data.getString("zptranstoken");

                        ZaloPaySDK.getInstance().payOrder(ZaloPayActivity.this, token, "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                                Toast.makeText(ZaloPayActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPaymentCanceled(String zpTransToken, String appTransID) {
                                Toast.makeText(ZaloPayActivity.this, "Thanh toán bị hủy", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                                Toast.makeText(ZaloPayActivity.this, "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}