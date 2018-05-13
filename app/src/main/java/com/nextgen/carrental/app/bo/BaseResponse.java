package com.nextgen.carrental.app.bo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResponse <T>{
	@JsonProperty("success")
	private boolean success;
	@JsonProperty("error")
	private ServiceError error;
	@JsonProperty("sessionId")
	private String sessionId;
	@JsonProperty("response")
	private T response;
	
	public BaseResponse() {
	}
	public BaseResponse(String code, String type, String text) {
		this.success = false;
		setServiceError(code, type, text);
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public ServiceError getError() {
		return error;
	}
	public void setError(ServiceError error) {
		this.error = error;
	}
	public void setServiceError(String code, String type, String text) {
		this.error = new ServiceError(code, type, text);
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public T getResponse() {
		return response;
	}
	public void setResponse(T response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "BaseResponse{" +
				"success=" + success +
				", error=" + error+
				", response=" + response +
				'}';
	}
}
