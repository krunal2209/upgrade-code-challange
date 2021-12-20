package com.upgrade.camp.reservation.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.upgrade.camp.reservation.entity.CampSiteEntity;

@Repository
public interface CampSitesRepository extends CrudRepository<CampSiteEntity, Long> {

	Optional<CampSiteEntity> findByUuid(String uuid);

}
