package com.upgrade.camp.reservation.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import org.springframework.http.HttpStatus;

@Getter
public class AbstractReservationServiceException extends RuntimeException {

	private ApiError apiError;

	public AbstractReservationServiceException(HttpStatus status, String message, Throwable cause) {
		super(message, cause);
		this.apiError = ApiError.builder()
				.status(status)
				.message(message)
				.build();
	}

	@Value
	@Builder
	public static class ApiError {
		HttpStatus status;
		String message;
	}
}
