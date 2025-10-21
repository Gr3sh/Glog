<template>
  <el-header>
    <el-menu 
      :default-active="1"    
      class="el-menu-header" 
      mode="horizontal"
      router
    >
      <el-menu-item index="/">首页</el-menu-item>
      <el-menu-item index="/essay">博客</el-menu-item>
      <el-menu-item index="/prose">随笔</el-menu-item>
      <el-sub-menu index="/album">
        <template #title>相册</template>
        <el-menu-item index="/album/upload">上传</el-menu-item>
        <el-menu-item index="/album/select">筛选</el-menu-item>
      </el-sub-menu>
      <el-menu-item index="/calendar">日程</el-menu-item>
    </el-menu>
  </el-header>
  
  <el-row justify="space-around">
    <el-col :span="6" class="aside">
      <div>
        <div class="custom-card-avatar"
          @mouseenter="animateIn" 
          @mouseleave="animateOut">
          <el-avatar :size="80" :src="avatarUrl" :class="rollClass" />
        </div>
        <el-card class="custom-card" :class="moveClass">
          card
        </el-card>
      </div>
    </el-col>

    <el-col :span="12" class="main">
      <router-view />
    </el-col>

    <el-col :span="6" class="right">
      <div class="calendar">
        <el-calendar v-model="value" />
      </div>
    </el-col>
  </el-row>
</template>

<script lang="ts" setup>
  import avatarUrl from './assets/avatar.jpg'
  import { ref } from 'vue'

  const value = ref(new Date())
  const rollClass = ref('')
  const moveClass = ref('')
  const animateIn = () => { rollClass.value = 'roll-in'; moveClass.value = 'move-in' }
  const animateOut = () => { rollClass.value = 'roll-out'; moveClass.value = 'move-out' }
</script>

<style scoped>
.aside {
  background-color: #f5f5f5;
  min-height: 100vh;
  display: flex;
  justify-content: right;
  padding-right: 20px;
}
.custom-card-avatar {
  z-index: 10;
  position: sticky;
  top: 40px;
  margin: 40px 0 -40px;
  display: flex;
  justify-content: center;
}
.custom-card {
  position: sticky;
  top: 90px;
  z-index: 9;
  width: 240px;
  height: 120px;
  background: #fff;
  border-radius: 4px;
  display: flex;
  align-items: flex-end;
  justify-content: center;
}

.roll-in { animation: rollZoomIn 1.2s ease forwards; }
.roll-out { animation: rollZoomOut 1.2s ease forwards; }
.move-in { animation: moveZoomIn 1.2s ease forwards; }
.move-out { animation: moveZoomOut 1.2s ease forwards; }

@keyframes rollZoomIn {
  0% { transform: rotate(0deg) scale(1) translateY(0);}
  100% { transform: rotate(360deg) scale(2) translateY(20px);}
}
@keyframes rollZoomOut {
  0% { transform: rotate(0deg) scale(2) translateY(20px);}
  100% { transform: rotate(360deg) scale(1) translateY(0);}
}
@keyframes moveZoomIn {
  0% { transform:translateY(0);}
  100% { transform:translateY(60px);}
}
@keyframes moveZoomOut {
  0% { transform:translateY(60px);}
  100% { transform:translateY(0);}
}


.main {
  background-color: #e2e2e2;
  min-height: 100vh;
  padding: 20px 20px 0 20px;
}

.right {
  background-color: #f5f5f5;
  padding-top: 40px;
}
.calendar {
  padding: 20px;
  border-radius: 4px;
}
</style>

