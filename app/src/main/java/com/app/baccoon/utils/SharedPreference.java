package com.app.baccoon.utils;



import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreference {

	private static SharedPreference pref;
	private SharedPreferences sharedPreference;
	private Context ctx;
	private Editor editor;
	
	private SharedPreference(Context ctx)
	{
		this.ctx=ctx;
		sharedPreference=ctx.getSharedPreferences("checkpreference", Context.MODE_PRIVATE);
		editor=sharedPreference.edit();

	}

	public static SharedPreference getInstance(Context ctx)
	{
		if(pref==null)
		{
			pref=new SharedPreference(ctx);
		}
		return pref;
	}
	
	public void putString(String key,String value)
	{
		editor.putString(key, value).commit();

	}
	
	public void putBoolean(String key,boolean value)
	{
		editor.putBoolean(key, value);
		editor.commit();
	}
	public void putInteger(String key, int value)
	{
		editor.putInt(key, value);
		editor.commit();
	}
	
	public void putFloat(String key,Float value)
	{
		editor.putFloat(key, value);
		editor.commit();
	}


	public void putInt(String key,int value)
	{
		editor.putInt(key, value);
		editor.commit();
	}
	public String getString(String key,String defValue)
	{
		return sharedPreference.getString(key, defValue);
	}



	public int getInt(String key,int defValue)
	{
		return sharedPreference.getInt(key, defValue);
	}


	public boolean getBoolean(String key,boolean defValue)
	{
		return sharedPreference.getBoolean(key, defValue);
	}
	public int getInteger(String key,int defValue)
	{
		return sharedPreference.getInt(key, defValue);
	}



	public Float getFloat(String key,Float defValue)
	{
		return sharedPreference.getFloat(key, defValue);
	}
	
	public void deletePreference()
	{
		 // editor.remove(AppConstants.Email_Key);
		  //editor.apply();
		editor.clear().commit();


	}
	

}
