package com.app.baccoon.activity;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.app.baccoon.R;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

public class SplashActivity extends AppCompatActivity {


    // splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private SharedPreference pref;
    private boolean isPause = false;
    private Handler handler;
    private boolean checkThreadStart = false;
    private static int counter = 0;
    private boolean splashLoaded = false;
    private GoogleCloudMessaging gcm;
    public static final String SENDER_ID = "219483859213";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9001;
    private String regId;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
ctx=this;
        pref = SharedPreference.getInstance(this);


        class Multi extends Thread {
            public void run() {
                System.out.println("Thread running");
                try {
                    init();
                }catch (Exception e)
                {e.printStackTrace();}
            }
        }
        Multi m1=new Multi();
        m1.run();


        handler = new Handler();
        handler.postDelayed(mRunnable, 2000);


    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            splashLoaded = true;

            moveToNextScreen();


        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        checkThreadStart = false;
        if (splashLoaded == true) {
            if (counter == 0) {
                moveToNextScreen();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkThreadStart = true;
    }


    private void moveToNextScreen() {
        if (!checkThreadStart) {


            SharedPreference prefs = SharedPreference.getInstance(this);


            if (pref.getString(SpKey.LatLng, "").equals("")) {
                startActivity(new Intent(SplashActivity.this, GetLocationActivity.class));
                finish();
            } else {
                if (prefs.getString("FIRST_TIME", "True").equals("True")) {

                    startActivity(new Intent(SplashActivity.this, SignupActivity.class));
                    finish();

                } else if (prefs.getBoolean(SpKey.isLogged, false) && !prefs.getBoolean(SpKey.onPaymentScreen, false))
                {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                } else if (prefs.getBoolean(SpKey.onPaymentScreen, false))
                {

                    startActivity(new Intent(SplashActivity.this, PaymentActivity.class).putExtra("fromSignup", true));

                    finish();
                }
                else{

                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }

            }

            counter = 1;
        } else {
            counter = 0;
        }
    }

    private void init() {
        try {
            if (checkPlayServices() && Common.isInternetOn(ctx)) {
                gcm = GoogleCloudMessaging.getInstance(ctx);
                regId = getRegistrationId(ctx);

                if (regId.isEmpty()) {
                    registerInBackground();
                } else {
                    //sharedPreference.commitStringValue(SyncStateContract.Constants.DEVICE_TOKEN, regId);
                    String device_token = regId;
                    Log.e("splash", "device_token= " + device_token);

                   // registerTokenToServer(regId);
                }
            }
        }catch(Exception e)
        {e.printStackTrace();}
    }
    //APA91bGDwAvVQ1rI8b56qjsN1eN6MgHY-8ero3W5WgJ_oQHD3n4eE4CEOt0HCMmCOOIzV9lhd0utEhj205VpyOd7fM4eVRYWjcA5WjFN_W07Y8LP-rYWEZvYQJHv5geKP8PUDVwF3qy3

    /*private void registerTokenToServer(String regId) {
        pref.putString(SpKey.DeviceToken, regId);
        Log.e("Splash","In registraton");
        Ion.with(getApplicationContext())
                .load(API.registerTokenForGcm)
                .setBodyParameter("token", regId)
//                .setBodyParameter("prevToken", "")
                .setBodyParameter("os", "Android")
                        //.setBodyParameter("email", "gopalgupta93@gmail.com")
                        // .setBodyParameter("userCategory", "0")
                .setBodyParameter("lang", "en")

                .asString().withResponse()
                .setCallback(new FutureCallback<Response<String>>() {


                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        Log.d("onComplete", "home fragment");

                        if (e == null) {

                            if (result != null && result.getHeaders().code() == 200) {
                                Log.e("In result", "got result");
                                Log.e("No Exception. Data is", "");


                            }
                        } else {

                            e.printStackTrace();

                            // mSwipyRefreshLayout.setRefreshing(false);

                            Log.e("Exception", "In Exception");
                        }


                    }
                });


    }
*/


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.e("", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {

        String registrationId = pref.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = pref.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }

        return registrationId;
    }

//    private SharedPreferences getGCMPreferences(Context context) {
//        // This sample app persists the registration ID in shared preferences,
//        // but
//        // how you store the regID in your app is up to you.
//        Log.e("getGCMPreferences", "package= " + context.getPackageName());
//        return getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
//    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(ctx);
                    }
                    regId = gcm.register(SENDER_ID);

                    msg = "Device registered, registration ID=" + regId;
                    Log.e("Push Token",regId);


                    storeRegistrationId(ctx, regId);
                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (regId != null && regId.length() > 0) {
                    // prefs.edit().putString(Common.Push_Token, regId).commit();
                    Log.e("Push Token",regId);
                    pref.putString(SpKey.DeviceToken, regId);

                } else {

                }
            }
        }.execute();
    }

    private void storeRegistrationId(Context context, String regId) {

        int appVersion = getAppVersion(context);
        pref.putString(PROPERTY_REG_ID, regId);
        pref.putString(SpKey.DeviceToken, regId);
        pref.putInt(PROPERTY_APP_VERSION, appVersion); ;

    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }



}
