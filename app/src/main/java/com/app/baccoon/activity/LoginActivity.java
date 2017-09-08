package com.app.baccoon.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.facebook.FacebookManager;
import com.app.baccoon.facebook.OnFacebookLoginListener;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.app.baccoon.utils.Constant;
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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener ,OnFacebookLoginListener {

    private TextView btnLogin, btnGuest, btnSignup,btnForgetPass;
    private EditText txtEmail, txtPass;

    private String email,password, fbid;
    private boolean isFBLogin=false;
    private FacebookManager fbManager;


Intent intent;
    private JsonObject jsonObject;
    private String socialType="0";
    private String socialId="";
    private Context ctx;
    private boolean isPasswordReqiuired=true;
    private LinearLayout btnVK,btnFB;

    private static final String[] sMyScope = new String[]{
            VKScope.FRIENDS,
            VKScope.WALL,
            VKScope.PHOTOS,
            VKScope.NOHTTPS,
            VKScope.MESSAGES,
            VKScope.DOCS
    };
    private SharedPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx=this;
        pref=SharedPreference.getInstance(ctx);
        initViews();
     /*   intent=new Intent(LoginActivity.this, SignupActivity.class);*/
        fbManager = new FacebookManager(this);
        fbManager.setOnLoginListener(this);
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        btnFB.setOnClickListener(this);
        btnVK.setOnClickListener(this);
        btnForgetPass.setOnClickListener(this);

        }

    private void initViews() {

        btnLogin=(TextView) findViewById(R.id.btnLogin);
        btnSignup=(TextView) findViewById(R.id.btnSignup);
        btnFB=(LinearLayout) findViewById(R.id.btnFB);
        btnVK=(LinearLayout) findViewById(R.id.btnVK);
        btnForgetPass=(TextView) findViewById(R.id.btnFogotPass);
        txtEmail=(EditText) findViewById(R.id.txtEmail);
        txtPass=(EditText) findViewById(R.id.txtPassword);

    }
    public void onClick(View v) {


        if(v.getId()==btnLogin.getId())
        {
            Common.hideSoftKeyBoard(this);
            isPasswordReqiuired=true;
             loginUser();

        }
        if(v.getId()==btnFB.getId())
        {
            Log.e("fb clicked", "");
            if(Common.isConnected(this))
            {fbManager.doLogin();}
        }


        if(v.getId()==btnVK.getId())
        {
           Log.e("vk clicked", "");
            if(Common.isConnected(this))
            {VKSdk.login(this, sMyScope);}
        }




        if(v.getId()==btnForgetPass.getId())
        {
            //Toast.makeText(getApplicationContext(), "Forgot password clicked", Toast.LENGTH_LONG).show();


           Intent i=new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(i);
        }


if(v.getId()==btnSignup.getId())
        {
            Intent i=new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void loginUser() {

        password=txtPass.getText().toString();
        email=txtEmail.getText().toString();
        if(isPasswordReqiuired)
        {
if(validData() && Common.isConnected(this))
{callLoginApi();}
        }

        else
        {
         if(Common.isConnected(this))callLoginApi();
        }


    }

    private void callLoginApi() {

/* login json request
        {
            "deviceToken": "klfjdkjw978kj43j4kj34h3kj3",
                "deviceType": "ios",
                "socialId": "40",
                "socialType": "2",
                "email": "ajay@techaheadcorp.com",
                "password": "123456",
        }
        */
        jsonObject = new JsonObject();

        jsonObject.addProperty("deviceToken", ""+pref.getString(SpKey.DeviceToken,""));
        jsonObject.addProperty("deviceType", "android");
        jsonObject.addProperty("socialType", socialType);
        jsonObject.addProperty("socialId", socialId);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);



        Common.showProgress(this);
        Ion.with(this)//ec2-52-205-20-98.compute-1.amazonaws.com/
                .load("http://ec2-52-205-20-98.compute-1.amazonaws.com/baccoon/index.php/login")
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
                                        pref.putBoolean(SpKey.isLogged, true);

                                      JSONObject resultObj=new JSONObject(js.getString("Result"));
                                        pref.putString("userid", resultObj.getString("userId"));


                                        pref.putString(SpKey.UID, resultObj.getString("userId"));
                                            pref.putString(SpKey.FIRST_NAME, resultObj.getString("firstName"));
                                            pref.putString(SpKey.LastName, resultObj.getString("lastName"));
                                            pref.putString(SpKey.EMAIL, resultObj.getString("email"));
                                        pref.putFloat(SpKey.WALLET, (float)resultObj.getInt("wallet"));
                                        pref.putInteger(SpKey.ProductCount, resultObj.getInt("product_count"));

                                        pref.putString(SpKey.ImageUlr, resultObj.getString("profileImage"));

                                        pref.putString(SpKey.DHL_Price, resultObj.getString("by_dhl_fee"));
                                        pref.putString(SpKey.Post_Price, resultObj.getString("by_post_office_fee"));

                                      if(resultObj.getString("by_dhl").equalsIgnoreCase("1"))
                                      {
                                          pref.putBoolean(SpKey.isDHL, true);
                                      }
                                          else
                                      {
                                          pref.putBoolean(SpKey.isDHL, false);
                                      }


                                        if(resultObj.getString("put_by_buyer").equalsIgnoreCase("1"))
                                        {
                                            pref.putBoolean(SpKey.isBuyer, true);
                                        }
                                        else
                                        {
                                            pref.putBoolean(SpKey.isBuyer, false);
                                        }
                                        if(resultObj.getString("by_post_office").equalsIgnoreCase("1"))
                                        {
                                            pref.putBoolean(SpKey.isPost, true);
                                        }
                                        else
                                        {
                                            pref.putBoolean(SpKey.isPost, false);
                                        }




                                        if(resultObj.getString("company").equalsIgnoreCase("Company") && resultObj.getInt("isPaymentMade")==0)
                                        {


                                            intent = new Intent(LoginActivity.this, PaymentActivity.class);
                                            pref.putBoolean(SpKey.onPaymentScreen,true);
                                            intent.putExtra("fromSignup",true);

                                            startActivity(intent);
                                            finish();

                                            Intent intentForFinishBack = new Intent().setAction(Constant.FINISH_LOGIN);
                                            sendBroadcast(intentForFinishBack);


                                        }
                                        else if(resultObj.getString("company").equalsIgnoreCase("Person")){
                                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            intent.putExtra("fromSignup",false);
                                            startActivity(intent);
                                            finish();
                                            Intent intentForFinishBack = new Intent().setAction(Constant.FINISH_LOGIN);
                                            sendBroadcast(intentForFinishBack);



                                        }
                                        else
                                        {
                                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            intent.putExtra("fromSignup",false);
                                            startActivity(intent);
                                            finish();
                                            Intent intentForFinishBack = new Intent().setAction(Constant.FINISH_LOGIN);
                                            sendBroadcast(intentForFinishBack);

                                        }

                                       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


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


                                    } else  {
                                        txtEmail.setText("");
                                        txtPass.setText("");
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

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

//	    if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
//	    {
//	    	isValid=true;
//	    }
        String expression = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    private boolean validData() {
        Boolean ifValid = false;
        if ((email.replaceAll(" ", "").equals("")) || (password.replaceAll(" ", "").equals(""))) {

            Toast.makeText(getApplicationContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return false;
        } else {


            if (!isEmailValid(email)) {
                Toast.makeText(getApplicationContext(), "Please enter valid email id.", Toast.LENGTH_SHORT).show();
                txtEmail.setFocusable(true);
                return ifValid;
            }  else {
                ifValid = true;
                return ifValid;
            }
        }
    }





    @Override
    public void onFacebookLogin(String name, String email, String id,
                                String imageUrl) {
        Log.e("name=", name);
        Log.e("email=", email);
        Log.e("id=", id);
       // Toast.makeText(getApplicationContext(), "name="+name+"\nemail="+email+"\nfbid="+id,Toast.LENGTH_LONG).show();


        isFBLogin=true;
        fbid=id;
        socialId=id;
        password="";
        socialType="1";
        this.email="";
        isPasswordReqiuired=false;
       loginUser();

    }

    @Override
    public void onError(String message) {
        // TODO Auto-generated method stub

        Log.e("error msg=", message);
        String errorCode="190";
        if(message.contains(errorCode))
        {
            LoginManager.getInstance().logOut();
            Toast.makeText(getApplicationContext(), "Please login again.",Toast.LENGTH_LONG).show();
            //fbManager.doLogin();
        }


    }

    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);

        fbManager.onActivityResult(requestCode, responseCode, data);




        VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // User passed Authorization
                //startTestActivity();
                Log.e("start test activity", ""+res.userId);
                callLoginByVK(res);



            }

            @Override
            public void onError(VKError error) {
                // User didn't pass Authorization
                Log.e("onError", "User didn't pass Authorization" + error.toString());


            }
        };

        if (!VKSdk.onActivityResult(requestCode, responseCode, data, callback)) {
            super.onActivityResult(requestCode, responseCode, data);
        }


    }


    private void callLoginByVK(VKAccessToken res) {

        socialId=res.userId;
        socialType="2";
        password="";
        email="";
        isPasswordReqiuired=false;



        loginUser();





    }

}
