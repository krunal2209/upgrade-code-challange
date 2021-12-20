package com.upgrade.camp.reservation.service;

import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.BOOKING_ALREADY_EXIST_FOR_GIVEN_DATE;
import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.DEPARTURE_DATE_SHOULD_BE_AFTER_ARRIVAL_DATE;
import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.THE_CAMPSITE_CAN_BE_RESERVED_MINIMUM_1_DAY_S_AHEAD_OF_ARRIVAL;
import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.THE_CAMPSITE_CAN_BE_RESERVED_UPTO_1_MONTH_IN_ADVANCE;
import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.THE_CAMPSITE_CAN_NOT_BE_RESERVED_FOR_MINIMUM_1_DAY;
import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.THE_CAMPSITE_CAN_NOT_BE_RESERVED_MORE_THAN_3_DAYS;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.RESERVATION_ID;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.campSiteEntity;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.requestVOForMoreThan1MonthInAdvance;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.requestVORequestForToday;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.requestVOWithArrivalDateIsAfterDepartureDate;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.requestVOWithLessThan1Day;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.requestVOWithMoreThan3Days;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.reservationEntity;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.reservationVO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.upgrade.camp.reservation.entity.ReservationEntity;
import com.upgrade.camp.reservation.exception.InvalidRequestException;
import com.upgrade.camp.reservation.exception.ReservationAlreadyExistException;
import com.upgrade.camp.reservation.repository.CampSitesRepository;
import com.upgrade.camp.reservation.repository.ReservationsRepository;
import com.upgrade.camp.reservation.repository.ReservedDatesRepository;
import com.upgrade.camp.reservation.service.impl.ReservationServiceImpl;
import com.upgrade.camp.reservation.vo.ReservationVO;

public class ReservationServiceTest {

	ReservationServiceImpl testee;

	@Mock
	private ReservationsRepository reservationsRepository;

	@Mock
	private CampSitesRepository campSitesRepository;

	@Mock
	private ReservedDatesRepository reservedDatesRepository;

	@BeforeEach
	public void setup() {
		openMocks(this);
		when(campSitesRepository.findByUuid(any())).thenReturn(Optional.of(campSiteEntity()));
		when(reservationsRepository.findByUuidAndCancellationDateIsNull(any())).thenReturn(Optional.of(reservationEntity()));
		testee = new ReservationServiceImpl(reservationsRepository, campSitesRepository, reservedDatesRepository);
	}

	@Test
	public void testCreateReservation() {
		when(reservationsRepository.findByUuidIn(any())).thenReturn(Collections.emptyList());
		when(reservationsRepository.save(any())).thenReturn(reservationEntity());
		ReservationVO request = reservationVO();
		ReservationVO response = testee.createReservation(request);

		assertThat(response).isNotNull();

		assertThat(response.getFullName()).isEqualTo(request.getFullName());
		assertThat(response.getEmailAddress()).isEqualTo(request.getEmailAddress());
		assertThat(response.getArrivalDate()).isEqualTo(request.getArrivalDate());
		assertThat(response.getDepartureDate()).isEqualTo(request.getDepartureDate());
		assertThat(response.getId()).isEqualTo(RESERVATION_ID);
	}

	@Test
	public void testCreateReservationAlreadyExist() {
		when(reservationsRepository.findByUuidIn(any())).thenReturn(List.of(reservationEntity()));
		assertThatThrownBy(() -> testee.createReservation(reservationVO()))
				.isInstanceOf(ReservationAlreadyExistException.class)
				.hasFieldOrPropertyWithValue("message", BOOKING_ALREADY_EXIST_FOR_GIVEN_DATE);
	}

	@Test
	public void testUpdateReservation() {
		when(reservationsRepository.save(any())).thenReturn(reservationEntity());
		ReservationVO request = reservationVO();
		ReservationVO response = testee.updateReservation(request, RESERVATION_ID);

		assertThat(response).isNotNull();

		assertThat(response.getFullName()).isEqualTo(request.getFullName());
		assertThat(response.getEmailAddress()).isEqualTo(request.getEmailAddress());
		assertThat(response.getArrivalDate()).isEqualTo(request.getArrivalDate());
		assertThat(response.getDepartureDate()).isEqualTo(request.getDepartureDate());
		assertThat(response.getId()).isEqualTo(RESERVATION_ID);
	}

	@Test
	public void testUpdateReservationAlreadyExist() {
		ReservationEntity existingEntity = reservationEntity();
		existingEntity.setUuid(UUID.randomUUID().toString());
		when(reservationsRepository.findByUuidAndCancellationDateIsNull(any())).thenReturn(Optional.of(reservationEntity()));
		when(reservationsRepository.findByUuidIn(any())).thenReturn(List.of(existingEntity));
		ReservationVO request = reservationVO();
		assertThatThrownBy(() -> testee.updateReservation(request, RESERVATION_ID))
				.isInstanceOf(ReservationAlreadyExistException.class)
				.hasFieldOrPropertyWithValue("message", BOOKING_ALREADY_EXIST_FOR_GIVEN_DATE);

	}

	@Test
	public void testCreateReservationWithArrivalDateIsAfterDepartureDate() {
		assertThatThrownBy(() -> testee.createReservation(requestVOWithArrivalDateIsAfterDepartureDate()))
				.isInstanceOf(InvalidRequestException.class)
				.hasFieldOrPropertyWithValue("message", DEPARTURE_DATE_SHOULD_BE_AFTER_ARRIVAL_DATE);
	}

	@Test
	public void testCreateReservationForLessThanOneDay() {
		assertThatThrownBy(() -> testee.createReservation(requestVOWithLessThan1Day()))
				.isInstanceOf(InvalidRequestException.class)
				.hasFieldOrPropertyWithValue("message", THE_CAMPSITE_CAN_NOT_BE_RESERVED_FOR_MINIMUM_1_DAY);
	}

	@Test
	public void testCreateReservationForMoreThanThreeDays() {
		assertThatThrownBy(() -> testee.createReservation(requestVOWithMoreThan3Days()))
				.isInstanceOf(InvalidRequestException.class)
				.hasFieldOrPropertyWithValue("message", THE_CAMPSITE_CAN_NOT_BE_RESERVED_MORE_THAN_3_DAYS);
	}

	@Test
	public void testCreateReservationForToday() {
		assertThatThrownBy(() -> testee.createReservation(requestVORequestForToday()))
				.isInstanceOf(InvalidRequestException.class)
				.hasFieldOrPropertyWithValue("message", THE_CAMPSITE_CAN_BE_RESERVED_MINIMUM_1_DAY_S_AHEAD_OF_ARRIVAL);
	}

	@Test
	public void testCreateReservationForMoreThanOneMonthInAdvance() {
		assertThatThrownBy(() -> testee.createReservation(requestVOForMoreThan1MonthInAdvance()))
				.isInstanceOf(InvalidRequestException.class)
				.hasFieldOrPropertyWithValue("message", THE_CAMPSITE_CAN_BE_RESERVED_UPTO_1_MONTH_IN_ADVANCE);
	}

	@Test
	public void testUpdateReservationWithArrivalDateIsAfterDepartureDate() {
		assertThatThrownBy(() -> testee.updateReservation(requestVOWithArrivalDateIsAfterDepartureDate(), RESERVATION_ID))
				.isInstanceOf(InvalidRequestException.class)
				.hasFieldOrPropertyWithValue("message", DEPARTURE_DATE_SHOULD_BE_AFTER_ARRIVAL_DATE);
	}

	@Test
	public void testUpdateReservationForLessThanOneDay() {
		assertThatThrownBy(() -> testee.updateReservation(requestVOWithLessThan1Day(), RESERVATION_ID))
				.isInstanceOf(InvalidRequestException.class)
				.hasFieldOrPropertyWithValue("message", THE_CAMPSITE_CAN_NOT_BE_RESERVED_FOR_MINIMUM_1_DAY);
	}

	@Test
	public void testUpdateReservationForMoreThanThreeDays() {
		assertThatThrownBy(() -> testee.updateReservation(requestVOWithMoreThan3Days(), RESERVATION_ID))
				.isInstanceOf(InvalidRequestException.class)
				.hasFieldOrPropertyWithValue("message", THE_CAMPSITE_CAN_NOT_BE_RESERVED_MORE_THAN_3_DAYS);
	}

	@Test
	public void testUpdateReservationForToday() {
		assertThatThrownBy(() -> testee.updateReservation(requestVORequestForToday(), RESERVATION_ID))
				.isInstanceOf(InvalidRequestException.class)
				.hasFieldOrPropertyWithValue("message", THE_CAMPSITE_CAN_BE_RESERVED_MINIMUM_1_DAY_S_AHEAD_OF_ARRIVAL);
	}

	@Test
	public void testUpdateReservationForMoreThanOneMonthInAdvance() {
		assertThatThrownBy(() -> testee.updateReservation(requestVOForMoreThan1MonthInAdvance(), RESERVATION_ID))
				.isInstanceOf(InvalidRequestException.class)
				.hasFieldOrPropertyWithValue("message", THE_CAMPSITE_CAN_BE_RESERVED_UPTO_1_MONTH_IN_ADVANCE);
	}

}
