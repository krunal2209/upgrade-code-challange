package com.upgrade.camp.reservation.exception;

import org.springframework.http.HttpStatus;

public class UpdateReservationException extends AbstractReservationServiceException {
	public UpdateReservationException(String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, message, null);
	}
}
