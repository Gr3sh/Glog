<template>
  <div class="album-container">
    <!-- 上传区域 -->
    <UploadButton
      accept="image/*"
      button-text="选择照片" 
      @files-selected="handleFileChange"
    />
      <!-- 信息填写区域 -->
      <div class="info-section" v-if="pendingFiles.length > 0">
        <!-- 待发送照片区域 -->
        <div class="pending-area">
          <h3>待上传照片 ({{ pendingFiles.length }})</h3>
          <div class="pending-photos">
            <div v-for="(file, index) in pendingFiles" :key="file.id" class="pending-item">
              <img :src="file.previewUrl" :alt="file.name" />
              <div class="file-info">
                <span class="file-name">{{ truncateFileName(file.name) }}</span>
                <span class="file-size">{{ formatFileSize(file.size) }}</span>
              </div>
              <button class="remove-btn" @click="removePendingFile(index)">&times;</button>
            </div>
          </div>
        </div>

              <!-- 替换后的标签组件 -->
      <TagInput 
        v-model="selectedTags"
        :suggestions="availableTags"
        label="照片标签"
      />

        <!-- 日期选择 -->
        <div class="form-group">
          <label>上传日期</label>
          <input
            type="date"
            v-model="photoDate"
            class="date-input"
          />
        </div>

        <!-- 操作按钮 -->
        <div class="action-buttons">
          <button class="cancel-btn" @click="clearPendingFiles">取消</button>
          <button class="confirm-btn" @click="uploadFiles">上传全部</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import UploadButton from '@/components/UploadButton.vue'

export default {
  components: {
    UploadButton 
  },
  setup() {
    const handleFileChange = (files) => {
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
    }
    return {
      handleFileChange
    }
  }
}







import axios from 'axios'
import TagInput from '@/components/TagInput.vue' // 引入新组件


  setup() {
    // 待上传文件列表
    const pendingFiles = ref([])
    const fileInput = ref(null)
    
    // 标签功能
    const availableTags = ref(['风景', '人像', '美食', '旅行', '宠物', '家庭', '工作', '活动'])
    const selectedTags = ref([])
    const newTag = ref('')
    
    // 日期功能
    const photoDate = ref(new Date().toISOString().split('T')[0])

    // 触发文件选择
    const triggerUpload = () => {
      fileInput.value.click()
    }

    // 处理文件选择
    const handleFileChange = (e) => {
      const files = Array.from(e.target.files)
      if (files.length === 0) return
      
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
      e.target.value = ''
    }

    // 移除待上传文件
    const removePendingFile = (index) => {
      URL.revokeObjectURL(pendingFiles.value[index].previewUrl)
      pendingFiles.value.splice(index, 1)
    }

    // 清空待上传列表
    const clearPendingFiles = () => {
      pendingFiles.value.forEach(file => {
        URL.revokeObjectURL(file.previewUrl)
      })
      pendingFiles.value = []
      selectedTags.value = []
      photoDate.value = new Date().toISOString().split('T')[0]
    }

    // 切换标签选择
    const toggleTag = (tag) => {
      const index = selectedTags.value.indexOf(tag)
      if (index >= 0) {
        selectedTags.value.splice(index, 1)
      } else {
        selectedTags.value.push(tag)
      }
    }

    // 添加新标签
    const addNewTag = () => {
      if (newTag.value.trim() && !availableTags.value.includes(newTag.value.trim())) {
        availableTags.value.push(newTag.value.trim())
        selectedTags.value.push(newTag.value.trim())
        newTag.value = ''
      }
    }

    // 文件名截断显示
    const truncateFileName = (name) => {
      if (name.length > 20) {
        return name.substring(0, 15) + '...' + name.split('.').pop()
      }
      return name
    }

    // 文件大小格式化
    const formatFileSize = (bytes) => {
      if (bytes === 0) return '0 Bytes'
      const k = 1024
      const sizes = ['Bytes', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    }

    // 上传文件方法
    const uploadFiles = async () => {
      if (pendingFiles.value.length === 0) return
      
      const formData = new FormData()
      pendingFiles.value.forEach(file => {
        formData.append('files', file.rawFile)
      })
      
      // 添加标签和日期到表单数据
      if (selectedTags.value.length > 0) {
        formData.append('tags', selectedTags.value.join(','))
      }
      formData.append('date', photoDate.value)

      try {
        const res = await axios.post('http://localhost:8080/photos/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        console.log('上传成功:', res.data)
        alert('上传成功 ' + res.data.length + ' 张照片！')
        clearPendingFiles()
      } catch (err) {
        console.error('上传失败:', err)
        alert('上传失败，请重试！')
      }
    }

    return {
      pendingFiles,
      fileInput,
      availableTags,
      selectedTags,
      newTag,
      photoDate,
      triggerUpload,
      handleFileChange,
      removePendingFile,
      clearPendingFiles,
      toggleTag,
      addNewTag,
      truncateFileName,
      formatFileSize,
      uploadFiles
    }
  }
}
</script>

<style scoped>
.album-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.upload-section {
  margin-bottom: 30px;
}

.upload-btn {
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.upload-btn:hover {
  background-color: #3aa876;
}

.info-section {
  margin-top: 20px;
}

.pending-area {
  border: 1px dashed #ddd;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 20px;
}

.pending-area h3 {
  margin-top: 0;
  color: #555;
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

.form-group {
 margin-bottom: 20px;
}

.form-group label {
 display: block;
 margin-bottom: 8px;
 font-weight: 500;
 color: #555;
}

.tags-container {
 display: flex;
 flex-wrap: wrap;
 gap: 8px;
 align-items: center;
}

.tag {
 padding: 6px 12px;
 background-color: #f0f0f0;
 border-radius: 16px;
 cursor: pointer;
 font-size: 14px;
 transition: all 0.2s;
}

.tag:hover {
 background-color: #e0e0e0;
}

.tag.selected {
 background-color: #42b983;
 color: white;
}

.tag-input {
 padding: 6px 12px;
 border: 1px solid #ddd;
 border-radius: 16px;
 outline: none;
 font-size: 14px;
}

.date-input {
 padding: 8px 12px;
 border: 1px solid #ddd;
 border-radius: 4px;
 font-size: 14px;
}

.action-buttons {
 display: flex;
 justify-content: flex-end;
 gap: 10px;
 margin-top: 20px;
}

.cancel-btn {
 padding: 8px 16px;
 background-color: #f0f0f0;
 border: none;
 border-radius: 4px;
 cursor: pointer;
}

.confirm-btn {
 padding: 8px 16px;
 background-color: #42b983;
 color: white;
 border: none;
 border-radius: 4px;
 cursor: pointer;
}
</style>
