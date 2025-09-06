package com.event.booking.management.exception;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
	private LocalDate timeStamp;
	private String message;
	private String details;
	private String httpCodeMessage;
	
	
	
}
