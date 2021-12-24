package com.upgrade.camp.reservation.exception;

import org.springframework.http.HttpStatus;

public class ReservationNotFoundException extends AbstractReservationServiceException {
	public ReservationNotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}
}
