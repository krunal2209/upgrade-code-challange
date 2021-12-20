package com.upgrade.camp.reservation.fixture;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import com.upgrade.camp.reservation.ws.ErrorResponseWS;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorResponseFixture {

	public static final String DEPARTURE_DATE_SHOULD_BE_AFTER_ARRIVAL_DATE = "Departure date should be after arrival date.";
	public static final String BOOKING_ALREADY_EXIST_FOR_GIVEN_DATE = "Booking already Exist for given date.";
	public static final String THE_CAMPSITE_CAN_NOT_BE_RESERVED_FOR_MINIMUM_1_DAY = "The campsite can not be reserved for minimum 1 day.";
	public static final String THE_CAMPSITE_CAN_NOT_BE_RESERVED_MORE_THAN_3_DAYS = "The campsite can not be reserved more than 3 days.";
	public static final String THE_CAMPSITE_CAN_BE_RESERVED_MINIMUM_1_DAY_S_AHEAD_OF_ARRIVAL = "The campsite can be reserved minimum 1 day(s) ahead of arrival.";
	public static final String THE_CAMPSITE_CAN_BE_RESERVED_UPTO_1_MONTH_IN_ADVANCE = "The campsite can be reserved upto 1 month in advance.";

	public static ErrorResponseWS bookingAlreadyExist() {
		return ErrorResponseWS.builder()
				.status(409)
				.message(BOOKING_ALREADY_EXIST_FOR_GIVEN_DATE)
				.build();
	}

	public static ErrorResponseWS arrivalDateAfterDepartureDate() {
		return ErrorResponseWS.builder()
				.status(400)
				.message(DEPARTURE_DATE_SHOULD_BE_AFTER_ARRIVAL_DATE)
				.build();
	}

	public static ErrorResponseWS minimumOneDayRequired() {
		return ErrorResponseWS.builder()
				.status(400)
				.message(THE_CAMPSITE_CAN_NOT_BE_RESERVED_FOR_MINIMUM_1_DAY)
				.build();
	}

	public static ErrorResponseWS moreThanThreeDaysReservation() {
		return ErrorResponseWS.builder()
				.status(400)
				.message(THE_CAMPSITE_CAN_NOT_BE_RESERVED_MORE_THAN_3_DAYS)
				.build();
	}

	public static ErrorResponseWS minimumOneDayBeforeArrival() {
		return ErrorResponseWS.builder()
				.status(400)
				.message(THE_CAMPSITE_CAN_BE_RESERVED_MINIMUM_1_DAY_S_AHEAD_OF_ARRIVAL)
				.build();
	}

	public static ErrorResponseWS maxOneMonthInAdvance() {
		return ErrorResponseWS.builder()
				.status(400)
				.message(THE_CAMPSITE_CAN_BE_RESERVED_UPTO_1_MONTH_IN_ADVANCE)
				.build();
	}
}
