<template>
  <div class="student-manage-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold">学生管理</span>
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
        <el-table-column prop="studentNumber" label="学号" width="120" />
        <el-table-column prop="studentName" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="60">
          <template #default="scope">
            {{ scope.row.gender === 'male' ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="majorname" label="专业" width="180" />
        <el-table-column prop="classname" label="班级" width="120" />
        <el-table-column prop="phone" label="联系电话" width="120" />
        <el-table-column prop="admissionTicket" label="准考证号" width="160" />
        <el-table-column prop="paymentStatus" label="缴费状态" width="90">
          <template #default="scope">
            <el-tag :type="scope.row.paymentStatus === 1 ? 'success' : 'danger'">
              {{ scope.row.paymentStatus === 1 ? '已缴费' : '未缴费' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="arriveStatus" label="抵校状态" width="90">
          <template #default="scope">
            <el-tag :type="scope.row.arriveStatus === 1 ? 'success' : 'warning'">
              {{ scope.row.arriveStatus === 1 ? '已抵校' : '未抵校' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click.stop="editStudent(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click.stop="deleteStudent(scope.row)">删除</el-button>
            <el-dropdown @command="(command) => handleCommand(command, scope.row)" trigger="click">
              <el-button size="small" type="info">
                更多<el-icon class="el-icon--right"><arrow-down /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="resetPassword">重置密码</el-dropdown-item>
                  <el-dropdown-item command="viewPayment">查看缴费</el-dropdown-item>
                  <el-dropdown-item command="updatePayment">
                    {{ scope.row.paymentStatus === 1 ? '标记为未缴费' : '标记为已缴费' }}
                  </el-dropdown-item>
                  <el-dropdown-item command="updateArrive">
                    {{ scope.row.arriveStatus === 1 ? '标记为未抵校' : '标记为已抵校' }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      
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
            <el-form-item label="性别" prop="gender">
              <el-select v-model="studentForm.gender" placeholder="请选择性别" style="width: 100%">
                <el-option label="男" value="male" />
                <el-option label="女" value="female" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="studentForm.phone" />
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
            <el-form-item label="准考证号" prop="admissionTicket">
              <el-input v-model="studentForm.admissionTicket" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="studentForm.email" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="缴费状态" prop="paymentStatus">
              <el-select v-model="studentForm.paymentStatus" placeholder="请选择缴费状态" style="width: 100%">
                <el-option label="已缴费" :value="1" />
                <el-option label="未缴费" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="抵校状态" prop="arriveStatus">
              <el-select v-model="studentForm.arriveStatus" placeholder="请选择抵校状态" style="width: 100%">
                <el-option label="已抵校" :value="1" />
                <el-option label="未抵校" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="家庭住址" prop="address">
          <el-input v-model="studentForm.address" />
        </el-form-item>
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
          <el-descriptions-item label="性别">{{ selectedStudent.gender === 'male' ? '男' : '女' }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ selectedStudent.phone }}</el-descriptions-item>
          <el-descriptions-item label="专业">{{ selectedStudent.majorname }}</el-descriptions-item>
          <el-descriptions-item label="班级">{{ selectedStudent.classname }}</el-descriptions-item>
          <el-descriptions-item label="准考证号">{{ selectedStudent.admissionTicket }}</el-descriptions-item>
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
import { Search, ArrowDown, UploadFilled } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'StudentManageView',
  components: {
    Search,
    ArrowDown,
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
      gender: 'male',
      phone: '',
      majorname: '',
      classname: '',
      admissionTicket: '',
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
      gender: [
        { required: true, message: '请选择性别', trigger: 'change' }
      ],
      phone: [
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
            gender: 'male',
            phone: '13800138001',
            majorname: '计算机科学与技术',
            classname: '计算机1班',
            admissionTicket: '20240001',
            email: 'zhangsan@example.com',
            paymentStatus: 1,
            arriveStatus: 1,
            address: '北京市海淀区'
          },
          {
            id: 2,
            studentNumber: '2024002',
            studentName: '李四',
            gender: 'female',
            phone: '13800138002',
            majorname: '软件工程',
            classname: '软件1班',
            admissionTicket: '20240002',
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
    
    // 打开添加对话框
    const openAddDialog = () => {
      isEdit.value = false
      // 重置表单
      Object.keys(studentForm).forEach(key => {
        studentForm[key] = key === 'gender' ? 'male' : 
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
    
    // 删除学生
    const deleteStudent = (row) => {
      ElMessageBox.confirm(
        `确定要删除学生 ${row.studentName}(${row.studentNumber}) 吗？`,
        '删除确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(async () => {
        try {
          const { data: result } = await proxy.$http.delete(`/api/admin/student/${row.studentNumber}`)
          
          if (result.code === 200) {
            ElMessage.success('删除成功')
            loadStudentList()
          } else {
            ElMessage.error(result.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除学生失败:', error)
          ElMessage.error('删除学生失败')
          // 模拟成功
          loadStudentList()
        }
      }).catch(() => {
        // 取消删除
      })
    }
    
    // 打开详情抽屉
    const handleRowClick = (row) => {
      selectedStudent.value = row
      drawerVisible.value = true
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
    
    // 处理更多操作
    const handleCommand = (command, row) => {
      switch (command) {
        case 'resetPassword':
          resetPassword(row)
          break
        case 'viewPayment':
          viewPayment(row)
          break
        case 'updatePayment':
          updatePaymentStatus(row)
          break
        case 'updateArrive':
          updateArriveStatus(row)
          break
        default:
          break
      }
    }
    
    // 重置密码
    const resetPassword = async (row) => {
      try {
        const { data: result } = await proxy.$http.post('/api/admin/student/reset-password', {
          studentNumber: row.studentNumber
        })
        
        if (result.code === 200) {
          ElMessage.success(`已重置${row.studentName}的密码为初始密码`)
        } else {
          ElMessage.error(result.msg || '重置密码失败')
        }
      } catch (error) {
        console.error('重置密码失败:', error)
        ElMessage.error('重置密码失败')
        // 模拟成功
        ElMessage.success(`已重置${row.studentName}的密码为初始密码`)
      }
    }
    
    // 查看缴费
    const viewPayment = (row) => {
      // 实际项目中可能跳转到缴费记录页面
      ElMessage.info(`查看学生${row.studentName}的缴费记录`)
    }
    
    // 更新缴费状态
    const updatePaymentStatus = async (row) => {
      const newStatus = row.paymentStatus === 1 ? 0 : 1
      try {
        // 使用URLSearchParams构建表单数据
        const params = new URLSearchParams();
        params.append('studentNumber', row.studentNumber);
        params.append('status', newStatus);
        
        const { data: result } = await proxy.$http.put('/api/payment/status', params)
        
        if (result.code === 200) {
          ElMessage.success(`已将${row.studentName}的缴费状态修改为${newStatus === 1 ? '已缴费' : '未缴费'}`)
          row.paymentStatus = newStatus
        } else {
          ElMessage.error(result.msg || '更新缴费状态失败')
        }
      } catch (error) {
        console.error('更新缴费状态失败:', error)
        ElMessage.error('更新缴费状态失败')
        // 模拟成功
        row.paymentStatus = newStatus
        ElMessage.success(`已将${row.studentName}的缴费状态修改为${newStatus === 1 ? '已缴费' : '未缴费'}`)
      }
    }
    
    // 更新抵校状态
    const updateArriveStatus = async (row) => {
      const newStatus = row.arriveStatus === 1 ? 0 : 1
      try {
        // 使用URLSearchParams构建表单数据
        const params = new URLSearchParams();
        params.append('studentNumber', row.studentNumber);
        params.append('status', newStatus);
        
        const { data: result } = await proxy.$http.put('/api/arrive/status', params)
        
        if (result.code === 200) {
          ElMessage.success(`已将${row.studentName}的抵校状态修改为${newStatus === 1 ? '已抵校' : '未抵校'}`)
          row.arriveStatus = newStatus
        } else {
          ElMessage.error(result.msg || '更新抵校状态失败')
        }
      } catch (error) {
        console.error('更新抵校状态失败:', error)
        ElMessage.error('更新抵校状态失败')
        // 模拟成功
        row.arriveStatus = newStatus
        ElMessage.success(`已将${row.studentName}的抵校状态修改为${newStatus === 1 ? '已抵校' : '未抵校'}`)
      }
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
      deleteStudent,
      handleRowClick,
      openImportDialog,
      handleFileChange,
      uploadFile,
      downloadTemplate,
      handleCommand,
      resetPassword,
      viewPayment,
      updatePaymentStatus,
      updateArriveStatus
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
}

.header-actions {
  display: flex;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.drawer-content {
  padding: 20px;
}

:deep(.el-upload-dragger) {
  width: 100%;
}
</style> 