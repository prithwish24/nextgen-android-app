package com.nextgen.carrental.app.bo;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UserProfile {
	private String fullName;
	private String userId;
	private String mobileNo;
	private String emailId;
	private String password;
	private Date dateOfBirth;
	private List<Preference> preferences;

	public UserProfile() {
	}

	public UserProfile(String emailId, String fullName) {
		this.fullName = fullName;
		this.emailId = emailId;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public String getUserId() {
		return userId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getPassword() {
		return password;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public List<Preference> getPreferences() {
		if (preferences == null) {
			preferences = new ArrayList<>();
		}
		return preferences;
	}
}
