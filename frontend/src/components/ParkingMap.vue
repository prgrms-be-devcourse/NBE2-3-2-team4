<template>
  <div class="map-container">
    <div v-if="latitude && longitude">
      <KakaoMap 
        :lat="latitude" 
        :lng="longitude" 
        :draggable="true"
        :disable-double-click-zoom="true" 
        :height="2000" 
        :width="2000" 
        id="map"
        @onLoadKakaoMap="onLoadKakaoMap"
      >
        <KakaoMapMarker 
          :lat="latitude" 
          :lng="longitude" 
        />
        <KakaoMapMarker 
          v-if="selectedPlace"
          :lat="selectedPlace.y"
          :lng="selectedPlace.x" 
        />
         <!-- 사용자 지정 오버레이 -->
          <KakaoMapCustomOverlay 
          v-if="selectedPlace" 
          :lat="selectedPlace.y" 
          :lng="selectedPlace.x"
          >
          <div class="custom-overlay">
            <h4>{{ selectedPlace.place_name }}</h4>
            <p>{{ selectedPlace.road_address_name || selectedPlace.address_name }}</p>
            <p>거리: {{ distanceToSelectedPlace }} km</p>
          </div>
          </KakaoMapCustomOverlay>
         
       <!-- 주차장 마커들 -->
       <template v-if="nearbyPklt && nearbyPklt.length > 0">
          <KakaoMapMarker
            v-for="pklt in nearbyPklt"
            :key="pklt.id"
            :lat="Number(pklt.latitude)"
            :lng="Number(pklt.longitude)"
            :options="{
              image: {
                src: '/parking-marker.png',
                size: { width: 24, height: 24 }
              }
            }"
          />    
      </template>
      </KakaoMap>

      <!-- 현재 위치 버튼 -->
      <button @click="moveToCurrentLocation" class="current-location-button">
        <img 
          :src="currentLocationIcon" 
          alt="현재 위치 버튼" 
          style="width: 60%; height: 60%; object-fit: contain;" 
        />
      </button>
    </div>
    <div v-else class="loading">
      <p>위치 정보를 불러오는 중입니다...</p>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted, watch, defineProps, defineEmits } from 'vue';
import { KakaoMap, KakaoMapMarker, KakaoMapCustomOverlay } from 'vue3-kakao-maps';
import currentLocationIcon from "@/assets/current-location-button.png";

const props = defineProps({
  selectedPlace: Object,
  nearbyPklt: Object
});

const latitude = ref(null);
const longitude = ref(null);
const map = ref();
const distanceToSelectedPlace = ref(0);

const getLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        latitude.value = position.coords.latitude;
        longitude.value = position.coords.longitude;
      },
      (err) => {
        console.error(`오류 발생: ${err.message}`);
      }
    );
  } else {
    console.error("Geolocation을 지원하지 않는 브라우저입니다.");
  }
};

const onLoadKakaoMap = (mapRef) => {
  map.value = mapRef;
};

const emit = defineEmits(['resetPlace']); // 부모에게 이벤트 전달

const moveToCurrentLocation = () => {
  if (map.value && latitude.value && longitude.value) {
    getLocation();
    const targetPosition = new kakao.maps.LatLng(latitude.value, longitude.value);
    map.value.panTo(targetPosition);
    emit('resetPlace');
  }
};

const calculateDistance = (lat1, lng1, lat2, lng2) => {
  const R = 6371;
  const dLat = ((lat2 - lat1) * Math.PI) / 180;
  const dLng = ((lng2 - lng1) * Math.PI) / 180;
  const a = 
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos((lat1 * Math.PI) / 180) * 
    Math.cos((lat2 * Math.PI) / 180) * 
    Math.sin(dLng / 2) * Math.sin(dLng / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return (R * c).toFixed(2);
};

watch(
  () => props.selectedPlace,
  (newPlace) => {
    if (newPlace && map.value) {
      const { y: lat, x: lng } = newPlace;
      const targetPosition = new kakao.maps.LatLng(parseFloat(lat), parseFloat(lng));
      map.value.panTo(targetPosition);

      distanceToSelectedPlace.value = calculateDistance(
        latitude.value, 
        longitude.value, 
        parseFloat(lat), 
        parseFloat(lng)
      );
    }
  }
);
// nearbyPklt 변경 감시
watch(() => props.nearbyPklt, () => {
      console.log('주변 주차장 조회 완료');
    }, { immediate: true });
onMounted(() => {
  getLocation();
});
</script>

<style scoped>

.map-container {
    display: flex;
    justify-content: center; /* 가로 중앙 정렬 */
    align-items: center;
    overflow: hidden;
    height: 100vh; /* 화면 전체 높이를 기준으로 정렬 */
  }
.current-location-button {
    position: fixed;
    top: 25%;
    left: 3%;
    z-index: 1000;
    background-color: #fff;
    border: 1px solid #ddd;
    border-radius: 50%; /* 완벽한 원형 */
    padding: 0; /* 불필요한 padding 제거 */
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
    cursor: pointer;
    width: 35px; /* 고정된 너비 */
    height: 35px; /* 고정된 높이 */
    display: flex;
    justify-content: center; /* 가로 중앙 정렬 */
    align-items: center; /* 세로 중앙 정렬 */
}
.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}
.custom-overlay {
  position: absolute;
  background-color: #fff; /* 배경색 */
  top: 50%; /* 부모 요소 기준 세로 중앙 */
  left: 50%; /* 부모 요소 기준 가로 중앙 */
  transform: translate(-50%, -180%); /* 중앙으로 이동 */
  border-radius: 10px; /* 모서리 둥글게 */
  padding: 10px; /* 내부 여백 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 그림자 효과 */
  max-width: 300px; /* 최대 너비 제한 */
  font-family: Arial, sans-serif; /* 폰트 설정 */
  color: #333; /* 글자 색상 */
  overflow: hidden;
}

.custom-overlay h4 {
  margin: 0 0 10px 0; /* 제목 아래 여백 */
  font-size: 1em; /* 제목 글자 크기 */
  color: #007BFF; /* 제목 색상 */
}

.custom-overlay p {
  margin: 3px 0; /* 문단 간 여백 */
  font-size: 0.7em; /* 문단 글자 크기 */
  color: #555; /* 문단 색상 */
}

.custom-overlay p:last-of-type {
  font-weight: bold; /* 거리 텍스트 강조 */
  color: #28A745; /* 거리 텍스트 색상 */
}


</style>