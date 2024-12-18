package com.team4.ttukttak_parking.domain.admin.dto.holiday;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@XmlRootElement(name = "header")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class HolidayHeaderDTO {

    @XmlElement(name = "resultCode")
    private String resultCode;

    @XmlElement(name = "resultMsg")
    private String resultMsg;

}




