package com.app.baccoon.dialog;


import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.baccoon.R;
import com.app.baccoon.activity.MyBaccoonActivity;
import com.app.baccoon.bean.ShippingInfoBean;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AddressDialog extends AppCompatDialog implements View.OnClickListener {


    private OnCodeSelectionListener listener;
    private LayoutInflater inflater;
    private EditText et_name, et_address, et_state, et_city, et_country, et_phone, et_pincode;
    private TextView btnSave, txtHeader;
    private RelativeLayout btnCross;
    LinearLayout parent;
    boolean isNew = false;
    Context context;
    private JsonObject jsonObject;
    ShippingInfoBean editBean;

    public AddressDialog(Context context, LayoutInflater inflater, OnCodeSelectionListener listener, boolean isNew, ShippingInfoBean editBean) {
        super(context, R.style.myDialogTheme);


      //  supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.add_address_popup_layout);



        this.context = context;
        this.inflater = inflater;
        this.listener = listener;
        this.isNew = isNew;
        this.editBean=editBean;
        setViews();


    }

    private void setViews() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_address = (EditText) findViewById(R.id.et_address);
        et_city = (EditText) findViewById(R.id.et_city);
        et_state = (EditText) findViewById(R.id.et_state);
        et_country = (EditText) findViewById(R.id.et_country);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pincode = (EditText) findViewById(R.id.et_pincode);
        btnCross = (RelativeLayout) findViewById(R.id.btnCross);
        btnSave = (TextView) findViewById(R.id.btnSave);
        txtHeader = (TextView) findViewById(R.id.txtHeader);
        parent = (LinearLayout) findViewById(R.id.parent);

        if(editBean!=null)
        {
            et_name.setText(editBean.getShipTo());
            et_address.setText(editBean.getShipAddress());
            et_city.setText(editBean.getShipCity());
            et_state.setText(editBean.getShipState());
            et_country.setText(editBean.getShipCountry());
            et_phone.setText(editBean.getShipPhone());
            et_pincode.setText(editBean.getShipPincode());
            txtHeader.setText("Edit Shipping Address");

        }


        btnCross.setOnClickListener(this);
        btnSave.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnCross:

                dismiss();

                break;

            case R.id.btnSave:
                Log.e("save", "df");
                if (isNew) {
                    addShippingAddress();
                } else if (!isNew) {

                    editShippingAddress();

                }

                break;
        }

    }

    private void addShippingAddress() {

        if (validate()) {
          /*  "user_id" : "1",
                "shipTo" : "test1",
                "shipAddress" : "test1",
                "shipCity" : "test1",
                "shipState" : "test1",
                "shipCountry" : "test1",
                "shipPhone" : "test1",
                "shipPincode" : "test1"*/

            jsonObject = new JsonObject();
            jsonObject.addProperty("user_id", MyBaccoonActivity.uid);
            jsonObject.addProperty("shipTo", et_name.getText().toString().trim());
            jsonObject.addProperty("shipAddress", et_address.getText().toString().trim());
            jsonObject.addProperty("shipCity", et_city.getText().toString().trim());
            jsonObject.addProperty("shipState", et_state.getText().toString().trim());
            jsonObject.addProperty("shipCountry", et_country.getText().toString().trim());
            jsonObject.addProperty("shipPhone", et_phone.getText().toString().trim());
            jsonObject.addProperty("shipPincode", et_pincode.getText().toString().trim());


            if (Common.isConnected(context)) {
                Common.showProgress(context);

                Ion.with(context)
                        .load(API.Add_Shiping_Address)
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


                                                JSONArray jsArray = new JSONArray(resultObj.getString("Result"));

                                          /*      ship_id": "8",
                                                "user_id": "1",
                                                        "shipTo": "test1",
                                                        "shipAddress": "test1",
                                                        "shipCity": "test1",
                                                        "shipState": "test1",
                                                        "shipCountry": "test1",
                                                        "shipPhone": "test1",
                                                        "shipPincode": "test1",
                                                        "shipStatus": "0"*/
                                                ShippingInfoBean bean = new ShippingInfoBean(jsArray.getJSONObject(0).getString("ship_id"), jsArray.getJSONObject(0).getString("user_id"),
                                                        jsArray.getJSONObject(0).getString("shipTo"), jsArray.getJSONObject(0).getString("shipAddress"),
                                                        jsArray.getJSONObject(0).getString("shipCity"), jsArray.getJSONObject(0).getString("shipState"),
                                                        jsArray.getJSONObject(0).getString("shipCountry"), jsArray.getJSONObject(0).getString("shipPhone"), jsArray.getJSONObject(0).getString("shipPincode"));

                                                listener.onCodeSelection(bean);
                                             dismiss();

                                            }


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

        }
    }
    private void editShippingAddress() {

        if (validate()) {
          /*
           "ship_id" : "11",
    "user_id" : "6",
    "shipTo" : "test",
    "shipAddress" : "test",
    "shipCity" : "test1",
    "shipState" : "test1",
    "shipCountry" : "test1",
    "shipPhone" : "test1",
    "shipPincode" : "test1"


                */

            jsonObject = new JsonObject();
            jsonObject.addProperty("user_id",MyBaccoonActivity.uid);
            jsonObject.addProperty("ship_id",editBean.getShip_id());
            jsonObject.addProperty("shipTo", et_name.getText().toString().trim());
            jsonObject.addProperty("shipAddress", et_address.getText().toString().trim());
            jsonObject.addProperty("shipCity", et_city.getText().toString().trim());
            jsonObject.addProperty("shipState", et_state.getText().toString().trim());
            jsonObject.addProperty("shipCountry", et_country.getText().toString().trim());
            jsonObject.addProperty("shipPhone", et_phone.getText().toString().trim());
            jsonObject.addProperty("shipPincode", et_pincode.getText().toString().trim());


            if (Common.isConnected(context)) {
                Common.showProgress(context);

                Ion.with(context)
                        .load(API.Edit_Shiping_Address)
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


                                                JSONArray jsArray = new JSONArray(resultObj.getString("Result"));

                                          /*      ship_id": "8",
                                                "user_id": "1",
                                                        "shipTo": "test1",
                                                        "shipAddress": "test1",
                                                        "shipCity": "test1",
                                                        "shipState": "test1",
                                                        "shipCountry": "test1",
                                                        "shipPhone": "test1",
                                                        "shipPincode": "test1",
                                                        "shipStatus": "0"*/
                                                ShippingInfoBean bean = new ShippingInfoBean(jsArray.getJSONObject(0).getString("ship_id"), jsArray.getJSONObject(0).getString("user_id"),
                                                        jsArray.getJSONObject(0).getString("shipTo"), jsArray.getJSONObject(0).getString("shipAddress"),
                                                        jsArray.getJSONObject(0).getString("shipCity"), jsArray.getJSONObject(0).getString("shipState"),
                                                        jsArray.getJSONObject(0).getString("shipCountry"), jsArray.getJSONObject(0).getString("shipPhone"), jsArray.getJSONObject(0).getString("shipPincode"));

                                                listener.onCodeSelection(bean);
                                       dismiss();


                                            }
                                            else {
                                                Common.showToast(context,resultObj.getString("message"));
                                            }


                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                            Common.showToast(context, "Network Error");
                                        }


                                    }

                                } else {

                                    e.printStackTrace();
                                    Common.showToast(context, "Network Error");

                                }


                            }
                        });


            }

        }
    }

    private boolean validate() {
        boolean val = true;
        String message = "";
        if (et_name.getText().toString().trim().equals("")) {
            message = "Please enter name";
            val = false;
        } else if (et_address.getText().toString().trim().equals("")) {
            message = "Please enter address";
            val = false;
        } else if (et_state.getText().toString().equals("")) {
            message = "Please enter state";
            val = false;
        } else if (et_city.getText().toString().trim().equals("")) {
            message = "Please enter city";
            val = false;

        } else if (et_country.getText().toString().equals("")) {
            message = "Please enter country";
            val = false;
        } else if (et_phone.getText().toString().trim().equals("")) {
            message = "Please enter phone no";
            val = false;
        } else if (et_pincode.getText().toString().trim().equals("")) {
            message = "Please enter pincode";
            val = false;
        }
else if (et_phone.getText().toString().trim().length()<6) {
            message = "Please enter valid phone no";
            val = false;
        }


        if (!val) {
            ToastUtil.showShortSnackBar(parent, message);
        }


        return val;
    }



    public interface OnCodeSelectionListener {
        void onCodeSelection(ShippingInfoBean bean);
    }
}