package com.upgrade.camp.reservation.ws;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestWS {

	@NotNull
	@Size(min=1,max=255)
	@Schema(example = "abc@test.com", description = "Email address")
	private String emailAddress;

	@NotNull
	@Size(min=1,max=255)
	@Schema(example = "TestName", description = "Full name")
	private String fullName;

	@NotNull
	@Schema(description = "Arrival date. It should be future date except today.")
	private LocalDate arrivalDate;

	@NotNull
	@Schema(description = "Departure date. It should be greater than arrival date.")
	private LocalDate departureDate;

}
