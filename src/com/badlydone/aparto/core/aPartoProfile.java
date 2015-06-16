package com.badlydone.aparto.core;

/**
 * @author dario
 *
 */
public class aPartoProfile {
	
	private long Id;
	private String ProfileName;
	private String PhoneNumber;
	private String MessageText;
	private Boolean DeliveryReport;
	
	public long getId() {
		return Id;
	}
	public void setId(long id) {
		Id = id;
	}
	public String getProfileName() {
		return ProfileName;
	}
	public void setProfileName(String profileName) {
		ProfileName = profileName;
	}
	public String getPhoneNumber() {
		return PhoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}
	public String getMessageText() {
		return MessageText;
	}
	public void setMessageText(String messageText) {
		MessageText = messageText;
	}
	public Boolean getDeliveryReport() {
		return DeliveryReport;
	}
	public void setDeliveryReport(Boolean deliveryReport) {
		DeliveryReport = deliveryReport;
	}
	
}
