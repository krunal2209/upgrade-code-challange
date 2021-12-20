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

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upgrade.camp.reservation.exception.CreateReservationException;
import com.upgrade.camp.reservation.exception.UpdateReservationException;
import com.upgrade.camp.reservation.mapper.ReservationRequestMapper;
import com.upgrade.camp.reservation.mapper.ReservationResponseMapper;
import com.upgrade.camp.reservation.mapper.UpdateReservationRequestMapper;
import com.upgrade.camp.reservation.service.ReservationService;
import com.upgrade.camp.reservation.ws.ReservationRequestWS;
import com.upgrade.camp.reservation.ws.ReservationResponseWS;
import com.upgrade.camp.reservation.ws.UpdateReservationRequestWS;

@Slf4j
@RestController
@RequestMapping(value = BASE_PATH)
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;
	private final ReservationRequestMapper reservationRequestMapper;
	private final UpdateReservationRequestMapper updateReservationRequestMapper;
	private final ReservationResponseMapper reservationResponseMapper;

	@RequestMapping(value = PATH_RESERVATION,
			produces = {"application/json"},
			consumes = {"application/json"},
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

	@RequestMapping(value = PATH_RESERVATION_WITH_ID,
			produces = {"application/json"},
			consumes = {"application/json"},
			method = RequestMethod.DELETE)
	public ResponseEntity<Void> cancelReservation(@PathVariable String reservationId) {
		log.info("Delete reservation request received for id = {}.", reservationId);
		reservationService.cancelReservation(reservationId);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = PATH_RESERVATION_WITH_ID,
			produces = {"application/json"},
			consumes = {"application/json"},
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

	@RequestMapping(value = PATH_RESERVATION_AVAILABLE_DATES,
			produces = {"application/json"},
			method = RequestMethod.GET)
	public ResponseEntity<List<LocalDate>> findAvailableDates(
			@RequestParam(value = "startDate", required = false) LocalDate startDate,
			@RequestParam(value = "endDate", required = false) LocalDate endDate) {
		log.info("Find reservations request received with startDate = {} and endDate = {}.", startDate, endDate);
		return ResponseEntity.ok(reservationService.findAvailableDates(startDate, endDate));
	}
}
