package com.team4.ttukttak_parking.domain.admin.dto.holiday;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class HolidayDTO {
    @XmlElement(name = "header")
    private HolidayHeaderDTO header;

    @XmlElement(name = "body")
    private HolidayBodyDTO body;
}




