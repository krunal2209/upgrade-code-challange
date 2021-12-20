package com.upgrade.camp.reservation.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends AbstractReservationServiceException {
	public InvalidRequestException(String message) {
		super(HttpStatus.BAD_REQUEST, message, null);
	}
}
