<template>
  <div class="class-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold">我的班级信息</span>
        </div>
      </template>
      
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
      
      <div v-else-if="!myClassInfo" class="empty-data">
        <el-empty description="暂无班级信息" />
      </div>
      
      <div v-else class="class-info">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="系别">{{ myClassInfo.department }}</el-descriptions-item>
          <el-descriptions-item label="专业">{{ myClassInfo.majorname }}</el-descriptions-item>
          <el-descriptions-item label="班级">{{ myClassInfo.classroom }}</el-descriptions-item>
          <el-descriptions-item label="辅导员">{{ myClassInfo.fteacherName }}</el-descriptions-item>
          <el-descriptions-item label="学习导师">{{ myClassInfo.steacherName }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'StudentClassView',
  data() {
    return {
      loading: true,
      myClassInfo: null,
      classData: []
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
    
    async fetchMyClassInfo() {
      this.loading = true;
      try {
        // 从sessionStorage获取当前学生学号
        const studentNumber = sessionStorage.getItem('studentNumber');
        if (!studentNumber) {
          this.$message.error('未找到学生信息，请重新登录');
          this.loading = false;
          return;
        }
        
        // 请求时添加学号参数
        const response = await this.$http.get(`/api/student/class/my-class?studentNumber=${studentNumber}`);
        console.log('我的班级信息返回数据:', response.data);
        if (response.data.code === 200) {
          this.myClassInfo = response.data.data;
          console.log('我的班级信息:', this.myClassInfo);
        } else {
          this.$message.error(response.data.msg || '获取班级信息失败');
        }
      } catch (error) {
        console.error('获取我的班级信息失败:', error);
        this.$message.error('获取班级信息失败: ' + (error.response?.data?.msg || error.message));
      } finally {
        this.loading = false;
      }
    }
  },
  mounted() {
    this.fetchMyClassInfo();
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

.loading-container {
  padding: 20px;
}

.empty-data {
  padding: 30px;
  display: flex;
  justify-content: center;
}

.class-info {
  padding: 10px;
}

/* 添加按钮样式 */
:deep(.el-button.add-button) {
  width: auto;
  min-width: 120px;
  max-width: 180px;
  margin-left: auto;
  margin-right: 20px;
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

/* 描述列表样式优化 */
:deep(.el-descriptions) {
  width: 100%;
}

:deep(.el-descriptions__label) {
  width: 120px;
  font-weight: bold;
  background-color: #f5f7fa;
}

:deep(.el-descriptions__content) {
  padding: 12px 10px;
}
</style>