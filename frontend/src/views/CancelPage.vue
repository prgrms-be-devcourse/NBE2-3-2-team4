<template>
  <NavigationBar />
  <NoInputBar @back="handleBack" />
  <div v-if="isLoading" class="loading-message">로딩 중입니다...</div>
  <div v-else class="content">
    <div class="map-container">
      <KakaoMap 
        :lat="pklt.latitude" 
        :lng="pklt.longitude" 
        :draggable="false"
        :disable-double-click-zoom="true"
        :scrollwheel="false"
        :width="'90%'"
        :height="200"
        id="map"
        @onLoadKakaoMap="onLoadKakaoMap"
        style="position: fixed; border-radius: 10px; margin-left: 5px;"
      >
        <KakaoMapMarker 
          :lat="pklt.latitude" 
          :lng="pklt.longitude" 
        />
      </KakaoMap> 
    </div>

    <div v-if="checkValue === '출차시간이 지났는데 아직 안들어감'">
      <div class="parking-info">
        <span>
          <h3 class="parking-name" ><img src="@/assets/dark-car-icon.png" style="margin-right: 10px;" alt="parking" class="car-icon">{{ pklt.pkltName }}</h3>
        </span>
        <div class="parking-address" style="font-size: 16px; color: #858585; font-weight: bold; text-align: left; margin-top: 5px;margin-left: 40px;">{{ pklt.address }}</div>
      </div>
      
      <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
        <span style="display: flex; align-items: center;">
          <p>차량 번호</p>
          {{ parkingOrder.carNum }}
        </span> 
      </div>
      <div class="ticket-info">
        <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
          <span style="display: flex; align-items: center;">
            <p>구매 시간</p>
            {{ parkingOrder.duration }}시간
          </span>
        </div>
        <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
          <span style="display: flex; align-items: center;">
            <p style="width: 110px;">입차 예정 시간</p>
            {{ formatDateTime(startTime) }}
          </span>
        </div>
        <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
          <span style="display: flex; align-items: center;">
            <p style="width: 110px;">출차 예정 시간</p>
            {{ formatDateTime(endTime) }}
          </span>
        </div>
        <div style="padding:10px; font-size:16px; color:#797979; font-weight:bold; text-align:center;">
          출차 시간이 지났는데 주차하지 않으셨네요
        </div>
        <div style="padding:10px; font-size:16px; color:#797979; font-weight:bold; text-align:center; margin-top: -5px;">
          초과 시간 만큼 추가 요금이 청구되요
        </div>
        <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
          <span style="display: flex; align-items: center;">
            <p>초과 시간</p>
            {{ overTime }}
          </span>
        </div>
        <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
          <span style="display: flex; align-items: center;">
            <p>추가 요금</p>
            ₩ {{ addPrkCrg }} / {{ addPrkHr }}분
          </span>
        </div>
      </div>
    </div>
    <div v-if="checkValue === '출차시간이 지나지 않고 대기중'">
      <div class="parking-info">
        <span>
          <h3 class="parking-name" ><img src="@/assets/dark-car-icon.png" style="margin-right: 10px;" alt="parking" class="car-icon">{{ pklt.pkltName }}</h3>
        </span>
        <div class="parking-address" style="font-size: 16px; color: #858585; font-weight: bold; text-align: left; margin-top: 5px;margin-left: 40px;">{{ pklt.address }}</div>
      </div>
      
      <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
        <span style="display: flex; align-items: center;">
          <p>차량 번호</p>
          {{ parkingOrder.carNum }}
        </span> 
      </div>
      <div class="ticket-info">
        <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
          <span style="display: flex; align-items: center;">
            <p>구매 시간</p>
            {{ parkingOrder.duration }}시간
          </span>
        </div>
        <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
          <span style="display: flex; align-items: center;">
            <p style="width: 110px;">입차 예정 시간</p>
            {{ formatDateTime(startTime) }}
          </span>
        </div>
        <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
          <span style="display: flex; align-items: center;">
            <p style="width: 110px;">출차 예정 시간</p>
            {{ formatDateTime(endTime) }}
          </span>
        </div>
        <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
          <span style="display: flex; align-items: center;">
            <p style="width: 80px;">결제 시간</p>
            {{ formatDateTime(timeDate) }}
          </span>
        </div>
        <div style="padding:15px; font-size:16px; color:#797979; font-weight:bold; text-align:center;">
          환불 처리를 도와드릴게요
        </div>
        <div v-if="t10check" style="padding:10px; font-size:16px; color:#797979; font-weight:bold; text-align:center; margin-top: -5px;">
            구매 후 10분이 지나지 않아 전액 환불 가능해요
        </div>
        <div v-else style="padding:10px; font-size:16px; color:#797979; font-weight:bold; text-align:center; margin-top: -5px;">
            구매 후 10분이 지나 50% 환불 가능해요
        </div>
      </div>
    </div>
  </div>
  <div class="pay-footer" v-if="checkValue === '출차시간이 지났는데 아직 안들어감'">
      <div class="price">
        <span style="display: flex; justify-content: space-between; align-items: center;">
          <p style="font-size:20px; font-weight:bolder; display:inline;">총 결제 금액</p>
          <p style="font-size:20px; font-weight:bolder; display:inline; text-align: right;">{{ calculateOverTimeFee() }}원</p>
        </span>
      </div>
      <div class="pay-button">
        <button @click="handlePayment">위의 내용을 확인했습니다</button>
      </div>
    </div>
  <div class="pay-footer" v-if="checkValue === '출차시간이 지나지 않고 대기중'">    
    <div class="price">
        <span style="display: flex; justify-content: space-between; align-items: center;">
          <p style="font-size:20px; font-weight:bolder; display:inline;">환불 금액</p>
          <p style="font-size:20px; font-weight:bolder; display:inline; text-align: right;">{{ totalPrice }}원</p>
        </span>
      </div>
      <div class="pay-button">
        <button @click="handlePayment">위의 내용을 확인했습니다</button>
      </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { onMounted, ref } from 'vue';
import { useStore } from 'vuex';
import NavigationBar from '@/components/NavigationBar.vue';
import NoInputBar from '@/components/NoInputBar.vue';
import {KakaoMap, KakaoMapMarker} from 'vue3-kakao-maps';
import axios from '@/plugins/axios';

const router = useRouter();
const parkingOrder = ref(null);
const pklt = ref(null);
const store = useStore();
const isLoading = ref(true);
const pkltInfo = ref(null);

const handlePayment = async () => {
    if(checkValue.value === '출차시간이 지나지 않고 대기중') {
        if(totalPrice.value === 0) {
            router.push('/success?code=전액환불&orderId=' + parkingOrder.value.orderId + '&price=' + totalPrice.value);
        } else {
            router.push('/success?code=50%환불&orderId=' + parkingOrder.value.orderId + '&price=' + totalPrice.value);
        }
    } else {
        router.push('/success?code=환불&orderId=' + parkingOrder.value.orderId + '&price=' + calculateOverTimeFee());
    }
};

const getState = () => {
    parkingOrder.value = store.state.selectedParkingOrder;
    pklt.value = store.state.selectedParkingLot;
    console.log(parkingOrder.value);
    console.log(pklt.value);
    isLoading.value = false;
}
const formatDateTime = (date) => {
  if (!date) return '';
  return date.toLocaleString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    hour12: true
  });
};
const addPrkCrg = ref(null);
const addPrkHr = ref(null);

const getPkltInfo = async () => {
    const response = await axios.get(`/api/pklt/${pklt.value.pkltId}/info`);
    pkltInfo.value = response.data.data;
    addPrkCrg.value = pkltInfo.value.addPrkCrg;
    addPrkHr.value = pkltInfo.value.addPrkHr;
    console.log(pkltInfo.value);
}

const timeDate = ref(null);
const startTime = ref(null);
const endTime = ref(null);

const getTime = () => {
    const timeStr = parkingOrder.value.time; // "2024년 12월 31일 오후 10시 37분"
    const matches = timeStr.match(/(\d+)년\s*(\d+)월\s*(\d+)일\s*(오전|오후)\s*(\d+)시\s*(\d+)분/);
    
    if (matches) {      
        const [_, year, month, day, ampm, hour, minute] = matches;
        let adjustedHour = parseInt(hour);
        if (ampm === '오후' && adjustedHour !== 12) {
            adjustedHour += 12;
        }
        if (ampm === '오전' && adjustedHour === 12) {
            adjustedHour = 0;
        }
        const date = new Date(
          parseInt(year),
          parseInt(month) - 1, // 월은 0부터 시작
          parseInt(day),
          adjustedHour,
          parseInt(minute)
        );
        timeDate.value = date;
        startTime.value = getStartTime(timeDate.value);
        endTime.value = getEndTime(timeDate.value, parkingOrder.value.duration);
        console.log(_);
    }
}

const getStartTime = (timeDate) => {
  return new Date(timeDate.getTime() + 10 * 60 * 1000);
}

const getEndTime = (timeDate, pkDuration) => {
  return new Date(timeDate.getTime() + pkDuration * 60 * 60 * 1000 + 10 * 60 * 1000);
}

const checkNotParking = () => {
    const currentDateTime = new Date();
    
    // 현재 시간이 종료 시간을 지났는지 확인
    if (endTime.value && currentDateTime > endTime.value) {
        // 주차 상태가 '대기 중'인 경우
        if (parkingOrder.value.status === '주차 대기') {
            return '출차시간이 지났는데 아직 안들어감';
        } else if (parkingOrder.value.status === '주차중') {
            return '출차시간이 지나고 주차중';
        }
    }
     else {
        if(parkingOrder.value.status === '주차 대기') {
            return '출차시간이 지나지 않고 대기중';
        } else if (parkingOrder.value.status === '주차중') {
            return '출차시간이 지나지 않고 주차중';
        }
    }
}
const totalPrice = ref(null);
const t10check = ref(null);
const check10Minutes = () => {
    const currentDateTime = new Date();
    const timeDiff = currentDateTime - timeDate.value;
    console.log('시간 차이(ms):', timeDiff);
    
    if(timeDiff <= 10 * 60 * 1000) {
        t10check.value = true;
        totalPrice.value = parkingOrder.value.price;
    } else {
        t10check.value = false;
        totalPrice.value = Math.floor(parkingOrder.value.price * 0.5);
    }
    console.log('10분 이내:', t10check.value);
    console.log('환불 금액:', totalPrice.value);
}
const checkValue = ref(null);

const checkcheck = () => {
    switch(checkNotParking()) {
        case '출차시간이 지나고 주차중':
            alert('주차장에서 결제를 진행해주세요.');
            router.go(-1);
            break;
        case '출차시간이 지났는데 아직 안들어감':
            checkValue.value = '출차시간이 지났는데 아직 안들어감';
            break;
        case '출차시간이 지나지 않고 대기중':
            checkValue.value = '출차시간이 지나지 않고 대기중';
            break;
        case '출차시간이 지나지 않고 주차중':
            alert('주차장에서 결제를 진행해주세요.');
            router.go(-1);
            break;
    }
    check10Minutes();
}
const handleBack = () => {
    router.go(-1);
}

const overTime = ref(null);
const calcOverTime = () => {
    const currentTime = new Date();
    const overTimeMs = currentTime - endTime.value;
    
    // 밀리초를 분으로 변환
    const totalMinutes = Math.floor(overTimeMs / (1000 * 60));
    
    // 일, 시간, 분 계산
    const days = Math.floor(totalMinutes / (24 * 60));
    const hours = Math.floor((totalMinutes % (24 * 60)) / 60);
    const minutes = totalMinutes % 60;
    
    // 결과 문자열 생성
    let result = '';
    if (days > 0) result += `${days}일 `;
    if (hours > 0) result += `${hours}시간 `;
    if (minutes > 0) result += `${minutes}분`;
    
    overTime.value = result.trim();
    console.log(overTime.value);
};

const calculateOverTimeFee = () => {
    const currentTime = new Date();
    const overTimeMs = currentTime - endTime.value;
    const totalMinutes = Math.floor(overTimeMs / (1000 * 60));
    const additionalUnits = Math.ceil(totalMinutes / addPrkHr.value);
    const fee = additionalUnits * addPrkCrg.value;
    return fee.toLocaleString();
};

onMounted(async () => {
    getState();
    await getPkltInfo();
    getTime();
    checkcheck();
    calcOverTime();
});
</script>

<style scoped>  

.overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.confirmation-dialog {
    border: none;
    padding: 30px;
    background-color: #f8f9fa;
    border-radius: 11px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    width: 85%;
}

.confirmation-dialog p {
    margin-bottom: 20px;
    font-size: 18px;
    font-weight: bold;
    color: #343a40;
    text-align: center;
}

.button-container {
    display: flex;
    justify-content: center;
    margin-top: 10px;
}

.confirmation-dialog button {
    margin: 0 5px;
    padding: 12px 20px;
    border: none;
    border-radius: 10px;
    background-color: #007bff;
    color: white;
    cursor: pointer;
    font-weight: bold;
    font-size: 13px;
    transition: background-color 0.3s, transform 0.2s;
}

.confirmation-dialog button:hover {
    background-color: #0056b3;
    transform: scale(1.05);
}
.loading-message {
    text-align: center;
    font-size: 18px;
    color: #007bff; /* 로딩 메시지 색상 */
    margin-top: 20px;
}

.content {
    padding: 15px; /* 내용 패딩 */
    position: absolute;
    top: 13%;
    left: 50%;
    transform: translateX(-50%);
    width: 100%;
    height: 100%;
}
.parking-info {
    position: relative;
    justify-content: space-between;
    align-items: center;
    padding: 15px;
    background-color: #f9f9f9;
    border-radius: 10px;
    z-index: 1000;
    margin-top: 220px;
    margin-bottom: 15px;
}
/* 전체 페이지 스크롤바 제거 */
::-webkit-scrollbar {
  display: none;
}

/* IE, Edge, Firefox 브라우저용 스크롤바 제거 */
* {
  -ms-overflow-style: none;  /* IE, Edge */
  scrollbar-width: none;     /* Firefox */
}
.map-container {
    position: fixed;
    z-index: 1000;
}

.parking-name {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    font-size: 19px;
    color: #333;
    margin-left: 10px;
    font-weight: bold; /* 주차장 이름 강조 */
}

.car-number {
    font-size: 18px;
    color: #797979;
    margin-right: 15px;
    font-weight: bold; /* 차량 번호 강조 */
}
.car-icon {
    width: 20px; /* 아이콘 너비 */
    height: 17px; /* 아이콘 높이 */
    margin-right: 5px; /* 오른쪽 여백 추가 */
}

/* 이전에 작성한 ticket-info 스타일 유지 */
.ticket-info {
    border-radius: 10px;

}

.ticket-info h3 {
    font-size: 24px;
    color: #252525;
    margin-bottom: 8px;
    text-align: center;
}

.ticket-details {
    display: flex;
    justify-content: space-between;
    margin-top: 10px;
}

.ticket-details span {
    font-weight: bold;
    color: #333;
    font-size: 16px;
}

/* 추가된 스타일 */
.parking-name.highlight {
    color: #007bff; /* 주차장 이름 색상 변경 */
}

.car-number.highlight {
    color: #ff5722; /* 차량 번호 색상 변경 */
}
.current-time {
    display: flex;
    justify-content: center;
    padding: 5px;
    font-size: 16.5px;
    color: #3d3d3d;
    font-weight: 600;
    align-items: center;
    justify-content: center;
}
.clock-icon {
    margin-right: 10px; /* 오른쪽 여백 추가 */
}

.current-time p {
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
    width: 80px;
    flex-shrink: 0;
    white-space: nowrap;
}
.pay-footer {
    position: fixed;
    bottom: 0;
    width: 100%;
    padding: 25px;
    background-color: #fff;
    border-top: 1px solid #e0e0e0;
}

.input-container {
    position: relative;
    width: 100%;
    margin-bottom: 12px;
}

.pay-footer input {
    width: 100%;
    padding: 16px 16px 16px 56px;
    border-radius: 10px;
    font-size: 16px;
    font-weight: bold;
    background-color: #dbdbdb;
    border: none;
}

.pay-footer input::placeholder {
    text-align: center;
    color: #fff;
}
.pay-footer input:focus {
    outline: none;
    color: #202020;
    font-weight: bold;
}
.pay-button {
    margin-top: 20px;
    display: flex;
    gap: 8px;
    padding: 0;
}

.pay-button button {
    width: 100%;
    padding: 15px;
    border: none;
    border-radius: 15px;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    background-color: #2563EB;
    color: white;
}
</style>