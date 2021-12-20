package com.upgrade.camp.reservation.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.upgrade.camp.reservation.entity.ReservedDatesEntity;

@Repository
public interface ReservedDatesRepository extends JpaRepository<ReservedDatesEntity, Long> {

	@Query(value = "SELECT DISTINCT reservation_id  from reserved_dates where reserved_date >= :startDate AND reserved_date <= :endDate", nativeQuery = true)
	List<String> findDistinctReservation(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

