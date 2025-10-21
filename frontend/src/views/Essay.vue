<template>
  <div class="album-container">
    <div class="upload-section">
      <UploadButton 
        accept="image/*"
        button-text="选择照片"
        @files-selected="handleFileChange"
      />
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
</script>