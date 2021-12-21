package com.upgrade.camp.reservation.ws;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReservationRequestWS {

	@NotNull
	@Schema(description = "Arrival date. It should be future date except today.")
	private LocalDate arrivalDate;

	@NotNull
	@Schema(description = "Departure date. It should be greater than arrival date.")
	private LocalDate departureDate;

}
