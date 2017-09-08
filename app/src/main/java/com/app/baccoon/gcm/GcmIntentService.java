package com.app.baccoon.gcm;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.baccoon.R;
import com.app.baccoon.activity.HomeActivity;
import com.app.baccoon.utils.SharedPreference;

import com.app.baccoon.utils.SpKey;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;


public class GcmIntentService extends IntentService {
    private final String NOTIFICATION_ID = "notification _id";


    private String post_id, title;

    private Context context;
    private SharedPreference sp;

    Bundle extras;



    public GcmIntentService() {

        super("GcmIntentService");
        Log.d("Push intent service", "called");

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String myNot = "";
        extras = intent.getExtras();
        sp= SharedPreference.getInstance(getApplicationContext());


              GoogleCloudMessaging cloud = GoogleCloudMessaging.getInstance(this);
              //	prefs=ReferenceWrapper.getInstance(this).getPreferences();
              String messageType = cloud.getMessageType(intent);


                   if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

              if(sp.getBoolean(SpKey.isLogged, false))
              {sendNotification(""+extras.getString("msg"));                  }



        }

        GcmReceiver.completeWakefulIntent(intent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(String msg) {
        context = getApplicationContext();

        int notifyId = sp.getInt(NOTIFICATION_ID, 0);
        if (notifyId > 1000) {
            sp.putInt(NOTIFICATION_ID, 0);
        } else {
            sp.putInt(NOTIFICATION_ID, notifyId + 1);
        }


        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent=null ;
        JSONObject jsonObject=null;
        String pushMsg="";
        try {
            jsonObject= new JSONObject(msg);

            pushMsg=jsonObject.getString("message");

            Log.e("GCMService json",""+jsonObject.toString());




        } catch (JSONException e) {
            e.printStackTrace();
            pushMsg="no message";
        }

        intent = new Intent(this, HomeActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pIntent = PendingIntent.getActivity(this, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

       NotificationCompat.BigTextStyle bigTextStyle= new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(""+pushMsg);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Notification notification = mBuilder.setContentTitle("Baccoon Information")
               .setPriority(NotificationCompat.PRIORITY_MAX)

                .setTicker("Baccoon Notification")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pIntent)
                .setSmallIcon(getNotificationIcon())

                .setStyle(bigTextStyle)
                        //.setStyle(new Notification.BigPictureStyle()
                        //.bigPicture(getOfferImage()).setSummaryText(OFFER_DESC))
                        .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        notificationManager.notify(notifyId, notification);
        Log.d("Push Message", "" + msg);
    }


    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.app_icon : R.mipmap.app_icon;
    }

}
