package com.badlydone.aparto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import com.badlydone.aparto.core.*;
import com.badlydone.aparto.sms.aPartoSMSListener;
import com.badlydone.aparto.sms.aPartoSMSResult;
import com.badlydone.aparto.utils.ChangeLog;
import com.badlydone.aparto.utils.aPartoToast;

public class aparto extends Activity {
	
	private aPartoCore _aParto;
	private ArrayList<aPartoProfile> _Profiles;
	private aPartoProfileAdapter _Adapter;
	private ListView _listViewProfiles;
	private Context _Context;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // set the context
        _Context = this;
        
        // init the main class
        _aParto = new aPartoCore(this);
        
        // handle the events
        _aParto.Sms().setOnSmsResultListener(new aPartoSMSListener() {
			
			public void SmsSent(aPartoSMSResult result) {
				
				// show the message result
				aPartoToast.ShowToast(_Context, 
						getCurrentFocus(), 
						result.getIdStringResult());
				
				// if ok, i close the app
				if (result.getResult())
					finish();
				else
					_listViewProfiles.setEnabled(true);
				
			}
		});
        
        // set the listview
        _listViewProfiles = (ListView)findViewById(R.id.listViewProfiles);
        
        // event on item click
        _listViewProfiles.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				// set that i'm sending to avoid to send multiple message
				_listViewProfiles.setEnabled(false);
				
				// start the animation
				StartAnimation(arg1);
				
				// get the item profile
				aPartoProfile profile = _Profiles.get(arg2);
				
				// send the sms
				_aParto.Sms().SendSMS(profile);
				
			}
		});
        
        // handle the menu after long press
        _listViewProfiles.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				
				menu.setHeaderTitle(R.string.tools);
				MenuInflater inflater = getMenuInflater();
				inflater.inflate(R.menu.menu_edit_profile, menu);
			}
		});
		
		// show the change log the first time
		ChangeLog cl = new ChangeLog(this);
		if (cl.firstRun())
            cl.getLogDialog().show();
        
    }

	private void StartAnimation(View v)
    {
    	
        // get the image
    	ImageView img = (ImageView) v.findViewById(R.id.imgCarStart);
    	img.bringToFront();
    	
    	// start animantion
    	Animation CarAnim = AnimationUtils.loadAnimation(this, R.anim.car_start);
    	CarAnim.setZAdjustment(Animation.ZORDER_TOP);
		img.startAnimation(CarAnim);
    	
    }
    
    
    @Override
	public boolean onContextItemSelected(MenuItem item) {
		
		switch (item.getItemId() ) {
		case R.id.mnuEditProfile:
			this.OpenProfileView(getProfileFromMenu(item).getId());
			break;

		case R.id.mnuDeleteProfile:
			this.AstToDeleteProfile(getProfileFromMenu(item));
			break;
		default:
			break;
		}
		
		return super.onContextItemSelected(item);
	}
    
    @Override
	protected void onResume() {
		
		super.onResume();
		
		
		PopulateList();
		
	}

	@Override
	protected void onPause() {
		
		super.onPause();
		
		// close the database
		_aParto.Close();
		
	}
	
	// populate all profile
	private void PopulateList()
    {
    	// open the database
		if (_aParto.OpenDb())
		{
		
	        // get all profiles
	        _Profiles = _aParto.getProfilesList();
	        
	        // create the adapter
	        _Adapter = new aPartoProfileAdapter(this, R.layout.row_profile, _Profiles);
	        
	        // set the adapter
	        _listViewProfiles.setAdapter(_Adapter);
        
		}
    }
	
    // open the menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }
    
    // menu click
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.mnuAddProfile:
        	
        	// add a new profile
        	OpenProfileView(0);
        	
            return true;
            
        case R.id.mnuAbout:
        	
        	// open about
        	OpenAboutView();
        	
        	return true;
        	
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private aPartoProfile getProfileFromMenu(MenuItem menu)
    {
    	// get the menu info for the position
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) menu.getMenuInfo();
		
		// get the item info
		aPartoProfile item = this._Adapter.getItem(menuInfo.position);
		
		return item;
    }
    
    // delete a profile asking about it
    private void AstToDeleteProfile(final aPartoProfile profile)
    {
    	
    	// create the question
    	String q = String.format(
    			getResources().getString(R.string.do_want_delete_profile),
    			profile.getProfileName());
    	
    	// ask if you really want to delete the profile
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(q)
		       .setCancelable(true)
		       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.cancel();
		        	   DeleteProfile(profile);
		           }
		       })
		       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		
		AlertDialog alert = builder.create();
		alert.show();	
    }
	
    private void DeleteProfile(aPartoProfile profile)
    {
    	if (_aParto.deleteProfile(profile))
    		PopulateList();

    }
    
    // open the options view
    private void OpenProfileView(long id_profile)
    {    	
    	Intent i = new Intent(this, apartoOptions.class);
    	i.putExtra(getResources().getString(R.string.profile_id), id_profile);
    	startActivityForResult(i, 1);
    }
    
    // open the about view
    private void OpenAboutView()
    {    	
    	Intent i = new Intent(this, aPartoAbout.class);
    	startActivityForResult(i, 1);
    }
    
}
