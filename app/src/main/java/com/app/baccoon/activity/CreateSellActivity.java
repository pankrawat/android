package com.app.baccoon.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.app.baccoon.R;
import com.app.baccoon.adapter.NothingSelectedSpinnerAdapter;
import com.app.baccoon.bean.MyProductBean;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.Constant;
import com.app.baccoon.utils.ImageIntent;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class CreateSellActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int CROPER_REQEST_CODE_GALLERY = 22;
    private String CATEGORY_SPINER_LABLE = "Select a Category";
    private String CURRENCY_SPINER_LABLE = "Select Currncy";
    private ArrayAdapter<String> adapterCategory, adapterCurrency;
    private ImageView btnBack, btnDone, productPic, backgroundProfilePic;
    private TextView btnSell, lblCurTwo, lblCurThree;
    private EditText txtProductName, txtproductPrice, txtDHLprice, txtPostPrice, txtDesc;
    private String currentPath;
    private ToggleButton toggleBuyer, toggleDhl, togglePost;
    String category = "", currncy = "";
    String image = "";
    SharedPreference sharedPreference;
    String[] catArray = new String[]{"Footwear", "Mobiles", "Mobile Accessories", "Art and Antiques", "Fashion and Accessories", "Cars and Motors", "Bikes and Scooter", "Books,Films and Music", "Audio & Video", "Televisions", "Computer and Electronics", "SmartWatches and Wearables", "Games World", "Sports and Fitness", "Smart Home Appliances", "Kitchen Appliances", "Furniture and Tables", "House and Office", "Baby and Children", "Beauty and Wellness", "Health Care", "Jewellery", "Animal's Accessories", "Others"};
    String[] currencyArray = new String[]{"INR", "USD", "RUB"};
    private Spinner spinerCategory, spinerCur_one;
    String by_buyer = "0", by_dhl = "0", by_postoffice = "0";
    List<Address> addresses;
    private String dhl_price, post_price;
    private int productCount;
    private boolean isUpdateMode=false;
    MyProductBean bean;
    private Date dNow;
    private SimpleDateFormat ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sell);
        sharedPreference = SharedPreference.getInstance(this);
        productCount = sharedPreference.getInteger(SpKey.ProductCount, 0);
        initView();

        ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dhl_price = sharedPreference.getString(SpKey.DHL_Price, "0");
        post_price = sharedPreference.getString(SpKey.Post_Price, "0");

        if(getIntent().hasExtra("MyProductBean"))
        {
bean=(MyProductBean) this.getIntent().getSerializableExtra("MyProductBean");
            isUpdateMode=true;
            setValuesForUpdate();

        }
        else
        { isUpdateMode=false;
            setValuesForNew();

        }


    }


    private void setValuesForNew() {


        toggleBuyer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    by_buyer = "1";
                } else if (!isChecked) {
                    by_buyer = "0";
                }
            }
        });

        toggleDhl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    by_dhl = "1";
                    txtDHLprice.setEnabled(true);
                    txtDHLprice.setText(dhl_price);
                } else if (!isChecked) {
                    by_dhl = "0";
                    txtDHLprice.setEnabled(false);
                    txtDHLprice.setText("");
                }
            }
        });

        togglePost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    by_postoffice = "1";
                    txtPostPrice.setEnabled(true);
                    txtPostPrice.setText(post_price);
                } else if (!isChecked) {
                    by_postoffice = "0";
                    txtPostPrice.setEnabled(false);
                    txtPostPrice.setText("");
                }
            }
        });




        String lat = sharedPreference.getString(SpKey.Lat, "0");
        String lng = sharedPreference.getString(SpKey.Lng, "0");
     /*   if (sharedPreference.getString(SpKey.Place, "").equals("")) {
            Geocoder geocoder;

            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0);
                sharedPreference.putString(SpKey.Place, address);
            } catch (IOException e) {
                e.printStackTrace();
            }



        }*/
    }


    private void setValuesForUpdate() {

        toggleBuyer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    by_buyer = "1";
                } else if (!isChecked) {
                    by_buyer = "0";
                }
            }
        });

        toggleDhl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    by_dhl = "1";
                    txtDHLprice.setEnabled(true);
                    txtDHLprice.setText(dhl_price);
                } else if (!isChecked) {
                    by_dhl = "0";
                    txtDHLprice.setEnabled(false);
                    txtDHLprice.setText("");
                }
            }
        });

        togglePost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    by_postoffice = "1";
                    txtPostPrice.setEnabled(true);
                    txtPostPrice.setText(post_price);
                } else if (!isChecked) {
                    by_postoffice = "0";
                    txtPostPrice.setEnabled(false);
                    txtPostPrice.setText("");
                }
            }
        });





        txtProductName.setText(bean.getProd_name());
        txtproductPrice.setText(bean.getProd_price());
        setProductImage(bean.getProd_image());
        txtDesc.setText(bean.getProd_desc());
        spinerCategory.setSelection((Arrays.asList(catArray).indexOf(bean.getProd_category())) + 1);
        category=bean.getProd_category();
        spinerCur_one.setSelection(Arrays.asList(currencyArray).indexOf(bean.getProd_currency()));
        currncy=bean.getProd_currency();
        lblCurTwo.setText(currncy);
        lblCurThree.setText(currncy);
        btnSell.setText("Update");


        if (bean.getBy_postoffice().equalsIgnoreCase("1")) {
            togglePost.setChecked(true);
            by_postoffice = "1";
            txtPostPrice.setEnabled(true);
            txtPostPrice.setText(post_price);
        } else {
            togglePost.setChecked(false);
            by_postoffice = "0";
            txtPostPrice.setEnabled(false);
            txtPostPrice.setText("");
        }

        if (bean.getBy_dhl().equalsIgnoreCase("1")) {
            toggleDhl.setChecked(true);
            by_dhl = "1";
            txtDHLprice.setEnabled(true);
            txtDHLprice.setText(dhl_price);
        } else  {
            toggleDhl.setChecked(false);
            by_dhl = "0";
            txtDHLprice.setEnabled(false);
            txtDHLprice.setText("");
        }


        if (bean.getPut_by_buyer().equalsIgnoreCase("1")) {

            toggleBuyer.setChecked(true);
            by_buyer = "1";
        } else  {
            toggleBuyer.setChecked(false);
            by_buyer = "0";
        }


    }

    private void initView() {

        spinerCategory = (Spinner) findViewById(R.id.spinerCategory);
        spinerCur_one = (Spinner) findViewById(R.id.SpinerCur_one);


        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnDone = (ImageView) findViewById(R.id.btnDone);
        lblCurTwo = (TextView) findViewById(R.id.lblCurTwo);
        lblCurThree = (TextView) findViewById(R.id.lblCurThree);

        btnSell = (TextView) findViewById(R.id.btnSell);
        txtProductName = (EditText) findViewById(R.id.product_name);
        txtproductPrice = (EditText) findViewById(R.id.txtProductCost);
        txtDHLprice = (EditText) findViewById(R.id.txtDHL);
        txtPostPrice = (EditText) findViewById(R.id.txtPost);
        txtDesc = (EditText) findViewById(R.id.txtDesc);

        txtPostPrice.setEnabled(false);
        txtDHLprice.setEnabled(false);

        productPic = (ImageView) findViewById(R.id.productPic);
        backgroundProfilePic = (ImageView) findViewById(R.id.backgroundProfilePic);

        toggleBuyer = (ToggleButton) findViewById(R.id.togglebuyer);
        toggleDhl = (ToggleButton) findViewById(R.id.toggledhl);
        togglePost = (ToggleButton) findViewById(R.id.togglepost);


        productPic.setOnClickListener(this);
        btnSell.setOnClickListener(this);
        spinerCategory.setOnItemSelectedListener(this);
        spinerCur_one.setOnItemSelectedListener(this);
        btnBack.setOnClickListener(this);
        btnDone.setOnClickListener(this);


        //set values for category spiner
        adapterCategory = new ArrayAdapter<String>(this, R.layout.textview_layout_spiner, catArray);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerCategory.setPrompt("Select a Category");
        spinerCategory.setAdapter(new NothingSelectedSpinnerAdapter(adapterCategory, R.layout.spiner_default_text_layout, this, CATEGORY_SPINER_LABLE));


        //set values for Currency spiner
        adapterCurrency = new ArrayAdapter<String>(this, R.layout.textview_layout_spiner, currencyArray);
        adapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // spinerCategory.setPrompt("Select Currency");

        spinerCur_one.setAdapter(adapterCurrency);
        //  spinerCategory.setAdapter(new NothingSelectedSpinnerAdapter(adap, R.layout.spiner_default_text_layout, this, CATEGORY_SPINER_LABLE));



    }

    @Override
    public void onClick(View v) {
        if (v.getId() == productPic.getId()) {
            selectImage();
        }

        if (v.getId() == btnSell.getId()) {
            validateData();
        }

        if (v.getId() == btnBack.getId()) {
            finish();
        }

        if (v.getId() == btnDone.getId()) {

        }
    }

    private void validateData() {
        String productname = "", desc = "", productPrice = "", dhlprice = "", postprice = "", msg = "";
        productname = txtProductName.getText().toString();
        desc = txtDesc.getText().toString();
        productPrice = txtproductPrice.getText().toString();
        dhlprice = txtDHLprice.getText().toString();
        postprice = txtPostPrice.getText().toString();

        if (productname.replaceAll(" ", "").equals("")) {
            msg = "Please enter product name.";
        } else if (desc.replaceAll(" ", "").equals("")) {
            msg = "Please enter description.";
        } else if (category.equals("")) {
            msg = "Please select a category.";
        } else if (productPrice.replaceAll(" ", "").equals("")) {
            msg = "Please enter product price.";
        } else if (!toggleBuyer.isChecked() && !togglePost.isChecked() && !toggleDhl.isChecked()) {
            msg = "Please select any delivery option.";
        } else if (toggleDhl.isChecked() && dhlprice.replaceAll(" ", "").equals("")) {
            msg = "Please enter DHL price.";
        } else if (togglePost.isChecked() && postprice.replaceAll(" ", "").equals("")) {
            msg = "Please enter post price.";
        } else if (image.equals("")) {
            msg = "Please select product image.";
        }
        if (!msg.equals("")) {
            Common.showToast(this, msg);
        } else {
            String lat = "", lng = "", place = "";
            lat = sharedPreference.getString(SpKey.Lat, "");
            lng = sharedPreference.getString(SpKey.Lng, "");
            place = sharedPreference.getString(SpKey.Place, "");

/*

            if (productCount > 5 ||true ) {
                showPopupForPayment(productname, desc, productPrice, lat, lng, place, postprice, dhlprice);
            } else {
                sendDataToServer(productname, desc, productPrice, lat, lng, place, postprice, dhlprice);
            }
*/
          if(isUpdateMode)
          {
              updateProductDetails(productname,desc, productPrice, bean.getProd_lat(),bean.getProd_long(), bean.getProd_location());
          }else
          {sendDataToServer(productname, desc, productPrice, lat, lng, place, postprice, dhlprice);}
        }
    }

    private void showPopupForPayment(String msg) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Payment...");

        // Setting Dialog Message
        alertDialog.setMessage(msg);

        // Setting Icon to Dialog
        // alertDialog.setIcon(R.drawable.);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (Common.isConnected(CreateSellActivity.this)) {
                  startActivity(new Intent(CreateSellActivity.this, PaymentActivity.class));
                }

            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(CreateSellActivity.this, "You can not post this product.", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }



    private void updateProductDetails(String productname, String desc, String productPrice, String lat, String lng, String place) {


       /* "user_id" : "10",
        "product_name" : "test",
        "product_desc" : "test",
        "product_category" : "test",
        "product_image" : "",
        "product_price" : "10",
        "product_currency" : "INR",
        “prod_location” : “”
        “prod_lat” : “”
        “prod_long” : “”
        "put_by_buyer" : "1",
        "by_dhl" : "1",
        "by_postoffice" : "1"*/


        String priceOfDHL = "", priceOfPost = "";
        if (toggleDhl.isChecked()) {
            priceOfDHL = txtDHLprice.getText().toString().trim();
        }

        if (togglePost.isChecked()) {
            priceOfPost = txtPostPrice.getText().toString().trim();
        }

      /*  {
            "prod_id" : "56",
                "sellerId" : "53",
                "product_name" :"fastrack Watch",
                "product_desc" :"Thanks",
                "product_category" : "Watches",
                "product_image" :"",
                "product_price" : "1000",
                "product_currency": "INR",
                "prod_location" : "Noida",
                "prod_lat" : "28.6271388",
                "prod_long" : "77.375625",
                "put_by_buyer" : "1",
                "by_dhl": "0",
                "by_dhl_price" :"1009",
                "by_postoffice" :"0",
                "by_postoffice_price":"0",
                "Y-m-d H:i:s":"2016-07-22 13:27:22"

        }*/

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("sellerId",bean.getSellerId());
        jsonObject.addProperty("prod_id",bean.getProd_id());
        jsonObject.addProperty("product_name", productname);
        jsonObject.addProperty("product_desc", desc);
        jsonObject.addProperty("product_category", category);
        jsonObject.addProperty("product_image", image);
        jsonObject.addProperty("product_price", productPrice);
        jsonObject.addProperty("product_currency", currncy);
        jsonObject.addProperty("prod_location", place);
        jsonObject.addProperty("prod_lat", lat);
        jsonObject.addProperty("prod_long", lng);
        jsonObject.addProperty("put_by_buyer", by_buyer);
        jsonObject.addProperty("by_dhl", by_dhl);
        jsonObject.addProperty("by_postoffice", by_postoffice);
        jsonObject.addProperty("by_dhl_price", priceOfDHL);
        jsonObject.addProperty("by_postoffice_price", priceOfPost);
        jsonObject.addProperty("Y-m-d H:i:s", ""+ft.format(new Date()));
        jsonObject.addProperty("buyerId", bean.getBuyerId());

        Common.showProgress(this);
        Ion.with(this)
                .load(API.UpdateMyProduct)
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
                                    if (js.getString("isSuccess").equals("true") || true) {

                                        Common.showToast(CreateSellActivity.this, (js.getString("message")));


                                        setResult(RESULT_OK, getIntent().putExtra("edited",true));


                                        finish();

                                    } else {
                                        //  Common.showToast(CreateSellActivity.this, (js.getString("message")));

                                        Common.showToast(CreateSellActivity.this, (js.getString("message")));
                                    }


                                } catch (JSONException e1) {
                                    e1.printStackTrace();

                                    Common.showToast(CreateSellActivity.this, "Network Error");
                                }
                            }
                        } else {
                            e.printStackTrace();
                            Log.e("Exception", "" + e);
                            Common.showToast(CreateSellActivity.this, "Network Error");
                        }
                    }
                });
    }


    private void sendDataToServer(String productname, String desc, String productPrice, String lat, String lng, String place, String postprice, String dhlprice) {


       /* "user_id" : "10",
        "product_name" : "test",
        "product_desc" : "test",
        "product_category" : "test",
        "product_image" : "",
        "product_price" : "10",
        "product_currency" : "INR",
        “prod_location” : “”
        “prod_lat” : “”
        “prod_long” : “”
        "put_by_buyer" : "1",
        "by_dhl" : "1",
        "by_postoffice" : "1"*/


        String priceOfDHL = "", priceOfPost = "";
        if (toggleDhl.isChecked()) {
            priceOfDHL = txtDHLprice.getText().toString().trim();
        }

        if (togglePost.isChecked()) {
            priceOfPost = txtPostPrice.getText().toString().trim();
        }



        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("user_id",sharedPreference.getString(SpKey.UID,""));
        jsonObject.addProperty("product_name", productname);
        jsonObject.addProperty("product_desc", desc);
        jsonObject.addProperty("product_category", category);
        jsonObject.addProperty("product_image", image);
        jsonObject.addProperty("product_price", productPrice);
        jsonObject.addProperty("product_currency", currncy);
        jsonObject.addProperty("prod_location", place);
        jsonObject.addProperty("prod_lat", lat);
        jsonObject.addProperty("prod_long", lng);
        jsonObject.addProperty("put_by_buyer", by_buyer);
        jsonObject.addProperty("by_dhl", by_dhl);
        jsonObject.addProperty("by_postoffice", by_postoffice);
        jsonObject.addProperty("by_dhl_price", priceOfDHL);
        jsonObject.addProperty("by_postoffice_price", priceOfPost);


        Common.showProgress(this);
        Ion.with(this)
                .load(API.Create_Sell)
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
                                    if (js.getString("isSuccess").equals("true") || true) {

                                        Common.showToast(CreateSellActivity.this, (js.getString("message")));
                                        sharedPreference.putBoolean(SpKey.isNewAdded, true);
                                        productCount = productCount + 1;
                                        sharedPreference.putInt(SpKey.ProductCount, productCount);
                                        finish();

                                    } else {
                                        //  Common.showToast(CreateSellActivity.this, (js.getString("message")));

                                        showPopupForPayment(js.getString("message"));

                                    }


                                } catch (JSONException e1) {
                                    e1.printStackTrace();

                                    Common.showToast(CreateSellActivity.this, "Network Error");
                                }
                            }
                        } else {
                            e.printStackTrace();
                            Log.e("Exception", "" + e);
                            Common.showToast(CreateSellActivity.this, "Network Error");
                        }
                    }
                });
    }

    private void selectImage() {
        Log.e("Select Image", "In Select Image");
        final CharSequence[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Camera")) {

                    openCamera();

                } else if (options[item].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }

            }
        });

        builder.show();
    }

    private void openCamera() {
        Log.e("open camera", "In open camera");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       /* cameraIntent.putExtra("return-data", true);
        this.startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);*/
        File file = null;
        file = new ImageIntent(this).createImageFile();
        if (file != null) {
            currentPath = file.getAbsolutePath();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            cameraIntent.putExtra("return-data", false);
            cameraIntent.putExtra("path", currentPath);
            try {
                this.startActivityForResult(cameraIntent, Constant.PICK_CAMERA);

            } catch (Exception e) {
                Log.e("Exception", "" + e);
            }
        }
    }

    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);

        Intent croperIntent = new Intent(this, CropActivity.class);

        if (responseCode == RESULT_OK) {
            if (requestCode == Constant.PICK_CAMERA) {
                if (currentPath != null || !currentPath.equals("")) {
                    croperIntent.putExtra(Constant.IMAGE_PATH, currentPath);
                    startActivityForResult(croperIntent, CROPER_REQEST_CODE_GALLERY);
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Log.e("Select image", "Gallery");
                try {
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    Log.e("Select image", "in try");
                    Bitmap thumbnail;
                    if (picturePath == null) {
                        picturePath = selectedImage.getPath();
                        Log.e("Select image Gallery", "" + picturePath);
                    }
                    croperIntent.putExtra(Constant.IMAGE_PATH, picturePath);
                    startActivityForResult(croperIntent, CROPER_REQEST_CODE_GALLERY);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("In Exception", "" + e);
                }

            }


            if (requestCode == CROPER_REQEST_CODE_GALLERY) {
                final String pic_url = data.getExtras().getString(Constant.IMAGE_PATH);
                if (pic_url != null || !pic_url.equals("")) {

                    Log.e("croper path", pic_url);


              setProductImage(pic_url);
                } else {
                    Log.e("croper path blank", "sorry");
                }
            }


        }


    }

    private void setProductImage(String pic_url) {
        Ion.with(this).load(pic_url).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap bitmap) {
                if (e == null) {
                    if (bitmap != null) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
                        Log.e("croper path bitmap", "" + bitmap);
                        backgroundProfilePic.setImageBitmap(bitmap);
                        productPic.setAlpha(0f);


                        byte[] byteArray = out.toByteArray();
                        image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        //UserBean.getObect().profile_img=pic_url;
                    }

                } else {
                    e.printStackTrace();
                }
            }
        });



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Log.e("in spinner", "onItemSelected");
        Log.e("position", "" + position);
        Log.e("view.getId()", "" + view.getId());
        Log.e("spinerCategory.getId()", "" + spinerCategory.getId());
        Log.e("spinerCur_one.getId()", "" + spinerCur_one.getId());
        Log.e("parent.getId()", "" + parent.getId());

        if (parent.getId() == spinerCategory.getId()) {
            Log.e("Spinner Selected", "Category");
            if (position > 0)
                category = spinerCategory.getItemAtPosition(position).toString();
            Log.e("category", "" + category);
        }

        if (parent.getId() == spinerCur_one.getId()) {
            Log.e("Spinner Selected", "currency");
            currncy = spinerCur_one.getItemAtPosition(position).toString();
            Log.e("currency", "" + currncy);
            lblCurTwo.setText(currncy);
            lblCurThree.setText(currncy);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
