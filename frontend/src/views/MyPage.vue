<template>
  <div class="root-container">
    <NavigationBar />
    <NoInputBar @back="handleBack" />
    <div class="page-container">
      <div v-if="isLogin" class="my-page-content">
        <div class="profile-section">
          <h2>{{ userInfo.name }}님의 마이페이지</h2>
        </div>
        
        <div class="menu-section">
          <div class="menu-group">
            <button class="menu-button" @click="editMyInfo">
              <i class="fas fa-user"></i>
              내 정보 수정
            </button>
            <button class="menu-button">
              <i class="fas fa-car"></i>
              내 차량 관리
            </button>
          </div>
          
          <div class="menu-group">
            <button class="menu-button logout-button" @click="handleLogout">
              <i class="fas fa-sign-out-alt" style="text-align: center;"></i>
              로그아웃
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import NavigationBar from '@/components/NavigationBar.vue';
import NoInputBar from '@/components/NoInputBar.vue';
import axios from '@/plugins/axios';

export default {
  name: 'MyPage',
  components: {
    NavigationBar,
    NoInputBar
  },
  data() {
    return {
      userInfo: null,
      isLogin: false
    }
  },
  methods: {
    async getUserInfo() {
      const response = await axios.get('/api/members/info');
      if(response) {
        this.userInfo = response.data.data;
        console.log(this.userInfo);
        this.isLogin = true;
      } else {
        alert('로그인 페이지로 이동합니다');
        this.$router.push('/login');
      }
    },
    handleLogout() {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      alert('로그아웃이 완료되었습니다.');
      this.$router.push('/');
    },
    handleBack() {
      this.$router.go(-1);
    },
    editMyInfo() {
      this.$router.push('/mypage/edit');
    }
  },
  mounted() {
    this.getUserInfo();
  }
}
</script>

<style scoped>
.root-container {
  position: fixed;
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

.page-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
  margin-top: 80px;
  height: calc(100vh - 80px);
  overflow-y: auto;
}

.my-page-content {
  background: white;
  padding: 30px;
}

.profile-section {
  margin-top: 30px;
  text-align: center;
  font-weight: bold;
  margin-bottom: 40px;
}

.profile-section h2 {
  font-size: 1.4rem;
  color: #333;
  margin-bottom: 20px;
}

.menu-section {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.menu-group {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.menu-group h3 {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 10px;
}

.menu-button {
  width: 100%;
  padding: 15px 20px;
  background-color: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 12px;
  font-size: 1rem;
  color: #495057;
  text-align: left;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 10px;
}

.menu-button:hover {
  background-color: #e9ecef;
  transform: translateY(-2px);
}

.menu-button i {
  font-size: 1.2rem;
  width: 24px;
}

.logout-button {
  margin-top: 30px;
  background-color: #2563EB;
  color: #fff;
  font-weight: bold;
  text-align: center;
  border: none;
}

.logout-button i {
  text-align: center;
}

</style>
