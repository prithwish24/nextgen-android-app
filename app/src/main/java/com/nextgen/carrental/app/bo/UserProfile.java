package com.nextgen.carrental.app.bo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties
public class UserProfile {
	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("lastName")
	private String lastName;
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
	@JsonProperty("carTypePref")
	private String carTypePref;
	@JsonProperty("preferences")
	private List<Preference> preferences;

	public UserProfile() {
	}

	public UserProfile(String emailId, String fullName) {
		this.firstName = fullName;
		this.emailId = emailId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public void setCarTypePref(String carTypePref) {
		this.carTypePref = carTypePref;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
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

	public String getCarTypePref() {
		return carTypePref;
	}

	public List<Preference> getPreferences() {
		if (preferences == null) {
			preferences = new ArrayList<>();
		}
		return preferences;
	}

}
