package com.app.baccoon.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.activity.HomeActivity;
import com.app.baccoon.activity.ProductDetailsActivity;
import com.app.baccoon.recycle.HomeGridAdapter;
import com.app.baccoon.bean.FilterBean;
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

public class SearchFragment extends Fragment {


    private Context context;
    private boolean uu=false;


    private ArrayList<ProductBean> productList;
    private  TextView txtDistance, txtError;
    String nid, title, image, description;
    String Latitude,Longtitude;
    SharedPreference prefs;
    String[] sortArray = new String[]{"Newest First","Popularity", "Price low-to-high","Price high-to-low"};

    HomeGridAdapter custAdapter;
    Bundle data=new Bundle();
    //variables for filter
    String category="", sortby="", radius="", priceTo="", priceFrom="";

    GridView list;
    JsonObject jsonObject = new JsonObject();
    private String Lat="",Lng="";
    private String sort_command="";
    private String place="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
HomeActivity.searchResult=false;

        context = getActivity();

         prefs = SharedPreference.getInstance(context);
        Latitude=prefs.getString(SpKey.Lat,"");
        Longtitude= prefs.getString(SpKey.Lng,"");



        // ((OnFragmentChange)context).setHeaderText("Gallery",false);
        list = (GridView) view.findViewById(R.id.gridview1);
        txtDistance = (TextView) view.findViewById(R.id.txtDistance);
        txtError = (TextView) view.findViewById(R.id.txtError);

        productList = new ArrayList<ProductBean>();





      data=getArguments();
      Lat=  data.getString("Latitude");
      Lng=  data.getString("Longitude");
        place=data.getString("Place");
      category=  data.getString("Category");
       sort_command= data.getString("sortBy");
       radius= data.getString("Radius");
      priceFrom =data.getString("PriceFrom");
      priceTo=  data.getString("PriceTo");
        if (!radius.equals("")) {
            txtDistance.setText(" Find Offers Within " + radius + " KM");
        }
        else {
            txtDistance.setText(" Find Offers Within 50 KM");

        }
        searchProduct();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                    Log.e("pos=",""+position);
                    Intent intent =new Intent(getActivity(), ProductDetailsActivity.class);
                    intent.putExtra("ProductBean",productList.get(position));
                intent.putExtra("frg", "SEARCH");

                    startActivity(intent);

            }
        });


        return view;
    }

    public SearchFragment() {
        // Required empty public constructor
    }


    public void searchProduct()
    {

//        Latitude = "" + 28.641589;
//        Longtitude = "" + 77.295347;
//
//        {
//            "prod_category" : "Footwear",
//                "priceFrom" : "8000",
//                "priceTo" : "20000",
//                "lattitude" : "28.641589",
//                "longitude" : "77.295347",
//                "range"     : "10"
//        }


        if(Lat.equals(""))
        {
            Lat=Latitude;
          //  Lat=Latitude;

        }
        if(Lng.equals(""))
        {
            Lng=Longtitude;
            //Lng=Longtitude;
        }
//
//
//        if(radius.equals(""))
//        {
//            radius="1";
//
//        }

//        if(!sortby.equals(""))
//        {
//            if(sortby.equals(sortArray[0]))
//            {
//                sort_command="new";
//            }
//            else  if(sortby.equals(sortArray[1]))
//            {
//                sort_command="fav";
//            }
//            else  if(sortby.equals(sortArray[2]))
//            {
//                sort_command="asc";
//            }
//            else  if(sortby.equals(sortArray[3]))
//            {
//                sort_command="desc";
//            }
//
//
//
//
//
//        }



Log.e("sp lat",""+prefs.getString(SpKey.Lat,""));

        if(productList.size()>0)
        {
            productList.clear();
            custAdapter.notifyDataSetChanged();
        }

        FilterBean filterBean=FilterBean.getInstance();
        filterBean.setLocation(place);
        filterBean.setLat(Lat);
        filterBean.setLng(Lng);
        jsonObject.addProperty("prod_category", "" + category);
        jsonObject.addProperty("priceFrom", priceFrom);
        jsonObject.addProperty("priceTo", priceTo);
        jsonObject.addProperty("lattitude", ""+Lat);
        jsonObject.addProperty("longitude", ""+Lng);
        jsonObject.addProperty("range", ""+radius);
        jsonObject.addProperty("sort_type", "" +sort_command);

        Log.e("Json",""+jsonObject.toString());

        if (Common.isConnected(context)) {
            Common.showProgress(context);
            Ion.with(this)
                    .load(API.Search)
                    .setTimeout(60 * 1000)
                    .setJsonObjectBody(jsonObject)
                    .asString()
                    .setCallback(new FutureCallback<String>() {

                        @TargetApi(Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onCompleted(Exception e, String jsonString) {
                            Log.d("calllll", "callllll");
                            Common.hideProgress(context);

                            if (e == null) {
                                if (jsonString != null && jsonString != "") {

                                    Log.e("json2", jsonString);
                                    if(productList.size()>0)
                                    {
                                        productList.clear();


                                    }

                                    try {
                                        JSONObject resultObj = new JSONObject(jsonString);
                                        // JSONArray jsArray = new JSONArray(jsonString);
                                        //  Log.e("array path", "" + jsArray.getJSONObject(0).getString("image_title"));
                                        //  jsArray.getJSONObject(0).getString("status").equals("1");
                                        // ProductBean(String pid, String productName, String productPrice, String prod_category, String productImageUrl, String prod_fav, String prod_location, String prod_desc, String productPlace) {
                                        if (resultObj.getBoolean("isSuccess")) {
                                            JSONArray jsArray = new JSONArray(resultObj.getJSONArray("Result").toString());

                                            if (jsArray.length() > 0) {

                                                for (int i = 0; i < jsArray.length(); i++) {
                                                    // productList.add(new AchievementsBean(jsArray.getJSONObject(i).getString("nid"), jsArray.getJSONObject(i).getString("title"), jsArray.getJSONObject(i).getString("image"), jsArray.getJSONObject(i).getString("description"),jsArray.getJSONObject(i).getString("thumbnail_path")));
                                                    productList.add(new ProductBean(jsArray.getJSONObject(i).getString("prod_id"),
                                                            jsArray.getJSONObject(i).getString("prod_name"), jsArray.getJSONObject(i).getString("prod_price"),
                                                            jsArray.getJSONObject(i).getString("prod_category"), jsArray.getJSONObject(i).getString("prod_image"),
                                                            jsArray.getJSONObject(i).getString("prod_location"), jsArray.getJSONObject(i).getString("prod_desc"),
                                                            jsArray.getJSONObject(i).getString("by_postoffice_price"), jsArray.getJSONObject(i).getString("by_dhl_price")
                                                            ,jsArray.getJSONObject(i).getString("by_postoffice"),jsArray.getJSONObject(i).getString("by_dhl")
                                                            ,jsArray.getJSONObject(i).getString("put_by_buyer"),jsArray.getJSONObject(i).getString("prod_currency"), "", 0, jsArray.getJSONObject(i).getInt("isFav"),
                                                            jsArray.getJSONObject(i).getString("sellerId"), jsArray.getJSONObject(i).get("isSold").toString(),0));
                                                }
                                                //custAdapter = new HomeGridAdapter(getActivity(), productList);

                                                //list.setAdapter(custAdapter);
                                                if (productList.size() > 0 && custAdapter!=null) {
                                                    custAdapter.notifyDataSetChanged();
                                                } else {
                                                    custAdapter = new HomeGridAdapter(getActivity(), productList);



                                                    list.setAdapter(custAdapter);

                                                }
                                                HomeActivity.searchResult = true;

                                                if (!radius.equals("")) {
                                                    txtDistance.setText(" Find Offers Within " + radius + " KM");
                                                }
                                                else {
                                                    txtDistance.setText(" Find Offers Within 50 KM");

                                                }
                                                txtError.setVisibility(View.INVISIBLE);
                                                txtDistance.setVisibility(View.VISIBLE);
                                                list.setVisibility(View.VISIBLE);

                                            } else {
                                                if (resultObj.has("message")) {
                                                    txtDistance.setVisibility(View.INVISIBLE);
                                                    txtError.setVisibility(View.VISIBLE);
                                                    list.setVisibility(View.INVISIBLE);
                                                  //  Toast.makeText(context, "" + resultObj.getString("message"), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }


                                        //        [{"status":"1","message":"Thank you! for Submitting Appointment Request ."}]

                                    } catch (JSONException e1) {
                                        Toast.makeText(context, "Network Error.", Toast.LENGTH_SHORT).show();

                                        e1.printStackTrace();
                                    }


                                }

                            } else {
                                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();

                                e.printStackTrace();

                            }


                        }
                    });
        }


    }






    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        radius="";
        category="";
        sortby="";
        priceFrom="";
        priceTo="";
        sort_command="";
        productList.clear();

        if(requestCode==1000)
        {
            if(resultCode== getActivity().RESULT_OK)
            {

                Log.e("On activity result","Home Fragment reached");
                Log.e("Category", data.getStringExtra("Category"));
                Log.e("sortBy",data.getStringExtra("sortBy"));
                Log.e("Radius", data.getStringExtra("Radius"));
               radius= data.getStringExtra("Radius");
                if(data!=null)
                {
                    if(data.hasExtra("Category") && data.getStringExtra("Category")!=null)
                    {
category=data.getStringExtra("Category");

                    }
                    if(data.hasExtra("sortBy") && data.getStringExtra("sortBy")!=null)
                    {
                        sortby=data.getStringExtra("sortBy");
                    }
                    if(data.hasExtra("Radius") && data.getStringExtra("Radius")!=null)
                    {
                      //  radius=data.getStringExtra("Radius");
                    }
                    if(data.hasExtra("priceTo") && data.getStringExtra("priceTo")!=null)
                    {
                        priceTo=data.getStringExtra("priceTo");
                    }
                    if(data.hasExtra("priceFrom") && data.getStringExtra("priceFrom")!=null)
                    {
                        priceFrom=data.getStringExtra("priceFrom");

                    }

                    if(data.hasExtra("Lat") && data.getStringExtra("Lat")!=null)
                    {
                        Lat=data.getStringExtra("Lat");

                    }
                    if(data.hasExtra("Lng") && data.getStringExtra("Lng")!=null)
                    {
                        Lng=data.getStringExtra("Lng");

                    }
                    if(data.hasExtra("Place") && data.getStringExtra("Place")!=null)
                    {
                        place=data.getStringExtra("Place");

                    }


                    // formatted data
                    if (Lat.equals("")) {
                        Lat = Latitude;

                    }
                    if (Lng.equals("")) {
                        Lng = Longtitude;
                    }


                    if (radius.equals("")) {
                        radius = "50";

                    }
                    //txtDistance.setVisibility(View.INVISIBLE);
                    if (!radius.equals("")) {
                        txtDistance.setText(" Find Offers Within " + radius + " KM");
                    }
                    else {
                        txtDistance.setText(" Find Offers Within 50 KM");

                    }

                    if (!sortby.equals("")) {
                        if (sortby.equals(sortArray[0])) {
                            sort_command = "new";
                        } else if (sortby.equals(sortArray[1])) {
                            sort_command = "fav";
                        } else if (sortby.equals(sortArray[2])) {
                            sort_command = "asc";
                        } else if (sortby.equals(sortArray[3])) {
                            sort_command = "desc";
                        }
                    }
                    else
                    {
                        sort_command="";
                    }

                    searchProduct();


                }

            }
        }

    }




}
