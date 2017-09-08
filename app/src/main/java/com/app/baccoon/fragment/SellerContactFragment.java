package com.app.baccoon.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.app.baccoon.R;
import com.app.baccoon.activity.MyBaccoonActivity;
import com.app.baccoon.adapter.SellerContactListAdapter;

import com.app.baccoon.bean.SellerContactBean;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SellerContactFragment extends Fragment {

    Context context;
    private ArrayList<SellerContactBean> sellerContactList;
    String nid, title, image, description;
    TextView btnPost, txtError;

    SellerContactListAdapter custAdapter;

    GridView grid;
    JsonObject jsonObject = new JsonObject();


    public SellerContactFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_contact, container, false);


        context = getActivity();
        grid = (GridView) view.findViewById(R.id.gridview1);
        txtError = (TextView) view.findViewById(R.id.txtError);
        // { "opcode": "health_tips","type": "tips_grid" }
        sellerContactList = new ArrayList<SellerContactBean>();

        jsonObject.addProperty("userId", MyBaccoonActivity.uid);


        if (Common.isConnected(context)) {

            Ion.with(this)
                    .load(API.Seller_Contact)
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
                                           /*
                                                "userId": "10",
"sellerName": "Preeti Sundriyal",
"sellerMobile": "0",
"sellerPic": ""*/
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


        return view;
    }

}
