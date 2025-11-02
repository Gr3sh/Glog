<template>
  <div class="schedule-container">
    <!-- 年月导航 -->
    <div class="header">
      <el-button @click="prevMonth"><<</el-button>
      <span class="month-title">{{ currentYear }}年{{currentMonth}}月</span>
      <el-button @click="nextMonth">>></el-button>
    </div>

    <!-- 月任务 -->
    <el-card class="month-tasks">
        <div class="section-title">{{ currentMonth }}月任务</div>
        <div class="task-list-two">
            <div
                v-for="(task, mindex) in tasks"
                :key="mindex"
                class="task-item"
            >
                <el-checkbox v-model="task.done">{{ task.text }}</el-checkbox>
            </div>
            <el-input
                v-model="newTask"
                placeholder="添加代办"
                @keyup.enter="addTask"
                clearable
            />
        </div>
    </el-card>

    <!-- 周任务 -->
    <el-card class="week-tasks">
        <div class="section-title">周任务</div>

        <div class="week-grid">
            <el-card
                v-for="(week, windex) in 4"
                :key="windex"
                class="task-list-one"
                :shadow="selectedWeek === (windex) ? 'hover' : 'always'"
                @click="selectWeek(windex)"
            >
            <div class="week-title">第 {{ windex + 1 }} 周</div>
        
            <div class="week-tasks-list">
                <div
                    v-for="(task, tIdx) in weekTasks[windex]"
                    :key="tIdx"
                    class="task-item"
                >
                <el-checkbox v-model="task.done">{{ task.text }}</el-checkbox>
                </div>
                <div v-if="selectedWeek === (windex)" class="week-input-area">
                    <el-input
                        v-model="newWeekTask[windex]"
                        placeholder="添加周任务"
                        @keyup.enter="addWeekTask(windex)"
                        clearable
                        :ref="el => (weekTaskInput.value[windex] = el)"
                    />
                </div>
            </div>
            </el-card>
        </div>
    </el-card>

    <!-- 日任务 -->
  </div>
</template>

<script setup>
import { ref, computed, reactive, nextTick } from 'vue'
import { ElButton, ElCard, ElInput, ElCheckbox, ElCheckboxGroup } from 'element-plus'

const selectedDate = ref(new Date())
const currentYear = computed(() => selectedDate.value.getFullYear())
const currentMonth = computed(() => selectedDate.value.getMonth() + 1)

const prevMonth = () => {
  const d = new Date(selectedDate.value)
  d.setMonth(d.getMonth() - 1)
  selectedDate.value = d
}
const nextMonth = () => {
  const d = new Date(selectedDate.value)
  d.setMonth(d.getMonth() + 1)
  selectedDate.value = d
}

// 月任务
const newTask = ref("")
const tasks = ref([])
const addTask = () => {
  if (newTask.value.trim() !== "") {
    tasks.value.push({
      text: newTask.value,
      done: false,
    })
    newTask.value = ""
  }
}
//周任务
const selectedWeek = ref(null)
const weekTasks = ref([[], [], [], []])
const newWeekTask = ref(['', '', '', ''])
const weekTaskInput = ref([])
const selectWeek = (windex) => {
    selectedWeek.value = windex
    nextTick(() => {
        weekTaskInput.value[windex]?.focus()
    })
}
const addWeekTask = (idx) => {
  const text = (newWeekTask.value[idx] || '').trim()
  if (!text) return
  weekTasks.value[idx].push({ text, done: false })
  newWeekTask.value[idx] = ''
}
</script>

<style scoped>
.schedule-container {
  margin: 20px 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.header {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  font-size: 20px;
}

.month-tasks, .week-tasks, .day-plan {
  border-radius: 8px;
}
.month-tasks {
  background-color: #f0f9ff;
}
.week-tasks {
  background-color: #e8f0fe;
}
.day-tasks {
  background-color: #fff4e8;
}

.section-title {
  font-size: 20px;
  font-weight: bold;
}
.task-list-two {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 20px;
  margin-top: 10px;
}
.task-list-one {
  display: grid;
  gap: 0 20px;
  margin-top: 10px;
}
.task-item {
  display: flex;
  align-items: center;
  transition: 0.25s;
}
::v-deep(.el-checkbox.is-checked .el-checkbox__label) {
  text-decoration: line-through;
  color: #999;
  transition: color 0.2s, text-decoration 0.2s;
}
.task-item:hover {
  background: #f9f9f9;
  border-radius: 4px;
  padding-left: 5px;
}

.week-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}
</style>
