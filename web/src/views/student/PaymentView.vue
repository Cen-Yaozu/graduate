<template>
  <div class="payment-container">
    <el-card class="payment-card">
      <template #header>
        <div class="card-header">
          <span>缴费管理</span>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="待缴费项目" name="unpaid">
          <el-table :data="unpaidItems" style="width: 100%; height: 300px" :resize-observer="false">
            <el-table-column prop="name" label="项目名称" />
            <el-table-column prop="amount" label="金额" width="120">
              <template #default="scope">
                ¥{{ scope.row.amount }}
              </template>
            </el-table-column>
            <el-table-column prop="deadline" label="截止日期" width="120" />
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button type="primary" size="small" @click="handlePay(scope.row)">缴费</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="缴费记录" name="history">
          <el-table :data="paymentHistory" style="width: 100%; height: 300px" :resize-observer="false">
            <el-table-column prop="name" label="项目名称" />
            <el-table-column prop="amount" label="金额" width="120">
              <template #default="scope">
                ¥{{ scope.row.amount }}
              </template>
            </el-table-column>
            <el-table-column prop="paymentDate" label="缴费日期" width="120" />
            <el-table-column prop="status" label="状态" width="120">
              <template #default="scope">
                <el-tag :type="scope.row.status === '已支付' ? 'success' : 'info'">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-dialog v-model="paymentDialogVisible" title="缴费确认" width="30%">
      <el-form :model="currentPayment" label-width="80px">
        <el-form-item label="项目名称">
          <span>{{ currentPayment.name }}</span>
        </el-form-item>
        <el-form-item label="缴费金额">
          <span>¥{{ currentPayment.amount }}</span>
        </el-form-item>
        <el-form-item label="支付方式">
          <el-radio-group v-model="paymentMethod">
            <el-radio label="alipay">支付宝</el-radio>
            <el-radio label="wechat">微信支付</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="paymentDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmPayment">确认支付</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'PaymentView',
  data() {
    return {
      activeTab: 'unpaid',
      paymentDialogVisible: false,
      paymentMethod: 'alipay',
      currentPayment: {},
      unpaidItems: [],
      paymentHistory: []
    }
  },
  created() {
    this.fetchUnpaidItems()
    this.fetchPaymentHistory()
  },
  methods: {
    async fetchUnpaidItems() {
      try {
        const studentNumber = localStorage.getItem('studentNumber')
        const response = await axios.get(`/api/student/payment/unpaid/${studentNumber}`)
        this.unpaidItems = response.data
      } catch (error) {
        this.$message.error('获取待缴费项目失败')
      }
    },
    async fetchPaymentHistory() {
      try {
        const studentNumber = localStorage.getItem('studentNumber')
        const response = await axios.get(`/api/student/payment/history/${studentNumber}`)
        this.paymentHistory = response.data
      } catch (error) {
        this.$message.error('获取缴费记录失败')
      }
    },
    handlePay(item) {
      this.currentPayment = item
      this.paymentDialogVisible = true
    },
    async confirmPayment() {
      try {
        const studentNumber = localStorage.getItem('studentNumber')
        await axios.post('/api/student/payment/pay', {
          studentNumber,
          paymentId: this.currentPayment.id,
          paymentMethod: this.paymentMethod
        })
        this.$message.success('支付成功')
        this.paymentDialogVisible = false
        
        // 刷新数据
        this.fetchUnpaidItems()
        this.fetchPaymentHistory()
      } catch (error) {
        this.$message.error('支付失败')
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
  max-width: 1200px;
  margin: 0 auto;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>