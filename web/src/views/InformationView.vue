<template>
  <div class="information-container">
    <!-- 信息已提交提示 -->
    <el-alert
      v-if="isInfoCompleted"
      title="信息收集已完成，不可再次修改"
      type="info"
      :closable="false"
      show-icon
      style="margin-bottom: 20px;"
    />
    
    <!-- 表单区域 -->
    <el-form :model="studentForm" :rules="rules" ref="studentFormRef" label-width="100px" :disabled="isInfoCompleted">
      <el-descriptions title="学生基本信息" border>
        <el-descriptions-item label="姓名">{{ studentName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ sex }}</el-descriptions-item>
        <el-descriptions-item label="出生年月">{{ birth }}</el-descriptions-item>
        <el-descriptions-item label="籍贯">
          <el-input
            v-model="studentForm.hometown"
            style="width: 240px"
            clearable
            placeholder="请输入籍贯"
          />
        </el-descriptions-item>
        <el-descriptions-item label="民族">
          <el-input
              v-model="studentForm.nation"
              style="width: 240px"
              clearable
              placeholder="请输入民族"
          />
        </el-descriptions-item>
        <el-descriptions-item label="政治面貌">
          <el-input
              v-model="studentForm.politeAspect"
              style="width: 240px"
              clearable
              placeholder="请输入政治面貌"
          />
        </el-descriptions-item>
        <el-descriptions-item label="证件">身份证</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ idCard }}</el-descriptions-item>
        <el-descriptions-item label="电话">
          <el-input
              v-model="studentForm.studentPhone"
              style="width: 240px"
              clearable
              placeholder="请输入电话号码"
          />
        </el-descriptions-item>
        <el-descriptions-item label="详细通讯地址">
          <el-input
              v-model="studentForm.address"
              style="width: 400px"
              clearable
              placeholder="请输入详细通讯地址"
          />
        </el-descriptions-item>
      </el-descriptions>
    </el-form>
    
    <br>
    <el-descriptions title="学生简历" :column="3" border></el-descriptions>
    <el-table :data="resumeData" style="width: 100%" max-height="250">
      <el-table-column fixed label="开始时间" width="200">
        <template #default="scope">
          <el-input v-model="scope.row.startTime" placeholder="例如：2019年9月" :disabled="isInfoCompleted"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" width="200">
        <template #default="scope">
          <el-input v-model="scope.row.endTime" placeholder="例如：2022年6月" :disabled="isInfoCompleted"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="单位学习、工作" width="300">
        <template #default="scope">
          <el-input v-model="scope.row.organization" placeholder="请输入单位或学校名称" :disabled="isInfoCompleted"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="职务" width="120">
        <template #default="scope">
          <el-input v-model="scope.row.hats" placeholder="例如：学生" :disabled="isInfoCompleted"></el-input>
        </template>
      </el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-button class="mt-4" type="primary" style="width: 20%" @click="onAddItem" :disabled="isInfoCompleted">
            添加
          </el-button>
        </template>
        <template #default="scope">
          <el-button
              size="small"
              type="danger"
              @click="handleDelete(scope.$index, resumeData)"
              :disabled="isInfoCompleted"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <br>
    <el-descriptions title="家庭主要成员" :column="3" border></el-descriptions>
    <el-table :data="familyData" style="width: 100%" max-height="250">
      <el-table-column fixed label="称谓" width="120">
        <template #default="scope">
          <el-input v-model="scope.row.familyPart" placeholder="例如：父亲" :disabled="isInfoCompleted"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="姓名" width="150">
        <template #default="scope">
          <el-input v-model="scope.row.familyName" placeholder="请输入姓名" :disabled="isInfoCompleted"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="年龄" width="100">
        <template #default="scope">
          <el-input v-model="scope.row.familyAge" placeholder="请输入年龄" :disabled="isInfoCompleted"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="政治面貌" width="150">
        <template #default="scope">
          <el-input v-model="scope.row.familyPoliteAspect" placeholder="请输入政治面貌" :disabled="isInfoCompleted"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="单位学习、工作" width="300">
        <template #default="scope">
          <el-input v-model="scope.row.familyOrganization" placeholder="请输入工作单位" :disabled="isInfoCompleted"></el-input>
        </template>
      </el-table-column>
      <el-table-column align="right">
        <template #header>
          <el-button class="mt-4" type="primary" style="width: 55%" @click="onAddFamilyItem" :disabled="isInfoCompleted">
            添加
          </el-button>
        </template>
        <template #default="scope">
          <el-button
              size="small"
              type="danger"
              @click="familyDelete(scope.$index, familyData)"
              :disabled="isInfoCompleted"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 提交按钮 -->
    <div class="actions-container">
      <el-button type="primary" @click="saveAllInfo" :loading="loading" :disabled="isInfoCompleted">保存全部信息</el-button>
      <el-button @click="resetForm" :disabled="isInfoCompleted">重置</el-button>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'

export default {
  name: "InformationView",
  data() {
    return {
      loading: false,
      studentNumber: window.sessionStorage.getItem('studentNumber') || '',
      studentName: window.sessionStorage.getItem('studentName') || '',
      sex: '女',  // 这里可以根据已有信息填充
      birth: '2003年8月19日',  // 这里可以根据已有信息填充
      idCard: '442000200609103879',  // 这里可以根据已有信息填充
      isInfoCompleted: false,  // 表示信息是否已经完成
      
      // 学生基本信息表单
      studentForm: {
        studentNumber: '',
        hometown: '',
        nation: '',
        politeAspect: '',
        studentPhone: '',
        address: '',
        idType: '身份证'
      },
      
      // 表单验证规则
      rules: {
        studentPhone: [
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入详细通讯地址', trigger: 'blur' }
        ]
      },
      
      // 简历数据
      resumeData: [],
      
      // 家庭成员数据
      familyData: []
    }
  },
  created() {
    // 初始化表单数据
    this.initFormData();
  },
  methods: {
    // 初始化表单数据
    initFormData() {
      this.studentForm.studentNumber = this.studentNumber;
      
      // 从服务器获取已有信息
      if (this.studentNumber) {
        this.loading = true;
        this.$http.get(`/api/freshman-report/get-student-info?studentNumber=${this.studentNumber}`)
          .then(response => {
            if (response.data.code === 200 && response.data.data) {
              const data = response.data.data;
              
              // 设置学生基本信息
              if (data.student) {
                // 保存表单不可修改的字段
                if (data.student.studentName) this.studentName = data.student.studentName;
                if (data.student.sex) this.sex = data.student.sex;
                if (data.student.birth) this.birth = data.student.birth;
                if (data.student.idCard) this.idCard = data.student.idCard;
                
                // 设置可修改的表单字段
                this.studentForm.hometown = data.student.hometown || '';
                this.studentForm.nation = data.student.nation || '';
                this.studentForm.politeAspect = data.student.politeAspect || '';
                this.studentForm.studentPhone = data.student.studentPhone || '';
                this.studentForm.address = data.student.address || '';
                
                // 检查是否所有必填字段都已填写
                const isAllFieldsFilled = 
                  this.studentForm.hometown && 
                  this.studentForm.nation && 
                  this.studentForm.politeAspect && 
                  this.studentForm.studentPhone && 
                  this.studentForm.address;
                
                // 检查是否有简历和家庭成员记录
                const hasResume = data.resumes && data.resumes.length > 0 && 
                  data.resumes.some(resume => resume.organization);
                
                const hasFamily = data.familyMembers && data.familyMembers.length > 0 && 
                  data.familyMembers.some(family => family.familyName);
                
                // 只有当所有必填字段都已填写，才将表单标记为已完成
                if (isAllFieldsFilled && hasResume && hasFamily) {
                  this.isInfoCompleted = true;
                  ElMessage.info('信息已全部填写完成，不可再次修改');
                } else {
                  this.isInfoCompleted = false;
                  ElMessage.info('部分信息未填写，请完善信息后提交');
                }
              }
              
              // 设置简历信息
              if (data.resumes && data.resumes.length > 0) {
                this.resumeData = data.resumes.map(resume => ({
                  startTime: resume.startTime || '',
                  endTime: resume.endTime || '',
                  organization: resume.organization || '',
                  hats: resume.hats || ''
                }));
              } else {
                // 没有记录则显示一条空记录
                this.resumeData = [
                  {
                    startTime: '',
                    endTime: '',
                    organization: '',
                    hats: ''
                  }
                ];
              }
              
              // 设置家庭成员信息
              if (data.familyMembers && data.familyMembers.length > 0) {
                this.familyData = data.familyMembers.map(family => ({
                  familyPart: family.familyPart || '',
                  familyName: family.familyName || '',
                  familyAge: family.familyAge || '',
                  familyPoliteAspect: family.familyPoliteAspect || '',
                  familyOrganization: family.familyOrganization || ''
                }));
              } else {
                // 没有记录则显示一条空记录
                this.familyData = [
                  {
                    familyPart: '',
                    familyName: '',
                    familyAge: '',
                    familyPoliteAspect: '',
                    familyOrganization: ''
                  }
                ];
              }
              
              ElMessage.success('数据加载成功');
            } else {
              // 处理未找到记录的情况，显示空表单
              this.setEmptyData();
              console.log('未找到学生记录或获取数据失败');
            }
          })
          .catch(error => {
            console.error('获取数据失败:', error);
            // 处理错误，显示空表单
            this.setEmptyData();
            // 如果不是 404 错误（未找到记录），则显示错误消息
            if (error.response && error.response.status !== 404) {
              ElMessage.error('获取数据失败: ' + (error.response?.data?.msg || '服务器错误'));
            }
          })
          .finally(() => {
            this.loading = false;
          });
      } else {
        // 没有学号则显示空表单
        this.setEmptyData();
        ElMessage.warning('未获取到学号信息，请重新登录');
      }
    },
    
    // 设置空的表单数据
    setEmptyData() {
      // 暂时使用示例数据
      this.resumeData = [
        {
          startTime: '',
          endTime: '',
          organization: '',
          hats: ''
        }
      ];
      
      this.familyData = [
        {
          familyPart: '',
          familyName: '',
          familyAge: '',
          familyPoliteAspect: '',
          familyOrganization: ''
        }
      ];
    },
    
    // 添加简历项
    onAddItem() {
      this.resumeData.push({
        startTime: '',
        endTime: '',
        organization: '',
        hats: ''
      });
    },
    
    // 删除简历项
    handleDelete(index, rows) {
      if (rows.length === 1) {
        ElMessage.warning('至少保留一条记录');
        return;
      }
      rows.splice(index, 1);
    },
    
    // 添加家庭成员
    onAddFamilyItem() {
      this.familyData.push({
        familyPart: '',
        familyName: '',
        familyAge: '',
        familyPoliteAspect: '',
        familyOrganization: ''
      });
    },
    
    // 删除家庭成员
    familyDelete(index, rows) {
      if (rows.length === 1) {
        ElMessage.warning('至少保留一条记录');
        return;
      }
      rows.splice(index, 1);
    },
    
    // 保存所有信息
    saveAllInfo() {
      if (!this.studentNumber) {
        ElMessage.error('未获取到学号信息，请重新登录');
        return;
      }
      
      // 检查是否所有必填字段都已填写
      if (!this.studentForm.address) {
        ElMessage.warning('请填写详细通讯地址');
        return;
      }
      
      // 检查是否有简历信息
      const hasValidResume = this.resumeData.some(item => 
        item.organization && item.startTime && item.endTime);
      if (!hasValidResume) {
        ElMessage.warning('请至少填写一条完整的简历信息');
        return;
      }
      
      // 检查是否有家庭成员信息
      const hasValidFamily = this.familyData.some(item => 
        item.familyName && item.familyPart);
      if (!hasValidFamily) {
        ElMessage.warning('请至少填写一条完整的家庭成员信息');
        return;
      }
      
      this.loading = true;
      
      // 处理数据
      const allData = {
        student: {
          ...this.studentForm,
          studentNumber: this.studentNumber,
          studentName: this.studentName,
          sex: this.sex,
          birth: this.birth,
          idCard: this.idCard
        },
        resumes: this.resumeData.filter(item => item.organization || item.startTime || item.endTime || item.hats),
        familyMembers: this.familyData.filter(item => item.familyName || item.familyPart || item.familyAge || item.familyPoliteAspect || item.familyOrganization)
      };
      
      // 发送请求到后端
      this.$http.post('/api/freshman-report/save-all-info', allData)
        .then(response => {
          if (response.data.code === 200) {
            // 检查所有必填信息是否已填写完整
            const isAllFieldsFilled = 
              this.studentForm.hometown && 
              this.studentForm.nation && 
              this.studentForm.politeAspect && 
              this.studentForm.studentPhone && 
              this.studentForm.address;
            
            if (isAllFieldsFilled && hasValidResume && hasValidFamily) {
              this.isInfoCompleted = true;
              ElMessage.success('信息填写完整并保存成功，不可再次修改');
            } else {
              ElMessage.success('信息已保存，部分信息未完善，可继续编辑');
            }
          } else {
            ElMessage.error(response.data.msg || '信息保存失败');
          }
        })
        .catch(error => {
          console.error('保存失败:', error);
          if (error.response && error.response.status === 403) {
            // 如果是权限错误，可能是因为信息已完全填写且提交
            // 重新获取信息检查是否完整
            this.initFormData();
            ElMessage.warning('无法修改，可能信息已完全填写');
          } else {
            ElMessage.error('保存失败: ' + (error.response?.data?.msg || '服务器错误'));
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    
    // 重置表单
    resetForm() {
      this.studentForm = {
        studentNumber: this.studentNumber,
        hometown: '',
        nation: '',
        politeAspect: '',
        studentPhone: '',
        address: '',
        idType: '身份证'
      };
      
      this.resumeData = [
        {
          startTime: '',
          endTime: '',
          organization: '',
          hats: ''
        }
      ];
      
      this.familyData = [
        {
          familyPart: '',
          familyName: '',
          familyAge: '',
          familyPoliteAspect: '',
          familyOrganization: ''
        }
      ];
    }
  }
}
</script>

<style scoped>
.information-container {
  padding: 20px;
}

.actions-container {
  margin-top: 20px;
  text-align: center;
}

.el-descriptions {
  margin-bottom: 15px;
}
</style>