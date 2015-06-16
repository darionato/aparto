package com.badlydone.aparto.sms;

import java.util.EventObject;


public class aPartoSMSResult extends EventObject {
	
	private static final long serialVersionUID = 1L;
	private Boolean _Sent;
	private int _IdStringResult;
	

	public aPartoSMSResult(Object source) {
        super(source);
        _Sent = false;
    }
	
	public Boolean getResult()
	{
		return _Sent;
	}
	
	public void setSent(Boolean sent) {
		_Sent = sent;
	}
	
	public int getIdStringResult()
	{
		return _IdStringResult;
	}
	
	public void setIdStringResult(int idStringError) {
		_IdStringResult = idStringError;
	}
	
}
