<template>
  <div class="search-header">
      <div class="back-button">
        <img src="@/assets/back-arrow.png" alt="뒤로가기" @click="handleBackClick">
      </div>
      <div class="search-bar">
        <input 
        type="text" 
        placeholder="목적지를 검색하세요"
        v-model="keyword"
        @input="handleInputChange"
        @focus="handleFocus"   
      >
      </div>
    </div>
</template>

<script>
import { ref } from 'vue';

export default {
    name: 'InputBar',
    data() {
        return {
            keyword: '',
            minLength: 1,
            debounceTime: 300
        }
    },
    setup() {
        // ref를 사용하여 타이머 ID 유지
        const timerId = ref(null);
        
        // 디바운스 함수 생성
        const debounce = (callback, delay) => {
            return (...args) => {
                if (timerId.value) clearTimeout(timerId.value);
                timerId.value = setTimeout(() => {
                    callback(...args);
                }, delay);
            };
        };

        return {
            timerId,
            debounce
        };
    },
    methods: {
        handleFocus() {
            // 입력창이 포커스될 때 실행할 코드
            this.$emit('input-focus');
        },
        handleBackClick() {
            this.$emit('back-click');
        },
        handleInputChange() {
            const updateRoute = (trimmedKeyword) => {
                this.$router.push({
                    name: 'SearchPage',
                    query: { keyword: trimmedKeyword }
                });
            };

            const debouncedUpdate = this.debounce(updateRoute, this.debounceTime);
            
            const trimmedKeyword = this.keyword.trim();
            
            if (trimmedKeyword.length < this.minLength) {
                updateRoute('');
                return;
            }

            if (this.$route.query.keyword === trimmedKeyword) {
                return;
            }

            debouncedUpdate(trimmedKeyword);
        }
    },
    beforeUnmount() {
        if (this.timerId) {
            clearTimeout(this.timerId);
        }
    }
}
</script>

<style>
.search-header {
  position: fixed;
  top: 50px; /* NavigationBar 높이만큼 여백 추가 */
  left: 0;
  right: 0;
  padding: 15px;
  background-color: white;
  display: flex;
  align-items: center;
  z-index: 100;
  border-bottom: 1px solid #f0f0f0;
}
.back-button {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-button img {
  cursor: pointer;
}

.search-bar {
  flex: 1;
  margin-left: 5px;
  margin-right: 5px;
}

.search-bar input {
  width: 100%;
  padding: 13px 15px;
  border: 0;
  border-radius: 10px;
  font-size: 15px;
  background-color: #f5f5f5;
}

.search-bar input:focus {
    outline: none;
}

</style>