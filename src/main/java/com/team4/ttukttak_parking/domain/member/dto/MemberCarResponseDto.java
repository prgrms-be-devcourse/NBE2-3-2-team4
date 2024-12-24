package com.team4.ttukttak_parking.domain.member.dto;

import com.team4.ttukttak_parking.domain.member.entity.Car;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@Getter
@AllArgsConstructor
public class MemberCarResponseDto {
    private Long carId;
    private String carNum;
    private Boolean isPrimaryCar;


    public static MemberCarResponseDto from(Car car){
        return new MemberCarResponseDto(car.getCarId(),car.getCarNum(),car.getIsPrimaryCar());
    }
}
