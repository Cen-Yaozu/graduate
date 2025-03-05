<template>
  <div class="information-container">
    <el-tabs v-model="activeTab" type="border-card">
      <!-- 基本信息标签页 -->
      <el-tab-pane label="基本信息" name="basic">
        <el-card class="info-card">
          <template #header>
            <div class="card-header">
              <span>个人基本信息</span>
            </div>
          </template>
          
          <el-form 
            :model="studentInfo" 
            :rules="basicRules" 
            ref="basicForm" 
            label-width="100px" 
            disabled
          >
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="学号" prop="studentNumber">
                  <el-input v-model="studentInfo.studentNumber" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="studentInfo.name" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="性别" prop="gender">
                  <el-radio-group v-model="studentInfo.gender" disabled>
                    <el-radio label="男">男</el-radio>
                    <el-radio label="女">女</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="出生日期" prop="birthDate">
                  <el-date-picker 
                    v-model="studentInfo.birthDate" 
                    type="date" 
                    placeholder="选择日期"
                    disabled
                  ></el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="手机号码" prop="phone">
                  <el-input v-model="studentInfo.phone"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="studentInfo.email"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="民族" prop="nation">
                  <el-input v-model="studentInfo.nation" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="政治面貌" prop="politicalStatus">
                  <el-select v-model="studentInfo.politicalStatus" placeholder="请选择政治面貌">
                    <el-option label="群众" value="群众"></el-option>
                    <el-option label="共青团员" value="共青团员"></el-option>
                    <el-option label="党员" value="党员"></el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="身份证号" prop="idCard">
                  <el-input v-model="studentInfo.idCard" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="籍贯" prop="hometown">
                  <el-input v-model="studentInfo.hometown" disabled></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item label="家庭住址" prop="address">
                  <el-input 
                    v-model="studentInfo.address" 
                    type="textarea" 
                    :rows="3"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-tab-pane>
      
      <!-- 简历信息标签页 -->
      <el-tab-pane label="简历信息" name="resume">
        <el-card class="info-card">
          <template #header>
            <div class="card-header">
              <span>教育经历</span>
            </div>
          </template>
          
          <el-form 
            :model="resumeInfo" 
            ref="resumeForm" 
            label-width="100px" 
            disabled
          >
            <div v-for="(edu, index) in resumeInfo.education" :key="index" class="education-item">
              <el-divider v-if="index > 0"></el-divider>
              
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="'学校名称'" :prop="'education.' + index + '.schoolName'">
                    <el-input v-model="edu.schoolName"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="'专业'" :prop="'education.' + index + '.major'">
                    <el-input v-model="edu.major"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="'开始时间'" :prop="'education.' + index + '.startDate'">
                    <el-date-picker 
                      v-model="edu.startDate" 
                      type="date" 
                      placeholder="选择日期"
                    ></el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="'结束时间'" :prop="'education.' + index + '.endDate'">
                    <el-date-picker 
                      v-model="edu.endDate" 
                      type="date" 
                      placeholder="选择日期"
                    ></el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-row>
                <el-col :span="24">
                  <el-form-item :label="'描述'" :prop="'education.' + index + '.description'">
                    <el-input 
                      v-model="edu.description" 
                      type="textarea" 
                      :rows="2"
                    ></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-form>
        </el-card>
      </el-tab-pane>
      
      <!-- 家庭信息标签页 -->
      <el-tab-pane label="家庭信息" name="family">
        <el-card class="info-card">
          <template #header>
            <div class="card-header">
              <span>家庭成员信息</span>
            </div>
          </template>
          
          <el-form 
            :model="familyInfo" 
            ref="familyForm" 
            label-width="100px" 
            disabled
          >
            <div v-for="(member, index) in familyInfo.members" :key="index" class="family-item">
              <el-divider v-if="index > 0"></el-divider>
              
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item :label="'姓名'" :prop="'members.' + index + '.name'">
                    <el-input v-model="member.name"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item :label="'关系'" :prop="'members.' + index + '.relation'">
                    <el-select v-model="member.relation" placeholder="请选择关系" style="width: 100%;">
                      <el-option label="父亲" value="父亲"></el-option>
                      <el-option label="母亲" value="母亲"></el-option>
                      <el-option label="兄弟" value="兄弟"></el-option>
                      <el-option label="姐妹" value="姐妹"></el-option>
                      <el-option label="其他" value="其他"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item :label="'年龄'" :prop="'members.' + index + '.age'">
                    <el-input v-model="member.age"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="'单位'" :prop="'members.' + index + '.workplace'">
                    <el-input v-model="member.workplace"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="'政治面貌'" :prop="'members.' + index + '.occupation'">
                    <el-input v-model="member.occupation"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  name: 'StudentSInformation',
  data() {
    return {
      activeTab: 'basic',
      studentInfo: {
        studentNumber: '',
        name: '',
        gender: '',
        birthDate: '',
        phone: '',
        email: '',
        nation: '',
        politicalStatus: '',
        idCard: '',
        hometown: '',
        address: ''
      },
      basicRules: {
        phone: [
          { required: true, message: '请输入手机号码', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入家庭住址', trigger: 'blur' }
        ]
      },
      resumeInfo: {
        education: [
          {
            schoolName: '',
            major: '',
            startDate: '',
            endDate: '',
            description: ''
          }
        ]
      },
      familyInfo: {
        members: [
          {
            name: '',
            relation: '',
            age: '',
            workplace: '',
            occupation: ''
          }
        ]
      }
    }
  },
  created() {
    this.getStudentInfo()
    this.getResumeInfo()
    this.getFamilyInfo()
  },
  methods: {
    getStudentInfo() {
      // 从会话存储中获取学号
      const studentName = window.sessionStorage.getItem('studentName')
      if (!studentName) {
        this.$message.error('未找到学号信息，请重新登录')
        return
      }
      
      // 设置学号
      this.studentInfo.studentNumber = studentName
      
      this.$http.get(`/api/student/info/${studentName}`)
        .then(response => {
          if (response.data.code === 200) {
            const data = response.data.data;
            // 映射后端字段到前端字段
            this.studentInfo = {
              studentNumber: data.studentNumber || studentName,
              name: data.studentName || '',
              gender: data.sex || '',
              birthDate: data.birth || '', // 使用birth字段
              phone: data.studentPhone || '',
              email: data.email || '',
              nation: data.nation || '',
              politicalStatus: data.politeAspect || '',
              idCard: data.idCard || '',
              hometown: data.hometown || '',
              address: data.address || ''
            };
          } else {
            this.$message.error(response.data.msg || '获取学生信息失败')
          }
        })
        .catch(error => {
          console.error('获取学生信息失败:', error)
          this.$message.error('获取学生信息失败，请稍后重试')
        })
    },
    getResumeInfo() {
      const studentName = window.sessionStorage.getItem('studentName')
      if (!studentName) return
      
      this.$http.get(`/api/student/resume/${studentName}`)
        .then(response => {
          if (response.data.code === 200 && response.data.data) {
            // 映射后端数据到前端数据结构
            const resumeData = response.data.data
            this.resumeInfo = {
              education: [{
                schoolName: resumeData.organization || '',
                major: '',
                startDate: resumeData.startTime || '',
                endDate: resumeData.endTime || '',
                description: resumeData.hats || ''
              }]
            }
          }
        })
        .catch(error => {
          console.error('获取简历信息失败:', error)
        })
    },
    getFamilyInfo() {
      const studentName = window.sessionStorage.getItem('studentName')
      if (!studentName) return
      
      this.$http.get(`/api/student/family/${studentName}`)
        .then(response => {
          console.log('后端返回的家庭成员数据:', response.data)
          if (response.data.code === 200 && response.data.data && response.data.data.length > 0) {
            // 映射后端数据到前端数据结构
            const familyData = response.data.data
            console.log('家庭成员详细数据:', familyData)
            this.familyInfo = {
              members: familyData.map(member => ({
                name: member.familyName || '',
                relation: member.familyPart || '',
                age: member.familyAge || '',
                workplace: member.familyOrganization || '',
                occupation: member.familyPoliteAspect || ''
              }))
            }
          } else {
            // 如果没有数据，设置一个默认的空家庭成员
            this.familyInfo = {
              members: [{
                name: '',
                relation: '',
                age: '',
                workplace: '',
                occupation: ''
              }]
            }
            console.log('未找到家庭成员信息或数据为空')
          }
        })
        .catch(error => {
          console.error('获取家庭信息失败:', error)
          // 发生错误时也设置一个默认的空家庭成员
          this.familyInfo = {
            members: [{
              name: '',
              relation: '',
              age: '',
              workplace: '',
              occupation: ''
            }]
          }
        })
    }
  }
}
</script>

<style scoped>
.information-container {
  padding: 20px;
}

.info-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text-right {
  text-align: right;
}

.text-center {
  text-align: center;
  margin: 20px 0;
}

.education-item, .family-item {
  margin-bottom: 30px;
}

/* 修复表单样式问题 */
.el-form-item {
  margin-bottom: 22px;
}

.el-input, .el-select {
  width: 100%;
}

.el-row {
  margin-bottom: 15px;
}

/* 确保所有字段正确显示 */
:deep(.el-form-item__label) {
  font-weight: bold;
  line-height: 20px;
  padding-bottom: 8px;
}

:deep(.el-input__inner) {
  width: 100%;
  height: 40px;
}
</style>