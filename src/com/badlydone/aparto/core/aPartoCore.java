package com.badlydone.aparto.core;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;

import com.badlydone.aparto.helper.aPartoHelper;
import com.badlydone.aparto.sms.aPartoSMS;
import com.badlydone.aparto.utils.aPartoMigrate;

public class aPartoCore {

	private aPartoHelper _Helper;
	private SQLiteDatabase _Db;
	private Context _Context;
	private aPartoSMS _Sms;
	
	public aPartoCore(Context context)
	{
		_Context = context;
		_Sms = new aPartoSMS(context);
	}
	
	public aPartoSMS Sms()
	{
		return _Sms;
	}
	
	public Boolean OpenDb()
	{
		
		// initialize the db
		_Helper = new aPartoHelper(_Context, this);
		_Db = _Helper.getWritableDatabase();

		if (_Db != null && _Helper.getCreatedNewDb())
		{
			// execure the migration
			aPartoMigrate mig = new aPartoMigrate(_Context);
			mig.Migrate(this);	
		}
		
		return (_Db != null);
		
	}
	
	public void Close()
	{
		// close the database if open
		if (_Db != null)
			_Db.close();
		_Db = null;
	}
	
	private ContentValues getContentValusFromProfile(aPartoProfile profile)
	{
		// create the values
		ContentValues values = new ContentValues();
		values.put("profile_name", profile.getProfileName());
		values.put("phone_number", profile.getPhoneNumber());
		values.put("message_text", profile.getMessageText());
		values.put("delivery_report", profile.getDeliveryReport()?1:0);
		return values;
	}
	
	public aPartoProfile addProfile(aPartoProfile profile)
	{
		
		// insert the new profile
		profile.setId(_Db.insert(_Helper.getTableProfilesName(), 
				"profile_name, " +
                "phone_number, " +
                "message_text, " +
                "delivery_report", 
                getContentValusFromProfile(profile)));
		
		return profile.getId() == -1?null:profile;
		
	}
	
	public aPartoProfile updateProfile(aPartoProfile profile)
	{
		
		// update the values
		int ret = _Db.update(_Helper.getTableProfilesName(), 
				getContentValusFromProfile(profile), 
				String.format("id_profile = %d", profile.getId()), 
				null);
		
		if (ret != 0)
			return profile;
		else
			return null;
	}
	
	public Boolean deleteProfile(aPartoProfile profile)
	{
		
		// delete the profile
		int ret = _Db.delete(_Helper.getTableProfilesName(), 
				String.format("id_profile = %d", profile.getId()), 
				null);
		
		return ret != 0;
		
	}
	
	public aPartoProfile getProfile(long id_profile)
	{
		
		// try to get the profile
		ArrayList<aPartoProfile> tmpList = this.getProfilesList(id_profile);
		
		// if not found return null
		if (tmpList.isEmpty()) return null;
		
		// return the first and unique element
		return tmpList.get(0);
		
	}
	
	public ArrayList<aPartoProfile> getProfilesList(long id_profile)
	{
		
		String filter = null;
		
		// check if there is a filter by id
		if (id_profile != 0)
			filter = String.format("id_profile = %d", id_profile);
		
		// create a query to select the items
		SQLiteCursor cursor = (SQLiteCursor) _Db.query(
				_Helper.getTableProfilesName(),
				new String[] {"id_profile", "profile_name", 
					"phone_number", "message_text", "delivery_report"}, 
					filter, null, null, null, null);
		
		// create the return variable
		ArrayList<aPartoProfile> ret = new ArrayList<aPartoProfile>();
		
		// loop all items
		if (cursor.moveToFirst())
		{
			do
			{
				
				aPartoProfile i = new aPartoProfile();
				i.setId(cursor.getLong(0));
				i.setProfileName(cursor.getString(1));
				i.setPhoneNumber(cursor.getString(2));
				i.setMessageText(cursor.getString(3));
				i.setDeliveryReport(cursor.getInt(4) == 1?true:false);
				
				ret.add(i);
				
			} while (cursor.moveToNext());
		}
		
		// close the cursor
		if (cursor != null && !cursor.isClosed())
	         cursor.close();
		
		return ret;
		
	}
	
	public ArrayList<aPartoProfile> getProfilesList()
	{		
		return this.getProfilesList(0);
	}
	
}
