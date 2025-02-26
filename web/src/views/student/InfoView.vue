<template>
  <div class="info-container">
    <el-card class="info-card">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <el-button type="primary" @click="handleEdit" v-if="!isEditing">编辑</el-button>
          <div v-else>
            <el-button type="success" @click="handleSave">保存</el-button>
            <el-button @click="handleCancel">取消</el-button>
          </div>
        </div>
      </template>
      <el-form :model="studentInfo" :disabled="!isEditing" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学号">
              <el-input v-model="studentInfo.studentNumber" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名">
              <el-input v-model="studentInfo.name"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="studentInfo.gender" placeholder="请选择性别">
                <el-option label="男" value="male"></el-option>
                <el-option label="女" value="female"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期">
              <el-date-picker v-model="studentInfo.birthDate" type="date" placeholder="选择日期"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="studentInfo.phone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input v-model="studentInfo.email"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="家庭住址">
              <el-input v-model="studentInfo.address"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'

export default {
  name: 'InfoView',
  data() {
    return {
      isEditing: false,
      studentInfo: {
        studentNumber: '',
        name: '',
        gender: '',
        birthDate: '',
        phone: '',
        email: '',
        address: ''
      },
      originalInfo: null
    }
  },
  created() {
    this.fetchStudentInfo()
  },
  methods: {
    async fetchStudentInfo() {
      try {
        const studentNumber = window.sessionStorage.getItem('studentName');
        if (!studentNumber) {
          ElMessage.error('未找到学号信息');
          return;
        }
        
        const response = await this.$http.get(`/api/student/info/${studentNumber}`);
        // 处理数据
        this.studentInfo = response.data || {
          studentNumber: studentNumber,
          name: '',
          gender: '',
          birthDate: '',
          phone: '',
          email: '',
          address: ''
        };
      } catch (error) {
        console.error('获取学生信息失败:', error);
        ElMessage.error('获取学生信息失败');
      }
    },
    handleEdit() {
      this.isEditing = true
      this.originalInfo = JSON.parse(JSON.stringify(this.studentInfo))
    },
    async handleSave() {
      try {
        await this.$http.put('/api/student/info', this.studentInfo)
        ElMessage.success('保存成功')
        this.isEditing = false
      } catch (error) {
        ElMessage.error('保存失败')
      }
    },
    handleCancel() {
      this.studentInfo = JSON.parse(JSON.stringify(this.originalInfo))
      this.isEditing = false
    }
  }
}
</script>

<style scoped>
.info-container {
  padding: 20px;
}
.info-card {
  max-width: 1000px;
  margin: 0 auto;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.el-row {
  margin-bottom: 20px;
}
</style>