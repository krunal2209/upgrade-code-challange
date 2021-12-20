package com.upgrade.camp.reservation.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.upgrade.camp.reservation.vo.ReservationVO;
import com.upgrade.camp.reservation.ws.ReservationRequestWS;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReservationRequestMapper {

	ReservationVO toReservationVO(ReservationRequestWS reservationRequestWS);
}
