<template>
    <NavigationBar />
    <InputBar @back-click="handleBack" @input-focus="handleInputFocus" />
    <div class="map-container">
        <div v-if="latitude && longitude">
            <KakaoMap 
                :lat="latitude" 
                :lng="longitude" 
                :draggable="true"
                :disable-double-click-zoom="true"
                :width="'100%'"
                :height="750"
                id="map"
                @onLoadKakaoMap="onLoadKakaoMap"
            >
                <KakaoMapMarker 
                    :lat="latitude" 
                    :lng="longitude" 
                />

                <KakaoMapMarker
                    v-if="selectedPlace"
                    :lat="Number(selectedPlace.y)"
                    :lng="Number(selectedPlace.x)"
                    :clickable="true"
                    :image="{
                        imageSrc: getDestinationMarker(),
                        imageWidth: 45,
                        imageHeight: 45
                    }"
                />

        <!-- 주차장 마커들 -->
        <template v-if="pkltList">
            <KakaoMapMarker
                v-for="(pklt, index) in pkltList"
                :key="index"
                :lat="Number(pklt.latitude)"
                :lng="Number(pklt.longitude)"
                :clickable="true"
                :id="pklt.placeId"
                :image="{
                    imageSrc: getMarkerImage(pklt.status),
                    imageWidth: 40,
                    imageHeight: 56,
                }"
               @onClickKakaoMapMarker="handleParkingClick(pklt)"
            />    
        </template>
        
            </KakaoMap>
        </div>
        <!-- 검색 결과창 수정 -->
        <div v-if="showSearchResults" class="search-results">
            <div v-for="(place, index) in searchResults.data"
                :key="index" 
                class="result-item" 
                @click="handlePlaceClick(place)"
            >
                <div class="content">
                    <span class="location-name">                    
                        <img src="@/assets/gray-marker.png" alt="marker" class="marker-image" style="width: 20px; height: auto;">
                        {{ place.name }}
                        <div class="distance">{{ getDistance(place) }}km</div>
                    </span>
                    <div class="road-address">
                        <p>도로명</p>
                        <span>{{ place.roadAddress }}</span>
                    </div>
                    <div class="road-address">
                        <p>지번</p>
                        <span>{{ place.address }}</span>
                    </div>
                </div>  
            </div>
        </div>
        <ParkingStatus 
            v-show="!successSearch"
            :latitude="statusLatitude" 
            :longitude="statusLongitude" 
            @update-pklt-list="handlePkltListUpdate"
            @select-parking="(Parking) => handleStatusClick(Parking)"
        />
        <div v-if="selectedParking" class="parking-detail-overlay">
            <div class="parking-detail-content">
                <div class="parking-status">
                    <h3 class="status-title">실시간 주차 현황</h3>
                    <div class="status-grid">
                        <div class="status-item total">
                            <span class="status-label">전체</span>
                            <span class="status-value">{{ pkltStatus?.data?.totalSpots }}</span>
                        </div>
                        <div class="status-item used">
                            <span class="status-label">사용 중</span>
                            <span class="status-value">{{ pkltStatus?.data?.usedSpots }}</span>
                        </div>
                        <div class="status-item available">
                            <span class="status-label">여유</span>
                            <span class="status-value">{{ pkltStatus?.data?.availableSpots }}</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="parking-info">
                <div class="location-name">                    
                    <div class="parking-name"><p>주소</p> <span>{{ pkltInfo?.data?.address}}</span></div>
                </div>
                <div class="location-name">                    
                    <div class="parking-name">
                        <p>운영</p>
                        <span>24시간 운영</span>
                    </div>
                </div>
            </div>
            <div class="parking-ticket-info">
                <h3 class="ticket-title">주차권 정보</h3>
                <div class="ticket-list">
                    <label v-for="(ticket, index) in pkltTicketInfo?.data" 
                           :key="index" 
                           class="ticket-item"
                           :id="ticket.ticketId"
                    >
                        <input 
                            type="radio" 
                            name="parking-ticket" 
                            :value="ticket"
                            v-model="selectedTicket"
                            class="ticket-radio"
                        >
                        <div class="ticket-content">
                            <div class="ticket-duration">{{ ticket.pkDuration }}시간 주차권</div>
                            <div class="ticket-price">₩ {{ ticket.price.toLocaleString() }}</div>
                        </div>
                    </label>
                </div>
            </div>
            <PayFooter @payment="handlePayment" />
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref, watch, onBeforeUnmount } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import InputBar from '@/components/InputBar.vue';
import NavigationBar from '@/components/NavigationBar.vue';
import { KakaoMap, KakaoMapMarker } from 'vue3-kakao-maps';
import { getLocation } from '@/utils/geolocation';
import axios from '@/plugins/axios'; 
import ParkingStatus from '@/components/ParkingStatus.vue';
import PayFooter from '@/components/PayFooter.vue';
import { useStore } from 'vuex';

const store = useStore();

const route = useRoute();  // 추가
const map = ref(null);
const latitude = ref(null);
const longitude = ref(null);
const searchKeyword = ref(route.query.keyword || '');  // 추가
const searchResults = ref([]);
const showSearchResults = ref(false);
const statusLatitude = ref(null);
const statusLongitude = ref(null);
const selectedPlace = ref(null);
const selectedParking = ref(null);

const router = useRouter();  // router 인스턴스 생성

const onLoadKakaoMap = (mapRef) => {
  map.value = mapRef;
};

const searchPlaces = async () => {
    const response = await axios.get(`/api/kakao/search?keyword=${searchKeyword.value}&size=15`);
    searchResults.value = response.data;
    console.log("함수",searchResults.value);
};

const handleBack = () => {
    if (showSearchResults.value && selectedParking.value === null) {
        showSearchResults.value = false;
        router.replace({ 
            query: {} 
        });
    } else if (selectedParking.value !== null) {
        selectedParking.value = null;
        selectedTicket.value = null;
        store.commit('setCancellationData', null);
    } else {
        router.push('/');
    }
};

const handleInputFocus = () => {
    showSearchResults.value = true;
    selectedParking.value = null;
};

// URL 쿼리 변경 감지
watch(() => route.query.keyword, (newKeyword) => {
    if (newKeyword) {
        searchKeyword.value = newKeyword;
        searchPlaces();
    } else {
        searchKeyword.value = '';  
    }
}, { immediate: true });



onMounted(async () => {
    const position = await getLocation();
    latitude.value = position.latitude;
    longitude.value = position.longitude;
    handlePlaceStatus();
    checkCancellation();
});

onBeforeUnmount(() => {
});


// 특정 장소와의 거리를 계산하는 메서드
const getDistance = (place) => {
  return calculateDistance(
    latitude.value,
    longitude.value,
    place.y,  // 카카오맵 API는 y가 위도(latitude)입니다
    place.x   // x가 경도(longitude)입니다
  );

}

const getMarkerImage = (status) => {
    const images = {
        '여유': '/images/parking-available.png',
        '보통': '/images/parking-normal.png',
        '혼잡': '/images/parking-busy.png'
    };

    return images[status] || images['보통'];
}

// 거리 계산 함수 (단위: km)
const calculateDistance = (lat1, lon1, lat2, lon2) => {
    const R = 6371; 
    const dLat = toRad(lat2 - lat1);
    const dLon = toRad(lon2 - lon1);
    
    const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
             Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * 
             Math.sin(dLon/2) * Math.sin(dLon/2);
    
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    const distance = R * c;
    
    return Number(distance.toFixed(1)); // 소수점 첫째자리까지 반환
}

// 각도를 라디안으로 변환하는 헬퍼 함수
const toRad = (value) => {
    return value * Math.PI / 180;
}

// handlePlaceStatus 함수 수정
const handlePlaceStatus = () => {
    statusLatitude.value = latitude.value;
    statusLongitude.value = longitude.value;
};

// handlePlaceClick 함수 수정
const handlePlaceClick = (place) => {
    statusLatitude.value = place.y;
    statusLongitude.value = place.x;
    selectedPlace.value = place;
    
    if (map.value) {
        const moveLatLng = new window.kakao.maps.LatLng(place.y, place.x);
        map.value.setCenter(moveLatLng);
        map.value.setLevel(4);
    }

    showSearchResults.value = false;  // 검색 결과창 닫기
};

const pkltList = ref([]);

const handlePkltListUpdate = (newPkltList) => {
  pkltList.value = newPkltList;
};

// getDestinationMarker 함수 추가
const getDestinationMarker = () => {
    const svgMarker = `
        <svg width="45" height="45" viewBox="0 0 45 45" xmlns="http://www.w3.org/2000/svg">
            <path d="M22.5 2C14.5 2 8 8.5 8 16.5C8 27.5 22.5 43 22.5 43S37 27.5 37 16.5C37 8.5 30.5 2 22.5 2ZM22.5 22C19.5 22 17 19.5 17 16.5C17 13.5 19.5 11 22.5 11C25.5 11 28 13.5 28 16.5C28 19.5 25.5 22 22.5 22Z" 
              fill="#4B89DC"/>
        </svg>
    `;
    return 'data:image/svg+xml;charset=utf-8,' + encodeURIComponent(svgMarker);
};

const pkltTicketInfo = ref(null);

const setPkltTicketInfo = async (pkltId) => {
    const response = await axios.get(`/api/pklt/${pkltId}/tickets`);
    pkltTicketInfo.value = response.data;
}

const pkltInfo = ref(null);

const setPkltInfo = async (pkltId) => {
    const response = await axios.get(`/api/pklt/${pkltId}`);
    pkltInfo.value = response.data;
}

const pkltStatus = ref(null);

const setPkltStatus = async (pkltId) => {
    const response = await axios.get(`/api/pklt/${pkltId}/status`);
    pkltStatus.value = response.data;
};

const handleParkingClick = async (parking) => {
    selectedParking.value = parking;
    setPkltStatus(parking.pkltId);
    setPkltTicketInfo(parking.pkltId);
    setPkltInfo(parking.pkltId);
    console.log(parking);
};

const handleStatusClick = (parking) => {
    if (map.value) {
        const moveLatLng = new window.kakao.maps.LatLng(parking.latitude, parking.longitude);
        map.value.setCenter(moveLatLng);    
        map.value.setLevel(4);
    }
};



const selectedTicket = ref(null);
const handlePayment = (carNumber) => {
    if (!selectedTicket.value) {
        alert('주차권을 선택해 주세요.');  // 주차권 선택 확인
        return;
    }
    
    if (!carNumber) {
        alert('차량번호를 입력해 주세요.');  // 차량번호 입력 확인
        return;
    }


    // 결제 처리 로직
    processPayment(selectedParking.value, selectedTicket.value, carNumber);
}

const processPayment = (SelectedParking, SelectedTicket, carNumber) => {

    // 결제 처리 로직
    // Vuex 스토어에 결제 정보 저장
    store.dispatch('setPaymentInfo', { 
        selectedParking: SelectedParking,  // 객체 자체 저장
        selectedTicket: SelectedTicket,     // 객체 자체 저장
        carNumber 
    });
    router.push('/payments');  // 이동할 페이지의 경로
}


const checkCancellation = () => {
    const cancellationData = store.state.cancellationData;
    if (cancellationData) {
        selectedParking.value = cancellationData.pklt;
        selectedTicket.value = cancellationData.ticket;
        setPkltStatus(cancellationData.pklt.pkltId);
        setPkltTicketInfo(cancellationData.pklt.pkltId);
        setPkltInfo(cancellationData.pklt.pkltId);
    }
};
</script>

<style scoped>
.map-container {
  width: 100%;
  height: calc(100vh - 120px);
  position: fixed;
  top: 120px;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 0;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.index {
  min-width: 24px;
  height: 24px;
  background-color: #4a90e2;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 10px;
  font-size: 0.9em;
}

.search-results {
    position: absolute;
    top: 0px;
    left: 50%;
    transform: translateX(-50%);
    width: 100%;
    height: 100%;
    background-color: white;
    border-radius: 7px;
    overflow-y: auto;
    box-shadow: 2px 2px 2px rgba(0,0,0,0.1);
    z-index: 1000;
    scrollbar-width: none;  
    -ms-overflow-style: none;
    padding-top: 20px;
}

.search-results::-webkit-scrollbar {
    display: none;  
}

.content {
    padding: 6px;
    display: flex;
    flex-direction: column;
}

.location-name {
    display: flex;
    align-items: center;
    font-weight: bolder;
    margin-bottom: 10px;
    justify-content: space-between;
}

.result-item {
    background-color: #f8f8f8;
    border-radius: 10px;
    margin: 15px;
    padding: 10px;
    cursor: pointer;
}

.marker-image {
    width: 25px;
    height: 20px;
    margin-right: 10px;
    margin-bottom: 2px;
}

.road-address {
    font-size: 0.9em;
    font-weight: bold;
    color: #797979;
    margin-bottom: 5px;
    margin-left: 0;
    display: flex;
    align-items: center;
    white-space: nowrap;
}

.road-address p {
    min-width: 54px;
    margin: 0;
    margin-right: 10px;
    margin-top: 2px;
    padding: 2px 8px;
    background-color: #f0f0f0;
    border-radius: 4px;
    font-size: 0.8em;
    color: #666;
    text-align: center;
    border: 1px solid #e0e0e0;
    width: 45px;
    flex-shrink: 0;
    white-space: nowrap;
}

.road-address span {
    flex: 1;
    white-space: normal;
    word-break: break-all;
    margin-left: 10px;
}

.distance {
    font-size: 0.9em;
    color: #666;
    margin-top: 4px;
    margin-left: auto;
}

.parking-detail-overlay {
    position: absolute;
    top: 0px;
    left: 50%;
    transform: translateX(-50%);
    width: 100%;
    height: 100%;
    background-color: white;
    border-radius: 7px;
    overflow-y: auto;
    box-shadow: 2px 2px 2px rgba(0,0,0,0.1);
    z-index: 1000;
    scrollbar-width: none;  
    -ms-overflow-style: none;
    padding-top: 20px;
}


.label {
    font-weight: bold;
    min-width: 70px;
    color: #666;
}

.status {
    padding: 8px 16px;
    border-radius: 4px;
    font-weight: bold;
}

.status.여유 {
    background-color: #e8f5e9;
    color: #2e7d32;
}

.status.보통 {
    background-color: #fff3e0;
    color: #ef6c00;
}

.status.혼잡 {
    background-color: #ffebee;
    color: #c62828;
}

.status-grid {
    display: flex;
    gap: 15px;
    padding: 0 10px;
    border-radius: 12px;
}

.status-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 15px;
    border-radius: 10px;
}

.status-label {
    font-size: 1em;
    color: #666;
}

.status-value {
    font-size: 1.8em;
    font-weight: bold;
}

.total {
    background-color: #E3F2FD;
}

.total .status-value {
    color: #1976D2;
}

.used {
    background-color: #FFF3E0;
}

.used .status-value {
    color: #F57C00;
}

.available {
    background-color: #E8F5E9;
}

.available .status-value {
    color: #2E7D32;
}

.status-title {
    top: 20px;
    margin-bottom: 20px;
    padding: 0 10px;
    font-size: 1.2em;
    color: #2b2b2b;
    font-weight: 600;
}

.parking-info {
    padding: 10px;
    background-color: #fff;
    border-radius: 14px;
    margin-top: 10px;
}

.parking-info .location-name {
    display: flex;
    align-items: center;
    font-weight: 600;
    color: #333;
    font-size: 1.1em;
    margin-bottom: 5px;
}

.marker-image {
    width: 24px;
    height: 24px;
    margin-right: 12px;
    object-fit: contain;
}
.time-image {
    width: 20px;
    height: 20px;
    margin-right: 12px;
    object-fit: contain;
    margin-left: 4px;
}

.parking-name {
    display: flex;
    align-items: center;
    width: 100%;
}

.parking-name p {
    min-width: 54px;
    margin: 0;
    margin-right: 10px;
    padding: 2px 8px;
    background-color: #f0f0f0;
    border-radius: 4px;
    font-size: 0.8em;
    color: #666;
    text-align: center;
    border: 1px solid #e0e0e0;
    flex-shrink: 0;
    box-sizing: border-box;
}

.parking-name span {
    flex: 1;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.parking-ticket-info {
    padding: 20px 10px;
    margin-top: 10px;
}

.ticket-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding-bottom: 80px;
    max-height: 300px;
    overflow-y: auto;
    -ms-overflow-style: none;
    scrollbar-width: none;
}

.ticket-list::-webkit-scrollbar {
    display: none;
}

.ticket-title {
    font-size: 1.2em;
    color: #2b2b2b;
    font-weight: 600;
    margin-bottom: 15px;
}

.ticket-item {
    position: relative;
    cursor: pointer;
    display: block;
}

.ticket-radio {
    position: absolute;
    opacity: 0;
    cursor: pointer;
}

.ticket-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 20px;
    border-radius: 8px;
    background-color: #F3F4F6;
    transition: all 0.2s ease;
    margin-bottom: 5px;
}

.ticket-radio:checked + .ticket-content {
    background-color: #C0C0C0;
}

.ticket-duration {
    font-weight: bold;
    color: #2c2c2c;
}

.ticket-price {
    font-weight: 600;
    color: #2563EB;
}


.parking-name p {
    min-width: 54px;
    margin: 0;
    margin-right: 10px;
    margin-top: 2px;
    padding: 2px 8px;
    background-color: #f0f0f0;
    border-radius: 4px;
    font-size: 0.8em;
    color: #666;
    text-align: center;
    border: 1px solid #e0e0e0;
    width: 45px;
    flex-shrink: 0;
    white-space: nowrap;
}

</style>