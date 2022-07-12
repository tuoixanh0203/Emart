package com.example.ecomapplication.activities;
import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.ecomapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNamePayment;

public class TestPaymentActivity extends AppCompatActivity {


    TextView tvEnvironment;
    TextView tvMerchantCode;
    TextView tvMerchantName;
    EditText edAmount;
    TextView tvMessage;
    Button btnPayMoMo;
    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "CGV Cinemas";
    private String merchantCode = "CGV19072017";
    private String merchantNameLabel = "Nhà cung cấp";
    private String description = "Fast & Furious 8";
    static String KEY_ENVIRONMENT = "key_environment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_payment);
        Bundle data = getIntent().getExtras();
        tvEnvironment = findViewById(R.id.tvEnvironment);
        tvMerchantCode = findViewById(R.id.tvMerchantCode);
        tvMerchantName = findViewById(R.id.tvMerchantName);
        edAmount = findViewById(R.id.edAmount);
        tvMessage = findViewById(R.id.tvMessage);
        btnPayMoMo = findViewById(R.id.btnPayMoMo);
        btnPayMoMo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              requestPayment();
            }
        });
        if(data != null){
            environment = data.getInt(KEY_ENVIRONMENT);
        }
        if(environment == 0){
            AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEBUG);
            tvEnvironment.setText("Development Environment");
        }else if(environment == 1){
            AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
            tvEnvironment.setText("Development Environment");
        }else if(environment == 2){
            AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.PRODUCTION);
            tvEnvironment.setText("PRODUCTION Environment");
        }

        tvMerchantCode.setText("Merchant Code: "+merchantCode);
        tvMerchantName.setText("Merchant Name: "+merchantName);
    }

    //example payment
    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        if (edAmount.getText().toString() != null && edAmount.getText().toString().trim().length() != 0)
            amount = edAmount.getText().toString().trim();

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME, merchantName);
        eventValue.put(MoMoParameterNamePayment.MERCHANT_CODE, merchantCode);
        eventValue.put(MoMoParameterNamePayment.AMOUNT, amount);
        eventValue.put(MoMoParameterNamePayment.DESCRIPTION, description);
        //client Optional
        eventValue.put(MoMoParameterNamePayment.FEE, fee);
        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME_LABEL, merchantNameLabel);

        eventValue.put(MoMoParameterNamePayment.REQUEST_ID,  merchantCode+"-"+ UUID.randomUUID().toString());
        eventValue.put(MoMoParameterNamePayment.PARTNER_CODE, "CGV19072017");

        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
            objExtraData.put("ticket", "{\"ticket\":{\"01\":{\"type\":\"std\",\"price\":110000,\"qty\":3}}}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put(MoMoParameterNamePayment.EXTRA_DATA, objExtraData.toString());
        eventValue.put(MoMoParameterNamePayment.REQUEST_TYPE, "payment");
        eventValue.put(MoMoParameterNamePayment.LANGUAGE, "vi");
        eventValue.put(MoMoParameterNamePayment.EXTRA, "");
        //Request momo app
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"));

                    if(data.getStringExtra("data") != null && !data.getStringExtra("data").equals("")) {
                        // TODO:

                    } else {
                        tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    tvMessage.setText("message: " + message);
                } else if(data.getIntExtra("status", -1) == 2) {
                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                } else {
                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
                }
            } else {
                tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
            }
        } else {
            tvMessage.setText("message: " + this.getString(R.string.not_receive_info_err));
        }
    }

}