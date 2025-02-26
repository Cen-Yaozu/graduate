<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-header">
            <div class="title">学生总数</div>
            <div class="value">{{ stats.studentCount }}</div>
          </div>
          <div class="icon-container">
            <el-icon class="icon"><el-icon-user /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-header">
            <div class="title">缴费完成率</div>
            <div class="value">{{ stats.paymentRate }}%</div>
          </div>
          <div class="icon-container">
            <el-icon class="icon"><el-icon-money /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-header">
            <div class="title">抵校率</div>
            <div class="value">{{ stats.arrivalRate }}%</div>
          </div>
          <div class="icon-container">
            <el-icon class="icon"><el-icon-location /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-header">
            <div class="title">预计新生数</div>
            <div class="value">{{ stats.newStudentCount }}</div>
          </div>
          <div class="icon-container">
            <el-icon class="icon"><el-icon-star /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>学生专业分布</span>
            </div>
          </template>
          <div class="chart-container">
            <div class="chart-placeholder">专业分布图表将显示在这里</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>缴费状态统计</span>
            </div>
          </template>
          <div class="chart-container">
            <div class="chart-placeholder">缴费统计图表将显示在这里</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最近抵校学生</span>
            </div>
          </template>
          <el-table :data="recentArrivals" style="width: 100%">
            <el-table-column prop="studentNumber" label="学号" width="120" />
            <el-table-column prop="name" label="姓名" width="120" />
            <el-table-column prop="major" label="专业" />
            <el-table-column prop="arrivalTime" label="抵校时间" width="180" />
            <el-table-column prop="dormitory" label="宿舍号" width="120" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
// 引入需要使用的图标
import {
  User,
  Money,
  Location,
  Star
} from '@element-plus/icons-vue'

export default {
  name: 'DashboardView',
  components: {
    // 注册图标组件
    ElIconUser: User,
    ElIconMoney: Money,
    ElIconLocation: Location,
    ElIconStar: Star
  },
  setup() {
    const stats = ref({
      studentCount: 587,
      paymentRate: 83,
      arrivalRate: 75,
      newStudentCount: 215
    })

    const recentArrivals = ref([
      {
        studentNumber: '2024001',
        name: '张三',
        major: '计算机科学与技术',
        arrivalTime: '2024-08-31 09:30:45',
        dormitory: 'A栋301'
      },
      {
        studentNumber: '2024002',
        name: '李四',
        major: '软件工程',
        arrivalTime: '2024-08-31 10:15:22',
        dormitory: 'A栋302'
      },
      {
        studentNumber: '2024003',
        name: '王五',
        major: '信息安全',
        arrivalTime: '2024-08-31 10:45:18',
        dormitory: 'B栋201'
      },
      {
        studentNumber: '2024004',
        name: '赵六',
        major: '人工智能',
        arrivalTime: '2024-08-31 11:20:35',
        dormitory: 'B栋205'
      }
    ])

    const fetchStats = () => {
      // 实际项目中，这里应该调用API获取统计数据
      console.log('加载仪表盘数据')
    }

    onMounted(() => {
      fetchStats()
    })

    return {
      stats,
      recentArrivals
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.box-card {
  height: 120px;
  position: relative;
  overflow: hidden;
}

.card-header {
  position: relative;
  z-index: 1;
}

.title {
  font-size: 16px;
  color: #606266;
}

.value {
  font-size: 28px;
  font-weight: bold;
  margin-top: 10px;
}

.icon-container {
  position: absolute;
  right: 20px;
  bottom: 20px;
}

.icon {
  font-size: 48px;
  color: rgba(0, 0, 0, 0.1);
}

.chart-card {
  min-height: 350px;
}

.chart-container {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chart-placeholder {
  color: #909399;
  font-size: 14px;
}
</style> 