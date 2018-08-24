package com.houz.chef.firebasepush;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.houz.chef.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    String msg;
    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            JSONObject json = new JSONObject(remoteMessage.getData().toString());
            JSONObject data = json.getJSONObject("data");
            JSONObject payload = data.getJSONObject("payload");
            msg = data.getString("message");
            String notification_type = payload.getString("notification_type");
            Log.e("Firebase response ",""+json.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(RemoteMessage messageBody, PendingIntent resultPendingIntent) {
        try {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("GateKasher")
                    .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSound(defaultSoundUri);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendImageNotification(RemoteMessage messageBody, String strlink, PendingIntent resultPendingIntent, Bitmap aBigBitmap) {
        try {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("GateKasher")
                    .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setContentIntent(resultPendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSound(defaultSoundUri)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(aBigBitmap).setSummaryText(msg+"\n"+strlink));

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public void showToast(final String msg)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),
                        msg,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}