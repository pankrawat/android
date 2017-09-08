package com.app.baccoon.fragment;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.activity.HomeActivity;
import com.app.baccoon.activity.ProductDetailsActivity;
import com.app.baccoon.adapter.RecyclerItemClickListener;
import com.app.baccoon.adapter.SectionedGridRecyclerViewAdapter;

import com.app.baccoon.adapter.SimpleAdapter;
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
import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener {


    private Context context;
    private int dataCount = 0;
    String USERID;
    private ArrayList<ProductBean> productList;
    private TextView txtDistance, txtError;
    String nid, title, image, description;
    String Latitude, Longtitude;
    SharedPreference prefs;
    private ImageView cross;
    int[] positionHeader = new int[10];
    String[] sortArray = new String[]{"Newest First", "Popularity", "Price low-to-high", "Price high-to-low"};

    //variables for filter
    String category = "", sortby = "", radius = "", priceTo = "", priceFrom = "";

    // GridView list;
    JsonObject jsonObject = new JsonObject();
    private String Lat = "", Lng = "";
    private String sort_command = "";
    RecyclerView list;
    private SimpleAdapter mAdapter;

    public BroadcastReceiver MyLoginBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("OnRecive call", "===");
//			if(intent.getAction().equals(AppConstants.FINISH_LOGIN))

        }
    };
    private IntentFilter intentFilter;

    @Override
    public void onResume() {
        super.onResume();
        if (prefs.getBoolean(SpKey.isNewAdded, false) )
        {
            reloadData();
        }
        cross= (ImageView) getActivity().findViewById(R.id.filter);
        cross.setFocusable(true);
        cross.setImageResource(R.mipmap.filter);
        cross.setVisibility(View.VISIBLE);
       // cross.setOnClickListener(this);
     //  reloadData();

    }

    private void reloadData() {
        dataCount = 0;
        productList.clear();
        jsonObject.addProperty("lattitude", "" + Latitude);
        jsonObject.addProperty("longitude", "" + Longtitude);
        jsonObject.addProperty("userid", "" +USERID );
        jsonObject.addProperty("range", "");

        if (Common.isConnected(context)) {
            Common.showProgress(context);
            Ion.with(this)
                    .load(API.Home_screen).setTimeout(60 * 1000)
                    .setJsonObjectBody(jsonObject)

                    .asString()
                    .setCallback(new FutureCallback<String>() {

                        @TargetApi(Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onCompleted(Exception e, String jsonString) {
                            Log.d("calllll", "callllll");
                            Common.hideProgress(context);

                            if (e == null) {
                                if (jsonString != null && jsonString != "") {

                                    Log.e("json2", jsonString);

                                    try {
                                        JSONObject rootObj = new JSONObject(jsonString);
                                        //This is the code to provide a sectioned grid
                                        List<SectionedGridRecyclerViewAdapter.Section> sections =
                                                new ArrayList<SectionedGridRecyclerViewAdapter.Section>();

                                        if (rootObj.getBoolean("isSuccess")) {
                                            JSONObject jsResultObj = new JSONObject(rootObj.getString("Result"));

                                            int[] strHeader = {1, 10, 20, 30, 40, 50,100, 200, 500, 1000};
                                            int m;
                                            for (m = 0; m < 10; m++) {
                                                JSONArray jsArray = new JSONArray(jsResultObj.get(strHeader[m] + " KM").toString());
                                                Log.e("obj" + m, "" + jsArray.toString());

                                                for (int i = 0; i < jsArray.length(); i++) {
                                                    dataCount++;
//                                                    public ProductBean(String pid, String productName, String productPrice, String prod_category, String productImageUrl, String prod_fav, String prod_location, String prod_desc,
//                                                            String Post_Price,String DHL_Price,String isPost, String isDHL,String isBuyer, String prod_currency, String noOfLikes) {
                                                        // productList.add(new AchievementsBean(jsArray.getJSONObject(i).getString("nid"), jsArray.getJSONObject(i).getString("title"), jsArray.getJSONObject(i).getString("image"), jsArray.getJSONObject(i).getString("description"),jsArray.getJSONObject(i).getString("thumbnail_path")));
                                                    productList.add(new ProductBean(jsArray.getJSONObject(i).getString("prod_id"),
                                                            jsArray.getJSONObject(i).getString("prod_name"), jsArray.getJSONObject(i).getString("prod_price"),
                                                            jsArray.getJSONObject(i).getString("prod_category"), jsArray.getJSONObject(i).getString("prod_image"),
                                                            jsArray.getJSONObject(i).getString("prod_location"), jsArray.getJSONObject(i).getString("prod_desc"),
                                                            jsArray.getJSONObject(i).getString("by_postoffice_price"), jsArray.getJSONObject(i).getString("by_dhl_price")
                                                            ,jsArray.getJSONObject(i).getString("by_postoffice"),jsArray.getJSONObject(i).getString("by_dhl")
                                                            ,jsArray.getJSONObject(i).getString("put_by_buyer"),jsArray.getJSONObject(i).getString("prod_currency"), jsArray.getJSONObject(i).getString("no_of_likes"),
                                                            jsArray.getJSONObject(i).getInt("isLiked"), jsArray.getJSONObject(i).getInt("isFav"),
                                                            jsArray.getJSONObject(i).getString("sellerId"), jsArray.getJSONObject(i).get("isSold").toString()
                                                             ,jsArray.getJSONObject(i).getInt("isOffered")
                                                    ));
                                                }
                                                if (jsArray.length() > 0) {
                                                    if (m == 0) {
                                                        positionHeader[m] = 0;
                                                        sections.add(new SectionedGridRecyclerViewAdapter.Section(positionHeader[m], "Find Offers Within " + strHeader[m] + " KM"));

                                                    } else {
                                                        positionHeader[m] = dataCount - jsArray.length();
                                                        sections.add(new SectionedGridRecyclerViewAdapter.Section(positionHeader[m], "Find Offers Within " + strHeader[m] + " KM"));
                                                    }
                                                }
                                            }
                                            if (mAdapter != null) {
                                                mAdapter.notifyDataSetChanged();
                                            } else {
                                                mAdapter = new SimpleAdapter(context, productList);
                                            }
                                            //Add your adapter to the sectionAdapter
                                            SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
                                            SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                                                    SectionedGridRecyclerViewAdapter(context, R.layout.section, R.id.section_text, list, mAdapter);
                                            mSectionedAdapter.setSections(sections.toArray(dummy));

                                            list.setAdapter(mSectionedAdapter);
                                            prefs.putBoolean(SpKey.isNewAdded, false);

                                            //Your RecyclerView.Adapter
                                            if (dataCount == 0) {
                                                txtError.setVisibility(View.VISIBLE);
                                                list.setVisibility(View.VISIBLE);
                                                //  Toast.makeText(context, "" + resultObj.getString("message"), Toast.LENGTH_SHORT).show();

                                                // Toast.makeText(context,"No Data Found.", Toast.LENGTH_SHORT).show();
                                            } else {

                                                txtError.setVisibility(View.GONE);
                                                list.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        //        [{"status":"1","message":"Thank you! for Submitting Appointment Request ."}]
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            } else {
                                e.printStackTrace();
                                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            txtError.setVisibility(View.VISIBLE);
            txtError.setText("No Internet Connection.Tap to try Again");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample, null);
        HomeActivity.searchResult = false;

        context = getActivity();

        prefs = SharedPreference.getInstance(context);
        prefs.putBoolean(SpKey.isNewAdded, false);
        Latitude = prefs.getString(SpKey.Lat, "");
        Longtitude = prefs.getString(SpKey.Lng, "");
        USERID= prefs.getString(SpKey.UID, "");

        // ((OnFragmentChange)context).setHeaderText("Gallery",false);
        list = (RecyclerView) view.findViewById(R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new GridLayoutManager(context, 2));

        txtDistance = (TextView) view.findViewById(R.id.txtDistance);
        txtError = (TextView) view.findViewById(R.id.txtError_home);
        txtError.setOnClickListener(this);
        productList = new ArrayList<ProductBean>();
        positionHeader[0] = 0;

        jsonObject.addProperty("lattitude", "" + Latitude);
        jsonObject.addProperty("longitude", "" + Longtitude);
        jsonObject.addProperty("user_id", "" + USERID);
        jsonObject.addProperty("range", "");

        if (Common.isConnected(context)) {
            Common.showProgress(context);
            Ion.with(this)
                    .load(API.Home_screen).setTimeout(60 * 1000)
                    .setJsonObjectBody(jsonObject)

                    .asString()
                    .setCallback(new FutureCallback<String>() {

                        @TargetApi(Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onCompleted(Exception e, String jsonString) {
                            Log.d("calllll", "callllll");
                            Common.hideProgress(context);

                            if (e == null) {
                                if (jsonString != null && jsonString != "") {

                                    Log.e("json2", jsonString);

                                    try {
                                        JSONObject rootObj = new JSONObject(jsonString);

                                        //This is the code to provide a sectioned grid
                                        List<SectionedGridRecyclerViewAdapter.Section> sections =
                                                new ArrayList<SectionedGridRecyclerViewAdapter.Section>();
                                        // JSONArray jsArray = new JSONArray(jsonString);
                                        //  Log.e("array path", "" + jsArray.getJSONObject(0).getString("image_title"));
                                        //  jsArray.getJSONObject(0).getString("status").equals("1");
                                        // ProductBean(String pid, String productName, String productPrice, String prod_category, String productImageUrl, String prod_fav, String prod_location, String prod_desc, String productPlace) {

                                        if (rootObj.getBoolean("isSuccess")) {
                                            // JSONArray jsArray = new JSONArray(rootObj.getJSONArray("Result").toString());
                                            JSONObject jsResultObj = new JSONObject(rootObj.getString("Result"));


                                            //   Iterator<String> keys=jsResultObj.keys();
                                            //   int count =0;

                                            int[] strHeader = {1, 10, 20, 30, 40, 50, 100, 200, 500, 1000};
                                            int m;
                                            for (m = 0; m < 10; m++) {

                                                JSONArray jsArray = new JSONArray(jsResultObj.get(strHeader[m] + " KM").toString());
                                                Log.e("obj" + m, "" + jsArray.toString());


                                                for (int i = 0; i < jsArray.length(); i++) {
                                                    dataCount++;
                                                    // productList.add(new AchievementsBean(jsArray.getJSONObject(i).getString("nid"), jsArray.getJSONObject(i).getString("title"), jsArray.getJSONObject(i).getString("image"), jsArray.getJSONObject(i).getString("description"),jsArray.getJSONObject(i).getString("thumbnail_path")));
                                                    productList.add(new ProductBean(jsArray.getJSONObject(i).getString("prod_id"),
                                                            jsArray.getJSONObject(i).getString("prod_name"), jsArray.getJSONObject(i).getString("prod_price"),
                                                            jsArray.getJSONObject(i).getString("prod_category"), jsArray.getJSONObject(i).getString("prod_image"),
                                                            jsArray.getJSONObject(i).getString("prod_location"), jsArray.getJSONObject(i).getString("prod_desc"),
                                                            jsArray.getJSONObject(i).getString("by_postoffice_price"), jsArray.getJSONObject(i).getString("by_dhl_price")
                                                            , jsArray.getJSONObject(i).getString("by_postoffice"), jsArray.getJSONObject(i).getString("by_dhl")
                                                            , jsArray.getJSONObject(i).getString("put_by_buyer"), jsArray.getJSONObject(i).getString("prod_currency"),
                                                            jsArray.getJSONObject(i).getString("no_of_likes"), jsArray.getJSONObject(i).getInt("isLiked"),
                                                            jsArray.getJSONObject(i).getInt("isFav"), jsArray.getJSONObject(i).getString("sellerId"),
                                                            jsArray.getJSONObject(i).get("isSold").toString(), jsArray.getJSONObject(i).getInt("isOffered")));
                                                }
                                                if (jsArray.length() > 0) {
                                                    if (m == 0) {
                                                        positionHeader[m] = 0;
                                                        sections.add(new SectionedGridRecyclerViewAdapter.Section(positionHeader[m], "Find Offers Within " + strHeader[m] + " KM"));

                                                    } else {
                                                        positionHeader[m] = dataCount - jsArray.length();
                                                        sections.add(new SectionedGridRecyclerViewAdapter.Section(positionHeader[m], "Find Offers Within " + strHeader[m] + " KM"));
                                                    }
                                                }
                                            }
                                            // custAdapter = new HomeGridAdapter(getActivity(), productList);

                                            mAdapter = new SimpleAdapter(context, productList);

                                            //Add your adapter to the sectionAdapter
                                            SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
                                            SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                                                    SectionedGridRecyclerViewAdapter(context, R.layout.section, R.id.section_text, list, mAdapter);
                                            mSectionedAdapter.setSections(sections.toArray(dummy));

                                            //Apply this adapter to the RecyclerView
                                            list.setAdapter(mSectionedAdapter);

                                            //Your RecyclerView.Adapter
                                            if (dataCount == 0) {


                                                txtError.setVisibility(View.VISIBLE);
                                                list.setVisibility(View.VISIBLE);
                                                //  Toast.makeText(context, "" + resultObj.getString("message"), Toast.LENGTH_SHORT).show();
                                                // Toast.makeText(context,"No Data Found.", Toast.LENGTH_SHORT).show();
                                            } else {

                                                txtError.setVisibility(View.GONE);
                                                list.setVisibility(View.VISIBLE);
                                            }
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
        else
        {
            txtError.setVisibility(View.VISIBLE);
            txtError.setText("No Internet Connection.Tap to try Again");
        }

list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
    @Override
    public void onItemClick(View view, int position) {

if(view.getTag()!=null) {
    int pos = (Integer) view.getTag();
    Log.e("pos=",""+pos);
    Intent intent =new Intent(getActivity(), ProductDetailsActivity.class);
    intent.putExtra("ProductBean",productList.get(pos));
    intent.putExtra("frg","HOME");
    getActivity().startActivity(intent);

}

    }
}));

        return view;
    }

    public HomeFragment() {
        // Required empty public constructor
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == getActivity().RESULT_OK) {

                Log.e("On activity result", "Home Fragment reached");
                Log.e("Category", data.getStringExtra("Category"));
                Log.e("sortBy", data.getStringExtra("sortBy"));
                Log.e("Radius", data.getStringExtra("Radius"));
                radius = data.getStringExtra("Radius");
                if (data != null) {
                    if (data.hasExtra("Category") && data.getStringExtra("Category") != null) {
                        category = data.getStringExtra("Category");

                    }
                    if (data.hasExtra("sortBy") && data.getStringExtra("sortBy") != null) {
                        sortby = data.getStringExtra("sortBy");
                    }
                    if (data.hasExtra("Radius") && data.getStringExtra("Radius") != null) {
                        //  radius=data.getStringExtra("Radius");
                    }
                    if (data.hasExtra("priceTo") && data.getStringExtra("priceTo") != null) {
                        priceTo = data.getStringExtra("priceTo");
                    }
                    if (data.hasExtra("priceFrom") && data.getStringExtra("priceFrom") != null) {
                        priceFrom = data.getStringExtra("priceFrom");

                    }

                    if (data.hasExtra("Lat") && data.getStringExtra("Lat") != null) {
                        Lat = data.getStringExtra("Lat");

                    }
                    if (data.hasExtra("Lng") && data.getStringExtra("Lng") != null) {
                        Lng = data.getStringExtra("Lng");

                    }


                    Bundle bundle = new Bundle();

                    // formatted data
                    if (Lat.equals("")) {
                        Lat = Latitude;

                    }
                    if (Lng.equals("")) {
                        Lng = Longtitude;
                    }


                    if (radius.equals("")) {
                        radius = "50";

                    }

                    if (!sortby.equals("")) {
                        if (sortby.equals(sortArray[0])) {
                            sort_command = "new";
                        } else if (sortby.equals(sortArray[1])) {
                            sort_command = "fav";
                        } else if (sortby.equals(sortArray[2])) {
                            sort_command = "asc";
                        } else if (sortby.equals(sortArray[3])) {
                            sort_command = "desc";
                        }
                    }
                    Fragment frg = new SearchFragment();
                    bundle.putString("Latitude", Lat);
                    bundle.putString("Longitude", Lng);
                    bundle.putString("Category", category);
                    bundle.putString("sortBy", sort_command);
                    bundle.putString("Radius", radius);
                    bundle.putString("PriceFrom", priceFrom);
                    bundle.putString("PriceTo", priceTo);
                    bundle.putString("Place", data.getStringExtra("Place"));
                    frg.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.flContent, frg).
                            addToBackStack(this.toString()).commit();

                }

            }
        }

    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.txtError_home)
        {
            reloadData();
        }
    }
}
