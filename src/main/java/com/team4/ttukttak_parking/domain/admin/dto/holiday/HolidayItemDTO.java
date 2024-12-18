package com.team4.ttukttak_parking.domain.admin.dto.holiday;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class HolidayItemDTO {
    @XmlElement(name = "dateKind")
    private String dateKind;

    @XmlElement(name = "dateName")
    private String dateName;

    private boolean holidayFlag;

    private LocalDate localDate;

    @XmlElement(name = "seq")
    private int seq;

    @XmlElement(name = "locdate")
    public void setLocdate(String locdate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        this.localDate = LocalDate.parse(locdate, formatter);;
    }

    @XmlElement(name = "isHoliday")
    public void setIsHoliday(String isHoliday) {
        this.holidayFlag = isHoliday.equals("Y") ? true : false;
    }
}




