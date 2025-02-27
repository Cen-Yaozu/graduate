<template>
  <div class="class-manage">
    <!-- 班级分配部分 -->
    <el-card class="box-card mb-20">
      <template #header>
        <div class="card-header">
          <span>班级分配</span>
        </div>
      </template>
      
      <!-- 搜索筛选区域 -->
      <div class="filter-container">
        <el-form :inline="true" :model="filterForm" class="demo-form-inline">
          <el-form-item label="系别">
            <el-select v-model="filterForm.department" placeholder="选择系别" @change="handleDepartmentChange" clearable style="width: 180px;">
              <el-option v-for="dept in departmentList" :key="dept" :label="dept" :value="dept" />
            </el-select>
          </el-form-item>
          <el-form-item label="专业">
            <el-select v-model="filterForm.majorid" placeholder="选择专业" clearable @change="handleMajorChange" style="width: 220px;">
              <el-option v-for="major in majorList" :key="major.majorid" :label="major.majorname" :value="major.majorid" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="searchUnassignedStudents" icon="el-icon-search">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 学生列表 -->
      <div class="student-list">
        <div class="table-header">
          <div class="header-left">
            <el-radio-group v-model="filterType" @change="filterStudents" size="medium">
              <el-radio-button label="all">所有学生</el-radio-button>
              <el-radio-button label="unassigned">未分配</el-radio-button>
              <el-radio-button label="assigned">已分配</el-radio-button>
            </el-radio-group>
          </div>
          <div class="header-right">
            <el-select v-model="assignedClassroom" placeholder="选择班级" style="width: 200px;">
              <el-option v-for="item in classList" :key="item.majorid" :label="item.classroom" :value="item.majorid" />
            </el-select>
            <el-button type="primary" @click="assignStudents" :disabled="selectedStudents.length === 0">批量分班</el-button>
          </div>
        </div>
        
        <el-table 
          :data="studentList" 
          style="width: 100%" 
          @selection-change="handleSelectionChange"
          border
          stripe
        >
          <el-table-column type="selection" width="45" align="center" />
          <el-table-column prop="studentNumber" label="学号" min-width="120" align="center" />
          <el-table-column prop="studentName" label="姓名" min-width="90" align="center" />
          <el-table-column prop="sex" label="性别" width="70" align="center" />
          <el-table-column prop="department" label="系别" min-width="120" align="center" />
          <el-table-column prop="majorname" label="专业" min-width="160" align="center" />
          <el-table-column label="班级" min-width="120" align="center">
            <template #default="scope">
              <span>{{ getClassroomName(scope.row.classroomId) }}</span>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="pagination.currentPage"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="pagination.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </el-card>
    
    <!-- 班级信息部分 -->
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>班级信息</span>
          <el-button type="primary" @click="openClassDialog()">添加班级</el-button>
        </div>
      </template>
      
      <!-- 班级列表 -->
      <el-table :data="classList" style="width: 100%" border stripe>
        <el-table-column prop="majorid" label="班级ID" width="80" align="center" />
        <el-table-column prop="classroom" label="班级名称" min-width="120" align="center" />
        <el-table-column prop="extraData.majorName" label="所属专业" min-width="160" align="center" />
        <el-table-column prop="department" label="系别" min-width="120" align="center" />
        <el-table-column prop="classroomNum" label="班级人数" width="100" align="center" />
        <el-table-column prop="fteacherid" label="辅导员ID" width="100" align="center" />
        <el-table-column prop="steacherid" label="学习导师ID" width="120" align="center" />
        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button size="small" type="primary" @click="openClassDialog(scope.row)">编辑</el-button>
            <el-popconfirm title="确定删除该班级吗？" @confirm="deleteClass(scope.row.majorid)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 班级分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="classPagination.currentPage"
          v-model:page-size="classPagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="classPagination.total"
          @size-change="handleClassSizeChange"
          @current-change="handleClassCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 班级信息对话框 -->
    <el-dialog
      v-model="classDialog.visible"
      :title="classDialog.isEdit ? '编辑班级' : '添加班级'"
      width="500px"
    >
      <el-form :model="classForm" label-width="100px" :rules="rules" ref="classFormRef">
        <el-form-item label="班级名称" prop="classroom">
          <el-input v-model="classForm.classroom" placeholder="请输入班级名称" />
        </el-form-item>
        <el-form-item label="所属专业" prop="majorid">
          <el-select v-model="classForm.majorid" placeholder="选择专业" @change="handleClassMajorChange">
            <el-option v-for="major in majorList" :key="major.majorid" :label="major.majorname" :value="major.majorid" />
          </el-select>
        </el-form-item>
        <el-form-item label="辅导员ID" prop="fteacherid">
          <el-input v-model="classForm.fteacherid" placeholder="请输入辅导员ID" />
        </el-form-item>
        <el-form-item label="学习导师ID" prop="steacherid">
          <el-input v-model="classForm.steacherid" placeholder="请输入学习导师ID" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="classDialog.visible = false">取消</el-button>
          <el-button type="primary" @click="saveClass">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default {
  name: 'ClassManageView',
  setup() {
    // 班级列表数据
    const classList = ref([])
    const majorList = ref([])
    const departmentList = ref([])
    const studentList = ref([])
    const selectedStudents = ref([])
    const assignedClassroom = ref(null)
    
    // 筛选表单
    const filterForm = reactive({
      department: '',
      majorid: '',
      majorname: ''
    })
    
    // 筛选类型
    const filterType = ref('unassigned')
    
    // 学生分页
    const pagination = reactive({
      currentPage: 1,
      pageSize: 10,
      total: 0
    })
    
    // 班级分页
    const classPagination = reactive({
      currentPage: 1,
      pageSize: 10,
      total: 0
    })
    
    // 班级对话框
    const classDialog = reactive({
      visible: false,
      isEdit: false
    })
    
    // 班级表单
    const classForm = reactive({
      majorid: null,
      classroom: '',
      fteacherid: null,
      steacherid: '',
      department: '',
      classroomNum: 0
    })
    
    // 表单规则
    const rules = {
      classroom: [
        { required: true, message: '请输入班级名称', trigger: 'blur' }
      ],
      majorid: [
        { required: true, message: '请选择所属专业', trigger: 'change' }
      ]
    }
    
    const classFormRef = ref(null)
    
    // 初始化数据
    const fetchData = async () => {
      try {
        // 获取所有系别
        const deptRes = await axios.get('/api/admin/class/departments')
        departmentList.value = deptRes.data.data
        
        // 获取专业列表
        await fetchMajorList()
        
        // 获取班级列表
        await fetchClassList()
        
        // 获取未分配班级的学生
        searchUnassignedStudents()
      } catch (error) {
        console.error('获取数据失败：', error)
        ElMessage.error('获取数据失败')
      }
    }
    
    // 获取专业列表
    const fetchMajorList = async () => {
      try {
        const majorRes = await axios.get('/api/admin/class/majors', {
          params: {
            department: filterForm.department
          }
        })
        majorList.value = majorRes.data.data
      } catch (error) {
        console.error('获取专业列表失败：', error)
      }
    }
    
    // 获取班级列表
    const fetchClassList = async () => {
      try {
        const classRes = await axios.get('/api/admin/class/list', {
          params: {
            page: classPagination.currentPage,
            size: classPagination.pageSize,
            department: filterForm.department,
            majorid: filterForm.majorid
          }
        })
        classList.value = classRes.data.data.list
        classPagination.total = classRes.data.data.total
      } catch (error) {
        console.error('获取班级列表失败：', error)
      }
    }
    
    // 系别选择变化
    const handleDepartmentChange = async () => {
      filterForm.majorid = ''
      filterForm.majorname = ''
      await fetchMajorList()
    }
    
    // 专业选择变化
    const handleMajorChange = () => {
      const selectedMajor = majorList.value.find(major => major.majorid === filterForm.majorid)
      filterForm.majorname = selectedMajor ? selectedMajor.majorname : ''
    }
    
    // 查询学生
    const searchUnassignedStudents = async () => {
      try {
        let url = '/api/admin/class/unassigned-students'
        
        // 根据筛选类型调整API路径
        if (filterType.value === 'all') {
          url = '/api/admin/class/students'
        } else if (filterType.value === 'assigned') {
          url = '/api/admin/class/assigned-students'
        }
        
        const params = {
          page: pagination.currentPage,
          size: pagination.pageSize,
          department: filterForm.department,
          majorname: filterForm.majorname
        }
        
        const res = await axios.get(url, { params })
        studentList.value = res.data.data.list
        pagination.total = res.data.data.total
      } catch (error) {
        console.error('获取学生列表失败：', error)
        ElMessage.error('获取学生列表失败')
      }
    }
    
    // 过滤学生列表
    const filterStudents = () => {
      pagination.currentPage = 1
      searchUnassignedStudents()
    }
    
    // 班级分页大小变化
    const handleClassSizeChange = (val) => {
      classPagination.pageSize = val
      fetchClassList()
    }
    
    // 班级页码变化
    const handleClassCurrentChange = (val) => {
      classPagination.currentPage = val
      fetchClassList()
    }
    
    // 学生分页大小变化
    const handleSizeChange = (val) => {
      pagination.pageSize = val
      searchUnassignedStudents()
    }
    
    // 学生页码变化
    const handleCurrentChange = (val) => {
      pagination.currentPage = val
      searchUnassignedStudents()
    }
    
    // 选择变化事件
    const handleSelectionChange = (val) => {
      selectedStudents.value = val
    }
    
    // 获取班级名称
    const getClassroomName = (classroomId) => {
      if (!classroomId) return '未分配'
      const classroom = classList.value.find(c => c.majorid === classroomId)
      return classroom ? classroom.classroom : `班级(${classroomId})`
    }
    
    // 分配学生到班级
    const assignStudents = async () => {
      if (selectedStudents.value.length === 0) {
        ElMessage.warning('请选择要分配的学生')
        return
      }
      
      if (!assignedClassroom.value) {
        ElMessage.warning('请选择要分配的班级')
        return
      }
      
      try {
        // 确保我们获取到正确的student_number字段
        const studentNumbers = selectedStudents.value.map(student => student.studentNumber)
        
        // 可以在控制台打印一下数据，方便调试
        console.log('提交的数据：', {
          studentNumbers,
          classroomId: assignedClassroom.value
        })
        
        const response = await axios.post('/api/admin/class/assign', {
          studentNumbers,
          classroomId: assignedClassroom.value
        })
        
        console.log('服务器响应：', response.data)
        
        ElMessage.success(`成功将${studentNumbers.length}名学生分配到班级`)
        
        // 清空选中的学生
        selectedStudents.value = []
        
        // 根据当前筛选状态重新加载学生列表
        searchUnassignedStudents()
        
        // 重新获取班级列表更新人数
        fetchClassList()
      } catch (error) {
        console.error('分配班级失败：', error)
        if (error.response && error.response.data && error.response.data.msg) {
          ElMessage.error(error.response.data.msg)
        } else {
          ElMessage.error('分配班级失败，请检查网络或后台日志')
        }
      }
    }
    
    // 打开班级对话框
    const openClassDialog = (row) => {
      classDialog.isEdit = !!row
      if (row) {
        // 编辑模式
        Object.assign(classForm, row)
      } else {
        // 添加模式
        Object.assign(classForm, {
          majorid: null,
          classroom: '',
          fteacherid: null,
          steacherid: '',
          department: '',
          classroomNum: 0
        })
      }
      classDialog.visible = true
    }
    
    // 处理班级专业选择变化
    const handleClassMajorChange = () => {
      const major = majorList.value.find(m => m.majorid === classForm.majorid)
      if (major) {
        classForm.department = major.department
      }
    }
    
    // 保存班级
    const saveClass = () => {
      classFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            if (classDialog.isEdit) {
              // 更新班级
              await axios.put('/api/admin/class/update', classForm)
              ElMessage.success('更新班级成功')
            } else {
              // 添加班级
              await axios.post('/api/admin/class/add', classForm)
              ElMessage.success('添加班级成功')
            }
            classDialog.visible = false
            fetchClassList()
          } catch (error) {
            console.error('保存班级失败：', error)
            ElMessage.error('保存班级失败')
          }
        }
      })
    }
    
    // 删除班级
    const deleteClass = async (majorid) => {
      try {
        await axios.delete(`/api/admin/class/${majorid}`)
        ElMessage.success('删除班级成功')
        fetchClassList()
      } catch (error) {
        console.error('删除班级失败：', error)
        if (error.response && error.response.data && error.response.data.msg) {
          ElMessage.error(error.response.data.msg)
        } else {
          ElMessage.error('删除班级失败')
        }
      }
    }

    onMounted(() => {
      fetchData()
    })

    return {
      classList,
      majorList,
      departmentList,
      studentList,
      selectedStudents,
      assignedClassroom,
      filterForm,
      filterType,
      pagination,
      classPagination,
      classDialog,
      classForm,
      rules,
      classFormRef,
      handleDepartmentChange,
      handleMajorChange,
      searchUnassignedStudents,
      handleClassSizeChange,
      handleClassCurrentChange,
      handleSizeChange,
      handleCurrentChange,
      handleSelectionChange,
      filterStudents,
      getClassroomName,
      assignStudents,
      openClassDialog,
      handleClassMajorChange,
      saveClass,
      deleteClass
    }
  }
}
</script>

<style scoped>
.class-manage {
  padding: 20px;
}

.mb-20 {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-container {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f7f8fa;
  border-radius: 4px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 10px 0;
}

.header-right {
  display: flex;
  gap: 10px;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  padding: 10px 0;
}

/* 表格样式美化 */
:deep(.el-table) {
  border-radius: 4px;
  overflow: hidden;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: bold;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background-color: #fafafa;
}

:deep(.el-button--small) {
  padding: 8px 15px;
}
</style>