package com.nvcomputers.ten;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.views.home.LandingActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class GCMPushReceiverService extends GcmListenerService {

    //This method will be called on every new message received
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Getting the message from the bundle
        String currentUserId = new SharedPrefsHelper(GCMPushReceiverService.this).get(PreferenceKeys.PREF_USER_ID, "");
        if (currentUserId == null && currentUserId.length() == 0) {
            return;
        }

        String message = data.getString("message");
        String user = data.getString("user");
        if (user != null && user.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(user);
                if (jsonObject.has("idUser")) {

                    String userId = jsonObject.getString("idUser");
                    if (currentUserId.equals(userId)) {
                        return;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        //Displaying a notiffication with the message
        sendNotification(message);
    }

    //This method is generating a notification and displaying the notification
    private void sendNotification(String message) {
        if (LandingActivity.landingActivity != null) {
            LandingActivity.landingActivity.showBadgeIcon();
        }

        Intent intent = new Intent(this, LandingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("from_notification", true);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.iclauncher_notification)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
    }
}
