package com.HelloExperts.HelloExpert.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;

import com.HelloExperts.HelloExpert.Question_view;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import com.HelloExperts.HelloExpert.dashboard;
import com.HelloExperts.HelloExpert.app.Config;
import com.HelloExperts.HelloExpert.util.NotificationUtils;

/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        Log.d("this","message");
        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
          //  notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String timestamp = data.getString("timestamp");
            String question_id = data.getString("question_id");
            String question_status = data.getString("question_status");
            String notification_type = data.getString("notification_type");



//            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//
//                Bundle b1 =new Bundle();
//                b1.putString("message",message);
//                b1.putInt("question_id",Integer.parseInt(question_id));
//
//                pushNotification.putExtras(b1);
//
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//                // play notification sound
//                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                notificationUtils.playNotificationSound();
//            } else {
                // app is in background, show the notification in notification tray

                Intent resultIntent;
                Bundle b = new Bundle();
            if(notification_type.equals("0")){
                resultIntent = new Intent(getApplicationContext(), Question_view.class);

                b.putInt("question_id",Integer.parseInt(question_id));
                b.putBoolean("notification_from",true);
                b.putBoolean("show_status",false);
                b.putBoolean("This_is_already_locked_question",false);
                b.putString("from_recent","false");
            }else if(notification_type.equals("1")){

                resultIntent = new Intent(getApplicationContext(), Question_view.class);

                b.putInt("question_id",Integer.parseInt(question_id));
                b.putBoolean("show_status",true);
                b.putString("from_recent","false");
                b.putString("status",question_status);


            }else{
                resultIntent = new Intent(getApplicationContext(), dashboard.class);
                b.putString("message",message);
              //  b.putInt("question_id",Integer.parseInt(question_id));
              //  b.putString("notification_from",question_id);
            }
                resultIntent.putExtras(b);
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
           // }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
}
