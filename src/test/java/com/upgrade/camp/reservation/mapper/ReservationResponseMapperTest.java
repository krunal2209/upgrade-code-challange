package com.upgrade.camp.reservation.mapper;

import static com.upgrade.camp.reservation.fixture.ReservationFixture.reservationVO;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.upgrade.camp.reservation.vo.ReservationVO;
import com.upgrade.camp.reservation.ws.ReservationResponseWS;

public class ReservationResponseMapperTest {

	ReservationResponseMapperImpl testee;

	@BeforeEach
	public void setUp() {
		testee = new ReservationResponseMapperImpl();
	}

	@Test
	public void testReservationResponseWSMapping() {
		ReservationVO reservationVO = reservationVO();
		ReservationResponseWS response = testee.toReservationResponseWS(reservationVO);

		assertThat(response.getId()).isEqualTo(reservationVO.getId());
		assertThat(response.getFullName()).isEqualTo(reservationVO.getFullName());
		assertThat(response.getEmailAddress()).isEqualTo(reservationVO.getEmailAddress());
		assertThat(response.getArrivalDate()).isEqualTo(reservationVO.getArrivalDate());
		assertThat(response.getDepartureDate()).isEqualTo(reservationVO.getDepartureDate());
	}

}
