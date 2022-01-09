package com.upgrade.camp.reservation.service;

import java.time.LocalDate;
import java.util.List;

import com.upgrade.camp.reservation.vo.ReservationVO;

public interface ReservationService {

	ReservationVO createReservation(ReservationVO reservationRequestVO);

	void cancelReservation(String reservationId);

	ReservationVO updateReservation(ReservationVO updateReservationRequestVO, String reservationId);

	ReservationVO getReservation(String reservationId);

	List<LocalDate> findAvailableDates(LocalDate startDate, LocalDate endDate);
}
