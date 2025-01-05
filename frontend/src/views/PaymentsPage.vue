<template>
    <div>
        <NavigationBar />
        <NoInputBar @back="showConfirmation" />
        <div v-if="isConfirmationVisible" class="overlay">
            <div class="confirmation-dialog">
                <p>결제를 취소하시겠습니까?</p>
                <div class="button-container">
                    <button @click="confirmCancellation">취소해주세요</button>
                    <button @click="cancelCancellation">계속할래요</button>
                </div>
            </div>
        </div>
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
            <div class="parking-info">
                <span>
                <h3 class="parking-name" ><img src="@/assets/dark-car-icon.png" style="margin-right: 10px;" alt="parking" class="car-icon">{{ pklt.pkltNm }}</h3></span>
                <div class="parking-address" style="font-size: 16px; color: #858585; font-weight: bold; text-align: left; margin-top: 5px;margin-left: 40px;">{{ addr }}</div>
            </div>
            
            <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
                <span style="display: flex; align-items: center;">
                    <p>차량 번호</p>
                    {{ carNumber }}
                </span>
            </div>
            <div class="ticket-info" v-if="ticket">
                <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
                <span style="display: flex; align-items: center;">
                    <p>구매 시간</p>
                    {{ ticket.pkDuration }}시간
                </span>
            </div>
            <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
                <span style="display: flex; align-items: center;">
                    <p>입차 시간</p>
                    {{ currentTime }}
                </span>
            </div>
            <div style="padding:10px; font-size:16px; color:#797979; font-weight:bold; text-align:center;">
                주차권 구매 후 10분의 유예시간이 제공됩니다
            </div>
            <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
                <span style="display: flex; align-items: center;">
                    <p>출차 시간</p>
                    {{ addTime }}
                </span>
            </div>
            <div style="padding:10px; font-size:16px; color:#797979; font-weight:bold; text-align:center;">
                출차 시간 이후부터 추가 요금이 발생합니다
            </div>
            <div class="current-time" style="display: flex; align-items: center; justify-content: space-between;">
                <span style="display: flex; align-items: center;">
                    <p>추가 요금</p>
                    ₩ {{ addPrkCrg }} / {{ addPrkHrs }}분
                </span>
            </div>
            </div>
        </div>
    </div>
    <div class="pay-footer">
        <div class="price">
         <span style="display: flex; justify-content: space-between; align-items: center;">
             <p style="font-size:20px; font-weight:bolder; display:inline;">총 결제 금액</p>
          <p style="font-size:20px; font-weight:bolder; display:inline; text-align: right;">{{ ticket.price }}원</p>
         </span>
        </div>
        <div class="pay-button">
            <button @click="handlePayment()">위의 내용을 확인했습니다</button>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useStore } from 'vuex';
import NavigationBar from '@/components/NavigationBar.vue';
import NoInputBar from '../components/NoInputBar.vue';
import { useRouter } from 'vue-router';
import { KakaoMap, KakaoMapMarker } from 'vue3-kakao-maps';
import axios from '../plugins/axios';

const store = useStore();
const ticket = ref({});
const pklt = ref({});
const carNumber = ref('');
const router = useRouter();
const isConfirmationVisible = ref(false);
const isLoading = ref(true);
const map = ref(null);
const currentTime = ref(new Date(new Date().getTime() + 10 * 60000).toLocaleString('ko-KR', { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit', hour12: true }));
const addTime = ref(null);

const onLoadKakaoMap = (mapRef) => {
  map.value = mapRef;
};


onMounted(async () => {
    await getUserInfo();
    await loadData();
    await getPkltInfo();
    await getPklt();
});



const loadData = async () => {
    ticket.value = store.state.selectedTicket || {};
    pklt.value = store.state.selectedParking || {};
    carNumber.value = store.state.carNumber || '';
    isLoading.value = false;
    addTime.value = ref(new Date(new Date().getTime() + (ticket.value.pkDuration * 60 * 60 * 1000) + 10*60*1000).toLocaleString('ko-KR', { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit', hour12: true }));
};

const userInfo = ref({});
const getUserInfo = async () => {
    const response = await axios.get('/api/members/info');
    if(response) {
        userInfo.value = response.data;   
    } else {
        alert('로그인 후 결제가 가능합니다.');
        router.push('/login');
    }
};

const showConfirmation = () => {
    isConfirmationVisible.value = true;
};

const addr = ref('');
const getPklt = async () => {
    const response = await axios.get(`/api/pklt/${pklt.value.pkltId}`);
    addr.value = response.data.data.address;
};

const confirmCancellation = () => {
    const data = { ticket, pklt, carNumber };
    store.commit('setCancellationData', data);
    router.push('/search');
};

const cancelCancellation = () => {
    isConfirmationVisible.value = false;
};

const pkltInfo = ref({});
const addPrkCrg = ref(0);
const addPrkHrs = ref(0);

const getPkltInfo = async () => {
    const response = await axios.get(`/api/pklt/${pklt.value.pkltId}/info`);
    pkltInfo.value = response.data;
    addPrkCrg.value = pkltInfo.value.data.addPrkCrg;
    addPrkHrs.value = pkltInfo.value.data.addPrkHr;
};

const randomId = new Date().getTime() + Math.random();
const paymentId = btoa(randomId.toString());

const handlePayment = async () => {
    const response = await axios.post('/api/orders/tickets', {
        ticketId: ticket.value.ticketId,
        carNumber: carNumber.value,
        paymentId: paymentId,
    });
    if(!response) {
        alert('이미 주차중인 차량입니다.');
        return;
    } else {
        router.push(`/payments/process/${paymentId}`);
    }
};

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

