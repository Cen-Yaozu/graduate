<template>
  <div class="calendar-container">
    <el-card class="calendar-card">
      <template #header>
        <div class="card-header">
          <span>校园日历</span>
          <div class="calendar-actions">
          <el-radio-group v-model="calendarView" size="small">
            <el-radio-button label="month">月视图</el-radio-button>
            <el-radio-button label="week">周视图</el-radio-button>
          </el-radio-group>
            <el-button type="primary" size="small" @click="openAddEventDialog">添加事件</el-button>
          </div>
        </div>
      </template>
      
      <el-calendar v-if="calendarView === 'month'" ref="calendar">
        <template #dateCell="{ data }">
          <div class="calendar-cell">
            <p :class="data.isSelected ? 'is-selected' : ''">
              {{ data.day.split('-').slice(2).join('-') }}
            </p>
            <div class="event-list">
              <div 
                v-for="(event, index) in getEventsForDate(data.day)" 
                :key="index"
                class="event-item"
                :class="event.type"
                @click="handleEventClick(event)"
              >
                {{ event.title }}
              </div>
            </div>
          </div>
        </template>
      </el-calendar>
      
      <div v-else class="week-view">
        <el-calendar ref="weekCalendar" :range="weekRange">
          <template #dateCell="{ data }">
            <div class="calendar-cell">
              <p :class="data.isSelected ? 'is-selected' : ''">
                {{ data.day.split('-').slice(2).join('-') }}
              </p>
              <div class="event-list">
                <div 
                  v-for="(event, index) in getEventsForDate(data.day)" 
                  :key="index" 
                  class="event-item"
                  :class="event.type"
                  @click="handleEventClick(event)"
                >
                  {{ event.title }}
                </div>
              </div>
        </div>
          </template>
        </el-calendar>
      </div>
    </el-card>
    
    <!-- 添加事件对话框 -->
    <el-dialog v-model="dialogVisible" title="添加校园事件" width="500px">
      <el-form :model="eventForm" label-width="100px" :rules="rules" ref="eventFormRef">
        <el-form-item label="事件标题" prop="title">
          <el-input v-model="eventForm.title" placeholder="请输入事件标题"></el-input>
        </el-form-item>
        <el-form-item label="事件日期" prop="date">
          <el-date-picker 
            v-model="eventForm.date" 
            type="date" 
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="事件类型" prop="type">
          <el-select v-model="eventForm.type" placeholder="请选择事件类型">
            <el-option label="普通事件" value="normal"></el-option>
            <el-option label="重要事件" value="important"></el-option>
            <el-option label="考试事件" value="exam"></el-option>
            <el-option label="假期事件" value="holiday"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="事件内容" prop="content">
          <el-input 
            v-model="eventForm.content" 
            type="textarea" 
            :rows="4" 
            placeholder="请输入事件详细内容"
          ></el-input>
        </el-form-item>
        <el-form-item label="适用对象" prop="target">
          <el-checkbox-group v-model="eventForm.target">
            <el-checkbox label="all">全体师生</el-checkbox>
            <el-checkbox label="teacher">教师</el-checkbox>
            <el-checkbox label="student">学生</el-checkbox>
            <el-checkbox label="freshman">新生</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveEvent">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 事件详情对话框 -->
    <el-dialog v-model="eventDetailVisible" :title="currentEvent.title" width="500px">
      <div class="event-detail">
        <p><strong>日期:</strong> {{ currentEvent.date }}</p>
        <p><strong>类型:</strong> {{ getEventTypeName(currentEvent.type) }}</p>
        <p><strong>适用对象:</strong> {{ getEventTargets(currentEvent.target) }}</p>
        <p><strong>内容:</strong></p>
        <div class="event-content">{{ currentEvent.content }}</div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="eventDetailVisible = false">关闭</el-button>
          <el-button type="warning" @click="openEditDialog">编辑</el-button>
          <el-button type="danger" @click="deleteEvent">删除</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'CalendarView',
  setup() {
    const calendarView = ref('month')
    const dialogVisible = ref(false)
    const eventDetailVisible = ref(false)
    const eventFormRef = ref(null)
    const currentEvent = ref({})
    const currentDate = ref(new Date())
    const isEditMode = ref(false)

    // 模拟后端数据
    const events = ref([
        {
          id: 1,
          title: '新生入学报到',
        date: '2024-09-01',
        type: 'important',
        content: '所有2024级新生在各系报到点进行入学报到手续办理，请携带录取通知书和相关证件。',
        target: ['freshman']
        },
        {
          id: 2,
        title: '开学典礼',
        date: '2024-09-03',
        type: 'normal',
        content: '全校开学典礼在大礼堂举行，请各班负责人带领学生准时参加。',
        target: ['all']
        },
        {
          id: 3,
        title: '期中考试',
        date: '2024-10-25',
        type: 'exam',
        content: '上学期期中考试，各科考试安排将在考试前一周公布。',
        target: ['student']
        },
        {
          id: 4,
        title: '国庆节放假',
        date: '2024-10-01',
        type: 'holiday',
        content: '国庆节放假时间：10月1日至10月7日，10月8日正常上课。',
        target: ['all']
      }
    ])

    const eventForm = reactive({
      id: null,
      title: '',
      date: '',
      type: 'normal',
      content: '',
      target: ['all']
    })

    const rules = {
      title: [{ required: true, message: '请输入事件标题', trigger: 'blur' }],
      date: [{ required: true, message: '请选择事件日期', trigger: 'change' }],
      type: [{ required: true, message: '请选择事件类型', trigger: 'change' }],
      content: [{ required: true, message: '请输入事件内容', trigger: 'blur' }],
      target: [{ required: true, message: '请选择适用对象', trigger: 'change' }]
    }

    // 计算当前周的起止日期
    const weekRange = computed(() => {
      const start = new Date(currentDate.value)
      const day = start.getDay() || 7
      start.setDate(start.getDate() - day + 1)
      const end = new Date(start)
      end.setDate(end.getDate() + 6)
      
      return [start, end]
    })

    // 根据日期获取事件
    const getEventsForDate = (date) => {
      return events.value.filter(event => event.date === date)
    }

    // 打开添加事件对话框
    const openAddEventDialog = () => {
      isEditMode.value = false
      resetForm()
      dialogVisible.value = true
    }

    // 保存事件
    const saveEvent = () => {
      eventFormRef.value.validate((valid) => {
        if (valid) {
          if (isEditMode.value) {
            // 更新现有事件
            const index = events.value.findIndex(e => e.id === eventForm.id)
            if (index !== -1) {
              events.value[index] = { ...eventForm }
              ElMessage.success('事件更新成功')
              // 保存到后端
              saveEventToServer(events.value[index])
            }
          } else {
            // 添加新事件
            const newEvent = { ...eventForm, id: Date.now() }
            events.value.push(newEvent)
            ElMessage.success('事件添加成功')
            // 保存到后端
            saveEventToServer(newEvent)
          }
          dialogVisible.value = false
          eventDetailVisible.value = false
        } else {
          return false
        }
      })
    }

    // 处理事件点击
    const handleEventClick = (event) => {
      currentEvent.value = event
      eventDetailVisible.value = true
    }

    // 打开编辑对话框
    const openEditDialog = () => {
      Object.assign(eventForm, currentEvent.value)
      isEditMode.value = true
      eventDetailVisible.value = false
      dialogVisible.value = true
    }

    // 删除事件
    const deleteEvent = () => {
      ElMessageBox.confirm('确定要删除这个事件吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const index = events.value.findIndex(e => e.id === currentEvent.value.id)
        if (index !== -1) {
          events.value.splice(index, 1)
          ElMessage.success('事件已删除')
          eventDetailVisible.value = false
        }
      }).catch(() => {})
    }

    // 重置表单
    const resetForm = () => {
      eventForm.id = null
      eventForm.title = ''
      eventForm.date = ''
      eventForm.type = 'normal'
      eventForm.content = ''
      eventForm.target = ['all']
      if (eventFormRef.value) {
        eventFormRef.value.resetFields()
      }
    }

    // 获取事件类型名称
    const getEventTypeName = (type) => {
      const typeMap = {
        normal: '普通事件',
        important: '重要事件',
        exam: '考试事件',
        holiday: '假期事件'
      }
      return typeMap[type] || type
    }

    // 获取事件适用对象名称
    const getEventTargets = (targets) => {
      if (!targets || targets.length === 0) return ''
      
      const targetMap = {
        all: '全体师生',
        teacher: '教师',
        student: '学生',
        freshman: '新生'
      }
      
      return targets.map(target => targetMap[target] || target).join('、')
    }

    // 获取事件数据
    const fetchEvents = async () => {
      try {
        // 实际项目中，这里应该调用API获取事件数据
        // const response = await axios.get('/api/admin/calendar/events')
        // events.value = response.data
      } catch (error) {
        console.error('获取日历事件失败:', error)
        ElMessage.error('获取日历事件失败')
      }
    }

    // 保存事件到后端
    const saveEventToServer = async (eventData) => {
      try {
        // 实际项目中，这里应该调用API保存事件数据
        console.log('保存事件到后端:', eventData)
        // const response = await axios.post('/api/admin/calendar/events', eventData)
        // return response.data
      } catch (error) {
        console.error('保存事件失败:', error)
        ElMessage.error('保存事件失败')
        return null
      }
    }

    onMounted(() => {
      fetchEvents()
    })

    return {
      calendarView,
      dialogVisible,
      eventDetailVisible,
      eventFormRef,
      currentEvent,
      weekRange,
      events,
      eventForm,
      rules,
      getEventsForDate,
      openAddEventDialog,
      saveEvent,
      handleEventClick,
      openEditDialog,
      deleteEvent,
      getEventTypeName,
      getEventTargets
    }
  }
}
</script>

<style scoped>
.calendar-container {
  padding: 20px;
}

.calendar-card {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.calendar-actions {
  display: flex;
  gap: 10px;
}

.calendar-cell {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.is-selected {
  color: #1989fa;
  font-weight: bold;
}

.event-list {
  flex: 1;
  overflow-y: auto;
  padding-top: 4px;
}

.event-item {
  margin-bottom: 4px;
  padding: 2px 4px;
  font-size: 12px;
  border-radius: 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
  color: #fff;
  background-color: #409eff;
}

.event-item.important {
  background-color: #f56c6c;
}

.event-item.exam {
  background-color: #e6a23c;
}

.event-item.holiday {
  background-color: #67c23a;
}

.event-item:hover {
  opacity: 0.8;
}

.event-detail {
  line-height: 1.8;
}

.event-content {
  margin-top: 8px;
  padding: 12px;
  background-color: #f8f8f8;
  border-radius: 4px;
  white-space: pre-wrap;
}

.week-view {
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>