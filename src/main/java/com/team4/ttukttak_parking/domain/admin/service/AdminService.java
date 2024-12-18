package com.team4.ttukttak_parking.domain.admin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.ttukttak_parking.domain.admin.dto.holiday.HolidayDTO;
import com.team4.ttukttak_parking.domain.admin.dto.holiday.HolidayItemDTO;
import com.team4.ttukttak_parking.domain.admin.entity.Holiday;
import com.team4.ttukttak_parking.domain.admin.repository.HolidayRepository;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pklt.entity.PkltInfo;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltInfoRepository;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltRepository;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.repository.PkltStatusRepository;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PkltRepository pkltRepository;
    private final PkltStatusRepository pkltStatusRepository;
    private final PkltInfoRepository pkltInfoRepository;
    private final HolidayRepository holidayRepository;
    private final String year = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));

    @Transactional
    public Void loadDefaultDataByJson() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root = objectMapper.readTree(new ClassPathResource("static/data.json").getFile());
        JsonNode dataArray = root.get("DATA");

        for (JsonNode data : dataArray) {
            Pklt pklt = Pklt.to(data);
            pkltRepository.save(pklt);
            PkltInfo pkltInfo = PkltInfo.to(data, pklt);
            pkltInfoRepository.save(pkltInfo);
            PkltStatus pkltStatus = PkltStatus.to(data, pklt);
            pkltStatusRepository.save(pkltStatus);
        }

        return null;
    }

    @Transactional
    public Void loadDefaultDataByXml() throws Exception{
        String urlBuilder = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo" +
                "?" + URLEncoder.encode("serviceKey", StandardCharsets.UTF_8) + "=Q5jQKcexkQ4zVD9oiB5QOkZWjy0gH0UNSxGjlar4otATImX%2Fb8V6jzGNSygnCxywNVDOtwvTnD0npTOK%2B0%2F8Sg%3D%3D" +
                "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1000", StandardCharsets.UTF_8) +
                "&" + URLEncoder.encode("solYear", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(year, StandardCharsets.UTF_8);

        URL url = new URL(urlBuilder);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        String xmlData = sb.toString();

        JAXBContext jaxbContext = JAXBContext.newInstance(HolidayDTO.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        HolidayDTO holidayDTO = (HolidayDTO) unmarshaller.unmarshal(new StringReader(xmlData));

        holidayRepository.deleteAll();
        for (HolidayItemDTO item : holidayDTO.getBody().getItems().getItem()) {
            Holiday holiday = Holiday.to(item);
            holidayRepository.save(holiday);
        }

        return null;
    }

}
