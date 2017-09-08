package com.app.baccoon.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.facebook.FacebookManager;
import com.app.baccoon.facebook.OnFacebookLoginListener;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.Constant;
import com.app.baccoon.utils.PasswordValidator;
import com.app.baccoon.utils.SharedPreference;
import com.app.baccoon.utils.SpKey;
import com.facebook.login.LoginManager;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener, OnFacebookLoginListener {

    private TextView btnLogin;
    private LinearLayout btnFB;
    private LinearLayout btnVK;
    private TextView btnSignup;
    private EditText txtFirstName, txtLastName, txtEmail, txtPass, txtCnfPass;
    private TextView spinerPickup, spinerPopup;
    private String firstName, lastName, email, pass, cnfpass;
    private String[] userType = {"Company", "Person"};
    private ArrayAdapter adapterUserType;
    private Context ctx;
    private String strUserType = "";
    private PopupWindow popupWindow;

    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.DOCS
    };
    private JsonObject jsonObject;
    private String socialId = "";
    private String userID;
    private User user;
    private SharedPreference pref;
    private Spinner spiner;
    private String socialType = "0";
    private boolean isFBLogin = false;
    private FacebookManager fbManager;
    private String fbid;
    private boolean isPasswordReqiuired = true;
    private boolean isPopupShowing = false;


    public BroadcastReceiver MyLoginBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("OnRecive call", "===");
//			if(intent.getAction().equals(AppConstants.FINISH_LOGIN))
           SignupActivity.this.finish();


        }
    };
    private IntentFilter intentFilter;
    private Intent intent;

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        this.unregisterReceiver(MyLoginBroadcast);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        ctx = this;
        intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.FINISH_LOGIN);
        this.registerReceiver(MyLoginBroadcast, intentFilter);
        initViews();
        pref = SharedPreference.getInstance(ctx);


    }


    private void showCarConditionPopUp() {

        popupWindow = new PopupWindow(this);
        Common.hideSoftKeyBoard(this);
        View tmpView = this.getLayoutInflater().inflate(R.layout.spiner_popup_layout, null);


        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(tmpView);
        popupWindow.showAsDropDown(spinerPickup, 0, 0);
        isPopupShowing = true;

        TextView txtCompmany = (TextView) tmpView.findViewById(R.id.txtCompany);
        TextView txtPerson = (TextView) tmpView.findViewById(R.id.txtPerson);
        TextView txtclose = (TextView) tmpView.findViewById(R.id.txtPerson_dash);
        txtclose.setVisibility(View.GONE);

        txtCompmany.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                strUserType = userType[0];
                spinerPickup.setText(strUserType);
                popupWindow.dismiss();
                isPopupShowing = false;
            }
        });

        txtPerson.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                strUserType = userType[1];
                spinerPickup.setText(strUserType);
                popupWindow.dismiss();
                isPopupShowing = false;
            }
        });


    }

    public void showPopup() {


        LayoutInflater layoutInflater
                = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.spiner_popup_layout, null);
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        isPopupShowing = true;
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAsDropDown(spinerPickup, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isPopupShowing = false;
            }
        });


    }

    public void hidePopupWindow(View v) {
        if (isPopupShowing) {
            popupWindow.dismiss();
            isPopupShowing = false;
        }
    }


    private void initViews() {

        btnSignup = (TextView) findViewById(R.id.btnSignup);
        btnFB = (LinearLayout) findViewById(R.id.btnFB);
        btnVK = (LinearLayout) findViewById(R.id.btnVK);
        btnLogin = (TextView) findViewById(R.id.btnSignin);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPass = (EditText) findViewById(R.id.txtPassword);
        txtCnfPass = (EditText) findViewById(R.id.txtCnfPassword);
        spinerPickup = (TextView) findViewById(R.id.spinerPick);
        // spiner = (Spinner) findViewById(R.id.spiner);
        btnSignup.setOnClickListener(this);
        btnFB.setOnClickListener(this);
        btnVK.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        spinerPickup.setOnClickListener(this);
        fbManager = new FacebookManager(this);
        fbManager.setOnLoginListener(this);


    }


    @Override
    public void onClick(View v) {


        if (v.getId() == btnSignup.getId()) {
            Log.e("signup image clicked", "gfh");
            Common.hideSoftKeyBoard(this);

            registerUser();

        }
        if (v.getId() == btnFB.getId()) {
            Log.e("fb image clicked", "ghg");
            if (Common.isConnected(this)) {
                fbManager.doLogin();
            }


        }
        if (v.getId() == btnVK.getId()) {
            Log.e("vk image clicked", "gf");
            if (Common.isConnected(this))


            {
                VKSdk.login(this, sMyScope);
            }


        }
        if (v.getId() == btnLogin.getId()) {
            Log.e("signin image clicked", "h");
//            Toast.makeText(getApplicationContext(),"Login clicked", Toast.LENGTH_LONG).show();
            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(i);
            finish();

        }

        if (v.getId() == spinerPickup.getId()) {
            Log.e("spiner clicked", "gg");

            showCarConditionPopUp();

        }


    }


    private void registerUser() {

        firstName = txtFirstName.getText().toString();
        lastName = txtLastName.getText().toString();

        email = txtEmail.getText().toString();


        if (validData() && (!isPasswordReqiuired || validPass()) && Common.isConnected(this)) {

            Log.e("yes submitted", "ff");
            createProfile();

        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isPopupShowing) {
            popupWindow.dismiss();
            isPopupShowing = false;
        }

    }

    private boolean validData() {
        Boolean ifValid = false;
        if ((firstName.replaceAll(" ", "").equals("")) || (lastName.replaceAll(" ", "").equals("")) || (email.replaceAll(" ", "").equals("")) || (spinerPickup.getText().toString().equalsIgnoreCase("Signup as"))) {

            Toast.makeText(getApplicationContext(), "Please fill all the fields.", Toast.LENGTH_SHORT).show();
            return false;
        } else {


            if (!isEmailValid(email)) {
                Toast.makeText(getApplicationContext(), "Please enter valid email id.", Toast.LENGTH_SHORT).show();
                txtEmail.setFocusable(true);
                return ifValid;
            } else {
                ifValid = true;
                return ifValid;
            }
        }
    }


    private boolean validPass() {


        pass = txtPass.getText().toString();
        cnfpass = txtCnfPass.getText().toString();
        if ((pass.replaceAll(" ", "").equals("")) || (cnfpass.replaceAll(" ", "").equals(""))) {
            Toast.makeText(getApplicationContext(), "Please fll all the fields.", Toast.LENGTH_SHORT).show();
            return false;
        } else if ((pass.length() < 8)) {
            Toast.makeText(getApplicationContext(), "Password must be 8-15 characters long.", Toast.LENGTH_SHORT).show();

            return false;
        } else if ((!new PasswordValidator().validate(pass))) {
            Toast.makeText(getApplicationContext(), "Password must contain atleast 1 uppercase, 1 lowercase, 1 Numeric and 1 Special character including @ . ! ", Toast.LENGTH_SHORT).show();

            return false;
        } else if (!pass.equals(cnfpass)) {
            Toast.makeText(getApplicationContext(), "Password does not match.", Toast.LENGTH_SHORT).show();

            return false;
        } else {
            return true;
        }


    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private void createProfile() {
        // 1=fb, 2=vk, 0=email signup

/*
        {
            "deviceToken": "klfjdkjw978kj43j4kj34h3kj3",
                "deviceType": "ios",
                "socialId": "40",
                "socialType": "0",
                "firstName": "Ajay",
                "lastName": "Ajay",
                "email": "ajay@techaheadcorp.com",
                "password": "123456",
                "
            ""company"": ""appsquadz"""
        }
        */
        jsonObject = new JsonObject();

        jsonObject.addProperty("deviceToken", ""+pref.getString(SpKey.DeviceToken,""));
        jsonObject.addProperty("deviceType", "android");

        jsonObject.addProperty("socialType", socialType);
        jsonObject.addProperty("socialId", socialId);
        jsonObject.addProperty("firstName", firstName);
        jsonObject.addProperty("lastName", lastName);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", pass);
        jsonObject.addProperty("company", "" + strUserType);


        Common.showProgress(ctx);
        Ion.with(this)
                .load(API.Signup)
                .setTimeout(60 * 1000)
                .setJsonObjectBody(jsonObject)
                .asString()

                .setCallback(new FutureCallback<String>() {

                    @Override
                    public void onCompleted(Exception e, String jsonString) {


                        Log.d("calllll", "callllll");
                        Common.hideProgress(ctx);

                        if (e == null) {
                            if (jsonString != null && jsonString != "") {
                                Log.e("json2", jsonString);



                                 /*   {
                                        "isSuccess": "True",
                                            "Message": "SignUp successfully done",
                                            "messageCode": "1",
                                            "Result": [
                                        {
                                            "userId": "132",
                                                "firstName": "Ajay",
                                                "lastName": "chauhan",
                                                "email": "ajay@techaheadcorp.com",
                                                "
                                            ""company"": ""appsquadz"""
                                            "socialType": "0"
                                        }
                                        ]
                                    }
                                    */

                                try {
                                    //   JSONObject resultObj = new JSONObject(jsonString);
                                    JSONObject js = new JSONObject(jsonString);
//                                    Log.e("array", "" + js.getString("status"));
                                    if (js.getBoolean("isSuccess")) {
                                       Common.showToast(ctx, js.getString("message"));


                                        pref.putString(SpKey.UID, js.getString("Result"));
                                        pref.putString(SpKey.FIRST_NAME, txtFirstName.getText().toString().trim());
                                        pref.putString(SpKey.LastName, txtLastName.getText().toString().trim());
                                        pref.putString(SpKey.EMAIL, txtEmail.getText().toString().trim());


                                        pref.putBoolean(SpKey.isLogged, true);

                                       if(strUserType.equalsIgnoreCase("Company"))
                                       {
                                            intent = new Intent(SignupActivity.this, PaymentActivity.class);
                                           intent.putExtra("fromSignup",true);
                                           pref.putBoolean(SpKey.onPaymentScreen,true);
                                       }
                                        else {
                                           intent = new Intent(SignupActivity.this, HomeActivity.class);
                                           intent.putExtra("fromSignup",false);
                                       }
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();

//                                       if (js.getString("messageCode").equals("1")) {
//
//
//                                            Common.showToast(ctx, js.getString("message")); // successfull registered
//
//                                            //save user data in user
//                                            userID = js.getString("uid");
//                                            user = User.getInstance();
//                                            user.setFirstName(firstName);
//                                            user.setLastName(lastName);
//                                            user.setEmail(email);
//                                            user.setUid(userID);
//
//                                            //save user data in shared preference
//                                            pref.putString(SpKey.UID, userID);
//                                            pref.putString(SpKey.FIRST_NAME, firstName);
//                                            pref.putString(SpKey.LastName, lastName);
//                                            pref.putString(SpKey.EMAIL, email);


                                    } else {
                                        Common.showToast(ctx, js.getString("message"));// already registered
                                    }


                                    //        [{"status":"1","message":"Thank you! for Submitting Appointment Request ."}]

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }


                            }

                        } else {
                            Common.showToast(ctx, "Network Error");

                            e.printStackTrace();

                        }


                    }
                });


    }


    @Override
    public void onFacebookLogin(String name, String email, String id,
                                String imageUrl) {
        Log.e("name=", name);
        Log.e("email=", email);
        Log.e("id=", id);
        //  Toast.makeText(getApplicationContext(), "name="+name+"\nemail="+email+"\nfbid="+id,Toast.LENGTH_LONG).show();

        hideFields();
        isFBLogin = true;
        fbid = id;
        socialId = id;
        socialType = "1";
        isPasswordReqiuired = false;
        pass = "";
        txtEmail.setText(email);
        txtFirstName.setText(name);


    }

    @Override
    public void onError(String message) {
        // TODO Auto-generated method stub

        Log.e("error msg=", message);
        String errorCode = "190";
        if (message.contains(errorCode)) {
            LoginManager.getInstance().logOut();
            Toast.makeText(getApplicationContext(), "Please login again.", Toast.LENGTH_LONG).show();
            //fbManager.doLogin();
        }


    }

    @Override
    protected void onResume() {


        super.onResume();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        fbManager.onActivityResult(requestCode, resultCode, data);


        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // User passed Authorization
                //startTestActivity();
                Log.e("start test activity", "" + res.userId);
                callRegisterByVK(res);


            }

            @Override
            public void onError(VKError error) {
                // User didn't pass Authorization
                Log.e("onError", "User didn't pass Authorization" + error.toString());


            }
        };

        if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void callRegisterByVK(VKAccessToken res) {

        socialId = res.userId;
        socialType = "2";
        pass = "";

        isPasswordReqiuired = false;
        txtEmail.setText(email);
        hideFields();


    }


    private void showFields() {
        btnFB.setVisibility(View.VISIBLE);

        btnVK.setVisibility(View.VISIBLE);
        txtPass.setVisibility(View.VISIBLE);
        txtCnfPass.setVisibility(View.VISIBLE);
    }

    private void hideFields() {
        btnFB.setVisibility(View.GONE);
        btnVK.setVisibility(View.GONE);
        txtPass.setVisibility(View.GONE);
        txtCnfPass.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {

        if (socialType.equals("0"))

        {
            super.onBackPressed();
        } else {
            socialType = "0";
            socialId = "";
            isPasswordReqiuired = true;
            txtFirstName.setText("");
            txtLastName.setText("");
            txtEmail.setText("");

            spinerPickup.setText("Signup as");

            showFields();
        }
    }
}





