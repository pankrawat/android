package com.app.baccoon.activity;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
;
import android.app.AlertDialog;
import com.app.baccoon.R;
import com.app.baccoon.camera.ImageIntent;
import com.app.baccoon.fragment.BillingInfoFragment;
import com.app.baccoon.fragment.ItemSoldFragment;
import com.app.baccoon.fragment.SellerContactFragment;
import com.app.baccoon.fragment.ShippingInfoFragment;

import com.app.baccoon.utils.API;
import com.app.baccoon.utils.CircleImageView;
import com.app.baccoon.utils.CircularTextView;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.Constant;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.google.android.gms.maps.model.Circle;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyBaccoonActivity extends AppCompatActivity implements ImageIntent.OnImageChoosenListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView btnBack, backgroundProfilePic;
    private ViewPagerAdapter adapter;

    private SharedPreference pref;
    TextView txtBalance;
    public static String uid="";
    ImageIntent imageIntent;
    CircleImageView circularProfilePic;


TextView txtName,txtEmail, btnPay;

    String email, name;
    private String imagePath="";
    private String image="";
    private String picURL="";
    private String profileImageUrl="";
    private String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_baccoon);


        imageIntent=new ImageIntent(this,this);
        viewPager =(ViewPager)findViewById(R.id.viewpager);
        btnBack =(ImageView)findViewById(R.id.btnBack);
        backgroundProfilePic =(ImageView)findViewById(R.id.backgroundProfilePic);
        txtEmail =(TextView)findViewById(R.id.txtEmail);
        txtName =(TextView)findViewById(R.id.txtName);
        txtBalance =(TextView)findViewById(R.id.txtBalance);
        btnPay =(TextView)findViewById(R.id.btnPay);
        circularProfilePic =(CircleImageView) findViewById(R.id.circularProfilePic);
        pref=SharedPreference.getInstance(this);
        uid=   pref.getString("userid", "");
        name=   pref.getString(SpKey.FIRST_NAME, "")+ " "+pref.getString(SpKey.LastName, "");
        email=   pref.getString(SpKey.EMAIL,"");

        url=pref.getString(SpKey.ImageUlr,"");

        if(!url.equals(""))
        {
            setImageOnView(url);
        }

        txtName.setText(""+name);
        txtEmail.setText(""+email);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MyBaccoonActivity.this, PaymentActivity.class));
            }
        });


      /*  BitmapDrawable drawable = (BitmapDrawable) backgroundProfilePic.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        Bitmap blurred = blurRenderScript(bitmap,17);//second parametre is radius
        backgroundProfilePic.setImageBitmap(blurred);                          //radius decide blur amount

*/
        circularProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageIntent.showImageChooser();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideSoftKeyBoard();


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);


      tabLayout.setupWithViewPager(viewPager);

    }

    public void hideSoftKeyBoard() {
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    private void setupViewPager(ViewPager viewPager) {

        //context.getSupportFragmentManager().beginTransaction().replace(R.id.homeContainer,new AboutUsMain()).commit();
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


       adapter.addFragment(new ItemSoldFragment(), "Item Sold");
       adapter.addFragment(new SellerContactFragment(), "Seller Contact");
      adapter.addFragment(new ShippingInfoFragment(), "Shipping Info");
      adapter.addFragment(new BillingInfoFragment(), "Billing Info");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);

    }

    @Override
    public void onImageFileCreated(String path) {
        imagePath=path;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();



        private Fragment mCurrentFragment;

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }



        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }



    }

    @SuppressLint("NewApi")
    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {

        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(this);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }

    private Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }


    @Override
    protected void onResume() {
        super.onResume();
        txtBalance.setText(""+pref.getString(SpKey.CURRENCY, "INR")+ " "+pref.getFloat(SpKey.WALLET, (float)0));

    }

    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if (responseCode == RESULT_OK && requestCode == Constant.PICK_GALLERY) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            try {
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                if (picturePath == null) {
                    picturePath = selectedImage.getPath();
                }
                doCrop(picturePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (responseCode == RESULT_OK && requestCode == Constant.PICK_CAMERA) {
            if (imagePath != null) {
                doCrop(imagePath);
            }
        } else if (responseCode == RESULT_OK && requestCode == Constant.CROP_REQ) {
            String pic_url = data.getExtras().getString(Constant.IMAGE_PATH);

            picURL = pic_url;

            if (pic_url != null) {
                Ion.with(this).load(pic_url).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap bitmap) {
                        if (e == null) {
                            if (bitmap != null) {
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
                                Log.e("croper path bitmap", "" + bitmap);
                                //circularProfilePic.setImageBitmap(bitmap);
                               // Bitmap blurred = blurRenderScript(bitmap,17);//second parametre is radius
                                //backgroundProfilePic.setImageBitmap(blurred);
                                byte[] byteArray = out.toByteArray();
                                String image = Base64.encodeToString(byteArray, Base64.DEFAULT);

                                sendImageToServer(image,bitmap);



                            }

                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }



    private void sendImageToServer(String image, Bitmap bitmap)
    {


        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("user_id",pref.getString(SpKey.UID,""));
        jsonObject.addProperty("profileImage", image);


        Common.showProgress(this);
        Ion.with(this)
                .load(API.ImageUpload)
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

                                        Common.showToast(MyBaccoonActivity.this, (js.getString("message")));
                                        JSONObject resultObj=new JSONObject(js.getString("Result"));
                                        profileImageUrl= resultObj.getString("profileImage");
                                        pref.putString(SpKey.ImageUlr, profileImageUrl);
                                        setImageOnView(profileImageUrl);

                                    } else {
                                          Common.showToast(MyBaccoonActivity.this, (js.getString("message")));
                                    }


                                } catch (JSONException e1) {
                                    e1.printStackTrace();

                                    Common.showToast(MyBaccoonActivity.this, "Network Error");
                                }
                            }
                        } else {
                            e.printStackTrace();
                            Log.e("Exception", "" + e);
                            Common.showToast(MyBaccoonActivity.this, "Network Error");
                        }
                    }
                });
    }

    private void setImageOnView(final String profileImageUrl) {


      //  Ion.with(imgProductPic).error(R.mipmap.bg).placeholder(R.mipmap.default_bg).load(bean.getProductImageUrl());

        Ion.with(this)
                .load(profileImageUrl)
                .setTimeout(60 * 1000)
                .asBitmap()
                .setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {

                        if(e==null)
                        {
                            if(result!=null)
                            {
                               circularProfilePic.setImageBitmap(result);
                                backgroundProfilePic.setImageBitmap(result);
                                BitmapDrawable drawable = (BitmapDrawable) backgroundProfilePic.getDrawable();
                                Bitmap bitmap = drawable.getBitmap();

                                Bitmap blurred = blurRenderScript(bitmap,17);//second parametre is radius
                                backgroundProfilePic.setImageBitmap(blurred);                          //radius decide blur amount




                            }
                        }
                        else
                        {
                            e.printStackTrace();
                            circularProfilePic.setImageResource(R.mipmap.profilepic);
                            backgroundProfilePic.setImageResource(R.mipmap.bg);

                        }


                    }
                });



    }


    private void doCrop(String picturePath) {
        Intent croperIntent = new Intent(this, CropActivity.class);
        croperIntent.putExtra(Constant.IMAGE_PATH, picturePath);
        startActivityForResult(croperIntent, Constant.CROP_REQ);
    }


}
