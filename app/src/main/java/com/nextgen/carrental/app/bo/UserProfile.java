package com.nextgen.carrental.app.bo;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UserProfile {
	@JsonProperty("fullname")
	private String fullName;
	@JsonProperty("username")
	private String username;
	@JsonProperty("mobileNo")
	private String mobileNo;
	@JsonProperty("emailId")
	private String emailId;
	@JsonProperty("password")
	private String password;
	@JsonProperty("dateOfBirth")
	private String dateOfBirth;
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

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public String getUsername() {
		return username;
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

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public List<Preference> getPreferences() {
		if (preferences == null) {
			preferences = new ArrayList<>();
		}
		return preferences;
	}
}
