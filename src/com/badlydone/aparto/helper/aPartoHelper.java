package com.badlydone.aparto.helper;

import com.badlydone.aparto.core.aPartoCore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class aPartoHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
    private static final String DICTIONARY_TABLE_NAME = "aparto_profiles";
    private static final String DICTIONARY_TABLE_CREATE =
                "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
                "id_profile INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "profile_name TEXT NOT NULL, " +
                "phone_number TEXT, " +
                "message_text TEXT, " +
                "delivery_report INTEGER);";
    private Boolean _CreatedNewDb = false;
	
    public String getTableProfilesName()
    {
    	return DICTIONARY_TABLE_NAME;
    }
    
    public Boolean getCreatedNewDb()
    {
    	return _CreatedNewDb;
    }
    
	public aPartoHelper(Context context, aPartoCore core) {
		super(context, DICTIONARY_TABLE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DICTIONARY_TABLE_CREATE);
		
		_CreatedNewDb = true;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
