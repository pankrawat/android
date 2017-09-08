package com.app.baccoon.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.activity.FilterActivity;
import com.app.baccoon.activity.ProductDetailsActivity;
import com.app.baccoon.adapter.FavGridAdapter;
import com.app.baccoon.adapter.NothingSelectedSpinnerAdapter;
import com.app.baccoon.bean.ProductBean;
import com.app.baccoon.bean.ProductBean;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private Context context;
    boolean gg;
    public boolean isFiltered = false;
    private String CATEGORY_SPINER_LABLE = "Select a Category";
    private String PRICE_SPINER_LABLE = "Price";
    ArrayAdapter<String> adapterCategory, adapterPrice;
    String[] catArray = new String[]{"Footwear", "Mobiles", "Mobile Accessories", "Art and Antiques", "Fashion and Accessories", "Cars and Motors", "Bikes and Scooter", "Books,Films and Music", "Audio & Video", "Televisions", "Computer and Electronics", "SmartWatches and Wearables", "Games World", "Sports and Fitness", "Smart Home Appliances", "Kitchen Appliances", "Furniture and Tables", "House and Office", "Baby and Children", "Beauty and Wellness", "Health Care", "Jewellery", "Animal's Accessories", "Others"};
    String[] priceArray = new String[]{"500", "1000", "5000", "10000"};
    private ArrayList<ProductBean> productList, backupList;
    private TextView txtDistance, txtError;
    String Latitude, Longtitude;
    SharedPreference prefs;
    FavGridAdapter custAdapter;
    GridView list;
    JsonObject jsonObject = new JsonObject();
    private String Lat = "", Lng = "";
    private Spinner spinerCategory, spinerPrice;
    private String category = "";
    private ImageView cross;
    private String price = "";


    @Override
    public void onResume() {
        super.onResume();

        if( getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent)  instanceof FavoriteFragment) {
            cross.setImageResource(R.mipmap.close);
            if(isFiltered)
                cross.setVisibility(View.VISIBLE);
            else
            cross.setVisibility(View.INVISIBLE);
        }
        else
                cross.setVisibility(View.VISIBLE);


        if (prefs.getBoolean("favReload", false)) {
            reloaddata();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        cross.setVisibility(View.VISIBLE);

      //  cross.setImageResource(R.mipmap.cross_popup);
        boolean x = true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, null);


        context = getActivity();

        prefs = SharedPreference.getInstance(context);
        Latitude = prefs.getString(SpKey.Lat, "");
        Longtitude = prefs.getString(SpKey.Lng, "");
        list = (GridView) view.findViewById(R.id.gridview1);
        backupList=new ArrayList<ProductBean>();
        cross= (ImageView) getActivity().findViewById(R.id.filter);
        cross.setImageResource(R.mipmap.close);
        cross.setVisibility(View.INVISIBLE);
        txtError = (TextView) view.findViewById(R.id.txtError);

        spinerCategory = (Spinner) view.findViewById(R.id.spinerCategory);
        spinerPrice = (Spinner) view.findViewById(R.id.spinerPrice);
     //   cross=(ImageView)view.findViewById(R.id.Remove_filter);

        cross.setOnClickListener(this);

        spinerCategory.setOnItemSelectedListener(this);
        spinerPrice.setOnItemSelectedListener(this);


        //set values for category spiner
        adapterCategory = new ArrayAdapter<String>(context, R.layout.textview_layout_spiner, catArray);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerCategory.setPrompt("By Product");
        spinerCategory.setAdapter(new NothingSelectedSpinnerAdapter(adapterCategory, R.layout.spiner_default_text_layout, context, CATEGORY_SPINER_LABLE));


        //set values for sortby spiner
        adapterPrice = new ArrayAdapter<String>(context, R.layout.textview_layout_spiner, priceArray);
        adapterPrice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerPrice.setPrompt("Price");
        spinerPrice.setAdapter(new NothingSelectedSpinnerAdapter(adapterPrice, R.layout.spiner_default_text_layout, context, PRICE_SPINER_LABLE));


        //  userId" : "10
        productList = new ArrayList<ProductBean>();
        jsonObject.addProperty("userId", prefs.getString("userid", prefs.getString(SpKey.UID, "")));

        if (Common.isConnected(context)) {
            Common.showProgress(context);
            Ion.with(context)
                    .load(API.Favorite_Product_List)
                    .setTimeout(60 * 1000)
                    .setJsonObjectBody(jsonObject)
                    .asString()
                    .setCallback(new FutureCallback<String>() {


                        @Override
                        public void onCompleted(Exception e, String jsonString) {
                            Log.d("call fav", "callllll");
                            Common.hideProgress(context);
                            list.setVisibility(View.VISIBLE);
                            txtError.setVisibility(View.INVISIBLE);
                            if (e == null) {
                                if (jsonString != null && jsonString != "") {

                                    Log.e("json2", jsonString);

                                    try {
                                        JSONObject resultObj = new JSONObject(jsonString);
                                        // JSONArray jsArray = new JSONArray(jsonString);
                                        //  Log.e("array path", "" + jsArray.getJSONObject(0).getString("image_title"));
                                        //  jsArray.getJSONObject(0).getString("status").equals("1");
                                        // ProductBean(String pid, String productName, String productPrice, String prod_category, String productImageUrl, String prod_fav, String prod_location, String prod_desc, String productPlace) {

                                        if (resultObj.getBoolean("isSuccess")) {
                                            JSONArray jsArray = new JSONArray(resultObj.getJSONArray("Products").toString());
                                            for (int i = 0; i < jsArray.length(); i++) {
                                                // productList.add(new AchievementsBean(jsArray.getJSONObject(i).getString("nid"), jsArray.getJSONObject(i).getString("title"), jsArray.getJSONObject(i).getString("image"), jsArray.getJSONObject(i).getString("description"),jsArray.getJSONObject(i).getString("thumbnail_path")));
                                                productList.add(new ProductBean(jsArray.getJSONObject(i).getString("prod_id"),
                                                        jsArray.getJSONObject(i).getString("prod_name"), jsArray.getJSONObject(i).getString("prod_price"),
                                                        jsArray.getJSONObject(i).getString("prod_category"), jsArray.getJSONObject(i).getString("prod_image"),
                                                        jsArray.getJSONObject(i).getString("prod_location"), jsArray.getJSONObject(i).getString("prod_desc"),
                                                        jsArray.getJSONObject(i).getString("by_postoffice_price"), jsArray.getJSONObject(i).getString("by_dhl_price")
                                                        , jsArray.getJSONObject(i).getString("by_postoffice"), jsArray.getJSONObject(i).getString("by_dhl")
                                                        , jsArray.getJSONObject(i).getString("put_by_buyer"), jsArray.getJSONObject(i).getString("prod_currency")
                                                        , jsArray.getJSONObject(i).getString("no_of_likes"), jsArray.getJSONObject(i).getInt("isLiked")
                                                        , 1, jsArray.getJSONObject(i).getString("sellerId"), jsArray.getJSONObject(i).get("isSold").toString(),1));
                                            }
                                            custAdapter = new FavGridAdapter(getActivity(), productList);

                                            list.setAdapter(custAdapter);
                                            txtError.setVisibility(View.INVISIBLE);
                                            list.setVisibility(View.VISIBLE);
                                        }

                                        //        [{"status":"1","message":"Thank you! for Submitting Appointment Request ."}]

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                                    }


                                }

                            } else {

                                e.printStackTrace();
                                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();

                            }


                        }
                    });
        }


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("ProductBean", productList.get(position));
                intent.putExtra("frg", "FAV");
                startActivity(intent);
                if(isFiltered)
                    cross.setVisibility(View.VISIBLE);
                else
                    cross.setVisibility(View.INVISIBLE);

            }
        });


        return view;
    }

    public void reloaddata() {

        jsonObject.addProperty("userId", prefs.getString("userid", prefs.getString(SpKey.UID, "")));

        if (Common.isConnected(context)) {
            Common.showProgress(context);
            Ion.with(context)
                    .load(API.Favorite_Product_List)
                    .setTimeout(60 * 1000)
                    .setJsonObjectBody(jsonObject)
                    .asString()
                    .setCallback(new FutureCallback<String>() {


                        @Override
                        public void onCompleted(Exception e, String jsonString) {
                            Log.d("call fav", "callllll");
                            Common.hideProgress(context);
                            list.setVisibility(View.VISIBLE);
                            txtError.setVisibility(View.INVISIBLE);
                            if (e == null) {
                                if (jsonString != null && jsonString != "") {

                                    Log.e("json2", jsonString);

                                    try {
                                        JSONObject resultObj = new JSONObject(jsonString);
                                        // JSONArray jsArray = new JSONArray(jsonString);
                                        //  Log.e("array path", "" + jsArray.getJSONObject(0).getString("image_title"));
                                        //  jsArray.getJSONObject(0).getString("status").equals("1");
                                        // ProductBean(String pid, String productName, String productPrice, String prod_category, String productImageUrl, String prod_fav, String prod_location, String prod_desc, String productPlace) {

                                        if (resultObj.getBoolean("isSuccess")) {
                                            JSONArray jsArray = new JSONArray(resultObj.getJSONArray("Products").toString());
                                            if (productList.size() > 0) {
                                                productList.clear();
                                                //9582708151
                                            }
                                            for (int i = 0; i < jsArray.length(); i++) {
                                                // productList.add(new AchievementsBean(jsArray.getJSONObject(i).getString("nid"), jsArray.getJSONObject(i).getString("title"), jsArray.getJSONObject(i).getString("image"), jsArray.getJSONObject(i).getString("description"),jsArray.getJSONObject(i).getString("thumbnail_path")));
                                                productList.add(new ProductBean(jsArray.getJSONObject(i).getString("prod_id"),
                                                        jsArray.getJSONObject(i).getString("prod_name"), jsArray.getJSONObject(i).getString("prod_price"),
                                                        jsArray.getJSONObject(i).getString("prod_category"), jsArray.getJSONObject(i).getString("prod_image"),
                                                        jsArray.getJSONObject(i).getString("prod_location"), jsArray.getJSONObject(i).getString("prod_desc"),
                                                        jsArray.getJSONObject(i).getString("by_postoffice_price"), jsArray.getJSONObject(i).getString("by_dhl_price")
                                                        , jsArray.getJSONObject(i).getString("by_postoffice"), jsArray.getJSONObject(i).getString("by_dhl")
                                                        , jsArray.getJSONObject(i).getString("put_by_buyer"), jsArray.getJSONObject(i).getString("prod_currency")
                                                        , jsArray.getJSONObject(i).getString("no_of_likes"), jsArray.getJSONObject(i).getInt("isLiked")
                                                        , 1, jsArray.getJSONObject(i).getString("sellerId"), jsArray.getJSONObject(i).get("isSold").toString(),1));
                                            }
                                            if (custAdapter == null) {
                                                custAdapter = new FavGridAdapter(getActivity(), productList);
                                                backupList.addAll(productList);


                                                list.setAdapter(custAdapter);
                                            } else {
                                                custAdapter.notifyDataSetChanged();
                                                backupList.addAll(productList);
                                            }
                                            txtError.setVisibility(View.INVISIBLE);
                                            list.setVisibility(View.VISIBLE);
                                            prefs.putBoolean("favReload", false);
                                        }
                                        else
                                        {
                                            txtError.setVisibility(View.VISIBLE);
                                            list.setVisibility(View.GONE);
                                        }

                                        //        [{"status":"1","message":"Thank you! for Submitting Appointment Request ."}]

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                                    }


                                }

                            } else {

                                e.printStackTrace();
                                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();

                            }


                        }
                    });
        }


    }

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public void filterFavProduct(String cat, String price) {


        jsonObject.addProperty("userId", prefs.getString(prefs.getString(SpKey.UID, ""), ""));
        jsonObject.addProperty("cat", cat);
        jsonObject.addProperty("price", price);

        if (Common.isConnected(context)) {
            Common.showProgress(context);
            Ion.with(context)
                    .load(API.Favorite_Product_List_Search)
                    .setTimeout(60 * 1000)
                    .setJsonObjectBody(jsonObject)
                    .asString()
                    .setCallback(new FutureCallback<String>() {


                        @Override
                        public void onCompleted(Exception e, String jsonString) {
                            Log.d("call fav", "callllll");
                            Common.hideProgress(context);

                            if (e == null) {
                                if (jsonString != null && jsonString != "") {

                                    Log.e("json2", jsonString);
                                    if (productList.size() > 0) {
                                        productList.clear();
                                    }

                                    try {
                                        JSONObject resultObj = new JSONObject(jsonString);
                                        // JSONArray jsArray = new JSONArray(jsonString);
                                        //  Log.e("array path", "" + jsArray.getJSONObject(0).getString("image_title"));
                                        //  jsArray.getJSONObject(0).getString("status").equals("1");
                                        // ProductBean(String pid, String productName, String productPrice, String prod_category, String productImageUrl, String prod_fav, String prod_location, String prod_desc, String productPlace) {

                                        if (resultObj.getBoolean("isSuccess")) {
                                            JSONArray jsArray = new JSONArray(resultObj.getJSONArray("Products").toString());
                                            for (int i = 0; i < jsArray.length(); i++) {
                                                productList.add(new ProductBean(jsArray.getJSONObject(i).getString("prod_id"),
                                                        jsArray.getJSONObject(i).getString("prod_name"), jsArray.getJSONObject(i).getString("prod_price"),
                                                        jsArray.getJSONObject(i).getString("prod_category"), jsArray.getJSONObject(i).getString("prod_image"),
                                                        jsArray.getJSONObject(i).getString("prod_location"), jsArray.getJSONObject(i).getString("prod_desc"),
                                                        jsArray.getJSONObject(i).getString("by_postoffice_price"), jsArray.getJSONObject(i).getString("by_dhl_price")
                                                        , jsArray.getJSONObject(i).getString("by_postoffice"), jsArray.getJSONObject(i).getString("by_dhl")
                                                        , jsArray.getJSONObject(i).getString("put_by_buyer"), jsArray.getJSONObject(i).getString("prod_currency"),
                                                        jsArray.getJSONObject(i).getString("no_of_likes"), jsArray.getJSONObject(i).getInt("isLiked"),
                                                        jsArray.getJSONObject(i).getInt("isFav"), jsArray.getJSONObject(i).getString("sellerId"), jsArray.getJSONObject(i).get("isSold").toString(),0));

                                            }


                                            if (custAdapter == null) {
                                                custAdapter = new FavGridAdapter(getActivity(), productList);

                                                list.setAdapter(custAdapter);
                                            } else {
                                                custAdapter.notifyDataSetChanged();
                                            }
                                            txtError.setVisibility(View.INVISIBLE);
                                            list.setVisibility(View.VISIBLE);
                                        }

                                        //        [{"status":"1","message":"Thank you! for Submitting Appointment Request ."}]

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                                    }


                                }

                            } else {

                                e.printStackTrace();
                                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();

                            }


                        }
                    });
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        Spinner spinerCat = (Spinner) parent;
        Spinner spinerPrice = (Spinner) parent;

        if (spinerCat.getId() == R.id.spinerCategory) {
            if (position > 0) {
                category = spinerCategory.getItemAtPosition(position).toString();


            }


            //  Log.e("category", "" + catArray[position-1]+" "+position);
        }
        if (spinerPrice.getId() == R.id.spinerPrice) {
            if (position > 0) {
                price = spinerPrice.getItemAtPosition(position).toString();
            }
            Log.e("sort", "" + price);

        }


        filterData(productList, price,category);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void filterData(ArrayList<ProductBean> actualList, String price, String category) {


        if (productList != null && actualList.size() > 0) {
            ArrayList<ProductBean> filteredList = new ArrayList<ProductBean>();
            if (!category.equalsIgnoreCase("") && !price.equalsIgnoreCase("")) {

                for (int i = 0; i < actualList.size(); i++) {
                    if (category.equalsIgnoreCase(actualList.get(i).getProd_category()) && Integer.parseInt(price) >= Integer.parseInt(actualList.get(i).getProductPrice())) {
                        filteredList.add(actualList.get(i));
                    }
                }
            }

             else if (category.equalsIgnoreCase("") && !price.equalsIgnoreCase("")) {

                for (int i = 0; i < actualList.size(); i++) {
                    if (Integer.parseInt(price) >= Integer.parseInt(actualList.get(i).getProductPrice())) {
                        filteredList.add(actualList.get(i));
                    }
                }


            }

           else if (!category.equalsIgnoreCase("") && price.equalsIgnoreCase("")) {

                for (int i = 0; i < actualList.size(); i++) {
                    if (category.equalsIgnoreCase(actualList.get(i).getProd_category())) {
                        filteredList.add(actualList.get(i));
                    }
                }


            }
            cross.setVisibility(View.VISIBLE);
            if (filteredList.size() > 0)
            {
                list.setVisibility(View.VISIBLE);
                txtError.setVisibility(View.INVISIBLE);

                isFiltered = true;
                productList.clear();
                productList.addAll(filteredList);
                custAdapter.notifyDataSetChanged();

            }
            else
            {
                list.setVisibility(View.GONE);
                txtError.setVisibility(View.VISIBLE);
                txtError.setText("No data found");
              //  Common.showToast(context,"No data found");
            }


        }
    }



public void clearFilter()
{
    isFiltered=false;
    productList.clear();
    reloaddata();
    cross.setVisibility(View.GONE);
    spinerCategory.setSelection(0);
    spinerPrice.setSelection(0);
   // spinerCategory.setPrompt("Select a Category");
   // spinerCategory.setAdapter("Select a Category");

}

    @Override
    public void onClick(View v) {

        if( getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent)  instanceof HomeFragment) {
            Intent i = new Intent(context, FilterActivity.class);
            startActivityForResult(i, 1000);
        }
            else if( getActivity().getSupportFragmentManager().findFragmentById(R.id.flContent)  instanceof FavoriteFragment)
       clearFilter();
        else
            cross.setVisibility(View.GONE);
    }
}


