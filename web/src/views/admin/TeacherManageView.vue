<template>
  <div class="teacher-manage-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold; font-size: 18px;">教师管理</span>
          <div class="header-actions">
            <el-input
              v-model="searchKeyword"
              placeholder="请输入教师姓名/工号进行搜索"
              style="width: 250px; margin-right: 15px"
              clearable
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-button type="primary" @click="handleAdd">添加教师</el-button>
          </div>
        </div>
      </template>
      
      <!-- 教师数据表格 -->
      <el-table
        v-loading="loading"
        :data="teacherList"
        style="width: 100%"
        border
        row-key="teacherNumber"
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
        <el-table-column prop="teacherNumber" label="工号" min-width="120" align="center" />
        <el-table-column prop="teacherName" label="姓名" min-width="120" align="center" />
        <el-table-column prop="teacherPhone" label="联系电话" min-width="150" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="scope">
            <el-button type="primary" link size="small" @click="handleEdit(scope.row)">
              <el-icon><Edit /></el-icon>编辑
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(scope.row)">
              <el-icon><Delete /></el-icon>删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 没有数据时显示 -->
      <el-empty v-if="teacherList.length === 0 && !loading" description="暂无教师数据" />
      
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
          <el-input v-model="jumpPage" type="text" style="width: 50px" @keyup.enter="handleJumpPage" />
          <span>页</span>
        </div>
      </div>
    </el-card>

    <!-- 添加/编辑教师对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加教师' : '编辑教师'"
      width="500px"
    >
      <el-form :model="teacherForm" :rules="rules" ref="teacherFormRef" label-width="100px">
        <el-form-item label="工号" prop="teacherNumber">
          <el-input v-model="teacherForm.teacherNumber" placeholder="请输入工号" :disabled="dialogType === 'edit'" />
        </el-form-item>
        <el-form-item label="姓名" prop="teacherName">
          <el-input v-model="teacherForm.teacherName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="teacherPhone">
          <el-input v-model="teacherForm.teacherPhone" placeholder="请输入联系电话" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Search, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

// API基础路径
const baseURL = '/api/admin/teacher'

// 定义API请求函数
const api = {
  // 获取教师列表
  getTeacherPage(params) {
    return axios.get(`${baseURL}/page`, { params })
  },
  // 根据ID获取教师
  getTeacherById(id) {
    return axios.get(`${baseURL}/${id}`)
  },
  // 添加教师
  addTeacher(data) {
    return axios.post(baseURL, data)
  },
  // 更新教师
  updateTeacher(data) {
    return axios.put(baseURL, data)
  },
  // 删除教师
  deleteTeacher(id) {
    return axios.delete(`${baseURL}/${id}`)
  }
}

// 数据列表
const teacherList = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchKeyword = ref('')
const jumpPage = ref('')

// 对话框相关
const dialogVisible = ref(false)
const dialogType = ref('add')
const teacherFormRef = ref(null)
const teacherForm = ref({
  teacherNumber: '',
  teacherName: '',
  teacherPhone: ''
})

// 表单验证规则
const rules = {
  teacherNumber: [{ required: true, message: '请输入工号', trigger: 'blur' }],
  teacherName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  teacherPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 表格行样式
const tableRowClassName = ({ rowIndex }) => {
  return rowIndex % 2 === 0 ? 'even-row' : 'odd-row'
}

// 获取教师列表
const getTeacherList = async () => {
  loading.value = true
  try {
    const res = await api.getTeacherPage({
      current: currentPage.value,
      size: pageSize.value,
      keyword: searchKeyword.value
    })
    if (res.data.code === 200) {
      teacherList.value = res.data.data.records
      total.value = res.data.data.total
    } else {
      ElMessage.error(res.data.msg || '获取教师列表失败')
    }
  } catch (error) {
    console.error('获取教师列表失败：', error)
    if (error.response && error.response.status === 401) {
      ElMessage.error('未授权，请重新登录')
    } else {
      ElMessage.error('获取教师列表失败')
    }
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  getTeacherList()
}

// 分页相关
const handleSizeChange = (val) => {
  pageSize.value = val
  getTeacherList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getTeacherList()
}

// 页码跳转
const handleJumpPage = () => {
  const page = parseInt(jumpPage.value)
  if (page && page > 0 && page <= Math.ceil(total.value / pageSize.value)) {
    currentPage.value = page
    getTeacherList()
  } else {
    ElMessage.warning('请输入有效的页码')
  }
  jumpPage.value = ''
}

// 添加教师
const handleAdd = () => {
  dialogType.value = 'add'
  teacherForm.value = {
    teacherNumber: '',
    teacherName: '',
    teacherPhone: ''
  }
  dialogVisible.value = true
}

// 编辑教师
const handleEdit = (row) => {
  dialogType.value = 'edit'
  teacherForm.value = { ...row }
  dialogVisible.value = true
}

// 删除教师
const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除教师${row.teacherName}吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await api.deleteTeacher(row.teacherNumber)
      if (res.data.code === 200) {
        ElMessage.success('删除成功')
        getTeacherList()
      } else {
        ElMessage.error(res.data.msg || '删除教师失败')
      }
    } catch (error) {
      console.error('删除教师失败：', error)
      if (error.response && error.response.status === 401) {
        ElMessage.error('未授权，请重新登录')
      } else {
        ElMessage.error('删除教师失败')
      }
    }
  }).catch(() => {
    // 用户取消删除操作
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!teacherFormRef.value) return

  await teacherFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        let res
        if (dialogType.value === 'add') {
          res = await api.addTeacher(teacherForm.value)
        } else {
          res = await api.updateTeacher(teacherForm.value)
        }
        
        if (res.data.code === 200) {
          ElMessage.success(dialogType.value === 'add' ? '添加成功' : '更新成功')
          dialogVisible.value = false
          getTeacherList()
        } else {
          ElMessage.error(res.data.msg || '提交失败')
        }
      } catch (error) {
        console.error('提交表单失败：', error)
        if (error.response && error.response.status === 401) {
          ElMessage.error('未授权，请重新登录')
        } else {
          ElMessage.error('提交失败')
        }
      }
    }
  })
}

onMounted(() => {
  getTeacherList()
})
</script>

<style scoped>
.teacher-manage-container {
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