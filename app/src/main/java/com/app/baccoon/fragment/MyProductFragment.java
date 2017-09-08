package com.app.baccoon.fragment;

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
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.activity.ProductDetailsActivity;
import com.app.baccoon.adapter.FavGridAdapter;
import com.app.baccoon.adapter.MyProductAdapter;
import com.app.baccoon.adapter.NothingSelectedSpinnerAdapter;
import com.app.baccoon.bean.MyProductBean;
import com.app.baccoon.bean.ProductBean;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyProductFragment extends Fragment {


    private Context context;


    private ArrayList<MyProductBean> productList;
    private TextView txtError;

    SharedPreference prefs;
    MyProductAdapter custAdapter;
    GridView list;
    JsonObject jsonObject = new JsonObject();

    @Override
    public void onResume() {
        super.onResume();
      }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_product, null);

        context = getActivity();
        prefs = SharedPreference.getInstance(context);
        list = (GridView) view.findViewById(R.id.gridview1);
        txtError = (TextView) view.findViewById(R.id.txtError);
        getMyProduct();
        return view;
    }

    private void getMyProduct() {

        //  userId" : "10"
        productList = new ArrayList<MyProductBean>();
        jsonObject.addProperty("sellerId", prefs.getString(SpKey.UID, ""));


        if (Common.isConnected(context)) {
            Common.showProgress(context);
            Ion.with(context)
                    .load(API.MyProducts)
                    .setTimeout(60 * 1000)
                    .setJsonObjectBody(jsonObject)
                    .asString()
                    .setCallback(new FutureCallback<String>() {


                        @Override
                        public void onCompleted(Exception e, String jsonString) {
                            Log.d("call fav", "callllll");
                            Common.hideProgress(context);

                            if (e == null) {
                                if (jsonString != null && jsonString != "") {

                                    Log.e("json2", jsonString);

                                    try {
                                        JSONObject resultObj = new JSONObject(jsonString);

                                        /**
                                         * prod_id : 58
                                         * sellerId : 55
                                         * buyerId : 0
                                         * prod_name : Footwear product
                                         * prod_desc : this is gopal product
                                         * prod_category : Footwear
                                         * prod_image : http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/images/productImages/121470405849.png
                                         * prod_price : 999
                                         * prod_currency : INR
                                         * prod_fav : 0
                                         * prod_location : Vaishali, Ghaziabad, Uttar Pradesh, India
                                         * prod_lat : 28.643317500000002
                                         * prod_long : 77.33818939999999
                                         * isSold : 1
                                         * put_by_buyer : 1
                                         * by_dhl : 1
                                         * by_dhl_price : 200
                                         * by_postoffice : 0
                                         * by_postoffice_price : 0
                                         * createdDate : 2016-06-02 13:21:45
                                         */


                                        if (resultObj.getBoolean("isSuccess")) {
                                            JSONArray jsArray = new JSONArray(resultObj.getJSONArray("Result").toString());
                                            for (int i = 0; i < jsArray.length(); i++) {

                                                  productList.add(new MyProductBean(jsArray.getJSONObject(i).getString("prod_id"),jsArray.getJSONObject(i).getString("sellerId"),jsArray.getJSONObject(i).getString("buyerId"),
                                                        jsArray.getJSONObject(i).getString("prod_name"), jsArray.getJSONObject(i).getString("prod_desc"),  jsArray.getJSONObject(i).getString("prod_category"),
                                                          jsArray.getJSONObject(i).getString("prod_image"),  jsArray.getJSONObject(i).getString("prod_price"), jsArray.getJSONObject(i).getString("prod_currency"),  jsArray.getJSONObject(i).getString("prod_fav"),

                                                          jsArray.getJSONObject(i).getString("prod_location"), jsArray.getJSONObject(i).getString("prod_lat"), jsArray.getJSONObject(i).getString("prod_long")
                                                          , jsArray.getJSONObject(i).getString("isSold"),  jsArray.getJSONObject(i).getString("put_by_buyer"), jsArray.getJSONObject(i).getString("by_dhl")
                                                          , jsArray.getJSONObject(i).getString("by_dhl_price"),jsArray.getJSONObject(i).getString("by_postoffice"),   jsArray.getJSONObject(i).getString("by_postoffice_price")


                                                        , jsArray.getJSONObject(i).getString("createdDate")));
                                            }
                                            custAdapter = new MyProductAdapter(getActivity(), productList, MyProductFragment.this);

                                            list.setAdapter(custAdapter);
                                            txtError.setVisibility(View.INVISIBLE);
                                            list.setVisibility(View.VISIBLE);
                                        }

                                        //        [{"status":"1","message":"Thank you! for Submitting Appointment Request ."}]

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            } else {

                                e.printStackTrace();
                                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("MyProductBean", productList.get(position));
                intent.putExtra("frg", "MyProduct");
                startActivity(intent);

            }
        });



    }


    public MyProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==getActivity().RESULT_OK && data.getBooleanExtra("edited", false))
        {

        }

    }
}

