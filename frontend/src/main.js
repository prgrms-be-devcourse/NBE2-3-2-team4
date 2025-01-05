import { createApp } from 'vue';
import App from './App.vue';
import { useKakao } from 'vue3-kakao-maps/@utils';
import router from './router';
useKakao(process.env.VUE_APP_KAKAO_MAP_API_KEY, ['services']); 
import { createStore } from 'vuex';


const store = createStore({
    state: {
        ticketId: null,
        carNumber: null,
        cancellationData: null,
        selectedParkingOrder: null,
        selectedParkingLot: null
    },
    mutations: {
        setCancellationData(state, data) {
            state.cancellationData = data;
        },
        SET_PAYMENT_INFO(state, { selectedParking, selectedTicket, carNumber }) {
            state.selectedParking = selectedParking;
            state.selectedTicket = selectedTicket;
            state.carNumber = carNumber;
        },
        SET_PARKING_INFO(state, { parkingOrder, parkingLot }) {
            state.selectedParkingOrder = parkingOrder;
            state.selectedParkingLot = parkingLot;
        },  
        SET_PAYMENT_STATUS(state, paymentStatus) {
            state.paymentStatus = paymentStatus;
        },
        SET_PAYMENT_DATA(state, paymentData) {
            state.paymentData = paymentData;
        }
    },
    actions: {
        setPaymentInfo({ commit }, { selectedParking, selectedTicket, carNumber }) {
            commit('SET_PAYMENT_INFO', { selectedParking, selectedTicket, carNumber });
        },
        setParkingInfo({ commit }, { parkingOrder, parkingLot }) {
            commit('SET_PARKING_INFO', { parkingOrder, parkingLot });
        },
        setPaymentStatus({ commit }, paymentStatus) {
            commit('SET_PAYMENT_STATUS', paymentStatus);
        },
        setPaymentData({ commit }, paymentData) {
            commit('SET_PAYMENT_DATA', paymentData);
        }
    }
});

const app = createApp(App);
app.use(router);
app.use(store);
app.mount('#app'); 