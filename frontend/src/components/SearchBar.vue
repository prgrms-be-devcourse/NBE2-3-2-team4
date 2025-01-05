<!-- eslint-disable vue/no-multiple-template-root -->
<!-- eslint-disable no-unused-vars -->
<!-- eslint-disable no-undef -->
<template>
    
        <!-- 검색창 -->
    <div class="search-bar">
      <input 
        type="text" 
        placeholder="목적지를 입력해주세요" 
        v-model="keyword"
        @input="onInput"
        @keypress.enter="onSearch"
        class="search-input" 
      />
      <button @click="onSearch" class="search-button">🔍</button>
    </div>
    <!-- 검색 결과 -->
     <div class="search-results" v-if="places.length">
        <div v-for="(place, index) in places" :key="index" class="result-item" @click="() => clickAddr(place)">
          <span style="font-size:medium; font-weight: bolder; color: #000;">{{ place.place_name }}</span>
          <span style="float: right; font-weight: bold; color: gray;">{{ (place.distance / 1000).toFixed(2) }} km</span>
          <div>{{ place.road_address_name || place.address_name }}</div>
        </div>
    </div>
    

</template>
<script setup>
import { ref } from 'vue';

const keyword = ref('');
const places = ref([]);
const userLocation = ref(null);

// 부모 컴포넌트에 데이터 전달
const emit = defineEmits(['select-place']);

// 장소를 클릭했을 때 부모에게 전달
// eslint-disable-next-line no-unused-vars
const clickAddr = (place) => {
  emit('select-place', place);
  places.value = [];
};

// 사용자 현재 위치 가져오기
navigator.geolocation.getCurrentPosition(
  (position) => {
    userLocation.value = {
      lat: position.coords.latitude,
      lng: position.coords.longitude,
    };
  },
  (error) => {
    console.error('위치 정보를 가져올 수 없습니다.', error);
  }
);

// eslint-disable-next-line no-unused-vars
const onInput = () => {
  if (keyword.value.trim().length === 0) {
    places.value = [];
  }
};

// eslint-disable-next-line no-unused-vars
const onSearch = () => {
  if (!keyword.value) return;

  const ps = new window.kakao.maps.services.Places();

  ps.keywordSearch(keyword.value, (data, status) => {
  // eslint-disable-next-line no-undef
  if (status === kakao.maps.services.Status.OK) {
    // 거리 계산 후 정렬
    places.value = data
      .map(place => {
        const distance = getDistance(
          userLocation.value.lat,
          userLocation.value.lng,
          place.y,
          place.x
        );
        return { ...place, distance };
      })
      //.sort((a, b) => a.distance - b.distance); // 거리 기준 보조 정렬
  } else {
    console.error('검색 결과가 없습니다.');
    places.value = [];
  }
}, {
  size: 45
}

);

};

// 두 좌표 사이의 거리 계산 함수
const getDistance = (lat1, lng1, lat2, lng2) => {
  const R = 6371e3; // 지구 반지름 (미터)
  const toRad = (value) => (value * Math.PI) / 180;
  const φ1 = toRad(lat1);
  const υ2 = toRad(lat2);
  const Δφ = toRad(lat2 - lat1);
  const Δλ = toRad(lng2 - lng1);

  const a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
            Math.cos(φ1) * Math.cos(υ2) *
            Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

  return Math.round(R * c); // 거리 반환 (미터)
};
</script>
<style>
.search-bar {
    position: fixed; 
    top: 60px; /* 화면 상단에서 20px 아래 */
    left: 50%; /* 가로 중앙 정렬 */
    transform: translateX(-50%); /* 정확히 중앙에 배치 */
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 6px; /* 입력창과 버튼 간격 */
    background-color: rgba(255, 255, 255, 1); /* 반투명한 흰색 배경 */
    padding: 10px 15px; /* 내부 여백 */
    border-radius: 5px; /* 둥근 모서리 */
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2); /* 그림자 효과 */
    z-index: 1000; /* 다른 요소 위에 배치 */
    width: 85%;
}
.search-input {
    flex: 1;
    padding: 8px 10px;
    border: 1px solid #ddd;
    border-radius: 3px;
    font-size: 0.7rem;
    outline: none;
}
.search-button {
    background-color: rgba(182, 182, 182, 0.467);
    color: white;
    border: none;
    border-radius: 3px;
    padding: 8px 10px;
    font-size: 0.65rem;
    cursor: pointer;
}
.search-button:hover {
    background-color: #676767;
}
.search-results {
    position: fixed; 
    top: 120px; /* 화면 상단에서 20px 아래 */
    left: 50%; /* 가로 중앙 정렬 */
    transform: translateX(-50%); /* 정확히 중앙에 배치 */
    display:flexbox;
    background-color: rgba(255, 255, 255, 1); /* 반투명한 흰색 배경 */
    border-radius: 5px; /* 둥근 모서리 */
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3); /* 그림자 효과 */
    z-index: 1000; /* 다른 요소 위에 배치 */
    width: 85%;
    height: 65%;
    overflow-y: auto;
    padding: 10px 15px; /* 내부 여백 */

}
.result-item {
    text-align: left;
    border-bottom: 1px solid #f1f1f1;
    display: flexbox;
    font-size: 0.8rem;
    padding: 10px;
    width: 90%;
}
.result-item:last-child {
    border-bottom: none;
}
.result-item:hover {
    background-color: #f9f9f9;
    cursor: pointer;
}
</style>