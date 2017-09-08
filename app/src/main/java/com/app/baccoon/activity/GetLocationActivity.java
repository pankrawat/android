package com.app.baccoon.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.location.GPSTracker;
import com.app.baccoon.location.LocationCallBack;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.GPSManager;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.app.baccoon.utils.ToastUtil;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.util.List;
import java.util.Locale;

public class GetLocationActivity extends AppCompatActivity implements View.OnClickListener, LocationCallBack {


    private static final int REQUEST_CODE_LOCATION = 100;
    boolean hasLoc=false;
    private TextView btnManually;
    private LinearLayout btnGPS, btnWIFI;
    private TextView txtLocation;
    private SharedPreference sharedPreference;
    double Latitude,Longtitude;
    Intent intent;
    String strLat,strLng;
    private List<Address> addresses;
    String myLocation="";
    private SharedPreference prefs;
    private String place="";
    Button bb;
private Handler handler = new Handler();
    private Runnable r;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(r);
        Log.e("removed","bACK pRESS");
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(r);
        Log.e("removed","STOPPED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        sharedPreference = SharedPreference.getInstance(this);
       // sharedPreference.putString("FIRST_TIME", "False");
        initView();
    }

    private void initView() {

        btnManually = (TextView) findViewById(R.id.btnManually);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        btnGPS = (LinearLayout) findViewById(R.id.btnGPS);
        btnWIFI = (LinearLayout) findViewById(R.id.btnWIFI);

        btnManually.setOnClickListener(this);
        txtLocation.setOnClickListener(this);
        btnGPS.setOnClickListener(this);
        btnWIFI.setOnClickListener(this);
        if (sharedPreference.getString("FIRST_TIME", "True").equals("True")) {

            intent =new Intent(GetLocationActivity.this, SignupActivity.class);


        } else if (sharedPreference.getBoolean(SpKey.isLogged, false))
        {
            intent= new Intent(GetLocationActivity.this, HomeActivity.class);

        }
        else{

            intent= new Intent(GetLocationActivity.this, LoginActivity.class);

        }




    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnManually:

                if(txtLocation.getText().toString().equals(""))
              {
                  //Snackbar.make(fin,"Please select your location",Snackbar.LENGTH_SHORT).show();

                  Toast.makeText(this,"Please select your location",Toast.LENGTH_SHORT).show();

              }
                else
              {

                //  storeLocation(txtLocation.getText().toString());
                  double lat = Latitude;
                  if (lat != 0.0) {

                      hasLoc=true;

                      strLat=""+Latitude;
                      strLng=""+Longtitude;
                      intent.putExtra("LAT", ""+strLat);
                      intent.putExtra("LNG", "" + strLng);
                      strLat=""+Latitude;
                      strLng=""+Longtitude;
                      myLocation="" + strLat + "," +strLng;
                      storeLocation(myLocation);

                  }
              }
                break;


            case R.id.btnGPS:
                hasLoc=false;
                getLocationFromGPS();

                break;


            case R.id.btnWIFI:
                hasLoc=false;
                getLocationFromWifiCellID();

                break;

            case R.id.txtLocation:
                Intent intent =new Intent(GetLocationActivity.this,MapsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_LOCATION);


                break;



        }


    }

    private void getLocationFromGPS() {

        GPSManager gpsTracker=new GPSManager(this);
        if(gpsTracker.canGetLocation())
        {
            Common.showProgress(this);

            Log.e("Loaction Gps Lat",""+gpsTracker.getLatitude()+"  "+gpsTracker.getLatitude()
           );
            double lat = gpsTracker.getLatitude();


             r = new Runnable() {
                public void run() {
                    Common.hideProgress(GetLocationActivity.this);
                    ToastUtil.showShortToast(GetLocationActivity.this, "Location not found");
                }
            };
            handler.postDelayed(r, 60000);

            if(lat!=0.0)
            {
                hasLoc=true;
                Common.hideProgress(this);
                Latitude= gpsTracker.getLatitude();
                Longtitude=gpsTracker.getLongitude();

                strLat=""+Latitude;
                strLng=""+Longtitude;
                intent.putExtra("LAT", ""+strLat);
                intent.putExtra("LNG", "" + strLng);

                strLat=""+Latitude;
                strLng=""+Longtitude;
                myLocation="" + strLat + "," +strLng;
                getLocationName(Latitude,Longtitude);
                storeLocation(myLocation);
            }
           /* else
            {
                Common.hideProgress(this);
                ToastUtil.showShortToast(GetLocationActivity.this,"Location not found");
            }*/


        }
        else
        {
            gpsTracker.showSettingsAlert();
        }



        /*
        //1=GPS, 2=Wifi
        GPSTracker gpsTracker=new GPSTracker(this,1,this);
        if(gpsTracker.canGetLocation())
        {
            Common.showProgress(this);

          Log.e("Loaction Gps Lat",""+gpsTracker.getLatitude()+"  "+gpsTracker.getLatitude());
          double lat=  gpsTracker.getLatitude();
            if(lat!=0.0)
            {
                hasLoc=true;
                Common.hideProgress(this);
                Latitude= gpsTracker.getLatitude();
                Longtitude=gpsTracker.getLongitude();

                strLat=""+Latitude;
                strLng=""+Longtitude;
                intent.putExtra("LAT", ""+strLat);
                intent.putExtra("LNG", "" + strLng);

                strLat=""+Latitude;
                strLng=""+Longtitude;
                myLocation="" + strLat + "," +strLng;
                storeLocation(myLocation);


            }


        }
        else
        {
            gpsTracker.showSettingsAlert("GPS Settings","GPS is not enabled. Do you want to go to settings menu?");
        }

*/


    }

    private void getLocationName(double latitude, double longtitude) {

        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longtitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0);
            sharedPreference.putString(SpKey.Place, address);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getLocationFromWifiCellID() {


        //1=GPS, 2=Wifi
        GPSTracker gpsTracker=new GPSTracker(this,2,this);
        if(gpsTracker.canGetLocation()) {
            Common.showProgress(this);
            Log.e("Loaction Gps Lat", "" + gpsTracker.getLatitude() + "  " +
                    gpsTracker.getLatitude());


            double lat = gpsTracker.getLatitude();


            if (lat != 0.0) {
                hasLoc=true;
                Common.hideProgress(this);
                Latitude = gpsTracker.getLatitude();
                Longtitude = gpsTracker.getLongitude();
                strLat=""+Latitude;
                strLng=""+Longtitude;
                intent.putExtra("LAT", ""+strLat);
                intent.putExtra("LNG", "" + strLng);

                myLocation="" + strLat + "," +strLng;
                getLocationName(Latitude,Longtitude);
                storeLocation(myLocation);

            }
        }
        else
        {
            gpsTracker.showSettingsAlert("Newtork Settings","Network Location Service is not enabled. Do you want to go to settings menu?");

        }





    }

    private void storeLocation(String loc) {


        sharedPreference.putString(SpKey.LatLng,loc);
        sharedPreference.putString(SpKey.Lat, strLat);
        sharedPreference.putString(SpKey.Lng,strLng);
      //  sharedPreference.putString(SpKey.Place, place);

        Toast.makeText(this, "Your location set successfully.",Toast.LENGTH_SHORT).show();
       startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_LOCATION && resultCode==RESULT_OK)
        {

            if(data!=null)
            {
               String lat=  data.getStringExtra("LATITUDE");

                Latitude=Double.parseDouble(data.getStringExtra("LATITUDE"));
               String lng=  data.getStringExtra("LONGTITUDE");
                Longtitude=Double.parseDouble(data.getStringExtra("LONGTITUDE"));
                myLocation=""+lat+","+lng;
                place=""+data.getStringExtra("LOCATION");
            txtLocation.setText(""+data.getStringExtra("LOCATION"));
            }
        }



    }

    @Override
    public void onGetLocation(Location location) {


        Log.e("Callback loc",""+location.getLatitude());
        Log.e("Callback loc",""+location.getProvider());
double lat=location.getLatitude();
        if (lat !=0.0) {
            if(!hasLoc) {

                if (Common.isProgressShowing(this)) {
                    Common.hideProgress(this);
                }
                Latitude = location.getLatitude();
                Longtitude = location.getLongitude();
                intent.putExtra("LAT", ""+strLat);
                intent.putExtra("LNG", "" + strLng);
                strLat=""+Latitude;
                strLng=""+Longtitude;
                myLocation="" + strLat + "," +strLng;
                storeLocation(myLocation);
               // Toast.makeText(this, "Your location set successfully.",Toast.LENGTH_SHORT).show();

            }
        }



    }
}
