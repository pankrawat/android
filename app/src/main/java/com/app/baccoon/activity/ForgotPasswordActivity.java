package com.app.baccoon.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baccoon.R;
import com.app.baccoon.utils.API;
import com.app.baccoon.utils.Common;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener { /* , OnFacebookLoginListener */


    private EditText txtEmail;
    TextView btnSubmit;

    private String email, password;
    private boolean isFBLogin = false;
    //   private FacebookManager fbManager;

    Intent intent;
    private JsonObject jsonObject;

    private Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        ctx = this;


        initViews();


        intent = new Intent(ForgotPasswordActivity.this, SignupActivity.class);
//        fbManager = new FacebookManager(this);
//        fbManager.setOnLoginListener(this);
        btnSubmit.setOnClickListener(this);


    }

    private void initViews() {

        btnSubmit = (TextView) findViewById(R.id.btnSubmit);

        txtEmail = (EditText) findViewById(R.id.txtEmail);



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (txtEmail != null) {
            if (txtEmail.requestFocus()) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }

    }

    public void onClick(View v) {


        if (v.getId() == btnSubmit.getId()) {

            submitUserEmail();

        }


    }


    private void submitUserEmail() {


        email = txtEmail.getText().toString();
        Common.hideSoftKeyBoard(this);


        if (validData()) {

           if (Common.isConnected(this))
           {
               callForgotPasswordApi();
           }

        }


    }

    private void callForgotPasswordApi() {

/* forgot password json request/response
        Request Parmeter :
{
"email": "ajay@techaheadcorp.com",
}

Response:
{
"isSuccess": "True",
"Message": "Password has been sent to your email successfully",
"Result": []
}
        */
        jsonObject = new JsonObject();

        jsonObject.addProperty("email", email);


        Common.showProgress(ctx);
        Ion.with(this)
                .load(API.ForgotPassword)
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


                                try {
                                    //   JSONObject resultObj = new JSONObject(jsonString);
                                    JSONObject js = new JSONObject(jsonString);
//                                    Log.e("array", "" + js.getString("status"));
                                    if (js.getBoolean("isSuccess")) {
                                        Common.showToast(ctx, js.getString("message"));
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
        if ((email.replaceAll(" ", "").equals(""))) {

            Toast.makeText(getApplicationContext(), "Please fill email", Toast.LENGTH_SHORT).show();
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




}
