package com.example.ecomapplication.Helper;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpUtils {
    private static final String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_TOKEN = "AAAAR_yaroM:APA91bEzoTSg0auMjPGtGQ2TCTqWuDVhZvaPFL7Ra4qm8wFFSdB0k9K3SUZ-z-7_ho8VjFmBdtkA4cMeKxRiVpQBoyCmF60H98FCDZ_LMXcFzzaGcSeIC5F2-jvYucIhzFx48dzegDwc";
    private static URL url = null;
    private static HttpURLConnection httpURLConnection;

    public static void setup() {
        try {
            url = new URL(BASE_URL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setReadTimeout(15*1000);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
            httpURLConnection.setRequestProperty("Authorization", "key=" + SERVER_TOKEN);

        } catch (Exception e) {
            Log.v("Test", "REST API Setup Error: " + e.getMessage());
        }
    }

    public static String post(String json) {
        OutputStreamWriter writer;
        StringBuilder stringBuilder = null;

        try {
            setup();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
            writer.write(json);
            writer.close();

            stringBuilder = new StringBuilder();
            int statusCode = httpURLConnection.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    Log.v("Test", "Get push notification response: " + stringBuilder);
                }
            }
        } catch (Exception e) {
            Log.v("Test", "Post notification failed: " + e.getMessage());
        }

        return stringBuilder.toString();
    }
}
