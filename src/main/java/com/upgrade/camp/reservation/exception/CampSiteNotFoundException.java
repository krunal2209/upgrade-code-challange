package com.upgrade.camp.reservation.exception;

import org.springframework.http.HttpStatus;

public class CampSiteNotFoundException extends AbstractReservationServiceException {
	public CampSiteNotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, message, null);
	}
}
