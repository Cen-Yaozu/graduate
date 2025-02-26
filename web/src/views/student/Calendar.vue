<template>
  <div class="calendar-container">
    <el-card class="calendar-card">
      <template #header>
        <div class="card-header">
          <span>校园日历</span>
          <el-radio-group v-model="calendarView" size="small">
            <el-radio-button label="month">月视图</el-radio-button>
            <el-radio-button label="week">周视图</el-radio-button>
          </el-radio-group>
        </div>
      </template>
      
      <el-calendar v-if="calendarView === 'month'">
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
                :class="'event-type-' + event.type"
                @click="showEventDetails(event)"
              >
                {{ event.title }}
              </div>
            </div>
          </div>
        </template>
      </el-calendar>
      
      <div v-else class="week-view">
        <el-row class="week-header">
          <el-col :span="3" class="time-column">时间</el-col>
          <el-col :span="3" v-for="day in weekDays" :key="day.date">
            <div class="day-header">
              <div class="day-name">{{ day.name }}</div>
              <div class="day-date">{{ day.date }}</div>
            </div>
          </el-col>
        </el-row>
        
        <div class="week-body">
          <el-row v-for="hour in hours" :key="hour" class="hour-row">
            <el-col :span="3" class="time-column">{{ hour }}:00</el-col>
            <el-col :span="3" v-for="(day, index) in weekDays" :key="index">
              <div class="hour-cell">
                <div 
                  v-for="(event, eventIndex) in getEventsForHour(day.fullDate, hour)" 
                  :key="eventIndex"
                  class="event-item"
                  :class="'event-type-' + event.type"
                  @click="showEventDetails(event)"
                >
                  {{ event.title }}
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-card>
    
    <el-card class="upcoming-events-card">
      <template #header>
        <div class="card-header">
          <span>新生入学日程安排</span>
        </div>
      </template>
      
      <el-timeline>
        <el-timeline-item
          timestamp="2024年9月7日"
          type="danger"
        >
          <h4>新生入学报到</h4>
          <p>新生入学报到，请携带录取通知书和身份证</p>
        </el-timeline-item>
        <el-timeline-item
          timestamp="2024年9月8日-10日"
          type="primary"
        >
          <h4>新生入学教育</h4>
          <p>新生入学教育，介绍学校规章制度</p>
        </el-timeline-item>
        <el-timeline-item
          timestamp="2024年9月11日"
          type="success"
        >
          <h4>新生开学典礼，军事训练动员大会</h4>
          <p>新生开学典礼，请穿着整齐校服参加</p>
        </el-timeline-item>
        <el-timeline-item
          timestamp="2024年9月11日-24日"
          type="warning"
        >
          <h4>新生军训</h4>
          <p>新生军训，请穿着运动服参加</p>
        </el-timeline-item>
      </el-timeline>
    </el-card>
    
    <el-dialog
      v-model="eventDialogVisible"
      :title="selectedEvent.title"
      width="30%"
    >
      <div class="event-details">
        <p><strong>日期:</strong> {{ selectedEvent.date }}</p>
        <p><strong>时间:</strong> {{ selectedEvent.time || '全天' }}</p>
        <p><strong>地点:</strong> {{ selectedEvent.location || '未指定' }}</p>
        <p><strong>描述:</strong> {{ selectedEvent.description }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "CalendarView",
  data() {
    return {
      calendarView: 'month',
      eventDialogVisible: false,
      selectedEvent: {},
      events: [
        {
          id: 1,
          title: '新生入学报到',
          date: '2024-09-07',
          time: '08:00-18:00',
          location: '学校大门',
          description: '新生入学报到，请携带录取通知书和身份证',
          type: 'important'
        },
        {
          id: 2,
          title: '新生入学教育',
          date: '2024-09-08',
          time: '09:00-12:00',
          location: '大礼堂',
          description: '新生入学教育，介绍学校规章制度',
          type: 'education'
        },
        {
          id: 3,
          title: '新生入学教育',
          date: '2024-09-09',
          time: '09:00-12:00',
          location: '大礼堂',
          description: '新生入学教育，介绍学校规章制度',
          type: 'education'
        },
        {
          id: 4,
          title: '新生入学教育',
          date: '2024-09-10',
          time: '09:00-12:00',
          location: '大礼堂',
          description: '新生入学教育，介绍学校规章制度',
          type: 'education'
        },
        {
          id: 5,
          title: '新生开学典礼',
          date: '2024-09-11',
          time: '09:00-11:00',
          location: '操场',
          description: '新生开学典礼，请穿着整齐校服参加',
          type: 'ceremony'
        },
        {
          id: 6,
          title: '军训动员大会',
          date: '2024-09-11',
          time: '14:00-16:00',
          location: '大礼堂',
          description: '军训动员大会，请穿着校服参加',
          type: 'ceremony'
        },
        {
          id: 7,
          title: '军训',
          date: '2024-09-12',
          time: '08:00-17:00',
          location: '操场',
          description: '新生军训，请穿着运动服参加',
          type: 'training'
        },
        {
          id: 8,
          title: '军训',
          date: '2024-09-13',
          time: '08:00-17:00',
          location: '操场',
          description: '新生军训，请穿着运动服参加',
          type: 'training'
        },
        {
          id: 9,
          title: '军训',
          date: '2024-09-14',
          time: '08:00-17:00',
          location: '操场',
          description: '新生军训，请穿着运动服参加',
          type: 'training'
        },
        {
          id: 10,
          title: '军训',
          date: '2024-09-15',
          time: '08:00-17:00',
          location: '操场',
          description: '新生军训，请穿着运动服参加',
          type: 'training'
        },
        {
          id: 11,
          title: '军训',
          date: '2024-09-16',
          time: '08:00-17:00',
          location: '操场',
          description: '新生军训，请穿着运动服参加',
          type: 'training'
        },
        {
          id: 12,
          title: '军训',
          date: '2024-09-17',
          time: '08:00-17:00',
          location: '操场',
          description: '新生军训，请穿着运动服参加',
          type: 'training'
        },
        {
          id: 13,
          title: '军训',
          date: '2024-09-18',
          time: '08:00-17:00',
          location: '操场',
          description: '新生军训，请穿着运动服参加',
          type: 'training'
        },
        {
          id: 14,
          title: '军训',
          date: '2024-09-19',
          time: '08:00-17:00',
          location: '操场',
          description: '新生军训，请穿着运动服参加',
          type: 'training'
        },
        {
          id: 15,
          title: '军训',
          date: '2024-09-20',
          time: '08:00-17:00',
          location: '操场',
          description: '新生军训，请穿着运动服参加',
          type: 'training'
        },
        {
          id: 16,
          title: '军训',
          date: '2024-09-21',
          time: '08:00-17:00',
          location: '操场',
          description: '新生军训，请穿着运动服参加',
          type: 'training'
        },
        {
          id: 17,
          title: '军训',
          date: '2024-09-22',
          time: '08:00-17:00',
          location: '操场',
          description: '新生军训，请穿着运动服参加',
          type: 'training'
        },
        {
          id: 18,
          title: '军训',
          date: '2024-09-23',
          time: '08:00-17:00',
          location: '操场',
          description: '新生军训，请穿着运动服参加',
          type: 'training'
        },
        {
          id: 19,
          title: '军训',
          date: '2024-09-24',
          time: '08:00-17:00',
          location: '操场',
          description: '新生军训，请穿着运动服参加',
          type: 'training'
        },
        {
          id: 20,
          title: '军训结业典礼',
          date: '2024-09-24',
          time: '16:00-17:30',
          location: '操场',
          description: '军训结业典礼，请穿着整齐军训服装参加',
          type: 'ceremony'
        },
        {
          id: 21,
          title: '正式上课',
          date: '2024-09-25',
          time: '08:00',
          location: '各教学楼',
          description: '正式上课，请按照课表到指定教室上课',
          type: 'education'
        }
      ]
    }
  },
  computed: {
    upcomingEvents() {
      const today = new Date();
      today.setHours(0, 0, 0, 0);
      
      return this.events
        .filter(event => {
          const eventDate = new Date(event.date);
          return eventDate >= today;
        })
        .sort((a, b) => new Date(a.date) - new Date(b.date))
        .slice(0, 5);
    },
    weekDays() {
      const days = [];
      const today = new Date();
      const dayNames = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
      
      // 获取本周的周一
      const monday = new Date(today);
      const dayOfWeek = today.getDay();
      const diff = dayOfWeek === 0 ? -6 : 1 - dayOfWeek; // 如果是周日，则获取上周一
      monday.setDate(today.getDate() + diff);
      
      // 生成一周的日期
      for (let i = 0; i < 7; i++) {
        const date = new Date(monday);
        date.setDate(monday.getDate() + i);
        
        const month = date.getMonth() + 1;
        const day = date.getDate();
        
        days.push({
          name: dayNames[(i + 1) % 7], // 从周一开始
          date: `${month}/${day}`,
          fullDate: date.toISOString().split('T')[0]
        });
      }
      
      return days;
    },
    hours() {
      return Array.from({ length: 14 }, (_, i) => i + 8); // 8:00 - 21:00
    }
  },
  methods: {
    getEventsForDate(date) {
      return this.events.filter(event => event.date === date);
    },
    getEventsForHour(date, hour) {
      return this.events.filter(event => {
        if (event.date !== date) return false;
        
        if (!event.time) return false;
        
        const eventHour = parseInt(event.time.split(':')[0]);
        return eventHour === hour;
      });
    },
    showEventDetails(event) {
      this.selectedEvent = event;
      this.eventDialogVisible = true;
    },
    getEventTypeIcon(type) {
      switch (type) {
        case 'important':
          return 'danger';
        case 'education':
          return 'primary';
        case 'ceremony':
          return 'success';
        case 'training':
          return 'warning';
        default:
          return 'info';
      }
    },
    async fetchCalendarEvents() {
      try {
        const studentNumber = window.sessionStorage.getItem('studentName');
        if (!studentNumber) return;
        
        const response = await this.$http.get('/api/student/calendar/events');
        if (response.data.code === 200) {
          this.events = response.data.data || this.events;
        }
      } catch (error) {
        console.error('获取日历事件失败:', error);
      }
    }
  },
  mounted() {
    this.fetchCalendarEvents();
  }
}
</script>

<style scoped>
.calendar-container {
  padding: 20px;
}

.calendar-card, .upcoming-events-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-size: 18px;
  font-weight: bold;
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
  margin-top: 4px;
  overflow-y: auto;
  max-height: 60px;
}

.event-item {
  font-size: 12px;
  padding: 2px 4px;
  margin-bottom: 2px;
  border-radius: 2px;
  cursor: pointer;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.event-type-important {
  background-color: #f56c6c;
  color: white;
}

.event-type-education {
  background-color: #409eff;
  color: white;
}

.event-type-ceremony {
  background-color: #67c23a;
  color: white;
}

.event-type-training {
  background-color: #e6a23c;
  color: white;
}

.week-view {
  margin-top: 20px;
}

.week-header {
  background-color: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
}

.time-column {
  text-align: center;
  padding: 8px;
  font-weight: bold;
  border-right: 1px solid #ebeef5;
}

.day-header {
  padding: 8px;
  text-align: center;
}

.day-name {
  font-weight: bold;
}

.day-date {
  font-size: 12px;
  color: #909399;
}

.week-body {
  height: 600px;
  overflow-y: auto;
}

.hour-row {
  border-bottom: 1px solid #ebeef5;
  min-height: 60px;
}

.hour-cell {
  height: 60px;
  padding: 4px;
  border-right: 1px solid #ebeef5;
}

.event-details p {
  margin: 8px 0;
}
</style>