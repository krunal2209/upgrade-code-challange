package com.upgrade.camp.reservation.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class AbstractAuditableEntity implements Serializable {

	@CreatedDate
	@Column(name = "created_on")
	private LocalDateTime createdOn;

	@Column(name = "updated_on")
	@LastModifiedDate
	private LocalDateTime updatedOn;

	@Version
	private int version = 1;
}
