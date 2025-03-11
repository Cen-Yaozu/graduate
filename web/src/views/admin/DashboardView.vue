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
            <div class="title">已选宿舍人数</div>
            <div class="value">{{ stats.dormSelectedCount }}</div>
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
          <div class="chart-container" v-loading="chartLoading">
            <div ref="majorChartRef" class="chart"></div>
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
          <div class="chart-container" v-loading="chartLoading">
            <div ref="paymentChartRef" class="chart"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, onMounted, onBeforeUnmount, nextTick, getCurrentInstance } from 'vue'
// 引入需要使用的图标
import {
  User,
  Money,
  Location,
  Star
} from '@element-plus/icons-vue'
// 引入 echarts 核心模块
import * as echarts from 'echarts/core'
// 引入图表类型
import { PieChart } from 'echarts/charts'
// 引入组件
import { TitleComponent, TooltipComponent, LegendComponent, GridComponent } from 'echarts/components'
// 引入渲染器
import { CanvasRenderer } from 'echarts/renderers'

// 注册必须的组件
echarts.use([TitleComponent, TooltipComponent, LegendComponent, GridComponent, PieChart, CanvasRenderer])

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
    const { proxy } = getCurrentInstance()
    
    const stats = ref({
      studentCount: 587,
      paymentRate: 83,
      dormSelectedCount: 0,
      newStudentCount: 215
    })
    
    const chartLoading = ref(false)
    const majorChartRef = ref(null)
    const paymentChartRef = ref(null)
    let majorChart = null
    let paymentChart = null
    
    // 专业分布图表配置
    const majorChartOption = {
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        right: 10,
        top: 'center',
        itemWidth: 10,
        itemHeight: 10,
        textStyle: {
          fontSize: 12
        }
      },
      series: [
        {
          name: '专业分布',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
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
              fontSize: 16,
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: [
            { value: 120, name: '计算机科学与技术' },
            { value: 85, name: '软件工程' },
            { value: 70, name: '人工智能' },
            { value: 65, name: '信息安全' },
            { value: 60, name: '物联网工程' },
            { value: 55, name: '数据科学与大数据技术' },
            { value: 50, name: '智能科学与技术' },
            { value: 45, name: '网络工程' },
            { value: 37, name: '电子信息工程' }
          ]
        }
      ]
    }
    
    // 缴费状态图表配置
    const paymentChartOption = {
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c} ({d}%)'
      },
      legend: {
        bottom: 'bottom',
        left: 'center',
        itemWidth: 12,
        itemHeight: 12,
        textStyle: {
          fontSize: 12
        }
      },
      color: ['#67C23A', '#F56C6C'],
      series: [
        {
          name: '缴费状态',
          type: 'pie',
          radius: '70%',
          center: ['50%', '45%'],
          data: [
            { value: 485, name: '已缴费', itemStyle: { color: '#67C23A' } },
            { value: 102, name: '未缴费', itemStyle: { color: '#F56C6C' } }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          },
          label: {
            formatter: '{b}: {c} ({d}%)',
            fontSize: 12
          }
        }
      ]
    }

    // 初始化图表
    const initCharts = () => {
      // 确保DOM已经渲染
      nextTick(() => {
        // 初始化专业分布图表
        if (majorChartRef.value) {
          majorChart = echarts.init(majorChartRef.value)
          majorChart.setOption(majorChartOption)
          
          // 添加窗口调整大小事件监听器
          window.addEventListener('resize', handleResize)
        }
        
        // 初始化缴费状态图表
        if (paymentChartRef.value) {
          paymentChart = echarts.init(paymentChartRef.value)
          paymentChart.setOption(paymentChartOption)
        }
      })
    }
    
    // 处理窗口大小调整
    const handleResize = () => {
      if (majorChart) {
        majorChart.resize()
      }
      if (paymentChart) {
        paymentChart.resize()
      }
    }

    // 获取数据并更新图表
    const fetchChartData = async () => {
      chartLoading.value = true
      try {
        // 确保proxy和$http存在
        if (proxy && proxy.$http) {
          // 调用API获取专业分布数据
          console.log('正在请求专业分布数据...')
          const { data: majorResult } = await proxy.$http.get('/api/admin/student/major-stats')
          console.log('收到专业分布数据响应:', majorResult)
          if (majorResult.code === 200) {
            const newData = majorResult.data.map(item => ({
              value: item.count,
              name: item.majorName
            }))
            if (majorChart) {
              majorChart.setOption({ series: [{ data: newData }] })
              console.log('专业分布图表更新完成')
            }
          }
          
          // 调用API获取缴费统计数据
          console.log('正在请求缴费统计数据...')
          const { data: paymentResult } = await proxy.$http.get('/api/admin/payment/stats')
          console.log('收到缴费统计数据响应:', paymentResult)
          if (paymentResult.code === 200) {
            const newData = [
              { value: paymentResult.data.paid, name: '已缴费', itemStyle: { color: '#67C23A' } },
              { value: paymentResult.data.unpaid, name: '未缴费', itemStyle: { color: '#F56C6C' } }
            ]
            if (paymentChart) {
              paymentChart.setOption({ series: [{ data: newData }] })
              console.log('缴费统计图表更新完成')
            }
            
            // 更新缴费率统计卡片
            stats.value.paymentRate = Math.round(paymentResult.data.paidRate)
            console.log('缴费率更新为:', stats.value.paymentRate)
          }
        } else {
          console.warn('HTTP客户端未定义，使用默认数据')
        }
        
        console.log('加载图表数据成功')
      } catch (error) {
        console.error('获取图表数据失败：', error)
        // 发生错误时使用默认数据（当前已配置的数据）
      } finally {
        chartLoading.value = false
      }
    }

    const fetchStats = async () => {
      try {
        // 确保proxy和$http存在
        if (proxy && proxy.$http) {
          // 获取学生总数
          console.log('正在请求学生总数...')
          const { data: studentCountResult } = await proxy.$http.get('/api/admin/student/list', { params: { page: 1, size: 1 } })
          console.log('收到学生总数响应:', studentCountResult)
          if (studentCountResult.code === 200) {
            stats.value.studentCount = studentCountResult.data.total
            console.log('学生总数更新为:', stats.value.studentCount)
          }
          
          // 获取缴费统计（包括缴费率）
          console.log('正在请求缴费统计数据...')
          const { data: paymentResult } = await proxy.$http.get('/api/admin/payment/stats')
          console.log('收到缴费统计响应:', paymentResult)
          if (paymentResult.code === 200) {
            stats.value.paymentRate = Math.round(paymentResult.data.paidRate)
            console.log('缴费率更新为:', stats.value.paymentRate)
          }
          
          // 获取已选择宿舍的学生数量
          console.log('正在请求宿舍选择统计数据...')
          const { data: dormResult } = await proxy.$http.get('/api/admin/dorm/student-dorm/stats')
          console.log('收到宿舍选择统计响应:', dormResult)
          if (dormResult.code === 200) {
            stats.value.dormSelectedCount = dormResult.data.selectedCount
            console.log('已选择宿舍人数更新为:', stats.value.dormSelectedCount)
          }
          
          // 获取预计新生数（这里仍使用默认值，可根据实际情况修改）
        } else {
          console.warn('HTTP客户端未定义，使用默认数据')
        }
        
        console.log('加载仪表盘数据成功')
      } catch (error) {
        console.error('获取仪表盘数据失败：', error)
        // 发生错误时保留默认数据
      }
    }

    onMounted(() => {
      fetchStats()
      initCharts()
      fetchChartData()
    })
    
    onBeforeUnmount(() => {
      // 移除事件监听器
      window.removeEventListener('resize', handleResize)
      
      // 销毁图表实例
      if (majorChart) {
        majorChart.dispose()
        majorChart = null
      }
      if (paymentChart) {
        paymentChart.dispose()
        paymentChart = null
      }
    })

    return {
      stats,
      chartLoading,
      majorChartRef,
      paymentChartRef
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
  height: 350px;
  width: 100%;
}

.chart {
  height: 100%;
  width: 100%;
}
</style> 