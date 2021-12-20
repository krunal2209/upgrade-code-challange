package com.upgrade.camp.reservation.fixture;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import com.upgrade.camp.reservation.entity.CampSiteEntity;
import com.upgrade.camp.reservation.entity.ReservationEntity;
import com.upgrade.camp.reservation.entity.ReservedDatesEntity;
import com.upgrade.camp.reservation.vo.ReservationVO;
import com.upgrade.camp.reservation.ws.ReservationRequestWS;
import com.upgrade.camp.reservation.ws.ReservationResponseWS;
import com.upgrade.camp.reservation.ws.UpdateReservationRequestWS;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReservationFixture {

	public static final String RESERVATION_ID = "1eb336f1-8cad-4b93-add1-94165137e02f";
	private static final String FULL_NAME = "TEST_NAME";
	private static final String EMAIL_ADDRESS = "abc@test.com";

	private static final String CAMP_UUID = "2f0d2963-8689-4a01-aa58-40cc93dc47f5";
	private static final String CAMP_NAME = "SITE-1";

	public static ReservationResponseWS reservationResponseWS() {
		return ReservationResponseWS.builder()
				.id(RESERVATION_ID)
				.fullName(FULL_NAME)
				.emailAddress(EMAIL_ADDRESS)
				.arrivalDate(LocalDate.now().plusDays(2))
				.departureDate(LocalDate.now().plusDays(4))
				.build();
	}

	public static ReservationRequestWS reservationRequestWS() {
		return buildRequest(LocalDate.now().plusDays(2), LocalDate.now().plusDays(4));
	}

	public static ReservationRequestWS reservationRequestWithArrivalDateIsAfterDepartureDate() {
		return buildRequest(LocalDate.now().plusDays(4), LocalDate.now().plusDays(2));
	}

	public static ReservationRequestWS reservationRequestWithMoreThan3Days() {
		return buildRequest(LocalDate.now().plusDays(2), LocalDate.now().plusDays(6));
	}

	public static ReservationRequestWS reservationRequestWithLessThan1Day() {
		return buildRequest(LocalDate.now().plusDays(2), LocalDate.now().plusDays(2));
	}

	public static ReservationRequestWS reservationRequestForToday() {
		return buildRequest(LocalDate.now(), LocalDate.now().plusDays(2));
	}

	public static ReservationRequestWS reservationRequestForMoreThan1MonthInAdvance() {
		return buildRequest(LocalDate.now().plusDays(32), LocalDate.now().plusDays(33));
	}

	public static ReservationVO reservationVO() {
		return buildReservationVO(LocalDate.now().plusDays(2), LocalDate.now().plusDays(4));
	}

	public static ReservationVO requestVOWithArrivalDateIsAfterDepartureDate() {
		return buildReservationVO(LocalDate.now().plusDays(4), LocalDate.now().plusDays(2));
	}

	public static ReservationVO requestVOWithMoreThan3Days() {
		return buildReservationVO(LocalDate.now().plusDays(2), LocalDate.now().plusDays(6));
	}

	public static ReservationVO requestVOWithLessThan1Day() {
		return buildReservationVO(LocalDate.now().plusDays(2), LocalDate.now().plusDays(2));
	}

	public static ReservationVO requestVORequestForToday() {
		return buildReservationVO(LocalDate.now(), LocalDate.now().plusDays(2));
	}

	public static ReservationVO requestVOForMoreThan1MonthInAdvance() {
		return buildReservationVO(LocalDate.now().plusDays(32), LocalDate.now().plusDays(33));
	}


	public static UpdateReservationRequestWS updateReservationRequestWS() {
		return buildUpdateReservationRequest(LocalDate.now().plusDays(2), LocalDate.now().plusDays(4));
	}

	public static UpdateReservationRequestWS updateReservationRequestWithArrivalDateIsAfterDepartureDate() {
		return buildUpdateReservationRequest(LocalDate.now().plusDays(4), LocalDate.now().plusDays(2));
	}

	public static UpdateReservationRequestWS updateReservationRequestWithMoreThan3Days() {
		return buildUpdateReservationRequest(LocalDate.now().plusDays(2), LocalDate.now().plusDays(6));
	}

	public static UpdateReservationRequestWS updateReservationRequestWithLessThan1Day() {
		return buildUpdateReservationRequest(LocalDate.now().plusDays(2), LocalDate.now().plusDays(2));
	}

	public static UpdateReservationRequestWS updateReservationRequestForToday() {
		return buildUpdateReservationRequest(LocalDate.now(), LocalDate.now().plusDays(2));
	}

	public static UpdateReservationRequestWS updateReservationRequestForMoreThan1MonthInAdvance() {
		return buildUpdateReservationRequest(LocalDate.now().plusDays(32), LocalDate.now().plusDays(33));
	}

	public static ReservationEntity reservationEntity() {
		return ReservationEntity.builder()
				.uuid(RESERVATION_ID)
				.fullName(FULL_NAME)
				.emailAddress(EMAIL_ADDRESS)
				.arrivalDate(LocalDate.now().plusDays(2))
				.departureDate(LocalDate.now().plusDays(4))
				.reservedDates(reservedDatesEntities())
				.build();
	}

	public static List<ReservedDatesEntity> reservedDatesEntities() {
		List<ReservedDatesEntity> reservedDatesEntities = new ArrayList<>();
		reservedDatesEntities.add(ReservedDatesEntity.builder().reservedDate(LocalDate.now().plusDays(2)).build());
		reservedDatesEntities.add(ReservedDatesEntity.builder().reservedDate(LocalDate.now().plusDays(3)).build());
		return reservedDatesEntities;
	}

	public static CampSiteEntity campSiteEntity() {
		return CampSiteEntity.builder()
				.name(CAMP_NAME)
				.uuid(CAMP_UUID)
				.build();
	}

	private static ReservationRequestWS buildRequest(LocalDate arrivalDate, LocalDate departureDate) {
		return ReservationRequestWS.builder()
				.fullName(FULL_NAME)
				.emailAddress(EMAIL_ADDRESS)
				.arrivalDate(arrivalDate)
				.departureDate(departureDate)
				.build();
	}

	private static UpdateReservationRequestWS buildUpdateReservationRequest(LocalDate arrivalDate, LocalDate departureDate) {
		return UpdateReservationRequestWS.builder()
				.arrivalDate(arrivalDate)
				.departureDate(departureDate)
				.build();
	}

	private static ReservationVO buildReservationVO(LocalDate arrivalDate, LocalDate departureDate) {
		return ReservationVO.builder()
				.id(RESERVATION_ID)
				.fullName(FULL_NAME)
				.emailAddress(EMAIL_ADDRESS)
				.arrivalDate(arrivalDate)
				.departureDate(departureDate)
				.build();
	}
}
