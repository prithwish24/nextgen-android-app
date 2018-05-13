package com.nextgen.carrental.app.bo;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UserProfile {
	@JsonProperty("fullName")
	private String fullName;
	@JsonProperty("userId")
	private String userId;
	@JsonProperty("mobileNo")
	private String mobileNo;
	@JsonProperty("emailId")
	private String emailId;
	@JsonProperty("password")
	private String password;
	@JsonProperty("dateOfBirth")
	private Date dateOfBirth;
	@JsonProperty("preferences")
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
