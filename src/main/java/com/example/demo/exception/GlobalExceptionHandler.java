package com.example.demo.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String firstname = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(firstname, message);
		});

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiError> poductNotFoundExceptionHandler(UserNotFoundException e,
			HttpServletRequest request) {

		ApiError error = new ApiError(HttpStatus.NOT_FOUND.value(), new Date(), HttpStatus.NOT_FOUND, e.getMessage(),
				request.getRequestURI());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ApiError> handleUserAlreadyExistsException(UserAlreadyExistsException e, HttpServletRequest request) {
	    ApiError error = new ApiError(HttpStatus.CONFLICT.value(), new Date(), HttpStatus.CONFLICT, e.getMessage(),
	            request.getRequestURI());
	    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

}
