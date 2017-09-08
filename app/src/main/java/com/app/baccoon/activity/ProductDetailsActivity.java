package com.app.baccoon.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.adapter.NothingSelectedSpinnerAdapter;
import com.app.baccoon.adapter.SectionedGridRecyclerViewAdapter;
import com.app.baccoon.adapter.SimpleAdapter;
import com.app.baccoon.bean.MyProductBean;
import com.app.baccoon.bean.ProductBean;
import com.app.baccoon.facebook.FacebookManager;
import com.app.baccoon.facebook.OnFacebookLoginListener;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.app.baccoon.utils.ToastUtil;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.vision.text.Line;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.entities.Story;
import com.sromku.simple.fb.listeners.OnPublishListener;


import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProductDetailsActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemSelectedListener, OnFacebookLoginListener {

    private String DELIVERY_OPTION_SPINER_LABLE = "Delivery Option";
    private ArrayAdapter<String> adapterDeliveryOption;
    private ImageView btnBack, imgProductPic, imgLike, imgFav,btnFb, btnTw, btnGp, btnMail, btnChat;
    private TextView btnMakeAnOffer, txtProductName,txtCategoryName, soldout,txtProductName2, txtPrice, txtDesc, txtDeliveryCost, txtRupSymbol;
    private SharedPreference sharedPreference;
    private String[] deliveryOptionArray = new String[]{"Put by Buyer", "By DHL", "By Post office"};
    private SharedPreference prefs;
    private boolean isFav = false;
    private boolean isLike = false;
    private LoginManager loginManager;
    private Bitmap bitmap;
    private Uri u;
    JsonObject jsonObject = new JsonObject();

    @Override
    public void onFacebookLogin(String name, String email, String id, String imageUrl) {
        Log.e("name=", name);
        Log.e("email=", email);
        Log.e("id=", id);
        share_on_fb();
    }

    @Override
    public void onError(String message) {
        Log.e("error msg=", message);
    }

    private enum options {fav, unfav, like, unlike}
    private ProgressDialog pbar;
    private Spinner SpinerDeliveryOption;
    ProductBean bean;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private String[] costArray = new String[]{"0", "0", "0"};
private LoginManager manager;
    private ArrayList<String> deliveryOPT = new ArrayList<String>(2);
    private ArrayList<String> deliveryOPTCost = new ArrayList<String>(2);
private FacebookManager fbmanager;
    private Resources res;

    private String[] strAr;
    MyProductBean myProductBean;
    private LinearLayout mainLayout;


    public ProductDetailsActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        sharedPreference = SharedPreference.getInstance(this);
        prefs = SharedPreference.getInstance(this);
        initView();


        if(getIntent().hasExtra("ProductBean"))

        {
            bean = (ProductBean) this.getIntent().getSerializableExtra("ProductBean");

            if( bean.getSellerId().equalsIgnoreCase(prefs.getString(SpKey.UID, ""))  ||  bean.getMake_Offer()==1)
                btnMakeAnOffer.setVisibility(View.GONE);

            Log.e("name=", bean.getProductName());
            res = this.getResources();

            setValues();

        }
        else if(getIntent().hasExtra("MyProductBean"))
        {
           myProductBean = (MyProductBean) this.getIntent().getSerializableExtra("MyProductBean");
          /*  String pid, String productName, String productPrice, String prod_category, String productImageUrl, String prod_location, String prod_desc,
                String Post_Price,String DHL_Price,String isPost, String isDHL,String isBuyer, String prod_currency, String noOfLikes, int isLiked, int isFav, String sellerId, String isSold) {
*/
            bean=new ProductBean(myProductBean.getProd_id(), myProductBean.getProd_name(), myProductBean.getProd_price(), myProductBean.getProd_category()
                    , myProductBean.getProd_image(), myProductBean.getProd_location(), myProductBean.getProd_desc(), myProductBean.getBy_postoffice_price()
                    , myProductBean.getBy_dhl_price(), myProductBean.getBy_postoffice(), myProductBean.getBy_dhl(), myProductBean.getPut_by_buyer(), myProductBean.getProd_currency()
            ,"0", 0,0, myProductBean.getSellerId(), myProductBean.getIsSold(),1
            );

            if( bean.getSellerId().equalsIgnoreCase(prefs.getString(SpKey.UID, ""))  ||  bean.getMake_Offer()==1)
                btnMakeAnOffer.setVisibility(View.GONE);

            Log.e("name=", bean.getProductName());
            res = this.getResources();

            setValues();

        }
        else {

            Uri data = this.getIntent().getData();
            if (data != null && data.isHierarchical()) {
                String uri = this.getIntent().getDataString();
                uri=uri.replaceAll("http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/productDetails/","");
                get_product_detail_from_server(Integer.parseInt(uri));
                Log.d("MyApp", "Deep link clicked " + uri);
            }
        }



    }

    private void get_product_detail_from_server(int prod_id) {

        jsonObject.addProperty("prod_id",  ""+prod_id);
        jsonObject.addProperty("user_id", prefs.getString(SpKey.UID, ""));


        if (Common.isConnected(this)) {
            Common.showProgress(this);
            Ion.with(this)
                    .load(API.ProductDetails).setTimeout(60 * 1000)
                    .setJsonObjectBody(jsonObject)

                    .asString()
                    .setCallback(new FutureCallback<String>() {

                        @TargetApi(Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onCompleted(Exception e, String jsonString) {
                            Log.d("calllll", "callllll");
                            Common.hideProgress(ProductDetailsActivity.this);
                            if (e == null) {
                                if (jsonString != null && jsonString != "") {
                                    Log.e("json2", jsonString);
                                    try {
                                        JSONObject rootObj = new JSONObject(jsonString);
                                        if (rootObj.getBoolean("isSuccess")) {
                                            JSONObject jsResultObj = new JSONObject(rootObj.getString("Result"));
                                         //   Log.e("Kya re", jsResultObj.toString() + "\n" + jsResultObj.optString("by_dhl_price"));
                                      /*      prod_id": "64",
                                            "sellerId": "55",
                                           *        "buyerId": "0",
                                                   "prod_name": "qwertyee",
                                                    "prod_desc": "Keyboardee",
                                                    "prod_category": "Smart Home Appliances",
                                                    "prod_image": "http:\/\/ec2-52-205-20-98.compute-1.amazonaws.com\/baccoon\/images\/productImages\/481470827817.png",
                                                    "prod_price": "25006",
                                                    "prod_currency": "INR",
                                           *         "prod_fav": "0",
                                                    "prod_location": "Noida, Uttar Pradesh, India",
                                                    "prod_lat": "28.5355161",
                                                    "prod_long": "77.3910265",
                                                    "isSold": "0",
                                                    "put_by_buyer": "1",
                                                    "by_dhl": "1",
                                                    "by_dhl_price": "0",
                                                    "by_postoffice": "0",
                                                    "by_postoffice_price": "0",
                                                    "createdDate": "2016-08-05 20:21:35",
                                                    "no_of_likes": 1,
                                                    "isLiked": 1,
                                                    "isFav": 1  */


                                            bean=new ProductBean(jsResultObj.optString("prod_id"),jsResultObj.optString("prod_name"), jsResultObj.optString("prod_price"),
                                                    jsResultObj.optString("prod_category"), jsResultObj.optString("prod_image"), jsResultObj.optString("prod_long"),
                                                    jsResultObj.optString("prod_desc"), jsResultObj.optString("by_postoffice_price")
                                                    , jsResultObj.optString("by_dhl_price"), jsResultObj.optString("by_postoffice"), jsResultObj.optString("by_dhl"),
                                                    jsResultObj.optString("put_by_buyer"), jsResultObj.optString("prod_currency")
                                                    ,jsResultObj.optString("no_of_likes"), jsResultObj.getInt("isLiked"),jsResultObj.getInt("isFav"), jsResultObj.optString("sellerId"), jsResultObj.optString("isSold"),1
                                            );

                                            if( bean.getSellerId().equalsIgnoreCase(prefs.getString(SpKey.UID, ""))  ||  bean.getMake_Offer()==1)
                                                btnMakeAnOffer.setVisibility(View.GONE);




                                                setValues();
                                        }

                                        //        [{"status":"1","message":"Thank you! for Submitting Appointment Request ."}]

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }

                                }

                            } else {

                                e.printStackTrace();
                                Toast.makeText(ProductDetailsActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                            }


                        }
                    });
        }

    }


    public void resetLikeFav(options action) {
        if (action == options.like) {
            isLike = true;
        } else if (action == options.unlike) {
            isLike = false;
        } else if (action == options.fav) {
            isFav = true;
        } else if (action == options.unfav) {
            isFav = false;
        }

        if (isLike) {
            imgLike.setImageResource(R.mipmap.rheart);
        } else {
            imgLike.setImageResource(R.mipmap.heart_p);

        }
        if (isFav) {
            isFav = true;
            imgFav.setImageResource(R.mipmap.ystar);
        } else {
            imgFav.setImageResource(R.mipmap.star);

        }


        if (getIntent().getStringExtra("frg").equalsIgnoreCase("HOME")) {
            prefs.putBoolean(SpKey.isNewAdded, true);
        } else if (getIntent().getStringExtra("frg").equalsIgnoreCase("FAV")) {
            prefs.putBoolean("favReload", true);
        } else if (getIntent().getStringExtra("frg").equalsIgnoreCase("SEARCH")) {
            prefs.putBoolean("searchReload", true);
        }


    }

    public String[] arrListToStrArray(ArrayList<String> list) {
        String[] str = new String[list.size()];
        int i;
        for (i = 0; i < list.size(); i++) {
            str[i] = list.get(i);
        }
        return str;
    }


    private void setValues() {
        if (bean != null) {
            if (bean.getIsBuyer().equals("1")) {
                deliveryOPT.add("Put by buyer");
                deliveryOPTCost.add("0");
            }
            if (bean.getIsDHL().equals("1")) {
                deliveryOPT.add("By DHL");
                deliveryOPTCost.add(bean.getDHL_Price());
            }
            if (bean.getIsPost().equals("1")) {
                deliveryOPT.add("By Post Office");
                deliveryOPTCost.add(bean.getPost_Price());
            }

            if (bean.getProd_currency().equals("RUP")) {
                txtRupSymbol.setText("" + res.getString(R.string.rupeeSymbol));

            } else if (bean.getProd_currency().equals("Doller")) {
                txtRupSymbol.setText("" + res.getString(R.string.dollorSymbol));

            } else if (bean.getProd_currency().equals("RUB")) {
                txtRupSymbol.setText("" + res.getString(R.string.rubleSymbol));

            }

            if(bean.getMake_Offer()==1)
                btnMakeAnOffer.setVisibility(View.GONE);
            else
                btnMakeAnOffer.setVisibility(View.VISIBLE);

            if (bean.getIsLiked() == 1) {
                isLike = true;
                imgLike.setImageResource(R.mipmap.rheart);
            }
            if (bean.getIsFav() == 1) {
                isFav = true;
                imgFav.setImageResource(R.mipmap.ystar);
            }
            if (bean.getIsSold().equals("1")) {
               btnMakeAnOffer.setVisibility(View.GONE);
                soldout.setVisibility(View.VISIBLE);
            }


            txtProductName.setText(bean.getProductName());
            txtProductName2.setText(bean.getProductName());
            txtPrice.setText( bean.getProductPrice());
            txtDesc.setText(bean.getProd_desc());
            txtCategoryName.setText(Html.fromHtml("<b>Category : </b>"+bean.getProd_category()));
            Ion.with(imgProductPic).error(R.mipmap.bg).placeholder(R.mipmap.default_bg).load(bean.getProductImageUrl());

            Ion.with(this)
                    .load(bean.getProductImageUrl())
                    .setTimeout(60 * 1000)
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {

                            if (e == null) {
                                if (result != null) {
                                    bitmap = result;
                                    imgProductPic.setImageBitmap(bitmap);
                                    File mFile = savebitmap(bitmap);
                                    u = null;
                                    u = Uri.fromFile(mFile);
                                }

                            } else {
                                e.printStackTrace();
                                imgProductPic.setImageResource(R.mipmap.bg);
                            }


                        }
                    });


            strAr = arrListToStrArray(deliveryOPT);

            adapterDeliveryOption = new ArrayAdapter<String>(this, R.layout.textview_layout_spiner, deliveryOptionArray);
            adapterDeliveryOption.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SpinerDeliveryOption.setPrompt("Select a delivery option");
            SpinerDeliveryOption.setAdapter(new NothingSelectedSpinnerAdapter(adapterDeliveryOption, R.layout.spiner_default_text_layout, this, DELIVERY_OPTION_SPINER_LABLE));


            adapterDeliveryOption = new ArrayAdapter<String>(this, R.layout.textview_layout_spiner, strAr);
            adapterDeliveryOption.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            SpinerDeliveryOption.setPrompt("Select a Category");
            SpinerDeliveryOption.setAdapter(new NothingSelectedSpinnerAdapter(adapterDeliveryOption, R.layout.spiner_default_text_layout, this, DELIVERY_OPTION_SPINER_LABLE));


        }

    }

    private void initView() {
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        SpinerDeliveryOption = (Spinner) findViewById(R.id.spinerDeliveryOption);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        imgProductPic = (ImageView) findViewById(R.id.imgProductPic);
        imgLike = (ImageView) findViewById(R.id.imgLike);
        pbar=new ProgressDialog(this);
        fbmanager=new FacebookManager(this);
        fbmanager.setOnLoginListener(this);
        loginManager = LoginManager.getInstance();
        imgFav = (ImageView) findViewById(R.id.imgFav);
        btnFb = (ImageView) findViewById(R.id.btnFb);
        btnTw = (ImageView) findViewById(R.id.btnTw);
        btnGp = (ImageView) findViewById(R.id.btnGp);
        btnMail = (ImageView) findViewById(R.id.btnMail);
        btnChat = (ImageView) findViewById(R.id.btnChat);
        btnMakeAnOffer = (TextView) findViewById(R.id.btnMakeAnOffer);
        txtProductName = (TextView) findViewById(R.id.txtProductName);
        txtProductName2 = (TextView) findViewById(R.id.txtProductName2);
        soldout=(TextView)findViewById(R.id.solddout);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtDeliveryCost = (TextView) findViewById(R.id.txtDeliveryCost);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        txtRupSymbol = (TextView) findViewById(R.id.txtRupSymbol);
        txtCategoryName=(TextView)findViewById(R.id.txtCategory);
        btnMakeAnOffer.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        imgFav.setOnClickListener(this);
        imgLike.setOnClickListener(this);
        btnFb.setOnClickListener(this);
        btnTw.setOnClickListener(this);
        btnGp.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        btnMail.setOnClickListener(this);
        SpinerDeliveryOption.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;

            case R.id.btnMakeAnOffer:

                if (!bean.getSellerId().equalsIgnoreCase(prefs.getString(SpKey.UID, ""))) {

                    Intent msgIntent = new Intent(ProductDetailsActivity.this, MakeOfferActivity.class);
                    msgIntent.putExtra("bean", bean);
                    msgIntent.putExtra("seller_id", bean.getSellerId());
                    msgIntent.putExtra("product_id", bean.getPid());
                    msgIntent.putExtra("offer_price", bean.getPost_Price());
                    startActivityForResult(msgIntent, 10);
                  //  if( sharedPreference.getBoolean(SpKey.make_offer, false))
                   // btnMakeAnOffer.setEnabled(false);
                } else {
                    btnMakeAnOffer.setEnabled(false);
                    Common.showToast(this, "You can not make an offer your product.");
                }
                break;
            case R.id.imgFav:
                if (!bean.getSellerId().equalsIgnoreCase(prefs.getString(SpKey.UID, "")))
                    favThisProduct();
                else
                    Common.showToast(this, "You can not favourite your product.");

                break;

            case R.id.imgLike:
             //   if (!bean.getSellerId().equalsIgnoreCase(prefs.getString(SpKey.UID, "")))
                   likeThisProduct();
               /* else
                    Common.showToast(this, "You can not like your product.")*/;

                break;


            case R.id.btnFb:
                fbmanager.doLogin();

                break;

            case R.id.btnGp:
                shareOnGp();

                break;

            case R.id.btnTw:
                shareOnTw();
                break;

            case R.id.btnMail:
                shareOnMail();
                break;

            case R.id.btnChat:
                shareOnChat();

                break;


        }

    }

    private void share_on_fb()
    {


      //  setup_login();
        final Feed feed = new Feed.Builder()
                .setMessage("Check out...")
                .setName(bean.getProductName())
                .setCaption("Baccon Product")
                .setDescription(bean.getProd_desc())
                .setPicture(bean.getProductImageUrl())
                .setLink("http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/productDetails/"+bean.getPid())
                .build();

        SimpleFacebook.getInstance(ProductDetailsActivity.this).publish(feed, new OnPublishListener() {
//                SimpleFacebook.getInstance().publish(feed, new OnPublishListener() {

            @Override
            public void onException(Throwable throwable) {
                pbar.hide();
                Log.e("problem", throwable.toString());
                ToastUtil.showShortToast(ProductDetailsActivity.this,"Server Problem. Please try after some time");
                // hideDialog();
                // mResult.setText(throwable.getMessage());
            }

            @Override
            public void onFail(String reason) {
                pbar.hide();
                Log.e("failed", reason);
                ToastUtil.showShortToast(ProductDetailsActivity.this,"Unable to post on facebook");
                // hideDialog();
                //  mResult.setText(reason);
            }

            @Override
            public void onThinking() {
                //  Common.showProgress(ProductDetailsActivity.this);
                pbar.setMessage("Posting Product");
                pbar.show();
            }

            @Override
            public void onComplete(String response) {
                //  hideDialog();
                //  mResult.setText(response);
                pbar.hide();
                ToastUtil.showShortToast(ProductDetailsActivity.this,"Shared on facebook");
                Log.e("Published", response);
            }
        });
    }


    //share on chat
    private void shareOnChat() {
        try {
            Intent fbIntent = new Intent(Intent.ACTION_SEND);
            fbIntent.putExtra(Intent.EXTRA_TEXT, "http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/productDetails/"+bean.getPid());
            fbIntent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
            fbIntent.putExtra(Intent.EXTRA_TITLE, "Title");

            if (u != null) {
                fbIntent.putExtra(Intent.EXTRA_STREAM, u);
            }
            else
            ToastUtil.showShortToast(this,"Unable to Share On Whatsapp");
            // fbIntent.setType("text/plain");
            fbIntent.setType("image/*");

            PackageManager packManager = getPackageManager();
            List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(fbIntent, PackageManager.MATCH_DEFAULT_ONLY);

            boolean resolved = false;
            for (ResolveInfo resolveInfo : resolvedInfoList) {

                if (resolveInfo.activityInfo.packageName.startsWith("com.whatsapp")) {
                    fbIntent.setClassName(
                            resolveInfo.activityInfo.packageName,
                            resolveInfo.activityInfo.name);
                    resolved = true;
                    break;
                }
            }
            if (resolved) {
                try {
                    startActivity(fbIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Whatsapp not available. ", Toast.LENGTH_LONG).show();

                }
            }
        } catch (Exception e) {
            //Log.e("Exception posting on fb", "" + e);
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();
        } catch (Throwable th) {
            th.printStackTrace();
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();

        }


    }


    //share on mail
    private void shareOnMail() {
        try {


/*            Intent intent = new Intent(Intent.ACTION_SEND, Uri.fromParts(
                    "mailto", "abc@gmail.com", null));

            intent.putExtra(Intent.EXTRA_SUBJECT, "Baccoon Product");
            intent.putExtra(Intent.EXTRA_STREAM, "http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/productDetails/"+bean.getPid());*/
         //   if (intent.resolveActivity(getPackageManager()) != null)



            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "abc@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Baccoon Product");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/productDetails/"+bean.getPid());
            emailIntent.putExtra(Intent.EXTRA_STREAM, u);
            List<ResolveInfo> matches = getPackageManager().queryIntentActivities(emailIntent, 0);
            for (ResolveInfo info : matches) {
                if (info.activityInfo.packageName.toLowerCase().startsWith("com.google.android.gm")) {
                    emailIntent.setPackage(info.activityInfo.packageName);
                    break;
                }
            }
            startActivity(emailIntent);
            // emailIntent.setType("image/*");

          //  startActivity(Intent.createChooser(emailIntent, "Send via..."));
        } catch (Exception e) {
            //Log.e("Exception posting on fb", "" + e);
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();
        } catch (Throwable th) {
            th.printStackTrace();
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();

        }

    }


    //share on twitter
    private void shareOnTw() {
        try {
            Intent tweetIntent = new Intent(Intent.ACTION_SEND);
            tweetIntent.putExtra(Intent.EXTRA_TEXT, "http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/productDetails/"+bean.getPid());

            if (u != null) {
                tweetIntent.putExtra(Intent.EXTRA_STREAM, u);
            }

            // tweetIntent.setType("image/*");

            PackageManager packManager = getPackageManager();
            List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

            boolean resolved = false;
            for (ResolveInfo resolveInfo : resolvedInfoList) {
                if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                    tweetIntent.setClassName(
                            resolveInfo.activityInfo.packageName,
                            resolveInfo.activityInfo.name);
                    resolved = true;
                    break;
                }
            }
            if (resolved) {
                startActivity(tweetIntent);
            } else {
                Intent i = new Intent();
                // i.putExtra(Intent.EXTRA_TEXT, "message");
                i.setAction(Intent.ACTION_VIEW);

                i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + bean.getProductName()));

                if (u != null) {
                    i.putExtra(Intent.EXTRA_STREAM, u);
                }

                //  i.setType("image/*");
// 4020024001738242   06/21
                startActivity(i);
                //Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //Log.e("Exception posting on fb", "" + e);
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();
        } catch (Throwable th) {
            th.printStackTrace();
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();

        }


    }

    private void shareOnGp() {

        try {
            Intent gpIntent = new Intent(Intent.ACTION_SEND);
            gpIntent.putExtra(Intent.EXTRA_TEXT, "http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/productDetails/"+bean.getPid());
            if (u != null) {
                gpIntent.putExtra(Intent.EXTRA_STREAM, u);
            }

            gpIntent.setType("text/plain");

            PackageManager packManager = getPackageManager();
            List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(gpIntent, PackageManager.MATCH_DEFAULT_ONLY);

            boolean resolved = false;
            for (ResolveInfo resolveInfo : resolvedInfoList) {

                if (resolveInfo.activityInfo.packageName.startsWith("com.google.android.apps.plus")) {
                    gpIntent.setClassName(
                            resolveInfo.activityInfo.packageName,
                            resolveInfo.activityInfo.name);
                    resolved = true;
                    break;
                }
            }
            if (resolved) {
                startActivity(gpIntent);
            } else {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String sharerUrl = "https://plus.google.com/share?post=YOUR_URL_HERE" + "link of post";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                startActivity(intent);
                // Toast.makeText(ctx,"Facebook app not Available",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            //Log.e("Exception posting on fb", "" + e);
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();
        } catch (Throwable th) {
            th.printStackTrace();
            Toast.makeText(this, "Some problem occured", Toast.LENGTH_SHORT).show();

        }


    }



    private void favThisProduct() {
        if (!isFav)
            likeOrFavHit(options.fav);
        else
            likeOrFavHit(options.unfav);

    }

    private void likeThisProduct() {
        if (!isLike)
            likeOrFavHit(options.like);
        else
            likeOrFavHit(options.unlike);
    }


    private void likeOrFavHit(final options action) {

        String URL = "";
        if (action == options.like) {
            URL = API.Hit_Product_Like;
        } else if (action == options.unlike) {
            URL = API.Hit_Product_unLike;
        } else if (action == options.fav) {
            URL = API.Hit_Product_Favourite;
        } else if (action == options.unfav) {
            URL = API.Hit_Product_unFavourite;
        }

        JsonObject jsonObject = new JsonObject();
        if(action==options.fav)
        {
            jsonObject.addProperty("user_id", prefs.getString(SpKey.UID, ""));
            jsonObject.addProperty("product_id", bean.getPid());
            jsonObject.addProperty("sellerId", bean.getSellerId());
        }

        else {
            jsonObject.addProperty("user_id", prefs.getString(SpKey.UID, ""));
            jsonObject.addProperty("product_id", bean.getPid());
        }

        Common.showProgress(this);
        Ion.with(this)
                .load(URL)
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

                                       // Common.showToast(ProductDetailsActivity.this, (js.getString("message")));
                                        resetLikeFav(action);


                                    } else {
                                        Common.showToast(ProductDetailsActivity.this, (js.getString("message")));
                                    }


                                } catch (JSONException e1) {
                                    e1.printStackTrace();

                                    Common.showToast(ProductDetailsActivity.this, "Network Error");
                                }
                            }
                        } else {
                            e.printStackTrace();
                            Log.e("Exception", "" + e);
                            Common.showToast(ProductDetailsActivity.this, "Network Error");
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (((Spinner) parent).getId() == R.id.spinerDeliveryOption) {

            if (position > 0) {
                String cost = SpinerDeliveryOption.getItemAtPosition(position).toString();
                txtDeliveryCost.setText("" + deliveryOPTCost.get(position - 1));
                Log.e("click", "click" + deliveryOPTCost.get(position - 1));

            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onStop() {
        super.onStop();
        /*accessTokenTracker.stopTracking();
        profileTracker.stopTracking();*/
    }
    private File savebitmap(Bitmap bmp) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        String temp = "Baccoon";
        File file = new File(extStorageDirectory, temp + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, temp + ".png");
        }

        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbmanager.onActivityResult(requestCode, resultCode, data);
      //  callbackManager.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK )
        {
            bean.setMake_offer(1);
            prefs.putBoolean(SpKey.isNewAdded, true);
            btnMakeAnOffer.setVisibility(View.GONE);
        }
        else
        {
            bean.setMake_offer(0);
            btnMakeAnOffer.setVisibility(View.VISIBLE);
        }
    }


}
