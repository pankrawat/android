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
import com.app.baccoon.adapter.ShippingInfoListAdapter;
import com.app.baccoon.bean.ShippingInfoBean;
import com.app.baccoon.dialog.AddressDialog;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ShippingInfoFragment extends Fragment implements ShippingInfoListAdapter.OnSettingClickListener {

    private Context context;
    private ArrayList<ShippingInfoBean> shippingInfoList;
    private ShippingInfoListAdapter custAdapter;
    private TextView txtAddress, btnAddAddress, txtAddressCount;
    ListView list;
    JsonObject jsonObject = new JsonObject();
    AddressDialog addressDialog;
    LayoutInflater inflater;

    public ShippingInfoFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shippinginfo_list_fragment, container, false);
        this.inflater = inflater;

        context = getActivity();
        list = (ListView) view.findViewById(R.id.list1);
        txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        txtAddressCount = (TextView) view.findViewById(R.id.txtAddressCount);
        btnAddAddress = (TextView) view.findViewById(R.id.btnAddAddress);
        shippingInfoList = new ArrayList<ShippingInfoBean>();

        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Click btn", "click");

                addressDialog = new AddressDialog(context, inflater, new AddressDialog.OnCodeSelectionListener() {
                    @Override
                    public void onCodeSelection(ShippingInfoBean bean) {

                        shippingInfoList.add(bean);
                        if (custAdapter == null) {
                            custAdapter = new ShippingInfoListAdapter(getActivity(), shippingInfoList, ShippingInfoFragment.this);
                            custAdapter.setListener(ShippingInfoFragment.this);
                            list.setAdapter(custAdapter);
                            addressDialog.dismiss();

                        } else {
                            custAdapter.notifyDataSetChanged();
                        }
                        txtAddressCount.setText("" + shippingInfoList.size());


                    }
                }, true, null);
                addressDialog.show();

            }
        });


        jsonObject.addProperty("userId", MyBaccoonActivity.uid);


        if (Common.isConnected(context)) {
            Common.showProgress(context);

            Ion.with(this)
                    .load(API.Shipping_Info)
                    .setTimeout(60 * 1000)
                    .setJsonObjectBody(jsonObject)
                    .asString()
                    .setCallback(new FutureCallback<String>() {

                        @Override
                        public void onCompleted(Exception e, String jsonString) {
                            Log.d("calllll", "callllll");
                            Common.hideProgress(context);


                            if (e == null) {
                                if (jsonString != null && jsonString != "") {
                                    Log.e("json2", jsonString);

                                    try {
                                        JSONObject resultObj = new JSONObject(jsonString);
                                        if (resultObj.getBoolean("isSuccess")) {
                                           /* ""ship_id": "1",
"user_id": "10",
"shipTo": "Test",
"shipAddress": "Test",
"shipCity": "Test",
"shipState": "Test",
"shipCountry": "Test",
"shipPhone": "9999363138",
"shipPincode": "201301",
"shipStatus": "1"
*/
                                            JSONArray jsArray = new JSONArray(resultObj.getString("shipaddress"));


                                            for (int i = 0; i < jsArray.length(); i++) {

                                                shippingInfoList.add(new ShippingInfoBean(jsArray.getJSONObject(i).getString("ship_id"), jsArray.getJSONObject(i).getString("user_id"),
                                                        jsArray.getJSONObject(i).getString("shipTo"), jsArray.getJSONObject(i).getString("shipAddress"),
                                                        jsArray.getJSONObject(i).getString("shipCity"), jsArray.getJSONObject(i).getString("shipState"),
                                                        jsArray.getJSONObject(i).getString("shipCountry"), jsArray.getJSONObject(i).getString("shipPhone"), jsArray.getJSONObject(i).getString("shipPincode")));

                                            }
                                        }


                                        custAdapter = new ShippingInfoListAdapter(getActivity(), shippingInfoList, ShippingInfoFragment.this);
                                        custAdapter.setListener(ShippingInfoFragment.this);
                                        list.setAdapter(custAdapter);
                                        txtAddressCount.setText(""+shippingInfoList.size());


                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }


                                }

                            } else {

                                e.printStackTrace();

                            }


                        }
                    });


        }


        return view;
    }

    @Override
    public void onSettingClick(final int position) {


        Log.e("edit","click "+position);


     addressDialog  = new AddressDialog(context, inflater, new AddressDialog.OnCodeSelectionListener() {
            @Override
            public void onCodeSelection(ShippingInfoBean bean) {
                if (custAdapter != null) {

                    shippingInfoList.set(position,bean);
                    custAdapter.notifyDataSetChanged();


                }

            }
        }, false,shippingInfoList.get(position));
        addressDialog.show();




    }


    public void updateAddressCountCallBack(int size)
    {
                    txtAddressCount.setText(""+size);

    }



}
