package com.app.baccoon.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.app.baccoon.R;
import com.app.baccoon.activity.MyBaccoonActivity;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;


public class BillingInfoFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    Context context;
    ToggleButton toggleBuyer, toggleDhl, togglePost;
    Boolean buyer = false, dhl = false, post = false;
    TextView btnSave;

    EditText et_DHL, et_Post;
   // TextView et_DHL, et_Post;

    JsonObject jsonObject = new JsonObject();
    boolean isSave = false;
    private SharedPreference prefs;


    public BillingInfoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_billing_info, container, false);
        context = getActivity();
        toggleBuyer = (ToggleButton) view.findViewById(R.id.togglebuyer);
        toggleDhl = (ToggleButton) view.findViewById(R.id.toggledhl);
        togglePost = (ToggleButton) view.findViewById(R.id.togglepost);
        btnSave = (TextView) view.findViewById(R.id.btnSave);
      //  et_DHL = (TextView) view.findViewById(R.id.et_DHL);
       // et_Post = (TextView) view.findViewById(R.id.et_Post);
        et_DHL = (EditText) view.findViewById(R.id.txtDHL);
        et_Post = (EditText) view.findViewById(R.id.txtPost);

        toggleBuyer.setOnCheckedChangeListener(this);
        toggleDhl.setOnCheckedChangeListener(this);
        togglePost.setOnCheckedChangeListener(this);

        prefs = SharedPreference.getInstance(context);

        enabledViews(false);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!isSave) {

                    enabledViews(true);
                } else {

                    if (validateData() && Common.isConnected(getActivity())) {
                        saveDataOnServer();
                    }
                }
            }
        });

        return view;
    }

    private void enabledViews(boolean b) {


        if (b) {

            et_DHL.setVisibility(View.GONE);
            et_Post.setVisibility(View.GONE);
            toggleBuyer.setEnabled(true);
            toggleDhl.setEnabled(true);
            togglePost.setEnabled(true);
            et_DHL.setVisibility(View.VISIBLE);
            et_Post.setVisibility(View.VISIBLE);
            btnSave.setText("Save");
            isSave = true;



            if(prefs.getBoolean(SpKey.isDHL,false))
            {
                et_DHL.setVisibility(View.GONE);
                et_DHL.setVisibility(View.VISIBLE);
                et_DHL.setEnabled(true);
            }
            else
            {
                et_DHL.setVisibility(View.VISIBLE);
                et_DHL.setVisibility(View.GONE);
            }

            if(prefs.getBoolean(SpKey.isPost,false))
            {
                et_Post.setVisibility(View.GONE);
                et_Post.setVisibility(View.VISIBLE);
                et_Post.setEnabled(true);
            }
            else
            {
                et_Post.setVisibility(View.VISIBLE);
                et_Post.setVisibility(View.GONE);
            }





        } else {
            et_DHL.setVisibility(View.VISIBLE);
            et_Post.setVisibility(View.VISIBLE);
            toggleBuyer.setEnabled(false);
            toggleDhl.setEnabled(false);
            togglePost.setEnabled(false);
          //  et_DHL.setVisibility(View.GONE);
           // et_Post.setVisibility(View.GONE);
            btnSave.setText("Edit");
            String dhl_price = prefs.getString(SpKey.DHL_Price, "0");
            String post_price = prefs.getString(SpKey.Post_Price, "0");
            et_DHL.setText(dhl_price);
            et_Post.setText(post_price);
            et_DHL.setText(dhl_price);
            et_Post.setText(post_price);
            if(prefs.getBoolean(SpKey.isDHL,false))
            {
                toggleDhl.setChecked(true);
            }

            if(prefs.getBoolean(SpKey.isPost,false))
            {
                togglePost.setChecked(true);
            }

            if(prefs.getBoolean(SpKey.isBuyer,true))
            {
                toggleBuyer.setChecked(true);
            }


        }


    }


    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
        if (view.getId() == toggleBuyer.getId()) {
            buyer = isChecked;


        } else if (view.getId() == togglePost.getId()) {
            post = isChecked;
            if (isChecked && isSave) {
                et_Post.setEnabled(true);
                et_Post.setVisibility(View.GONE);
                et_Post.setVisibility(View.VISIBLE);

            } else {
                et_Post.setEnabled(false);
                et_Post.setVisibility(View.VISIBLE);
                et_Post.setVisibility(View.GONE);

            }

        } else if (view.getId() == toggleDhl.getId()) {
            dhl = isChecked;
            if (isChecked && isSave) {
                et_DHL.setEnabled(true);
                et_DHL.setVisibility(View.GONE);
                et_DHL.setVisibility(View.VISIBLE);



            } else {
                et_DHL.setEnabled(false);
                et_DHL.setVisibility(View.VISIBLE);
                et_DHL.setVisibility(View.GONE);

            }

        }
    }


    private boolean validateData() {
        String bypost = "", bydhl = "", msg = "";
        boolean result = true;
        bypost = et_Post.getText().toString().trim();
        bydhl = et_DHL.getText().toString().trim();


        if (!checkAnyOneSelect()) {
            msg = "Please enable any one option.";
            result = false;
        } else if (dhl && bydhl.equals("")) {
            msg = "Please  enter cost of shipping via DHL";
            result = false;
        } else if (post && bypost.equals("")) {
            msg = "Please enter cost of shipping via Post Office.";
            result = false;
        }

        if (!msg.equals("")) {
            Common.showToast(getActivity(), msg);
        }
        return result;
    }

    private boolean checkAnyOneSelect() {
        if (post || dhl || buyer) {
            return true;
        } else {
            return false;
        }
    }

    private void saveDataOnServer() {

/*

         {
    "user_id":"5",
    "put_by_buyer":"1",
    "by_dhl":"1",
    "by_post_office":"1",
    "by_dhl_fee":"50",
    "by_post_office_fee":"1000"
}

*/
        int putByBuyer = 0, byDhl = 0, byPost = 0;
        if (buyer) {
            putByBuyer = 1;
        }
        if (post) {
            byPost = 1;
        }
        if (dhl) {
            byDhl = 1;
        }

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("user_id", MyBaccoonActivity.uid);
        jsonObject.addProperty("put_by_buyer", "" + putByBuyer);
        jsonObject.addProperty("by_dhl", "" + byDhl);
        jsonObject.addProperty("by_post_office", "" + byPost);
        jsonObject.addProperty("by_dhl_fee", et_DHL.getText().toString().trim());
        jsonObject.addProperty("by_post_office_fee", et_Post.getText().toString().trim());


        Common.showProgress(getActivity());
        Ion.with(this)
                .load(API.Update_Billing_Info)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asString()

                .setCallback(new FutureCallback<String>() {

                    @Override
                    public void onCompleted(Exception e, String jsonString) {


                        Log.d("calllll", "callllll");
                        Common.hideProgress(getActivity());

                        if (e == null) {
                            if (jsonString != null && jsonString != "") {
                                Log.e("json2", jsonString);
                                try {

                                    JSONObject js = new JSONObject(jsonString);
                                    if (js.getBoolean("isSuccess")) {
                                        Common.showToast(getActivity(), js.getString("message"));
                                        isSave = false;

                                        prefs.putString(SpKey.DHL_Price, et_DHL.getText().toString());
                                        prefs.putString(SpKey.Post_Price, et_Post.getText().toString());
                                        prefs.putBoolean(SpKey.isDHL, dhl);
                                        prefs.putBoolean(SpKey.isBuyer, buyer);
                                        prefs.putBoolean(SpKey.isPost, post);


                                        enabledViews(false);

                                        //.putBoolean(SpKey.isNewAdded,true);


                                    } else {
                                        Common.showToast(getActivity(), js.getString("message"));
                                    }


                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        } else {
                            e.printStackTrace();
                            Log.e("Exception", "" + e);
                            Common.showToast(getActivity(), "Network Error");

                        }
                    }
                });
    }


}
