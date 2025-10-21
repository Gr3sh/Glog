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
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  accept: {
    type: String,
    default: '*/*'
  },
  buttonText: {
    type: String,
    default: '选择文件'
  },
  multiple: {
    type: Boolean,
    default: true
  },
  modelValue: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['update:modelValue']) 
const fileInput = ref(null)
const pendingFiles = ref([])

// 如果父组件传入初始值 
watch( () => props.modelValue, (newVal) => 
  { pendingFiles.value = newVal },
  { immediate: true, deep: true } 
)

const triggerInput = () => {
  fileInput.value.click()
}

const handleChange = (e) => {
  const files = Array.from(e.target.files)
  files.forEach(file => {
    const previewUrl = URL.createObjectURL(file)
    pendingFiles.value.push({
      id: Date.now() + Math.random(),
      name: file.name,
      size: file.size,
      rawFile: file,
      previewUrl
    })
  })
  emit('update:modelValue', pendingFiles.value)
  e.target.value = '' // 重置 input
}

const removeFile = (id) => {
  const index = pendingFiles.value.findIndex(f => f.id === id)
  if (index !== -1) {
    URL.revokeObjectURL(pendingFiles.value[index].previewUrl)
    pendingFiles.value.splice(index, 1)
    emit('update:modelValue', pendingFiles.value)
  }
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
</style>
