package com.example.ecomapplication.Helper;

import com.example.ecomapplication.models.FCMNotification;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class NotificationApi {
    public static String pushNotification(FCMNotification notification) {
        Type objectType = new TypeToken<FCMNotification>(){}.getType();

        String json = new Gson().toJson(notification, objectType);

        return HttpUtils.post(json);
    }
}
