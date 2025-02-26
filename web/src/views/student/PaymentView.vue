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
          <div class="empty-block" v-if="unpaidItems.length === 0">
            <el-empty description="暂无待缴费项目" />
          </div>
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
            <el-table-column prop="paymentMethod" label="支付方式" width="120" />
          </el-table>
          <div class="empty-block" v-if="paymentHistory.length === 0">
            <el-empty description="暂无缴费记录" />
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="直通车缴费情况" name="fastTrack">
          <div class="filter-container">
            <el-input
              v-model="searchText"
              placeholder="搜索学号或姓名"
              style="width: 200px; margin-right: 10px"
              clearable
              @clear="handleSearch"
              @input="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            
            <el-select v-model="paymentStatus" placeholder="支付状态" style="width: 120px; margin-right: 10px" @change="handleSearch">
              <el-option label="全部" value="" />
              <el-option label="已支付" value="已支付" />
              <el-option label="未支付" value="未支付" />
            </el-select>
            
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </div>
          
          <el-table :data="filteredFastTrackPayments" style="width: 100%; margin-top: 20px" :resize-observer="false">
            <el-table-column prop="studentNumber" label="学号" width="120" />
            <el-table-column prop="studentName" label="姓名" width="120" />
            <el-table-column prop="major" label="专业" width="180" />
            <el-table-column prop="paymentItem" label="缴费项目" />
            <el-table-column prop="amount" label="金额" width="120">
              <template #default="scope">
                ¥{{ scope.row.amount }}
              </template>
            </el-table-column>
            <el-table-column prop="deadline" label="截止日期" width="120" />
            <el-table-column prop="status" label="状态" width="120">
              <template #default="scope">
                <el-tag :type="scope.row.status === '已支付' ? 'success' : 'danger'">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="paymentDate" label="缴费日期" width="120" />
          </el-table>
          <div class="pagination-container">
            <el-pagination
              background
              layout="prev, pager, next"
              :total="fastTrackPayments.length"
              :page-size="pageSize"
              v-model:current-page="currentPage"
              @current-change="handlePageChange"
            />
          </div>
          <div class="empty-block" v-if="filteredFastTrackPayments.length === 0">
            <el-empty description="暂无满足条件的数据" />
          </div>
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
            <el-radio label="微信支付">微信支付</el-radio>
            <el-radio label="支付宝">支付宝</el-radio>
            <el-radio label="银行卡">银行卡</el-radio>
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
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

export default {
  name: 'PaymentView',
  components: {
    Search
  },
  data() {
    return {
      activeTab: 'unpaid',
      paymentDialogVisible: false,
      paymentMethod: '微信支付',
      currentPayment: {},
      unpaidItems: [],
      paymentHistory: [],
      fastTrackPayments: [
        {
          studentNumber: '2024001',
          studentName: '张三',
          major: '计算机科学与技术',
          paymentItem: '学费',
          amount: 4800,
          deadline: '2024-09-01',
          status: '已支付',
          paymentDate: '2024-08-15'
        },
        {
          studentNumber: '2024002',
          studentName: '李四',
          major: '软件工程',
          paymentItem: '学费',
          amount: 4800,
          deadline: '2024-09-01',
          status: '已支付',
          paymentDate: '2024-08-20'
        },
        {
          studentNumber: '2024003',
          studentName: '王五',
          major: '信息安全',
          paymentItem: '学费',
          amount: 5200,
          deadline: '2024-09-01',
          status: '未支付',
          paymentDate: ''
        },
        {
          studentNumber: '2024004',
          studentName: '赵六',
          major: '人工智能',
          paymentItem: '学费',
          amount: 5500,
          deadline: '2024-09-01',
          status: '已支付',
          paymentDate: '2024-08-18'
        },
        {
          studentNumber: '2024005',
          studentName: '钱七',
          major: '计算机科学与技术',
          paymentItem: '住宿费',
          amount: 1200,
          deadline: '2024-09-01',
          status: '未支付',
          paymentDate: ''
        }
      ],
      searchText: '',
      paymentStatus: '',
      currentPage: 1,
      pageSize: 5
    }
  },
  computed: {
    filteredFastTrackPayments() {
      // 确保fastTrackPayments是数组
      if (!Array.isArray(this.fastTrackPayments)) {
        console.warn('fastTrackPayments不是数组', this.fastTrackPayments)
        return []
      }

      let filtered = this.fastTrackPayments
      
      // 搜索过滤
      if (this.searchText) {
        const searchLower = this.searchText.toLowerCase()
        filtered = filtered.filter(item => 
          item.studentNumber.includes(searchLower) ||
          item.studentName.toLowerCase().includes(searchLower)
        )
      }
      
      // 状态过滤
      if (this.paymentStatus) {
        filtered = filtered.filter(item => item.status === this.paymentStatus)
      }
      
      // 分页
      const startIndex = (this.currentPage - 1) * this.pageSize
      const endIndex = startIndex + this.pageSize
      
      return filtered.slice(startIndex, endIndex)
    }
  },
  created() {
    this.fetchUnpaidItems()
    this.fetchPaymentHistory()
  },
  methods: {
    async fetchUnpaidItems() {
      try {
        const studentNumber = window.sessionStorage.getItem('studentName')
        const response = await this.$http.get(`/api/student/payment/unpaid/${studentNumber}`)
        if (response.data && Array.isArray(response.data)) {
          this.unpaidItems = response.data
        } else {
          console.warn('获取到的待缴费项目不是数组格式', response.data)
          this.unpaidItems = []
        }
      } catch (error) {
        console.error('获取待缴费项目失败:', error)
        // 模拟数据
        this.unpaidItems = [
          {
            id: 1,
            name: '2024年秋季学费',
            amount: 4800,
            deadline: '2024-09-01'
          },
          {
            id: 2,
            name: '2024年秋季住宿费',
            amount: 1200,
            deadline: '2024-09-01'
          }
        ]
      }
    },
    async fetchPaymentHistory() {
      try {
        const studentNumber = window.sessionStorage.getItem('studentName')
        const response = await this.$http.get(`/api/student/payment/history/${studentNumber}`)
        if (response.data && Array.isArray(response.data)) {
          this.paymentHistory = response.data
        } else {
          console.warn('获取到的缴费记录不是数组格式', response.data)
          this.paymentHistory = []
        }
      } catch (error) {
        console.error('获取缴费记录失败:', error)
        // 模拟数据
        this.paymentHistory = [
          {
            id: 3,
            name: '2024年春季学费',
            amount: 4800,
            paymentDate: '2024-02-15',
            status: '已支付',
            paymentMethod: '微信支付'
          },
          {
            id: 4,
            name: '2024年春季住宿费',
            amount: 1200,
            paymentDate: '2024-02-15',
            status: '已支付',
            paymentMethod: '微信支付'
          }
        ]
      }
    },
    handlePay(item) {
      this.currentPayment = item
      this.paymentDialogVisible = true
    },
    async confirmPayment() {
      try {
        const studentNumber = window.sessionStorage.getItem('studentName')
        await this.$http.post('/api/student/payment/pay', {
          studentNumber,
          paymentId: this.currentPayment.id,
          method: this.paymentMethod
        })
        
        ElMessage.success('支付成功')
        this.paymentDialogVisible = false
        
        // 更新缴费项目和历史记录
        await this.fetchUnpaidItems()
        await this.fetchPaymentHistory()
      } catch (error) {
        console.error('支付失败:', error)
        ElMessage.error('支付失败，请稍后重试')
        
        // 模拟支付成功
        this.paymentDialogVisible = false
        
        // 将当前项目从待缴费列表移除
        this.unpaidItems = this.unpaidItems.filter(item => item.id !== this.currentPayment.id)
        
        // 添加到支付历史
        this.paymentHistory.unshift({
          id: this.currentPayment.id,
          name: this.currentPayment.name,
          amount: this.currentPayment.amount,
          paymentDate: new Date().toISOString().split('T')[0],
          status: '已支付',
          paymentMethod: this.paymentMethod
        })
      }
    },
    handleSearch() {
      this.currentPage = 1 // 重置到第一页
    },
    resetSearch() {
      this.searchText = ''
      this.paymentStatus = ''
      this.currentPage = 1
    },
    handlePageChange(page) {
      this.currentPage = page
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
.filter-container {
  display: flex;
  margin-bottom: 20px;
  align-items: center;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
.empty-block {
  margin-top: 40px;
}
</style>