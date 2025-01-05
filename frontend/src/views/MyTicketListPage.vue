<template>
    <NavigationBar />
    <NoInputBar @back="handleBack" />
    <div class="container">
        <H3>{{ name }}님의 주차권 기록이에요</H3>
        <div class="ticket-list">
            <div v-for="order in orderList" :key="order.orderId" class="ticket-item">
                <div class="ticket-header">
                    <h3 class="car-number"> {{ order.pkltName }} </h3>
                    <span class="status" :class="order.status">{{ order.status }}</span>
                </div>
                <div class="ticket-details">
                    <p>차량 번호</p> {{ order.carNum }} <br>
                    <p>구매 시간</p> {{ order.time }} <br>
                    <p>이용 시간</p> {{ order.duration }}시간 <br>
                    <p>결제 금액</p> {{ order.price }}원 <br>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import NavigationBar from '@/components/NavigationBar.vue';
import NoInputBar from '@/components/NoInputBar.vue';
import axios from '@/plugins/axios';
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
const userInfo = ref({});
const name = ref('');
const orderList = ref([]);
const router = useRouter();
const handleBack = () => {
    router.go(-1);
};

const getUserInfo = async () => {
    const response = await axios.get('/api/members/info');
    userInfo.value = response.data;
    name.value = userInfo.value.name;
}

const getOrderList = async () => {
    const response = await axios.get('/api/orders/order-history');
    if(response.data.data.length > 0){
        const orders = response.data.data.sort((a, b) => b.orderId - a.orderId);
        for (let order of orders) {
            order.pkltName = await GetPkltName(order.pkltId);
        }
        orderList.value = orders;
    }
}

const GetPkltName = async (pkltId) => {
    const response = await axios.get(`/api/pklt/${pkltId}`);
    return response.data.data.pkltName;
}

onMounted(async () => {
    await getUserInfo();
    await getOrderList();
});


</script>

<style scoped>
.container {
    max-width: 800px;
    margin: 0 auto;
    padding: 0 20px;
    transition: all 0.3s ease;
}

.ticket-list {
    margin-top: 20px;
    top: 100px;
    position: relative;
}

.ticket-item {
    border: 1px solid #eaeaea;
    border-radius: 12px;
    padding: 20px;
    margin-bottom: 20px;
    background-color: #fff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.ticket-item:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
}

.ticket-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    padding-bottom: 10px;
    border-bottom: 1px solid #f0f0f0;
}

.car-number {
    font-weight: 600;
    font-size: 1.1em;
    color: #000000;
}

.status {
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 0.9em;
    font-weight: 500;
    text-align: center;
    min-width: 80px;
}

.status.환불 {
    background-color: #fff2f0;
    color: #ff4d4f;
}

.status{
    background-color: #e6f7ff;
    color: #1890ff;
}

.status.주차중{
    background-color: #e6ffee;
    color: #28db73;
}

.ticket-details {
    
    font-weight: 600;
    padding: 0 0;
}

.ticket-details p {
    display: inline-block;
    margin: 8px 0;
    color: #595959;
    font-size: 0.95em;
    line-height: 1.5;
    margin-right: 10px;
}

@media (max-width: 600px) {
    .container {
        padding: 0 10px;
    }

    .ticket-item {
        padding: 15px;
    }

    .car-number, .status {
        font-size: 1em;
    }

    .ticket-details p {
        font-size: 0.9em;
    }
}

p {
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
</style>
