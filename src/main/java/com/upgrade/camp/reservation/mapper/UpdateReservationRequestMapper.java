package com.upgrade.camp.reservation.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.upgrade.camp.reservation.vo.ReservationVO;
import com.upgrade.camp.reservation.ws.UpdateReservationRequestWS;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UpdateReservationRequestMapper {

	ReservationVO toReservationVO(UpdateReservationRequestWS updateReservationRequestWS);
}
