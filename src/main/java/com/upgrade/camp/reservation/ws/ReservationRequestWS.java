package com.upgrade.camp.reservation.ws;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestWS {

	@NotNull
	@Size(min=1,max=255)
	private String emailAddress;

	@NotNull
	@Size(min=1,max=255)
	private String fullName;

	@NotNull
	private LocalDate arrivalDate;

	@NotNull
	private LocalDate departureDate;

}
