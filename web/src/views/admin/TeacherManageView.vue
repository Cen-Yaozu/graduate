<template>
  <div class="teacher-manage">
    <div class="header">
      <h2>教师管理</h2>
      <el-button type="primary" @click="handleAdd">添加教师</el-button>
    </div>

    <!-- 搜索栏 -->
    <div class="search-container">
      <el-input
        v-model="searchKeyword"
        placeholder="请输入教师姓名/工号进行搜索"
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

    <!-- 表格部分 -->
    <div class="table-container">
      <!-- 表头 -->
      <div class="table-header">
        <div class="header-item id-col">工号</div>
        <div class="header-item name-col">姓名</div>
        <div class="header-item phone-col">联系电话</div>
        <div class="header-item action-col">操作</div>
      </div>

      <!-- 表格内容 -->
      <div v-loading="loading" class="table-body">
        <div v-for="item in teacherList" :key="item.teacherNumber" class="table-row">
          <div class="cell id-col">{{ item.teacherNumber }}</div>
          <div class="cell name-col">{{ item.teacherName }}</div>
          <div class="cell phone-col">{{ item.teacherPhone }}</div>
          <div class="cell action-col">
            <el-button type="primary" size="small" @click="handleEdit(item)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(item)">删除</el-button>
          </div>
        </div>
        <div v-if="teacherList.length === 0 && !loading" class="empty-data">
          暂无数据
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-container">
        <div class="pagination-info">
          共 {{ total }} 条
        </div>
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          background
          layout="sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
        <div class="pagination-jumper">
          前往
          <el-input 
            v-model="jumpPage" 
            @keyup.enter="handleJumpPage"
            size="small"
            class="page-jump-input"
          ></el-input>
          页
        </div>
      </div>
    </div>

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
import { Search } from '@element-plus/icons-vue'
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
.teacher-manage {
  padding: 20px;
  background-color: #f8f8f8;
  min-height: calc(100vh - 60px);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 10px 20px;
  background-color: white;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.header h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

.search-container {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 15px;
}

.search-input {
  width: 320px;
}

.table-container {
  background-color: white;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

/* 表格样式 */
.table-header {
  display: flex;
  background-color: #f5f7fa;
  border-top: 1px solid #ebeef5;
  border-bottom: 1px solid #ebeef5;
}

.header-item {
  padding: 12px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-align: center;
  font-weight: 500;
  color: #606266;
}

.table-row {
  display: flex;
  border-bottom: 1px solid #ebeef5;
}

.table-row:hover {
  background-color: #f5f7fa;
}

.cell {
  padding: 12px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  text-align: center;
  color: #606266;
}

/* 列宽设置 */
.id-col {
  flex: 1;
  max-width: 120px;
}

.name-col {
  flex: 2;
  max-width: 180px;
}

.phone-col {
  flex: 2;
  max-width: 180px;
}

.action-col {
  flex: 2;
  min-width: 180px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.action-col .el-button {
  margin: 0 5px;
}

.empty-data {
  padding: 40px 0;
  text-align: center;
  color: #909399;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-info {
  color: #606266;
  font-size: 14px;
}

.pagination-jumper {
  display: flex;
  align-items: center;
  color: #606266;
  font-size: 14px;
}

.page-jump-input {
  width: 50px;
  margin: 0 5px;
}

:deep(.el-button) {
  margin-left: 5px;
  margin-right: 5px;
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background-color: #409eff;
}

:deep(.el-input__inner) {
  height: 36px;
}
</style>