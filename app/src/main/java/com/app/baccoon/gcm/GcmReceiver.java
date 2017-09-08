package com.app.baccoon.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.app.baccoon.utils.Constant;



public class GcmReceiver extends WakefulBroadcastReceiver {

	SharedPreferences prefs;
	@Override
	public void onReceive(Context context, Intent intent) {

		prefs= context.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
		Log.d("Push reciever", "called");
		
		Bundle extras=intent.getExtras();
		Log.e("Push reciever data",""+extras.getString("msg"));
	//	Log.e("Push reciever data",""+extras.toString());
//
//		for (String key : extras.keySet()) {
//			Log.e(key,extras.get(key)+"");
//			//Toast.makeText(context, "mgmhmm"+extras.get(key), Toast.LENGTH_SHORT).show();
//		}

		if(prefs.getBoolean(Constant.isNotificationEnabled,true) || true) {
			Log.e("call service","in reciver");
			ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
			startWakefulService(context, (intent.setComponent(comp)));
			setResultCode(Activity.RESULT_OK);
		}
		else
		{
			Log.e("push service disabled","");
		}
	}
}