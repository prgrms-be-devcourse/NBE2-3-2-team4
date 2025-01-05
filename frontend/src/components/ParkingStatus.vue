<template>
  <div class="parking-status">
    <div class="status-header">
      <span>주변 주차장</span>
      <span v-if="pkltList.length !== 0" class="found-count">{{ pkltList.length }}개의 발견됨</span>
    </div>
    <div class="parking-list">
      <div v-if="pkltList.length === 0" class="no-data">
        주차장 정보가 없습니다
      </div>
      <template v-else>
        <!-- 첫 번째 주차장 -->
        <div v-if="pkltList[0]" class="parking-item" >
          <div class="parking-info" @click="handleStatusClick(pkltList[0])" >
            <span class="label">{{ pkltList[0].pkltNm}}</span>
          </div>
          <div class="status" :class="getStatusClass(pkltList[0].status)">
            {{ getStatusText(pkltList[0].status) }}
          </div>
        </div>
        <!-- 두 번째 주차장 -->
        <div v-if="pkltList[1]" class="parking-item">
          <div class="parking-info" @click="handleStatusClick(pkltList[1])">
            <span class="label">{{pkltList[1].pkltNm}}</span>
          </div>
          <div class="status" :class="getStatusClass(pkltList[1].status)">
            {{ getStatusText(pkltList[1].status) }}
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import axios from '@/plugins/axios'; 

const props = defineProps({
    latitude: Number,
    longitude: Number
});

// emit 정의
const emit = defineEmits(['update-pklt-list', 'select-parking']);

const pkltList = ref([]);

const fetchPkltList = async () => {
    try {
        const lat = Number(props.latitude).toFixed(8);
        const lng = Number(props.longitude).toFixed(8);
        const response = await axios.get(`/api/pklt/search?lat=${lat}&lng=${lng}`);
        pkltList.value = response.data.data;
        // pkltList가 업데이트될 때마다 상위 컴포넌트로 전달
        emit('update-pklt-list', pkltList.value);
        console.log(pkltList.value);
    } catch (error) {
        console.error('주차장 정보 조회 실패:', error);
    }
};

// 좌표 변화 감지를 위한 watcher 추가
watch(
  () => [props.latitude, props.longitude],
  () => {
    if (props.latitude && props.longitude) {
      fetchPkltList();
    }
  }
);

// handleStatusClick 함수 추가
const handleStatusClick = (Parking) => {
  emit('select-parking', Parking);
};

const getStatusClass = (status) => {
    switch (status) {
        case '여유':
            return 'available';
        case '보통':
            return 'normal';
        case '혼잡':
            return 'busy';
        default:
            return '';
    }
};

const getStatusText = (status) => {
    switch (status) {
        case '여유':
            return '여유';
        case '보통':
            return '보통';
        case '혼잡':
            return '혼잡';
        default:
            return '상태 없음';
    }
};

</script>

<style scoped>
.parking-status {
  background-color: white;
  padding: 1rem 1rem;
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  z-index: 999;
  box-shadow: 0 -4px 6px rgba(0, 0, 0, 0.1);
}

.status-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 15px;
  font-weight: bold;
  margin-bottom: 1rem;
  color: #1a1a1a;
}

.found-count {
  color: #696969;
  font-weight: bold;
  font-size: 13px;
}

.parking-list {
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
}

.parking-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0.4rem;
  border-radius: 8px;
  background-color: #f5f5f5;
  transition: all 0.2s ease;
}

.parking-item:hover {
  transform: translateY(-1px);
}

.parking-info {
  display: flex;
  gap: 0.5rem;
  flex: 1;
  min-width: 0;
}

.label {
  font-size: 14px;
  color: #333333;
  font-weight: bold;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  margin-right: 8px;
  margin-left: 10px;
}

.status {
  font-size: 14px;
  padding: 4px 12px;
  border-radius: 20px;
  font-weight: bold;
  flex-shrink: 0;
  white-space: nowrap;
}

.available {
  color: #2ea043;
}

.normal {
  color: #eec927;
}

.busy {
  color: #ff4646;
}

.no-data {
  text-align: center;
  color: #666;
  padding: 0.7rem;
  background-color: #f5f5f5;
  font-weight: bold;
  border-radius: 8px;
}
</style> 