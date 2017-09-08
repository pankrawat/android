package com.app.baccoon.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.adapter.NothingSelectedSpinnerAdapter;
import com.app.baccoon.bean.FilterBean;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;


public class FilterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Spinner spinerCategory, spinerSortBy, spinerRadius;
    private String CATEGORY_SPINER_LABLE = "Select a Category";
    private String SORTBY_SPINER_LABLE = "Sort By";
    private String Radius_SPINER_LABLE = "Select Radius";
    ArrayAdapter<String> adapterCategory, adapterSoryBy, adapterRadius;
    private Toolbar toolbar;
    private ImageView btnBack;
    private TextView btnSearch, txtLocation, btnClear;
    private EditText priceTo, priceFrom;
    private final int LOCATION_REQUEST_CODE = 111;
    private String category = "";
    private String sortby = "";
    private String radius = "";
    private String lat = "", lng = "";
    FilterBean filterBean;
    Intent i;
    ImageView iconBack;
    String[] catArray = new String[]{"Footwear", "Mobiles", "Mobile Accessories", "Art and Antiques", "Fashion and Accessories",
            "Cars and Motors", "Bikes and Scooter", "Books,Films and Music", "Audio & Video", "Televisions", "Computer and Electronics", "SmartWatches and Wearables", "Games World", "Sports and Fitness", "Smart Home Appliances", "Kitchen Appliances", "Furniture and Tables", "House and Office", "Baby and Children", "Beauty and Wellness", "Health Care", "Jewellery", "Animal's Accessories", "Others"};
    String[] sortArray = new String[]{"Newest First", "Popularity", "Price low-to-high", "Price high-to-low"};
    String[] radiusArray = new String[]{"1", "10", "20", "30", "40", "50"};
    //private String oldLocation = "";
    //private String oldLat = "";
    //private String oldLng = "";
    Context ctx;
    private SharedPreference prefs;
    private String locationPlace = "";
    //  private String oldPlace = "";
    private boolean allFieldBlank = false;


    private String locationNew = "", latNew = "", lngNew = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        i = new Intent();

        ctx = this;
        prefs = SharedPreference.getInstance(ctx);
//        oldLat = prefs.getString(SpKey.Lat, "");
//        oldLng = prefs.getString(SpKey.Lng, "");
//        oldPlace = prefs.getString(SpKey.Place, "");
        locationNew = prefs.getString(SpKey.Place, "");


        filterBean = FilterBean.getInstance();
        if (filterBean.getLocation().equals("")) {
            filterBean.setLocation(prefs.getString(SpKey.Place, ""));
            filterBean.setLat(prefs.getString(SpKey.Lat, ""));
            filterBean.setLng(prefs.getString(SpKey.Lng, ""));
        }

        spinerCategory = (Spinner) findViewById(R.id.spinerCategory);
        spinerSortBy = (Spinner) findViewById(R.id.spinerSortBy);
        spinerRadius = (Spinner) findViewById(R.id.spinerRadius);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnSearch = (TextView) findViewById(R.id.btnSearch);
        btnClear = (TextView) findViewById(R.id.btnClear);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        priceFrom = (EditText) findViewById(R.id.txtPriceFrom);
        priceTo = (EditText) findViewById(R.id.txtPriceTo);
        iconBack = (ImageView) findViewById(R.id.btnBack);


        txtLocation.setText(""+prefs.getString(SpKey.Place,""));
        iconBack.setOnClickListener(this);
        txtLocation.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        spinerCategory.setOnItemSelectedListener(this);
        spinerSortBy.setOnItemSelectedListener(this);
        spinerRadius.setOnItemSelectedListener(this);
        priceTo.clearFocus();
        priceFrom.clearFocus();


        // Set a Toolbar to replace the ActionBar.
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        //set values for category spiner
        adapterCategory = new ArrayAdapter<String>(this, R.layout.textview_layout_spiner, catArray);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerCategory.setPrompt("Select a Category");
        spinerCategory.setAdapter(new NothingSelectedSpinnerAdapter(adapterCategory, R.layout.spiner_default_text_layout, this, CATEGORY_SPINER_LABLE));


        //set values for sortby spiner
        adapterSoryBy = new ArrayAdapter<String>(this, R.layout.textview_layout_spiner, sortArray);
        adapterSoryBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerSortBy.setPrompt("Sort By");
        spinerSortBy.setAdapter(new NothingSelectedSpinnerAdapter(adapterSoryBy, R.layout.spiner_default_text_layout, this, SORTBY_SPINER_LABLE));


        //set values for sortby spiner
        adapterRadius = new ArrayAdapter<String>(this, R.layout.textview_layout_spiner, radiusArray);
        adapterRadius.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerRadius.setPrompt("Select Radius");
        spinerRadius.setAdapter(new NothingSelectedSpinnerAdapter(adapterRadius, R.layout.spiner_default_text_layout, this, Radius_SPINER_LABLE));
        txtLocation.setText(filterBean.getLocation());

        setOldDataInFilter();

        // setOldValuesFromFilterBean();


    }

    private void setOldDataInFilter() {

        spinerCategory.setSelection(filterBean.getCategory());
        spinerRadius.setSelection(filterBean.getRadius());
        spinerSortBy.setSelection(filterBean.getSortby());
        priceTo.setText(filterBean.getPriceTo());
        priceFrom.setText(filterBean.getPriceFrom());
        if (!filterBean.getLocation().equalsIgnoreCase("")) {
            txtLocation.setText(filterBean.getLocation());


        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnBack:
//                i.putExtra("defaultLocation",true);
//                setResult(RESULT_OK, i);
                finish();
                break;

            case R.id.btnSearch:


                String msg = "";
                if ((category.equals("") && sortby.equals("") && radius.equals("") && priceTo.getText().toString().equals("") && priceFrom.getText().toString().equals(""))) {
                    // Log.e("Category",category);

                    //  msg = "Please select one filter.";
                    allFieldBlank = true;
                } else if (!priceTo.getText().toString().replaceAll(" ", "").equals("") && (Integer.parseInt(priceTo.getText().toString()) <= 0)) {

                    msg = "Please enter valid price";

                } else if (!priceFrom.getText().toString().replaceAll(" ", "").equals("") && (Integer.parseInt(priceFrom.getText().toString()) <= 0)) {

                    msg = "Please enter valid price";
                } else if ((!priceTo.getText().toString().equals("") && priceFrom.getText().toString().equals("")) || (priceTo.getText().toString().equals("") && !priceFrom.getText().toString().equals(""))) {
                    msg = "Please enter both prices.";
                } else {
                    if ((!priceFrom.getText().toString().replaceAll(" ", "").equals("")) && (!priceTo.getText().toString().replaceAll(" ", "").equals(""))) {
                        int to = Integer.parseInt(priceTo.getText().toString());
                        int from = Integer.parseInt(priceFrom.getText().toString());
                        if (to < from) {
                            msg = "End price must be greater than Start price.";
                        }
                    }
                }
                if (!msg.equals("")) {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                } else {
                    i.putExtra("priceTo", priceTo.getText().toString());
                    i.putExtra("priceFrom", priceFrom.getText().toString());
                    i.putExtra("Category", category);
                    i.putExtra("Radius", radius);
                    i.putExtra("sortBy", sortby);
                    i.putExtra("Lat", latNew);
                    i.putExtra("Lng", lngNew);
                    i.putExtra("Place", locationNew);

                    if (allFieldBlank && locationNew.equalsIgnoreCase(filterBean.getLocation())) {
                        i.putExtra("defaultLocation", true);
                        setResult(RESULT_OK, i);
                    } else {
                        setResult(RESULT_OK, i);
                        setValuesInFilterBean();
                    }
                    finish();
                }
                break;

            case R.id.txtLocation:
                Intent inLocation = new Intent(FilterActivity.this, MapsActivity.class);
                startActivityForResult(inLocation, LOCATION_REQUEST_CODE);
                break;

            case R.id.btnClear:
                clearFilter();
                break;


        }

    }

    private void setValuesInFilterBean() {
        if (locationNew.equalsIgnoreCase(""))

        {
//locationNew=oldLocation;
//    latNew=oldLat;
//    lngNew=oldLng;
            locationNew = prefs.getString(SpKey.Place, "");
            ;
            latNew = prefs.getString(SpKey.Lat, "");
            ;
            lngNew = prefs.getString(SpKey.Lng, "");
            ;

        }


        filterBean.setValues(spinerRadius.getSelectedItemPosition(),
                spinerSortBy.getSelectedItemPosition(), spinerCategory.getSelectedItemPosition(),
                priceTo.getText().toString(), priceFrom.getText().toString(), locationNew, latNew, lngNew);

    }

    private void clearFilter() {

        //     adapterCategory.clear()

        // spinerCategory.setAdapter(new NothingSelectedSpinnerAdapter(adapterCategory, R.layout.spiner_default_text_layout, this, CATEGORY_SPINER_LABLE));

        spinerCategory.setSelection(0);
        spinerSortBy.setSelection(0);
        spinerRadius.setSelection(0);
        sortby = "";
        category = "";
        radius = "";
        priceFrom.setText("");
        priceTo.setText("");
        txtLocation.setText(filterBean.getLocation());
        priceTo.clearFocus();
        priceFrom.clearFocus();
        filterBean.setValues(0, 0, 0, "", "", prefs.getString(SpKey.Place, ""),
                prefs.getString(SpKey.Lat, ""), prefs.getString(SpKey.Lng, ""));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                lat = data.getStringExtra("LATITUDE");
                latNew = lat;


                lng = data.getStringExtra("LONGTITUDE");
                lngNew = lng;

                locationNew = "" + data.getStringExtra("LOCATION");

                txtLocation.setText("" + data.getStringExtra("LOCATION"));
                Log.e("lat-", lat + ",   lng-" + lng);
            }

        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        Spinner spinerCat = (Spinner) parent;
        Spinner spinerSrtBy = (Spinner) parent;
        Spinner spinerRdius = (Spinner) parent;
        if (spinerCat.getId() == R.id.spinerCategory) {
            if (position > 0) {
                category = spinerCategory.getItemAtPosition(position).toString();
            }


            //  Log.e("category", "" + catArray[position-1]+" "+position);
        }
        if (spinerSrtBy.getId() == R.id.spinerSortBy) {
            if (position > 0) {
                sortby = spinerSortBy.getItemAtPosition(position).toString();
            }
            Log.e("sort", "" + sortby);

        }
        if (spinerRdius.getId() == R.id.spinerRadius) {
            // Log.e("radius", "" + radiusArray[position-1]+" "+position);
            if (position > 0) {
                radius = spinerRadius.getItemAtPosition(position).toString();
            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
