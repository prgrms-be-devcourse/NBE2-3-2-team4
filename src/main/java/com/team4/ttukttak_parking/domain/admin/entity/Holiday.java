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
    private String dateKind;
    private String dateName;
    @Column(name = "holidayFlag")
    private boolean holidayFlag;
    @Column(name = "localDate")
    private LocalDate localDate;
    private int seq;

    public static Holiday to(HolidayItemDTO dto) {
        return Holiday.builder()
            .dateKind(dto.getDateKind())
            .dateName(dto.getDateName())
            .holidayFlag(dto.isHolidayFlag())
            .localDate(dto.getLocalDate())
            .seq(dto.getSeq())
            .build();
    }
}
