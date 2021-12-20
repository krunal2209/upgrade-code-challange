package com.upgrade.camp.reservation.vo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationVO {

	private String id;

	private String emailAddress;

	private String fullName;

	private LocalDate arrivalDate;

	private LocalDate departureDate;

}
