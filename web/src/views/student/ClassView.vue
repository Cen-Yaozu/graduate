<template>
  <div class="class-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold">专业信息</span>
        </div>
      </template>
      <el-table :data="classData" style="width: 100%">
        <el-table-column prop="department" label="专业号" width="120" />
        <el-table-column prop="majorname" label="专业名" width="180" />
        <el-table-column prop="departmentPrize" label="专业介绍">
          <template #default="scope">
            <el-popover
              placement="top-start"
              :width="400"
              trigger="hover"
              :content="scope.row.departmentPrize"
            >
              <template #reference>
                <div class="description-text">{{ scope.row.departmentPrize }}</div>
              </template>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <el-card class="box-card" style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold">班级详情</span>
        </div>
      </template>
      <el-table :data="classDetailData" style="width: 100%">
        <el-table-column prop="majorid" label="班级号" width="120" />
        <el-table-column prop="classroom" label="班级名称" width="180" />
        <el-table-column prop="steacherid" label="联系电话" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ClassView',
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
          this.classDetailData = response.data.data || [];
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
</style>