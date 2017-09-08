package com.app.baccoon.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.adapter.AutoLocationAdapter;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
;
import com.app.baccoon.utils.ToastUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

   // private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(new  LatLng(13.5944192,74.0367149), new LatLng(30.9770136,83.255305));
    private GoogleMap mMap;
    private AutoCompleteTextView search_address;
    private AutoLocationAdapter adapter;
    private ArrayList<PlaceAutocomplete> updatedList;
    private MapsActivity context;
    private LatLng ltlng=null;
    private GoogleApiClient mGoogleApiClient;
    private  ImageView btnBack;

    private TextView btnContinue,btnSkip;
    private SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        context=this;

        sharedPreference=SharedPreference.getInstance(context);
        search_address = (AutoCompleteTextView) findViewById(R.id.search_address);
        search_address.addTextChangedListener(searchTextWatcher);
        adapter = new AutoLocationAdapter(this, R.layout.place_textview_layout);
        btnBack = (ImageView)findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        btnContinue = (TextView)findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ltlng!=null) {
                    Intent intent = new Intent();

                    intent.putExtra("LATITUDE", "" + ltlng.latitude);
                    intent.putExtra("LONGTITUDE", "" + ltlng.longitude);
                    intent.putExtra("LOCATION",""+search_address.getText().toString());


//    sharedPreference.putString(SpKey.LatLng, "" + ltlng.latitude + "," + ltlng.latitude);
//    sharedPreference.putString(SpKey.Lat, "" + ltlng.latitude);
//    sharedPreference.putString(SpKey.Lng, "" + ltlng.longitude);




                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    Toast.makeText(context,"Please select a location.",Toast.LENGTH_SHORT).show();
                }


            }
        });
        // btnSkip = (TextView)findViewById(R.id.btnSkip);
//        btnSkip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//    Intent intent = new Intent();
//
//
//
//
//    setResult(RESULT_CANCELED, intent);
//    finish();
//
//
//            }
//        });
//


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).enableAutoManage(this, 0, this).addConnectionCallbacks(this).build();
        }

        adapter.setNotifyOnChange(true);
        search_address.setAdapter(adapter);
        search_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String description = updatedList.get(position).description.toString();
                getLatLngFromPlaceId(updatedList.get(position).placeId.toString(), description);
                //postCarAdBean.address = description;
                search_address.clearFocus();
                Common.hideSoftKeyBoard(context);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);




        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    TextWatcher searchTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count % 2 == 1) {
                adapter.clear();
               /* GetPlaces task = new GetPlaces();
                task.execute(search_address.getText().toString());*/
               try {
                   getPlaceSearch();
               }catch(UnsupportedEncodingException e)
               {
                   e.printStackTrace();
               }
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }




    private void getPlaceSearch() throws UnsupportedEncodingException {


         String hint=search_address.getText().toString();
      //  hint = hint.replaceAll(" ","%20");
        //hint = hint.replaceAll(",","%2C");

      hint= URLEncoder.encode(hint,"UTF-8");
        Log.e("hhh", "" + hint);
                String url="https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+hint+"&radius=5000&key=AIzaSyCmGY0R01XlRWvVpl2LScTGTLPHQ1sLXfw";


        if (Common.isConnected(context)) {


            Ion.with(this)
                    .load(url)
                    .setTimeout(60 * 1000)
                    .asString()
                    .setCallback(new FutureCallback<String>() {

                        @Override
                        public void onCompleted(Exception e, String jsonString) {
                            Log.d("calllll", "callllll");


                            if (e == null) {
                                if (jsonString != null && jsonString != "") {
                                    Log.e("search data", "" + jsonString);

                                    try {
                                        ArrayList<PlaceAutocomplete> result=new ArrayList<PlaceAutocomplete>();
                                        JSONObject resultObj = new JSONObject(jsonString);
                                       JSONArray predictions= resultObj.getJSONArray("predictions");

                                        for (int i=0; i<predictions.length();i++)
                                        {
result.add(new PlaceAutocomplete(predictions.getJSONObject(i).getString("place_id"),predictions.getJSONObject(i).getString("description")));

                                        }

                                        onDataGet(result);






                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        ToastUtil.showShortToast(context, "Network Error");

                                    }


                                }

                            } else {

                                e.printStackTrace();
                                ToastUtil.showShortToast(context, "Network Error");

                            }


                        }
                    });


        }

    }




    class GetPlaces extends AsyncTask<String, Void, ArrayList<PlaceAutocomplete>> {

        @Override
        protected ArrayList<PlaceAutocomplete> doInBackground(String... args) {

            ArrayList<PlaceAutocomplete> predictionsArr = new ArrayList<PlaceAutocomplete>();

            try {
                predictionsArr = getAutocomplete(args[0]);

            } catch (Exception e) {

            }

            return predictionsArr;

        }

        @Override
        protected void onPostExecute(ArrayList<PlaceAutocomplete> result) {

            // update the adapter
            if (result != null) {
                adapter = new AutoLocationAdapter(context, R.layout.place_textview_layout);
                adapter.setNotifyOnChange(true);
                // attach the adapter to textview
                search_address.setAdapter(adapter);
                updatedList = result;
                for (int i = 0; i < result.size(); i++) {
                    PlaceAutocomplete prediction = result.get(i);
                    adapter.add(prediction.description.toString());
                    adapter.notifyDataSetChanged();

                }
            }
        }
    }


    protected void onDataGet(ArrayList<PlaceAutocomplete> result) {

        // update the adapter
        if (result != null) {
            adapter = new AutoLocationAdapter(context, R.layout.place_textview_layout);
            adapter.setNotifyOnChange(true);
            // attach the adapter to textview
            search_address.setAdapter(adapter);
            updatedList = result;
            for (int i = 0; i < result.size(); i++) {
                PlaceAutocomplete prediction = result.get(i);
                adapter.add(prediction.description.toString());
                adapter.notifyDataSetChanged();

            }
        }
    }


    private ArrayList<PlaceAutocomplete> getAutocomplete(CharSequence constraint) {
        if (mGoogleApiClient.isConnected()) {

            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                    .build();


            PendingResult<AutocompletePredictionBuffer> results = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, constraint.toString(),null, typeFilter);

            AutocompletePredictionBuffer autocompletePredictions = results.await(60, TimeUnit.SECONDS);
            // Confirm that the query completed successfully, otherwise return
            // null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                autocompletePredictions.release();
                return null;
            }

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList<PlaceAutocomplete> locationsArray = new ArrayList<>();
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                locationsArray.add(new PlaceAutocomplete(prediction.getPlaceId(), prediction.getFullText(null)));
            }
            autocompletePredictions.release();
            return locationsArray;
        }
        return null;
    }

    public void getLatLngFromPlaceId(String placeId, final String address) {
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            final Place myPlace = places.get(0);
                            LatLng queried_location = myPlace.getLatLng();
                            if (queried_location != null && address != null && !address.equals("")) {
                                ltlng = queried_location;
                                Log.e("My LatLng=", "" + ltlng);
                                //setMap();
                                showPlaceonMap(ltlng);

                            }
                        }
                        places.release();
                    }
                });
    }

    private void showPlaceonMap(LatLng latlng) {

        if(mMap!=null) {
            mMap.addMarker(new MarkerOptions().position(latlng).title(search_address.getText().toString()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

        }

    }


    public class PlaceAutocomplete {

        public CharSequence placeId;
        public CharSequence description;

        PlaceAutocomplete(CharSequence placeId, CharSequence description) {
            this.placeId = placeId;
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }
}