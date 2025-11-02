<template>
  <div class="date-picker-container">
    <label v-if="label" class="date-picker-label">{{ label }}</label>
    <input
      type="date"
      class="date-picker-input"
      :value="internalValue"
      @input="onInput"
      :min="minDate"
      :max="maxDate"
    />
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'

const props = defineProps({
  modelValue: {
    type: String,
    required: ''
  },
  label: {
    type: String,
    default: ''
  },
  minDate: {
    type: String,
    default: ''
  },
  maxDate: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])
const internalValue = ref(props.modelValue)

// 当父组件更新 modelValue 时同步
watch(() => props.modelValue, (val) => {
  internalValue.value = val
})

// input 事件
function onInput(event) {
  internalValue.value = event.target.value
  emit('update:modelValue', internalValue.value)
}

// 组件挂载时默认显示今天日期
onMounted(() => {
  if (!internalValue.value) {
    const today = new Date().toISOString().split('T')[0]
    internalValue.value = today
    emit('update:modelValue', today)
  }
})
</script>

<style scoped>
.date-picker-container {
  display: flex;
  flex-direction: column;
  margin-bottom: 16px;
}

.date-picker-label {
  margin-bottom: 6px;
  font-weight: 500;
  color: #000000; 
}

.date-picker-input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 16px; 
  font-size: 14px;
  background-color: #f9f9f9; 
  transition: all 0.2s ease;
  outline: none;
  width: 200px; 
}

.date-picker-input:hover {
  border-color: #aaa;
}

.date-picker-input:focus {
  border-color: #409eff; 
  box-shadow: 0 2px 6px rgba(64, 158, 255, 0.2);
  background-color: #fff;
}
</style>
