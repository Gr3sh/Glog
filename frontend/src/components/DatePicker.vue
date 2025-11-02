<template>
  <div class="date-picker-container">
    <label v-if="label" class="date-picker-label">{{ label }}</label>
    <input
      type="date"
      class="date-picker-input"
      v-model="localValue"
      @input="onInput"
      :min="minDate"
      :max="maxDate"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const props = defineProps({
  modelValue: String,
  label: { type: String, default: '' },
  minDate: { type: String, default: '' },
  maxDate: { type: String, default: '' }
})
const emit = defineEmits(['update:modelValue'])
const localValue = ref(props.modelValue)

function onInput(event) {
  localValue.value = event.target.value
  emit('update:modelValue', localValue.value)
}

// 组件挂载时默认显示今天日期
onMounted(() => {
  if (!localValue.value) {
    const today = new Date().toISOString().split('T')[0]
    localValue.value = today
    emit('update:modelValue', today)
  }
})
</script>

<style scoped>
.date-picker-container {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 16px;
}

.date-picker-label {
  font-weight: 500;
  color: #333;
}

.date-picker-input {
  padding: 8px 12px;
  width: 200px;
  border: 1px solid #ddd;
  border-radius: 16px;
  font-size: 14px;
  background: #fafafa;
  outline: none;
  transition: all 0.2s ease;
}

.date-picker-input:hover {
  border-color: #bbb;
}

.date-picker-input:focus {
  border-color: #409eff;
  box-shadow: 0 0 4px rgba(64, 158, 255, 0.3);
  background: #fff;
}
</style>
