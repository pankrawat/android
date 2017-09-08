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
import java.sql.CallableStatement;


public class PaymentCustomFragment extends Fragment  implements  AdapterView.OnItemSelectedListener {

    private static final int REQUEST_CODE_PAYMENT_CUSTOM = 1;
    Context context;
    TextView btnPay;
    EditText txtAmount;
    private String COUNTRY_SPINER_LABLE = "Country";
    private ArrayAdapter<String> adapterCountry;
    JsonObject jsonObject = new JsonObject();
    LinearLayout parent;
private AlertDialog dialog;
    String[] countryArray = new String[]{"France","United States of America","Canada","Russia","India"};
    String[] countryCodeArray = new String[]{"EUR","USD","CAD","RUB","INR"};
    private Spinner spinerCountry;
    PayPalPayment charges;
    private String country="";

String currencyCode="";
    private SharedPreference prefs;

    public PaymentCustomFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_payment_filter, container, false);

        context = getActivity();
        prefs=SharedPreference.getInstance(context);
        btnPay=(TextView)view.findViewById(R.id.btnPay);
        txtAmount=(EditText) view.findViewById(R.id.txtAmount);
        parent=(LinearLayout) view.findViewById(R.id.parent);
        btnPay.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

              if(validData()) {
                  if(!currencyCode.equals(prefs.getString(SpKey.CURRENCY,"INR")))
                      showpopup();
                  else
                  calltoPayPal(txtAmount.getText().toString().trim(), currencyCode);
              }
           }
       });



        spinerCountry=(Spinner)view.findViewById(R.id.spinerCountry);
        adapterCountry = new ArrayAdapter<String>(context, R.layout.textview_layout_spiner, countryArray);
        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerCountry.setPrompt("Select a country");
        spinerCountry.setAdapter(new NothingSelectedSpinnerAdapter(adapterCountry, R.layout.spiner_default_text_layout, context, COUNTRY_SPINER_LABLE));
        spinerCountry.setOnItemSelectedListener(this);





        return view;
    }
    private void showpopup() {
        TextView popup_ok,popup_cancel,popup_message;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View vdd = getActivity().getLayoutInflater().inflate(R.layout.popup_currency, null, false);
        builder.setView(vdd);
        dialog = builder.create();
        dialog.setCancelable(false);
        popup_ok=(TextView)vdd.findViewById(R.id.tv_yes);
        popup_cancel=(TextView)vdd.findViewById(R.id.tv_no);
        popup_message=(TextView)vdd.findViewById(R.id.pop_message);
        popup_message.setText(getResources().getString(R.string.Alert_Message_beg) +" "+
                prefs.getString(SpKey.CURRENCY, "INR") + ". "+getResources().getString(R.string.Alert_Message_end)
                +" "+currencyCode+"."
        );
        popup_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                calltoPayPal(txtAmount.getText().toString().trim(), currencyCode);
            }
        });

        popup_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private boolean validData() {
        boolean status=true;
        String amount="", msg = "";
        amount=txtAmount.getText().toString().trim();

        if(amount.equals(""))
        {
            msg="Please enter amount";
        }
        else if(country.trim().equals(""))
        {
            msg="Please select country";
        }


        if (!msg.equals("")) {
           ToastUtil.showShortSnackBar(parent, msg);
            status=false;
        }
        return status;
    }



    private void sendPaymentDetailsToServer(final String currency, String paymentTime, final int amount, String paymentId, String state) {

        jsonObject.addProperty("currency_code", currency);
        jsonObject.addProperty("paid_amount", amount);
      //  jsonObject.addProperty("create_time", paymentTime);
        jsonObject.addProperty("platform", "android");
        jsonObject.addProperty("payment_id", paymentId);
        jsonObject.addProperty("user_id", prefs.getString(SpKey.UID,""));
        jsonObject.addProperty("country", country);


        if (Common.isConnected(context)) {

            Ion.with(this)
                    .load(API.Payment_Details_Send_Custom).setTimeout(60 * 1000)
                    .setJsonObjectBody(jsonObject)
                    .asString()
                    .setCallback(new FutureCallback<String>() {

                        @Override
                        public void onCompleted(Exception e, String jsonString) {
                            Log.d("calllll", "callllll");


                            if (e == null) {
                                if (jsonString != null && jsonString != "") {
                                    Log.e("wallet response=", jsonString);

                                    try {
                                        JSONObject resultObj = new JSONObject(jsonString);
                                        if (resultObj.getBoolean("isSuccess")) {
                                            Common.showToast(context, resultObj.getString("message"));
                                      //      float wallet=prefs.getFloat(SpKey.WALLET,0f)+amount;
                                            prefs.putFloat(SpKey.WALLET, Float.valueOf( resultObj.getJSONObject("Result").optString("wallet_amount")));
                                            prefs.putString(SpKey.CURRENCY,resultObj.getJSONObject("Result").optString("currency_code"));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e("call pic1", "picwise");
        if (requestCode == REQUEST_CODE_PAYMENT_CUSTOM) {
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

                        int amount=0;
                        try {
                            amount =Integer.parseInt((confirm.getPayment().toJSONObject().getString("amount")));
                        }
                        catch(NumberFormatException e)
                        {
                            e.printStackTrace();
                        }

                        Log.e("payment details", "paymentId: " + paymentId
                                + " time:"+createTime+" amount="+amount);

                        ToastUtil.showShortSnackBar(parent,"Payment success.");

                        sendPaymentDetailsToServer(currencyCode,createTime,amount,paymentId, state);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("cancelled ","The user canceled.");
                ToastUtil.showShortSnackBar(parent,"Payment cancelled.");
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e("notes ", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
                ToastUtil.showShortSnackBar(parent,"An invalid Payment or PayPalConfiguration was submitted.");
            }
        }
    }







    private void calltoPayPal(String amount, String code) {
        charges = new PayPalPayment(new BigDecimal(amount), code,
                "Service Charges", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(context,
                com.paypal.android.sdk.payments.PaymentActivity.class);
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, charges);
        getActivity().startActivityForResult(intent, REQUEST_CODE_PAYMENT_CUSTOM);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (((Spinner) parent).getId() == R.id.spinerCountry) {
            if (position > 0) {
                country = spinerCountry.getItemAtPosition(position).toString();
               currencyCode=countryCodeArray[position-1];
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }


    private void calculateAmount(String ppc, String oldCurrency, String newCurrency) {


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("price", ppc);
        jsonObject.addProperty("currency1", ""+oldCurrency);
        jsonObject.addProperty("currency2", ""+newCurrency);
        //  jsonObject.addProperty("currency2", currencycode);


        if (Common.isConnected(context)) {
            Common.showProgress(getActivity());
            Ion.with(this)
                    .load(API.ConvertMoney).setTimeout(60 * 1000)
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

                                         String   ppamount = resultObj.getString("Result");


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


}
