package com.app.baccoon.fragment;


import android.app.Activity;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.activity.MyBaccoonActivity;
import com.app.baccoon.activity.PaymentActivity;
import com.app.baccoon.adapter.NothingSelectedSpinnerAdapter;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.app.baccoon.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;


public class PaymentPicturewiseFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_CODE_PAYMENT_PICYUREWISE = 2;
    Context context;
    TextView btnPay, txtAmountPerPic, btnCalculateAmount, txtCurrency, txtTotalAmount;
    private String COUNTRY_SPINER_LABLE = "Country";
    private String PICTURE_QUANTITY_SPINER_LABLE = "Picture quantity";

    private ArrayAdapter<String> adapterCountry, adapterPictureQuantity;
    JsonObject jsonObject = new JsonObject();
    LinearLayout parent;
    String[] countryArray = new String[]{"France", "United States of America", "Canada", "Russia", "India"};
    String[] countryCodeArray = new String[]{"EUR", "USD", "CAD", "RUB", "INR"};
    private Spinner spinerCountry, spinerPictureQuantity;
    private String country = "";
    EditText et_pic_quantity;
    String amount;
    boolean isCountrySelected = false;
    boolean isPicSelected = false;

    private String code = "";
    private String currencycode = "";
    private SharedPreference prefs;
    float wallet;
private Date date=new Date();

    private String[] costArray = {"3.00",
            "8.00",
            "10.00",
            "15.00",
            "20.30",
            "30.00",
            "50.00",
            "70.00",
            "79.2",
            "54",
            "84",
            "90",
            "96",
            "130",
            "200",
            "270",
            "0"
    };

    private String[] costPerPictureArray = {"0.75",
            "0.67",
            "0.33",
            "0.30",
            "0.29",
            "0.30",
            "0.33",
            "0.28",
            "0.22",
            "0.3",
            "0.21",
            "0.18",
            "0.16",
            "0.13",
            "0.1",
            "0.09",
            "0.09"
    };

    private String[] pictureQuantityArray = {"4",
            "12",
            "30",
            "50",
            "70",
            "100",
            "150",
            "250",
            "360",
            "180",
            "400",
            "500",
            "600",
            "1000",
            "2000",
            "3000",
            "above 3000"
    };


    private PayPalPayment charges;
    private boolean isMorePicSelected = false;
    private String totalPic = "0";
    private String perPicCost = "0";
    private boolean isCalculated = false;
    private String ppamount = "";
    private String noOfPic = "";


    public PaymentPicturewiseFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picturewise_payment, container, false);

        context = getActivity();
        prefs = SharedPreference.getInstance(context);
        wallet = prefs.getFloat(SpKey.WALLET, 0f);
        spinerCountry = (Spinner) view.findViewById(R.id.spinerCountry);
        spinerPictureQuantity = (Spinner) view.findViewById(R.id.spinerPictureQuantity);

        txtAmountPerPic = (TextView) view.findViewById(R.id.txtAmountPerPic);
        et_pic_quantity = (EditText) view.findViewById(R.id.et_pic_quantity);
        btnCalculateAmount = (TextView) view.findViewById(R.id.btnCalculateAmount);
        txtCurrency = (TextView) view.findViewById(R.id.txtCurrency);
        txtTotalAmount = (TextView) view.findViewById(R.id.txtTotalAmount);
        btnPay = (TextView) view.findViewById(R.id.btnPay);
        parent = (LinearLayout) view.findViewById(R.id.parent);

        btnCalculateAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validSelection()) {
                    float ppc = Float.parseFloat(ppamount);
                    int tp = Integer.parseInt(totalPic);
                    if (isMorePicSelected) {
                        tp = Integer.parseInt(et_pic_quantity.getText().toString().trim());
                    }

                    float tm = ppc * tp;
                    noOfPic = "" + tp;
                    txtCurrency.setText("" + currencycode);
                    //   DecimalFormat df = new DecimalFormat("#.##");

                    txtTotalAmount.setText("" + tm);
                    isCalculated = true;
                }
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCalculated) {
                    if (Common.isConnected(context)) {

                        if (Float.parseFloat(txtTotalAmount.getText().toString().trim()) <= wallet
                                && code.equals(prefs.getString(SpKey.CURRENCY,""))
                                ) {

                            sendPaymentDetailsToServer(currencycode, "", txtTotalAmount.getText().toString().trim());

                        } else {
                            calltoPayPal(txtTotalAmount.getText().toString().trim(), currencycode);
                        }
                    }
                } else {
                    ToastUtil.showShortSnackBar(parent, "Please calculate amount first");
                }
            }
        });


        adapterCountry = new ArrayAdapter<String>(context, R.layout.textview_layout_spiner, countryArray);
        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerCountry.setPrompt("Select a country");
        spinerCountry.setAdapter(new NothingSelectedSpinnerAdapter(adapterCountry, R.layout.spiner_default_text_layout, context, COUNTRY_SPINER_LABLE));
        spinerCountry.setOnItemSelectedListener(this);


        adapterPictureQuantity = new ArrayAdapter<String>(context, R.layout.textview_layout_spiner, pictureQuantityArray);
        adapterPictureQuantity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerPictureQuantity.setPrompt("Select a Plan period");
        spinerPictureQuantity.setAdapter(new NothingSelectedSpinnerAdapter(adapterPictureQuantity, R.layout.spiner_default_text_layout, context, PICTURE_QUANTITY_SPINER_LABLE));
        spinerPictureQuantity.setOnItemSelectedListener(this);

        return view;
    }

    private boolean validSelection() {
        boolean result = false;
        String msg = "";
        if (!isCountrySelected) {
            msg = "Please select country";
        } else if (!isPicSelected) {
            msg = "Please select picture quantity";
        } else if (isMorePicSelected) {
            if (et_pic_quantity.getText().toString().trim().equalsIgnoreCase("")) {
                msg = "Please enter no of pics.";
            } else if (Integer.parseInt(et_pic_quantity.getText().toString().trim()) < 3000) {
                msg = "Please enter no of more than 3000 pics.";
            } else {
                result = true;
            }

        } else {
            result = true;
        }

        if (!msg.equalsIgnoreCase("")) {
            ToastUtil.showShortSnackBar(parent, msg);
        }


        return result;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        switch (((Spinner) parent).getId()) {
            case R.id.spinerCountry:
                if (position > 0) {
                    country = spinerCountry.getItemAtPosition(position).toString();
                    isCountrySelected = true;
                    currencycode = countryCodeArray[position - 1];


                }
                break;


            case R.id.spinerPictureQuantity:
                if (position > 0) {

                    isPicSelected = true;
                    if (spinerPictureQuantity.getItemAtPosition(position).toString().equalsIgnoreCase("above 3000")) {
                        isMorePicSelected = true;
                        perPicCost = costPerPictureArray[position - 1];

                        et_pic_quantity.setVisibility(View.VISIBLE);
                        totalPic = "0";

                        //calculateAmount(perPicCost);


                    } else {
                        isMorePicSelected = false;
                        et_pic_quantity.setVisibility(View.GONE);
                        totalPic = pictureQuantityArray[position - 1];


                    }
                }

                break;


        }

        if (!isMorePicSelected && isCountrySelected && isPicSelected) {


            perPicCost = costPerPictureArray[position - 1];
            calculateAmount(perPicCost);


        }

        if (isMorePicSelected && isCountrySelected) {


            calculateAmount(perPicCost);


        }
        isCalculated = false;
        txtTotalAmount.setText("0");


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
        getActivity().startActivityForResult(intent, REQUEST_CODE_PAYMENT_PICYUREWISE);
    }





    private void sendPaymentDetailsToServer(String currency, String paymentTime, final String amount) {

        Timestamp time= new Timestamp(date.getTime());




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
        jsonObject.addProperty("create_time", time.toString());
        jsonObject.addProperty("platform", "android");
        jsonObject.addProperty("payment_id", "wallet");
        jsonObject.addProperty("state", "");
        jsonObject.addProperty("fullsubscription_month", "");
        jsonObject.addProperty("fullsubscription_type", "");
        jsonObject.addProperty("picturewise_subscription_picture", "1");
        jsonObject.addProperty("country", country);
        jsonObject.addProperty("product_limit", noOfPic);

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


    private void sendPaymentDetailsToServer(String currency, String paymentTime, String amount, String paymentId, String state) {
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
        jsonObject.addProperty("create_time", paymentTime);
        jsonObject.addProperty("platform", "android");
        jsonObject.addProperty("payment_id", paymentId);
        jsonObject.addProperty("state", state);
        jsonObject.addProperty("fullsubscription_month", "");
        jsonObject.addProperty("fullsubscription_type", "");
        jsonObject.addProperty("picturewise_subscription_picture", "1");
        jsonObject.addProperty("country", country);
        jsonObject.addProperty("product_limit", noOfPic);

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
                                            ((PaymentActivity) context).paymentDone();
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

    private void calculateAmount(String ppc) {


        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("price", ppc);
        jsonObject.addProperty("currency1", "USD");
        jsonObject.addProperty("currency2", "USD");
        //  jsonObject.addProperty("currency2", currencycode);


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

                                            ppamount = resultObj.getString("Result");
                                            txtAmountPerPic.setText("" + currencycode + " - " + ppamount);


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("call pic1", "picwise");
        if (requestCode == REQUEST_CODE_PAYMENT_PICYUREWISE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.e("call pic2", "picwise");
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

                        String amount = confirm.getPayment().toJSONObject().getString("amount");

                        Log.e("payment details", "paymentId: " + paymentId
                                + " time:" + createTime + " amount=" + amount);

                        ToastUtil.showShortSnackBar(parent, "Payment success.");
                        sendPaymentDetailsToServer(currencycode, createTime, amount, paymentId, state);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("cancelled ", "The user canceled.");
                ToastUtil.showShortSnackBar(parent, "Payment cancelled.");
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("notes ", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
                ToastUtil.showShortSnackBar(parent, "An invalid Payment or PayPalConfiguration was submitted.");
            }
        }
    }


}
