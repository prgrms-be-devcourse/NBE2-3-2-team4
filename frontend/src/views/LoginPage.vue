<template>
  <NavigationBar />
  <NoInputBar @back="goBack" />
  <div class="login-container">
    <div class="login-box">
      <h2>로그인</h2>
      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <label for="email">이메일</label>
          <input 
            type="email" 
            id="email" 
            v-model="email" 
            required 
            placeholder="이메일을 입력하세요"
          >
        </div>
        <div class="input-group">
          <label for="password">비밀번호</label>
          <input 
            type="password" 
            id="password" 
            v-model="password" 
            required 
            placeholder="비밀번호를 입력하세요"
          >
        </div>
        <button type="submit" class="login-btn">로그인</button>
      </form>
      <div class="signup-link">
        계정이 없으신가요? <a href="#" @click="handleSignup">회원가입</a>
      </div>
    </div>
  </div>
</template>

<script>
import axios from '@/plugins/axios';
import NavigationBar from '@/components/NavigationBar.vue';
import NoInputBar from '@/components/NoInputBar.vue';

export default {
  components: {
    NavigationBar,
    NoInputBar
  },

  name: 'LoginPage',
  data() {
    return {
      email: '',
      password: ''
    }
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    async handleLogin() {
      try {
        const response = await axios.post('/api/auth/login', {
          email: this.email,
          password: this.password
        }, {
          headers: {
            'Content-Type': 'application/json'
          }
        });
        
        if (response.data) {
            const accessToken = response.data.data.accessToken;
            const refreshToken = response.data.data.refreshToken;
            // 토큰을 로컬 스토리지에 저장
          localStorage.setItem('accessToken', accessToken);
          localStorage.setItem('refreshToken', refreshToken);
          
          alert('로그인 성공');
          // TODO: 로그인 성공 후 메인 페이지로 이동
          this.$router.push('/');
        }
      } catch (error) {
        console.error('로그인 실패:', error);
        alert('로그인에 실패했습니다.');
        // TODO: 에러 메시지 표시
      }
    },
    handleSignup() {
      // 회원가입 페이지로 이동
      this.$router.push('/signup');
    },

  }
}
</script>

<style scoped>
.login-container {
  position: fixed;
  justify-content: center;
  align-items: center;
  top: 65%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  height: 100%;
}

.login-box {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  width: 100%;
  max-width: 400px;
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 2rem;
}

.input-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #666;
}

input {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 10px;
  font-size: 1rem;
}

input:focus {
  outline: none;
  border-color: #4169e1;
}

.login-btn {
  width: 100%;
  padding: 0.8rem;
  background-color: #4169e1;
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.login-btn:hover {
  background-color: #4169e1;
}

.signup-link {
  text-align: center;
  margin-top: 1rem;
}

.signup-link a {
  color: #4CAF50;
  text-decoration: none;
}

.signup-link a:hover {
  text-decoration: underline;
}
</style>
