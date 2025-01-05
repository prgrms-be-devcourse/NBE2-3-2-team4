<template>
  <NavigationBar />
  <div class="main-container">
    <!-- 주차권 할인 섹션 -->
    <div class="ticket-section discount">
      <div class="section-header">
        <img src="@/assets/ticket-icon.png" alt="티켓 아이콘">
        <h3>주차권 확인</h3>
      </div>
      <p class="description">구매한 주차권을 확인하세요</p>
      <button class="action-button" @click="navigateToMyTicketPage">주차권 확인하기</button>
    </div>

    <!-- 주차권 구매 섹션 -->
    <div class="ticket-section purchase">
      <div class="section-header">
        <img src="@/assets/car-icon.png" alt="차 아이콘">
        <h3>주차권 구매</h3>
      </div>
      <p class="description">새로운 주차권을 구매하세요</p>
      <button class="action-button purchase" @click="navigateToSearchPage">주차권 구매하기</button>
    </div>
  </div>
</template>

<script>
import NavigationBar from '@/components/NavigationBar.vue';
import { useRouter } from 'vue-router';
export default {
    name: 'MainPage',
    components: {
        NavigationBar,
    },
    setup() {
        const router = useRouter();
        const navigateToSearchPage = () => {
            router.push('/search');
        };
        const navigateToMyTicketPage = () => {
            router.push('/myticket');
        };
        return { navigateToSearchPage, navigateToMyTicketPage };
    }
}
</script>

<style scoped>
.main-container {
  padding: 20px;
  max-width: 500px; /* 최대 너비 제한 */
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 90%; /* 반응형을 위한 너비 설정 */
  max-width: 500px; /* 최대 너비 제한 */
}

.ticket-section {
  background-color: #f8f9ff;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
}


.description {
  color: #7e7e7e;
  font-size: 14px;
  margin-bottom: 15px;
  font-weight: 500;
}

.action-button {
  width: 100%;
  padding: 15px;
  border: none;
  border-radius: 8px;
  color: white;
  font-weight: bolder;
  cursor: pointer;
  font-size: 16px;
  background-color: #4169e1;
}

.action-button.purchase {
  background-color: #2ea043;
}

.blue {
  background-color: #4169e1;
}

.green {
  background-color: #2e8b57;
}

.blue:hover {
  background-color: #3557c0;
}

.green:hover {
  background-color: #246b44;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.section-header h3 {
  margin: 0;
  height: 24px;
  font-size: 18px;
  font-weight: 900;
}
</style>