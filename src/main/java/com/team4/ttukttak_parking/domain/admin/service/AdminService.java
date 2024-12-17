package com.team4.ttukttak_parking.domain.admin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pklt.entity.PkltInfo;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltInfoRepository;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltRepository;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.repository.PkltStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PkltRepository pkltRepository;
    private final PkltStatusRepository pkltStatusRepository;
    private final PkltInfoRepository pkltInfoRepository;

    @Transactional
    public Void loadDefaultDataByJson() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root = objectMapper.readTree(new ClassPathResource("static/data.json").getFile());
        JsonNode dataArray = root.get("DATA");

        for (JsonNode data : dataArray) {
            Pklt pklt = Pklt.toEntity(data);
            pkltRepository.save(pklt);
            PkltInfo pkltInfo = PkltInfo.toEntity(data, pklt);
            pkltInfoRepository.save(pkltInfo);
            PkltStatus pkltStatus = PkltStatus.toEntity(data, pklt);
            pkltStatusRepository.save(pkltStatus);
        }

        return null;
    }

}
