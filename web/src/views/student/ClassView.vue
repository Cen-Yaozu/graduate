<template>
  <div class="class-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold">班级信息</span>
        </div>
      </template>
      <el-table :data="classData" style="width: 100%">
        <el-table-column prop="majorId" label="专业号" width="180" />
        <el-table-column prop="majorName" label="专业名" width="180" />
        <el-table-column prop="classCount" label="班级数量" />
      </el-table>
    </el-card>
    
    <el-card class="box-card" style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold">班级详情</span>
        </div>
      </template>
      <el-table :data="classDetailData" style="width: 100%">
        <el-table-column prop="classId" label="班级号" width="120" />
        <el-table-column prop="className" label="班级名称" width="180" />
        <el-table-column prop="studentCount" label="学生人数" width="120" />
        <el-table-column prop="headTeacher" label="班主任" width="120" />
        <el-table-column prop="contactPhone" label="联系电话" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ClassView',
  data() {
    return {
      classData: [
        {
          majorId: '1',
          majorName: '计算机科学与技术',
          classCount: 3
        },
        {
          majorId: '2',
          majorName: '软件工程',
          classCount: 2
        }
      ],
      classDetailData: [
        {
          classId: '101',
          className: '计算机科学与技术1班',
          studentCount: 30,
          headTeacher: '张老师',
          contactPhone: '13800138001'
        },
        {
          classId: '102',
          className: '计算机科学与技术2班',
          studentCount: 32,
          headTeacher: '李老师',
          contactPhone: '13800138002'
        },
        {
          classId: '103',
          className: '计算机科学与技术3班',
          studentCount: 31,
          headTeacher: '王老师',
          contactPhone: '13800138003'
        },
        {
          classId: '201',
          className: '软件工程1班',
          studentCount: 35,
          headTeacher: '赵老师',
          contactPhone: '13800138004'
        },
        {
          classId: '202',
          className: '软件工程2班',
          studentCount: 33,
          headTeacher: '钱老师',
          contactPhone: '13800138005'
        }
      ]
    }
  },
  methods: {
    async fetchClassInfo() {
      try {
        const studentNumber = window.sessionStorage.getItem('studentName');
        if (!studentNumber) return;
        
        const response = await this.$http.get(`/api/student/class/info/${studentNumber}`);
        if (response.data.code === 200) {
          // 处理返回的数据
          // this.classData = ...
          // this.classDetailData = ...
        }
      } catch (error) {
        console.error('获取班级信息失败:', error);
        this.$message.error('获取班级信息失败');
      }
    }
  },
  mounted() {
    this.fetchClassInfo();
  }
}
</script>

<style scoped>
.class-container {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>