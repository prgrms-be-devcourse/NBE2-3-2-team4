<template>
  <NavigationBar/>
  <NoInputBar @back="handleBack"  />

  <div class="ticket-container" v-if="isLogin">
    <div v-if="existsParkingOrder" class="map-container">
      <div class="content-wrapper">
        <KakaoMap v-if="pklt"
                  :lat="latitude" 
                  :lng="longitude" 
                  :draggable="false"
                  :disable-double-click-zoom="true"
                  :scrollwheel="false"
                  :width="'100%'"
                  :height="300"
                  id="map"
                  style="border-radius: 12px;"
                  @onLoadKakaoMap="onLoadKakaoMap"
                  >
                  <KakaoMapMarker 
                      :lat="latitude" 
                      :lng="longitude" 
                  />
        </KakaoMap> 
        <div class="info-section">
          <div class="parking-title" v-if="existsParkingOrder.status === '주차중'">{{ userInfo.data.name }}님은 주차중이시네요</div>
          <div class="parking-title" v-if="existsParkingOrder.status === '주차 대기'">{{ userInfo.data.name }}님을 기다리고 있어요</div>
          <div class="parking-info">
            <div class="info-item">
              <div class="location-wrapper">
                <div class="parking-name">
                  <img src="@/assets/dark-car-icon.png" class="car-icon" alt="parking">
                  {{ pklt.pkltName }}
                </div>
                <div class="address">
                  <i class="fas fa-map-marker-alt"></i>
                  <span class="address-text">{{ pklt.address }}</span>
                </div>
              </div>
            </div>

            <div class="info-item">
              <div class="current-time">
                <span class="time-row">
                    <p>차량 번호</p> {{ existsParkingOrder.carNum }}
                </span>
              </div>
            </div>
            
            <div class="info-item">
              <div class="current-time">
                <span v-if="existsParkingOrder.status === '주차 대기'" class="time-row">
                    <p>시작 시간</p> {{ formatDateTime(startTime) }}
                </span>
                <span v-if="existsParkingOrder.status === '주차중'" class="time-row">
                    <p>입차 시간</p> {{ startParkingTime }}
                </span>
              </div>
            </div>
            <div class="info-item">
              <div class="current-time">
                <span v-if="existsParkingOrder.status === '주차 대기'" class="time-row">
                    <p>종료 시간</p> {{ formatDateTime(endTime) }}
                </span>
                <span v-if="existsParkingOrder.status === '주차중'" class="time-row">
                    <p>종료 시간</p> {{ formatDateTime(endTime) }}
                </span>
              </div>
            </div>
          </div>
          <div class="notice">종료 시간 부터 추가금이 적용됩니다</div>
          <button class="button-style" style="margin-top: 20px; display: block; margin-left: auto; margin-right: auto;" @click="cancelParking">취소하기</button>
        </div>
      </div>
    </div>
  
    <div class="not-ticket-container" v-else>
      <div class="message">안녕하세요, {{ userInfo.data.name }}님<br><br>주차권이 필요하신가요?</div>
      <button style="margin-top: 20px;" @click="$router.push('/search')">주차권 구매하기</button>
    </div>
  </div>
  <div class="not-login-container" v-else>
    <div class="message">로그인 후 이용할 수 있어요</div>
    <button @click="$router.push('/login')">로그인하기</button>
  </div>

  <div v-if="isLogin" class="message-container">
    <div class="message-content">
      <h3>회원님의 주차권을 확인할 수 있어요</h3>
      <button class="check-button" @click="$router.push('/myticket/list')">목록 확인하기 ></button>
    </div>
  </div>
</template>

<script setup>
import NoInputBar from '@/components/NoInputBar.vue';
import NavigationBar from '@/components/NavigationBar.vue';
import axios from '@/plugins/axios';
import { useRouter } from 'vue-router';
import { ref,onMounted, nextTick } from 'vue';
import { KakaoMap, KakaoMapMarker } from 'vue3-kakao-maps';
import { useStore } from 'vuex';

const router = useRouter();
const userInfo = ref({});

const onLoadKakaoMap = (mapRef) => {
  map.value = mapRef;
};

const handleBack = () => {
  router.go(-1);
};
const existsParkingOrder = ref(null);

const timeDate = ref(null);
const startTime = ref(null);
const endTime = ref(null);

const formatDateTime = (date) => {
  if (!date) return '';
  
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hour = date.getHours();
  const minute = date.getMinutes();
  
  const ampm = hour < 12 ? '오전' : '오후';
  const displayHour = hour % 12 || 12; 
  
  return `${year}년 ${month}월 ${day}일 ${ampm} ${displayHour}시 ${minute}분`;
};

const getOrderHistory = async () => {
  const response = await axios.get('/api/orders/order-history');
  for(let i = 0; i < response.data.data.length; i++) {
    if(response.data.data[i].status === "주차 대기" || response.data.data[i].status === "주차중") {
      existsParkingOrder.value = response.data.data[i];
      console.log(existsParkingOrder.value);
      // 시간 문자열을 Date 객체로 변환
      const timeStr = existsParkingOrder.value.time; // "2024년 12월 31일 오후 10시 37분"
      const matches = timeStr.match(/(\d+)년\s*(\d+)월\s*(\d+)일\s*(오전|오후)\s*(\d+)시\s*(\d+)분/);
      
      if (matches) {
        const [_, year, month, day, ampm, hour, minute] = matches;
        let adjustedHour = parseInt(hour);
        
        // 오후인 경우 12를 더함 (오후 3시 -> 15시)
        if (ampm === '오후' && adjustedHour !== 12) {
          adjustedHour += 12;
        }
        // 오전 12시는 0시로 변환
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
        
        console.log('변환된 시간:', date);
        timeDate.value = date;
        startTime.value = getStartTime(timeDate.value);
        endTime.value = getEndTime(timeDate.value, existsParkingOrder.value.duration);
            console.log(_);
            console.log(startTime.value);
            console.log(endTime.value);
      }
      
      return true;  
    }
  }
  return false;
};

const getStartTime = (timeDate) => {
  return new Date(timeDate.getTime() + 10 * 60 * 1000);
}

const getEndTime = (timeDate, pkDuration) => {
  return new Date(timeDate.getTime() + pkDuration * 60 * 60 * 1000 + 10 * 60 * 1000);
}

const isLogin = ref(false);
const getUserInfo = async () => {
  const response = await axios.get('/api/members/info');
  if(response) {
    isLogin.value = true;
    userInfo.value = response.data;
    return true;
  }
};

const latitude = ref(0);
const longitude = ref(0);
const pklt = ref({});
const getParkingInfo = async () => {
  const response = await axios.get(`/api/pklt/${existsParkingOrder.value.pkltId}`);
  if(response) {
    pklt.value = response.data.data;
    latitude.value = pklt.value.latitude;
    longitude.value = pklt.value.longitude;
  }
};

// 메시지 컨테이너 위치 조정을 위한 ref
const messageContainerTop = ref('120px');

// 티켓 컨테이너 높이에 따라 메시지 컨테이너 위치 조정
const updateMessagePosition = async () => {
  await nextTick();
  const ticketContainer = document.querySelector('.ticket-container');
  if (ticketContainer) {
    const ticketHeight = ticketContainer.offsetHeight;
    const topPosition = ticketContainer.getBoundingClientRect().top;
    messageContainerTop.value = `${topPosition + ticketHeight + 20}px`;
  }
};

const store = useStore();

const cancelParking = async () => {
  store.dispatch('setParkingInfo', {
    parkingOrder: existsParkingOrder.value,
    parkingLot: pklt.value
  });
  router.push('/myticket/cancel');
};

const startParkingTime = ref(null);
const getStartParkingTime = async () => {
  const response = await axios.get(`/api/orders/${existsParkingOrder.value.orderId}`);
  if(response) {
    startParkingTime.value = response.data.data.startTime;
  }
}
onMounted(async () => {
  if(await getUserInfo()) {
    if(await getOrderHistory()) {
      await getParkingInfo();
      // existsParkingOrder가 설정된 후에 상태 확인
      if(existsParkingOrder.value && existsParkingOrder.value.status === '주차중') {
        await getStartParkingTime();
      }
    }
  }
  updateMessagePosition();
});
</script>

<style scoped>
.ticket-container {
  position: relative;
  width: 95%;
  border-radius: 12px;
  padding: 20px;
  margin: 140px auto 20px;
  background-color: #ffffff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.content-wrapper {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.info-section {
  padding: 0 10px;
}

.parking-title {
  font-size: 19px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
  color: #000;
}
.current-time {
    display: flex;
    justify-content: center;
    padding: 5px;
    font-size: 16.5px;
    color: #000;
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
.parking-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.parking-name {
  font-size: 18px;
  font-weight: 600;
  color: #333
}

.parking-number {
  font-size: 16px;
  color: #666;
  display: flex;
}

.address, .time, .price {
  display: flex;
  align-items: center;
  font-weight: bold;
  gap: 8px;
  color: #000;
  font-size: 16px;
}
.address {
  margin-left: 2px;
}
.notice {
  margin-top: 20px;
  color: #666;
  font-size: 14px;
  text-align: center;
  font-weight: 600;
}

i {
  color: #16A34A;
  width: 20px;
}

/* 구분선 추가 */

.time-row {
    display: flex;
    align-items: center;
    gap: 10px;
}

.time-row p {
    margin: 0;
}

.location-wrapper {
  display: flex;
  flex-direction: column;
  background-color: #E5E7EB;
  border-radius: 15px;
  width: 100%;
  padding: 20px;
  justify-content: center;
}

.parking-name {
  font-size: 18px;
  font-weight: bolder;
  color: #000;
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.car-icon {
  width: 20px;
  height: 18px;
  margin-right: 10px;
}

.address {
  margin-left: 4px;
  color: #666;
  font-size: 14px;
  display: flex;
  align-items: center;
}   

.address-text {
  word-break: keep-all;
  line-height: 1.4;
}

.not-login-container {
  position: relative;
  width: 95%;
  border-radius: 12px;
  padding: 70px;
  margin: 80px auto 20px;
  background-color: #ffffff;
  text-align: center;
}
.not-ticket-container {
  position: relative;
  width: 95%;
  border-radius: 12px;
  padding: 70px;
  margin: 80px auto 20px;
  background-color: #ffffff;
  text-align: center;
}
.not-login-container .message {
  font-size: 19px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #000;
}

.not-login-container button {
  padding: 12px 24px;
  background-color: #16A34A;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
}

.not-login-container button:hover {
  background-color: #15803d;
}

.not-ticket-container .message {
  font-size: 19px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #000;
}

.not-ticket-container button {
  padding: 12px 24px;
  background-color: #16A34A;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.not-ticket-container button:hover {
  background-color: #15803d;
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

.message-container {
  position: relative;
  width: 95%;
  margin: 0 auto;
  text-align: left;
  background-color: #ffffff;
}

.message-container h3 {
  margin: 0;
  font-size: 16px;
  color: #000;
  font-weight: 600;
}

.button-style {
  padding: 8px 18px;
  background-color: #4169e1;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s ease;
}


.message-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.check-button {
  padding: 6px 14px;
  color: #666;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  background-color: #fff;
}
</style>