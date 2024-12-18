package com.team4.ttukttak_parking.domain.admin.entity;

import com.team4.ttukttak_parking.domain.admin.dto.holiday.HolidayItemDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Holidays")
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long holidayId;
    private String dateName;
    @Column(name = "holidayFlag")
    private Boolean holidayFlag;
    @Column(name = "localDate")
    private LocalDate localDate;

    public static Holiday to(HolidayItemDTO dto) {
        return Holiday.builder()
            .dateName(dto.getDateName())
            .holidayFlag(dto.getHolidayFlag())
            .localDate(dto.getLocalDate())
            .build();
    }
}
