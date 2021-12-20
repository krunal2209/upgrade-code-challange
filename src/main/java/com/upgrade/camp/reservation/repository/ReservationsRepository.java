package com.upgrade.camp.reservation.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.upgrade.camp.reservation.entity.ReservationEntity;

@Repository
public interface ReservationsRepository extends CrudRepository<ReservationEntity, Long> {

	List<ReservationEntity> findByUuidIn(List<String> reservationIds);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query(value = "SELECT re FROM ReservationEntity re WHERE re.uuid = :reservationId AND re.cancellationDate IS NULL")
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "5000")})
	Optional<ReservationEntity> findByUuidAndCancellationDateIsNull(@Param("reservationId") String reservationId);
}

