<template>
  <div class="photo-filter-page">
    <h2>筛选照片</h2>

    <TagInput
      v-model="includeTags"
      label="选择想要的标签"
      ref="includeTagInput"
    />
    <TagInput
      v-model="excludeTags"
      label="选择不要的标签"
      ref="excludeTagInput"
    />

    <div class="photo-grid">
      <div
        v-for="photo in filteredPhotos"
        :key="photo.photoId"
        class="photo-item"
        @click="openFullscreen(photo)"
      >
        <img :src="photo.storagePath" :alt="photo.originalName" />
        <div class="photo-tags">
          {{ photo.tags.map(tag => tag.tagName).join(', ') }}
        </div>
      </div>
    </div>

    <div 
      v-if="fullscreenPhoto" 
      class="fullscreen-overlay" 
      @click="closeFullscreen"
    >
      <img :src="fullscreenPhoto.storagePath" :alt="fullscreenPhoto.originalName" />
    </div>
  </div>
</template>

<script setup>
import axios from 'axios'
import { ref, computed, onMounted } from 'vue'
import TagInput from '@/components/Album/TagInput.vue';

const photos = ref([])
const includeTags = ref([])
const excludeTags = ref([])
const baseUrl = 'http://localhost:8080/uploads/'
const fullscreenPhoto = ref(null)

async function loadData() {
  try {
    const [photosRes, tagsRes] = await Promise.all([
    axios.get('http://localhost:8080/photos/list'), 
    axios.get('http://localhost:8080/tags')
    ])
    
    photos.value = photosRes.data.map(photo => ({
      ...photo,
      storagePath: `${baseUrl}${photo.originalName}`
    }))

    tags.value = tagsRes.data
  } catch (err) {
    console.error('加载数据失败', err)
  }
}

onMounted(() => {
  loadData()
})

function openFullscreen(photo) {
  fullscreenPhoto.value = photo
}

function closeFullscreen() {
  fullscreenPhoto.value = null
}

const filteredPhotos = computed(() => {
  return photos.value.filter(photo => {
    const photoTagNames = photo.tags.map(tag => tag.tagName) 
    const hasInclude = includeTags.value.every(tag => photoTagNames.includes(tag))
    const hasExclude = excludeTags.value.every(tag => !photoTagNames.includes(tag))
    return hasInclude && hasExclude
  })
})
</script>

<style scoped>
.photo-grid {
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 16px;
  margin-top: 16px;
  column-width: 150px;
  column-gap: 20px; 
}

.photo-item {
  display: inline-block;
  width: 100%;
  cursor: pointer;
  margin-bottom: 12px;
}

.photo-item img {
  width: 100%;
  border-radius: 8px;
  display: block;
  transition: transform 0.3s ease;
}

.photo-item:hover img {
  transform: scale(1.1);
}

.photo-tags {
  font-size: 12px;
  margin-top: 4px;
  color: #555;
}

.fullscreen-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0,0,0,0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
  cursor: zoom-out;
}

.fullscreen-overlay img {
  max-width: 90%;
  max-height: 90%;
  border-radius: 8px;
  box-shadow: 0 0 20px rgba(0,0,0,0.5);
}
</style>
