<template>
  <NavigationBar />
  <NoInputBar @back="goBack" />
  <div class="edit-my-info">
    <section class="info-section">
      <div class="section-title">기본정보</div>
      
      <div class="input-group">
        <label class="input-label">이름</label>
        <input type="text" v-model="userInfo.name" placeholder="이름" />
      </div>
      
      <div class="input-group">
        <label class="input-label">휴대폰</label>
        <input type="tel" v-model="userInfo.contact" placeholder="010-1234-1234" />
      </div>
      
      <div class="input-group">
        <label class="input-label">이메일</label>
        <input 
          type="email" 
          v-model="userInfo.email" 
          placeholder="abc@naver.com"
          disabled
          class="disabled-input"
        />
      </div>
    </section>


    <button class="submit-button" @click="updateUserInfo">
      수정
    </button>
  </div>
</template>

<script>
import axios from '@/plugins/axios';
import NavigationBar from '@/components/NavigationBar.vue';
import NoInputBar from '@/components/NoInputBar.vue';


export default {
  name: 'EditMyInfoPage',
  components: {
    NavigationBar,
    NoInputBar
  },
  data() {
    return {
      userInfo: {
        name: '',
        contact: '',
        email: '',
      },
      isLogin: false,
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
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
    async updateUserInfo() {
      const response = await axios.put('/api/members', this.userInfo);
      console.log(response);
      if(response) {
        alert('수정되었습니다');
        this.$router.push('/mypage');
      }
    }
  },
  mounted() {
    this.getUserInfo();
  }
}
</script>

<style scoped>
.edit-my-info {
  position: fixed;
  width: 100%;
  background-color: #ffffff;
  min-height: 100vh;
  top: 13%;
}

.header {
  display: flex;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.back-button {
  border: none;
  background: none;
  font-size: 20px;
  padding: 0;
  margin-right: 16px;
}

.header h1 {
  font-size: 18px;
  font-weight: 500;
}

.section-title {
  padding: 20px;
  color: #000;
  font-weight: bold;
  font-size: 18px;
}

.input-group {
  padding: 16px 20px;
  background: white;
  margin-bottom: 10px;
}

.input-group label {
  display: block;
  color: #666;
  font-size: 17px;
  padding-bottom: 5px;
  font-weight: bold;
  margin-bottom: 8px;
}

.input-group input {
  width: 100%;
  border: none;
  border-radius: 10px;
  padding: 15px;
  background-color: #f5f5f5;
  font-size: 16px;
  color: #111;
  margin-top: 5px;
}

.input-group input:focus {
  outline: none;
}

.kakao-account img {
  width: 24px;
  height: 24px;
}

.car-number {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #333;
  font-size: 16px;
}

.arrow {
  color: #999;
  font-size: 20px;
}

.submit-button {
  position: fixed;
  bottom: 20px;
  left: 20px;
  right: 20px;
  width: calc(100% - 40px);
  padding: 15px;
  background-color: #4285f4;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
}

.disabled-input {
  background-color: #eee !important;
  color: #666 !important;
  cursor: not-allowed;
}
</style>
