package com.upgrade.camp.reservation.mapper;

import static com.upgrade.camp.reservation.fixture.ReservationFixture.reservationRequestWS;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.upgrade.camp.reservation.vo.ReservationVO;
import com.upgrade.camp.reservation.ws.ReservationRequestWS;

public class ReservationRequestMapperTest {

	ReservationRequestMapperImpl testee;

	@BeforeEach
	public void setUp() {
		testee = new ReservationRequestMapperImpl();
	}

	@Test
	public void testReservationVOMapping() {
		ReservationRequestWS reservationRequestWS = reservationRequestWS();
		ReservationVO response = testee.toReservationVO(reservationRequestWS);

		assertThat(response.getFullName()).isEqualTo(reservationRequestWS.getFullName());
		assertThat(response.getEmailAddress()).isEqualTo(reservationRequestWS.getEmailAddress());
		assertThat(response.getArrivalDate()).isEqualTo(reservationRequestWS.getArrivalDate());
		assertThat(response.getDepartureDate()).isEqualTo(reservationRequestWS.getDepartureDate());
	}

}
