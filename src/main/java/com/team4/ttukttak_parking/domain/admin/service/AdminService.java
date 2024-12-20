package com.team4.ttukttak_parking.domain.admin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.ttukttak_parking.domain.pklt.dto.PkltResponse.ReadPkltAndStatus;
import com.team4.ttukttak_parking.domain.pklt.entity.Pklt;
import com.team4.ttukttak_parking.domain.pklt.entity.PkltInfo;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltInfoRepository;
import com.team4.ttukttak_parking.domain.pklt.repository.PkltRepository;
import com.team4.ttukttak_parking.domain.pkltstatus.entity.PkltStatus;
import com.team4.ttukttak_parking.domain.pkltstatus.repository.PkltStatusRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Transactional(readOnly = true)
    public List<ReadPkltAndStatus> getLists() {
        List<Pklt> pklts = pkltRepository.findAll();  //주차장 정보 전체 조회
        List<Long> pkltIds = new ArrayList<>();  //주차장 고유 아이디 리스트 생성

        for (Pklt pklt : pklts) {      //갖고온 주차장 정보에서 아이디만 추출해 pkltIds에 삽입
            pkltIds.add(pklt.getPkltId());
        }

        List<PkltStatus> pkltStatuses = pkltStatusRepository.findByPkltPkltIdIn(
            pkltIds);//주차장 고유아이디로 주차장 상태정보 리스트로 갖고 오기

        Map<Long, PkltStatus> pkltStatusMap = new HashMap<>(); //맵 생성
        for (PkltStatus pkltStatus : pkltStatuses) {         //만든 맵에 키값을 주차장 상태정보의 주차장 아이디, 주차장 상태 삽입
            pkltStatusMap.put(pkltStatus.getPklt().getPkltId(), pkltStatus);
        }

        List<ReadPkltAndStatus> pkltReadPkltAndStatuses = new ArrayList<>(); //주차장의 정보와 주차장 상세정보를 담을 리스트 생성
        for (Pklt pklt : pklts) {
            PkltStatus pkltStatus = pkltStatusMap.get(
                pklt.getPkltId());//배열을 돌릴 때 그 값에 맞는 아이디를 불러오고 아이디를 통해 주차장 상세정보를 불러오게 한다.
            int availableSpots = pkltStatus.getTpkct() - pkltStatus.getNowPrkVhclCnt();
            pkltReadPkltAndStatuses.add(ReadPkltAndStatus.from(pklt, availableSpots, pkltStatus));
        }

        return pkltReadPkltAndStatuses;
    }

}
