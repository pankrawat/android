package com.app.baccoon.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.baccoon.R;
import com.google.gson.JsonObject;


public class PaymentThreeFragment extends Fragment {

    Context context;
    TextView btnPay;
    private String COUNTRY_SPINER_LABLE = "Country";
    private ArrayAdapter<String> adapterCountry;
    JsonObject jsonObject = new JsonObject();

    String[] countryArray = new String[]{"Footwear", "Mobiles", "Mobile Accessories", "Art and Antiques", "Fashion and Accessories", "Cars and Motors", "Bikes and Scooter", "Books,Films and Music", "Audio & Video", "Televisions", "Computer and Electronics", "SmartWatches and Wearables", "Games World", "Sports and Fitness", "Smart Home Appliances", "Kitchen Appliances", "Furniture and Tables", "House and Office", "Baby and Children", "Beauty and Wellness", "Health Care", "Jewellery", "Animal's Accessories", "Others"};

    private Spinner spinerCountry;



    public PaymentThreeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picturewise_payment, container, false);

        context = getActivity();
//        spinerCountry=(Spinner)view.findViewById(R.id.spinerCountry);
//
//        adapterCountry = new ArrayAdapter<String>(context, R.layout.textview_layout_spiner, countryArray);
//        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinerCountry.setPrompt("Select a country");
//        spinerCountry.setAdapter(new NothingSelectedSpinnerAdapter(adapterCountry, R.layout.spiner_default_text_layout, context, COUNTRY_SPINER_LABLE));
//

//createPayment();


        return view;
    }

   /* private void createPayment() {

        jsonObject.addProperty("userId", MyBaccoonActivity.uid);


        if (Common.isConnected(context)) {

            Ion.with(this)
                    .load(API.Seller_Contact)
                    .setJsonObjectBody(jsonObject)
                    .asString()
                    .setCallback(new FutureCallback<String>() {

                        @Override
                        public void onCompleted(Exception e, String jsonString) {
                            Log.d("calllll", "callllll");


                            if (e == null) {
                                if (jsonString != null && jsonString != "") {
                                    Log.e("json2", jsonString);

                                    try {
                                        JSONObject resultObj = new JSONObject(jsonString);
                                        if (resultObj.getBoolean("isSuccess")) {
                                           *//*
                                                "userId": "10",
"sellerName": "Preeti Sundriyal",
"sellerMobile": "0",
"sellerPic": ""*//*
                                            JSONArray jsArray = new JSONArray(resultObj.getString("Products"));
                                            Log.e("array", "" + jsArray.getJSONObject(0).getString("sellerName"));

                                            for (int i = 0; i < jsArray.length(); i++) {

                                                sellerContactList.add(new SellerContactBean(jsArray.getJSONObject(i).getString("sellerId"), jsArray.getJSONObject(i).getString("sellerPic"), jsArray.getJSONObject(i).getString("sellerMobile"), jsArray.getJSONObject(i).getString("sellerName")));
                                            }

                                            custAdapter = new SellerContactListAdapter(getActivity(), sellerContactList);
                                            grid.setAdapter(custAdapter);
                                            txtError.setVisibility(View.GONE);
                                            grid.setVisibility(View.VISIBLE);
                                        }
                                        else
                                        {
                                            txtError.setVisibility(View.VISIBLE);
                                            grid.setVisibility(View.GONE);
                                        }




                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        txtError.setVisibility(View.VISIBLE);
                                        grid.setVisibility(View.GONE);
                                    }


                                }

                            } else {

                                txtError.setVisibility(View.VISIBLE);
                                grid.setVisibility(View.GONE);
                                e.printStackTrace();

                            }


                        }
                    });


        }


    }*/

}
