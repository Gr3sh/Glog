<template>
  <div class="tag-input-container">
    <label v-if="label">{{ label }}</label>
    <div class="tags-display">
      <span v-for="(tag, index) in modelValue" :key="index" class="tag">
        {{ tag }}
        <span class="tag-remove" @click.stop="removeTag(index)">&times;</span>
      </span>

      <div 
        class="input-container" 
        :class="{ expanded: isExpanded }"
        :style="{ width: inputWidth }"
      >
        <button 
          v-if="!isExpanded" 
          class="add-button" 
          @click="expandInput"
        >
          <span class="plus-icon">+</span>
        </button>
        
        <input
          v-show="isExpanded"
          ref="inputRef"
          type="text"
          v-model="inputValue"
          @keydown.enter.prevent="addTag"
          @input="handleInputChange"
          @blur="shrinkInput"
          class="tag-input"
        />
      </div>
    </div>

    <div 
      v-if="showSuggestions && filteredSuggestions.length" 
      class="tag-suggestions"
    >
      <div
        v-for="(suggestion, index) in filteredSuggestions"
        :key="index"
        @mousedown.prevent="selectSuggestion(suggestion)"
        class="suggestion-item"
      >
        {{ suggestion }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, nextTick} from 'vue'
import axios from 'axios'

const props = defineProps({
  modelValue: {type: Array, default: () => []},
  label: {type: String, default: '标签'}
})

const emit = defineEmits(['update:modelValue'])

const inputRef = ref(null)
const inputValue = ref('')
const isExpanded = ref(false)
const showSuggestions = ref(false)
const suggestionTags = ref([])
const inputWidth = ref('32px')

async function fetchTags() {
  try {
    const res = await axios.get('http://localhost:8080/tags') 
    suggestionTags.value = res.data
      .sort((a, b) => b.usageCount - a.usageCount) 
      .map(item => item.tagName)
  } catch (err) {
    console.error('获取标签失败', err)
  }
}

const filteredSuggestions = computed(() => {
  return suggestionTags.value
    .filter(tag => !props.modelValue.includes(tag))
    .filter(tag => tag.toLowerCase().includes(inputValue.value.toLowerCase()))
    .slice(0, 10)
})

// 展开输入框
const expandInput = async () => {
  await fetchTags()
  isExpanded.value = true
  showSuggestions.value = true
  await nextTick()
  inputRef.value.focus()
}

// 收缩输入框
const shrinkInput = () => {
  const tag = inputValue.value.trim() 
  if (tag && !props.modelValue.includes(tag)) {
    emit('update:modelValue', [...props.modelValue, tag])
  }

  inputValue.value = ''
  isExpanded.value = false
  showSuggestions.value = false
  inputWidth.value = '32px'
}

// 动态调整输入框宽度
const handleInputChange = (e) => {
  if (e.target.value.length > 10) {
    e.target.value = e.target.value.slice(0, 20) 
  }
  const minWidth = 32 
  const padding = 12
  const text = e.target.value || ''
  const fakeSpan = document.createElement('span')
  fakeSpan.style.visibility = 'hidden'
  fakeSpan.style.whiteSpace = 'pre'
  fakeSpan.style.font = window.getComputedStyle(e.target).font
  fakeSpan.textContent = text
  document.body.appendChild(fakeSpan)
  const width = Math.max(minWidth, fakeSpan.getBoundingClientRect().width + padding)
  document.body.removeChild(fakeSpan)
  
  inputWidth.value = `${width}px`
}

const addTag = () => {
  const tag = inputValue.value.trim()
  if (tag && !props.modelValue.includes(tag)) {
    emit('update:modelValue', [...props.modelValue, tag])
    inputValue.value = ''
    handleInputChange({ target: inputRef.value })
  }
}

const removeTag = (index) => {
  const newTags = [...props.modelValue]
  newTags.splice(index, 1)
  emit('update:modelValue', newTags)
}

const selectSuggestion = (suggestion) => {
  if (!props.modelValue.includes(suggestion)) {
    emit('update:modelValue', [...props.modelValue, suggestion])
  }
  inputValue.value = ''
  shrinkInput()
}
</script>

<style scoped>
.tag-input-container {
  margin-bottom: 16px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
}

.tags-display {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

.tag {
  display: inline-flex;
  align-items: center;
  padding: 4px 12px;
  background-color: #f0f0f0;
  border-radius: 16px;
  font-size: 14px;
}

.tag-remove {
  margin-left: 6px;
  cursor: pointer;
  color: #999;
  transition: all 0.2s;
}

.tag-remove:hover {
  color: #ff4444;
}

.input-container {
  position: relative;
  height: 32px;
  transition: width 0.2s ease;
}

.input-container input {
  width: 100%;
  height: 32px;
  border: 1px solid #ddd;
  border-radius: 16px;
  padding: 0 12px;
  outline: none;
  transition: width 0.4s ease;
}

.add-button {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  border: 1px dashed #ccc;
  background: transparent;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.add-button:hover {
  border-color: #888;
  background-color: #f5f5f5;
}

.plus-icon {
  font-size: 18px;
  color: #666;
}

.tag-suggestions {
  margin-top: 4px;
  border: 1px solid #eee;
  border-radius: 4px;
  max-height: 150px;
  overflow-y: auto;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.suggestion-item {
  padding: 8px 12px;
 cursor: pointer;
}

.suggestion-item:hover {
  background-color: #f5f5f5;
}
</style>
