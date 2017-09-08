package com.app.baccoon.fragment;


import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.activity.Citrus_Card_Payement;
import com.app.baccoon.activity.HomeActivity;
import com.app.baccoon.activity.MyBaccoonActivity;
import com.app.baccoon.activity.PaymentActivity;
import com.app.baccoon.adapter.NothingSelectedSpinnerAdapter;
import com.app.baccoon.bean.ProductBean;
import com.app.baccoon.callback.PaymentDoneListener;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.app.baccoon.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;


public class PaymentFullMembershipFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_CODE_PAYMENT_FullMembership = 3;
    Context context;
    TextView btnPay, txtAmount, txtPicture;
    private String COUNTRY_SPINER_LABLE = "Country";
    private String PLAN_PERIOD_SPINER_LABLE = "Month Type";
    private boolean req_check=false;
    private AlertDialog dialog;
    private String PLAN_TYPE_SPINER_LABLE = "Subscription Type";
    private ArrayAdapter<String> adapterCountry, adapterPlanPeriod, adapterPlanSize;
    JsonObject jsonObject = new JsonObject();
    LinearLayout parent;
    String[] countryArray = new String[]{"France","United States of America","Canada","Russia","India"};
    String[] countryCodeArray = new String[]{"EUR","USD","CAD","RUB","INR"};

  private String fullsubscription_month="";
    private String fullsubscription_type="";
    private Spinner spinerCountry,spinerPlanPeriod, spinerPlanSize;
    private String country = "";
    int type=-1;
    int period=-1;

    String amount="";

    boolean isCountrySelected=false;
    private String[][] pictureQuantityMatrirx= {
            {"90","180","300"},
            {"180","360", "600"},
            {"1080","2160","3600"}

    };


    private String[][] costMatrirx= {
            {"24","50","80"},
            {"54","80", "96"},
            {"150","230","320"}

    };



ArrayList<String> listPLanPeriod=new ArrayList<String>();
ArrayList<String> listPlanSize=new ArrayList<String>();
    private PayPalPayment charges;
    private String code="";
    private SharedPreference prefs;
    private Date date= new Date();
    private float wallet;


    public PaymentFullMembershipFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_membership_payment, container, false);

        context = getActivity();
        prefs= SharedPreference.getInstance(context);
        wallet = prefs.getFloat(SpKey.WALLET, 0f);

        listPlanSize.add("Small");
        listPlanSize.add("Medium");
        listPlanSize.add("Large");

        listPLanPeriod.add("3 Months");
        listPLanPeriod.add("6 Months");
        listPLanPeriod.add("12 Months");


        spinerCountry = (Spinner) view.findViewById(R.id.spinerCountry);
        spinerPlanPeriod = (Spinner) view.findViewById(R.id.spinerPlanPeriod);
        spinerPlanSize = (Spinner) view.findViewById(R.id.spinerPlanSize);
        txtAmount = (TextView) view.findViewById(R.id.txtAmount);
        txtPicture = (TextView) view.findViewById(R.id.txtPicture);
        btnPay = (TextView) view.findViewById(R.id.btnPay);
        parent = (LinearLayout) view.findViewById(R.id.parent);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validData() && Common.isConnected(context))
                {
                   /* if(code!=prefs.getString(SpKey.CURRENCY,"INR"))
                        showpopup();
                    else {*/
                        if (Float.parseFloat(amount) <= wallet   &&code.equals(prefs.getString(SpKey.CURRENCY,""))) {
                            sendPaymentDetailsToServer(code, "", amount);
                        }
                        else {
                            if(code=="INR")
                            {
                                req_check=true;
                                Intent i=new Intent(context,Citrus_Card_Payement.class);
                                startActivity(i);
                               // ToastUtil.showLongSnackBar(getCurrentFocus(), "Transaction Sucessfull");
                                Log.e("",code);
                            }
                                else
                            calltoPayPal(amount, code);
                        }
                 //   }
                }
              /*  CountryPicker picker = CountryPicker.newInstance("Select Country");
                picker.show(getActivity().getSupportFragmentManager(), "COUNTRY_PICKER");
                picker.setListener(new CountryPickerListener() {
                    @Override    //4020024001738242          06/2021
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        // Implement your code here
                        Log.e("data",""+name+", "+code+", "+dialCode);

                    }
                });*/
            }
        });



        adapterCountry = new ArrayAdapter<String>(context, R.layout.textview_layout_spiner, countryArray);
        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerCountry.setPrompt("Select a country");
        spinerCountry.setAdapter(new NothingSelectedSpinnerAdapter(adapterCountry, R.layout.spiner_default_text_layout, context, COUNTRY_SPINER_LABLE));
        spinerCountry.setOnItemSelectedListener(this);




        adapterPlanPeriod = new ArrayAdapter<String>(context, R.layout.textview_layout_spiner, listPLanPeriod);
        adapterPlanPeriod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerPlanPeriod.setPrompt("Select a Plan period");
        spinerPlanPeriod.setAdapter(new NothingSelectedSpinnerAdapter(adapterPlanPeriod, R.layout.spiner_default_text_layout, context, PLAN_PERIOD_SPINER_LABLE));
        spinerPlanPeriod.setOnItemSelectedListener(this);



        adapterPlanSize = new ArrayAdapter<String>(context, R.layout.textview_layout_spiner, listPlanSize);
        adapterPlanSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerPlanSize.setPrompt("Select a Plan type");
        spinerPlanSize.setAdapter(new NothingSelectedSpinnerAdapter(adapterPlanSize, R.layout.spiner_default_text_layout, context, PLAN_TYPE_SPINER_LABLE));
        spinerPlanSize.setOnItemSelectedListener(this);

        return view;
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        switch (((Spinner) parent).getId()) {
            case R.id.spinerCountry:
                if (position > 0) {
                    country = spinerCountry.getItemAtPosition(position).toString();
                    isCountrySelected=true;
                    code=countryCodeArray[position-1];


                }
                break;


            case R.id.spinerPlanPeriod:
                if (position > 0) {

                period=position-1;
                }

                break;

            case R.id.spinerPlanSize:
                if (position > 0) {

                    type=position-1;
                }

                break;


        }
        amount="";

        if(type!=-1 && period!=-1 && isCountrySelected)
        {

//            txtAmount.setText("Rs - "+costMatrirx[period][type]);
//            txtPicture.setText(""+pictureQuantityMatrirx[period][type]+" Pics");

calculateAmount(position);

            Log.e("cost",""+costMatrirx[period][type]);

        }


    }

    private void calculateAmount(int pos) {

        switch(period)
        {
            case 0:
                fullsubscription_month="3";
                break;
            case 1:
                fullsubscription_month="6";
                break;
            case 2:
                fullsubscription_month="12";
                break;
        }

        switch(type)
        {
            case 0:
                fullsubscription_type="0";
                break;
            case 1:
                fullsubscription_type="1";
                break;
            case 2:
                fullsubscription_type="2";
                break;
        }

        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("price", costMatrirx[period][type]);
        jsonObject.addProperty("currency1", "USD");
//        jsonObject.addProperty("currency2", "USD");
        jsonObject.addProperty("currency2", code);
        Log.e("code",""+code);

        if (Common.isConnected(context)) {
            Common.showProgress(getActivity());
            Ion.with(this)
                    .load(API.ConvertMoney)
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
                                        JSONObject resultObj = new JSONObject(jsonString);
                                        if (resultObj.getBoolean("isSuccess")) {

                                        amount=resultObj.getString("Result");
                                            txtAmount.setText(code+" - "+(new BigDecimal(amount)));
                                        txtPicture.setText(""+pictureQuantityMatrirx[period][type]+" Pics");
                                        }

                                        else
                                        {
                                            ToastUtil.showShortSnackBar(parent,"Network Error");
                                        }

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        ToastUtil.showShortSnackBar(parent,"Network Error");

                                    }


                                }

                            } else {

                                ToastUtil.showShortSnackBar(parent,"Network Error");
                                e.printStackTrace();

                            }


                        }
                    });

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }


    private void calltoPayPal(String amount, String code) {
        charges = new PayPalPayment(new BigDecimal(amount), code,
                "Service Charges", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(context,
                com.paypal.android.sdk.payments.PaymentActivity.class);
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, charges);
        PayPalConfiguration object = new PayPalConfiguration();
        object = object.acceptCreditCards(false);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, object);
        getActivity().startActivityForResult(intent, REQUEST_CODE_PAYMENT_FullMembership);
    }



    private boolean validData() {
        boolean status=true;
        String amount="", msg = "";
        amount=txtAmount.getText().toString().trim();

        if(period==-1)
        {
            msg="Please select plan period";
        }

        else if(type==-1)
        {
            msg="Please select plan type";
        }
        else if(country.trim().equals(""))
        {
            msg="Please select country";
        }

        else if(amount.trim().equals("0"))
        {
            msg="Some error occured.Please select again.";
        }

        if (!msg.equals("")) {
            ToastUtil.showShortSnackBar(parent, msg);
            status=false;
        }
        return status;
    }



    private  void sendPaymentDetailsToServer(String currency, String paymentTime, final String amount) {


/*
        "user_id":"1",
                "currency_code":"usd",
                "amount":"50",
                "platform":"ios",
                "create_time":"2016-04-03 20:20:20",
                "payment_id":"sdaf3452f3454234f23443",
                "state":"1",
                "fullsubscription_month":"12",
                "fullsubscription_type":"2",
                "picturewise_subscription_picture":"",
                "country":"usa"*/
        jsonObject.addProperty("user_id", prefs.getString(SpKey.UID, ""));
        jsonObject.addProperty("currency_code", currency);
        jsonObject.addProperty("amount", amount);
        jsonObject.addProperty("create_time", paymentTime.toString());
        jsonObject.addProperty("platform", "android");
        jsonObject.addProperty("payment_id", "wallet");
        jsonObject.addProperty("state", "");
        jsonObject.addProperty("picturewise_subscription_picture", "");
        jsonObject.addProperty("country", country);
        jsonObject.addProperty("fullsubscription_month", fullsubscription_month);
        jsonObject.addProperty("fullsubscription_type", fullsubscription_type);
        jsonObject.addProperty("picturewise_subscription_picture", "");

        jsonObject.addProperty("product_limit", pictureQuantityMatrirx[period][type]);

        if (Common.isConnected(context)) {

            Ion.with(this)
                    .load(API.Payment_Details_Send)
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
                                            Common.showToast(context, resultObj.getString("message"));
                                            ((PaymentActivity) context).paymentDone(wallet,amount);


                                        } else {
                                            ToastUtil.showShortSnackBar(parent, "Network Error");
                                        }


                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        ToastUtil.showShortSnackBar(parent, "Network Error");

                                    }


                                }

                            } else {

                                ToastUtil.showShortSnackBar(parent, "Network Error");
                                e.printStackTrace();

                            }


                        }
                    });


        }
    }

    private void sendPaymentDetailsToServer(String currency,String paymentTime,String amount, String paymentId, String state) {
/*

        "user_id":"1",
                "currency_code":"usd",
                "amount":"50",
                "platform":"ios",
                "create_time":"2016-04-03 20:20:20",
                "payment_id":"sdaf3452f3454234f23443",
                "state":"1",
                "fullsubscription_month":"12",
                "fullsubscription_type":"2",
                "picturewise_subscription_picture":"",
                "country":"usa"*/
        jsonObject.addProperty("user_id", prefs.getString(SpKey.UID,""));
        jsonObject.addProperty("currency_code", currency);
        jsonObject.addProperty("amount", amount);
        jsonObject.addProperty("create_time", paymentTime);
        jsonObject.addProperty("platform", "android");
        jsonObject.addProperty("payment_id", paymentId);
        jsonObject.addProperty("state",state);
        jsonObject.addProperty("fullsubscription_month", fullsubscription_month);
        jsonObject.addProperty("fullsubscription_type", fullsubscription_type);
        jsonObject.addProperty("picturewise_subscription_picture", "");
        jsonObject.addProperty("country", country);
        jsonObject.addProperty("product_limit", pictureQuantityMatrirx[period][type]);


        if (Common.isConnected(context)) {

            Ion.with(this)
                    .load(API.Payment_Details_Send)
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

                                            Common.showToast(context,resultObj.getString("message"));

                                            ((PaymentActivity)context).paymentDone();

                                        }
                                        else
                                        {
                                            ToastUtil.showShortSnackBar(parent,"Network Error");
                                        }

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        ToastUtil.showShortSnackBar(parent,"Network Error");

                                    }


                                }

                            } else {

                                ToastUtil.showShortSnackBar(parent,"Network Error");
                                e.printStackTrace();

                            }
                        }
                    });


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(req_check) {
            String json = SharedPreference.getInstance(context).getString("citrus_json", "fg");
            Log.e("Array was", "" + json);
            try {
                JSONObject cit_result = new JSONObject(json);

                sendPaymentDetailsToServer(code, cit_result.optString("txnDateTime"), amount, cit_result.optString("transactionId"), "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ToastUtil.showLongSnackBar(getView(), "Transaction Sucessfull");
            req_check=false;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("call pic1", "picwise");
        if (requestCode == REQUEST_CODE_PAYMENT_FullMembership) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e("call custom","custom");
                PaymentConfirmation confirm = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.e("first ", "" + confirm.toJSONObject().toString(4));
                        Log.e("second ", "" + confirm.getPayment().toJSONObject().toString(4));
                        String paymentId = confirm.toJSONObject()
                                .getJSONObject("response").getString("id");
                        String state = confirm.toJSONObject()
                                .getJSONObject("response").getString("state");
                        String createTime = confirm.toJSONObject()
                                .getJSONObject("response").getString("create_time");

                        String amount =confirm.getPayment().toJSONObject().getString("amount");

                        Log.e("payment details", "paymentId: " + paymentId
                                + " time:"+createTime+" amount="+amount);

                        ToastUtil.showShortSnackBar(parent,"Payment success.");
                        sendPaymentDetailsToServer(code,createTime,amount,paymentId, state);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("cancelled ","The user canceled.");
                ToastUtil.showShortSnackBar(parent,"Payment cancelled.");
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("notes ", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
                ToastUtil.showShortSnackBar(parent,"An invalid Payment or PayPalConfiguration was submitted.");
            }
        }

     /*   else if(requestCode==50)
        {
            String json=SharedPreference.getInstance(context).getString("citrus_json","fg");

            Log.e("Array was", "" + json);


            try {
                JSONObject cit_result=new JSONObject(json);

                sendPaymentDetailsToServer(code,cit_result.optString("txnDateTime"), amount, cit_result.optString("transactionId"), "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ToastUtil.showShortToast(context,"Transaction Sucessfull");
            ToastUtil.showLongSnackBar(getView(), "Transaction Sucessfull");

        }*/
    }

}
