<template>
  <div class="album-display">
    <div v-for="[date, dayPhotos] in groupedPhotos" :key="date" class="photo-day">
      <h3 class="photo-date">{{ date }}</h3>
      <div class="photo-row">
        <div v-for="photo in dayPhotos" :key="photo.photoId" class="photo-item">
          <img :src="photo.storagePath" :alt="photo.originalName" @click="showLarge(photo)"/>
          <button class="delete-btn" @click="deletePhoto(photo.photoId)">×</button>
        </div>
      </div>
    </div>
    <div v-if="largePhoto" class="photo-modal" @click="largePhoto = null">
      <img :src="largePhoto.storagePath" :alt="largePhoto.originalName" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import axios from 'axios'

const photos = ref([])
const largePhoto = ref(null)

async function fetchPhotos() {
  try {
    const res = await axios.get('http://localhost:8080/photos/list')
    console.log('Fetched photos:', res.data)
    photos.value = res.data.map(photo => ({
      ...photo,
      storagePath: `http://localhost:8080/uploads/${encodeURIComponent(photo.originalName)}`
    }))
  } catch (err) {
    console.error('获取照片失败:', err)
  }
}

fetchPhotos()

async function deletePhoto(photoId) {
  try {
   console.log('Deleting photo with ID:', photoId)
    await axios.delete(`http://localhost:8080/photos/delete/${photoId}`)
    alert('删除成功')
    fetchPhotos() 
  } catch (err) {
    console.error('删除失败:', err)
    alert('删除失败，请重试')
  }
}

// 按日期分组，最新日期在上
const groupedPhotos = computed(() => {
  const groups = {}
  photos.value.forEach(photo => {
    const date = photo.photoTime.split('T')[0] // 取 yyyy-MM-dd
    if (!groups[date]) groups[date] = []
    groups[date].push(photo)
  })
  return Object.entries(groups).sort((a, b) => new Date(b[0]) - new Date(a[0]))
})

function showLarge(photo) {
  largePhoto.value = photo
}

// 暴露刷新方法给父组件
defineExpose({
  fetchPhotos
})
</script>

<style scoped>
.album-display {
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding: 16px;
}

.photo-day {
  display: flex;
  flex-direction: column;
}

.photo-date {
  font-weight: 600;
  margin-bottom: 12px;
  font-size: 1.2rem;
}

.photo-row {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.photo-item {
  position: relative;
}

.photo-item img {
  width: 150px;
  height: 150px;
  object-fit: cover;
  border-radius: 12px;
  transition: transform 0.2s;
}

.photo-item img:hover {
  transform: scale(1.05);
}

/* 删除按钮样式 */
.delete-btn {
  position: absolute;
  top: 4px;
  right: 4px;
  background-color: rgba(255, 0, 0, 0.8);
  color: white;
  border: none;
  border-radius: 50%;
  width: 24px;
  height: 24px;
  cursor: pointer;
  font-weight: bold;
  line-height: 24px;
  text-align: center;
  opacity: 0;
  transition: opacity 0.2s;
}

.photo-item:hover .delete-btn {
  opacity: 1;
}

/* 大图弹窗样式 */
.photo-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.photo-modal img {
  max-width: 90%;
  max-height: 90%;
  border-radius: 12px;
}
</style>
