<template>
  <div class="dorm-manage-container">
    <el-card class="dorm-card">
      <div class="filter-container">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索系别、宿舍楼或宿舍号"
          clearable
          style="width: 220px;"
          @keyup.enter="handleSearch"
        />
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleAddDorm">添加宿舍</el-button>
        <el-button type="primary" icon="el-icon-s-grid" @click="openBatchAssignDialog">批量分配</el-button>
      </div>

      <!-- 分类标签页 -->
      <el-tabs v-model="activeTab" @tab-click="handleTabChange">
        <el-tab-pane label="全部宿舍" name="all"></el-tab-pane>
        <el-tab-pane label="已分配" name="assigned"></el-tab-pane>
        <el-tab-pane label="未分配" name="unassigned"></el-tab-pane>
      </el-tabs>

      <el-table
        v-loading="tableLoading"
        :data="dormList"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column
          prop="department"
          label="系别"
          min-width="120"
          align="center"
        />
        <el-table-column
          prop="dormitory"
          label="宿舍楼"
          min-width="100"
          align="center"
        />
        <el-table-column
          prop="dormCard"
          label="宿舍号"
          min-width="100"
          align="center"
        />
        <el-table-column
          prop="dormType"
          label="宿舍类型"
          min-width="120"
          align="center"
        />
        <el-table-column
          prop="dormNum"
          label="宿舍容量"
          min-width="100"
          align="center"
        />
        <el-table-column
          prop="dormPrize"
          label="宿舍价格"
          min-width="100"
          align="center"
        />
        <el-table-column
          prop="dormsex"
          label="性别要求"
          min-width="100"
          align="center"
        />
        <el-table-column
          label="操作"
          width="220"
          align="center"
          fixed="right"
        >
          <template #default="scope">
            <div class="action-buttons">
              <el-button
                size="mini"
                type="primary"
                @click="handleViewMembers(scope.row)"
              >
                查看成员
              </el-button>
              <el-button
                size="mini"
                type="success"
                @click="handleEditDorm(scope.row)"
              >
                编辑
              </el-button>
              <el-button
                size="mini"
                type="danger"
                @click="handleDeleteDorm(scope.row)"
              >
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          :total="total"
          :current-page="currentPage"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 宿舍表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
      destroy-on-close
    >
      <el-form :model="dormForm" :rules="rules" ref="dormForm" label-width="100px">
        <el-form-item label="系别" prop="department">
          <el-input v-model="dormForm.department" placeholder="请输入系别"></el-input>
        </el-form-item>
        <el-form-item label="宿舍楼" prop="dormitory">
          <el-input v-model="dormForm.dormitory" placeholder="请输入宿舍楼"></el-input>
        </el-form-item>
        <el-form-item label="宿舍号" prop="dormCard">
          <el-input v-model="dormForm.dormCard" placeholder="请输入宿舍号"></el-input>
        </el-form-item>
        <el-form-item label="宿舍类型" prop="dormType">
          <el-select v-model="dormForm.dormType" placeholder="请选择宿舍类型" style="width: 100%">
            <el-option label="四人间" value="四人间"></el-option>
            <el-option label="六人间" value="六人间"></el-option>
            <el-option label="八人间" value="八人间"></el-option>
            <el-option label="单人间" value="单人间"></el-option>
            <el-option label="双人间" value="双人间"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="宿舍容量" prop="dormNum">
          <el-input-number v-model="dormForm.dormNum" :min="1" :max="12"></el-input-number>
        </el-form-item>
        <el-form-item label="宿舍价格" prop="dormPrize">
          <el-input-number v-model="dormForm.dormPrize" :min="0" :step="100"></el-input-number>
        </el-form-item>
        <el-form-item label="性别要求" prop="dormsex">
          <el-select v-model="dormForm.dormsex" placeholder="请选择性别要求" style="width: 100%">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitDormForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
    
    <!-- 宿舍成员对话框 -->
    <el-dialog
      title="宿舍成员"
      v-model="membersDialogVisible"
      width="800px"
      destroy-on-close
    >
      <div v-if="selectedDorm" class="dorm-info">
        <span>{{ selectedDorm.dormitory }} 栋 {{ selectedDorm.dormCard }} 房间 
          <span v-if="dormMembers.length > 0">(已住: {{ dormMembers.length }}人 / 总容量: {{ selectedDorm.dormNum }}人)</span>
        </span>
        <el-button 
          type="primary" 
          size="small" 
          icon="el-icon-plus" 
          style="margin-left: 20px;"
          @click="openAssignDialog"
          :disabled="dormMembers.length >= selectedDorm.dormNum"
        >
          分配学生
        </el-button>
      </div>
      
      <el-table
        v-if="dormMembers.length > 0"
        :data="dormMembers"
        border
        style="width: 100%; margin-top: 15px;"
      >
        <el-table-column
          prop="studentNumber"
          label="学号"
          min-width="120"
          align="center"
        />
        <el-table-column
          prop="name"
          label="姓名"
          min-width="100"
          align="center"
        />
        <el-table-column
          prop="gender"
          label="性别"
          width="80"
          align="center"
        />
        <el-table-column
          prop="department"
          label="系别"
          min-width="120"
          align="center"
        />
        <el-table-column
          prop="majorname"
          label="专业"
          min-width="150"
          align="center"
        />
        <el-table-column
          prop="classroom.name"
          label="班级"
          min-width="100"
          align="center"
        />
        <el-table-column
          label="操作"
          width="120"
          align="center"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              size="mini"
              type="danger"
              @click="handleRemoveFromDorm(scope.row)"
            >
              移除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div v-else class="empty-members">
        <el-empty description="暂无学生" :image-size="100"></el-empty>
      </div>
    </el-dialog>
    
    <!-- 分配学生对话框 -->
    <el-dialog
      title="分配学生到宿舍"
      v-model="assignDialogVisible"
      width="1000px"
      destroy-on-close
    >
      <div class="filter-container">
        <el-select
          v-model="departmentFilter"
          placeholder="选择系别"
          clearable
          style="width: 180px; margin-right: 10px;"
          @change="handleFilterChange"
        >
          <el-option
            v-for="item in departmentOptions"
            :key="item"
            :label="item"
            :value="item"
          />
        </el-select>
        
        <el-select
          v-model="majorFilter"
          placeholder="选择专业"
          clearable
          style="width: 220px; margin-right: 10px;"
          @change="handleFilterChange"
        >
          <el-option
            v-for="item in majorOptions"
            :key="item"
            :label="item"
            :value="item"
          />
        </el-select>
        
        <el-select
          v-model="classFilter"
          placeholder="选择班级"
          clearable
          style="width: 180px; margin-right: 10px;"
          @change="handleFilterChange"
        >
          <el-option
            v-for="item in classOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
        
        <el-button type="primary" icon="el-icon-search" @click="fetchUnassignedStudents">搜索</el-button>
      </div>
      
      <el-table
        v-loading="studentsLoading"
        :data="unassignedStudents"
        border
        style="width: 100%; margin-top: 15px;"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          width="55"
          align="center"
        />
        <el-table-column
          prop="studentNumber"
          label="学号"
          min-width="120"
          align="center"
        />
        <el-table-column
          prop="name"
          label="姓名"
          min-width="100"
          align="center"
        />
        <el-table-column
          prop="gender"
          label="性别"
          width="80"
          align="center"
        />
        <el-table-column
          prop="department"
          label="系别"
          min-width="120"
          align="center"
        />
        <el-table-column
          prop="majorname"
          label="专业"
          min-width="150"
          align="center"
        />
        <el-table-column
          prop="classroom.name"
          label="班级"
          min-width="100"
          align="center"
        />
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[10, 20, 50]"
          :page-size="studentPageSize"
          :total="studentTotal"
          :current-page="studentCurrentPage"
          @size-change="handleStudentSizeChange"
          @current-change="handleStudentCurrentChange"
        />
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <span style="float: left; line-height: 32px;">
            已选择 {{ selectedStudents.length }} 名学生，宿舍可分配人数: {{ selectedDorm ? (selectedDorm.dormNum - dormMembers.length) : 0 }}
          </span>
          <div style="float: left; width: 100%; margin-bottom: 10px; color: #E6A23C; font-size: 13px;">
            <i class="el-icon-warning-outline"></i> 提示：有选择好宿舍号才算分配成功，请确保学生性别与宿舍要求匹配
          </div>
          <el-button @click="assignDialogVisible = false">取 消</el-button>
          <el-button 
            type="primary" 
            @click="assignStudentsToDorm"
            :disabled="selectedStudents.length === 0"
          >
            确认分配
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 批量分配对话框 -->
    <el-dialog
      title="批量分配宿舍"
      v-model="batchAssignDialogVisible"
      width="1000px"
      destroy-on-close
    >
      <div class="batch-assign-container">
        <div class="filter-section">
          <h3>学生筛选</h3>
          <div class="filter-row">
            <el-select
              v-model="batchDepartmentFilter"
              placeholder="选择系别"
              clearable
              style="width: 180px; margin-right: 10px;"
              @change="handleBatchFilterChange"
            >
              <el-option
                v-for="item in departmentOptions"
                :key="item"
                :label="item"
                :value="item"
              />
            </el-select>
            
            <el-select
              v-model="batchMajorFilter"
              placeholder="选择专业"
              clearable
              style="width: 220px; margin-right: 10px;"
              @change="handleBatchFilterChange"
            >
              <el-option
                v-for="item in majorOptions"
                :key="item"
                :label="item"
                :value="item"
              />
            </el-select>
            
            <el-select
              v-model="batchClassFilter"
              placeholder="选择班级"
              clearable
              style="width: 180px; margin-right: 10px;"
              @change="handleBatchFilterChange"
            >
              <el-option
                v-for="item in classOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
            
            <el-button type="primary" icon="el-icon-search" @click="fetchBatchUnassignedStudents">搜索</el-button>
          </div>
        </div>
        
        <el-table
          v-loading="batchStudentsLoading"
          :data="batchUnassignedStudents"
          border
          style="width: 100%; margin-top: 15px;"
          height="250px"
          @selection-change="handleBatchSelectionChange"
        >
          <el-table-column
            type="selection"
            width="55"
            align="center"
          />
          <el-table-column
            prop="studentNumber"
            label="学号"
            min-width="120"
            align="center"
          />
          <el-table-column
            prop="name"
            label="姓名"
            min-width="100"
            align="center"
          />
          <el-table-column
            prop="gender"
            label="性别"
            width="80"
            align="center"
          />
          <el-table-column
            prop="department"
            label="系别"
            min-width="120"
            align="center"
          />
          <el-table-column
            prop="majorname"
            label="专业"
            min-width="150"
            align="center"
          />
          <el-table-column
            prop="classroom.name"
            label="班级"
            min-width="100"
            align="center"
          />
        </el-table>
        
        <div class="dorm-section">
          <h3>宿舍选择</h3>
          <div class="filter-row">
            <el-select
              v-model="batchDormType"
              placeholder="选择宿舍类型"
              style="width: 180px; margin-right: 10px;"
              @change="fetchAvailableDorms"
            >
              <el-option label="四人间" value="四人间"></el-option>
              <el-option label="六人间" value="六人间"></el-option>
              <el-option label="八人间" value="八人间"></el-option>
              <el-option label="单人间" value="单人间"></el-option>
              <el-option label="双人间" value="双人间"></el-option>
            </el-select>
            
            <el-select
              v-model="batchDormSex"
              placeholder="选择性别要求"
              style="width: 180px; margin-right: 10px;"
              @change="fetchAvailableDorms"
            >
              <el-option label="男" value="男"></el-option>
              <el-option label="女" value="女"></el-option>
            </el-select>
            
            <el-select
              v-model="batchDormBuilding"
              placeholder="选择宿舍楼"
              clearable
              style="width: 180px; margin-right: 10px;"
              @change="fetchAvailableDorms"
            >
              <el-option
                v-for="item in dormBuildingOptions"
                :key="item"
                :label="item"
                :value="item"
              />
            </el-select>
            
            <el-button type="primary" icon="el-icon-search" @click="fetchAvailableDorms">筛选宿舍</el-button>
          </div>
          
          <el-table
            v-loading="batchDormsLoading"
            :data="availableDorms"
            border
            style="width: 100%; margin-top: 15px;"
            height="250px"
            @selection-change="handleDormSelectionChange"
          >
            <el-table-column
              type="selection"
              width="55"
              align="center"
            />
            <el-table-column
              prop="department"
              label="系别"
              min-width="120"
              align="center"
            />
            <el-table-column
              prop="dormitory"
              label="宿舍楼"
              min-width="100"
              align="center"
            />
            <el-table-column
              prop="dormCard"
              label="宿舍号"
              min-width="100"
              align="center"
            />
            <el-table-column
              prop="dormType"
              label="宿舍类型"
              min-width="120"
              align="center"
            />
            <el-table-column
              prop="availableCount"
              label="可用床位"
              min-width="100"
              align="center"
            />
            <el-table-column
              prop="dormsex"
              label="性别要求"
              min-width="100"
              align="center"
            />
          </el-table>
        </div>
      </div>
      
      <div class="assignment-info" v-if="batchSelectedStudents.length > 0 && selectedDorms.length > 0">
        <el-alert
          title="分配预览"
          type="info"
          :closable="false"
        >
          <p>已选择 {{batchSelectedStudents.length}} 名学生 ({{studentGenderCount}})</p>
          <p>已选择 {{selectedDorms.length}} 个宿舍，共 {{totalAvailableBeds}} 个可用床位</p>
          <p>分配方式：{{assignmentMethod === 'auto' ? '自动分配' : '手动指定'}}</p>
        </el-alert>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <div style="float: left; width: 100%; margin-bottom: 10px; color: #E6A23C; font-size: 13px;">
            <i class="el-icon-warning-outline"></i> 提示：有选择好宿舍号才算分配成功，请确保学生性别与宿舍要求匹配
          </div>
          
          <div style="display: flex; gap: 10px; margin-bottom: 10px;">
            <span>分配方式：</span>
            <el-radio-group v-model="assignmentMethod" size="small">
              <el-radio label="auto">自动分配</el-radio>
              <el-radio label="manual">手动指定</el-radio>
            </el-radio-group>
          </div>
          
          <el-button @click="batchAssignDialogVisible = false">取 消</el-button>
          <el-button 
            type="primary" 
            @click="executeBatchAssignment"
            :disabled="batchSelectedStudents.length === 0 || selectedDorms.length === 0"
          >
            确认分配
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
import { debounce } from 'lodash'

export default {
  name: 'DormitoryManageView',
  data() {
    return {
      // 宿舍列表相关
      dormList: [],
      tableLoading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      searchKeyword: '',
      
      // 表单相关
      dialogVisible: false,
      dialogTitle: '添加宿舍',
      dormForm: {
        department: '',
        dormitory: '',
        dormCard: '',
        dormType: '',
        dormNum: 4,
        dormPrize: 800,
        dormsex: ''
      },
      rules: {
        department: [
          { required: true, message: '请输入系别', trigger: 'blur' }
        ],
        dormitory: [
          { required: true, message: '请输入宿舍楼', trigger: 'blur' }
        ],
        dormCard: [
          { required: true, message: '请输入宿舍号', trigger: 'blur' }
        ],
        dormType: [
          { required: true, message: '请选择宿舍类型', trigger: 'change' }
        ],
        dormNum: [
          { required: true, message: '请输入宿舍容量', trigger: 'blur' }
        ],
        dormsex: [
          { required: true, message: '请选择性别要求', trigger: 'change' }
        ]
      },
      
      // 宿舍成员相关
      membersDialogVisible: false,
      selectedDorm: null,
      dormMembers: [],
      
      // 分配学生相关
      assignDialogVisible: false,
      departmentFilter: '',
      majorFilter: '',
      classFilter: '',
      departmentOptions: [],
      majorOptions: [],
      classOptions: [],
      unassignedStudents: [],
      studentsLoading: false,
      selectedStudents: [],
      studentCurrentPage: 1,
      studentPageSize: 10,
      studentTotal: 0,
      
      // 新增数据
      activeTab: 'all',
      
      // 批量分配相关
      batchAssignDialogVisible: false,
      batchDepartmentFilter: '',
      batchMajorFilter: '',
      batchClassFilter: '',
      batchUnassignedStudents: [],
      batchStudentsLoading: false,
      batchSelectedStudents: [],
      
      batchDormType: '',
      batchDormSex: '',
      batchDormBuilding: '',
      dormBuildingOptions: [],
      availableDorms: [],
      batchDormsLoading: false,
      selectedDorms: [],
      assignmentMethod: 'auto',
    }
  },
  created() {
    this.fetchDormList()
    // 使用防抖处理的搜索方法
    this.debouncedFilterChange = debounce(this.fetchUnassignedStudents, 300)
  },
  methods: {
    // 获取宿舍列表
    fetchDormList() {
      this.tableLoading = true
      axios.get('/api/admin/dorm/list', {
        params: {
          page: this.currentPage,
          size: this.pageSize,
          keyword: this.searchKeyword || undefined,
          status: this.activeTab === 'all' ? undefined : this.activeTab
        }
      }).then(response => {
        const { data } = response.data
        this.dormList = data.list
        this.total = data.total
        this.tableLoading = false
      }).catch(error => {
        console.error('获取宿舍列表失败', error)
        this.tableLoading = false
        this.$message.error('获取宿舍列表失败')
      })
    },
    
    // 搜索
    handleSearch() {
      this.currentPage = 1
      this.fetchDormList()
    },
    
    // 分页
    handleSizeChange(val) {
      this.pageSize = val
      this.fetchDormList()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.fetchDormList()
    },

    // 添加宿舍
    handleAddDorm() {
      this.dialogTitle = '添加宿舍'
      this.dormForm = {
        department: '',
        dormitory: '',
        dormCard: '',
        dormType: '',
        dormNum: 4,
        dormPrize: 800,
        dormsex: ''
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.dormForm) {
          this.$refs.dormForm.clearValidate()
        }
      })
    },

    // 编辑宿舍
    handleEditDorm(row) {
      this.dialogTitle = '编辑宿舍'
      this.dormForm = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.dormForm) {
          this.$refs.dormForm.clearValidate()
        }
      })
    },
    
    // 提交宿舍表单
    submitDormForm() {
      this.$refs.dormForm.validate(valid => {
        if (valid) {
          const isEdit = this.dormForm.dormitory && this.dormForm.dormCard
          const method = isEdit ? 'put' : 'post'
          const url = isEdit ? '/api/admin/dorm/update' : '/api/admin/dorm/add'
          
          axios[method](url, this.dormForm)
            .then(() => {
              this.$message.success(isEdit ? '宿舍更新成功' : '宿舍添加成功')
              this.dialogVisible = false
              this.fetchDormList()
            })
            .catch(error => {
              console.error(isEdit ? '更新宿舍失败' : '添加宿舍失败', error)
              this.$message.error(isEdit ? '更新宿舍失败' : '添加宿舍失败')
            })
        }
      })
    },

    // 删除宿舍
    handleDeleteDorm(row) {
      this.$confirm('确定要删除该宿舍吗？删除前请确保宿舍中没有学生。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.delete('/api/admin/dorm/delete', {
          params: {
            dormitory: row.dormitory,
            dormCard: row.dormCard
          }
        })
          .then(() => {
            this.$message.success('宿舍删除成功')
            this.fetchDormList()
          })
          .catch(error => {
            console.error('删除宿舍失败', error)
            this.$message.error('删除宿舍失败，请确保宿舍中没有学生')
          })
      }).catch(() => {
        // 取消删除
      })
    },
    
    // 查看宿舍成员
    handleViewMembers(row) {
      this.selectedDorm = row
      this.dormMembers = row.members || []
      this.membersDialogVisible = true
    },
    
    // 移除学生
    handleRemoveFromDorm(student) {
      this.$confirm(`确定要将学生 ${student.name} 从宿舍中移除吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
      }).then(() => {
        axios.post('/api/admin/dorm/remove-student', {
          studentNumber: student.studentNumber
        })
          .then(() => {
            this.$message.success('学生移除成功')
            // 更新宿舍成员，不需要刷新整个宿舍列表
            const index = this.dormMembers.findIndex(item => item.studentNumber === student.studentNumber)
            if (index !== -1) {
              this.dormMembers.splice(index, 1)
            }
          })
          .catch(error => {
            console.error('移除学生失败', error)
            this.$message.error('移除学生失败')
          })
      }).catch(() => {
        // 取消移除
      })
    },
    
    // 打开分配学生对话框
    openAssignDialog() {
      this.assignDialogVisible = true
      this.departmentFilter = ''
      this.majorFilter = ''
      this.classFilter = ''
      this.selectedStudents = []
      this.studentCurrentPage = 1
      
      // 只在首次打开时加载筛选选项，避免重复请求
      if (this.departmentOptions.length === 0) {
        this.fetchFilterOptions()
      }
      
      this.fetchUnassignedStudents()
    },
    
    // 合并获取筛选选项的请求
    fetchFilterOptions() {
      // 使用一个请求获取所有筛选选项
      axios.get('/api/admin/filter-options')
        .then(response => {
          const { data } = response.data
          this.departmentOptions = data.departments || []
          this.majorOptions = data.majors || []
          this.classOptions = data.classrooms || []
        })
        .catch(error => {
          console.error('获取筛选选项失败', error)
          this.$message.warning('获取筛选选项失败，将使用默认选项')
          
          // 提供默认数据以便用户仍然可以使用该功能
          this.departmentOptions = ['计算机系', '机械系', '电子系']
          this.majorOptions = ['软件工程', '计算机科学与技术', '人工智能']
          this.classOptions = [
            { id: '1', name: '计算机2101' },
            { id: '2', name: '计算机2102' },
            { id: '3', name: '软件2101' }
          ]
        })
    },
    
    // 备选：分别获取筛选选项
    fetchDepartmentOptions() {
      axios.get('/api/admin/department/all')
        .then(response => {
          this.departmentOptions = response.data.data.map(item => item.name)
        })
        .catch(error => {
          console.error('获取系别失败', error)
        })
    },
    
    fetchMajorOptions() {
      axios.get('/api/admin/major/all')
        .then(response => {
          this.majorOptions = response.data.data.map(item => item.name)
        })
        .catch(error => {
          console.error('获取专业失败', error)
        })
    },
    
    fetchClassroomOptions() {
      axios.get('/api/admin/classroom/all')
        .then(response => {
          this.classOptions = response.data.data
        })
        .catch(error => {
          console.error('获取班级失败', error)
        })
    },
    
    // 筛选变化
    handleFilterChange() {
      this.studentCurrentPage = 1
      this.debouncedFilterChange()
    },
    
    // 获取未分配宿舍的学生
    fetchUnassignedStudents() {
      this.studentsLoading = true
      axios.get('/api/admin/dorm/unassigned-students', {
        params: {
          page: this.studentCurrentPage,
          size: this.studentPageSize,
          department: this.departmentFilter || undefined,
          majorname: this.majorFilter || undefined,
          classroomId: this.classFilter || undefined
        }
      }).then(response => {
        const { data } = response.data
        this.unassignedStudents = data.list
        this.studentTotal = data.total
        this.studentsLoading = false
      }).catch(error => {
        console.error('获取未分配学生失败', error)
        this.studentsLoading = false
        this.$message.error('获取未分配学生失败')
      })
    },
    
    // 学生表格分页
    handleStudentSizeChange(val) {
      this.studentPageSize = val
      this.fetchUnassignedStudents()
    },
    handleStudentCurrentChange(val) {
      this.studentCurrentPage = val
      this.fetchUnassignedStudents()
    },
    
    // 选择学生
    handleSelectionChange(val) {
      this.selectedStudents = val
    },
    
    // 分配学生到宿舍
    assignStudentsToDorm() {
      if (this.selectedStudents.length === 0) {
        this.$message.warning('请选择要分配的学生')
        return
      }
      
      if (!this.selectedDorm) {
        this.$message.warning('未选择宿舍')
        return
      }
      
      const availableSpaces = this.selectedDorm.dormNum - this.dormMembers.length
      if (this.selectedStudents.length > availableSpaces) {
        this.$message.warning(`该宿舍只能再容纳 ${availableSpaces} 名学生`)
        return
      }
      
      // 检查学生性别与宿舍性别是否匹配
      const mismatchedStudents = this.selectedStudents.filter(
        student => student.gender !== this.selectedDorm.dormsex
      )
      
      if (mismatchedStudents.length > 0) {
        this.$confirm(`有 ${mismatchedStudents.length} 名学生性别与宿舍要求不匹配，是否继续？`, '警告', {
          confirmButtonText: '继续',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.submitDormAssignment()
        }).catch(() => {
          // 取消分配
        })
      } else {
        this.submitDormAssignment()
      }
    },
    
    // 提交宿舍分配请求
    submitDormAssignment() {
      const studentNumbers = this.selectedStudents.map(student => student.studentNumber)
      
      axios.post('/api/admin/dorm/assign', {
        studentNumbers,
        dormType: this.selectedDorm.dormType,
        dormitory: this.selectedDorm.dormitory,
        dormCard: this.selectedDorm.dormCard
      })
        .then(() => {
          this.$message.success(`成功将 ${studentNumbers.length} 名学生分配到宿舍`)
          // 关闭分配对话框
          this.assignDialogVisible = false
          // 加载最新宿舍成员，避免重复请求
          this.dormMembers = [...this.dormMembers, ...this.selectedStudents]
        })
        .catch(error => {
          console.error('分配学生失败', error)
          this.$message.error('分配学生失败，请检查宿舍容量')
        })
    },

    // 处理标签页切换
    handleTabChange() {
      this.currentPage = 1
      this.fetchDormList()
    },
    
    // 打开批量分配对话框
    openBatchAssignDialog() {
      this.batchAssignDialogVisible = true
      this.batchDepartmentFilter = ''
      this.batchMajorFilter = ''
      this.batchClassFilter = ''
      this.batchSelectedStudents = []
      this.batchDormType = ''
      this.batchDormSex = ''
      this.batchDormBuilding = ''
      this.selectedDorms = []
      
      // 只在首次打开时加载筛选选项
      if (this.departmentOptions.length === 0) {
        this.fetchFilterOptions()
      }
      
      // 加载宿舍楼选项
      if (this.dormBuildingOptions.length === 0) {
        this.fetchDormBuildings()
      }
      
      this.fetchBatchUnassignedStudents()
    },
    
    // 获取宿舍楼选项
    fetchDormBuildings() {
      axios.get('/api/admin/dorm/buildings')
        .then(response => {
          this.dormBuildingOptions = response.data.data || []
        })
        .catch(error => {
          console.error('获取宿舍楼失败', error)
          this.$message.error('获取宿舍楼失败')
          // 提供默认数据
          this.dormBuildingOptions = ['宿舍楼1', '宿舍楼2', '宿舍楼3'].map(name => ({ name }))
        })
    },
    
    // 批量筛选变化
    handleBatchFilterChange() {
      this.batchSelectedStudents = []
      this.fetchBatchUnassignedStudents()
    },
    
    // 获取批量分配的未分配学生
    fetchBatchUnassignedStudents() {
      this.batchStudentsLoading = true
      axios.get('/api/admin/dorm/unassigned-students', {
        params: {
          page: 1,
          size: 1000, // 加载更多记录用于批量操作
          department: this.batchDepartmentFilter || undefined,
          majorname: this.batchMajorFilter || undefined,
          classroomId: this.batchClassFilter || undefined
        }
      }).then(response => {
        const { data } = response.data
        this.batchUnassignedStudents = data.list
        this.batchStudentsLoading = false
      }).catch(error => {
        console.error('获取未分配学生失败', error)
        this.batchStudentsLoading = false
        this.$message.error('获取未分配学生失败')
      })
    },
    
    // 批量学生选择变化
    handleBatchSelectionChange(val) {
      this.batchSelectedStudents = val
    },
    
    // 获取可用宿舍
    fetchAvailableDorms() {
      if (!this.batchDormType || !this.batchDormSex) {
        this.$message.warning('请选择宿舍类型和性别要求')
        return
      }
      
      this.batchDormsLoading = true
      axios.get('/api/admin/dorm/available', {
        params: {
          dormType: this.batchDormType,
          dormsex: this.batchDormSex,
          dormitory: this.batchDormBuilding || undefined
        }
      }).then(response => {
        const { data } = response.data
        // 计算每个宿舍的可用床位数
        this.availableDorms = data.map(dorm => ({
          ...dorm,
          availableCount: dorm.dormNum - (dorm.members ? dorm.members.length : 0)
        })).filter(dorm => dorm.availableCount > 0)
        this.batchDormsLoading = false
      }).catch(error => {
        console.error('获取可用宿舍失败', error)
        this.batchDormsLoading = false
        this.$message.warning('获取可用宿舍失败，将显示当前宿舍列表')
        
        // 使用宿舍列表中的数据作为备用
        this.availableDorms = this.dormList
          .filter(dorm => 
            (this.batchDormType ? dorm.dormType === this.batchDormType : true) && 
            (this.batchDormSex ? dorm.dormsex === this.batchDormSex : true) &&
            (this.batchDormBuilding ? dorm.dormitory === this.batchDormBuilding : true)
          )
          .map(dorm => ({
            ...dorm,
            availableCount: dorm.dormNum - (dorm.members ? dorm.members.length : 0)
          }))
          .filter(dorm => dorm.availableCount > 0)
      })
    },
    
    // 宿舍选择变化
    handleDormSelectionChange(val) {
      this.selectedDorms = val
    },
    
    // 执行批量分配
    executeBatchAssignment() {
      if (this.batchSelectedStudents.length === 0) {
        this.$message.warning('请选择要分配的学生')
        return
      }
      
      if (this.selectedDorms.length === 0) {
        this.$message.warning('请选择宿舍')
        return
      }
      
      // 检查学生性别与宿舍性别是否匹配
      const studentGender = this.batchSelectedStudents[0].gender
      const mismatchedDorms = this.selectedDorms.filter(dorm => dorm.dormsex !== studentGender)
      
      if (mismatchedDorms.length > 0) {
        this.$confirm(`存在性别不匹配的宿舍，是否继续？`, '警告', {
          confirmButtonText: '继续',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.performBatchAssignment()
        }).catch(() => {
          // 取消分配
        })
      } else {
        this.performBatchAssignment()
      }
    },
    
    // 执行实际的批量分配
    performBatchAssignment() {
      const totalAvailableBeds = this.selectedDorms.reduce((sum, dorm) => 
        sum + (dorm.dormNum - (dorm.members ? dorm.members.length : 0)), 0)
      
      if (this.batchSelectedStudents.length > totalAvailableBeds) {
        this.$message.warning(`选择的宿舍床位不足，最多可分配 ${totalAvailableBeds} 名学生`)
        return
      }
      
      // 根据分配方式进行不同的处理
      let assignments = []
      
      if (this.assignmentMethod === 'auto') {
        // 自动分配：尽量填满每个宿舍
        let studentIndex = 0
        const students = [...this.batchSelectedStudents]
        
        for (const dorm of this.selectedDorms) {
          const availableCount = dorm.dormNum - (dorm.members ? dorm.members.length : 0)
          const dormAssignments = []
          
          for (let i = 0; i < availableCount && studentIndex < students.length; i++) {
            dormAssignments.push({
              studentNumber: students[studentIndex].studentNumber,
              dormitory: dorm.dormitory,
              dormCard: dorm.dormCard,
              dormType: dorm.dormType
            })
            studentIndex++
          }
          
          assignments = [...assignments, ...dormAssignments]
          if (studentIndex >= students.length) break
        }
      } else {
        // 手动指定：每个宿舍平均分配
        const students = [...this.batchSelectedStudents]
        const dorms = [...this.selectedDorms]
        const avgPerDorm = Math.ceil(students.length / dorms.length)
        
        let studentIndex = 0
        for (const dorm of dorms) {
          const availableCount = Math.min(
            dorm.dormNum - (dorm.members ? dorm.members.length : 0),
            avgPerDorm
          )
          
          for (let i = 0; i < availableCount && studentIndex < students.length; i++) {
            assignments.push({
              studentNumber: students[studentIndex].studentNumber,
              dormitory: dorm.dormitory,
              dormCard: dorm.dormCard,
              dormType: dorm.dormType
            })
            studentIndex++
          }
          
          if (studentIndex >= students.length) break
        }
      }
      
      // 发送批量分配请求
      axios.post('/api/admin/dorm/batch-assign', { assignments })
        .then(() => {
          this.$message.success(`成功将 ${assignments.length} 名学生分配到宿舍`)
          this.batchAssignDialogVisible = false
          this.fetchDormList() // 刷新宿舍列表
        })
        .catch(error => {
          console.error('批量分配学生失败', error)
          this.$message.error('批量分配学生失败')
          // 如果后端未实现批量分配接口，使用单个分配接口
          this.fallbackSingleAssignments(assignments)
        })
    },
    
    // 备用方案：使用单个分配接口
    fallbackSingleAssignments(assignments) {
      this.$confirm('批量分配接口未实现，是否尝试使用单个分配接口逐个分配学生？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 使用Promise.all进行并行请求
        const requests = assignments.map(assignment => 
          axios.post('/api/admin/dorm/assign', {
            studentNumbers: [assignment.studentNumber],
            dormType: assignment.dormType,
            dormitory: assignment.dormitory,
            dormCard: assignment.dormCard
          })
        )
        
        Promise.allSettled(requests).then(results => {
          const successCount = results.filter(r => r.status === 'fulfilled').length
          const failCount = results.filter(r => r.status === 'rejected').length
          
          if (successCount > 0) {
            this.$message.success(`成功分配 ${successCount} 名学生`)
            this.batchAssignDialogVisible = false
            this.fetchDormList() // 刷新宿舍列表
          }
          
          if (failCount > 0) {
            this.$message.warning(`${failCount} 名学生分配失败`)
          }
        })
      }).catch(() => {
        // 用户取消
      })
    },
  },
  
  computed: {
    // 计算所选学生的性别统计
    studentGenderCount() {
      if (!this.batchSelectedStudents.length) return '';
      
      const maleCount = this.batchSelectedStudents.filter(s => s.gender === '男').length;
      const femaleCount = this.batchSelectedStudents.filter(s => s.gender === '女').length;
      
      return `男:${maleCount}, 女:${femaleCount}`;
    },
    
    // 计算所选宿舍的总可用床位
    totalAvailableBeds() {
      return this.selectedDorms.reduce((sum, dorm) => 
        sum + (dorm.dormNum - (dorm.members ? dorm.members.length : 0)), 0);
    }
  }
}
</script>

<style scoped>
.dorm-manage-container {
  padding: 20px;
}

.dorm-card {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.filter-container {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  flex-wrap: wrap;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dorm-info {
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: bold;
  display: flex;
  align-items: center;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.empty-members {
  padding: 30px 0;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
  background-color: #fafafa;
  border-radius: 4px;
}

.el-table {
  margin-bottom: 15px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 10px;
}

.action-buttons {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 5px;
}

/* 响应式调整 */
@media screen and (max-width: 768px) {
  .filter-container {
    flex-direction: column;
    align-items: stretch;
  }
  
  .dorm-info {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .dorm-info el-button {
    margin-left: 0;
    margin-top: 10px;
  }
}

.batch-assign-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.filter-section, .dorm-section {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 15px;
  background-color: #f9f9f9;
}

.filter-section h3, .dorm-section h3 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #303133;
  font-size: 16px;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 10px;
}

.filter-row {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.assignment-info {
  margin-top: 20px;
}

/* 响应式调整 */
@media screen and (max-width: 768px) {
  .filter-row {
    flex-direction: column;
    align-items: stretch;
  }
}
</style>