package com.upgrade.camp.reservation.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "reserved_dates")
@EqualsAndHashCode(callSuper = true)
public class ReservedDatesEntity extends AbstractAuditableEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "reserved_date", nullable = false)
	private LocalDate reservedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "camp_site_uuid", referencedColumnName = "uuid")
	private CampSiteEntity campSite;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id")
	private ReservationEntity reservation;
}
