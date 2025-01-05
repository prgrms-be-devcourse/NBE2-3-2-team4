<template>
    <div class="pay-footer">
        <div class="input-container">
            <img src="@/assets/dark-car-icon.png" alt="차량번호" class="car-icon">
            <input 
                type="text" 
                placeholder="차량번호를 입력해주세요"
                v-model="carNumber"  
            >
        </div>
        <div class="pay-button">
            <button @click="handlePayment">결제하기</button>
        </div>
    </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue';

const carNumber = ref('');

const emit = defineEmits(['payment']);

// 결제 이벤트를 부모에게 전송하는 함수
const handlePayment = () => {
    // 차량번호를 부모에게 전송
    emit('payment', carNumber.value);
};
</script>

<style scoped>
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

.car-icon {
    position: absolute;
    left: 16px;
    top: 50%;
    transform: translateY(-50%);
    width: 20px;
    height: 20px;
}

.pay-footer input {
    width: 100%;
    padding: 16px 16px 16px 56px;
    border-radius: 15px;
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
    padding: 14px;
    border: none;
    border-radius: 15px;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    background-color: #2563EB;
    color: white;
}
</style>
