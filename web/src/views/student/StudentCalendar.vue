<template>
  <div class="timeline-container">
    <el-card class="timeline-card">
      <template #header>
        <div class="card-header">
          <span>校园重要日程</span>
          <div class="event-legend">
            <span class="legend-item">
              <span class="color-dot important"></span>重要事件
            </span>
            <span class="legend-item">
              <span class="color-dot exam"></span>考试
            </span>
            <span class="legend-item">
              <span class="color-dot holiday"></span>假期
            </span>
          </div>
        </div>
      </template>
      
      <el-timeline>
        <el-timeline-item
          v-for="event in sortedEvents"
          :key="event.id"
          :type="event.type"
          :color="getEventColor(event.type)"
          :timestamp="formatDate(event.date)"
        >
          <div class="timeline-content" @click="handleEventClick(event)">
            <h4 class="event-title">{{ event.title }}</h4>
            <p class="event-target">适用对象：{{ getEventTargets(event.target) }}</p>
          </div>
        </el-timeline-item>
      </el-timeline>
    </el-card>
    
    <!-- 事件详情对话框 -->
    <el-dialog v-model="eventDetailVisible" :title="currentEvent.title" width="500px">
      <div class="event-detail">
        <p><strong>日期:</strong> {{ formatDateRange(currentEvent) }}</p>
        <p><strong>类型:</strong> {{ getEventTypeName(currentEvent.type) }}</p>
        <p><strong>适用对象:</strong> {{ getEventTargets(currentEvent.target) }}</p>
        <p><strong>内容:</strong></p>
        <div class="event-content">{{ currentEvent.content }}</div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="eventDetailVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed } from 'vue'

export default {
  name: 'StudentCalendar',
  setup() {
    const eventDetailVisible = ref(false)
    const currentEvent = ref({})

    // 固定的校历事件数据
    const events = ref([
      {
        id: 1,
        title: '新生入学报到',
        date: '2024-09-07',
        type: 'important',
        content: '2024级新生在各系报到点进行入学报到手续办理，请携带录取通知书和相关证件。',
        target: ['freshman']
      },
      {
        id: 2,
        title: '新生入学教育',
        date: '2024-09-08',
        type: 'important',
        content: '2024级新生入学教育活动，时间：9月8日-10日。',
        endDate: '2024-09-10',
        target: ['freshman']
      },
      {
        id: 3,
        title: '开学典礼暨军训动员大会',
        date: '2024-09-11',
        type: 'important',
        content: '2024级新生开学典礼暨军训动员大会，请准时参加。',
        target: ['freshman']
      },
      {
        id: 4,
        title: '新生军训',
        date: '2024-09-11',
        type: 'important',
        content: '2024级新生军训时间：9月11日-24日，请按照各系通知准时参加。',
        endDate: '2024-09-24',
        target: ['freshman']
      },
      {
        id: 5,
        title: '国庆节放假',
        date: '2024-10-01',
        type: 'holiday',
        content: '国庆节放假时间：10月1日至10月7日，10月8日正常上课。',
        endDate: '2024-10-07',
        target: ['all']
      },
      {
        id: 6,
        title: '期中考试',
        date: '2024-10-25',
        type: 'exam',
        content: '上学期期中考试，各科考试安排将在考试前一周公布。',
        target: ['student']
      },
      {
        id: 7,
        title: '期末考试',
        date: '2025-01-10',
        type: 'exam',
        content: '上学期期末考试周开始，请同学们提前做好复习准备。',
        target: ['student']
      },
      {
        id: 8,
        title: '寒假开始',
        date: '2025-01-20',
        type: 'holiday',
        content: '寒假开始，请同学们按照规定时间离校。',
        target: ['all']
      }
    ])

    // 按日期排序的事件列表
    const sortedEvents = computed(() => {
      return [...events.value].sort((a, b) => new Date(a.date) - new Date(b.date))
    })

    // 处理事件点击
    const handleEventClick = (event) => {
      currentEvent.value = event
      eventDetailVisible.value = true
    }

    // 获取事件类型名称
    const getEventTypeName = (type) => {
      const typeMap = {
        normal: '普通事件',
        important: '重要事件',
        exam: '考试',
        holiday: '假期'
      }
      return typeMap[type] || type
    }

    // 获取事件颜色
    const getEventColor = (type) => {
      const colorMap = {
        important: '#f56c6c',
        exam: '#e6a23c',
        holiday: '#67c23a',
        normal: '#409eff'
      }
      return colorMap[type] || '#409eff'
    }

    // 格式化日期
    const formatDate = (date) => {
      return new Date(date).toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      })
    }

    // 格式化日期范围
    const formatDateRange = (event) => {
      const startDate = formatDate(event.date)
      if (event.endDate) {
        const endDate = formatDate(event.endDate)
        return `${startDate} 至 ${endDate}`
      }
      return startDate
    }

    // 获取事件适用对象名称
    const getEventTargets = (targets) => {
      if (!targets || targets.length === 0) return ''
      
      const targetMap = {
        all: '全体师生',
        teacher: '教师',
        student: '在校生',
        freshman: '新生'
      }
      
      return Array.isArray(targets) ? targets.map(target => targetMap[target] || target).join('、') : targetMap[targets] || targets
    }

    return {
      eventDetailVisible,
      currentEvent,
      sortedEvents,
      handleEventClick,
      getEventTypeName,
      getEventTargets,
      getEventColor,
      formatDate,
      formatDateRange
    }
  }
}
</script>

<style scoped>
.timeline-container {
  padding: 20px;
}

.timeline-card {
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.event-legend {
  display: flex;
  gap: 15px;
}

.legend-item {
  display: flex;
  align-items: center;
  font-size: 12px;
}

.color-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 4px;
}

.color-dot.important {
  background-color: #f56c6c;
}

.color-dot.exam {
  background-color: #e6a23c;
}

.color-dot.holiday {
  background-color: #67c23a;
}

.timeline-content {
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.timeline-content:hover {
  background-color: #f5f7fa;
}

.event-title {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.event-target {
  margin: 4px 0 0;
  font-size: 12px;
  color: #909399;
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

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>