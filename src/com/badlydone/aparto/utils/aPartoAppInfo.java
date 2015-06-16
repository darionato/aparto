package com.badlydone.aparto.utils;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;

public class aPartoAppInfo {

	public static String getVersionName(Context context, Class<? extends Application> cls) 
	{
	  try {
	    ComponentName comp = new ComponentName(context, cls);
	    PackageInfo pinfo = context.getPackageManager().getPackageInfo(comp.getPackageName(), 0);
	    return pinfo.versionName;
	  } catch (android.content.pm.PackageManager.NameNotFoundException e) {
	    return "";
	  }
	}
	
	public static int getVersionCode(Context context, Class<? extends Application> cls) 
	{
	  try {
	    ComponentName comp = new ComponentName(context, cls);
	    PackageInfo pinfo = context.getPackageManager().getPackageInfo(comp.getPackageName(), 0);
	    return pinfo.versionCode;
	  } catch (android.content.pm.PackageManager.NameNotFoundException e) {
	    return 0;
	  }
	}
	
}
