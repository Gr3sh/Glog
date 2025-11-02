<template>
  <div class="upload-button-wrapper">
    <input
      type="file"
      ref="fileInput"
      multiple
      :accept="accept"
      style="display: none"
      @change="handleChange"
    />
    <button class="upload-button" @click="triggerInput">
      <slot> {{ buttonText }} </slot>
    </button>
  </div>

  <div class="pending-area" v-if="pendingFiles.length > 0">
    <h3>待上传照片 ({{ pendingFiles.length }})</h3>
    <div class="pending-photos">
      <div v-for="(file, index) in pendingFiles" :key="file.id" class="pending-item">
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
  modelValue: {type: Array, default: () => []},
  accept: {type: String, default: '*/*'},
  buttonText: {type: String, default: '选择文件'},
  multiple: {type: Boolean, default: true}
})

const emit = defineEmits(['update:modelValue']) 
const fileInput = ref(null)
const pendingFiles = ref([...props.modelValue])

watch(() => props.modelValue, (newVal) => {
  pendingFiles.value = [...newVal]
})

const triggerInput = () => {fileInput.value.click()}

const handleChange = (e) => {
  const selectedFiles = Array.from(e.target.files).map(file => ({
    id: Date.now() + Math.random(),
    name: file.name,
    size: file.size,
    rawFile: file,
    previewUrl: URL.createObjectURL(file)
  }))
  const newFiles = props.multiple? [...pendingFiles.value, ...selectedFiles]: selectedFiles
  pendingFiles.value = newFiles
  emit('update:modelValue', newFiles)
  e.target.value = ''
}

const removeFile = (index) => {
  const newFiles = [...props.modelValue]
  URL.revokeObjectURL(newFiles[index].previewUrl)
  newFiles.splice(index, 1)
  pendingFiles.value = newFiles
  emit('update:modelValue', newFiles)
}

const truncateFileName = (name) => {
  if (name.length > 20)
    return name.substring(0, 15) + '...' + name.split('.').pop()
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
.upload-button-wrapper {
  display: inline-block;
}
.upload-button {
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}
.upload-button:hover {
  background-color: #3aa876;
}

.pending-area {
  margin-top: 20px;
}
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
