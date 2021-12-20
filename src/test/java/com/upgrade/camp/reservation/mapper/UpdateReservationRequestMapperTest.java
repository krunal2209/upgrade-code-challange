package com.upgrade.camp.reservation.mapper;

import static com.upgrade.camp.reservation.fixture.ReservationFixture.updateReservationRequestWS;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.upgrade.camp.reservation.vo.ReservationVO;
import com.upgrade.camp.reservation.ws.UpdateReservationRequestWS;

public class UpdateReservationRequestMapperTest {

	UpdateReservationRequestMapperImpl testee;

	@BeforeEach
	public void setUp() {
		testee = new UpdateReservationRequestMapperImpl();
	}

	@Test
	public void testReservationResponseWSMapping() {
		UpdateReservationRequestWS updateReservationRequestWS = updateReservationRequestWS();
		ReservationVO response = testee.toReservationVO(updateReservationRequestWS);

		assertThat(response.getArrivalDate()).isEqualTo(updateReservationRequestWS.getArrivalDate());
		assertThat(response.getDepartureDate()).isEqualTo(updateReservationRequestWS.getDepartureDate());
	}
}
