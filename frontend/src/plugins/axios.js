// src/plugins/axios.js
import axios from 'axios';

const instance = axios.create({
  baseURL: 'http://localhost:8080', 
  timeout: 5000, 
  headers: { 'Content-Type': 'application/json' },
});

// 요청 인터셉터 설정
instance.interceptors.request.use(
  config => {
    const token = localStorage.getItem('accessToken');
    config.headers['authorization'] = `Bearer ${token}`;
    return config;
  },
  error => {
    console.error('요청 인터셉터 에러:', error);
    return Promise.reject(error);
  }
);

const reissueToken = async () => {
  try {
    console.log('토큰 재발급 시도');
    const refreshToken = localStorage.getItem('refreshToken');
    const accessToken = localStorage.getItem('accessToken');
    
    const response = await instance.post('/api/auth/reissue', 
      { refreshToken },
      {
        headers: {
          'Content-Type': 'application/json',
          'authorization': `Bearer ${accessToken}`
        }
      }
    );
    
    if (response.data) {
      localStorage.setItem('accessToken', response.data.data.accessToken);
      localStorage.setItem('refreshToken', response.data.data.refreshToken);
      return true;
    }
    return false;
  } catch (error) {
    console.error('토큰 재발급 실패:', error);
    return false;
  }
};

// 응답 인터셉터 설정
instance.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;

    if (error.response && error.response.status === 401 && !originalRequest._retry) {
      const errorMessage = error.response.data?.message;
      
      if (errorMessage?.includes('만료')) {
        console.log('토큰이 만료되었습니다. 재발급이 필요합니다.');
        originalRequest._retry = true;
        
        const isReissueSuccess = await reissueToken();
        if (isReissueSuccess) {
          // 새로운 토큰으로 원래 요청 재시도
          originalRequest.headers['authorization'] = `Bearer ${localStorage.getItem('accessToken')}`;
          return instance(originalRequest);
        }
      }
      
      // 토큰 재발급 실패 또는 유효하지 않은 토큰
      console.log('인증에 실패했습니다. 다시 로그인해주세요.');
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
    }

    // 다른 에러들은 기존대로 처리
    if (error.response && error.response.data) {
      const errorData = error.response.data;
      console.error('[ERROR] status: ', errorData.status, " message: " , errorData.message);
    } else {
      console.error('서버 연결 실패:', error.message);
    }

  }
);
export default instance;
