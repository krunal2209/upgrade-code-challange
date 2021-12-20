package com.upgrade.camp.reservation.exception;

import org.springframework.http.HttpStatus;

public class ReservationAlreadyExistException extends AbstractReservationServiceException {
	public ReservationAlreadyExistException(String message) {
		super(HttpStatus.CONFLICT, message, null);
	}
}
