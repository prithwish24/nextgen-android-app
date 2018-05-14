package com.nextgen.carrental.app.bo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceError {

	@JsonProperty("success")
	private String code;
	@JsonProperty("type")
	private String type;
	@JsonProperty("text")
	private String text;

	@Override
	public String toString() {
		return "ServiceError{" +
				"code='" + code + '\'' +
				", type='" + type + '\'' +
				", text='" + text + '\'' +
				'}';
	}

	public ServiceError() {
	}
	public ServiceError(String code, String type, String text) {
		this.code = code;
		this.type = type;
		this.text = text;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	

}
