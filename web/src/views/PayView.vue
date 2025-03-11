<template>
  <el-container>
    <el-header style="background-color: #244cb2; display: flex; align-items: center; justify-content: space-between;">
      <el-page-header @back="goBack" style="color: white">
        <template #content>
          <span class="text-large font-600 mr-3" style="font-weight: 800;color: white">费用缴纳</span>
        </template>
      </el-page-header>
      
      <!-- 添加用户头像和下拉菜单 -->
      <div class="user-avatar">
        <el-dropdown trigger="click">
          <span class="el-dropdown-link">
            <el-avatar :size="40" :icon="UserFilled" />
            <el-icon class="el-icon--right">
              <arrow-down />
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>{{ studentNumber }}</el-dropdown-item>
              <el-dropdown-item divided @click="toStudentHome">个人管理</el-dropdown-item>
              <el-dropdown-item @click="handleLogout">退出系统</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>
    <el-main style="padding: 0;">
      <el-card style="width: 90%;height: 100%;background: linear-gradient(to top right,rgba(90,149,207,.5),#f5f7fa);margin: 40px auto;" v-loading="loading">
        <el-descriptions
            title="广州软件学院综合收费"
            direction="horizontal"
            :column="1"
            align="center"
            border
        >
          <el-descriptions-item label="缴纳项目">广州软件学院综合收费</el-descriptions-item>
          <el-descriptions-item label="缴纳状态">{{ paymentData.status }}</el-descriptions-item>
          <el-descriptions-item label="缴费通道">直行通道</el-descriptions-item>
          <el-descriptions-item label="收费机构">广州软件学院</el-descriptions-item>
          <el-descriptions-item label="联系方式">87818009</el-descriptions-item>
          <el-descriptions-item label="支付方式">微信支付</el-descriptions-item>
          <el-descriptions-item label="缴费编号">{{ paymentData.paymentId }}</el-descriptions-item>
          <el-descriptions-item label="用户名称">{{ paymentData.userName }}</el-descriptions-item>
          <el-descriptions-item label="学费">{{ formatCurrency(paymentData.tuitionFee) }}</el-descriptions-item>
          <el-descriptions-item label="宿舍费">{{ formatCurrency(paymentData.accommodationFee) }}</el-descriptions-item>
          <el-descriptions-item label="医保">{{ formatCurrency(paymentData.insuranceFee) }}</el-descriptions-item>
          <el-descriptions-item label="缴费总金额">{{ formatCurrency(totalAmount) }}</el-descriptions-item>
        </el-descriptions>
        <div class="action-buttons">
          <el-button 
            v-if="paymentData.status === '未缴费'" 
            style="background: #98B6EC;padding: 0 44px" 
            @click="pay" 
            v-loading.fullscreen.lock="fullscreenLoading">确认支付</el-button>
          <el-button 
            v-if="paymentData.status === '已缴费'" 
            type="success" 
            @click="downloadReceipt">下载缴费单</el-button>
        </div>
      </el-card>
    </el-main>
  </el-container>
</template>

<script>
import router from "@/router";
import { UserFilled, ArrowDown } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import axios from 'axios'; // 确保已安装axios

export default {
  name: "PayView",
  data(){
    return{
      loading: false,
      isPlaying: false,
      activeName: 'first',
      activeIndex: "1",
      fullscreenLoading: false,
      UserFilled,
      ArrowDown,
      studentNumber: window.sessionStorage.getItem('studentNumber') || '未知用户',
      paymentData: {
        status: '未缴费',
        paymentId: '', // amountcard
        userName: '', // studentName
        tuitionFee: 0, // major表中的departmentPrize字段
        accommodationFee: 0, // dorm表中的dormPrize字段
        insuranceFee: 300, // 医保费固定为300
        receiptUrl: '' // img 图片路径
      }
    }
  },
  computed: {
    // 动态计算总金额
    totalAmount() {
      return this.paymentData.tuitionFee + this.paymentData.accommodationFee + this.paymentData.insuranceFee;
    }
  },
  created() {
    this.fetchPaymentData();
  },
  methods:{
    // 格式化金额为人民币格式
    formatCurrency(amount) {
      return '¥ ' + amount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    },
    // 获取缴费数据
    fetchPaymentData() {
      this.loading = true;
      // 使用新的综合查询接口获取学生缴费信息
      axios.get('/api/student/receipt/payment-info', {
        params: {
          studentNumber: this.studentNumber
        }
      })
      .then(response => {
        if (response.data.code === 200) {
          const data = response.data.data;
          this.paymentData = {
            status: data.indentStatue || '未缴费',
            paymentId: data.amountcard || '',
            userName: data.studentName || '',
            tuitionFee: data.tuitionFee || 0, // 从major表获取的学费
            accommodationFee: data.accommodationFee || 0, // 从dorm表获取的宿舍费
            insuranceFee: data.insuranceFee || 300, // 医保费，从后端获取，默认300
            receiptUrl: data.img || ''
          };
          
          // 调试信息，确保数据正确加载
          console.log('缴费数据加载成功:', this.paymentData);
        } else {
          ElMessage.error(response.data.msg || '获取缴费信息失败');
        }
      })
      .catch(error => {
        console.error('获取缴费信息失败:', error);
        ElMessage.error('获取缴费信息失败，请稍后重试');
      })
      .finally(() => {
        this.loading = false;
      });
    },
    goBack(){
      router.push('/freshmanreport')
    },
    toStudentHome() {
      router.push('/shome');
    },
    handleLogout() {
      // 清除会话存储中的所有信息
      window.sessionStorage.removeItem('token');
      window.sessionStorage.removeItem('role');
      window.sessionStorage.removeItem('userRole');
      window.sessionStorage.removeItem('studentNumber');
      window.sessionStorage.removeItem('studentName');
      
      ElMessage.success('已成功退出系统');
      
      // 跳转到登录页
      router.push('/login');
    },
    pay() {
      // 添加前端验证
      if (this.paymentData.accommodationFee === 0) {
        ElMessage.warning('请先选择宿舍后再进行缴费');
        return;
      }
      
      if (this.paymentData.tuitionFee === 0) {
        ElMessage.warning('学费信息有误，请联系管理员');
        return;
      }

      this.$confirm('是否确认支付?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.fullscreenLoading = true;
        
        // 调用支付接口 - 使用新的缴费单生成接口
        axios.post('/api/student/receipt/generate', {
          studentNumber: this.studentNumber,
          amountcard: this.paymentData.paymentId,
          hallway: '直行通道',
          allmoney: this.totalAmount // 使用计算属性获取总金额
        })
        .then(response => {
          if (response.data.code === 200) {
            // 支付成功，更新状态
            this.paymentData.status = '已缴费';
            this.paymentData.receiptUrl = response.data.data.img;
            
            // 刷新缴费数据
            this.fetchPaymentData();
            
            // 自动下载缴费单
            this.downloadReceipt();
            
            ElMessage.success('支付成功!');
          } else {
            ElMessage.error(response.data.msg || '支付失败');
          }
        })
        .catch(error => {
          console.error('支付失败:', error);
          ElMessage.error('支付处理失败，请稍后重试');
        })
        .finally(() => {
          this.fullscreenLoading = false;
        });
      }).catch(() => {
        ElMessage.info('已取消支付');
      });
    },
    // 下载缴费单
    downloadReceipt() {
      if (!this.paymentData.receiptUrl) {
        ElMessage.warning('未找到缴费单，请联系管理员');
        return;
      }
      
      this.loading = true;
      console.log('准备下载缴费单，路径:', this.paymentData.receiptUrl);
      
      // 创建下载链接 - 使用新的下载接口
      axios({
        url: `/api/student/receipt/download?filePath=${encodeURIComponent(this.paymentData.receiptUrl)}`,
        method: 'GET',
        responseType: 'blob'
      })
      .then(response => {
        // 检查响应类型
        const contentType = response.headers['content-type'];
        if (contentType && contentType.includes('application/json')) {
          // 如果是JSON响应，可能是错误信息
          const reader = new FileReader();
          reader.onload = () => {
            try {
              const errorData = JSON.parse(reader.result);
              ElMessage.error(errorData.msg || '下载缴费单失败');
            } catch (e) {
              ElMessage.error('下载缴费单失败：无法解析错误信息');
            }
          };
          reader.readAsText(response.data);
          return;
        }
        
        // 正常处理文件下载
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', `缴费单_${this.studentNumber}_${new Date().getTime()}.jpg`);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        ElMessage.success('缴费单下载成功');
      })
      .catch(error => {
        console.error('下载缴费单失败:', error);
        ElMessage.error(error.response?.data?.msg || '下载缴费单失败，请稍后重试');
      })
      .finally(() => {
        this.loading = false;
      });
    },
    jump(){
      router.push('/freshmanreport');
    }
  }
}
</script>

<style scoped>
/*表格*/
::v-deep .el-descriptions__header{
  justify-content: center;
}

/* 用户头像样式 */
.user-avatar {
  display: flex;
  align-items: center;
  margin-right: 20px;
  height: 60px;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.user-avatar .el-avatar {
  background-color: #fff;
  color: #244cb2;
  margin-right: 5px;
  transition: transform 0.3s;
}

.user-avatar:hover .el-avatar {
  transform: scale(1.1);
}

/* 下拉菜单箭头颜色 */
.el-icon--right {
  color: white;
}

/* 按钮样式 */
.action-buttons {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}
</style>