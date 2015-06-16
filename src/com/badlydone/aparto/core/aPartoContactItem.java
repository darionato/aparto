package com.badlydone.aparto.core;

public class aPartoContactItem {

	private String _Name;
	private String _PhoneNumber;
	private String _Type;
	
	public String getName() {
		return _Name;
	}
	public void setName(String name) {
		this._Name = name;
	}
	public String getPhoneNumber() {
		return _PhoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this._PhoneNumber = phoneNumber;
	}
	public String getType() {
		return _Type;
	}
	public void setType(String type) {
		this._Type = type;
	}
	@Override
	public String toString() {
		return this.getName() + " <" + this.getPhoneNumber() + ">";
	}
	
}
