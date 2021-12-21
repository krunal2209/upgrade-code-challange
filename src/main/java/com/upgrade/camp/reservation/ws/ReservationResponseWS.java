package com.upgrade.camp.reservation.ws;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseWS {

	@Schema(example = "7c48b7f2-76ec-4c4d-bf98-01a6e734f2f2", description = "Unique identifier for the reservation.")
	private String id;

	@Schema(example = "abc@test.com", description = "Email address")
	private String emailAddress;

	@Schema(example = "TestName", description = "Full name")
	private String fullName;

	@Schema(description = "Arrival date. It should be future date except today.")
	private LocalDate arrivalDate;

	@Schema(description = "Departure date. It should be greater than arrival date.")
	private LocalDate departureDate;

}
