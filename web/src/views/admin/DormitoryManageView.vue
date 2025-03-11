<template>
  <div class="dormitory-manage">
    <el-card class="main-card">
      <div class="page-header">
        <h2>宿舍管理系统</h2>
      </div>
      
      <el-tabs v-model="activeTab" @tab-click="handleTabChange">
        <el-tab-pane label="宿舍分配" name="assignment">
          <div v-if="activeTab === 'assignment'">
            <!-- 宿舍分配部分 -->
            
            <div class="status-filter">
              <el-radio-group v-model="assignmentStatus" @change="handleStatusChange">
                <el-radio-button label="all">全部</el-radio-button>
                <el-radio-button label="assigned">已分配</el-radio-button>
                <el-radio-button label="pending">待分配</el-radio-button>
                <el-radio-button label="unassigned">未分配</el-radio-button>
              </el-radio-group>
            </div>
            
            <!-- 批量分配宿舍按钮 - 仅在待分配状态显示 -->
            <div class="batch-operation" v-if="assignmentStatus === 'pending'">
              <el-button 
                type="primary" 
                @click="showAssignDialog" 
                :disabled="selectedStudents.length === 0">
                批量分配宿舍 
                <span v-if="selectedStudents.length > 0">(已选择 {{ selectedStudents.length }} 名学生)</span>
              </el-button>
            </div>
            
            <!-- 学生表格 -->
            <div class="table-container">
              <el-table
                ref="studentTable"
                :data="studentList"
                border
                style="width: 100%"
                @selection-change="handleSelectionChange"
                v-loading="loading"
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
                highlight-current-row>
                <el-table-column
                  v-if="assignmentStatus === 'pending'"
                  type="selection"
                  width="55">
                </el-table-column>
                <el-table-column
                  prop="studentNumber"
                  label="学号"
                  width="110">
                </el-table-column>
                <el-table-column
                  prop="name"
                  label="姓名"
                  width="100">
                </el-table-column>
                <el-table-column
                  prop="gender"
                  label="性别"
                  width="80">
                  <template #default="scope">
                    {{ scope.row.gender === 'MALE' ? '男' : '女' }}
                  </template>
                </el-table-column>
                <el-table-column
                  prop="department"
                  label="院系">
                </el-table-column>
                <el-table-column
                  prop="major"
                  label="专业">
                </el-table-column>
                <el-table-column
                  prop="className"
                  label="班级">
                  <template #default="scope">
                    {{ scope.row.classroom_name || scope.row.className }}
                  </template>
                </el-table-column>
                <el-table-column
                  label="状态">
                  <template #default="scope">
                    <el-tag
                      :type="getStudentStatusType(scope.row)"
                      effect="plain"
                    >
                      {{ getStudentStatusText(scope.row) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <!-- 只在待分配标签页显示操作列 -->
                <el-table-column
                  v-if="assignmentStatus === 'pending'"
                  label="操作"
                  width="200">
                  <template #default="scope">
                    <div class="action-buttons">
                      <!-- 待分配状态：显示分配按钮 -->
                      <el-button
                        v-if="isPendingStudent(scope.row)"
                        size="small"
                        type="primary"
                        @click="assignDormToStudent(scope.row)"
                      >
                        分配宿舍
                      </el-button>
                    </div>
                  </template>
                </el-table-column>
              </el-table>
              
              <div class="pagination-container">
                <el-pagination
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                  :current-page="currentPage"
                  :page-sizes="[10, 20, 30, 50]"
                  :page-size="pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="totalStudents">
                </el-pagination>
              </div>
            </div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="宿舍信息" name="information">
          <div v-if="activeTab === 'information'">
            <!-- 宿舍信息部分 -->
            <div class="filter-container">
              <el-form :inline="true" :model="dormFilter" class="filter-form">
                <el-form-item label="楼栋名称:">
                  <el-input v-model="dormFilter.buildingName" placeholder="楼栋名称" clearable @input="searchDorms"></el-input>
                </el-form-item>
                <el-form-item label="宿舍号:">
                  <el-input v-model="dormFilter.roomNumber" placeholder="宿舍号" clearable @input="searchDorms"></el-input>
                </el-form-item>
                <el-form-item label="宿舍类型:">
                  <el-select 
                    v-model="dormFilter.type" 
                    placeholder="宿舍类型" 
                    clearable 
                    @change="searchDorms"
                    style="width: 120px;">
                    <el-option label="四人间" value="FOUR"></el-option>
                    <el-option label="六人间" value="SIX"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="性别:">
                  <el-select 
                    v-model="dormFilter.gender" 
                    placeholder="性别" 
                    clearable 
                    @change="searchDorms"
                    style="width: 120px;">
                    <el-option label="男" value="MALE"></el-option>
                    <el-option label="女" value="FEMALE"></el-option>
                  </el-select>
                </el-form-item>
              </el-form>
              <div class="operation-buttons">
                <el-button type="primary" @click="showAddDormDialog">添加宿舍</el-button>
              </div>
            </div>
            
            <!-- 宿舍表格 -->
            <div class="table-container">
              <el-table
                v-if="dormList.length > 0"
                :data="dormList"
                border
                :row-class-name="tableRowClassName"
                v-loading="loadingDorms"
                style="width: 100%"
                row-key="id"
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
                highlight-current-row>
                <el-table-column
                  prop="department"
                  label="系别"
                  width="120">
                </el-table-column>
                <el-table-column
                  prop="buildingName"
                  label="楼栋名称"
                  width="120">
                </el-table-column>
                <el-table-column
                  prop="roomNumber"
                  label="宿舍号"
                  width="100">
                </el-table-column>
                <el-table-column
                  prop="type"
                  label="宿舍类型"
                  width="100">
                  <template #default="scope">
                    {{ scope.row.type === 'FOUR' ? '四人间' : 
                       scope.row.type === 'SIX' ? '六人间' : '未知' }}
                  </template>
                </el-table-column>
                <el-table-column
                  prop="gender"
                  label="性别"
                  width="70">
                  <template #default="scope">
                    {{ scope.row.gender === 'MALE' ? '男' : scope.row.gender === 'FEMALE' ? '女' : '未知' }}
                  </template>
                </el-table-column>
                <el-table-column
                  prop="capacity"
                  label="容量"
                  width="70">
                </el-table-column>
                <el-table-column
                  prop="occupied"
                  label="已住人数"
                  width="90">
                </el-table-column>
                <el-table-column
                  label="状态"
                  width="80">
                  <template #default="scope">
                    <el-tag type="success" v-if="scope.row.occupied < scope.row.capacity">可住</el-tag>
                    <el-tag type="danger" v-else>已满</el-tag>
                  </template>
                </el-table-column>
                <el-table-column
                  label="操作"
                  min-width="200">
                  <template #default="scope">
                    <div class="action-buttons">
                      <el-button
                        size="small"
                        type="primary"
                        @click="viewDormMembers(scope.row)">
                        查看成员
                      </el-button>
                      <el-button
                        size="small"
                        type="warning"
                        @click="editDorm(scope.row)">
                        编辑
                      </el-button>
                      <el-button
                        size="small"
                        type="danger"
                        @click="confirmDeleteDorm(scope.row)">
                        删除
                      </el-button>
                    </div>
                  </template>
                </el-table-column>
              </el-table>
              <div v-else-if="!loadingDorms" class="no-data">
                暂无宿舍数据
              </div>
              
              <div class="pagination-container">
                <el-pagination
                  @size-change="handleDormSizeChange"
                  @current-change="handleDormCurrentChange"
                  :current-page="dormCurrentPage"
                  :page-sizes="[10, 20, 30, 50]"
                  :page-size="dormPageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="totalDorms">
                </el-pagination>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    
    <!-- 对话框部分 -->
    <!-- 分配宿舍对话框 -->
    <el-dialog 
      title="分配宿舍" 
      v-model="assignDialogVisible"
      width="600px"
      @open="handleDialogOpen">
      <el-form :model="assignForm" label-width="100px" ref="assignForm">
        <el-form-item label="宿舍类型:" prop="dormType" required>
          <el-select v-model="assignForm.dormType" placeholder="请选择宿舍类型" @change="handleDormTypeChange" style="width: 100%">
            <el-option label="四人间" value="FOUR"></el-option>
            <el-option label="六人间" value="SIX"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="性别:" prop="gender">
          <el-tag type="success" size="large">{{ assignForm.gender === 'MALE' ? '男' : '女' }}</el-tag>
          <span class="note">* 根据学生性别自动选择</span>
        </el-form-item>
        <el-form-item label="宿舍楼栋:" prop="dormId" required>
          <el-select v-model="assignForm.dormId" placeholder="请选择宿舍" filterable style="width: 100%">
            <el-option
              v-for="item in availableDorms"
              :key="item.id"
              :label="`${item.buildingName} - ${item.roomNumber} (${item.department}) (已住${item.occupied}/${item.capacity})`"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="assignDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmAssignDorm">确定</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 宿舍成员对话框 -->
    <el-dialog 
      title="宿舍成员" 
      v-model="membersDialogVisible"
      width="800px">
      <div v-if="dormMembers.length === 0" class="no-data">
        该宿舍暂无成员
      </div>
      <el-table
        v-else
        :data="dormMembers"
        border
        style="width: 100%"
        :header-cell-style="{
          background: '#f5f7fa',
          color: '#606266',
          fontWeight: '600',
          height: '55px'
        }"
        :cell-style="{
          padding: '12px 0'
        }"
        highlight-current-row>
        <el-table-column
          prop="studentNumber"
          label="学号"
          width="150">
        </el-table-column>
        <el-table-column
          prop="name"
          label="姓名"
          width="120">
        </el-table-column>
        <el-table-column
          prop="gender"
          label="性别"
          width="80">
          <template #default="scope">
            {{ scope.row.gender === 'MALE' ? '男' : scope.row.gender === 'FEMALE' ? '女' : '未知' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="department"
          label="系部">
        </el-table-column>
        <el-table-column
          prop="major"
          label="专业">
        </el-table-column>
        <el-table-column
          prop="className"
          label="班级">
          <template #default="scope">
            {{ scope.row.classroom_name || scope.row.className }}
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    
    <!-- 添加/编辑宿舍对话框 -->
    <el-dialog 
      :title="dormFormTitle" 
      v-model="dormDialogVisible"
      width="500px">
      <el-form :model="dormForm" :rules="dormRules" label-width="100px" ref="dormForm">
        <el-form-item label="楼栋名称:" prop="buildingName">
          <el-input v-model="dormForm.buildingName" placeholder="请输入楼栋名称"></el-input>
        </el-form-item>
        <el-form-item label="宿舍号:" prop="roomNumber">
          <el-input v-model="dormForm.roomNumber" placeholder="请输入宿舍号"></el-input>
        </el-form-item>
        <el-form-item label="宿舍类型:" prop="type">
          <el-select v-model="dormForm.type" placeholder="请选择宿舍类型" style="width: 100%">
            <el-option label="四人间" value="FOUR"></el-option>
            <el-option label="六人间" value="SIX"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="性别:" prop="gender">
          <el-select v-model="dormForm.gender" placeholder="请选择性别" style="width: 100%">
            <el-option label="男" value="MALE"></el-option>
            <el-option label="女" value="FEMALE"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dormDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitDormForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'DormitoryManageView',
  data() {
    return {
      // 基础数据
      activeTab: 'assignment',
      studentFilter: {
        department: '',
        major: '',
        className: ''
      },
      departmentOptions: [],
      majorOptions: [],
      classOptions: [],
      assignmentStatus: 'all',
      selectedStudents: [],
      studentList: [],
      currentPage: 1,
      pageSize: 10,
      totalStudents: 0,
      dormFilter: {
        buildingName: '',
        roomNumber: '',
        type: '',
        gender: ''
      },
      dormList: [],
      dormCurrentPage: 1,
      dormPageSize: 10,
      totalDorms: 0,
      
      // 对话框相关数据
      assignDialogVisible: false,
      assignForm: {
        dormType: '',
        gender: '',
        dormId: ''
      },
      availableDorms: [],
      
      membersDialogVisible: false,
      dormMembers: [],
      
      dormDialogVisible: false,
      dormFormTitle: '添加宿舍',
      dormForm: {
        id: null,
        buildingName: '',
        roomNumber: '',
        type: '',
        gender: ''
      },
      dormRules: {
        buildingName: [
          { required: true, message: '请输入楼栋名称', trigger: 'blur' }
        ],
        roomNumber: [
          { required: true, message: '请输入宿舍号', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择宿舍类型', trigger: 'change' }
        ],
        gender: [
          { required: true, message: '请选择性别', trigger: 'change' }
        ]
      },
      
      // 当前操作的学生
      currentStudent: null,
      loading: false,
      allStudentsList: [],
      loadingDorms: false
    }
  },
  computed: {
    tableRowClassName() {
      return function({ row, rowIndex }) {
        // 首先判断奇偶行
        const baseClass = rowIndex % 2 === 0 ? 'even-row' : 'odd-row';
        
        // 再判断宿舍容量状态
        if (row.occupied >= row.capacity) {
          return baseClass + ' row-danger';
        }
        if (row.occupied < row.capacity) {
          return baseClass + ' row-success';
        }
        
        return baseClass;
      }
    }
  },
  created() {
    // 添加全局错误处理，忽略特定的 ResizeObserver 错误
    window.addEventListener('error', this.handleError);
    
    // 无论当前激活的标签页是什么，都加载宿舍数据
    console.log('组件created，准备加载宿舍数据');
    this.loadDorms();
    
    // 如果当前是分配标签页，则加载学生数据
    if (this.activeTab === 'assignment') {
      this.loadStudents();
    }
  },
  mounted() {
    console.log('组件mounted，当前激活标签页:', this.activeTab);
    // 确保在mounted时也加载适当的数据
    if (this.activeTab === 'information') {
      console.log('组件mounted时检测到information标签页，准备加载宿舍数据');
      this.loadDorms();
    }
  },
  methods: {
    // Tab切换处理
    handleTabChange(tab) {
      console.log('标签页切换为:', tab.name);
      // 在下一个微任务中执行数据加载，避免重叠的 DOM 操作
      this.$nextTick(() => {
        if (tab.name === 'information') {
          console.log('切换到宿舍信息标签页，准备加载宿舍数据');
          this.loadDorms()
        } else {
          this.loadStudents()
        }
      })
    },
  
    
    handleDepartmentChange() {
      // 清空专业和班级选择
      this.studentFilter.major = ''
      this.studentFilter.className = ''
      this.majorOptions = []
      this.classOptions = []
      
      if (this.studentFilter.department) {
        axios.get(`/api/admin/majors?department=${this.studentFilter.department}`)
          .then(response => {
            this.majorOptions = response.data.map(major => ({
              value: major,
              label: major
            }))
          })
          .catch(error => {
            console.error('获取专业信息失败:', error)
            this.$message.error('获取专业信息失败')
          })
      }
      
      // 触发搜索
      this.searchStudents()
    },
    
    handleMajorChange() {
      // 清空班级选择
      this.studentFilter.className = ''
      this.classOptions = []
      
      if (this.studentFilter.major) {
        axios.get(`/api/admin/classes?department=${this.studentFilter.department}&major=${this.studentFilter.major}`)
          .then(response => {
            this.classOptions = response.data.map(cls => ({
              value: cls,
              label: cls
            }))
          })
          .catch(error => {
            console.error('获取班级信息失败:', error)
            this.$message.error('获取班级信息失败')
          })
      }
      
      // 触发搜索
      this.searchStudents()
    },
    
    // 学生相关方法
    loadStudents() {
      this.loading = true;
      console.log('开始加载学生数据, 当前状态:', this.assignmentStatus);
      
      // 不需要翻页参数，因为后端接口没有实现分页
      const params = {
        status: this.assignmentStatus === 'all' ? null : this.assignmentStatus
      };
      
      console.log('请求参数:', params);
      
      // 直接调用后端接口
      axios.get('/api/admin/dorm/students', { params })
        .then(response => {
          console.log('后端返回的原始数据:', response.data);
          
            // 处理后端返回的数据格式
          if (response.data.code === 200 && response.data.data) {
            const responseData = response.data.data;
            
            // 提取学生列表数据和总数
            let allStudents = [];
            let total = 0;
            
            if (responseData.list && Array.isArray(responseData.list)) {
              allStudents = responseData.list;
              total = responseData.total || allStudents.length;
            } else {
              console.error('无法从响应中识别学生列表数据:', responseData);
            }
            
            console.log('提取的学生列表数据:', allStudents);
            
            // 格式化所有学生数据
            this.allStudentsList = allStudents.map(student => {
              return this.formatStudentData(student);
            });
            
            this.totalStudents = total;
            
            // 本地分页处理
            this.applyPagination();
            } else {
            console.error('后端返回错误或数据格式异常:', response.data);
            this.$message.error('获取学生数据失败: ' + (response.data.message || '未知错误'));
            this.studentList = [];
            this.totalStudents = 0;
          }
          
            this.loading = false;
          })
          .catch(error => {
            console.error('获取学生列表失败:', error);
          this.$message.error('获取学生列表失败: ' + (error.response?.data?.message || error.message));
          this.studentList = [];
          this.totalStudents = 0;
            this.loading = false;
          });
    },
    
    // 应用本地分页
    applyPagination() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      
      // 从全部数据中截取当前页数据
      this.studentList = this.allStudentsList.slice(start, end);
      console.log(`应用本地分页: 第${this.currentPage}页，每页${this.pageSize}条，显示${start+1}-${Math.min(end, this.totalStudents)}条`);
    },
    
    handleSizeChange(size) {
      this.pageSize = size;
      this.applyPagination();
    },
    
    handleCurrentChange(page) {
      this.currentPage = page;
      this.applyPagination();
    },
    
    // 格式化单个学生数据
    formatStudentData(student) {
      // 添加日志来查看输入
      console.log('正在格式化学生数据:', student);
      
      // 检查输入有效性
      if (!student) {
        console.error('收到无效的学生数据(null/undefined)');
        return {
          studentNumber: '未知',
          name: '未知',
          gender: 'UNKNOWN',
          dormInfo: null
        };
      }
      
      // 创建一个新对象，添加所需字段
      const formattedStudent = {};
       
      // 学号
      formattedStudent.studentNumber = student.studentNumber || '';
      
      // 姓名
      formattedStudent.name = student.studentName || '';
      
      // 性别 (转换为前端需要的格式)
      formattedStudent.gender = student.sex === '男' ? 'MALE' : student.sex === '女' ? 'FEMALE' : 'UNKNOWN';
      
      // 系部
      formattedStudent.department = student.department || '';
      
      // 专业
      formattedStudent.major = student.majorname || '';
      
      // 班级
      formattedStudent.className = student.classroom_name || student.className || '';
      
      // 处理宿舍类型 - 无论是否有宿舍信息，都尝试提取宿舍类型，这样在"继续分配"时可以使用
      formattedStudent.dormType = student.dormType || '';
      console.log('提取的宿舍类型:', formattedStudent.dormType);
      
      // 处理宿舍信息
      // 注意：学生表中的dormitory和dormNumber字段已删除
      // 现在宿舍信息应该从student_dorm关联表中获取
      if (student.studentDorm && student.studentDorm.dormitory && student.studentDorm.dormCard) {
        formattedStudent.dormitory = student.studentDorm.dormitory;
        formattedStudent.dormNumber = student.studentDorm.dormCard;
        
        // 处理宿舍类型
        if (student.studentDorm.dormType) {
          // 宿舍信息中包含宿舍类型
          let dormTypeText = '';
          if (student.studentDorm.dormType === 'FOUR') {
            dormTypeText = '四人间';
          } else if (student.studentDorm.dormType === 'SIX') {
            dormTypeText = '六人间';
          } else {
            dormTypeText = student.studentDorm.dormType;
          }
          formattedStudent.dormInfo = `${student.studentDorm.dormitory} - ${student.studentDorm.dormCard} (${dormTypeText})`;
        } else {
          formattedStudent.dormInfo = `${student.studentDorm.dormitory} - ${student.studentDorm.dormCard}`;
        }
      } else {
        formattedStudent.dormInfo = null;
      }
      
      // 分配状态
      formattedStudent.assignmentStatus = student.assignmentStatus || 'unassigned';
      
      console.log('格式化后的学生数据:', formattedStudent);
      return formattedStudent;
    },
    
    searchStudents() {
      this.currentPage = 1
      this.loadStudents()
    },
    
    resetFilter() {
      this.studentFilter = {
        department: '',
        major: '',
        className: ''
      }
      this.majorOptions = []
      this.classOptions = []
      this.searchStudents()
    },
    
    handleStatusChange() {
      this.currentPage = 1
      this.loadStudents()
    },
    
    handleSelectionChange(selection) {
      this.selectedStudents = selection
    },
    
    // 学生宿舍分配相关方法
    assignDormToStudent(student) {
      this.currentStudent = student
      
      // 如果是待分配状态，从学生现有数据中获取宿舍类型
      if (this.isPendingStudent(student)) {
      this.assignForm = {
          dormType: student.dormType || 'FOUR',  // 优先使用学生已有的宿舍类型，若没有则默认四人间
        gender: student.gender,
        dormId: ''
      }
        console.log('继续分配宿舍，使用已有宿舍类型:', student.dormType);
      } else {
        // 未分配状态，设置一个默认的宿舍类型
        this.assignForm = {
          dormType: 'FOUR',  // 默认四人间
          gender: student.gender,
          dormId: ''
        }
        console.log('新分配宿舍，使用默认宿舍类型: FOUR');
      }
      
      this.assignDialogVisible = true
    },
     
    reassignDorm(student) {
      this.currentStudent = student
      this.assignForm = {
        dormType: student.dormType || 'FOUR',  // 优先使用学生已有的宿舍类型，若没有则默认四人间
        gender: student.gender,
        dormId: ''
      }
      this.assignDialogVisible = true
      this.$message.warning('重新分配宿舍将会取消学生的当前宿舍分配')
    },
     
    removeDormAssignment(student) {
      this.$confirm('确定要移除该学生的宿舍分配吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete(`/api/admin/dorm/assign/${student.studentNumber}`)
          .then(() => {
            this.$message.success('移除宿舍分配成功')
            this.loadStudents()
          })
          .catch(error => {
            console.error('移除宿舍分配失败:', error)
            this.$message.error('移除宿舍分配失败')
          })
      }).catch(() => {
        // 取消操作
      })
    },

    // 执行宿舍分配（提交分配表单）
    confirmAssignDorm() {
      // 表单校验
      if (!this.assignForm.dormType) {
        this.$message.warning('请选择宿舍类型')
        return
      }

      if (!this.assignForm.dormId) {
        this.$message.warning('请选择宿舍')
        return
      }

      // 从已选择的宿舍中获取宿舍信息
      const selectedDorm = this.availableDorms.find(dorm => dorm.id === this.assignForm.dormId);
      if (!selectedDorm) {
        this.$message.error('无法获取所选宿舍信息');
        return;
      }
      
      console.log('已选择的宿舍信息:', selectedDorm);
      
      // 区分单个分配和批量分配
      if (this.currentStudent) {
        // 单个学生分配
        const data = {
          studentNumber: this.currentStudent.studentNumber,
          studentName: this.currentStudent.name,  // 添加学生姓名
          department: this.currentStudent.department,  // 添加系别
          dormType: this.assignForm.dormType,
          dormitory: selectedDorm.buildingName,  // 宿舍楼
          dormCard: selectedDorm.roomNumber   // 宿舍号
        }
        
        console.log('提交的宿舍分配数据:', data);
        
        axios.post('/api/admin/dorm/assign', data)
          .then((response) => {
            console.log('宿舍分配响应:', response);
            this.$message.success('分配宿舍成功')
            this.assignDialogVisible = false
            this.currentStudent = null
            this.loadStudents()
          })
          .catch(error => {
            console.error('分配宿舍失败:', error)
            this.$message.error('分配宿舍失败: ' + (error.response?.data?.message || '未知错误'))
          })
      } else {
        // 批量分配
        const studentNumbers = this.selectedStudents.map(student => student.studentNumber)
        
        // 检查批量分配数据的完整性
        if (!studentNumbers.length) {
          this.$message.error('未选择任何学生，无法进行批量分配');
          return;
        }
        
        // 检查宿舍类型是否有效
        if (this.assignForm.dormType !== 'FOUR' && this.assignForm.dormType !== 'SIX') {
          console.warn('宿舍类型可能不正确:', this.assignForm.dormType);
        }
        
        // 构建详细的学生信息数组，包含学号、姓名和系别
        const studentInfoList = this.selectedStudents.map(student => ({
          studentNumber: student.studentNumber,
          studentName: student.name,
          department: student.department
        }));
        
        const data = {
          studentNumbers: studentNumbers,
          studentInfoList: studentInfoList,  // 添加学生详细信息列表
          dormType: this.assignForm.dormType,
          dormitory: selectedDorm.buildingName,  // 宿舍楼
          dormCard: selectedDorm.roomNumber   // 宿舍号
        }
        
        console.log('提交的批量宿舍分配数据:', data);
        
        axios.post('/api/admin/dorm/assign/batch', data)
          .then(response => {
            const { success, failed } = response.data
            if (success > 0 && failed === 0) {
              this.$message.success(`成功为 ${success} 名学生分配宿舍`)
            } else if (success > 0 && failed > 0) {
              this.$message.warning(`${success} 名学生分配成功，${failed} 名学生分配失败`)
            } else {
              this.$message.error(`所有学生分配失败`)
            }
            this.assignDialogVisible = false
            this.loadStudents()
          })
          .catch(error => {
            console.error('批量分配宿舍失败:', error)
            this.$message.error('批量分配宿舍失败: ' + (error.response?.data?.message || '未知错误'))
          })
      }
    },
    
    // 宿舍分配对话框相关方法
    handleDialogOpen() {
      // 对话框打开时，如果已经有宿舍类型，自动加载可用宿舍
      if (this.assignForm.dormType) {
        this.loadAvailableDorms();
      }
    },
    
    handleDormTypeChange() {
      // 根据宿舍类型和学生性别查询可用宿舍
      this.assignForm.dormId = ''
      this.loadAvailableDorms()
    },
    
    loadAvailableDorms() {
      if (!this.assignForm.dormType || !this.assignForm.gender) {
        this.availableDorms = []
        return
      }
      
      const params = {
        dormType: this.assignForm.dormType,
        dormsex: this.getDormSexParam(this.assignForm.gender)
      };
      
      console.log('加载可用宿舍，请求参数:', params);
      
      axios.get('/api/admin/dorm/available', { params })
        .then(response => {
          // 检查响应格式
          console.log('完整响应数据:', response);
          let dormsData = [];
          
          // 判断响应格式并提取数据
          if (response.data && response.data.code === 200 && response.data.data) {
            dormsData = response.data.data;
            console.log('提取的可用宿舍数据:', dormsData);
          } else if (Array.isArray(response.data)) {
            dormsData = response.data;
            console.log('提取的可用宿舍数据(数组格式):', dormsData);
          } else {
            console.error('无法识别的响应格式:', response.data);
            this.$message.error('获取宿舍数据格式错误');
            return;
          }
          
          // 转换字段名称以匹配前端期望的格式
          this.availableDorms = dormsData.map(dorm => {
            return {
              id: dorm.id || dorm.dormCard,
              buildingName: dorm.dormitory || '未知楼栋',
              roomNumber: dorm.dormCard || '未知房间号',
              capacity: dorm.dormNum || 0,
              occupied: dorm.members ? dorm.members.length : 0,
              type: dorm.dormType || this.assignForm.dormType,
              gender: dorm.dormsex === '男' ? 'MALE' : dorm.dormsex === '女' ? 'FEMALE' : '',
              department: dorm.department || '未知'
            };
          });
          
          console.log('转换后的可用宿舍数据:', this.availableDorms);
        })
        .catch(error => {
          console.error('获取可用宿舍失败:', error)
          this.$message.error('获取可用宿舍失败')
        })
    },
    
    showAssignDialog() {
      if (this.selectedStudents.length === 0) {
        this.$message.warning('请选择学生')
        return
      }
      
      // 检查所选学生性别是否一致
      const genders = [...new Set(this.selectedStudents.map(student => student.gender))]
      if (genders.length > 1) {
        this.$message.error('批量分配宿舍要求所选学生性别一致')
        return
      }
      
      this.assignForm = {
        dormType: 'FOUR',  // 默认四人间
        gender: genders[0],
        dormId: ''
      }
      this.assignDialogVisible = true
    },
    
    // 宿舍相关方法
    loadDorms() {
      // 开始加载，显示加载指示器
      this.loadingDorms = true;
      console.log('开始加载宿舍数据...', new Date().toISOString());
      
      // 构建API请求参数，包括分页和筛选条件
      // 注意：所有筛选都由后端执行，前端不进行本地筛选
      const params = {
        page: this.dormCurrentPage,  // 当前页码
        size: this.dormPageSize,     // 每页数量
        keyword: this.dormFilter.roomNumber || this.dormFilter.buildingName || null,  // 关键词搜索(宿舍号或楼栋名)
        dormsex: this.getDormSexParam(this.dormFilter.gender),  // 性别筛选
        dormType: this.dormFilter.type || null  // 宿舍类型筛选(FOUR/SIX)
      };
      
      console.log('宿舍数据请求参数:', params);
      console.log('准备发送宿舍数据请求: /api/admin/dorm/list', params);
      
      axios.get('/api/admin/dorm/list', { params })
        .then(response => {
          console.log('宿舍数据API响应状态:', response.status, response.statusText);
          console.log('宿舍数据API响应:', response.data);
          // 处理后端返回的数据格式
          if (response.data.code === 200 && response.data.data) {
            const responseData = response.data.data;
            
            // 字段映射：将后端字段名映射到前端期望的字段名
            const mappedData = (responseData.list || []).map(item => {
              // 添加一些调试信息
              console.log('处理宿舍数据项:', item);
              
              // 转换宿舍类型从中文到枚举值
              let typeEnum = 'UNKNOWN';
              if (item.dormType === '四人间') {
                typeEnum = 'FOUR';
              } else if (item.dormType === '六人间') {
                typeEnum = 'SIX';
              }
              
              // 确保数据正确映射
              return {
                id: item.id || item.dormCard,
                buildingName: item.dormitory || '未知楼栋',
                roomNumber: item.dormCard || '未知房间号',
                type: typeEnum,  // 使用转换后的枚举值
                gender: item.dormsex === '男' ? 'MALE' : item.dormsex === '女' ? 'FEMALE' : 'UNKNOWN',
                capacity: Number(item.dormNum) || 0,
                occupied: item.members ? item.members.length : 0,
                department: item.department || '未知'
              };
            });
            
            console.log('映射后的宿舍数据:', mappedData);
            
            // 设置表格数据为后端返回的数据
            this.dormList = mappedData;
            
            // 使用后端返回的总数量
            if (responseData.total !== undefined && responseData.total !== null) {
              console.log('使用后端返回的总数量:', responseData.total);
              this.totalDorms = responseData.total;
            } else {
              // 后端没有提供总数，使用当前数据长度作为后备
              console.log('后端未返回总数量，使用当前页数据长度:', mappedData.length);
              this.totalDorms = mappedData.length;
            }
          } else {
            // 兜底处理
            this.dormList = [];
            this.totalDorms = 0;
            console.error('未知的宿舍数据格式:', response.data);
          }
        })
        .catch(error => {
          console.error('获取宿舍列表失败:', error);
          this.$message.error('获取宿舍列表失败');
        })
        .finally(() => {
          this.loadingDorms = false;
        });
    },
    
    // 将前端性别参数转换为后端参数格式
    getDormSexParam(gender) {
      if (!gender) return null;
      // 处理中文输入
      if (gender === '男' || gender === '女') {
        return gender === '男' ? 'MALE' : 'FEMALE';
      }
      // 处理英文输入
      if (gender === 'MALE' || gender === 'FEMALE') {
        return gender;
      }
      return null;
    },
    
    searchDorms() {
      // 每次筛选操作时，重置页码到第1页
      this.dormCurrentPage = 1;
      
      console.log('执行宿舍搜索，筛选条件:', this.dormFilter);
      console.log('筛选条件详情:',
                 '楼栋名称:', this.dormFilter.buildingName, 
                 '宿舍号:', this.dormFilter.roomNumber, 
                 '类型:', this.dormFilter.type, 
                 '性别:', this.dormFilter.gender);
      
      // 筛选条件会通过loadDorms方法发送到后端API，由后端执行筛选
      this.loadDorms();
    },
    
    resetDormFilter() {
      this.dormFilter = {
        buildingName: '',
        roomNumber: '',
        type: '',
        gender: ''
      }
      this.searchDorms()
    },
    
    handleDormSizeChange(size) {
      this.dormPageSize = size
      this.loadDorms()
    },
    
    handleDormCurrentChange(page) {
      this.dormCurrentPage = page
      this.loadDorms()
    },
    
    // 宿舍成员查看
    viewDormMembers(dorm) {
      // 更新API路径和参数
      const params = {
        dormitory: dorm.buildingName,
        dormCard: dorm.roomNumber
      };
      
      axios.get('/api/admin/dorm/members', { params })
        .then(response => {
          console.log('宿舍成员查询响应:', response);
          if (response.data.code === 200) {
            // 转换成员数据格式
            const memberData = (response.data.data || []).map(member => {
              return {
                studentNumber: member.studentNumber || member.studentId || '',
                name: member.studentName || member.name || '',
                gender: member.sex === '男' ? 'MALE' : member.sex === '女' ? 'FEMALE' : 'UNKNOWN',
                department: member.department || '',
                major: member.majorname || member.major || '',
                className: member.classroom_name || member.className || ''
              };
            });
            
            console.log('转换后的宿舍成员数据:', memberData);
            this.dormMembers = memberData;
          } else {
            this.dormMembers = [];
            console.error('获取宿舍成员响应异常:', response.data);
          }
          this.membersDialogVisible = true
        })
        .catch(error => {
          console.error('获取宿舍成员失败:', error)
          this.$message.error('获取宿舍成员失败')
          this.dormMembers = [];
        })
    },
    
    // 宿舍添加/编辑
    showAddDormDialog() {
      console.log('打开添加宿舍对话框');
      this.dormFormTitle = '添加宿舍'
      this.dormForm = {
        id: null,
        buildingName: '',
        roomNumber: '',
        type: '',
        gender: ''
      }
      this.dormDialogVisible = true
    },
    
    editDorm(dorm) {
      this.dormFormTitle = '编辑宿舍'
      this.dormForm = {
        id: dorm.id,
        buildingName: dorm.buildingName,
        roomNumber: dorm.roomNumber,
        type: dorm.type,
        gender: dorm.gender
      }
      this.dormDialogVisible = true
    },
    
    submitDormForm() {
      console.log('提交宿舍表单:', this.dormForm);
      this.$refs.dormForm.validate(valid => {
        if (valid) {
          // 转换前端字段到后端字段
          const dormData = {
            id: this.dormForm.id,
            dormitory: this.dormForm.buildingName,  // 转换字段名
            dormCard: this.dormForm.roomNumber,     // 转换字段名
            dormType: this.getApiDormType(this.dormForm.type),  // 转换宿舍类型格式
            dormsex: this.getApiDormSex(this.dormForm.gender)   // 转换性别格式
          };
          
          console.log('转换后的宿舍数据:', dormData);
          
          if (this.dormForm.id) {
            // 更新宿舍
            console.log('正在更新宿舍:', this.dormForm.id);
            axios.put(`/api/admin/dorm/dorm/update`, dormData)  // 更新API路径
              .then(response => {
                console.log('宿舍更新响应:', response);
                this.$message.success('宿舍信息更新成功')
                this.dormDialogVisible = false
                this.loadDorms()
              })
              .catch(error => {
                console.error('更新宿舍信息失败:', error)
                this.$message.error('更新宿舍信息失败: ' + (error.response?.data?.message || '未知错误'))
              })
          } else {
            // 新增宿舍
            console.log('正在添加新宿舍');
            axios.post('/api/admin/dorm/dorm/add', dormData)  // 更新API路径
              .then(response => {
                console.log('宿舍添加响应:', response);
                this.$message.success('宿舍添加成功')
                this.dormDialogVisible = false
                this.loadDorms()
              })
              .catch(error => {
                console.error('添加宿舍失败:', error)
                this.$message.error('添加宿舍失败: ' + (error.response?.data?.message || '未知错误'))
              })
          }
        } else {
          console.log('表单验证失败');
          return false;
        }
      })
    },
    
    // 辅助方法：转换宿舍类型为API需要的格式
    getApiDormType(type) {
      switch(type) {
        case 'FOUR': return '四人间';
        case 'SIX': return '六人间';
        default: return type;
      }
    },
    
    // 辅助方法：转换性别为API需要的格式
    getApiDormSex(gender) {
      switch(gender) {
        case 'MALE': return '男';
        case 'FEMALE': return '女';
        default: return gender;
      }
    },
    
    // 宿舍删除
    confirmDeleteDorm(dorm) {
      this.$confirm(`确定要删除宿舍 "${dorm.buildingName} - ${dorm.roomNumber}" 吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 更新删除API路径和参数
        const params = {
          dormitory: dorm.buildingName,
          dormCard: dorm.roomNumber
        };
        
        axios.delete('/api/admin/dorm/dorm/delete', { params })
          .then(response => {
            console.log('宿舍删除响应:', response);
            this.$message.success('宿舍删除成功')
            this.loadDorms()
          })
          .catch(error => {
            console.error('删除宿舍失败:', error)
            if (error.response && error.response.status === 400) {
              this.$message.error('该宿舍内还有学生，无法删除')
            } else {
              this.$message.error('删除宿舍失败')
            }
          })
      }).catch(() => {
        // 取消操作
      })
    },
    
    openAssignDormDialog(student) {
      this.currentStudent = student
      this.assignForm = {
        dormType: student.dormType || 'FOUR',  // 设置默认宿舍类型
        gender: student.gender,
        dormId: ''
      }
      this.assignDialogVisible = true
    },
    
    // 获取学生状态文本
    getStudentStatusText(student) {
      if (student.assignmentStatus === 'assigned') {
        return '已分配';
      } else if (student.assignmentStatus === 'pending') {
        return '待分配';
      } else {
        return '未分配';
      }
    },
    
    // 获取学生状态标签类型
    getStudentStatusType(student) {
      if (student.assignmentStatus === 'assigned') {
        return 'success';
      } else if (student.assignmentStatus === 'pending') {
        return 'warning';
      } else {
        return 'info';
      }
    },
    
    // 判断是否为待分配状态的学生
    isPendingStudent(student) {
      console.log('检查学生是否为待分配状态:', student);
      return student.assignmentStatus === 'pending';
    },

    // 添加事件处理函数
    handleError(event) {
      if (event.message && event.message.includes('ResizeObserver')) {
        event.stopImmediatePropagation();
      }
    },

    // 添加一个新方法：使用单个分配接口依次分配学生
    async assignStudentsOneByOne(studentNumbers, dormData) {
      const results = {
        success: 0,
        failed: 0,
        details: []
      };
      
      // 显示进度提示
      const loadingInstance = this.$loading({
        lock: true,
        text: `正在分配学生 (0/${studentNumbers.length})`,
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      
      try {
        for (let i = 0; i < studentNumbers.length; i++) {
          const studentNumber = studentNumbers[i];
          
          // 获取当前学生的完整信息
          const studentInfo = this.selectedStudents.find(student => student.studentNumber === studentNumber);
          if (!studentInfo) {
            results.failed++;
            results.details.push({
              studentNumber,
              success: false,
              message: '无法获取学生详细信息'
            });
            continue;
          }
          
          // 更新加载提示
          loadingInstance.text = `正在分配学生 (${i+1}/${studentNumbers.length})`;
          
          const singleData = {
            studentNumber: studentNumber,
            studentName: studentInfo.name,  // 添加学生姓名
            department: studentInfo.department,  // 添加系别
            dormType: dormData.dormType,
            dormitory: dormData.dormitory,
            dormCard: dormData.dormCard
          };
          
          // 调用单个分配接口
          await axios.post('/api/admin/dorm/assign', singleData)
            .then(response => {
              if (response.data.code === 200) {
                results.success++;
                results.details.push({
                  studentNumber,
                  success: true,
                  message: '分配成功'
                });
              } else {
                results.failed++;
                results.details.push({
                  studentNumber,
                  success: false,
                  message: response.data.message || '未知错误'
                });
              }
            })
            .catch(error => {
              results.failed++;
              results.details.push({
                studentNumber,
                success: false,
                message: error.response?.data?.message || error.message || '未知错误'
              });
            });
        }
      } catch (error) {
        console.error('分配学生失败:', error);
        results.failed = studentNumbers.length;
        results.details = studentNumbers.map(number => ({
          studentNumber: number,
          success: false,
          message: error.response?.data?.message || error.message || '未知错误'
        }));
      } finally {
        loadingInstance.close();
        if (results.success > 0 && results.failed === 0) {
          this.$message.success(`成功为 ${results.success} 名学生分配宿舍`);
        } else if (results.success > 0 && results.failed > 0) {
          this.$message.warning(`${results.success} 名学生分配成功，${results.failed} 名学生分配失败`);
        } else {
          this.$message.error(`所有学生分配失败`);
        }
        this.assignDialogVisible = false;
        this.loadStudents();
      }
    }
  },
  beforeUnmount() {
    // 移除全局错误监听器
    window.removeEventListener('error', this.handleError);
  }
}
</script>

<style scoped>
.dormitory-manage {
  margin: 20px;
}

.dormitory-manage .main-card {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
  border-radius: 4px;
}

.dormitory-manage .page-header {
  margin-bottom: 20px;
  text-align: center;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 15px;
}

.dormitory-manage .page-header h2 {
  margin: 0;
  padding: 0;
  color: #303133;
  font-weight: 600;
}

.dormitory-manage .filter-container {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;
}

.dormitory-manage .filter-form {
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.dormitory-manage .filter-form .el-form-item {
  margin-bottom: 15px;
  margin-right: 15px;
}

.dormitory-manage .filter-form .el-form-item .el-select .el-input__inner {
  border-radius: 4px;
}

.dormitory-manage .filter-form .el-form-item__label {
  font-weight: 500;
}

.dormitory-manage .operation-buttons {
  margin-left: 20px;
  display: flex;
  gap: 10px;
}

.dormitory-manage .status-filter {
  margin-bottom: 20px;
}

.dormitory-manage .status-filter .el-radio-button__inner {
  padding: 10px 20px;
}

.dormitory-manage .table-container .table-operations {
  margin-bottom: 15px;
}

.dormitory-manage .table-container .el-table {
  margin-bottom: 20px;
}

.dormitory-manage .table-container .el-table th {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: bold;
  padding: 8px 0;
  text-align: center;
}

.dormitory-manage .table-container .el-table td {
  padding: 8px;
  text-align: center;
}

.dormitory-manage .table-container .el-table .el-button {
  padding: 5px 10px;
  margin: 0 2px;
}

.dormitory-manage .pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dormitory-manage .unassigned {
  color: #F56C6C;
  font-weight: bold;
}

.dormitory-manage .note {
  margin-left: 10px;
  color: #909399;
  font-size: 12px;
}

.dormitory-manage .no-data {
  text-align: center;
  color: #909399;
  padding: 30px 0;
  font-size: 14px;
}

.dormitory-manage .action-buttons {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 8px;
}

.dormitory-manage .el-tag {
  font-weight: normal;
}

.dormitory-manage .el-tag.el-tag--success {
  background-color: #f0f9eb;
}

.dormitory-manage .el-tag.el-tag--warning {
  background-color: #fdf6ec;
}

.dormitory-manage .el-tag.el-tag--info {
  background-color: #f4f4f5;
}

.dormitory-manage :deep(.el-button--primary) {
  background-color: #409EFF;
}

.dormitory-manage :deep(.el-button--success) {
  background-color: #67C23A;
}

.dormitory-manage :deep(.el-button--warning) {
  background-color: #E6A23C;
}

.dormitory-manage :deep(.el-button--danger) {
  background-color: #F56C6C;
}

.dormitory-manage :deep(.row-success) {
  background-color: #f0f9eb !important;
}

.dormitory-manage :deep(.row-danger) {
  background-color: #fef0f0 !important;
}

.dormitory-manage :deep(.el-table .even-row.row-success) {
  background-color: #ebf5e6 !important;
}

.dormitory-manage :deep(.el-table .odd-row.row-success) {
  background-color: #f0f9eb !important;
}

.dormitory-manage :deep(.el-table .even-row.row-danger) {
  background-color: #fbe7e7 !important;
}

.dormitory-manage :deep(.el-table .odd-row.row-danger) {
  background-color: #fef0f0 !important;
}

.dormitory-manage .el-select-dropdown__item {
  padding: 0 15px;
}

.dormitory-manage :deep(.el-select-dropdown) {
  min-width: 120px !important;
}

.dormitory-manage :deep(.el-input__inner) {
  height: 36px;
  line-height: 36px;
}

/* 现代化表格样式 */
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
  text-align: center !important;
}

:deep(.el-table .even-row) {
  background-color: #fafafa;
}

:deep(.el-table .odd-row) {
  background-color: #ffffff;
}

:deep(.el-table .cell) {
  padding: 0 10px;
  text-align: center;
}

/* 表格操作按钮样式优化 */
.action-buttons {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 8px;
}

:deep(.el-button--small) {
  padding: 8px 12px;
  font-size: 12px;
  border-radius: 4px;
}

/* 批量操作按钮区域样式 */
.batch-operation {
  margin: 10px 0 20px;
  display: flex;
  justify-content: flex-end;
}

/* 添加全局表格文本居中样式 */
:deep(.el-table) {
  --el-table-border-color: #ebeef5;
  --el-table-header-bg-color: #f5f7fa;
  --el-table-row-hover-bg-color: #f5f7fa;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

:deep(.el-table th),
:deep(.el-table td) {
  text-align: center !important;
}
</style> 