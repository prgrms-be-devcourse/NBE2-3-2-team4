<template>
  <nav class="navigation-bar">
    <div class="nav-content">
      <div class="logo">
        <span>뚝딱파킹 🚗</span>
      </div>
      <button class="user-icon-btn" @click="navigateToMyPage">
        <img src="@/assets/user-icon.png" alt="사용자 아이콘" />
      </button>
    </div>
  </nav>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const navigateToMyPage = () => {
  router.push('/mypage');
};

</script>

<style scoped>
.navigation-bar {
  width: 100%;
  background-color: #ffffff;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
}

.nav-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  margin: 0 auto;
}

.logo {
  font-size: 21px;
  font-weight: bold;
}

.user-icon-btn {
  background: none;
  border: none;
  padding: 0;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.user-icon-btn img {
  height: 21px;
}

.user-icon-btn:hover {
  opacity: 0.8;
}
</style>