package com.upgrade.camp.reservation.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reservations")
@EqualsAndHashCode(callSuper = true)
public class ReservationEntity extends AbstractAuditableEntity {

	@Id
	@Column(name = "uuid", nullable = false, unique = true)
	private String uuid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "camp_site_uuid")
	private CampSiteEntity campSite;

	@Column(name = "email_address", nullable = false)
	private String emailAddress;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Column(name = "arrival_date", nullable = false)
	private LocalDate arrivalDate;

	@Column(name = "departure_date", nullable = false)
	private LocalDate departureDate;

	@Column(name = "cancellation_date")
	private LocalDateTime cancellationDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "reservation", cascade = CascadeType.ALL)
	@Builder.Default
	private List<ReservedDatesEntity> reservedDates = new ArrayList<>();

	private List<LocalDate> bookedDates() {
		return this.reservedDates.stream()
				.map(ReservedDatesEntity::getReservedDate)
				.collect(Collectors.toList());
	}
}
