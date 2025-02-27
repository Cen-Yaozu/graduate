<template>
  <div class="report-view">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>统计报表</span>
        </div>
      </template>

      <el-row :gutter="20">
        <!-- 各系专业男女比例 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">各系专业男女比例</div>
            </template>
            <div class="chart-container" ref="genderRatioRef"></div>
          </el-card>
        </el-col>

        <!-- 缴费完成度 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">缴费完成度</div>
            </template>
            <div class="chart-container" ref="paymentCompletionRef"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 宿舍入住完成度 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">宿舍入住完成度</div>
            </template>
            <div class="chart-container" ref="dormitoryCompletionRef"></div>
          </el-card>
        </el-col>

        <!-- 新生报到完成度 -->
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">新生报到完成度</div>
            </template>
            <div class="chart-container" ref="registrationCompletionRef"></div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

// 图表实例引用
const genderRatioRef = ref(null)
const paymentCompletionRef = ref(null)
const dormitoryCompletionRef = ref(null)
const registrationCompletionRef = ref(null)

// 保存图表实例
let genderRatioChart = null
let paymentCompletionChart = null
let dormitoryCompletionChart = null
let registrationCompletionChart = null

// 初始化性别比例图表
const initGenderRatioChart = () => {
  genderRatioChart = echarts.init(genderRatioRef.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['男生', '女生']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value'
    },
    yAxis: {
      type: 'category',
      data: ['计算机系', '机械系', '电子系', '管理系']
    },
    series: [
      {
        name: '男生',
        type: 'bar',
        stack: 'total',
        label: {
          show: true
        },
        emphasis: {
          focus: 'series'
        },
        data: [320, 302, 301, 150]
      },
      {
        name: '女生',
        type: 'bar',
        stack: 'total',
        label: {
          show: true
        },
        emphasis: {
          focus: 'series'
        },
        data: [120, 132, 101, 250]
      }
    ]
  }
  genderRatioChart.setOption(option)
}

// 初始化缴费完成度图表
const initPaymentCompletionChart = () => {
  paymentCompletionChart = echarts.init(paymentCompletionRef.value)
  const option = {
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
          { value: 1048, name: '已缴费' },
          { value: 235, name: '未缴费' }
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
  paymentCompletionChart.setOption(option)
}

// 初始化宿舍入住完成度图表
const initDormitoryCompletionChart = () => {
  dormitoryCompletionChart = echarts.init(dormitoryCompletionRef.value)
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '入住情况',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '20',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 1048, name: '已入住' },
          { value: 235, name: '未入住' }
        ]
      }
    ]
  }
  dormitoryCompletionChart.setOption(option)
}

// 初始化新生报到完成度图表
const initRegistrationCompletionChart = () => {
  registrationCompletionChart = echarts.init(registrationCompletionRef.value)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['报到人数', '计划人数']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['计算机系', '机械系', '电子系', '管理系']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '报到人数',
        type: 'line',
        stack: 'Total',
        data: [120, 132, 101, 134]
      },
      {
        name: '计划人数',
        type: 'line',
        stack: 'Total',
        data: [150, 150, 150, 150]
      }
    ]
  }
  registrationCompletionChart.setOption(option)
}

// 获取统计数据
const getStatisticsData = async () => {
  try {
    // TODO: 调用后端API获取统计数据
    // const res = await getStatisticsAPI()
    // 使用获取到的数据更新图表
  } catch (error) {
    console.error('获取统计数据失败：', error)
    ElMessage.error('获取统计数据失败')
  }
}

// 监听窗口大小变化，调整图表大小
const handleResize = () => {
  genderRatioChart?.resize()
  paymentCompletionChart?.resize()
  dormitoryCompletionChart?.resize()
  registrationCompletionChart?.resize()
}

onMounted(() => {
  // 初始化所有图表
  initGenderRatioChart()
  initPaymentCompletionChart()
  initDormitoryCompletionChart()
  initRegistrationCompletionChart()

  // 获取统计数据
  getStatisticsData()

  // 添加窗口大小变化监听
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  // 移除窗口大小变化监听
  window.removeEventListener('resize', handleResize)

  // 销毁图表实例
  genderRatioChart?.dispose()
  paymentCompletionChart?.dispose()
  dormitoryCompletionChart?.dispose()
  registrationCompletionChart?.dispose()
})
</script>

<style scoped>
.report-view {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-card {
  margin-bottom: 20px;
}

.chart-header {
  font-size: 16px;
  font-weight: bold;
}

.chart-container {
  height: 300px;
  width: 100%;
}
</style>