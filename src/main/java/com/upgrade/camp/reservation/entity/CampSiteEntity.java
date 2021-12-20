package com.upgrade.camp.reservation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "camp_sites")
@EqualsAndHashCode(callSuper = true)
public class CampSiteEntity extends AbstractAuditableEntity {

	@Id
	@Column(name = "uuid", nullable = false)
	private String uuid;

	private String name;
}
