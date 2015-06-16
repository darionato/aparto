package com.badlydone.aparto.utils;

import com.badlydone.aparto.R;
import com.badlydone.aparto.core.aPartoCore;
import com.badlydone.aparto.core.aPartoProfile;

import android.content.Context;
import android.content.SharedPreferences;

public class aPartoMigrate {

	private static final String PREFS_NAME = "aPartoOptions";
	private Context _Context;
	
	public aPartoMigrate(Context context)
	{
		_Context = context;
	}
	
	public void Migrate(aPartoCore core)
	{
		//SaveFakeOptions();
		aPartoProfile mig = OpenOptions();
		
		if (mig.getPhoneNumber().length() != 0 &&
				mig.getMessageText().length() != 0)
			
		{
		
			// set the defaul name
			mig.setProfileName(
				_Context.getResources().getString(R.string.default_profile));
			
			// add the profile
			core.addProfile(mig);
			
			// delete the settings
			SharedPreferences settings = _Context.getSharedPreferences(PREFS_NAME, 0);
			SharedPreferences.Editor editor = settings.edit();
			
			editor.clear();
			
			editor.commit();
			
		}
			
		
		
	}
	
	private aPartoProfile OpenOptions()
	{
		
		aPartoProfile ret = new aPartoProfile();
		
		// get the saved number
	    SharedPreferences settings = _Context.getSharedPreferences(PREFS_NAME, 0);
	       
        // open the number
	    ret.setPhoneNumber(settings.getString(
       		_Context.getResources().getString(R.string.setting_number), ""));
	       
	    // open the SMS
	    ret.setMessageText(settings.getString(
    		_Context.getResources().getString(R.string.setting_sms), ""));

	    // open the delivery reports
	    ret.setDeliveryReport(settings.getBoolean(
	    	_Context.getResources().getString(R.string.setting_delivery_reports), false));
	    
	    return ret;
	    
	}
	/*
	private void SaveFakeOptions()
	{
		
		SharedPreferences settings = _Context.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		// save the number
		editor.putString(_Context.getResources().getString(R.string.setting_number), 
				"144144144");
		
		// save the message
		editor.putString(_Context.getResources().getString(R.string.setting_sms), 
				"ciao gailna");

		// save the delivery report required
		editor.putBoolean(_Context.getResources().getString(R.string.setting_delivery_reports), 
				true);
		
		
		// Commit the edits!
		editor.commit();
		
		
	}
	*/
	
}
