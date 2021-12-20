package com.upgrade.camp.reservation.exception;

import org.springframework.http.HttpStatus;

public class CreateReservationException extends AbstractReservationServiceException {
	public CreateReservationException(String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
	}
}
