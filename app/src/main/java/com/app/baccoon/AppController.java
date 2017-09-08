package com.app.baccoon;

import android.content.Context;

import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import java.io.File;

public class AppController extends android.app.Application {

    public static File extStorageAppBasePath;
    public static File extStorageAppCachePath;
    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                Log.e("VKAccessToken invalid","no");
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this).withPayments();

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // Retrieve the base path for the application in the external storage
            File externalStorageDir = Environment.getExternalStorageDirectory();

            if (externalStorageDir != null) {
                // {SD_PATH}/Android/data/com.devahead.androidwebviewcacheonsd
                extStorageAppBasePath = new File(externalStorageDir.getAbsolutePath() +
                        File.separator + "Android" + File.separator + "data" +
                        File.separator + getPackageName()+ File.separator+"urendImages");
            }

            if (extStorageAppBasePath != null) {
                // {SD_PATH}/Android/data/com.devahead.androidwebviewcacheonsd/cache
                extStorageAppCachePath = new File(extStorageAppBasePath.getAbsolutePath() +
                        File.separator + "cache");

                boolean isCachePathAvailable = true;

                if (!extStorageAppCachePath.exists()) {
                    // Create the cache path on the external storage
                    isCachePathAvailable = extStorageAppCachePath.mkdirs();
                }

                if (!isCachePathAvailable) {
                    // Unable to create the cache path
                    extStorageAppCachePath = null;
                }
            }
        }

    }



    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }



    @Override
    public File getCacheDir() {
        if (extStorageAppCachePath != null) {
            return extStorageAppCachePath;
        } else {
            return super.getCacheDir();
        }


    }
}