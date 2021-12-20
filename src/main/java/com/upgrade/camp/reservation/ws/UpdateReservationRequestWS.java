package com.upgrade.camp.reservation.ws;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReservationRequestWS {

	@NotNull
	private LocalDate arrivalDate;

	@NotNull
	private LocalDate departureDate;

}
