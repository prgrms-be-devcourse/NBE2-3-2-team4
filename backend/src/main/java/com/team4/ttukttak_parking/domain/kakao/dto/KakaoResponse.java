package com.team4.ttukttak_parking.domain.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import org.springframework.security.core.parameters.P;

public record KakaoResponse() {

    public record Place(
        Long placeId,
        String address,
        String roadAddress,
        Double x,
        Double y,
        String name
    ) {

        public static Place from(JsonNode data) {
            return new Place(
                data.get("id").asLong(),
                data.get("address_name").asText(),
                data.get("road_address_name").asText(),
                data.get("x").asDouble(),
                data.get("y").asDouble(),
                data.get("place_name").asText()
            );
        }

    }

}
