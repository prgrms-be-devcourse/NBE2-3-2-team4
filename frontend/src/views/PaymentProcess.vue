<script setup>
import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'
import { loadTossPayments } from "@tosspayments/payment-sdk";

import axios from '@/plugins/axios';

const store = useStore()


const ticket = ref({});
const carNumber = ref('');


const orderId = ref(null);

const setPaymentInfo = async () => {
    ticket.value = store.state.selectedTicket;
    carNumber.value = store.state.carNumber;
    orderId.value = window.location.pathname.split('/').pop();
}

const userInfo = ref({});
const getUserInfo = async () => {
    const response = await axios.get('/api/members/info');
    userInfo.value = response.data;
}

const clientKey = process.env.VUE_APP_TOSS_CLIENT_ID;
let tossPayments = ref(null);

onMounted(async () => {
    await setPaymentInfo();
    await getUserInfo();

    try {
        const orderName = ticket.value.pkDuration + "시간 주차권";
        tossPayments.value = await loadTossPayments(clientKey);
        tossPayments.value.requestPayment('카드', {
            amount: ticket.value.price,
            orderId: orderId.value,
            orderName: orderName,
            customerName: userInfo.value.name,
            successUrl: 'http://localhost:3000/success',
            failUrl: 'http://localhost:3000/success',
        });
    } catch (error) {
        console.error(error);
    }
})

</script>

<style scoped>

</style>
