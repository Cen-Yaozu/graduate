<template>
  <div class="calendar-manage">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>校历管理</span>
          <el-button type="primary" @click="handleAdd">添加日程</el-button>
        </div>
      </template>

      <el-calendar v-model="currentDate">
        <template #dateCell="{ data }">
          <div class="calendar-cell">
            <p>{{ data.day.split('-').slice(2).join('') }}</p>
            <div class="event-list">
              <div
                v-for="event in getEventsForDate(data.day)"
                :key="event.id"
                class="event-item"
                :class="event.type"
                @click="handleEdit(event)"
              >
                {{ event.title }}
              </div>
            </div>
          </div>
        </template>
      </el-calendar>
    </el-card>

    <!-- 添加/编辑日程对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加日程' : '编辑日程'"
      width="500px"
    >
      <el-form :model="eventForm" :rules="rules" ref="eventFormRef" label-width="100px">
        <el-form-item label="日程标题" prop="title">
          <el-input v-model="eventForm.title" placeholder="请输入日程标题" />
        </el-form-item>
        <el-form-item label="日程类型" prop="type">
          <el-select v-model="eventForm.type" placeholder="请选择日程类型" style="width: 100%">
            <el-option label="开学" value="start" />
            <el-option label="放假" value="holiday" />
            <el-option label="考试" value="exam" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期" prop="date">
          <el-date-picker
            v-model="eventForm.date"
            type="date"
            placeholder="请选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="eventForm.description"
            type="textarea"
            placeholder="请输入日程描述"
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

// 日历数据
const calendarData = ref([])

// 获取日历数据
const getCalendarData = async () => {
  try {
    // TODO: 调用后端API获取日历数据
    // const res = await getCalendarDataAPI()
    // calendarData.value = res.data
  } catch (error) {
    console.error('获取日历数据失败：', error)
    ElMessage.error('获取日历数据失败')
  }
}

// 初始化获取数据
getCalendarData()

// 日历相关
const currentDate = ref(new Date())

// 事件列表（模拟数据）
const events = ref([
  {
    id: 1,
    title: '开学',
    type: 'start',
    date: '2024-02-26',
    description: '2024年春季学期开学'
  },
  {
    id: 2,
    title: '期中考试',
    type: 'exam',
    date: '2024-04-15',
    description: '2024年春季学期期中考试'
  }
])

// 对话框相关
const dialogVisible = ref(false)
const dialogType = ref('add')
const eventFormRef = ref(null)
const eventForm = ref({
  title: '',
  type: 'other',
  date: '',
  description: ''
})

// 表单验证规则
const rules = {
  title: [{ required: true, message: '请输入日程标题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择日程类型', trigger: 'change' }],
  date: [{ required: true, message: '请选择日期', trigger: 'change' }],
  description: [{ required: true, message: '请输入日程描述', trigger: 'blur' }]
}

// 获取指定日期的事件
const getEventsForDate = (date) => {
  return calendarData.value.filter(event => event.date === date) || []
}

// 添加日程
const handleAdd = () => {
  dialogType.value = 'add'
  eventForm.value = {
    title: '',
    type: 'other',
    date: '',
    description: ''
  }
  dialogVisible.value = true
}

// 编辑日程
const handleEdit = (event) => {
  dialogType.value = 'edit'
  eventForm.value = { ...event }
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!eventFormRef.value) return

  await eventFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // TODO: 调用后端API提交表单
        // if (dialogType.value === 'add') {
        //   await addEventAPI(eventForm.value)
        // } else {
        //   await updateEventAPI(eventForm.value)
        // }
        ElMessage.success(dialogType.value === 'add' ? '添加成功' : '更新成功')
        dialogVisible.value = false
        // 模拟更新数据
        if (dialogType.value === 'add') {
          events.value.push({
            ...eventForm.value,
            id: events.value.length + 1
          })
        } else {
          const index = events.value.findIndex(e => e.id === eventForm.value.id)
          if (index !== -1) {
            events.value[index] = { ...eventForm.value }
          }
        }
      } catch (error) {
        console.error('提交表单失败：', error)
        ElMessage.error('提交失败')
      }
    }
  })
}
</script>

<style scoped>
.calendar-manage {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.calendar-cell {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.event-list {
  margin-top: 4px;
}

.event-item {
  padding: 2px 4px;
  margin: 2px 0;
  border-radius: 2px;
  font-size: 12px;
  cursor: pointer;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.event-item.start {
  background-color: #67c23a;
  color: white;
}

.event-item.holiday {
  background-color: #f56c6c;
  color: white;
}

.event-item.exam {
  background-color: #e6a23c;
  color: white;
}

.event-item.other {
  background-color: #909399;
  color: white;
}
</style>