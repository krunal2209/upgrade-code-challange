package com.upgrade.camp.reservation.service.impl;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jdi.InternalException;
import com.upgrade.camp.reservation.entity.CampSiteEntity;
import com.upgrade.camp.reservation.entity.ReservationEntity;
import com.upgrade.camp.reservation.entity.ReservedDatesEntity;
import com.upgrade.camp.reservation.exception.CampSiteNotFoundException;
import com.upgrade.camp.reservation.exception.InvalidRequestException;
import com.upgrade.camp.reservation.exception.ReservationAlreadyExistException;
import com.upgrade.camp.reservation.exception.ReservationNotFoundException;
import com.upgrade.camp.reservation.repository.CampSitesRepository;
import com.upgrade.camp.reservation.repository.ReservationsRepository;
import com.upgrade.camp.reservation.repository.ReservedDatesRepository;
import com.upgrade.camp.reservation.service.ReservationService;
import com.upgrade.camp.reservation.vo.ReservationVO;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

	private final ReservationsRepository reservationsRepository;
	private final CampSitesRepository campSitesRepository;
	private final ReservedDatesRepository reservedDatesRepository;

	private static final String CAMP_SITE_UUID = "2f0d2963-8689-4a01-aa58-40cc93dc47f5";
	private final int MAX_DAYS_FOR_RESERVATION = 3;
	private final int MIN_DAY_AHEAD_OF_ARRIVAL_FOR_RESERVATION = 1;
	private final int MAX_MONTH_AHEAD_OF_ARRIVAL_FOR_RESERVATION = 1;

	@Override
	@Transactional
	public ReservationVO createReservation(ReservationVO reservationRequestVO) {
		CampSiteEntity campSiteEntity = getCampSite();

		LocalDate arrivalDate = reservationRequestVO.getArrivalDate();
		LocalDate departureDate = reservationRequestVO.getDepartureDate();

		validateInputDates(arrivalDate, departureDate);
		validateIfSiteIsAvailable(arrivalDate, departureDate.minusDays(1), null);

		ReservationEntity reservationsEntity = ReservationEntity.builder()
				.uuid(UUID.randomUUID().toString())
				.campSite(campSiteEntity)
				.emailAddress(reservationRequestVO.getEmailAddress())
				.fullName(reservationRequestVO.getFullName())
				.arrivalDate(arrivalDate)
				.departureDate(departureDate)
				.build();
		reservationsEntity.setReservedDates(buildReservedDatesEntity(arrivalDate, departureDate.minusDays(1), reservationsEntity, campSiteEntity));
		ReservationEntity savedReservation = saveReservation(reservationsEntity);
		return ReservationVO.builder()
				.id(savedReservation.getUuid())
				.emailAddress(savedReservation.getEmailAddress())
				.fullName(savedReservation.getFullName())
				.arrivalDate(savedReservation.getArrivalDate())
				.departureDate(savedReservation.getDepartureDate())
				.build();
	}

	@Override
	@Transactional
	public void cancelReservation(String reservationId) {
		ReservationEntity existingReservation = getReservation(reservationId);

		reservedDatesRepository.deleteAll(existingReservation.getReservedDates());
		existingReservation.setReservedDates(new ArrayList<>());
		existingReservation.setCancellationDate(LocalDateTime.now());
		reservationsRepository.save(existingReservation);
	}

	@Override
	@Transactional
	public ReservationVO updateReservation(ReservationVO updateReservationRequestVO, String reservationId) {
		CampSiteEntity campSiteEntity = getCampSite();

		ReservationEntity existingReservation = getReservation(reservationId);

		LocalDate arrivalDate = updateReservationRequestVO.getArrivalDate();
		LocalDate departureDate = updateReservationRequestVO.getDepartureDate();

		validateInputDates(arrivalDate, departureDate);
		validateIfSiteIsAvailable(arrivalDate, departureDate.minusDays(1), reservationId);

		existingReservation.getReservedDates()
				.forEach(reservedDatesEntity -> reservedDatesRepository.deleteById(reservedDatesEntity.getId()));
		existingReservation.getReservedDates().clear();
		reservedDatesRepository.flush();
		existingReservation.setArrivalDate(arrivalDate);
		existingReservation.setDepartureDate(departureDate);
		existingReservation.getReservedDates().addAll(buildReservedDatesEntity(arrivalDate, departureDate.minusDays(1), existingReservation, campSiteEntity));
		ReservationEntity updateEntity = saveReservation(existingReservation);

		return ReservationVO.builder()
				.id(updateEntity.getUuid())
				.emailAddress(updateEntity.getEmailAddress())
				.fullName(updateEntity.getFullName())
				.arrivalDate(updateEntity.getArrivalDate())
				.departureDate(updateEntity.getDepartureDate())
				.build();
	}

	@Override
	@Transactional(readOnly = true)
	public List<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate) {
		if (Objects.isNull(startDate)) {
			startDate = LocalDate.now();
		}
		if (Objects.isNull(endDate)) {
			endDate = startDate.plusMonths(1);
		}

		if (startDate.isAfter(endDate)) {
			throw new InvalidRequestException("Start date can not be after end date date.");
		}
		return findAvailableDates(startDate, endDate.minusDays(1), null);
	}

	private List<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate, String reservationId) {

		List<LocalDate> availableDates = startDate.datesUntil(endDate.plusDays(1))
				.collect(Collectors.toList());

		List<String> listOfBookingIdsInTheGivenRange = new ArrayList<>(reservedDatesRepository.findDistinctReservation(startDate, endDate));

		if (Objects.isNull(reservationId)) {
			reservationsRepository.findByUuidIn(listOfBookingIdsInTheGivenRange)
					.forEach(reservationEntity -> removeReservedDate(reservationEntity, availableDates));
		} else {
			reservationsRepository.findByUuidIn(listOfBookingIdsInTheGivenRange).stream()
					.filter(reservationEntity -> !reservationEntity.getUuid().equals(reservationId))
					.forEach(reservationEntity -> removeReservedDate(reservationEntity, availableDates));
		}
		return availableDates;
	}

	private void removeReservedDate(ReservationEntity reservationEntity, List<LocalDate> availableDates) {
		reservationEntity.getReservedDates()
				.forEach(reservedDatesEntity -> availableDates.remove(reservedDatesEntity.getReservedDate()));
	}

	private CampSiteEntity getCampSite() {
		return campSitesRepository.findByUuid(CAMP_SITE_UUID)
				.orElseThrow(() -> new CampSiteNotFoundException(String.format("No Campsite Found with id %s.", CAMP_SITE_UUID)));
	}

	private ReservationEntity getReservation(String reservationId) {
		try {
			return reservationsRepository.findByUuidAndCancellationDateIsNull(reservationId)
					.orElseThrow(() -> new ReservationNotFoundException(String.format("No reservation found with id: %s.", reservationId)));
		} catch (PessimisticLockingFailureException | ObjectOptimisticLockingFailureException e) {
			throw new InternalException(String.format("Reservation %s is locked by different request.", reservationId));
		}
	}

	private ReservationEntity saveReservation(ReservationEntity reservation) {
		try {
			return reservationsRepository.save(reservation);
		} catch (DataIntegrityViolationException | ObjectOptimisticLockingFailureException e) {
			throw new ReservationAlreadyExistException("Booking already Exist for given date.", e);
		}
	}

	private void validateIfSiteIsAvailable(LocalDate arrivalDate, LocalDate departureDate, String reservationId) {
		List<LocalDate> availableDates = findAvailableDates(arrivalDate, departureDate, reservationId);
		List<LocalDate> requestedDates = arrivalDate.datesUntil(departureDate.plusDays(1))
				.collect(Collectors.toList());

		if (!requestedDates.equals(availableDates)) {
			throw new ReservationAlreadyExistException("Booking already Exist for given date.");
		}
	}

	private void validateInputDates(LocalDate arrivalDate, LocalDate departureDate) {
		if (departureDate.isBefore(arrivalDate)) {
			throw new InvalidRequestException("Departure date should be after arrival date.");
		}

		long noOfDaysToStay = DAYS.between(arrivalDate, departureDate);
		if (noOfDaysToStay < 1) {
			throw new InvalidRequestException("The campsite can not be reserved for minimum 1 day.");
		}
		if (noOfDaysToStay > MAX_DAYS_FOR_RESERVATION) {
			throw new InvalidRequestException("The campsite can not be reserved more than 3 days.");
		}

		long noOfDaysBeforeArrival = DAYS.between(LocalDate.now(), arrivalDate);
		if (noOfDaysBeforeArrival < MIN_DAY_AHEAD_OF_ARRIVAL_FOR_RESERVATION) {
			throw new InvalidRequestException("The campsite can be reserved minimum 1 day(s) ahead of arrival.");
		}

		long noOfMaxDaysInAdvance = DAYS.between(LocalDate.now(), LocalDate.now().plusMonths(MAX_MONTH_AHEAD_OF_ARRIVAL_FOR_RESERVATION));
		if (noOfDaysBeforeArrival > noOfMaxDaysInAdvance) {
			throw new InvalidRequestException("The campsite can be reserved upto 1 month in advance.");
		}

	}

	private List<ReservedDatesEntity> buildReservedDatesEntity(LocalDate arrivalDate, LocalDate departureDate, ReservationEntity reservationEntity, CampSiteEntity campSiteEntity) {
		List<ReservedDatesEntity> reservedDatesEntities = new ArrayList<>();
		long daysBetween = DAYS.between(arrivalDate, departureDate);

		for (int i = 0; i <= daysBetween; i++) {
			ReservedDatesEntity reservedDate = ReservedDatesEntity.builder()
					.reservedDate(arrivalDate.plusDays(i))
					.reservation(reservationEntity)
					.campSite(campSiteEntity)
					.build();
			reservedDatesEntities.add(reservedDate);
		}
		return reservedDatesEntities;
	}
}
