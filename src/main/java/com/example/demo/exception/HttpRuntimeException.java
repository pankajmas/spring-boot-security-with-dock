package com.example.demo.exception;

import lombok.Getter;

@Getter
public class HttpRuntimeException extends RuntimeException {

    private String httpStatusCode;
    private String errorCode;
    
    

    public String getHttpStatusCode() {
		return httpStatusCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public HttpRuntimeException(Integer httpStatusCode, String errorCode) {
        super(
            Constants.HTTP_STATUS_MESSAGE_MAPPING.getOrDefault(
                httpStatusCode, String.format("HTTP %s occurred.", httpStatusCode)));
        this.httpStatusCode = httpStatusCode.toString();
        this.errorCode = errorCode;
    }

    public HttpRuntimeException(String message, Integer httpStatusCode, String errorCode) {
        super(message);
        this.httpStatusCode = httpStatusCode.toString();
        this.errorCode = errorCode;
    }
}
