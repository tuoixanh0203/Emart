package com.example.ecomapplication.models;

import android.util.Log;

import java.util.List;

public class FCMNotification {
    public Notification notification;
    public Data data;
    public List<String> registration_ids;

    public FCMNotification(Notification notification, Data data, List<String> registration_ids) {
        this.notification = notification;
        this.data = data;
        this.registration_ids = registration_ids;
    }

    public static Notification createNotification(String title, String body) {
        return new Notification(title, body);
    }

    public static String getCancelOrderTitle(){
        return "Thông báo hủy đơn hàng " ;
    }
    public static String getCancelOrderBody(String orderId){
        return "Đơn hàng " + orderId + "đã bị hủy bởi người bán" ;
    }
    public static String getPaymentSuccessTitle(){
        return "Thông báo thanh toán " ;
    }
    public static String getPaymentSuccessBody(String amount){
        return "Bạn đã thanh toán số  " + amount + " cho đơn hàng " ;
    }
    public static String getConfirmOrderTitle() {
        return "Thông báo xác nhận đơn hàng";
    }

    public static String getConfirmOrderBody(String orderId) {
        String message = "Đơn hàng " + orderId + " của bạn đã được chủ shop xác nhận có hàng" +
                " và đang được chuẩn bị để giao tới bạn. Hãy vào app để kiểm tra tình trạng của đơn hàng.";
        return message;
    }

    public static String getOrderTitle() {
        return "Thông báo đơn đặt hàng mới";
    }

    public static String getOrderBody(String userId) {
        return "Khách hàng " + userId + " đã đặt hàng của bạn. Hãy vào app để kiểm tra và xác nhận đơn hàng.";
    }

    public static String getReceivedOrderTitle() {
        return "Thông báo đơn hàng thành công";
    }

    public static String getReceivedOrderBody(String userId) {
        return "Khách hàng " + userId + " đã nhận được hàng. "
                + "Số tiền thanh toán của đơn hàng sẽ được gửi cho bạn trong vòng 24 giờ.";
    }

    public static Data createData(String title, String body) {
        return new Data(title, body);
    }

    public static class Notification {
        public String title;
        public String body;

        public Notification(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }

    public static class Data {
        public String title;
        public String body;

        public Data(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }
}
