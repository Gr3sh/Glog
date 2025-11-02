<template>
  <div class="album-container">
    <UploadButton v-model="pendingFiles" accept="image/*" button-text="选择照片" />
    <TagInput 
      ref="tagsInputRef" 
      v-show="pendingFiles.length > 0" 
      v-model="selectedTags" 
      label="标签"
    />
    <DatePicker v-show="pendingFiles.length > 0" v-model="selectedDate" label="日期"/> 
    <ActionButtons
      v-show="pendingFiles.length > 0"
      v-model:pendingFiles="pendingFiles"
      v-model:selectedTags="selectedTags"
      v-model:selectedDate="selectedDate"
      :uploadUrl="uploadUrl"
      @uploaded="onUploaded"
    />
  </div>

  <div>
    <PhotoDisplay ref="photoDisplayRef" />
  </div>
</template>

<script>
import { ref } from 'vue'
import axios from 'axios';

import UploadButton from '@/components/Album/UploadButton.vue';
import TagInput from '@/components/Album/TagInput.vue';
import DatePicker from '@/components/DatePicker.vue';
import ActionButtons from '@/components/Album/ActionButtons.vue';
import PhotoDisplay from '@/components/Album/PhotoDisplay.vue';

export default {
  components: {
    UploadButton,
    TagInput,
    DatePicker,
    ActionButtons,
    PhotoDisplay
  },
  setup() {
    const pendingFiles = ref([])
    const selectedTags = ref([])
    const selectedDate = ref('')
    const uploadUrl = ref('http://localhost:8080/photos/upload')
    const photoDisplayRef = ref(null)
    // 上传成功后的回调
    const onUploaded = (responseData) => {
      photoDisplayRef.value?.fetchPhotos()
    }

    return {
      pendingFiles,
      selectedTags,
      selectedDate,
      uploadUrl,
      photoDisplayRef,
      onUploaded
    }
  }
}
</script>