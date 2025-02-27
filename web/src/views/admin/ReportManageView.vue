<template>
  <div class="report-manage">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>报表管理</span>
        </div>
      </template>

      <!-- 报表类型选择 -->
      <div class="report-type-selector">
        <el-radio-group v-model="reportType">
          <el-radio-button label="student">学生统计</el-radio-button>
          <el-radio-button label="dormitory">宿舍统计</el-radio-button>
          <el-radio-button label="payment">缴费统计</el-radio-button>
        </el-radio-group>
      </div>

      <!-- 时间范围选择 -->
      <div class="date-range-picker">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
        />
        <el-button type="primary" @click="generateReport">生成报表</el-button>
      </div>

      <!-- 报表内容 -->
      <div class="report-content" v-loading="loading">
        <!-- 学生统计 -->
        <div v-if="reportType === 'student'">
          <el-descriptions title="学生基本统计" :column="3" border>
            <el-descriptions-item label="总人数">{{ studentStats.total }}</el-descriptions-item>
            <el-descriptions-item label="已报到人数">{{ studentStats.arrived }}</el-descriptions-item>
            <el-descriptions-item label="报到率">{{ studentStats.arrivalRate }}%</el-descriptions-item>
          </el-descriptions>
          <div class="chart-container">
            <div ref="studentChart" style="width: 100%; height: 400px"></div>
          </div>
        </div>

        <!-- 宿舍统计 -->
        <div v-if="reportType === 'dormitory'">
          <el-descriptions title="宿舍基本统计" :column="3" border>
            <el-descriptions-item label="总宿舍数">{{ dormStats.total }}</el-descriptions-item>
            <el-descriptions-item label="已分配数">{{ dormStats.assigned }}</el-descriptions-item>
            <el-descriptions-item label="使用率">{{ dormStats.occupancyRate }}%</el-descriptions-item>
          </el-descriptions>
          <div class="chart-container">
            <div ref="dormChart" style="width: 100%; height: 400px"></div>
          </div>
        </div>

        <!-- 缴费统计 -->
        <div v-if="reportType === 'payment'">
          <el-descriptions title="缴费基本统计" :column="3" border>
            <el-descriptions-item label="应缴总额">¥{{ paymentStats.totalAmount }}</el-descriptions-item>
            <el-descriptions-item label="已缴总额">¥{{ paymentStats.paidAmount }}</el-descriptions-item>
            <el-descriptions-item label="缴费率">{{ paymentStats.paymentRate }}%</el-descriptions-item>
          </el-descriptions>
          <div class="chart-container">
            <div ref="paymentChart" style="width: 100%; height: 400px"></div>
          </div>
        </div>
      </div>

      <!-- 导出按钮 -->
      <div class="export-actions">
        <el-button type="success" @click="exportReport" :disabled="!hasData">
          <el-icon><Download /></el-icon>导出报表
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { Download } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

// 报表类型和日期范围
const reportType = ref('student')
const dateRange = ref([])
const loading = ref(false)
const hasData = ref(false)

// 统计数据
const studentStats = ref({
  total: 0,
  arrived: 0,
  arrivalRate: 0
})

const dormStats = ref({
  total: 0,
  assigned: 0,
  occupancyRate: 0
})

const paymentStats = ref({
  totalAmount: 0,
  paidAmount: 0,
  paymentRate: 0
})

// 图表实例
let studentChartInstance = null
let dormChartInstance = null
let paymentChartInstance = null

// 生成报表
const generateReport = async () => {
  if (!dateRange.value || dateRange.value.length !== 2) {
    ElMessage.warning('请选择日期范围')
    return
  }

  loading.value = true
  try {
    // TODO: 调用后端API获取报表数据
    // const res = await getReportData({
    //   type: reportType.value,
    //   startDate: dateRange.value[0],
    //   endDate: dateRange.value[1]
    // })
    
    // 模拟数据
    setTimeout(() => {
      if (reportType.value === 'student') {
        studentStats.value = {
          total: 1000,
          arrived: 850,
          arrivalRate: 85
        }
        initStudentChart()
      } else if (reportType.value === 'dormitory') {
        dormStats.value = {
          total: 500,
          assigned: 450,
          occupancyRate: 90
        }
        initDormChart()
      } else {
        paymentStats.value = {
          totalAmount: 1000000,
          paidAmount: 800000,
          paymentRate: 80
        }
        initPaymentChart()
      }
      hasData.value = true
      loading.value = false
    }, 1000)
  } catch (error) {
    console.error('获取报表数据失败：', error)
    ElMessage.error('获取报表数据失败')
    loading.value = false
  }
}

// 初始化学生统计图表
const initStudentChart = () => {
  const chartDom = document.querySelector('.report-content .chart-container div')
  studentChartInstance = echarts.init(chartDom)
  
  const option = {
    title: {
      text: '学生报到情况'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '报到情况',
        type: 'pie',
        radius: '50%',
        data: [
          { value: studentStats.value.arrived, name: '已报到' },
          { value: studentStats.value.total - studentStats.value.arrived, name: '未报到' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  studentChartInstance.setOption(option)
}

// 初始化宿舍统计图表
const initDormChart = () => {
  const chartDom = document.querySelector('.report-content .chart-container div')
  dormChartInstance = echarts.init(chartDom)
  
  const option = {
    title: {
      text: '宿舍使用情况'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '使用情况',
        type: 'pie',
        radius: '50%',
        data: [
          { value: dormStats.value.assigned, name: '已分配' },
          { value: dormStats.value.total - dormStats.value.assigned, name: '未分配' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  dormChartInstance.setOption(option)
}

// 初始化缴费统计图表
const initPaymentChart = () => {
  const chartDom = document.querySelector('.report-content .chart-container div')
  paymentChartInstance = echarts.init(chartDom)
  
  const option = {
    title: {
      text: '缴费情况'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '缴费情况',
        type: 'pie',
        radius: '50%',
        data: [
          { value: paymentStats.value.paidAmount, name: '已缴费' },
          { value: paymentStats.value.totalAmount - paymentStats.value.paidAmount, name: '未缴费' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  paymentChartInstance.setOption(option)
}

// 导出报表
const exportReport = () => {
  ElMessage.success('报表导出成功')
}

// 监听报表类型变化
watch(reportType, () => {
  if (hasData.value) {
    generateReport()
  }
})

// 组件卸载时销毁图表实例
onMounted(() => {
  if (studentChartInstance) studentChartInstance.dispose()
  if (dormChartInstance) dormChartInstance.dispose()
  if (paymentChartInstance) paymentChartInstance.dispose()
})
</script>

<style scoped>
.report-manage {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.report-type-selector {
  margin-bottom: 20px;
}

.date-range-picker {
  margin-bottom: 20px;
  display: flex;
  gap: 16px;
  align-items: center;
}

.report-content {
  margin-top: 20px;
}

.chart-container {
  margin-top: 20px;
  border: 1px solid #ebeef5;
  padding: 20px;
  border-radius: 4px;
}

.export-actions {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>