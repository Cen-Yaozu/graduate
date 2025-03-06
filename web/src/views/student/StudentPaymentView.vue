<template>
  <div class="payment-container">
    <el-card class="payment-card">
      <template #header>
        <div class="card-header">
          <span>我的缴费信息</span>
          <el-button type="primary" @click="fetchPayments" size="small">刷新</el-button>
        </div>
      </template>

      <div class="payment-content">
        <el-divider>缴费记录</el-divider>
        
        <el-table 
          :data="payments" 
          style="width: 100%; margin-top: 20px" 
          v-loading="loading"
          border
        >
          <el-table-column prop="studentName" label="姓名" width="120">
            <template #default="scope">
              {{ scope.row.studentName || '未知' }}
            </template>
          </el-table-column>
          <el-table-column prop="amountcard" label="缴费编号" width="150">
            <template #default="scope">
              {{ scope.row.amountcard || '无' }}
            </template>
          </el-table-column>
          <el-table-column prop="hallway" label="缴费通道" width="120">
            <template #default="scope">
              {{ scope.row.hallway || '无' }}
            </template>
          </el-table-column>
          <el-table-column prop="allmoney" label="总金额" width="120">
            <template #default="scope">
              ¥{{ scope.row.allmoney || 0 }}
            </template>
          </el-table-column>
          <el-table-column prop="indentStatue" label="订单状态" width="120">
            <template #default="scope">
              <el-tag :type="(scope.row.indentStatue === '已支付') ? 'success' : 'danger'">
                {{ scope.row.indentStatue || '未支付' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注">
            <template #default="scope">
              {{ scope.row.remark || '无' }}
            </template>
          </el-table-column>
          <el-table-column label="收费凭证" width="120">
            <template #default="scope">
              <el-popover 
                placement="right" 
                trigger="hover" 
                v-if="scope.row.img"
              >
                <template #reference>
                  <el-button type="primary" link size="small">查看凭证</el-button>
                </template>
                <img :src="scope.row.img" style="max-width: 300px;" />
              </el-popover>
              <span v-else>无凭证</span>
            </template>
          </el-table-column>
        </el-table>
        
        <div class="empty-block" v-if="payments.length === 0 && !loading">
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
      payments: [],
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
          
          // 检查返回的数据类型并进行相应处理
          if (!response.data.data) {
            // 如果没有数据，设置为空数组
            this.payments = []
          } else if (Array.isArray(response.data.data)) {
            // 如果是数组，则进行映射处理
            this.payments = response.data.data.map(item => {
              return {
                studentNumber: item.studentNumber || '',
                studentName: item.studentName || '',
                hallway: item.hallway || '',
                amountcard: item.amountcard || '',
                allmoney: item.allmoney || 0,
                img: item.img || '',
                indentStatue: item.indentStatue || '未支付',
                remark: item.remark || ''
              }
            })
          } else {
            // 如果是单个对象，将其转换为数组
            const item = response.data.data
            this.payments = [{
              studentNumber: item.studentNumber || '',
              studentName: item.studentName || '',
              hallway: item.hallway || '',
              amountcard: item.amountcard || '',
              allmoney: item.allmoney || 0,
              img: item.img || '',
              indentStatue: item.indentStatue || '未支付',
              remark: item.remark || ''
            }]
          }
          
          console.log('处理后的缴费数据:', this.payments)
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

.empty-block {
  margin-top: 20px;
}
</style>