<template>
  <div class="information-container">
    <el-tabs v-model="activeTab" type="border-card">
      <!-- 基本信息标签页 -->
      <el-tab-pane label="基本信息" name="basic">
        <el-card class="info-card">
          <template #header>
            <div class="card-header">
              <span>个人基本信息</span>
              <el-button type="primary" @click="handleEdit('basicForm')" v-if="!isEditing">编辑</el-button>
              <div v-else>
                <el-button type="success" @click="handleSave('basicForm')">保存</el-button>
                <el-button @click="handleCancel('basicForm')">取消</el-button>
              </div>
            </div>
          </template>
          
          <el-form 
            :model="studentInfo" 
            :rules="basicRules" 
            ref="basicForm" 
            label-width="100px" 
            :disabled="!isEditing"
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
              <el-button type="primary" @click="handleEdit('resumeForm')" v-if="!isEditingResume">编辑</el-button>
              <div v-else>
                <el-button type="success" @click="handleSave('resumeForm')">保存</el-button>
                <el-button @click="handleCancel('resumeForm')">取消</el-button>
              </div>
            </div>
          </template>
          
          <el-form 
            :model="resumeInfo" 
            ref="resumeForm" 
            label-width="100px" 
            :disabled="!isEditingResume"
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
              
              <el-row v-if="isEditingResume">
                <el-col :span="24" class="text-right">
                  <el-button 
                    type="danger" 
                    size="small" 
                    @click="removeEducation(index)" 
                    v-if="resumeInfo.education.length > 1"
                  >
                    删除
                  </el-button>
                </el-col>
              </el-row>
            </div>
            
            <el-row v-if="isEditingResume">
              <el-col :span="24" class="text-center">
                <el-button type="primary" @click="addEducation">添加教育经历</el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-tab-pane>
      
      <!-- 家庭信息标签页 -->
      <el-tab-pane label="家庭信息" name="family">
        <el-card class="info-card">
          <template #header>
            <div class="card-header">
              <span>家庭成员信息</span>
              <el-button type="primary" @click="handleEdit('familyForm')" v-if="!isEditingFamily">编辑</el-button>
              <div v-else>
                <el-button type="success" @click="handleSave('familyForm')">保存</el-button>
                <el-button @click="handleCancel('familyForm')">取消</el-button>
              </div>
            </div>
          </template>
          
          <el-form 
            :model="familyInfo" 
            ref="familyForm" 
            label-width="100px" 
            :disabled="!isEditingFamily"
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
                    <el-select v-model="member.relation" placeholder="请选择关系">
                      <el-option label="父亲" value="父亲"></el-option>
                      <el-option label="母亲" value="母亲"></el-option>
                      <el-option label="兄弟" value="兄弟"></el-option>
                      <el-option label="姐妹" value="姐妹"></el-option>
                      <el-option label="其他" value="其他"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item :label="'联系电话'" :prop="'members.' + index + '.phone'">
                    <el-input v-model="member.phone"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item :label="'工作单位'" :prop="'members.' + index + '.workplace'">
                    <el-input v-model="member.workplace"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="'职业'" :prop="'members.' + index + '.occupation'">
                    <el-input v-model="member.occupation"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              
              <el-row v-if="isEditingFamily">
                <el-col :span="24" class="text-right">
                  <el-button 
                    type="danger" 
                    size="small" 
                    @click="removeFamilyMember(index)" 
                    v-if="familyInfo.members.length > 1"
                  >
                    删除
                  </el-button>
                </el-col>
              </el-row>
            </div>
            
            <el-row v-if="isEditingFamily">
              <el-col :span="24" class="text-center">
                <el-button type="primary" @click="addFamilyMember">添加家庭成员</el-button>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  name: 'SInformation',
  data() {
    return {
      activeTab: 'basic',
      isEditing: false,
      isEditingResume: false,
      isEditingFamily: false,
      originalBasicInfo: null,
      originalResumeInfo: null,
      originalFamilyInfo: null,
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
            phone: '',
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
            // 保存原始数据，用于取消编辑时恢复
            this.originalResumeInfo = JSON.parse(JSON.stringify(this.resumeInfo))
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
          if (response.data.code === 200 && response.data.data) {
            // 映射后端数据到前端数据结构
            const familyData = response.data.data
            this.familyInfo = {
              members: familyData.map(member => ({
                name: member.familyName || '',
                relation: member.familyPart || '',
                phone: '',
                workplace: member.familyOrganization || '',
                occupation: member.familyPoliteAspect || ''
              }))
            }
            
            // 如果没有家庭成员，添加一个空的
            if (this.familyInfo.members.length === 0) {
              this.familyInfo.members.push({
                name: '',
                relation: '',
                phone: '',
                workplace: '',
                occupation: ''
              })
            }
            
            // 保存原始数据，用于取消编辑时恢复
            this.originalFamilyInfo = JSON.parse(JSON.stringify(this.familyInfo))
          }
        })
        .catch(error => {
          console.error('获取家庭信息失败:', error)
        })
    },
    handleEdit(formName) {
      if (formName === 'basicForm') {
        this.isEditing = true
        this.originalBasicInfo = JSON.parse(JSON.stringify(this.studentInfo))
      } else if (formName === 'resumeForm') {
        this.isEditingResume = true
        this.originalResumeInfo = JSON.parse(JSON.stringify(this.resumeInfo))
      } else if (formName === 'familyForm') {
        this.isEditingFamily = true
        this.originalFamilyInfo = JSON.parse(JSON.stringify(this.familyInfo))
      }
    },
    handleCancel(formName) {
      if (formName === 'basicForm') {
        this.studentInfo = JSON.parse(JSON.stringify(this.originalBasicInfo))
        this.isEditing = false
      } else if (formName === 'resumeForm') {
        this.resumeInfo = JSON.parse(JSON.stringify(this.originalResumeInfo))
        this.isEditingResume = false
      } else if (formName === 'familyForm') {
        this.familyInfo = JSON.parse(JSON.stringify(this.originalFamilyInfo))
        this.isEditingFamily = false
      }
    },
    handleSave(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (formName === 'basicForm') {
            this.saveBasicInfo()
          } else if (formName === 'resumeForm') {
            this.saveResumeInfo()
          } else if (formName === 'familyForm') {
            this.saveFamilyInfo()
          }
        } else {
          return false
        }
      })
    },
    saveBasicInfo() {
      // 构建提交到后端的数据，使用与后端匹配的字段名
      const submitData = {
        studentNumber: this.studentInfo.studentNumber,
        studentName: this.studentInfo.name,
        sex: this.studentInfo.gender,
        birth: this.studentInfo.birthDate, // 使用birth字段
        studentPhone: this.studentInfo.phone,
        email: this.studentInfo.email,
        politeAspect: this.studentInfo.politicalStatus,
        address: this.studentInfo.address
      }
      
      this.$http.put('/api/student/update', submitData)
        .then(response => {
          if (response.data.code === 200) {
            this.$message.success('保存成功')
            this.isEditing = false
            this.getStudentInfo() // 刷新数据
          } else {
            this.$message.error(response.data.msg || '保存失败')
          }
        })
        .catch(error => {
          console.error('保存失败:', error)
          this.$message.error('保存失败，请稍后重试')
        })
    },
    saveResumeInfo() {
      // 构建提交到后端的数据，使用正确的字段名
      const resumeData = {
        studentNumber: this.studentInfo.studentNumber,
        startTime: this.resumeInfo.education[0].startDate,  // 使用 startTime 而不是 start_time
        endTime: this.resumeInfo.education[0].endDate,      // 使用 endTime 而不是 end_time
        organization: this.resumeInfo.education[0].schoolName,
        hats: this.resumeInfo.education[0].description
      }
      
      this.$http.put('/api/student/resume/update', resumeData)
        .then(response => {
          if (response.data.code === 200) {
            this.$message.success('保存成功')
            this.isEditingResume = false
          } else {
            this.$message.error(response.data.msg || '保存失败')
          }
        })
        .catch(error => {
          console.error('保存失败:', error)
          this.$message.error('保存失败，请稍后重试')
        })
    },
    saveFamilyInfo() {
      // 保存所有家庭成员
      const savePromises = this.familyInfo.members.map((member, index) => {
        const familyData = {
          studentNumber: this.studentInfo.studentNumber,
          familyName: member.name,           // 使用 familyName 而不是 family_name
          familyPart: member.relation,       // 使用 familyPart 而不是 family_part
          familyAge: '',
          familyPoliteAspect: member.occupation,  // 使用 familyPoliteAspect 而不是 family_polite_aspect
          familyOrganization: member.workplace    // 使用 familyOrganization 而不是 family_organization
        }
        
        // 如果是已有的家庭成员，则更新
        if (this.originalFamilyInfo && 
            this.originalFamilyInfo.members[index] && 
            this.originalFamilyInfo.members[index].id) {
          familyData.id = this.originalFamilyInfo.members[index].id
          return this.$http.put('/api/student/family/update', familyData)
        } else {
          // 否则添加新的家庭成员
          return this.$http.post('/api/student/family/add', familyData)
        }
      })
      
      Promise.all(savePromises)
        .then(responses => {
          const allSuccess = responses.every(response => response.data.code === 200)
          if (allSuccess) {
            this.$message.success('保存成功')
            this.isEditingFamily = false
            this.getFamilyInfo() // 刷新数据
          } else {
            this.$message.error('部分数据保存失败')
          }
        })
        .catch(error => {
          console.error('保存失败:', error)
          this.$message.error('保存失败，请稍后重试')
        })
    },
    addEducation() {
      this.resumeInfo.education.push({
        schoolName: '',
        major: '',
        startDate: '',
        endDate: '',
        description: ''
      })
    },
    removeEducation(index) {
      this.resumeInfo.education.splice(index, 1)
    },
    addFamilyMember() {
      this.familyInfo.members.push({
        name: '',
        relation: '',
        phone: '',
        workplace: '',
        occupation: ''
      })
    },
    removeFamilyMember(index) {
      this.familyInfo.members.splice(index, 1)
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
  margin-bottom: 20px;
}
</style>