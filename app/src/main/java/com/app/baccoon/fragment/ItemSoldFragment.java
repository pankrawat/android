package com.app.baccoon.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.app.baccoon.R;
import com.app.baccoon.activity.MyBaccoonActivity;
import com.app.baccoon.adapter.ItemSoldListAdapter;
import com.app.baccoon.bean.ItemSoldBean;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ItemSoldFragment extends Fragment {

    Context context;
    private ArrayList<ItemSoldBean> itemSoldList;
    String nid, title, image, description;
    TextView btnPost,txtError;

    ItemSoldListAdapter custAdapter;

    ListView list;
    JsonObject jsonObject = new JsonObject();


    public ItemSoldFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.itemsold_list_fragment, container, false);


        context = getActivity();
        list = (ListView) view.findViewById(R.id.list1);
        txtError = (TextView) view.findViewById(R.id.txtError);
        itemSoldList = new ArrayList<ItemSoldBean>();

        jsonObject.addProperty("userId", MyBaccoonActivity.uid);


        if (Common.isConnected(context)) {

            Ion.with(this)
                    .load(API.Item_Sold)
                    .setTimeout(60 * 1000)
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
                                           /* "prod_id": "1",
                                                "sellerId": "10",
                                                "buyerId": "2",
                                                "prod_name": "zara jackect",
                                                "prod_desc": "abc",
                                                "prod_category": "jacket",
                                                "prod_image": "abc.png",
                                                "prod_price": "10000",
                                                "prod_currency": "",
                                                "prod_fav": "45",
                                                "prod_location": "Hauz Khas",
                                                "prod_lat": "28.652781",
                                                "prod_long": "77.192144",
                                                "isSold": "1",
                                                "put_by_buyer": "0",
                                                "by_dhl": "0",
                                                "by_postoffice": "0",
                                                "buyerName": "hh ghgh",
                                                "buyerMobile": "0"*/

                                            JSONArray jsArray = new JSONArray(resultObj.getString("Products"));
                                            Log.e("array", "" + jsArray.getJSONObject(0).getString("prod_name"));

                                            for (int i = 0; i < jsArray.length(); i++) {

                                                itemSoldList.add(new ItemSoldBean(jsArray.getJSONObject(i).getString("prod_id"),
                                                        jsArray.getJSONObject(i).getString("prod_name"),
                                                        jsArray.getJSONObject(i).getString("prod_image"),
                                                        jsArray.getJSONObject(i).getString("prod_desc"),
                                                        jsArray.getJSONObject(i).getString("buyerMobile"),
                                                        jsArray.getJSONObject(i).getString("buyerName"),
                                                        jsArray.getJSONObject(i).getString("prod_price")));

                                            }
                                            custAdapter = new ItemSoldListAdapter(getActivity(), itemSoldList);
                                            list.setAdapter(custAdapter);
                                            txtError.setVisibility(View.GONE);
                                            list.setVisibility(View.VISIBLE);


                                        }
                                        else
                                        {
                                            txtError.setVisibility(View.VISIBLE);
                                            list.setVisibility(View.GONE);
                                        }





                                    } catch (JSONException e1) {
                                        txtError.setVisibility(View.VISIBLE);
                                        list.setVisibility(View.GONE);
                                        e1.printStackTrace();
                                    }


                                }

                            } else {
                                txtError.setVisibility(View.VISIBLE);
                                list.setVisibility(View.GONE);

                                e.printStackTrace();

                            }


                        }
                    });


        }


        return view;
    }

}
