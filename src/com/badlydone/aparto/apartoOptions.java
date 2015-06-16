package com.badlydone.aparto;

import java.util.ArrayList;

import android.app.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.badlydone.aparto.core.aPartoContactItem;
import com.badlydone.aparto.core.aPartoContactsAdapter;
import com.badlydone.aparto.core.aPartoCore;
import com.badlydone.aparto.core.aPartoProfile;
import com.badlydone.aparto.utils.aPartoToast;

public class apartoOptions extends Activity {
	
	private long _Id_profile;
	ArrayList<aPartoContactItem> _PeopleList;
	AutoCompleteTextView _txtNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		  
		setContentView(R.layout.options);

		// get the id
		_Id_profile = getIntent().getExtras().getLong(
				getResources().getString(R.string.profile_id));
		
		// register events
		TextView txt = (TextView)findViewById(R.id.txtMessage);
    	registerForContextMenu(txt);
		
		// open options
		OpenOptions();
		
		// handle close button
		Button bClose = (Button)findViewById(R.id.btnCancel);
		bClose.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
		
		// handle save button
		Button bSave = (Button)findViewById(R.id.btnSave);
		bSave.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				SaveOptions();
			}
		});		
		
		// create the adapter
		aPartoContactsAdapter adapter = new aPartoContactsAdapter(
				this, R.layout.row_contact, getContentResolver());
		

		// set up the autocomplete
		_txtNumber = (AutoCompleteTextView)findViewById(R.id.txtNumber);
		_txtNumber.setAdapter(adapter);
		
		_txtNumber.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
				
				aPartoContactItem contact = (aPartoContactItem) adapterView.getItemAtPosition(position);
			    _txtNumber.setText(contact.toString());
				_txtNumber.setSelection(_txtNumber.getText().length());
			}
			
			
		});
		
	}	
	
	private Boolean CheckDataBeforeSave()
	{
		
		// check the profile name
		EditText name = (EditText)findViewById(R.id.txtProfileName);
		
		if (name.getText().toString().length() == 0)
		{
			name.requestFocus();
			aPartoToast.ShowToast(this, getCurrentFocus(), R.string.option_error_missing_name);
			return false;
		}
		
		// check the number
		EditText numb = (AutoCompleteTextView)findViewById(R.id.txtNumber);
		
		if (numb.getText().toString().length() == 0)
		{
			numb.requestFocus();
			aPartoToast.ShowToast(this, getCurrentFocus(), R.string.option_error_missing_number);
			return false;
		}
		
		// check the text message
		EditText sms = (EditText)findViewById(R.id.txtMessage);
		
		if (sms.getText().toString().length() == 0)
		{
			sms.requestFocus();
			aPartoToast.ShowToast(this, getCurrentFocus(), R.string.option_error_missing_text);
			return false;
		}
		
		return true;
		
	}
	
	private void SaveOptions()
	{
		
		// check data
		if (CheckDataBeforeSave() == false)
			return;
		
		// get the values
		aPartoCore aP = new aPartoCore(this);
		if (aP.OpenDb())
		{
		
			EditText name = (EditText)findViewById(R.id.txtProfileName);
			EditText numb = (AutoCompleteTextView)findViewById(R.id.txtNumber);
			EditText sms = (EditText)findViewById(R.id.txtMessage);
			CheckBox report = (CheckBox)findViewById(R.id.CheckBoxReports);
			
			aPartoProfile profile = new aPartoProfile();
			
			// save the id for update
			profile.setId(_Id_profile);
			
			// save the profile name
			profile.setProfileName(name.getText().toString());
			
			// save the number
			profile.setPhoneNumber(numb.getText().toString());
			
			// save the message
			profile.setMessageText(sms.getText().toString());
	
			// save the delivery report required
			profile.setDeliveryReport(report.isChecked());
			
			// save the profile to db
			if (this._Id_profile == 0)
				profile = aP.addProfile(profile);
			else
				profile = aP.updateProfile(profile);
			
			// check if it was saved corectly
			if (profile != null)
				_Id_profile = profile.getId();
		
		}
		
		// show message
		aPartoToast.ShowToast(this, this.getCurrentFocus(), R.string.configuration_saved);
		
		// close the activity
		finish();
		
	}
	
	// menu click
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_help:
        	OpenMenuTags();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private void OpenMenuTags()
    {
    	TextView txt = (TextView)findViewById(R.id.txtMessage);
    	txt.showContextMenu();
    }
    
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu_tags, menu);
    }
    
    public boolean onContextItemSelected(MenuItem item) {
    	
    	TextView vw = (TextView)findViewById(R.id.txtMessage);
    	String txt_insert = "";
    	
    	switch (item.getItemId()) {
	        case R.id.itemDate_dmy:
	        	txt_insert = getString(R.string.format_date_dmy);
	        	break;
	        case R.id.itemDate_mdy:
	        	txt_insert = getString(R.string.format_date_mdy);
	        	break;
	        case R.id.itemTime12:
	        	txt_insert = getString(R.string.format_time_12);
	        	break;
	        case R.id.itemTime24:
	        	txt_insert = getString(R.string.format_time_24);
	        	break;
	        default:
	            return super.onContextItemSelected(item);
        }
    	
    	if (txt_insert.length() != 0)
    	{
    		int len = txt_insert.length();
    		Editable str = vw.getEditableText();
            str.insert(vw.getSelectionStart(), txt_insert, 0, len);
    	}
    	
    	return true;
    	
    }
    
	
	private void OpenOptions()
	{
		
		if (_Id_profile == 0) return;
		
		// get the values
		aPartoCore aP = new aPartoCore(this);
		if (aP.OpenDb())
		{
		
			// get the profile
			aPartoProfile profile = aP.getProfile(_Id_profile);
			
			// if not found i exit
			if (profile == null) return;
	        
			// open the profile name
	        EditText name = (EditText)findViewById(R.id.txtProfileName);
	        name.setText(profile.getProfileName());
			
	        // open the number
	        EditText numb = (AutoCompleteTextView)findViewById(R.id.txtNumber);
	        numb.setText(profile.getPhoneNumber());
	        
	        // open the SMS
	        EditText sms = (EditText)findViewById(R.id.txtMessage);
	        sms.setText(profile.getMessageText());
	        
	        // open the delivery reports
	        CheckBox report = (CheckBox)findViewById(R.id.CheckBoxReports);
	        report.setChecked(profile.getDeliveryReport());
        
	        // close the db
	        aP.Close();
	        
		}
        
	}
	
	// open the menu models
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_help, menu);
        return true;
    }

}
