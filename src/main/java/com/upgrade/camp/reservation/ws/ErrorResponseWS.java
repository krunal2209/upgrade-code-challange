package com.upgrade.camp.reservation.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseWS {

	@Schema(example = "409", description = "HTTP status code.")
	private Integer status;

	@Schema(example = "Booking already Exist for given date.", description = "Detailed error message.")
	private String message;

}

