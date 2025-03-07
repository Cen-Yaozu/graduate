<template>
  <div class="payment-container">
    <el-card class="payment-card">
      <template #header>
        <div class="card-header">
          <span>我的缴费信息</span>
          <el-button type="primary" @click="fetchPayments" size="small">刷新</el-button>
        </div>
      </template>

      <div class="payment-content" v-loading="loading">
        <div v-if="paymentInfo" class="payment-info">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="姓名">
              {{ paymentInfo.studentName || '未知' }}
            </el-descriptions-item>
            <el-descriptions-item label="缴费编号">
              {{ paymentInfo.amountcard || '无' }}
            </el-descriptions-item>
            <el-descriptions-item label="缴纳项目">
              广州软件学院综合收费
            </el-descriptions-item>
            <el-descriptions-item label="缴费通道">
              {{ paymentInfo.hallway || '无' }}
            </el-descriptions-item>
            <el-descriptions-item label="总金额">
              <span class="amount">¥{{ paymentInfo.allmoney || 0 }}</span>
              <span class="amount-desc">（总金额=宿舍费+学费+医保）</span>
            </el-descriptions-item>
            <el-descriptions-item label="订单状态">
              <el-tag :type="(paymentInfo.indentStatue === '已支付') ? 'success' : 'danger'">
                {{ paymentInfo.indentStatue || '未支付' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>
        
        <div class="empty-block" v-if="!paymentInfo && !loading">
          <el-empty description="暂无缴费记录" />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'

export default {
  name: 'StudentPaymentView',
  data() {
    return {
      paymentInfo: null,
      loading: false
    }
  },
  created() {
    this.fetchPayments()
  },
  methods: {
    async fetchPayments() {
      try {
        this.loading = true
        // 从会话存储中获取学生学号
        const studentNumber = window.sessionStorage.getItem('studentNumber')
        
        if (!studentNumber) {
          ElMessage.warning('未找到学生信息，请重新登录')
          return
        }

        // 调用API获取当前学生的缴费信息
        const response = await this.$http.get(`/api/student/payment?studentNumber=${studentNumber}`)
        
        if (response.data.code === 200) {
          console.log('原始返回数据:', response.data.data)
          
          // 后端现在只返回一条数据
          if (!response.data.data) {
            this.paymentInfo = null
          } else {
            // 处理数据，确保有默认值
            const item = response.data.data
            this.paymentInfo = {
              studentNumber: item.studentNumber || '',
              studentName: item.studentName || '',
              hallway: item.hallway || '',
              amountcard: item.amountcard || '',
              allmoney: item.allmoney || 0,
              indentStatue: item.indentStatue || '未支付'
            }
          }
          
          console.log('处理后的缴费数据:', this.paymentInfo)
        } else {
          throw new Error(response.data.message || '获取缴费数据失败')
        }
      } catch (error) {
        console.error('获取缴费数据失败:', error)
        ElMessage.error('获取缴费数据失败，请稍后重试')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.payment-container {
  padding: 20px;
}

.payment-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.payment-info {
  margin-top: 20px;
}

.empty-block {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

/* 描述列表样式 */
:deep(.el-descriptions__label) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 500;
  text-align: right;
  padding: 12px 15px;
  width: 150px;
}

:deep(.el-descriptions__content) {
  padding: 12px 15px;
  text-align: left;
}

:deep(.el-tag) {
  border-radius: 4px;
  padding: 0 8px;
  height: 24px;
  line-height: 24px;
}

.amount {
  font-weight: bold;
  color: #f56c6c;
}

.amount-desc {
  font-size: 0.8em;
  color: #909399;
  margin-left: 10px;
}
</style>