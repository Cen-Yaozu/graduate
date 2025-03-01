<template>
  <div class="dorm-manage-container">
    <el-card class="dorm-card">
      <el-alert
        title="宿舍管理使用指南"
        type="info"
        :closable="false"
        show-icon
        style="margin-bottom: 20px;"
      >
        <p>1. 可以通过搜索框和筛选条件查找学生</p>
        <p>2. 点击"添加宿舍"按钮可以新增宿舍</p>
        <p>3. 为未分配宿舍的学生分配宿舍</p>
        <p>4. 为已分配宿舍的学生调整宿舍或移除宿舍分配</p>
      </el-alert>
      
      <div class="filter-container">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索学号、姓名或专业"
          clearable
          style="width: 220px;"
          @keyup.enter="handleSearch"
        />
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        <el-button type="info" icon="el-icon-refresh" @click="resetSearch">重置</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleAddDorm">添加宿舍</el-button>
        <el-button type="primary" icon="el-icon-s-grid" @click="openBatchAssignDialog">批量分配</el-button>
      </div>

      <!-- 分类标签页 -->
      <el-tabs v-model="activeTab" @tab-click="handleTabChange">
        <el-tab-pane label="全部学生" name="all"></el-tab-pane>
        <el-tab-pane label="已分配学生" name="assigned"></el-tab-pane>
        <el-tab-pane label="未分配学生" name="unassigned"></el-tab-pane>
      </el-tabs>

      <!-- 学生表格 -->
      <el-table
        v-loading="tableLoading"
        :data="studentList"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column
          prop="studentNumber"
          label="学号"
          min-width="120"
          align="center"
        />
        <el-table-column
          prop="studentName"
          label="姓名"
          min-width="100"
          align="center"
        />
        <el-table-column
          prop="sex"
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
          prop="dormitory"
          label="宿舍楼"
          min-width="100"
          align="center"
        />
        <el-table-column
          prop="dorm_number"
          label="宿舍号"
          min-width="100"
          align="center"
        />
        <el-table-column
          prop="selectDorm"
          label="宿舍类型选择"
          min-width="120"
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
                v-if="!hasAssignedDorm(scope.row)"
                size="mini"
                type="primary"
                @click="openAssignToStudentDialog(scope.row)"
              >
                分配宿舍
              </el-button>
              <el-button
                v-if="hasAssignedDorm(scope.row)"
                size="mini"
                type="warning"
                @click="openReassignDialog(scope.row)"
              >
                调整宿舍
              </el-button>
              <el-button
                v-if="hasAssignedDorm(scope.row)"
                size="mini"
                type="danger"
                @click="handleRemoveFromDorm(scope.row)"
              >
                移除宿舍
              </el-button>
              <el-button
                size="mini"
                type="success"
                @click="openSelectDormTypeDialog(scope.row)"
              >
                选择宿舍类型
              </el-button>
            </div>
          </template>
        </el-table-column>
        <template #empty>
          <div class="empty-table-tip">
            <el-empty 
              :description="getEmptyTableTip()" 
              :image-size="120"
            >
              <template #description>
                <p>{{ getEmptyTableTip() }}</p>
                <p class="tip-extra" v-if="activeTab !== 'all'">
                  <el-button type="text" @click="activeTab = 'all'">查看全部学生</el-button>
                </p>
              </template>
            </el-empty>
          </div>
        </template>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[10, 20, 50, 100, 200, 500]"
          :page-size="studentPageSize"
          :total="studentTotal"
          :current-page="studentCurrentPage"
          @size-change="handleStudentSizeChange"
          @current-change="handleStudentCurrentChange"
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
    
    <!-- 选择宿舍类型对话框 -->
    <el-dialog
      title="选择宿舍类型"
      v-model="selectDormTypeDialogVisible"
      width="400px"
      destroy-on-close
    >
      <div v-if="selectedStudent" class="student-info mb-3">
        <p><strong>学号:</strong> {{ selectedStudent.studentNumber }}</p>
        <p><strong>姓名:</strong> {{ selectedStudent.studentName }}</p>
        <p><strong>性别:</strong> {{ selectedStudent.sex }}</p>
      </div>
      
      <el-form :model="dormTypeForm" ref="dormTypeForm" label-width="100px">
        <el-form-item label="宿舍类型" prop="dormType">
          <el-select v-model="dormTypeForm.dormType" placeholder="请选择宿舍类型" style="width: 100%">
            <el-option label="四人间" value="四人间"></el-option>
            <el-option label="六人间" value="六人间"></el-option>
            <el-option label="八人间" value="八人间"></el-option>
            <el-option label="单人间" value="单人间"></el-option>
            <el-option label="双人间" value="双人间"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="selectDormTypeDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitSelectDormType">确 定</el-button>
        </div>
      </template>
    </el-dialog>
    
    <!-- 分配宿舍对话框 -->
    <el-dialog
      title="分配宿舍"
      v-model="assignDialogVisible"
      width="800px"
      destroy-on-close
    >
      <div v-if="selectedStudent" class="student-info mb-3">
        <div class="student-details">
          <p><strong>学号:</strong> {{ selectedStudent.studentNumber }}</p>
          <p><strong>姓名:</strong> {{ selectedStudent.studentName }}</p>
          <p><strong>性别:</strong> {{ selectedStudent.sex }}</p>
          <p v-if="selectedStudent.selectDorm"><strong>选择的宿舍类型:</strong> {{ selectedStudent.selectDorm }}</p>
        </div>
      </div>
      
      <div class="filter-container">
        <el-select
          v-model="assignDormType"
          placeholder="选择宿舍类型"
          clearable
          style="width: 150px; margin-right: 10px;"
          @change="fetchAvailableDorms"
        >
          <el-option label="四人间" value="四人间"></el-option>
          <el-option label="六人间" value="六人间"></el-option>
          <el-option label="八人间" value="八人间"></el-option>
          <el-option label="单人间" value="单人间"></el-option>
          <el-option label="双人间" value="双人间"></el-option>
        </el-select>
        
        <el-select
          v-model="assignDormBuilding"
          placeholder="选择宿舍楼"
          clearable
          style="width: 150px; margin-right: 10px;"
          @change="fetchAvailableDorms"
        >
          <el-option
            v-for="item in dormBuildingOptions"
            :key="item"
            :label="item"
            :value="item"
          />
        </el-select>
        
        <el-button type="primary" size="small" icon="el-icon-search" @click="fetchAvailableDorms">筛选宿舍</el-button>
      </div>
      
      <el-table
        v-loading="availableDormsLoading"
        :data="availableDormsList"
        border
        style="width: 100%; margin-top: 15px;"
        @selection-change="handleDormSelectionChange"
        height="350px"
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
      
      <template #footer>
        <div class="dialog-footer">
          <div style="float: left; color: #E6A23C; font-size: 13px;">
            <i class="el-icon-warning-outline"></i> 提示：请确保学生性别与宿舍要求匹配
          </div>
          <el-button @click="assignDialogVisible = false">取 消</el-button>
          <el-button 
            type="primary" 
            @click="assignStudentToDorm"
            :disabled="selectedDorms.length !== 1"
          >
            确认分配
          </el-button>
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
          prop="studentName"
          label="姓名"
          min-width="100"
          align="center"
        />
        <el-table-column
          prop="sex"
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
          prop="studentName"
          label="姓名"
          min-width="100"
          align="center"
        />
        <el-table-column
          prop="sex"
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
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[10, 20, 50, 100, 200, 500]"
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
      width="90%"
      destroy-on-close
      class="batch-assign-dialog"
    >
      <template #header>
        <div class="dialog-header">
          <h3>批量分配宿舍</h3>
          <el-alert
            title="提示：选择学生和可用宿舍后，点击确认分配即可完成批量分配"
            type="info"
            :closable="false"
            show-icon
            style="margin: 10px 0;"
          />
        </div>
      </template>
      
      <div class="batch-assign-container">
        <div class="students-section">
          <div class="section-header">
            <h3>学生选择</h3>
            <div class="filter-container">
              <el-select
                v-model="batchDepartmentFilter"
                placeholder="选择系别"
                clearable
                style="width: 150px; margin-right: 10px;"
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
                style="width: 180px; margin-right: 10px;"
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
                style="width: 150px; margin-right: 10px;"
                @change="handleBatchFilterChange"
              >
                <el-option
                  v-for="item in classOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
              
              <el-button type="primary" size="small" icon="el-icon-search" @click="fetchBatchUnassignedStudents">搜索</el-button>
            </div>
          </div>
          
          <el-table
            v-loading="batchStudentsLoading"
            :data="batchUnassignedStudents"
            border
            style="width: 100%; margin-top: 15px;"
            height="350px"
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
              prop="studentName"
              label="姓名"
              min-width="100"
              align="center"
            />
            <el-table-column
              prop="sex"
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
          </el-table>

          <div class="selection-info" v-if="batchSelectedStudents.length > 0">
            <span>已选择: <strong>{{ batchSelectedStudents.length }}</strong> 名学生 ({{ studentGenderCount }})</span>
          </div>
        </div>
        
        <div class="dorm-section">
          <div class="section-header">
            <h3>宿舍选择</h3>
            <div class="filter-container">
              <el-select
                v-model="batchDormType"
                placeholder="选择宿舍类型"
                style="width: 150px; margin-right: 10px;"
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
                style="width: 150px; margin-right: 10px;"
                @change="fetchAvailableDorms"
              >
                <el-option label="男" value="男"></el-option>
                <el-option label="女" value="女"></el-option>
              </el-select>
              
              <el-select
                v-model="batchDormBuilding"
                placeholder="选择宿舍楼"
                clearable
                style="width: 150px; margin-right: 10px;"
                @change="fetchAvailableDorms"
              >
                <el-option
                  v-for="item in dormBuildingOptions"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
              
              <el-button type="primary" size="small" icon="el-icon-search" @click="fetchAvailableDorms">筛选宿舍</el-button>
            </div>
          </div>
          
          <el-table
            v-loading="batchDormsLoading"
            :data="availableDorms"
            border
            style="width: 100%; margin-top: 15px;"
            height="350px"
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
            <template #empty>
              <div class="empty-dormitory">
                <el-empty description="暂无符合条件的宿舍" :image-size="100">
                  <template #description>
                    <p>暂无符合条件的宿舍，请更改筛选条件后再试</p>
                  </template>
                </el-empty>
              </div>
            </template>
          </el-table>

          <div class="selection-info" v-if="selectedDorms.length > 0">
            <span>已选宿舍: <strong>{{ selectedDorms.length }}</strong> 间，可用床位: <strong>{{ totalAvailableBeds }}</strong> 个</span>
          </div>
        </div>
      </div>
      
      <div class="assignment-summary" v-if="selectedDorms.length > 0 && batchSelectedStudents.length > 0">
        <el-alert
          :title="`分配信息：${batchSelectedStudents.length}名学生 → ${selectedDorms.length}间宿舍 (总床位：${totalAvailableBeds}个)`"
          type="success"
          :closable="false"
          show-icon
        />
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <div style="float: left; color: #E6A23C; font-size: 13px; margin-right: 20px;">
            <i class="el-icon-warning-outline"></i> 提示：有选择好宿舍号才算分配成功，请确保学生性别与宿舍要求匹配
          </div>
          
          <div style="display: flex; align-items: center; gap: 10px;">
            <span>分配方式：</span>
            <el-radio-group v-model="assignmentMethod" size="small">
              <el-radio label="auto">自动分配</el-radio>
              <el-radio label="manual">手动指定</el-radio>
            </el-radio-group>
          </div>
          
          <div style="flex-grow: 1;"></div>
          
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
      // 学生列表相关
      studentList: [],
      tableLoading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      studentCurrentPage: 1,
      studentPageSize: 10,
      studentTotal: 0,
      searchKeyword: '',
      
      // 宿舍相关
      dormList: [], // 保留用于其他功能
      dormStudentList: [], // 宿舍内学生列表
      currentDorm: null, // 当前选中的宿舍
      
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
      
      // 标签页
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
      
      // 选择宿舍类型相关
      selectDormTypeDialogVisible: false,
      selectedStudent: null,
      dormTypeForm: {
        dormType: ''
      },
      
      // 分配宿舍对话框相关
      assignDormType: '',
      assignDormBuilding: '',
      availableDormsList: [],
      availableDormsLoading: false,
      
      // 批量选择宿舍类型相关
      batchSelectDormDialogVisible: false,
      batchDormTypeSelection: ''
    }
  },
  created() {
    // 初始化分页变量
    this.studentCurrentPage = 1
    this.studentPageSize = 10
    
    this.fetchStudentList()
    this.fetchDormBuildings()
    this.fetchFilterOptions()
    // 使用防抖处理的搜索方法
    this.debouncedFilterChange = debounce(this.fetchStudentList, 300)
  },
  methods: {
    // 判断学生是否已经分配宿舍
    hasAssignedDorm(student) {
      return student && student.dormitory && student.dormitory !== '' && 
             student.dorm_number && student.dorm_number !== '';
    },
    
    // 获取学生列表
    fetchStudentList() {
      this.tableLoading = true
      
      // 根据标签页状态准备参数
      const params = {
        page: this.studentCurrentPage,
        size: this.studentPageSize // 支持10、20、50、100、200、500等不同的每页显示数量
      };
      
      // 只有在搜索框有内容时才添加关键字
      if (this.searchKeyword && this.searchKeyword.trim().length > 0) {
        params.keyword = this.searchKeyword;
      }
      
      // 设置API路径
      let apiPath = '/api/admin/dorm/students';
      
      if (this.activeTab === 'assigned') {
        apiPath = '/api/admin/dorm/assigned-students';
      } else if (this.activeTab === 'unassigned') {
        apiPath = '/api/admin/dorm/unassigned-students';
      }
      
      console.log('发送学生列表请求参数:', params);
      
      axios.get(apiPath, { params })
        .then(response => {
          console.log('学生列表响应:', response.data)
          const { data } = response.data
          if (data && data.list) {
            this.studentList = data.list;
            this.total = data.total;
            this.studentTotal = data.total;
            console.log('处理后的学生列表:', this.studentList, '总数:', this.studentTotal);
          } else {
            this.studentList = []
            this.total = 0
            this.studentTotal = 0
            this.$message.info('暂无学生数据')
          }
          this.tableLoading = false
        })
        .catch(error => {
          console.error('获取学生列表失败', error)
          this.tableLoading = false
          this.$message.error('获取学生列表失败')
        })
    },
    
    // 搜索
    handleSearch() {
      console.log('执行搜索，关键字:', this.searchKeyword);
      this.studentCurrentPage = 1;
      this.fetchStudentList();
    },
    
    // 重置搜索
    resetSearch() {
      this.searchKeyword = '';
      this.studentCurrentPage = 1;
      this.fetchStudentList();
    },
    
    // 分页
    handleSizeChange(val) {
      this.studentPageSize = val
      this.fetchStudentList()
    },
    
    handleCurrentChange(val) {
      this.studentCurrentPage = val
      this.fetchStudentList()
    },
    
    // 学生表格分页
    handleStudentSizeChange(val) {
      this.studentPageSize = val
      this.fetchStudentList()
    },
    
    handleStudentCurrentChange(val) {
      this.studentCurrentPage = val
      this.fetchStudentList()
    },
    
    // 处理标签页切换
    handleTabChange() {
      console.log('切换到标签页:', this.activeTab);
      this.studentCurrentPage = 1;
      this.fetchStudentList();
    },
    
    // 打开选择宿舍类型对话框
    openSelectDormTypeDialog(student) {
      this.selectedStudent = student;
      this.dormTypeForm.dormType = student.selectDorm || '';
      this.selectDormTypeDialogVisible = true;
    },
    
    // 提交宿舍类型选择
    submitSelectDormType() {
      if (!this.dormTypeForm.dormType) {
        this.$message.warning('请选择宿舍类型');
        return;
      }
      
      const studentNumber = this.selectedStudent.studentNumber;
      const selectDorm = this.dormTypeForm.dormType;
      
      axios.post('/api/admin/dorm/update-selectdorm', {
        studentNumber,
        selectDorm
      })
        .then(() => {
          this.$message.success('宿舍类型选择已更新');
          this.selectDormTypeDialogVisible = false;
          
          // 更新学生列表中的数据
          const index = this.studentList.findIndex(s => s.studentNumber === studentNumber);
          if (index !== -1) {
            this.studentList[index].selectDorm = selectDorm;
          }
        })
        .catch(error => {
          console.error('更新宿舍类型选择失败', error);
          this.$message.error('更新宿舍类型选择失败');
        });
    },
    
    // 打开分配宿舍对话框
    openAssignToStudentDialog(student) {
      this.selectedStudent = student;
      this.assignDialogVisible = true;
      this.assignDormType = student.selectDorm || '';
      this.assignDormBuilding = '';
      this.selectedDorms = [];
      this.fetchAvailableDorms();
    },
    
    // 打开重新分配宿舍对话框
    openReassignDialog(student) {
      this.selectedStudent = student;
      this.assignDialogVisible = true;
      this.assignDormType = student.selectDorm || '';
      this.assignDormBuilding = '';
      this.selectedDorms = [];
      this.fetchAvailableDorms();
    },
    
    // 获取可用宿舍
    fetchAvailableDorms() {
      if (!this.selectedStudent) {
        return;
      }
      
      this.availableDormsLoading = true;
      this.availableDormsList = [];
      
      const params = {
        dormType: this.assignDormType || undefined,
        dormsex: this.selectedStudent.sex,
        dormitory: this.assignDormBuilding || undefined
      };
      
      axios.get('/api/admin/dorm/available', { params })
        .then(response => {
          console.log('可用宿舍响应:', response.data)
          const { data } = response.data
          
          if (data) {
            // 处理不同的数据结构
            let dormData = Array.isArray(data) ? data : 
                          (data.list ? data.list : []);
            
            // 计算可用床位
            this.availableDormsList = dormData.map(dorm => {
              const members = dorm.members && Array.isArray(dorm.members) ? dorm.members : [];
              return {
                ...dorm,
                availableCount: dorm.dormNum - members.length
              };
            }).filter(dorm => dorm.availableCount > 0);
            
            console.log('处理后的可用宿舍:', this.availableDormsList);
          }
          
          this.availableDormsLoading = false;
        })
        .catch(error => {
          console.error('获取可用宿舍失败', error);
          this.availableDormsLoading = false;
          this.$message.error('获取可用宿舍失败');
        });
    },
    
    // 宿舍选择变化
    handleDormSelectionChange(selection) {
      this.selectedDorms = selection;
    },
    
    // 分配学生到宿舍
    assignStudentToDorm() {
      if (this.selectedDorms.length !== 1) {
        this.$message.warning('请选择一个宿舍');
        return;
      }
      
      const dorm = this.selectedDorms[0];
      
      // 检查性别匹配
      if (this.selectedStudent.sex !== dorm.dormsex) {
        this.$confirm(`学生性别(${this.selectedStudent.sex})与宿舍要求(${dorm.dormsex})不匹配，是否继续？`, '警告', {
          confirmButtonText: '继续',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.submitDormAssignment(dorm);
        }).catch(() => {
          // 取消分配
        });
      } else {
        this.submitDormAssignment(dorm);
      }
    },
    
    // 提交宿舍分配
    submitDormAssignment(dorm) {
      axios.post('/api/admin/dorm/assign', {
        studentNumbers: [this.selectedStudent.studentNumber],
        dormType: dorm.dormType,
        dormitory: dorm.dormitory,
        dormCard: dorm.dormCard
      })
        .then(() => {
          this.$message.success('学生已成功分配到宿舍');
          this.assignDialogVisible = false;
          
          // 更新学生数据
          const index = this.studentList.findIndex(s => s.studentNumber === this.selectedStudent.studentNumber);
          if (index !== -1) {
            this.studentList[index].dormitory = dorm.dormitory;
            this.studentList[index].dorm_number = dorm.dormCard;
          }
          
          // 如果是在"未分配"标签页，刷新列表
          if (this.activeTab === 'unassigned') {
            this.fetchStudentList();
          }
        })
        .catch(error => {
          console.error('分配学生失败', error);
          this.$message.error('分配学生失败');
        });
    },
    
    // 从宿舍移除学生
    handleRemoveFromDorm(student) {
      this.$confirm(`确定要将学生 ${student.studentName} 从宿舍中移除吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.post('/api/admin/dorm/remove-student', {
          studentNumber: student.studentNumber
        })
          .then(() => {
            this.$message.success('学生已从宿舍移除');
            
            // 更新学生数据
            const index = this.studentList.findIndex(s => s.studentNumber === student.studentNumber);
            if (index !== -1) {
              this.studentList[index].dormitory = '';
              this.studentList[index].dorm_number = '';
            }
            
            // 如果是在"已分配"标签页，刷新列表
            if (this.activeTab === 'assigned') {
              this.fetchStudentList();
            }
          })
          .catch(error => {
            console.error('移除学生失败', error);
            this.$message.error('移除学生失败');
          });
      }).catch(() => {
        // 取消移除
      });
    },
    
    // 获取宿舍楼选项
    fetchDormBuildings() {
      axios.get('/api/admin/dorm/buildings')
        .then(response => {
          console.log('宿舍楼响应:', response.data);
          if (response.data.code === 200 && response.data.data) {
            if (Array.isArray(response.data.data)) {
              this.dormBuildingOptions = response.data.data;
            } else if (Array.isArray(response.data.data.list)) {
              this.dormBuildingOptions = response.data.data.list.map(item => item.dormitory);
            } else {
              // 尝试查找数组字段
              const possibleArrays = ['buildings', 'dormitories', 'list'];
              for (const field of possibleArrays) {
                if (Array.isArray(response.data.data[field])) {
                  this.dormBuildingOptions = response.data.data[field];
                  break;
                }
              }
            }
          }
          
          if (!this.dormBuildingOptions || this.dormBuildingOptions.length === 0) {
            this.fetchDormBuildingsFallback();
          }
        })
        .catch(() => {
          this.fetchDormBuildingsFallback();
        });
    },
    
    // 获取宿舍楼备选方案
    fetchDormBuildingsFallback() {
      // 从宿舍列表提取或使用默认值
      axios.get('/api/admin/dorm/list', {
        params: { page: 1, size: 100 }
      }).then(response => {
        if (response.data.code === 200 && response.data.data && response.data.data.list) {
          const buildingSet = new Set();
          response.data.data.list.forEach(dorm => {
            if (dorm.dormitory) {
              buildingSet.add(dorm.dormitory);
            }
          });
          this.dormBuildingOptions = Array.from(buildingSet);
        } else {
          this.dormBuildingOptions = ['1号楼', '2号楼', '3号楼', '4号楼', '5号楼'];
        }
      }).catch(() => {
        this.dormBuildingOptions = ['1号楼', '2号楼', '3号楼', '4号楼', '5号楼'];
      });
    },
    
    // 获取筛选选项
    fetchFilterOptions() {
      axios.get('/api/admin/dorm/filter-options')
        .then(response => {
          const { data } = response.data
          this.departmentOptions = data.departments || []
          this.majorOptions = data.majors || []
          this.classOptions = data.classrooms || []
        })
        .catch(() => {
          // 提供默认选项
          this.departmentOptions = ['计算机系', '机械系', '电子系']
          this.majorOptions = ['软件工程', '计算机科学与技术', '人工智能']
          this.classOptions = [
            { id: '1', name: '计算机2101' },
            { id: '2', name: '计算机2102' },
            { id: '3', name: '软件2101' }
          ]
        })
    },
    
    // 根据当前标签页状态返回空表格提示
    getEmptyTableTip() {
      if (this.searchKeyword) {
        return `没有找到匹配"${this.searchKeyword}"的学生`;
      }
      
      switch (this.activeTab) {
        case 'assigned':
          return '暂无已分配宿舍的学生';
        case 'unassigned':
          return '所有学生均已分配宿舍';
        default:
          return '暂无学生数据';
      }
    },
    
    // 保留原有的宿舍管理功能
    
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
              // 添加或编辑宿舍后重新获取可用宿舍
              if (this.assignDialogVisible) {
                this.fetchAvailableDorms()
              }
            })
            .catch(error => {
              console.error(isEdit ? '更新宿舍失败' : '添加宿舍失败', error)
              this.$message.error(isEdit ? '更新宿舍失败' : '添加宿舍失败')
            })
        }
      })
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
        console.log('批量未分配学生响应:', response.data)
        const { data } = response.data
        if (data && data.list) {
          this.batchUnassignedStudents = data.list
        } else {
          this.batchUnassignedStudents = []
          this.$message.info('暂无未分配学生')
        }
        this.batchStudentsLoading = false
      }).catch(error => {
        console.error('获取未分配学生失败', error)
        this.batchStudentsLoading = false
        this.$message.error('获取未分配学生失败')
      })
    },
    
    // 批量筛选变化
    handleBatchFilterChange() {
      this.batchSelectedStudents = []
      this.fetchBatchUnassignedStudents()
    },
    
    // 批量学生选择变化
    handleBatchSelectionChange(val) {
      this.batchSelectedStudents = val
    },
    
    // 获取宿舍内学生列表
    fetchDormStudentList() {
      if (!this.currentDorm) return;
      
      this.studentsLoading = true;
      const params = {
        page: this.studentCurrentPage,
        size: this.studentPageSize,
        dormitory: this.currentDorm.dormitory,
        dormCard: this.currentDorm.dormCard
      };
      
      axios.get('/api/admin/dorm/assigned-students', { params })
        .then(response => {
          const { data } = response.data;
          if (data && data.list) {
            this.dormStudentList = data.list;
            this.studentTotal = data.total;
          } else {
            this.dormStudentList = [];
            this.studentTotal = 0;
            this.$message.info('宿舍内暂无学生');
          }
          this.studentsLoading = false;
        })
        .catch(error => {
          console.error('获取宿舍学生失败', error);
          this.studentsLoading = false;
          this.$message.error('获取宿舍学生失败');
        });
    },
  },
  
  computed: {
    // 计算所选学生的性别统计
    studentGenderCount() {
      if (!this.batchSelectedStudents.length) return '';
      
      const maleCount = this.batchSelectedStudents.filter(s => s.sex === '男').length;
      const femaleCount = this.batchSelectedStudents.filter(s => s.sex === '女').length;
      
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

.student-info {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  border-left: 3px solid #409EFF;
}

.student-details {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 10px;
}

.student-details p {
  margin: 5px 0;
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

.empty-dormitory {
  padding: 30px 0;
  height: 200px;
  background-color: #fafafa;
  border-radius: 4px;
}

.empty-table-tip {
  padding: 40px 0;
  background-color: #fafafa;
}

.tip-extra {
  margin-top: 10px;
  color: #909399;
  font-size: 14px;
}

/* 标签页样式增强 */
:deep(.el-tabs__item) {
  position: relative;
  padding: 0 20px;
  height: 40px;
  font-size: 15px;
}

:deep(.el-tabs__item.is-active) {
  font-weight: bold;
}

:deep(.el-tabs__active-bar) {
  height: 3px;
  border-radius: 3px;
}

.el-table {
  margin-bottom: 15px;
}

.dialog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.action-buttons {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 5px;
}

.action-buttons .el-button {
  margin: 2px !important;
}

/* 批量分配对话框样式 */
.batch-assign-dialog :deep(.el-dialog__body) {
  padding: 10px 20px;
}

.batch-assign-container {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.students-section, 
.dorm-section {
  flex: 1;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
  background-color: #f9f9f9;
}

.section-header {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}

.section-header h3 {
  margin: 0 0 15px 0;
  color: #303133;
  font-size: 16px;
  border-left: 3px solid #409EFF;
  padding-left: 10px;
}

.section-header .filter-container {
  padding: 10px;
  margin-bottom: 0;
}

.assignment-summary {
  margin: 15px 0;
}

.selection-info {
  margin-top: 10px;
  padding: 8px 12px;
  background-color: #f0f9eb;
  border-radius: 4px;
  color: #67c23a;
  font-size: 14px;
}

.mb-3 {
  margin-bottom: 15px;
}

/* 响应式调整 */
@media screen and (max-width: 1200px) {
  .batch-assign-container {
    flex-direction: column;
  }
  
  .students-section, 
  .dorm-section {
    width: 100%;
  }
}

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
  
  .action-buttons {
    flex-direction: column;
    width: 100%;
  }
  
  .action-buttons .el-button {
    margin: 3px 0 !important;
    width: 100%;
  }
  
  .dialog-footer {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
  }
}
</style>