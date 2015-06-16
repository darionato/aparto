package com.badlydone.aparto.sms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import com.badlydone.aparto.R;
import com.badlydone.aparto.core.aPartoProfile;

// class to send sms
public class aPartoSMS {

	private Context _Context;
	
	// Create the listener list
    protected ArrayList<aPartoSMSListener> 
    	_ListenerList = new ArrayList<aPartoSMSListener>();
	
	public aPartoSMS(Context context)
	{
		_Context = context;
	}
	
    // This methods allows classes to register for MyEvents
    public void setOnSmsResultListener(aPartoSMSListener listener) {
    	_ListenerList.add(listener);
    }

    // This methods allows classes to unregister for MyEvents
    public void removeSmsResultListener(aPartoSMSListener listener) {
    	_ListenerList.remove(listener);
    }

    // This private class is used to fire MyEvents
    private void fireSmsResult(aPartoSMSResult sms_result) {
    	
    	for (aPartoSMSListener listener : _ListenerList) {
			listener.SmsSent(sms_result);
		}
    	
    }
	
	public void SendSMS(aPartoProfile profile)
	{
		// send the sms
		this.sendSMSNow(profile.getPhoneNumber(), 
			getFormatSms(profile.getMessageText()),
			profile.getDeliveryReport());
	}
	
	private String getFormatSms(String sms)
    {
		// replace all the constats
    	sms = sms.replace(_Context.getString(R.string.format_date_dmy), getDate("dd/MM/yyyy"));
    	sms = sms.replace(_Context.getString(R.string.format_date_mdy), getDate("MM/dd/yyyy"));
    	sms = sms.replace(_Context.getString(R.string.format_time_12), getTime("hh:mm:ss"));
    	sms = sms.replace(_Context.getString(R.string.format_time_24), getTime("HH:mm:ss"));
    	return sms;
    }
	
	// send SMS with delivery report
    private void sendSMSNow(String phoneNumber, String message, Boolean delivery_report)
    {        
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
 
        // create the intent for sending the SMS
        PendingIntent sentPI = PendingIntent.getBroadcast(_Context, 0,
            new Intent(SENT), 0);
 
        // create the intent for delivery report (if needed)
        PendingIntent deliveredPI = null;
        if (delivery_report)
        	deliveredPI = PendingIntent.getBroadcast(_Context, 0,
        			new Intent(DELIVERED), 0);
 
        // SMS sent       
        _Context.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
            	
            	aPartoSMSResult res = new aPartoSMSResult(this);
            	
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                    	
                    	// sms sent
                    	res.setSent(true);
                    	res.setIdStringResult(R.string.sms_sent);
                    	
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        
                    	res.setIdStringResult(R.string.sms_error_generic_failure);
                    	
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        
                    	res.setIdStringResult(R.string.sms_error_no_service);
                    	
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        
                    	res.setIdStringResult(R.string.sms_error_null_pdu);
                    	
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                    	
                    	res.setIdStringResult(R.string.sms_error_radio_off);
                        
                        break;
                }
                
                fireSmsResult(res);
                
            }
        }, new IntentFilter(SENT));
 
        // SMS delivered
        _Context.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        
                        break;
                    case Activity.RESULT_CANCELED:
                    	
                        break;                        
                }
            }
        }, new IntentFilter(DELIVERED));        
 
        // send the sms
        SmsManager sms = SmsManager.getDefault();
        
        try
        {
        	sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        }
        catch (Exception ex)
        {
        	aPartoSMSResult res = new aPartoSMSResult(this);
        	res.setIdStringResult(R.string.sms_error_arguments);
        	fireSmsResult(res);
        }
        
    }
    
    private String getDate(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    private String getTime(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }
	
}
