<template>
  <div class="teacher-manage">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>教师管理</span>
          <el-button type="primary" @click="handleAdd">添加教师</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <div class="search-bar">
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

      <!-- 教师列表 -->
      <el-table :data="teacherList" style="width: 100%" v-loading="loading">
        <el-table-column prop="teacherNumber" label="工号" width="120" />
        <el-table-column prop="teacherName" label="姓名" width="120" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="scope">
            {{ scope.row.gender === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="department" label="所属院系" width="150" />
        <el-table-column prop="title" label="职称" width="120" />
        <el-table-column prop="phone" label="联系电话" width="150" />
        <el-table-column prop="email" label="邮箱" width="200" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button-group>
              <el-button type="primary" @click="handleEdit(scope.row)" size="small">编辑</el-button>
              <el-button type="danger" @click="handleDelete(scope.row)" size="small">删除</el-button>
            </el-button-group>
          </template>
        </el-table-column>
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

    <!-- 添加/编辑教师对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加教师' : '编辑教师'"
      width="500px"
    >
      <el-form :model="teacherForm" :rules="rules" ref="teacherFormRef" label-width="100px">
        <el-form-item label="工号" prop="teacherNumber">
          <el-input v-model="teacherForm.teacherNumber" placeholder="请输入工号" />
        </el-form-item>
        <el-form-item label="姓名" prop="teacherName">
          <el-input v-model="teacherForm.teacherName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="teacherForm.gender">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="2">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="所属院系" prop="department">
          <el-input v-model="teacherForm.department" placeholder="请输入所属院系" />
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-input v-model="teacherForm.title" placeholder="请输入职称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="teacherForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="teacherForm.email" placeholder="请输入邮箱" />
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

// 数据列表
const teacherList = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchKeyword = ref('')

// 对话框相关
const dialogVisible = ref(false)
const dialogType = ref('add')
const teacherFormRef = ref(null)
const teacherForm = ref({
  teacherNumber: '',
  teacherName: '',
  gender: 1,
  department: '',
  title: '',
  phone: '',
  email: ''
})

// 表单验证规则
const rules = {
  teacherNumber: [{ required: true, message: '请输入工号', trigger: 'blur' }],
  teacherName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  department: [{ required: true, message: '请输入所属院系', trigger: 'blur' }],
  title: [{ required: true, message: '请输入职称', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

// 获取教师列表
const getTeacherList = async () => {
  loading.value = true
  try {
    // TODO: 调用后端API获取教师列表
    // const res = await getTeacherListAPI({
    //   page: currentPage.value,
    //   size: pageSize.value,
    //   keyword: searchKeyword.value
    // })
    // teacherList.value = res.data.list
    // total.value = res.data.total
  } catch (error) {
    console.error('获取教师列表失败：', error)
    ElMessage.error('获取教师列表失败')
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

// 添加教师
const handleAdd = () => {
  dialogType.value = 'add'
  teacherForm.value = {
    teacherNumber: '',
    teacherName: '',
    gender: 1,
    department: '',
    title: '',
    phone: '',
    email: ''
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
      // TODO: 调用后端API删除教师
      // await deleteTeacherAPI(row.id)
      ElMessage.success('删除成功')
      getTeacherList()
    } catch (error) {
      console.error('删除教师失败：', error)
      ElMessage.error('删除教师失败')
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!teacherFormRef.value) return

  await teacherFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // TODO: 调用后端API提交表单
        // if (dialogType.value === 'add') {
        //   await addTeacherAPI(teacherForm.value)
        // } else {
        //   await updateTeacherAPI(teacherForm.value)
        // }
        ElMessage.success(dialogType.value === 'add' ? '添加成功' : '更新成功')
        dialogVisible.value = false
        getTeacherList()
      } catch (error) {
        console.error('提交表单失败：', error)
        ElMessage.error('提交失败')
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
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>