package com.badlydone.aparto.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.badlydone.aparto.R;

public class aPartoToast {

	public static void ShowToast(Context context, View v, int text_id)
	{
		// show message
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(
								Context.LAYOUT_INFLATER_SERVICE);
    	View layout = inflater.inflate(R.layout.toast_aparto,
               (ViewGroup) v.findViewById(R.id.toast_layout_root));
    	
		TextView text = (TextView) layout.findViewById(R.id.text);
    	text.setText(context.getResources().getString(text_id));
    	
    	Toast toast = new Toast(context);
    	toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    	toast.setDuration(Toast.LENGTH_SHORT);
    	toast.setView(layout);
    	toast.show();
	}
	
}
