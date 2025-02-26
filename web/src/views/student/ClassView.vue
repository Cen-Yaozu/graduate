<template>
  <div class="class-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span style="font-weight: bold">专业信息</span>
          <el-input
            v-model="searchText"
            placeholder="搜索专业"
            style="width: 200px; margin-left: 20px"
            clearable
            @clear="handleSearch"
            @input="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </template>
      <el-table :data="filteredClassData" style="width: 100%">
        <el-table-column prop="majorId" label="专业号" width="120" />
        <el-table-column prop="majorName" label="专业名" width="180" />
        <el-table-column prop="majorDescription" label="专业介绍">
          <template #default="scope">
            <el-popover
              placement="top-start"
              :width="400"
              trigger="hover"
              :content="scope.row.majorDescription"
            >
              <template #reference>
                <div class="description-text">{{ scope.row.majorDescription }}</div>
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
import { Search } from '@element-plus/icons-vue'

export default {
  name: 'ClassView',
  components: {
    Search
  },
  data() {
    return {
      searchText: '',
      classData: [
        {
          majorId: '01',
          majorName: '计算机科学与技术',
          majorDescription: '计算机科学与技术专业培养掌握计算机科学与技术包括计算机硬件、软件与应用的基本理论、基本知识和基本技能与方法，能在科研部门、教育单位、企业、事业、技术和行政管理部门等单位从事计算机教学、科学研究和应用的计算机科学与技术学科的高级专门人才。'
        },
        {
          majorId: '02',
          majorName: '软件工程',
          majorDescription: '软件工程专业培养能从事软件开发、测试、维护和软件项目管理的专业人才，具备扎实的编程基础，熟悉软件开发流程，能够设计、实现和维护软件系统。'
        },
        {
          majorId: '03',
          majorName: '信息安全',
          majorDescription: '信息安全专业培养具备信息安全技术与管理能力的高级专门人才，掌握网络安全、密码学、系统安全等技术，能够从事信息安全相关工作。'
        },
        {
          majorId: '04',
          majorName: '人工智能',
          majorDescription: '人工智能专业培养具备扎实的数学基础和计算机科学知识，掌握机器学习、深度学习、自然语言处理等人工智能核心技术的专业人才。'
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
    };
  },
  computed: {
    filteredClassData() {
      if (!this.searchText) {
        return this.classData;
      }
      
      const searchLower = this.searchText.toLowerCase();
      return this.classData.filter(item => 
        item.majorId.includes(searchLower) ||
        item.majorName.toLowerCase().includes(searchLower) ||
        item.majorDescription.toLowerCase().includes(searchLower)
      );
    }
  },
  methods: {
    handleSearch() {
      // 搜索功能通过计算属性实现
    },
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