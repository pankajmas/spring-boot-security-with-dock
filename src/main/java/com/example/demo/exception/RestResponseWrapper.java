package com.example.demo.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.example.demo.utility.JsonNodeUtil;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class RestResponseWrapper<ResponseClass, ErrorClass> {

    protected Optional<ResponseClass> response;
    protected Optional<ErrorClass> error;
    protected Optional<String> errorString;

    protected boolean successful;

    protected int code;

    protected String url;

    protected HttpHeaders headers;
    

    public Optional<ResponseClass> getResponse() {
		return response;
	}

	public Optional<ErrorClass> getError() {
		return error;
	}

	public Optional<String> getErrorString() {
		return errorString;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public int getCode() {
		return code;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public RestResponseWrapper(ResponseEntity<ResponseClass> responseEntity) throws IOException {
        this.code = responseEntity.getStatusCodeValue();
        this.successful = responseEntity.getStatusCode().is2xxSuccessful();
        this.response = Optional.ofNullable(responseEntity.getBody());
        this.headers = responseEntity.getHeaders();
    }

    public RestResponseWrapper(ResponseEntity<ResponseClass> responseEntity, Class<ErrorClass> errorClass)
            throws IOException {
        this(responseEntity, errorClass, StringUtils.EMPTY);

        // if no error body is present, we should throw
        // exception for error handling
        if (!isSuccessful() && error.isEmpty()) {
            throw new HttpRuntimeException(responseEntity.getStatusCodeValue(), String.valueOf(responseEntity.getStatusCodeValue()));
        }
    }

    public RestResponseWrapper(
            ResponseEntity<ResponseClass> responseEntity, Class<ErrorClass> errorClass, String url)
            throws IOException {
        this(responseEntity);

        this.url = url;
        this.error = getError(responseEntity, errorClass);
    }

    protected Optional<ErrorClass> getError(
            ResponseEntity<ResponseClass> responseEntity, Class<ErrorClass> errorClass) throws IOException {
        try {
            // successful, error should be empty
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return Optional.empty();
            }

            final String bodyString = getResponseBodyAsString(responseEntity.getBody());
            this.errorString = Optional.of(bodyString);
            // suppress WARN bugsnag here
            // if we get a non-success code and the error is empty we will throw above
            return parseErrorSilently(bodyString, errorClass);
        } catch (Exception e) {
            // Handle exceptions if any
            return Optional.empty();
        }
    }

    public String getUrl() {
        return StringUtils.isEmpty(url) ? StringUtils.EMPTY : url;
    }

    protected Optional<ErrorClass> parseError(String error, Class<ErrorClass> errorClass) {
        // Implement your error parsing logic here
         return JsonNodeUtil.parse(error, errorClass);
        
    }

    protected Optional<ErrorClass> parseErrorSilently(String body, Class<ErrorClass> errorClass) {
        // Implement your error parsing logic here
         return JsonNodeUtil.parseSilently(body, errorClass);
       
    }

    protected Optional<ResponseClass> parseResponseBody(
            String body, Class<ResponseClass> responseClass) {
        // Implement your response body parsing logic here
         return JsonNodeUtil.parse(body, responseClass);
       
    }

    protected String getResponseBodyAsString(ResponseClass body) throws IOException {
        // Implement your response body to string conversion logic here
         return body != null ? body.toString() : StringUtils.EMPTY;

    }

    public static <ResponseClass, ErrorClass> RestResponseWrapper<ResponseClass, ErrorClass> wrap(
            ResponseEntity<ResponseClass> responseEntity, Class<ErrorClass> errorClass) throws IOException {
        return new RestResponseWrapper<>(responseEntity, errorClass);
    }
}

