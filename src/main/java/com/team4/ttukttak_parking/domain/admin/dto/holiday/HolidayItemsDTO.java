package com.team4.ttukttak_parking.domain.admin.dto.holiday;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

import java.util.List;

@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class HolidayItemsDTO {
    @XmlElement(name = "item")
    private List<HolidayItemDTO> item;
}




