<template>
  <NavigationBar />
  <NoInputBar @back="goBack" />
  <div class="signup-page">
    <section class="info-section">
      <div class="section-title">회원가입</div>
      
      <div class="input-group">
        <label class="input-label">이름</label>
        <input type="text" v-model="userInfo.name" placeholder="이름" />
      </div>
      
      <div class="input-group">
        <label class="input-label">이메일</label>
        <input type="email" v-model="userInfo.email" placeholder="abc@naver.com" />
      </div>

      <div class="input-group">
        <label class="input-label">비밀번호</label>
        <input type="password" v-model="userInfo.password" placeholder="비밀번호" />
      </div>

      <div class="input-group">
        <label class="input-label">비밀번호 확인</label>
        <input type="password" v-model="userInfo.passwordConfirm" placeholder="비밀번호 확인" />
      </div>
      
      <div class="input-group">
        <label class="input-label">휴대폰</label>
        <input type="tel" v-model="userInfo.contact" placeholder="010-1234-1234" />
      </div>
    </section>

    <button class="submit-button" @click="signup">
      가입하기
    </button>
  </div>
</template>

<script>
import axios from '@/plugins/axios';
import NavigationBar from '@/components/NavigationBar.vue';
import NoInputBar from '@/components/NoInputBar.vue';

export default {
  name: 'SignupPage',
  components: {
    NavigationBar,
    NoInputBar
  },
  data() {
    return {
      userInfo: {
        name: '',
        email: '',
        password: '',
        passwordConfirm: '',
        contact: '',
      }
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    async signup() {
      if (!this.validateForm()) {
        return;
      }

      try {
        const response = await axios.post('/api/auth/signup', {
          name: this.userInfo.name,
          email: this.userInfo.email,
          password: this.userInfo.password,
          contact: this.userInfo.contact
        });

        if (response) {
          alert('회원가입이 완료되었습니다.');
          this.$router.push('/login');
        } 
      } catch (error) {
        alert('회원가입에 실패했습니다.');
        
      }
    },
    validateForm() {
      if (!this.userInfo.name || !this.userInfo.email || !this.userInfo.password || !this.userInfo.contact) {
        alert('모든 필드를 입력해주세요.');
        return false;
      }

      const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
      if (!emailRegex.test(this.userInfo.email)) {
        alert('올바른 이메일 형식이 아닙니다.');
        return false;
      }

      if (this.userInfo.password !== this.userInfo.passwordConfirm) {
        alert('비밀번호가 일치하지 않습니다.');
        return false;
      }
      return true;
    }
  }
}
</script>

<style scoped>
.signup-page {
  position: relative;
  width: 100%;
  background-color: #ffffff;
  min-height: 100vh;
  padding-top: 28%;
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
</style>
