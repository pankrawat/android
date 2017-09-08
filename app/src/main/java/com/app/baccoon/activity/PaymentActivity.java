package com.app.baccoon.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.callback.PaymentDoneListener;
import com.app.baccoon.fragment.PaymentCustomFragment;
import com.app.baccoon.fragment.PaymentPicturewiseFragment;
import com.app.baccoon.fragment.PaymentThreeFragment;
import com.app.baccoon.fragment.PaymentFullMembershipFragment;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.app.baccoon.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.vk.sdk.api.model.VKApiUserFull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity implements PaymentDoneListener{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView btnBack, btnCancel;
    private AlertDialog dialog;
    private ViewPagerAdapter adapter;
    private SharedPreference pref;
    public static String uid = "";
    private Toolbar toolbar;
    boolean fromSignup = false;
private RelativeLayout parent;
    Context context;


/*
    [03/05/16 6:15:34 pm] Muqeem Ahmad: 4020024001738242
            [03/05/16 6:15:45 pm] Muqeem Ahmad: 06/2021

  */


    private static final String CONFIG_CLIENT_ID = "AXRAI9Nuk-n2vtMwnQjsi0HTntv0wc2zMaKN_Zp8820Jm11TF_7-fdRMl32eErQnTbt0AchEKnm1CAEU";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final int REQUEST_CODE_PAYMENT_CUSTOM = 1;
    private static final int REQUEST_CODE_PAYMENT_PICYUREWISE = 2;
    private static final int REQUEST_CODE_PAYMENT_FullMembership = 3;


    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID).rememberUser(false)
// the following are only used in PayPalFuturePaymentActivity.
            .merchantName("Baccoon");

    PayPalPayment adcarpostcharges;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_pager);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        parent = (RelativeLayout) findViewById(R.id.parentLay);
        context = PaymentActivity.this;
        pref = SharedPreference.getInstance(this);
        uid = pref.getString("userid", "");



        fromSignup = getIntent().getBooleanExtra("fromSignup", false);
        if (fromSignup) {
            btnCancel.setVisibility(View.VISIBLE);

        }
        else
        {
            btnCancel.setVisibility(View.GONE);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(Common.isConnected(context))
               { logout();}
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (pref.getBoolean(SpKey.isCompany, false)) {
            if (!pref.getBoolean(SpKey.isPaymentMade, false)) {
                btnCancel.setVisibility(View.VISIBLE);
            } else {
                btnCancel.setVisibility(View.GONE);
            }

        }


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
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.addFragment(new PaymentFullMembershipFragment(), "Full Membership");
        adapter.addFragment(new PaymentCustomFragment(), "Custom");
        adapter.addFragment(new PaymentPicturewiseFragment(), "Picturewise");


        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);


    }

    @Override
    public void PaymentDoneListener() {
        Log.e("done","done");

    }

    public void paymentDone(float wallet, String amount) {

        
/*
{
"user_id":"1",
"deducted_amount":"200",
"remaining_amount":"300"
}*/


        float deductedAmount=0;
        try {
            deductedAmount=Float.parseFloat(amount);
        }catch(NumberFormatException nfe)
        {
            nfe.printStackTrace();
            deductedAmount=0;
        }
        final Float remainingAmount= wallet-deductedAmount;
        JsonObject jsonObject= new JsonObject();

        jsonObject.addProperty("user_id", pref.getString(SpKey.UID, ""));
        jsonObject.addProperty("deducted_amount", amount);
        jsonObject.addProperty("remaining_amount", remainingAmount);


        if (Common.isConnected(context)) {

            Ion.with(this)
                    .load(API.Update_Wallet)
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
                                            pref.putFloat(SpKey.WALLET,remainingAmount);
                                            paymentDone();




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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((adapter.getCurrentFragment() instanceof PaymentCustomFragment)) {
            if (requestCode == REQUEST_CODE_PAYMENT_CUSTOM) {
                Log.e("custom", "custom");
                (adapter.getCurrentFragment()).onActivityResult(requestCode, resultCode, data);
            }
        } else if ((adapter.getCurrentFragment() instanceof PaymentPicturewiseFragment)) {
            if (requestCode == REQUEST_CODE_PAYMENT_PICYUREWISE) {
                Log.e("picturewise", "picture");
                (adapter.getCurrentFragment()).onActivityResult(requestCode, resultCode, data);
            }
        } else if ((adapter.getCurrentFragment() instanceof PaymentFullMembershipFragment)) {
            if (requestCode == REQUEST_CODE_PAYMENT_FullMembership) {
                Log.e("membership", "member");
                (adapter.getCurrentFragment()).onActivityResult(requestCode, resultCode, data);
            }
        } else {
            Log.e("nothing", "nothing");
        }


    }

    private void logout() {

        pref.deletePreference();
        pref.putString("FIRST_TIME", "False");
        pref.putBoolean(SpKey.onPaymentScreen,false);
        Intent intent = new Intent(PaymentActivity.this, GetLocationActivity.class);
        startActivity(intent);
        finish();
    }

    public void paymentDone()
    {
        Log.e("done","done123");
        if(fromSignup)
        {
            pref.putBoolean(SpKey.onPaymentScreen,false);
            startActivity(new Intent(PaymentActivity.this, HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            finish();
        }
        else
        {
            Common.showToast(this,"Thank you for payment.");
            finish();
        }
    }
}