package com.badlydone.aparto;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.badlydone.aparto.utils.ChangeLog;
import com.badlydone.aparto.utils.aPartoAppInfo;

public class aPartoAbout extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.about);
		
		// set the app version
		TextView txtV = (TextView) findViewById(R.id.txtVersion);
		txtV.setText(aPartoAppInfo.getVersionName(this, getApplication().getClass()));
		
		final Context context = this;
		
		// handle save button
		Button bChangeLog = (Button)findViewById(R.id.btnChangeLog);
		bChangeLog.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				// show the change log the first time
				ChangeLog cl = new ChangeLog(context);
		        cl.getFullLogDialog().show();
				
			}
		});	
	}

}
