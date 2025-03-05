<template>
  <div class="class-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold">专业信息</span>
        </div>
      </template>
      <el-table :data="classData" style="width: 100%" border stripe :cell-style="{padding: '12px 0'}">
        <el-table-column prop="majorid" label="专业号" width="80" align="center" />
        <el-table-column prop="majorname" label="专业名" min-width="180" align="center" />
        <el-table-column prop="department" label="系别" min-width="180" align="center" />
        <el-table-column prop="departmentPrize" label="学费" width="80" align="center" />
      </el-table>
    </el-card>
    
    <el-card class="box-card" style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold">班级详情</span>
        </div>
      </template>
      <el-table :data="classDetailData" style="width: 100%" border stripe :cell-style="{padding: '12px 0'}">
        <el-table-column prop="majorid" label="专业号" width="80" align="center" />
        <el-table-column prop="classroom" label="班级" width="80" align="center" />
        <el-table-column prop="fteacherid" label="辅导员" width="80" align="center" />
        <el-table-column prop="steacherid" label="学习导师" width="80" align="center" />
        <el-table-column prop="department" label="系别" min-width="150" align="center" />
        <el-table-column prop="studentCount" label="班级人数" width="80" align="center" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'StudentClassView',
  data() {
    return {
      classData: [],
      classDetailData: []
    };
  },
  methods: {
    async fetchMajorInfo() {
      try {
        const response = await this.$http.get('/api/student/class/majors');
        console.log('专业信息返回数据:', response.data);
        if (response.data.code === 200) {
          this.classData = response.data.data || [];
          // 处理可能的字段命名问题
          this.classData = this.classData.map(item => {
            // 学费格式化
            if (item.departmentPrize) {
              item.departmentPrize = Number(item.departmentPrize).toFixed(0);
            }
            return item;
          });
          console.log('处理后的专业信息数据:', this.classData);
        }
      } catch (error) {
        console.error('获取专业信息失败:', error);
        this.$message.error('获取专业信息失败');
      }
    },
    async fetchClassInfo() {
      try {
        const response = await this.$http.get('/api/student/class/classes');
        console.log('班级信息返回数据:', response.data);
        if (response.data.code === 200) {
          // 确保正确映射字段
          this.classDetailData = (response.data.data || []).map(item => {
            // 处理返回的每个班级数据，确保字段正确映射
            return {
              majorid: item.majorid || '',
              classroom: item.classroom || '',
              // 使用教师姓名代替教师ID
              fteacherid: item.fteacherName || item.fteacherid || '',
              steacherid: item.steacherName || item.steacherid || '',
              department: item.department || '',
              studentCount: item.classroomNum || item.studentCount || item.student_count || 0
            };
          });
          console.log('处理后的班级信息数据:', this.classDetailData);
        }
      } catch (error) {
        console.error('获取班级信息失败:', error);
        this.$message.error('获取班级信息失败');
      }
    }
  },
  mounted() {
    this.fetchMajorInfo();
    this.fetchClassInfo();
  }
};
</script>

<style scoped>
.class-container {
  padding: 20px;
}

.card-header {
  display: flex;
  align-items: center;
}

.description-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 400px;
  cursor: pointer;
}

/* 表格样式优化 */
:deep(.el-table) {
  --el-table-header-bg-color: #f5f7fa;
  width: auto !important;
  max-width: 100%;
  margin: 0 auto;
}

:deep(.el-table th) {
  font-weight: bold;
  color: #303133;
  background: var(--el-table-header-bg-color);
  padding: 12px 0;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background-color: #fafafa;
}

:deep(.el-table__row) {
  height: 48px;
}

/* 确保内容在单元格中居中对齐 */
:deep(.el-table .cell) {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: center;
}

/* 修复固定列的样式 */
:deep(.el-table__fixed) {
  height: 100%;
  position: absolute;
  top: 0;
  overflow: hidden;
  box-shadow: none;
}
</style>