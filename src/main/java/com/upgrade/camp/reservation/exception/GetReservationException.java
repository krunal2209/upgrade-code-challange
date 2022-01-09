package com.upgrade.camp.reservation.exception;

import org.springframework.http.HttpStatus;

public class GetReservationException extends AbstractReservationServiceException {
	public GetReservationException(String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}
}
