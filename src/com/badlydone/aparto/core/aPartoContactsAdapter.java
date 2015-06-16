package com.badlydone.aparto.core;

import java.util.ArrayList;
import java.util.Collections;

import com.badlydone.aparto.R;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class aPartoContactsAdapter extends ArrayAdapter<aPartoContactItem> implements Filterable, Runnable {

	private ArrayList<aPartoContactItem> _Contacts;
	private ContentResolver _Content;
	private Thread _Thread;
	private Boolean _IsPhoneNumber;
	private String _Constraint;
	private Boolean _StopThread;
	
	public aPartoContactsAdapter(Context context, int textViewResourceId, ContentResolver content) {
        super(context, textViewResourceId);
        
        _Content = content;
        _Thread = new Thread(this);
        _Thread.setPriority(Thread.MAX_PRIORITY);
        _Thread.setName("aPartoContactsThread");
    }
	
	@Override
	public int getCount() {
		return _Contacts.size();
	}

	@Override
	public aPartoContactItem getItem(int position) {
		return _Contacts.get(position);
	}
	
	@Override
	public Filter getFilter() {
		
		Filter filter = new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				
				FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                	GetContactsList(constraint.toString());
                    
                    // Assign the data to the FilterResults
                    filterResults.values = _Contacts;
                    filterResults.count = _Contacts.size();
                }
                return filterResults;
                
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
				
			}
			
		};
		
		return filter;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
        
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)
            	getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row_contact, null);
        }
        
        aPartoContactItem o = _Contacts.get(position);
        if (o != null) {
        	
        	// set the descriptiorn
            TextView txtName = (TextView) v.findViewById(R.id.txtContactName);
            if (txtName != null)
            	txtName.setText(o.getName());
            
            // set the phone number
            TextView txtPhone = (TextView) v.findViewById(R.id.txtContactNumber);
            if (txtPhone != null)
            	txtPhone.setText(o.getPhoneNumber());
            
            // set the number type
            TextView txtType = (TextView) v.findViewById(R.id.txtContactType);
            if (txtType != null)
            	txtType.setText(o.getType());
            
        }
        return v;
		
		
	}
	
	private void GetContactsList(String constraint)
    {

		_Constraint = constraint;
		_IsPhoneNumber = constraint.replaceAll("\\d+", "").length() == 0;
		
		
		if (_Thread.isAlive())
		{
			_StopThread = true;
			try {
				_Thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		_StopThread = false;
		_Thread.run();
		
    }
	
	private ArrayList<aPartoContactItem> GetContactsListByName(String constraint)
    {
		
		ArrayList<aPartoContactItem> PeopleList = new ArrayList<aPartoContactItem>();
		
		// set the needed contact columns
		String[] columnsContanct = { ContactsContract.Contacts._ID, 
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.HAS_PHONE_NUMBER };
		
		// set the needed phone columns
		String[] columnsPhoneNumeber = { 
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID, 
				ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.TYPE };
		
	    Cursor people = _Content.query(
	    		ContactsContract.Contacts.CONTENT_URI, 
	    		columnsContanct,
	    		ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'" +
	    		" AND " + ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?", 
	    		new String[] { "%" + constraint + "%" }, 
	    		ContactsContract.Contacts.DISPLAY_NAME);
		
	    
	    while (people.moveToNext())
	    {
	    	
	    	if (_StopThread) break;
	    	
		    String contactName = people.getString(people.getColumnIndex(
		    		ContactsContract.Contacts.DISPLAY_NAME));
		
		    String contactId = people.getString(people.getColumnIndex(
		    		ContactsContract.Contacts._ID));
	
		    
	    	
	    	
		    // You know have the number so now query it like this
		    Cursor phones = _Content.query(
		    		ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
		    		columnsPhoneNumeber,
		    		ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
		    		null, 
		    		ContactsContract.CommonDataKinds.Phone.TYPE);
		    
		    while (phones.moveToNext()) {
		
		    	// create a new contact
		    	aPartoContactItem contact = new aPartoContactItem();
		    	
		    	// set the name
		    	contact.setName(contactName);
		    	
			    // set the phone number
		    	contact.setPhoneNumber(phones.getString(
				    phones.getColumnIndex(
				    		ContactsContract.CommonDataKinds.Phone.NUMBER)));
			
	    		// get the phone type
		    	contact.setType(GetPhoneType(
			    		phones.getString(phones.getColumnIndex(
					    		ContactsContract.CommonDataKinds.Phone.TYPE))));
		
			    // then add the contact to the list
			    PeopleList.add(contact);
		    
		    }
		    
		    phones.close();
		    
	    }
	    
	    people.close();
	    
	    
	    return PeopleList;
		
    }
	
	private ArrayList<aPartoContactItem> GetContactsListByNumber(String constraint)
    {
		
		ArrayList<aPartoContactItem> PeopleList = new ArrayList<aPartoContactItem>();
		
	    
	    // set the needed phone columns
		String[] columnsPhoneNumeber = { 
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID, 
				ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.CommonDataKinds.Phone.TYPE };
		
		// set the needed contact columns
		String[] columnsContanct = { ContactsContract.Contacts._ID, 
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.HAS_PHONE_NUMBER };
    	
	    // You know have the number so now query it like this
	    Cursor phones = _Content.query(
	    		ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
	    		columnsPhoneNumeber,
	    		ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?",
	    		new String[] { "%" + constraint + "%" }, 
	    		null);
		
	    
	    while (phones.moveToNext())
	    {
	    	
	    	String contactId = phones.getString(phones.getColumnIndex(
	    			ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
			
		    Cursor people = _Content.query(
		    		ContactsContract.Contacts.CONTENT_URI, 
		    		columnsContanct,
		    		ContactsContract.Contacts._ID + " = " + contactId, 
		    		null, 
		    		null);
	    	
		    if (people.moveToNext())
		    {
		    	
		    	if (_StopThread) break;
		    	
		    	// create a new contact
		    	aPartoContactItem contact = new aPartoContactItem();
		    	
		    	// set the name
		    	contact.setName(people.getString(people.getColumnIndex(
		    		    ContactsContract.Contacts.DISPLAY_NAME)));
		    	
			    // set the phone number
		    	contact.setPhoneNumber(phones.getString(
				    phones.getColumnIndex(
				    		ContactsContract.CommonDataKinds.Phone.NUMBER)));
			
	    		// get the phone type		    
			    contact.setType(GetPhoneType(
			    		phones.getString(phones.getColumnIndex(
					    		ContactsContract.CommonDataKinds.Phone.TYPE))));
			    
		
			    // then add the contact to the list
			    PeopleList.add(contact);
		    	
		    }
		    
		    people.close();	

		    
	    }
	    
	    phones.close();
	    
	    // sort the result
    	Collections.sort(PeopleList, new aPartoCompareContact());
	    
	    return PeopleList;
		
    }
	
	private String GetPhoneType(String numberType)
	{
		if(numberType.equals("0"))
	    	return getContext().getString(R.string.work);
	    else if(numberType.equals("1"))
	    	return getContext().getString(R.string.home);
	    else if(numberType.equals("2"))
	    	return getContext().getString(R.string.mobile);
	    else
	    	return getContext().getString(R.string.other);
	}

	public void run() {
		
		try
		{
		
			if (_IsPhoneNumber)
				
				_Contacts = GetContactsListByNumber(_Constraint);
			
			else
				
				_Contacts = GetContactsListByName(_Constraint);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
}
