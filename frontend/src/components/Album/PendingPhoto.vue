<template>
  <div class="pending-area" v-if="files.length > 0">
    <h3>待上传照片 ({{ files.length }})</h3>
    <div class="pending-photos">
      <div v-for="(file, index) in files" :key="file.id" class="pending-item">
        <img :src="file.previewUrl" :alt="file.name" />
        <div class="file-info">
          <span class="file-name">{{ truncateFileName(file.name) }}</span>
          <span class="file-size">{{ formatFileSize(file.size) }}</span>
        </div>
        <button class="remove-btn" @click="removeFile(index)">
          &times;
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  files: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['update:files'])
const internalFiles = ref([...props.files])

// 监听外部文件数组变化，保持同步
watch(() => props.files, (newFiles) => {
  internalFiles.value = [...newFiles]
})

// 删除文件
const removeFile = (index) => {
  internalFiles.value.splice(index, 1)
  emit('update:files', internalFiles.value)
}

const truncateFileName = (name) => {
  if (name.length > 20) {
    return name.substring(0, 15) + '...' + name.split('.').pop()
  }
  return name
}

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}
</script>

<style scoped>
.pending-photos {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 15px;
  margin: 15px 0;
}

.pending-item {
  position: relative;
  border: 1px solid #eee;
  border-radius: 6px;
  padding: 10px;
}

.pending-item img {
  width: 100%;
  height: 120px;
  object-fit: cover;
  border-radius: 4px;
}

.file-info {
  margin-top: 8px;
  display: flex;
  flex-direction: column;
}

.file-name {
  font-size: 14px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.file-size {
  font-size: 12px;
  color: #999;
}

.remove-btn {
  position: absolute;
  top: 5px;
  right: 5px;
  width: 24px;
  height: 24px;
  background-color: rgba(255, 0, 0, 0.7);
  color: white;
  border: none;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 14px;
}
</style>
