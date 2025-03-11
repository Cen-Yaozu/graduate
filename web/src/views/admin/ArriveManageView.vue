<template>
  <div class="arrive-manage-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold; font-size: 18px;">抵校管理</span>
          <div class="header-actions">
            <el-input
              v-model="searchText"
              placeholder="搜索学号/姓名"
              style="width: 250px; margin-right: 15px"
              clearable
              @clear="handleSearch"
              @input="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :shortcuts="dateShortcuts"
              style="margin-right: 15px"
              @change="handleDateRangeChange"
            />
            <el-button type="primary" @click="handleSearch">查询</el-button>
          </div>
        </div>
      </template>
      
      <!-- 抵校数据表格 -->
      <el-table
        v-loading="loading"
        :data="arriveList"
        style="width: 100%"
        border
        row-key="studentNumber"
        :header-cell-style="{
          background: '#f5f7fa',
          color: '#606266',
          fontWeight: '600',
          height: '55px'
        }"
        :cell-style="{
          padding: '12px 0',
          height: '60px'
        }"
        :row-class-name="tableRowClassName"
        highlight-current-row
      >
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="studentNumber" label="学号" min-width="120" align="center" />
        <el-table-column prop="studentName" label="姓名" min-width="100" align="center" />
        <el-table-column prop="date" label="抵校日期" min-width="120" align="center">
          <template #default="scope">
            {{ formatDate(scope.row.date) }}
          </template>
        </el-table-column>
        <el-table-column prop="time" label="抵校时间" min-width="120" align="center" />
        <el-table-column prop="tool" label="交通工具" min-width="120" align="center" />
        <el-table-column prop="familyNum" label="陪同家人数" min-width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.familyNum > 0 ? 'success' : 'info'" size="small">
              {{ scope.row.familyNum }}人
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <!-- 没有数据时显示 -->
      <el-empty v-if="arriveList.length === 0" description="暂无抵校数据" />
      
      <!-- 分页 -->
      <div class="pagination-container">
        <span class="total-text">共 {{ total }} 条</span>
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
        />
        <div class="page-info">
          <span>前往</span>
          <el-input v-model="goToPage" type="text" style="width: 50px" @keyup.enter="handleGoTo" />
          <span>页</span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted, getCurrentInstance } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'ArriveManageView',
  components: {
    Search
  },
  setup() {
    const { proxy } = getCurrentInstance()
    
    // 列表数据
    const arriveList = ref([])
    const loading = ref(false)
    const total = ref(0)
    const currentPage = ref(1)
    const pageSize = ref(10)
    const searchText = ref('')
    const goToPage = ref('')
    
    // 日期范围
    const dateRange = ref(null)
    const dateShortcuts = [
      {
        text: '最近一周',
        value: () => {
          const end = new Date()
          const start = new Date()
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
          return [start, end]
        },
      },
      {
        text: '最近一个月',
        value: () => {
          const end = new Date()
          const start = new Date()
          start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
          return [start, end]
        },
      },
      {
        text: '本学期',
        value: () => {
          // 假设本学期从2月开始到7月结束，或从9月开始到次年1月结束
          const now = new Date()
          const year = now.getFullYear()
          const month = now.getMonth() + 1
          
          let start, end
          if (month >= 2 && month <= 7) {
            // 春季学期
            start = new Date(year, 1, 1) // 2月1日
            end = new Date(year, 6, 31) // 7月31日
          } else {
            // 秋季学期
            start = new Date(year, 8, 1) // 9月1日
            if (month >= 9) {
              end = new Date(year + 1, 0, 31) // 次年1月31日
            } else {
              end = new Date(year, 0, 31) // 1月31日
              start = new Date(year - 1, 8, 1) // 前一年9月1日
            }
          }
          return [start, end]
        },
      }
    ]
    
    // 表格行样式
    const tableRowClassName = ({ rowIndex }) => {
      return rowIndex % 2 === 0 ? 'even-row' : 'odd-row'
    }
    
    // 加载抵校记录
    const loadArriveList = async () => {
      loading.value = true
      
      // 构建查询参数
      const params = {
        page: currentPage.value,
        size: pageSize.value
      }
      
      // 添加关键词搜索
      if (searchText.value) {
        params.keyword = searchText.value
      }
      
      // 添加日期范围
      if (dateRange.value && dateRange.value.length === 2) {
        params.startDate = formatDateForApi(dateRange.value[0])
        params.endDate = formatDateForApi(dateRange.value[1])
      }
      
      try {
        const { data: result } = await proxy.$http.get('/api/admin/arrive/list', { params })
        
        if (result.code === 200) {
          arriveList.value = result.data.list
          total.value = result.data.total
        } else {
          ElMessage.error(result.msg || '获取抵校记录失败')
        }
      } catch (error) {
        console.error('获取抵校记录失败:', error)
        ElMessage.error('获取抵校记录失败')
        
        // 模拟数据（仅供前端开发阶段使用）
        arriveList.value = [
          {
            studentNumber: '2024001',
            name: '张三',
            date: '2024-08-31',
            time: '09:30:45',
            tool: '高铁',
            familyNum: 2
          },
          {
            studentNumber: '2024002',
            name: '李四',
            date: '2024-08-31',
            time: '10:15:22',
            tool: '飞机',
            familyNum: 1
          },
          {
            studentNumber: '2024003',
            name: '王五',
            date: '2024-08-31',
            time: '11:45:18',
            tool: '汽车',
            familyNum: 2
          },
          {
            studentNumber: '2024004',
            name: '赵六',
            date: '2024-08-31',
            time: '12:20:35',
            tool: '高铁',
            familyNum: 0
          }
        ]
        total.value = 100
      } finally {
        loading.value = false
      }
    }
    
    // 处理搜索
    const handleSearch = () => {
      currentPage.value = 1
      loadArriveList()
    }
    
    // 处理日期范围变化
    const handleDateRangeChange = () => {
      currentPage.value = 1
      loadArriveList()
    }
    
    // 分页处理
    const handleSizeChange = (size) => {
      pageSize.value = size
      loadArriveList()
    }
    
    const handleCurrentChange = (page) => {
      currentPage.value = page
      loadArriveList()
    }
    
    // 跳转到指定页
    const handleGoTo = () => {
      const pageNum = parseInt(goToPage.value)
      if (pageNum && pageNum > 0 && pageNum <= Math.ceil(total.value / pageSize.value)) {
        currentPage.value = pageNum
        loadArriveList()
      } else {
        ElMessage.warning('请输入有效的页码')
      }
      goToPage.value = ''
    }
    
    // 格式化日期
    const formatDate = (dateStr) => {
      if (!dateStr) return ''
      // 如果是字符串格式，直接返回
      if (typeof dateStr === 'string') {
        // 仅保留日期部分
        if (dateStr.includes('T')) {
          return dateStr.split('T')[0]
        }
        return dateStr
      }
      // 如果是Date对象
      try {
        const date = new Date(dateStr)
        return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
      } catch (e) {
        return dateStr
      }
    }
    
    // 为API格式化日期
    const formatDateForApi = (date) => {
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
    }
    
    onMounted(() => {
      loadArriveList()
    })
    
    return {
      arriveList,
      loading,
      total,
      currentPage,
      pageSize,
      searchText,
      goToPage,
      dateRange,
      dateShortcuts,
      tableRowClassName,
      handleSearch,
      handleDateRangeChange,
      handleSizeChange,
      handleCurrentChange,
      handleGoTo,
      formatDate
    }
  }
}
</script>

<style scoped>
.arrive-manage-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-actions {
  display: flex;
  align-items: center;
}

/* 表格样式优化 */
:deep(.el-table) {
  --el-table-border-color: #ebeef5;
  --el-table-header-bg-color: #f5f7fa;
  --el-table-row-hover-bg-color: #f5f7fa;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

:deep(.el-table::before) {
  display: none; /* 去除默认底部边框 */
}

:deep(.el-table__header) {
  border: none;
}

:deep(.el-table__cell) {
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-table .even-row) {
  background-color: #fafafa;
}

:deep(.el-table .odd-row) {
  background-color: #ffffff;
}

:deep(.el-table .cell) {
  padding: 0 10px;
}

/* 分页容器样式 */
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 10px 0;
}

.total-text {
  margin-right: 15px;
  color: #606266;
  font-size: 13px;
}

.page-info {
  margin-left: 15px;
  color: #606266;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 5px;
}

:deep(.el-pagination) {
  margin: 0;
  padding: 0;
}

:deep(.el-pagination .el-input__inner) {
  height: 28px;
  line-height: 28px;
}
</style> 