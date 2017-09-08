package com.app.baccoon.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.baccoon.R;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.app.baccoon.utils.Validation;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.vk.sdk.api.model.VKApiUserFull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin1 on 3/5/16.
 */
public class MakeOfferActivity extends Activity implements View.OnClickListener {

    SharedPreference sharedPreference;
    EditText etOffer, etMessage;
    TextView btnSend;
    Intent intent;
    RelativeLayout btnBack;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_offer);
        sharedPreference = SharedPreference.getInstance(this);
        intent = getIntent();
        initView();
    }

    private void initView() {

        etOffer = (EditText) findViewById(R.id.offer);
        etMessage = (EditText) findViewById(R.id.message);
        btnSend = (TextView) findViewById(R.id.btnSend);
        btnBack=(RelativeLayout)findViewById(R.id.btnBack);
        btnSend.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
     //   sharedPreference.putBoolean(SpKey.make_offer, false);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == btnSend.getId()) {
            String offr = etOffer.getText().toString();
            String message = etMessage.getText().toString();
            if(validateData(offr, message))
            {
                if (intent != null) {
                    if (intent.hasExtra("seller_id") && intent.hasExtra("product_id") && intent.hasExtra("offer_price")) {
                        String buyer_id = sharedPreference.getString(SpKey.UID, "");
                        String seller_id = intent.getStringExtra("seller_id");
                        String product_id = intent.getStringExtra("product_id");
                        String offer_price = intent.getStringExtra("offer_price");
                        sendDataToServer(buyer_id, seller_id, product_id, offer_price, offr, message);
                    }
                }
            }
        }
        else if(v.getId()==btnBack.getId())
        {
        //    sharedPreference.putBoolean(SpKey.make_offer,false);
            finish();
        }


    }

    private boolean validateData(String offr, String message) {

        String msg = "";
        boolean status=true;

        if (offr.replaceAll(" ", "").equals("")) {
            msg = "Please enter your offer";
        } else if (message.replaceAll(" ", "").equals("")) {
            msg = "Please enter message";
        }
        else if (!new Validation().isFloat(offr)) {
            msg = "Please enter valid price";
        }

        if (!msg.equals("")) {
            Common.showToast(this, msg);
            status=false;
        }

        return status;
    }




    private void sendDataToServer(String buyer_id, String seller_id, String product_id, String offer_price, String offr, String message) {

Log.e("here","here");
       /* "buyer_id":"1",
                "seller_id":"2",
                "product_id":"3",
                "offer_price":"200",
                "chat_msg":"testing"*/

        /*"isSuccess": true,
                "message": "Message send successfully!",
                "Result": 2*/


        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("buyer_id", buyer_id);
        jsonObject.addProperty("seller_id", seller_id);
        jsonObject.addProperty("product_id", product_id);
        jsonObject.addProperty("offer_price", offer_price);
        jsonObject.addProperty("chat_msg", message);


        Common.showProgress(this);
        Ion.with(MakeOfferActivity.this)
                .load(API.Make_Offer)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asString()

                .setCallback(new FutureCallback<String>() {

                    @Override
                    public void onCompleted(Exception e, String jsonString) {


                        Log.d("calllll", "callllll");
                        Common.hideProgress(getApplicationContext());

                        if (e == null) {
                            if (jsonString != null && jsonString != "") {
                                Log.e("json2", jsonString);
                                try {

                                    JSONObject js = new JSONObject(jsonString);
                                    if (js.getString("isSuccess").equals("true")) {

                                        Common.showToast(MakeOfferActivity.this, (js.getString("message")));
                                    //    sharedPreference.putBoolean(SpKey.make_offer, true);
                                        setResult(-1);
                                        finish();

                                    } else {
                                        Common.showToast(MakeOfferActivity.this, (js.getString("message")));
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();

                                    Common.showToast(MakeOfferActivity.this, "Network Error");
                                }
                            }
                        } else {
                            e.printStackTrace();
                            Log.e("Exception", "" + e);
                            Common.showToast(MakeOfferActivity.this, "Network Error");
                        }
                    }
                });
    }
}
