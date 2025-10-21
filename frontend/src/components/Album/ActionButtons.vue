<template>
  <div class="action-buttons">
    <button 
      class="cancel-btn" 
      :disabled="disabled"
      @click="handleCancel"
    >
      {{ cancelText }}
    </button>
    <button 
      class="confirm-btn" 
      :disabled="isConfirmDisabled"
      @click="handleConfirm"
    >
      {{ confirmText }}
    </button>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import axios from 'axios'

// Props: 可自定义按钮文案和状态
const props = defineProps({
  pendingFiles: { type: Array, default: () => [] },
  selectedTags: { type: Array, default: () => [] },
  selectedDate: { type: String, default: () => '' },
  uploadUrl: { type: String, default: 'http://localhost:8080/photos/upload' },
  confirmText: { type: String, default: '上传全部' },
  cancelText: { type: String, default: '取消' },
  disabled: { type: Boolean, default: false },
  primaryDisabled: { type: Boolean, default: false }
})

// Emits
const emit = defineEmits([
  'update:pendingFiles',
  'update:selectedTags',
  'update:selectedDate',
  'uploaded',
  'cancelled',
  'error'
])

// 按钮禁用状态
const isConfirmDisabled = computed(() => {
  return props.disabled || props.primaryDisabled || props.pendingFiles.length === 0
})

// 格式化日期 YYYY-MM-DD
function formatDate(date) {
  return date.toISOString().split('T')[0]
}

// 取消操作
function handleCancel() {
  emit('update:pendingFiles', [])
  emit('update:selectedTags', [])
  emit('update:selectedDate', formatDate(new Date()))
  emit('cancelled')
}

// 确认上传
async function handleConfirm() {
  if (props.pendingFiles.length === 0) return

  try {
    const formData = new FormData()
    props.pendingFiles.forEach(file => formData.append('photos', file.rawFile || file))
    if (props.selectedTags.length > 0)
      formData.append('tags', JSON.stringify(props.selectedTags))
    formData.append('date', props.selectedDate || formatDate(new Date()))

    const response = await axios.post(props.uploadUrl, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })

    alert(`成功上传 ${props.pendingFiles.length} 张照片！`)
    console.log('服务器响应:', response.data)

    // 上传完成后清空父组件状态
    handleCancel()
    emit('uploaded', response.data)

  } catch (err) {
    console.error('上传失败:', err)
    emit('error', err)
    alert('上传失败，请重试')
  }
}
</script>

<style scoped>
.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 12px;
}

.cancel-btn {
  padding: 8px 16px;
  background-color: #f0f0f0;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.cancel-btn:hover:not(:disabled) {
  background-color: #e0e0e0;
}

.cancel-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.confirm-btn {
  padding: 8px 16px;
  background-color: #42b983;
  color: white;
 border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.confirm-btn:hover:not(:disabled) {
  background-color: #3aa876;
}

.confirm-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
