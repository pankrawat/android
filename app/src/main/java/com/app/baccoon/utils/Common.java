package com.app.baccoon.utils;

import android.R.bool;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/**
 * Created by admin on 12/23/2015.
 */
public class Common {
    private static ProgressDialog progress;

    public static void showToast(Context ctx,String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            return true;
        } else {
            showToast(context,"No internet connection");
            return false;
        }
    }
    
    
    
//
//    public static boolean isLocationEnabled(Context context) {
//        int locationMode = 0;
//        String locationProviders;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            try {
//                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
//
//            } catch (Settings.SettingNotFoundException e) {
//                e.printStackTrace();
//            }
//
//            return locationMode != Settings.Secure.LOCATION_MODE_OFF && locationMode != Settings.Secure.LOCATION_MODE_SENSORS_ONLY;
//
//        } else {
//            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//
//            return !TextUtils.isEmpty(locationProviders) && locationProviders.contains("network") && locationProviders.contains(AppConstants.GPS);
//        }
//    }
    
    
    public static boolean isInternetOn(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            return true;
        } else {
            
            return false;
        }
    }
    
    
    


    public static void hideSoftKeyBoard(Activity context) {
        View focusedView = context.getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    public static void showSoftKeyBoard(Context context) {
        View focusedView = ((Activity) context).getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
        }
    }



    public static boolean checkDate(String chooseDate)
    {
        boolean result=false;
      final  Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(calendar.getTime());
        try {
            Date currentDate = df.parse(formattedDate);
//Date newDate=df.parse()
//            if ()
//            {
//
//              result= false;
//
//            }
//            else {
//                result= true;
//
//
//            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }


   public static void showProgress(Context ctx)
    {
        progress = new  ProgressDialog(ctx);
        progress.setMessage("Please Wait ");

        progress.setCancelable(false);
        progress.show();


    }

    public static void hideProgress(Context ctx)
    {
       progress.dismiss();


    }


    public static boolean isProgressShowing(Context ctx)
    {
boolean isShowing=false;

        if(progress.isShowing())
        {
            isShowing=true;
        }

return isShowing;
    }
    public static String roundOffTo2DecPlaces(Float val)
    {
        return String.format("%.2f", val);
    }
    public static boolean CheckEnableGPS(Context ctx){
        String provider = Settings.Secure.getString(ctx.getContentResolver(),
          Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
           if(!provider.equals("")){
        	   
        	   if(provider.contains("gps")){
        	         //GPS Enabled
        	         Log.e("GPS provider","yes");
        	         showToast(ctx, "Please enable GPS.");
        	            return true;
          		 } 
        	   else
        	   {  showToast(ctx, "Please enable GPS.");
        		   return false;
        	   }
          	   
      
           }else{
        	   
          //  Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
            //   ctx.startActivity(intent);
               return false;
           }

       }


}




