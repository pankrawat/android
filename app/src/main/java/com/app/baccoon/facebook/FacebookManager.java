package com.app.baccoon.facebook;

import java.util.Arrays;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

public class FacebookManager implements FacebookCallback<LoginResult> {
	private CallbackManager callbackManager;
	private LoginManager loginManager;
	private Activity activity;
	private String fb_name;
	private String fb_email;
	private String fb_image;
	private String fb_id;
	private OnFacebookLoginListener listener;
	private static FacebookManager manager;
	private AccessTokenTracker accessTokenTracker;
	private AccessToken accessToken;

	public FacebookManager(Activity activity) {
		FacebookSdk.sdkInitialize(activity.getApplicationContext());
		this.activity = activity;
		callbackManager = CallbackManager.Factory.create();
		loginManager = LoginManager.getInstance();
		if (loginManager != null) {
			loginManager.registerCallback(callbackManager, this);
		}
		accessTokenTracker = new AccessTokenTracker() {
			@Override
			protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
				if (currentAccessToken != null) {
					accessToken = currentAccessToken;
				}
			}
		};
		accessToken = AccessToken.getCurrentAccessToken();
		manager = this;
	}

	public static FacebookManager instanceOf(Activity activity) {
		if (manager == null) {
			manager = new FacebookManager(activity);
		}
		return manager;
	}


	public void getCheckins()
	{


		Bundle params = new Bundle();
		//params.putString("with", "location");
/* make the API call */
		new GraphRequest(
				AccessToken.getCurrentAccessToken(),
				"/me/feed",
				null,
				HttpMethod.GET,
				new GraphRequest.Callback() {
					public void onCompleted(GraphResponse response) {
            /* handle the result */
					if(response!=null)
					{
						JSONObject jsonObject = response.getJSONObject();
						Log.e("checkins json",jsonObject.toString());
					}

					}
				}
		).executeAsync();	}

	public void doLogin() {

		if (loginManager != null) {
			loginManager.logInWithReadPermissions(activity, Arrays.asList("public_profile", "user_friends","email","user_posts"));
		} else {
			if (listener != null) {
				listener.onError("Login manager is not initialized");
			}
		}

	}

	public void doLoginWithPublish() {
		if (loginManager != null) {
			loginManager.logInWithPublishPermissions(activity, Arrays.asList("publish_actions"));
		} else {
			if (listener != null) {
				listener.onError("Login manager is not initialized");
			}
		}
	}

	public void setOnLoginListener(OnFacebookLoginListener listener) {
		this.listener = listener;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSuccess(LoginResult result) {
//
//		/* make the API call */
//		new GraphRequest(
//				AccessToken.getCurrentAccessToken(),
//				"/{check-in-id}",
//				null,
//				HttpMethod.GET,
//				new GraphRequest.Callback() {
//					public void onCompleted(GraphResponse response) {
//            /* handle the result */
//					}
//				}
//		).executeAsync();

		GraphRequest request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
			@Override
			public void onCompleted(JSONObject object, GraphResponse response) {

				try {
					Log.e("", "jason = " + String.valueOf(object));
					JSONObject jsonObject = response.getJSONObject();
					Log.e("gopal json",jsonObject.toString());
					fb_name = jsonObject.getString("name");
					if (jsonObject.has("email")) {
						fb_email = jsonObject.getString("email");
					} else {
						fb_email = "";
					}
					JSONObject picture = jsonObject.getJSONObject("picture");
					JSONObject picData = picture.getJSONObject("data");
					fb_image = picData.getString("url");
					fb_id = jsonObject.getString("id");
					if (listener != null) {
						listener.onFacebookLogin(fb_name, fb_email,fb_id,fb_image);
					} else {
						listener.onError("OnFacebookLoginListener not registered");
					}
				} catch (Exception e) {
					e.printStackTrace();
					//listener.onError(e.getMessage());
				}
			}

		});

		Bundle parameters = new Bundle();
		parameters.putString("fields", "id,email,name,picture.width(200).height(200)");
		request.setParameters(parameters);
		request.executeAsync();
	}

	@Override
	public void onCancel() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {

				Toast.makeText(activity, "Facebook login was canceled", Toast.LENGTH_SHORT).show();

			}
		});
	}

	@Override
	public void onError(final FacebookException error) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				listener.onError(error.getMessage());
//				Toast.makeText(activity, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

			}
		});
	}

	public AccessToken getCurrentToken() {
		return accessToken;
	}

	public void onDestroy() {
		accessTokenTracker.startTracking();
	}

}
