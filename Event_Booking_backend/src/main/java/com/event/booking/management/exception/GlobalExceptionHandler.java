package com.event.booking.management.exception;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	//1 Validation Errors (400 Bad Request)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException e, WebRequest request) {
		String errors = e.getBindingResult().getFieldErrors().stream()
				.map(x -> x.getField() + " : " + x.getDefaultMessage())
				.collect(Collectors.joining(", "));

		ExceptionResponse response = new ExceptionResponse(
				LocalDate.now(),
				errors,
				request.getDescription(false),
				"BAD_REQUEST"
		);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	//2 Resource Not Found (404)
	@ExceptionHandler({
		ResourceNotFoundException.class,
		BookingNotFoundException.class,
		EventNotFoundException.class,
		UserNotFoundException.class
	})
	public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
		ExceptionResponse response = new ExceptionResponse(
				LocalDate.now(),
				ex.getMessage(),
				request.getDescription(false),
				"NOT_FOUND"
		);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	//3 Invalid Credentials (401 Unauthorized)
	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<Object> handleInvalidPassword(InvalidPasswordException ex, WebRequest request) {
		ExceptionResponse response = new ExceptionResponse(
				LocalDate.now(),
				ex.getMessage(),
				request.getDescription(false),
				"UNAUTHORIZED"
		);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	//4 General Exception (500 Internal Server Error)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
		ExceptionResponse response = new ExceptionResponse(
				LocalDate.now(),
				ex.getMessage(),
				request.getDescription(false),
				"INTERNAL_SERVER_ERROR"
		);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(DuplicatePaymentException.class)
	public ResponseEntity<Map<String, String>> handleDuplicatePayment(DuplicatePaymentException ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.CONFLICT); // 409 Conflict
	}

	@ExceptionHandler(UnauthorizedPaymentException.class)
	public ResponseEntity<Map<String, String>> handleUnauthorized(UnauthorizedPaymentException ex) {
		Map<String, String> error = new HashMap<>();
		error.put("error", ex.getMessage());
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}

}
