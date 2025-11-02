<template>
  <div>
    <el-card class="blog-publish-card" shadow="hover">
      <div class="input-section">
        <el-input
          placeholder="请输入标题（必填）"
          v-model="title"
          maxlength="100"
          show-word-limit
          class="title-input"
        />
      </div>
      <div class="input-section">
        <el-input
          type="textarea"
          :rows="5"
          placeholder="请输入内容"
          v-model="content"
          resize="none"
          class="content-input"
        />
      </div>
    </el-card>
  </div>
  <el-card class = "test-card"> </el-card>
  <el-card class = "test-card"> </el-card>
  <el-card class = "test-card"> </el-card>
</template>

<script setup>  
import { ref } from 'vue'

const title = ref('')
const content = ref('')
</script>

<style scoped>
.test-card {
  height: 400px;
}
</style>