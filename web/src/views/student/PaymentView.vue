<template>
  <div class="payment-container">
    <el-card class="payment-card">
      <template #header>
        <div class="card-header">
          <span>我的缴费信息</span>
        </div>
      </template>

      <div class="payment-content">
          <!-- 学生信息摘要 -->
          <div class="student-summary" v-if="studentInfo">
            <el-descriptions :column="3" border size="small">
              <el-descriptions-item label="学号">{{ studentInfo.studentNumber }}</el-descriptions-item>
              <el-descriptions-item label="姓名">{{ studentInfo.studentName }}</el-descriptions-item>
              <el-descriptions-item label="性别" v-if="studentInfo.sex">{{ studentInfo.sex }}</el-descriptions-item>
              <el-descriptions-item label="学院" v-if="studentInfo.department">{{ studentInfo.department }}</el-descriptions-item>
              <el-descriptions-item label="专业" v-if="studentInfo.majorname">{{ studentInfo.majorname }}</el-descriptions-item>
            </el-descriptions>
          </div>

          <el-divider>缴费记录</el-divider>
          
          <div class="filter-container">
            <el-input
              v-model="searchText"
              placeholder="搜索缴费项目"
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
            <el-table-column prop="paymentItem" label="缴费项目" />
            <el-table-column prop="major" label="缴费类型" width="120" />
            <el-table-column prop="amount" label="金额" width="120">
              <template #default="scope">
                ¥{{ scope.row.amount }}
              </template>
            </el-table-column>
            <el-table-column prop="deadline" label="截止日期" width="120" />
            <el-table-column prop="status" label="状态" width="120">
              <template #default="scope">
                <el-tag :type="scope.row.status === '已激活' || scope.row.status === '已支付' ? 'success' : 'danger'">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="paymentDate" label="缴费日期" width="120" />
            <el-table-column label="操作" width="120" fixed="right">
              <template #default="scope">
                <el-button 
                  v-if="scope.row.status !== '已支付'" 
                  type="primary" 
                  size="small" 
                  @click="openPaymentDialog(scope.row)"
                >
                  支付
                </el-button>
              </template>
            </el-table-column>
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
            <el-empty description="暂无缴费记录" />
          </div>
      </div>
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
      fastTrackPayments: [],
      searchText: '',
      paymentStatus: '',
      currentPage: 1,
      pageSize: 5,
      paymentDialogVisible: false,
      currentPayment: {
        name: '',
        amount: 0,
        studentNumber: '',
        amountcard: ''
      },
      paymentMethod: '',
      loading: false,
      studentInfo: null
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
          (item.paymentItem && item.paymentItem.toLowerCase().includes(searchLower))
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
    this.fetchStudentInfo()
    this.fetchFastTrackPayments()
  },
  methods: {
    async fetchStudentInfo() {
      try {
        // 从session获取学生学号
        const studentNumber = window.sessionStorage.getItem('studentNumber')
        if (!studentNumber) {
          ElMessage.warning('未找到学生信息，请重新登录')
          return
        }
        
        // 调用API获取学生详细信息
        const response = await this.$http.get(`/api/student/info/${studentNumber}`)
        
        if (response.data.code === 200) {
          const data = response.data.data
          this.studentInfo = {
            studentNumber: data.studentNumber || studentNumber,
            studentName: data.studentName || window.sessionStorage.getItem('studentName') || '未知',
            department: data.department || '未知',
            majorname: data.majorname || '未知',
            sex: data.sex || '未知'
          }
        } else {
          // 如果获取失败，使用sessionStorage中的信息
          this.studentInfo = {
            studentNumber: studentNumber,
            studentName: window.sessionStorage.getItem('studentName') || '未知'
          }
        }
      } catch (error) {
        console.error('获取学生信息失败:', error)
        // 失败时使用sessionStorage中的信息
        this.studentInfo = {
          studentNumber: window.sessionStorage.getItem('studentNumber'),
          studentName: window.sessionStorage.getItem('studentName') || '未知'
        }
      }
    },
    async fetchFastTrackPayments() {
      try {
        this.loading = true
        // 从会话存储中获取学生学号
        const studentNumber = window.sessionStorage.getItem('studentNumber')
        
        if (!studentNumber) {
          ElMessage.warning('未找到学生信息，请重新登录')
          return
        }

        // 调用API获取当前学生的缴费信息
        const response = await this.$http.get(`/api/student/payment/fasttrack?studentNumber=${studentNumber}`)
        
        if (response.data.code === 200) {
          // 对后端返回的数据进行字段映射转换
          const mappedData = (response.data.data || []).map(item => ({
            studentNumber: item.studentNumber,
            studentName: item.studentName,
            major: item.hallway || '学费', // 使用hallway字段作为类型
            paymentItem: item.remark || '学费', // 使用remark作为缴费项目
            amount: item.allmoney || 0, // 金额
            deadline: '2024-09-01', // 设置默认截止日期
            status: item.indentStatue || '未支付', // 直接使用indentStatue作为状态
            paymentDate: item.indentStatue === '已支付' ? new Date().toLocaleDateString() : '', // 设置支付日期
            amountcard: item.amountcard || '' // 保存缴费编号
          }));
          this.fastTrackPayments = mappedData;
        } else {
          throw new Error(response.data.message || '获取缴费数据失败')
        }
      } catch (error) {
        console.error('获取缴费数据失败:', error)
        ElMessage.error('获取缴费数据失败')
      } finally {
        this.loading = false
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
    },
    openPaymentDialog(payment) {
      this.currentPayment = {
        name: payment.paymentItem,
        amount: payment.amount,
        studentNumber: payment.studentNumber,
        amountcard: payment.amountcard
      }
      this.paymentDialogVisible = true
    },
    async confirmPayment() {
      try {
        if (!this.paymentMethod) {
          ElMessage.warning('请选择支付方式')
          return
        }

        const paymentInfo = {
          studentNumber: this.currentPayment.studentNumber,
          amountcard: this.currentPayment.amountcard,
          method: this.paymentMethod
        }

        const response = await this.$http.post('/api/student/payment/pay', paymentInfo)
        
        if (response.data.code === 200) {
          ElMessage.success('支付成功')
          this.paymentDialogVisible = false
          // 刷新缴费数据
          this.fetchFastTrackPayments()
        } else {
          throw new Error(response.data.message || '支付处理失败')
        }
      } catch (error) {
        console.error('支付处理失败:', error)
        ElMessage.error('支付处理失败')
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

.filter-container {
  display: flex;
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.empty-block {
  margin-top: 20px;
}

.student-summary {
  margin-top: 30px;
}
</style>