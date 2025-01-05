<template>
    <NavigationBar />

    <div v-if="isSuccess" class="container">
        <div class="success-icon">
            <svg viewBox="0 0 52 52">
                <circle class="circle" cx="26" cy="26" r="25" fill="none"/>
                <path class="check" d="M14.1 27.2l7.1 7.2 16.7-16.8"/>
            </svg>
        </div>
        <h1 style="margin-bottom: 20px;">결제 성공</h1>
        <button @click="goToMyTicket">구매한 주차권 확인하기</button>
        <button @click="goToMain">메인으로 돌아가기</button>   

    </div>
    <div v-if="isCanceled" class="container">
        <div class="success-icon">
            <svg viewBox="0 0 52 52">
                <circle class="circle" cx="26" cy="26" r="25" fill="none"/>
                <path class="check" d="M14.1 27.2l7.1 7.2 16.7-16.8"/>
            </svg>
        </div>
        <h1 style="margin-bottom: 20px;">취소되었습니다</h1>
        <h3 v-if="price">{{ price }}원이 환불되었습니다.</h3>
        <button @click="goToMain">메인으로 돌아가기</button>   
    </div>
    <div v-if="isPayCancel" class="container">
        <div class="success-icon">
            <svg viewBox="0 0 52 52">
                <circle class="circle" cx="26" cy="26" r="25" fill="none"/>
                <path class="check" d="M14.1 27.2l7.1 7.2 16.7-16.8"/>
            </svg>
        </div>
        <h1 style="margin-bottom: 20px;">취소되었습니다</h1>
        <h3 v-if="price">{{ price }}원이 결제되었습니다.</h3>
        <button @click="goToMain">메인으로 돌아가기</button>   
    </div>
    <div v-if="isCancel" class="container">
        <div class="success-icon">
            <svg viewBox="0 0 52 52">
                <circle class="circle" cx="26" cy="26" r="25" fill="none"/>
                <path class="check" d="M14.1 27.2l7.1 7.2 16.7-16.8"/>
            </svg>
        </div>
        <h1 style="margin-bottom: 20px;">취소되었습니다</h1>
        <button @click="goToMain">메인으로 돌아가기</button>   
    </div>
</template>

<script setup>
    import NavigationBar from '@/components/NavigationBar.vue';
    import { onMounted, ref } from 'vue';
    import axios from '@/plugins/axios';
    import { useRouter } from 'vue-router';

    const orderId = ref(null);
    const router = useRouter();
    const isCanceled = ref(false);
    const goToMyTicket = () => {
        router.push(`/myticket`);
    }

    const goToMain = () => {
        router.push('/');
    }

    const isSuccess = ref(false);
    const isCancel = ref(false);
    const price = ref(null);
    const isPayCancel = ref(false);

    onMounted(async () => {
        const urlParams = new URLSearchParams(window.location.search);
        const payId = urlParams.get('orderId');
        const code = urlParams.get('code');
        
        // 결제 취소 상태 체크
        if (code === 'PAY_PROCESS_CANCELED') {
            isCancel.value = true;
            if (payId) {
                await axios.delete(`/api/orders/${payId}`);
            }
            return;
        }

        else if (code === '전액환불') { 
            const orderId = urlParams.get('orderId');
            await axios.put(`/api/orders/${orderId}/cancel`);
            isCanceled.value = true;
        }

         else if (code === '50%환불') { 
            const orderId = urlParams.get('orderId');
            await axios.put(`/api/orders/${orderId}/cancel`);
            price.value = urlParams.get('price');
            isCanceled.value = true;
        }

        else if(code === '환불') {
            const orderId = urlParams.get('orderId');
            await axios.put(`/api/orders/${orderId}/cancel`);
            price.value = urlParams.get('price');
            isPayCancel.value = true;
        } else {

        // 기존 성공 로직
        const response = await axios.put(`/api/orders/${payId}/success`);
        if(response) {
            orderId.value = response.data.data.orderId;
            isSuccess.value = true;
        } else {
            isSuccess.value = false;
            await axios.delete(`/api/orders/${payId}`);
        }}
    });
</script>

<style scoped>
.container {
    text-align: center;
    padding: 2rem;
    position: fixed;
    top: 40%;
    left: 50%;
    width: 80%;
    transform: translate(-50%, -50%);   
}

.success-icon {
    width: 100px;
    height: 100px;
    margin: 0 auto 2rem;
}

.circle {
    stroke: #4CAF50;
    stroke-width: 2;
    stroke-dasharray: 166;
    stroke-dashoffset: 166;
    animation: circle 0.6s ease-in-out forwards;
}

.check {
    stroke: #4CAF50;
    stroke-width: 2;
    stroke-dasharray: 48;
    stroke-dashoffset: 48;
    animation: check 0.6s ease-in-out 0.4s forwards;
    fill: none;
}

@keyframes circle {
    100% {
        stroke-dashoffset: 0;
    }
}

@keyframes check {
    100% {
        stroke-dashoffset: 0;
    }
}

.cancel-icon {
    width: 100px;
    height: 100px;
    margin: 0 auto 2rem;
}

.circle-cancel {
    stroke: #FF5252;
    stroke-width: 2;
    stroke-dasharray: 166;
    stroke-dashoffset: 166;
    animation: circle 0.6s ease-in-out forwards;
}

.cross {
    stroke: #FF5252;
    stroke-width: 2;
    stroke-dasharray: 48;
    stroke-dashoffset: 48;
    animation: cross 0.6s ease-in-out 0.4s forwards;
    fill: none;
}

@keyframes cross {
    100% {
        stroke-dashoffset: 0;
    }
}

button {
    background-color: #4CAF50;
    color: white;
    border: none;
    padding: 15px 24px;
    border-radius: 8px;
    font-size: 16px;
    margin: 10px;
    cursor: pointer;
    width: 100%;
    transition: background-color 0.3s ease;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

button:hover {
    background-color: #45a049;
}

button:active {
    transform: translateY(1px);
}

/* 메인으로 돌아가기 버튼은 다른 스타일 적용 */
button:last-child {
    background-color: #2352ec;
}

button:last-child:hover {
    background-color: #2352ec;
}
</style>

