package com.example.ecomapplication.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.ecomapplication.MainActivity;
import com.example.ecomapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseInstanceIDService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.v("Test", "New token: " + token);

        getSharedPreferences("_", MODE_PRIVATE).edit()
                .putString("token", token).apply();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Map<String, String> messageReceived = message.getData();

        String title = messageReceived.get("title");
        String body = messageReceived.get("body");

        Log.v("Test", "Notification: " + title + " " + body);
        showNotification(title, body);
    }

    public void showNotification(String title, String body) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_01")
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        manager.notify(0, builder.build());

    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("channel_01", name, importance);
            channel.setDescription(description);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            Log.v("Test", "Created a notification channel");
        }
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE)
                .getString("token", null);
    }
}
