package com.upgrade.camp.reservation.rest;

import static com.upgrade.camp.reservation.ReservationConstant.BASE_PATH;
import static com.upgrade.camp.reservation.ReservationConstant.PATH_RESERVATION;
import static com.upgrade.camp.reservation.ReservationConstant.PATH_RESERVATION_AVAILABLE_DATES;
import static com.upgrade.camp.reservation.ReservationConstant.PATH_RESERVATION_WITH_ID;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upgrade.camp.reservation.exception.CreateReservationException;
import com.upgrade.camp.reservation.exception.GetReservationException;
import com.upgrade.camp.reservation.exception.UpdateReservationException;
import com.upgrade.camp.reservation.mapper.ReservationRequestMapper;
import com.upgrade.camp.reservation.mapper.ReservationResponseMapper;
import com.upgrade.camp.reservation.mapper.UpdateReservationRequestMapper;
import com.upgrade.camp.reservation.service.ReservationService;
import com.upgrade.camp.reservation.ws.ErrorResponseWS;
import com.upgrade.camp.reservation.ws.ReservationRequestWS;
import com.upgrade.camp.reservation.ws.ReservationResponseWS;
import com.upgrade.camp.reservation.ws.UpdateReservationRequestWS;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Slf4j
@RestController
@RequestMapping(value = BASE_PATH)
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;
	private final ReservationRequestMapper reservationRequestMapper;
	private final UpdateReservationRequestMapper updateReservationRequestMapper;
	private final ReservationResponseMapper reservationResponseMapper;

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reservation created successfully", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReservationResponseWS.class))}),
			@ApiResponse(responseCode = "400", description = "Bad request", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseWS.class))}),
			@ApiResponse(responseCode = "409", description = "Reservation already exists.", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseWS.class))})}
	)
	@RequestMapping(value = PATH_RESERVATION,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.POST)
	public ResponseEntity<ReservationResponseWS> createReservation(@Valid @RequestBody ReservationRequestWS reservationWS) {
		log.info("Create reservation request received with request = {}.", reservationWS);
		return Optional.ofNullable(reservationWS)
				.map(reservationRequestMapper::toReservationVO)
				.map(reservationService::createReservation)
				.map(reservationResponseMapper::toReservationResponseWS)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new CreateReservationException("Error while creating a reservation."));
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Reservation canceled successfully"),
			@ApiResponse(responseCode = "404", description = "No reservation found for given reservation id.", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseWS.class))}),
			@ApiResponse(responseCode = "409", description = "Reservation already exists.", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseWS.class))}),
			@ApiResponse(responseCode = "500", description = "Concurrent update on same reservation.", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseWS.class))})}
	)
	@RequestMapping(value = PATH_RESERVATION_WITH_ID,
			method = RequestMethod.DELETE)
	public ResponseEntity<Void> cancelReservation(@PathVariable String reservationId) {
		log.info("Delete reservation request received for id = {}.", reservationId);
		reservationService.cancelReservation(reservationId);
		return ResponseEntity.noContent().build();
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reservation updates successfully", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReservationResponseWS.class))}),
			@ApiResponse(responseCode = "404", description = "No reservation found for given reservation id.", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseWS.class))}),
			@ApiResponse(responseCode = "409", description = "Reservation already exists.", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseWS.class))}),
			@ApiResponse(responseCode = "500", description = "Concurrent update on same reservation.", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseWS.class))})}
	)
	@RequestMapping(value = PATH_RESERVATION_WITH_ID,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.PATCH)
	public ResponseEntity<ReservationResponseWS> updateReservation(@PathVariable String reservationId, @Valid @RequestBody UpdateReservationRequestWS updateReservationRequestWS) {
		log.info("Update reservation request received for id = {} with request = {}.", reservationId, updateReservationRequestWS);
		return Optional.ofNullable(updateReservationRequestWS)
				.map(updateReservationRequestMapper::toReservationVO)
				.map(requestVO -> reservationService.updateReservation(requestVO, reservationId))
				.map(reservationResponseMapper::toReservationResponseWS)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new UpdateReservationException("Error while updating reservation."));
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Get Reservation successfully", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReservationResponseWS.class))}),
			@ApiResponse(responseCode = "404", description = "No reservation found for given reservation id.", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponseWS.class))})}
	)
	@RequestMapping(value = PATH_RESERVATION_WITH_ID,
			produces = MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.GET)
	public ResponseEntity<ReservationResponseWS> getReservation(@PathVariable String reservationId) {
		log.info("Get reservation request received for id = {}.", reservationId);
		return Optional.ofNullable(reservationId)
				.map(reservationService::getReservation)
				.map(reservationResponseMapper::toReservationResponseWS)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new GetReservationException("Error while fetching reservation."));
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of available dates for reservation.")}
	)
	@RequestMapping(value = PATH_RESERVATION_AVAILABLE_DATES,
			produces = MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.GET)
	public ResponseEntity<List<LocalDate>> findAvailableDates(
			@Parameter(description = "The start date to find available dates for reservation. Default value is today if not provided.") @RequestParam(value = "startDate", required = false) LocalDate startDate,
			@Parameter(description = "The end date to find available dates for reservation. Default value is the date after 1 month if not provided.") @RequestParam(value = "endDate", required = false) LocalDate endDate) {
		log.info("Find reservations request received with startDate = {} and endDate = {}.", startDate, endDate);
		return ResponseEntity.ok(reservationService.findAvailableDates(startDate, endDate));
	}
}
