package com.upgrade.camp.reservation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReservationConstant {

	public static final String BASE_PATH = "/api/v1";
	public static final String PATH_RESERVATION = "/reservation";
	public static final String PATH_RESERVATION_WITH_ID = "/reservation/{reservationId}";
	public static final String PATH_RESERVATION_AVAILABLE_DATES = "/reservation/availableDates";
}
