<template>
  <div class="student-manage-container">
    <!-- 最上层显示图片预览 -->
    <div class="image-viewer-container" v-if="showImageViewer" @click.self="closeViewer">
      <el-image-viewer
        v-if="previewUrl"
        :url-list="[previewUrl]"
        :z-index="9999"
        :initial-index="0"
        :hide-on-click-modal="true"
        @close="closeViewer"
      />
    </div>
    
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold; font-size: 18px;">学生管理</span>
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
          </div>
        </div>
      </template>
      
      <!-- 现代化表格样式 -->
      <el-table
        v-loading="loading"
        :data="studentList"
        style="width: 100%"
        border
        row-key="id"
        @selection-change="handleSelectionChange"
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
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="studentNumber" label="学号" min-width="110" align="center" />
        <el-table-column prop="studentName" label="姓名" min-width="90" align="center" />
        <el-table-column label="性别" width="70" align="center">
          <template #default="scope">
            <el-tag 
              :type="scope.row.sex === '男' ? 'info' : 'danger'" 
              effect="plain" 
              size="small"
            >
              {{ scope.row.sex }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="学生图片" width="100" align="center">
          <template #default="scope">
            <div class="student-image-cell">
              <el-avatar 
                :size="40" 
                :src="getFullImageUrl(scope.row.studentPicture)"
                @click.stop="openImageViewer(scope.row.studentPicture)"
              />
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="idCard" label="身份证号" min-width="180" align="center" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.idCard.substr(0, 6) + '********' + scope.row.idCard.substr(-4) }}
          </template>
        </el-table-column>
        <el-table-column prop="department" label="系别" min-width="120" align="center" />
        <el-table-column prop="majorname" label="专业" min-width="180" align="center" show-overflow-tooltip />
        <el-table-column prop="studentPhone" label="联系电话" min-width="130" align="center" />
        <el-table-column label="缴费状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.paymentStatus === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.paymentStatus === 1 ? '已缴费' : '未缴费' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="抵校状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.arriveStatus === 1 ? 'success' : 'warning'" size="small">
              {{ scope.row.arriveStatus === 1 ? '已抵校' : '未抵校' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="90" align="center" fixed="right">
          <template #default="scope">
            <el-button type="primary" link size="small" @click.stop="showStudentDetails(scope.row)">
              <el-icon><More /></el-icon>详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 没有数据时显示 -->
      <el-empty v-if="studentList.length === 0" description="暂无学生数据" />
      
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
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="studentForm.idCard" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="系别" prop="department">
              <el-input v-model="studentForm.department" />
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
    
    <!-- 学生详情抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      title="学生详细信息"
      size="60%"
      direction="rtl"
    >
      <div v-if="selectedStudent" class="drawer-content">
        <!-- 基本信息部分 -->
        <el-descriptions title="基本信息" :column="2" border>
          <el-descriptions-item label="学号">{{ selectedStudent.studentNumber }}</el-descriptions-item>
          <el-descriptions-item label="姓名">{{ selectedStudent.studentName }}</el-descriptions-item>
          <el-descriptions-item label="性别">{{ selectedStudent.sex }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ selectedStudent.studentPhone }}</el-descriptions-item>
          <el-descriptions-item label="身份证号">{{ selectedStudent.idCard }}</el-descriptions-item>
          <el-descriptions-item label="系别">{{ selectedStudent.department }}</el-descriptions-item>
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

        <!-- 简历信息部分 -->
        <div v-loading="resumeLoading" class="section-container">
          <el-descriptions title="教育经历" :column="1" border v-if="resumeInfo && resumeInfo.hats">
            <el-descriptions-item label="身份">{{ resumeInfo.hats }}</el-descriptions-item>
            <el-descriptions-item label="学校名称">{{ resumeInfo.organization }}</el-descriptions-item>
            <el-descriptions-item label="入学时间">{{ resumeInfo.startTime }}</el-descriptions-item>
            <el-descriptions-item label="毕业时间">{{ resumeInfo.endTime }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <!-- 家庭信息部分 -->
        <div v-loading="familyLoading" class="section-container">
          <el-card class="family-card" v-if="familyInfo && familyInfo.length > 0">
            <template #header>
              <div class="family-card-header">
                <span class="family-title">家庭成员信息</span>
                <el-pagination
                  v-if="familyInfo.length > 1"
                  class="family-pagination"
                  v-model:current-page="familyCurrentPage"
                  :total="familyInfo.length"
                  :page-size="1"
                  layout="prev, pager, next"
                  small
                  background
                />
              </div>
            </template>
            <div class="family-member-info" v-if="currentFamilyMember">
              <el-descriptions :column="2" border>
                <el-descriptions-item label="家庭成员姓名">{{ currentFamilyMember.familyName }}</el-descriptions-item>
                <el-descriptions-item label="与学生关系">{{ currentFamilyMember.familyPart }}</el-descriptions-item>
                <el-descriptions-item label="年龄">{{ currentFamilyMember.familyAge }}</el-descriptions-item>
                <el-descriptions-item label="联系电话">{{ currentFamilyMember.familyPhone || '暂无' }}</el-descriptions-item>
                <el-descriptions-item label="工作单位" :span="2">{{ currentFamilyMember.familyWorkplace || '暂无' }}</el-descriptions-item>
              </el-descriptions>
              <div class="family-pager-info" v-if="familyInfo.length > 1">
                <span>当前显示: 第 {{ familyCurrentPage }} 位成员，共 {{ familyInfo.length }} 位</span>
              </div>
            </div>
          </el-card>
          <el-empty v-else description="暂无家庭成员信息" />
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { ref, reactive, onMounted, getCurrentInstance, computed } from 'vue'
import { Search, More } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox, ElImageViewer } from 'element-plus'

export default {
  name: 'StudentManageView',
  components: {
    Search,
    More,
    ElImageViewer
  },
  setup() {
    const { proxy } = getCurrentInstance()
    // 添加baseURL变量
    const baseURL = ref('')
    
    // 列表数据
    const studentList = ref([])
    const loading = ref(false)
    const total = ref(0)
    const currentPage = ref(1)
    const pageSize = ref(10)
    const searchText = ref('')
    const goToPage = ref('')
    const selectedRows = ref([])
    
    // 对话框相关
    const dialogVisible = ref(false)
    const drawerVisible = ref(false)
    const studentFormRef = ref(null)
    const isEdit = ref(false)
    const selectedStudent = ref(null)
    
    // 学生详情相关
    const resumeInfo = ref(null)
    const familyInfo = ref([])
    const resumeLoading = ref(false)
    const familyLoading = ref(false)
    const familyCurrentPage = ref(1)
    
    // 计算当前显示的家庭成员
    const currentFamilyMember = computed(() => {
      if (familyInfo.value && familyInfo.value.length > 0) {
        const index = familyCurrentPage.value - 1;
        return index < familyInfo.value.length ? familyInfo.value[index] : familyInfo.value[0];
      }
      return null;
    });
    
    // 表单数据
    const studentForm = reactive({
      id: null,
      studentNumber: '',
      studentName: '',
      sex: '男',
      studentPhone: '',
      idCard: '',
      department: '',
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
      idCard: [
        { required: true, message: '请输入身份证号', trigger: 'blur' },
        { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号码', trigger: 'blur' }
      ],
      majorname: [
        { required: true, message: '请输入专业', trigger: 'blur' }
      ],
      department: [
        { required: true, message: '请输入系别', trigger: 'blur' }
      ]
    }
    
    // 表格行样式
    const tableRowClassName = ({ rowIndex }) => {
      return rowIndex % 2 === 0 ? 'even-row' : 'odd-row';
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
            studentNumber: '214073001',
            studentName: '钱梦怡',
            sex: '男',
            studentPhone: '15255715527',
            idCard: '330102200001015578',
            department: '计算机系',
            majorname: '计算机科学与技术',
            classname: '计算机1班',
            email: 'qianmy@example.com',
            paymentStatus: 0,
            arriveStatus: 0,
            address: '北京市海淀区',
            studentPicture: '/uploads/student-photos/669463f6-a0b8-40cf-9443-80230f6bcfc4.jpg'
          },
          {
            id: 2,
            studentNumber: '214073002',
            studentName: '侯珀怜',
            sex: '女',
            studentPhone: '13707533319',
            idCard: '330102200002025569',
            department: '计算机系',
            majorname: '计算机科学与技术',
            classname: '计算机1班',
            email: 'houpelian@example.com',
            paymentStatus: 0,
            arriveStatus: 0,
            address: '上海市浦东新区',
            studentPicture: '/uploads/student-photos/569463f6-a0b8-40cf-9443-80230f6bcfc5.jpg'
          },
          {
            id: 3,
            studentNumber: '214073003',
            studentName: '西门龙婧',
            sex: '女',
            studentPhone: '13354924193',
            idCard: '330102200003036680',
            department: '计算机系',
            majorname: '计算机科学与技术',
            classname: '计算机1班',
            email: 'ximenlong@example.com',
            paymentStatus: 0,
            arriveStatus: 0,
            address: '广州市天河区',
            studentPicture: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
          },
          {
            id: 4,
            studentNumber: '214073004',
            studentName: '曹莹洋',
            sex: '女',
            studentPhone: '15065724113',
            idCard: '330102200004047781',
            department: '计算机系',
            majorname: '计算机科学与技术',
            classname: '计算机1班',
            email: 'caoyingy@example.com',
            paymentStatus: 0,
            arriveStatus: 0,
            address: '深圳市南山区',
            studentPicture: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
          },
          {
            id: 5,
            studentNumber: '214073005',
            studentName: '尤炅',
            sex: '女',
            studentPhone: '15589739287',
            idCard: '330102200005058892',
            department: '计算机系',
            majorname: '计算机科学与技术',
            classname: '计算机1班',
            email: 'youj@example.com',
            paymentStatus: 0,
            arriveStatus: 0,
            address: '武汉市洪山区',
            studentPicture: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
          },
          {
            id: 6,
            studentNumber: '214073006',
            studentName: '郑子鑫',
            sex: '女',
            studentPhone: '15207468250',
            idCard: '330102200006069903',
            department: '计算机系',
            majorname: '计算机科学与技术',
            classname: '计算机1班',
            email: 'zhengzx@example.com',
            paymentStatus: 0,
            arriveStatus: 0,
            address: '南京市玄武区',
            studentPicture: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
          },
          {
            id: 7,
            studentNumber: '214073007',
            studentName: '尤有菊',
            sex: '女',
            studentPhone: '13923404505',
            idCard: '330102200007070014',
            department: '计算机系',
            majorname: '计算机科学与技术',
            classname: '计算机1班',
            email: 'youyj@example.com',
            paymentStatus: 0,
            arriveStatus: 0,
            address: '杭州市西湖区',
            studentPicture: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
          },
          {
            id: 8,
            studentNumber: '214073008',
            studentName: '精燕',
            sex: '男',
            studentPhone: '13923263024',
            idCard: '330102200008081125',
            department: '计算机系',
            majorname: '计算机科学与技术',
            classname: '计算机1班',
            email: 'jingyan@example.com',
            paymentStatus: 0,
            arriveStatus: 0,
            address: '成都市武侯区',
            studentPicture: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
          },
          {
            id: 9,
            studentNumber: '214073009',
            studentName: '韩睿敏',
            sex: '男',
            studentPhone: '18868557032',
            idCard: '330102200009092236',
            department: '计算机系',
            majorname: '计算机科学与技术',
            classname: '计算机1班',
            email: 'hanrm@example.com',
            paymentStatus: 0,
            arriveStatus: 0,
            address: '重庆市渝中区',
            studentPicture: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
          },
          {
            id: 10,
            studentNumber: '2140730010',
            studentName: '郑如雪',
            sex: '女',
            studentPhone: '13895038053',
            idCard: '330102200010103347',
            department: '计算机系',
            majorname: '计算机科学与技术',
            classname: '计算机1班',
            email: 'zhengrx@example.com',
            paymentStatus: 0,
            arriveStatus: 0,
            address: '西安市雁塔区',
            studentPicture: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
          }
        ]
        total.value = 200
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
    
    // 选择行变化
    const handleSelectionChange = (selection) => {
      selectedRows.value = selection
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
    
    // 删除学生
    const deleteStudent = (row) => {
      ElMessageBox.confirm(
        `确定要删除学生"${row.studentName}"吗？`,
        '删除确认',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(async () => {
        try {
          const { data: result } = await proxy.$http.delete(`/api/admin/student/delete/${row.id}`)
          
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
    const showStudentDetails = async (row) => {
      selectedStudent.value = row
      drawerVisible.value = true
      familyCurrentPage.value = 1  // 重置家庭成员分页
      
      // 获取简历信息
      resumeLoading.value = true
      try {
        const { data: resumeResult } = await proxy.$http.get(`/api/student/resume/${row.studentNumber}`)
        if (resumeResult.code === 200) {
          resumeInfo.value = resumeResult.data
          console.log('简历信息：', resumeInfo.value)
        } else {
          ElMessage.warning(resumeResult.msg || '获取简历信息失败')
          resumeInfo.value = null
        }
      } catch (error) {
        console.error('获取学生简历信息失败:', error)
        resumeInfo.value = null
      } finally {
        resumeLoading.value = false
      }
      
      // 获取家庭信息
      familyLoading.value = true
      try {
        const { data: familyResult } = await proxy.$http.get(`/api/student/family/${row.studentNumber}`)
        if (familyResult.code === 200) {
          familyInfo.value = familyResult.data || []
          console.log('家庭信息：', familyInfo.value)
        } else {
          ElMessage.warning(familyResult.msg || '获取家庭信息失败')
          familyInfo.value = []
        }
      } catch (error) {
        console.error('获取学生家庭信息失败:', error)
        familyInfo.value = []
      } finally {
        familyLoading.value = false
      }
    }
    
    // 打开详情抽屉
    // eslint-disable-next-line no-unused-vars
    const handleRowClick = () => {
      // 不再使用点击行打开详情
      // 示例: showStudentDetails(studentData)
    }
    
    // 图片预览相关
    const showImageViewer = ref(false)
    const previewUrl = ref('')
    
    // 打开图片预览
    const openImageViewer = (url) => {
      if (!url) return
      // 使用完整的 URL
      previewUrl.value = getFullImageUrl(url)
      showImageViewer.value = true
      // 阻止背景滚动
      document.body.style.overflow = 'hidden'
    }
    
    // 关闭图片预览
    const closeViewer = () => {
      // 先将URL清空，再隐藏组件，避免闪现
      previewUrl.value = ''
      // 使用setTimeout确保DOM操作顺序正确
      setTimeout(() => {
        showImageViewer.value = false
        // 恢复背景滚动
        document.body.style.overflow = ''
      }, 0)
    }
    
    onMounted(() => {
      // 设置baseURL
      if (proxy && proxy.$http && proxy.$http.defaults) {
        baseURL.value = proxy.$http.defaults.baseURL || ''
      }
      loadStudentList()
    })
    
    // 获取完整图片URL的方法
    const getFullImageUrl = (path) => {
      return path ? (baseURL.value + path) : 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    }
    
    return {
      studentList,
      loading,
      total,
      currentPage,
      pageSize,
      searchText,
      goToPage,
      dialogVisible,
      drawerVisible,
      studentFormRef,
      isEdit,
      selectedStudent,
      studentForm,
      rules,
      selectedRows,
      baseURL,
      tableRowClassName,
      getFullImageUrl,
      handleSearch,
      handleSizeChange,
      handleCurrentChange,
      openAddDialog,
      editStudent,
      deleteStudent,
      submitForm,
      handleRowClick,
      showStudentDetails,
      handleGoTo,
      handleSelectionChange,
      showImageViewer,
      previewUrl,
      openImageViewer,
      closeViewer,
      resumeInfo,
      familyInfo,
      resumeLoading,
      familyLoading,
      familyCurrentPage,
      currentFamilyMember
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

/* 学生头像样式 */
.student-image-cell {
  display: flex;
  justify-content: center;
  align-items: center;
}

:deep(.el-avatar) {
  cursor: pointer;
  transition: transform 0.3s;
}

:deep(.el-avatar:hover) {
  transform: scale(1.1);
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

.drawer-content {
  padding: 20px;
}

.section-container {
  margin-top: 20px;
}

/* 家庭成员卡片样式 */
.family-card {
  margin-top: 10px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.family-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.family-title {
  font-size: 16px;
  font-weight: bold;
}

.family-pagination {
  margin: 0;
}

.family-member-info {
  padding: 10px 0;
}

.family-pager-info {
  margin-top: 15px;
  text-align: right;
  color: #909399;
  font-size: 13px;
}

:deep(.el-descriptions__label) {
  width: 120px;
  font-weight: bold;
}

:deep(.el-descriptions__title) {
  font-size: 16px;
  margin-bottom: 15px;
  font-weight: bold;
}

:deep(.el-descriptions__content) {
  padding: 12px;
}

/* 图片预览容器样式 */
.image-viewer-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.95);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: opacity 0.3s ease;
}
</style> 