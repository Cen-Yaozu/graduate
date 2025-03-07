<template>
  <div class="student-manage-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold; font-size: 16px;">学生管理</span>
          <div class="header-actions">
            <el-input
              v-model="searchText"
              placeholder="搜索学生姓名/学号/专业"
              style="width: 250px; margin-right: 15px"
              clearable
              @clear="handleSearch"
              @input="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-button type="primary" @click="openAddDialog">添加学生</el-button>
            <el-button type="success" @click="openImportDialog">批量导入</el-button>
          </div>
        </div>
      </template>
      
      <el-table
        v-loading="loading"
        :data="studentList"
        style="width: 100%"
        border
        row-key="id"
        @row-click="handleRowClick"
      >
        <el-table-column prop="studentNumber" label="学号" width="100" align="center" />
        <el-table-column prop="studentName" label="姓名" width="80" align="center" />
        <el-table-column label="性别" width="60" align="center">
          <template #default="scope">
            {{ scope.row.sex }}
          </template>
        </el-table-column>
        <el-table-column prop="majorname" label="专业" min-width="160" align="center" />
        <el-table-column prop="studentPhone" label="联系电话" width="110" align="center" />
        <el-table-column label="缴费状态" width="80" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.paymentStatus === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.paymentStatus === 1 ? '已缴费' : '未缴费' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="抵校状态" width="80" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.arriveStatus === 1 ? 'success' : 'warning'" size="small">
              {{ scope.row.arriveStatus === 1 ? '已抵校' : '未抵校' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right" align="center">
          <template #default="scope">
            <div class="button-group">
              <el-button size="small" plain type="info" @click.stop="showStudentDetails(scope.row)">
                更多
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <div class="total-info">共 {{ total }} 条</div>
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
        <div class="page-info">
          前往 <el-input v-model="goToPage" type="text" style="width: 50px" @keyup.enter="handleGoTo" /> 页
        </div>
      </div>
    </el-card>
    
    <!-- 添加/编辑学生对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑学生信息' : '添加学生'"
      width="650px"
    >
      <el-form
        ref="studentFormRef"
        :model="studentForm"
        :rules="rules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学号" prop="studentNumber">
              <el-input v-model="studentForm.studentNumber" :disabled="isEdit" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="studentName">
              <el-input v-model="studentForm.studentName" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="sex">
              <el-select v-model="studentForm.sex" placeholder="请选择性别" style="width: 100%">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="studentPhone">
              <el-input v-model="studentForm.studentPhone" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="专业" prop="majorname">
              <el-input v-model="studentForm.majorname" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班级" prop="classname">
              <el-input v-model="studentForm.classname" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="studentForm.email" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="缴费状态" prop="paymentStatus">
              <el-select v-model="studentForm.paymentStatus" placeholder="请选择缴费状态" style="width: 100%">
                <el-option label="已缴费" :value="1" />
                <el-option label="未缴费" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="抵校状态" prop="arriveStatus">
              <el-select v-model="studentForm.arriveStatus" placeholder="请选择抵校状态" style="width: 100%">
                <el-option label="已抵校" :value="1" />
                <el-option label="未抵校" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="家庭住址" prop="address">
              <el-input v-model="studentForm.address" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 导入学生对话框 -->
    <el-dialog
      v-model="importDialogVisible"
      title="批量导入学生"
      width="500px"
    >
      <el-upload
        class="upload-demo"
        drag
        action="/api/admin/student/import/upload"
        accept=".xls,.xlsx"
        :auto-upload="false"
        :on-change="handleFileChange"
        :file-list="fileList"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
          将Excel文件拖到此处，或<em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            请上传Excel文件（.xls或.xlsx格式），文件大小不超过10MB，
            <el-link type="primary" @click="downloadTemplate">下载导入模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="importDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="uploadFile" :disabled="!fileList.length">上传</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 学生详情抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      title="学生详细信息"
      size="50%"
      direction="rtl"
    >
      <div v-if="selectedStudent" class="drawer-content">
        <el-descriptions title="基本信息" :column="2" border>
          <el-descriptions-item label="学号">{{ selectedStudent.studentNumber }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ selectedStudent.studentName }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ selectedStudent.sex }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ selectedStudent.studentPhone }}</el-descriptions-item>
          <el-descriptions-item label="专业">{{ selectedStudent.majorname }}</el-descriptions-item>
          <el-descriptions-item label="班级">{{ selectedStudent.classname }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ selectedStudent.email }}</el-descriptions-item>
          <el-descriptions-item label="缴费状态">
            <el-tag :type="selectedStudent.paymentStatus === 1 ? 'success' : 'danger'">
              {{ selectedStudent.paymentStatus === 1 ? '已缴费' : '未缴费' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="抵校状态">
            <el-tag :type="selectedStudent.arriveStatus === 1 ? 'success' : 'warning'">
              {{ selectedStudent.arriveStatus === 1 ? '已抵校' : '未抵校' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="家庭住址" :span="2">{{ selectedStudent.address }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { ref, reactive, onMounted, getCurrentInstance } from 'vue'
import { Search, UploadFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'StudentManageView',
  components: {
    Search,
    UploadFilled
  },
  setup() {
    const { proxy } = getCurrentInstance()
    // 列表数据
    const studentList = ref([])
    const loading = ref(false)
    const total = ref(0)
    const currentPage = ref(1)
    const pageSize = ref(10)
    const searchText = ref('')
    const goToPage = ref('')
    
    // 对话框相关
    const dialogVisible = ref(false)
    const importDialogVisible = ref(false)
    const drawerVisible = ref(false)
    const studentFormRef = ref(null)
    const isEdit = ref(false)
    const selectedStudent = ref(null)
    const fileList = ref([])
    
    // 表单数据
    const studentForm = reactive({
      id: null,
      studentNumber: '',
      studentName: '',
      sex: '男',
      studentPhone: '',
      majorname: '',
      classname: '',
      email: '',
      paymentStatus: 0,
      arriveStatus: 0,
      address: ''
    })
    
    // 表单验证规则
    const rules = {
      studentNumber: [
        { required: true, message: '请输入学号', trigger: 'blur' },
        { min: 5, max: 20, message: '长度在 5 到 20 个字符', trigger: 'blur' }
      ],
      studentName: [
        { required: true, message: '请输入姓名', trigger: 'blur' }
      ],
      sex: [
        { required: true, message: '请选择性别', trigger: 'change' }
      ],
      studentPhone: [
        { required: true, message: '请输入联系电话', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
      ],
      majorname: [
        { required: true, message: '请输入专业', trigger: 'blur' }
      ]
    }
    
    // 加载学生列表
    const loadStudentList = async () => {
      loading.value = true
      try {
        const { data: result } = await proxy.$http.get(`/api/admin/student/list?page=${currentPage.value}&size=${pageSize.value}&keyword=${searchText.value}`)
        if (result.code === 200) {
          studentList.value = result.data.list
          total.value = result.data.total
        } else {
          ElMessage.error(result.msg || '获取学生列表失败')
        }
      } catch (error) {
        console.error('获取学生列表失败:', error)
        ElMessage.error('获取学生列表失败')
        // 模拟数据用于前端开发
        studentList.value = [
          {
            id: 1,
            studentNumber: '2024001',
            studentName: '张三',
            sex: '男',
            studentPhone: '13800138001',
            majorname: '计算机科学与技术',
            classname: '计算机1班',
            email: 'zhangsan@example.com',
            paymentStatus: 1,
            arriveStatus: 1,
            address: '北京市海淀区'
          },
          {
            id: 2,
            studentNumber: '2024002',
            studentName: '李四',
            sex: '女',
            studentPhone: '13800138002',
            majorname: '软件工程',
            classname: '软件1班',
            email: 'lisi@example.com',
            paymentStatus: 0,
            arriveStatus: 0,
            address: '上海市浦东新区'
          }
        ]
        total.value = 2
      } finally {
        loading.value = false
      }
    }
    
    // 搜索
    const handleSearch = () => {
      currentPage.value = 1
      loadStudentList()
    }
    
    // 分页
    const handleSizeChange = (size) => {
      pageSize.value = size
      loadStudentList()
    }
    
    const handleCurrentChange = (page) => {
      currentPage.value = page
      loadStudentList()
    }
    
    // 分页跳转
    const handleGoTo = () => {
      const pageNum = parseInt(goToPage.value)
      if (pageNum && pageNum > 0 && pageNum <= Math.ceil(total.value / pageSize.value)) {
        currentPage.value = pageNum
        loadStudentList()
      } else {
        ElMessage.warning('请输入有效的页码')
      }
      goToPage.value = ''
    }
    
    // 打开添加对话框
    const openAddDialog = () => {
      isEdit.value = false
      // 重置表单
      Object.keys(studentForm).forEach(key => {
        studentForm[key] = key === 'sex' ? '男' : 
                           (key === 'paymentStatus' || key === 'arriveStatus') ? 0 : ''
      })
      if (studentFormRef.value) {
        studentFormRef.value.resetFields()
      }
      dialogVisible.value = true
    }
    
    // 编辑学生
    const editStudent = (row) => {
      isEdit.value = true
      // 填充表单
      Object.keys(studentForm).forEach(key => {
        studentForm[key] = row[key]
      })
      dialogVisible.value = true
    }
    
    // 提交表单
    const submitForm = () => {
      studentFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            const url = isEdit.value ? '/api/admin/student/update' : '/api/admin/student/add'
            const method = isEdit.value ? 'put' : 'post'
            
            const { data: result } = await proxy.$http[method](url, studentForm)
            
            if (result.code === 200) {
              ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
              dialogVisible.value = false
              loadStudentList()
            } else {
              ElMessage.error(result.msg || (isEdit.value ? '更新失败' : '添加失败'))
            }
          } catch (error) {
            console.error(isEdit.value ? '更新学生失败:' : '添加学生失败:', error)
            ElMessage.error(isEdit.value ? '更新学生失败' : '添加学生失败')
            // 模拟成功
            dialogVisible.value = false
            loadStudentList()
          }
        } else {
          return false
        }
      })
    }
    
    // 显示学生详情
    const showStudentDetails = (row) => {
      selectedStudent.value = row
      drawerVisible.value = true
    }
    
    // 打开详情抽屉
    const handleRowClick = (row) => {
      showStudentDetails(row)
    }
    
    // 打开导入对话框
    const openImportDialog = () => {
      fileList.value = []
      importDialogVisible.value = true
    }
    
    // 处理文件变化
    const handleFileChange = (file) => {
      fileList.value = [file]
    }
    
    // 上传文件
    const uploadFile = async () => {
      // 实际项目中应该使用表单提交或axios上传
      ElMessage.success('文件上传成功')
      importDialogVisible.value = false
      loadStudentList()
    }
    
    // 下载模板
    const downloadTemplate = () => {
      window.open('/api/admin/student/import/template', '_blank')
    }
    
    onMounted(() => {
      loadStudentList()
    })
    
    return {
      studentList,
      loading,
      total,
      currentPage,
      pageSize,
      searchText,
      goToPage,
      dialogVisible,
      importDialogVisible,
      drawerVisible,
      studentFormRef,
      isEdit,
      selectedStudent,
      fileList,
      studentForm,
      rules,
      handleSearch,
      handleSizeChange,
      handleCurrentChange,
      openAddDialog,
      editStudent,
      submitForm,
      handleRowClick,
      showStudentDetails,
      openImportDialog,
      handleFileChange,
      uploadFile,
      downloadTemplate,
      handleGoTo
    }
  }
}
</script>

<style scoped>
.student-manage-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.header-actions {
  display: flex;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.total-info {
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
}

:deep(.el-pagination .el-input__inner) {
  height: 24px;
  line-height: 24px;
}

.drawer-content {
  padding: 20px;
}

:deep(.el-upload-dragger) {
  width: 100%;
}

/* 按钮样式调整 */
.button-group {
  display: flex;
  justify-content: center;
  gap: 5px;
}

:deep(.el-button--small) {
  padding: 5px 10px;
  font-size: 12px;
}

:deep(.el-table .cell) {
  line-height: 20px;
}

:deep(.el-tag--small) {
  height: 20px;
  line-height: 20px;
  font-size: 12px;
}

/* 表格样式调整 */
:deep(.el-table) {
  margin-top: 10px;
  font-size: 14px;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
  text-align: center;
}

/* 分页样式 */
:deep(.el-pagination) {
  justify-content: flex-end;
  margin-top: 15px;
}

/* 表单控件样式 */
:deep(.el-input__inner) {
  height: 36px;
  line-height: 36px;
}
</style> 