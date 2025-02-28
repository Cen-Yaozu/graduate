<template>
  <div class="payment-manage">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>缴费管理</span>
        </div>
      </template>

      <!-- 筛选选项 -->
      <div class="filter-container">
        <div class="filter-group">
          <span class="filter-label">缴费状态：</span>
          <el-radio-group v-model="paymentStatusFilter" @change="handleFilterChange" class="status-filter">
            <el-radio-button label="all">缴费记录</el-radio-button>
            <el-radio-button label="已缴费">已缴费</el-radio-button>
            <el-radio-button label="未缴费">未缴费</el-radio-button>
          </el-radio-group>
        </div>
        
        <!-- 搜索栏 -->
        <el-input
          v-model="searchKeyword"
          placeholder="请输入学号/姓名进行搜索"
          class="search-input"
          clearable
          @clear="handleSearch"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
      </div>

      <!-- 缴费记录列表 -->
      <el-table :data="paymentList" style="width: 100%" v-loading="loading" border>
        <el-table-column prop="studentNumber" label="学号" min-width="120">
          <template #default="scope">
            {{ scope.row?.studentNumber || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="studentName" label="姓名" min-width="100">
          <template #default="scope">
            {{ scope.row?.studentName || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="hallway" label="缴费通道" min-width="120">
          <template #default="scope">
            {{ scope.row?.hallway || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="amountcard" label="缴费编号" min-width="120">
          <template #default="scope">
            {{ scope.row?.amountcard || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="allmoney" label="金额" min-width="100">
          <template #default="scope">
            {{ scope.row?.allmoney ? `¥${scope.row.allmoney}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="indentStatue" label="状态" min-width="100">
          <template #default="scope">
            <el-tag v-if="scope.row?.indentStatue" :type="scope.row.indentStatue === '已缴费' ? 'success' : 'warning'">
              {{ scope.row.indentStatue }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150">
          <template #default="scope">
            {{ scope.row?.remark || '-' }}
          </template>
        </el-table-column>
        
        <!-- 自定义空数据显示 -->
        <template #empty>
          <div class="empty-data">
            <el-empty :description="getEmptyDescription()" />
          </div>
        </template>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

// 数据列表
const paymentList = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchKeyword = ref('')
const paymentStatusFilter = ref('all') // 缴费状态筛选

// 根据当前筛选状态返回空数据描述
const getEmptyDescription = () => {
  if (loading.value) return '加载中...';
  
  if (searchKeyword.value) {
    return `没有找到符合"${searchKeyword.value}"的记录`;
  }
  
  switch (paymentStatusFilter.value) {
    case 'all':
      return '暂无缴费记录';
    case '已缴费':
      return '暂无已缴费学生';
    case '未缴费':
      return '暂无未缴费学生';
    default:
      return '暂无数据';
  }
}

// 获取缴费记录列表
const getPaymentList = async () => {
  loading.value = true;
  paymentList.value = []; // 清空之前的数据，避免出现旧数据
  
  try {
    // 构建查询参数
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchKeyword.value
    }
    
    // 使用统一的API端点
    const url = '/api/admin/payment/all-payment-records';
    
    // 根据筛选选项设置相应的参数
    if (paymentStatusFilter.value !== 'all') {
      if (paymentStatusFilter.value === '已缴费') {
        params.status = 1;
      } else if (paymentStatusFilter.value === '未缴费') {
        params.status = 0;
      }
    }
    // 对于"缴费记录"选项卡，不传递status参数
    
    console.log('发送请求参数:', params);
    const res = await axios.get(url, { params });
    console.log('API返回的数据:', res.data);
    
    if (res.data && res.data.code === 200 && res.data.data) {
      // 获取列表数据
      const list = Array.isArray(res.data.data.list) ? res.data.data.list : [];
      
      // 处理数据格式，确保所有记录都有必要的字段
      paymentList.value = list.map(item => {
        if (!item) return null;
        
        return {
          ...item,
          studentNumber: item.studentNumber || '',
          studentName: item.studentName || '',
          hallway: item.hallway || '',
          amountcard: item.amountcard || '',
          allmoney: typeof item.allmoney === 'number' ? item.allmoney : null,
          indentStatue: item.indentStatue || '',
          remark: item.remark || ''
        };
      }).filter(item => item !== null); // 过滤掉可能的null值
      
      total.value = res.data.data.total || 0;
    } else {
      ElMessage.error(res.data?.msg || '获取缴费记录列表失败');
      total.value = 0;
    }
  } catch (error) {
    console.error('获取缴费记录列表失败：', error);
    ElMessage.error(error.response?.data?.msg || '网络异常，请稍后重试');
    total.value = 0;
  } finally {
    loading.value = false;
  }
}

// 筛选状态变化处理
const handleFilterChange = () => {
  currentPage.value = 1
  getPaymentList()
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  getPaymentList()
}

// 分页相关
const handleSizeChange = (val) => {
  pageSize.value = val
  getPaymentList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getPaymentList()
}

onMounted(() => {
  getPaymentList()
})
</script>

<style scoped>
.payment-manage {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-group {
  display: flex;
  align-items: center;
}

.filter-label {
  font-weight: bold;
  margin-right: 10px;
  color: #606266;
}

.status-filter {
  margin-right: 10px;
}

.search-input {
  width: 300px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.empty-data {
  padding: 30px 0;
}
</style>