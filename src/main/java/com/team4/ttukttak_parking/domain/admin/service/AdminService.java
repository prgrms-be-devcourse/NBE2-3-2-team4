package com.team4.ttukttak_parking.domain.admin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pklt.entity.PkltInfo;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltInfoRepository;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltRepository;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.repository.PkltStatusRepository;
import java.io.IOException;
import java.util.HashMap;
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
    public Void loadDefaultDataByJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root = objectMapper.readTree(new ClassPathResource("static/data.json").getFile());
        JsonNode dataArray = root.get("DATA");

        HashMap<String, Pklt> pkltMap = new HashMap<>();
        HashMap<Pklt, PkltInfo> infoMap = new HashMap<>();
        HashMap<Pklt, PkltStatus> statMap = new HashMap<>();

        // "prk_type_nm", 노상 주차장은 총 주차면이 1로 여러개의 데이터로 구성되어 있다.
        // 모두 다 더하면 해당 주차장의 총 주차면 수가 나옴
        for (JsonNode data : dataArray) {
            String key = data.get("pklt_nm").asText();

            if (pkltMap.containsKey(key)) {
                PkltStatus pkltStatus = statMap.get(pkltMap.get(key));
                pkltStatus.fixStreetPklt();

                statMap.put(pkltMap.get(key), pkltStatus);
                continue;
            }

            Pklt pklt = Pklt.to(data);
            PkltInfo pkltInfo = PkltInfo.to(data, pklt);
            PkltStatus pkltStatus = PkltStatus.to(data, pklt);

            pkltMap.put(key, pklt);
            infoMap.put(pklt, pkltInfo);
            statMap.put(pklt, pkltStatus);
        }

        pkltMap.forEach((key, value) -> pkltRepository.save(value));
        infoMap.forEach((key, value) -> pkltInfoRepository.save(value));
        statMap.forEach((key, value) -> pkltStatusRepository.save(value));

        return null;
    }

}
