<template>
  <div class="dorm-container">
    <el-card class="dorm-card">
      <template #header>
        <div class="card-header">
          <span>宿舍信息</span>
        </div>
      </template>
      <el-descriptions class="margin-top" title="基本信息" :column="2" border>
        <el-descriptions-item label="宿舍楼">
          {{ dormInfo.building }}
        </el-descriptions-item>
        <el-descriptions-item label="房间号">
          {{ dormInfo.roomNumber }}
        </el-descriptions-item>
        <el-descriptions-item label="床位号">
          {{ dormInfo.bedNumber }}
        </el-descriptions-item>
        <el-descriptions-item label="入住状态">
          <el-tag :type="dormInfo.status === '已入住' ? 'success' : 'warning'">
            {{ dormInfo.status }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>

      <el-divider>室友信息</el-divider>

      <el-table :data="roommateList" style="width: 100%">
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="studentNumber" label="学号" width="120" />
        <el-table-column prop="major" label="专业" />
        <el-table-column prop="bedNumber" label="床位号" width="100" />
        <el-table-column prop="phone" label="联系电话" width="120" />
      </el-table>

      <el-divider>宿舍设施</el-divider>

      <el-form :model="facilities" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="空调">
              <el-tag>{{ facilities.airConditioner ? '正常' : '报修中' }}</el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="热水器">
              <el-tag>{{ facilities.waterHeater ? '正常' : '报修中' }}</el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="网络">
              <el-tag>{{ facilities.internet ? '正常' : '报修中' }}</el-tag>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div class="repair-section">
        <el-button type="primary" @click="handleRepair">报修申请</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'

export default {
  name: 'DormView',
  data() {
    return {
      dormInfo: {
        building: '',
        roomNumber: '',
        bedNumber: '',
        status: ''
      },
      roommateList: [],
      facilities: {
        airConditioner: true,
        waterHeater: true,
        internet: true
      }
    }
  },
  created() {
    this.fetchDormInfo()
    this.fetchRoommateInfo()
  },
  methods: {
    async fetchDormInfo() {
      try {
        const studentNumber = window.sessionStorage.getItem('studentName')
        if (!studentNumber) {
          ElMessage.error('未找到学号信息，请重新登录')
          return
        }
        
        const response = await this.$http.get(`/api/student/dorm/${studentNumber}`)
        console.log('宿舍信息API返回数据：', response.data)
        if (response.data.code === 200) {
          const dormData = response.data.data
          this.dormInfo = {
            building: dormData.dormitory || '',
            roomNumber: dormData.dormCard || '',
            bedNumber: dormData.dormType || '',
            status: '已入住'
          }
          console.log('处理后的宿舍信息：', this.dormInfo)
        } else {
          throw new Error(response.data.message || '获取宿舍信息失败')
        }
      } catch (error) {
        console.error('获取宿舍信息失败:', error)
        ElMessage.error('获取宿舍信息失败')
      }
    },
    async fetchRoommateInfo() {
      try {
        const studentNumber = window.sessionStorage.getItem('studentName')
        if (!studentNumber) {
          ElMessage.error('未找到学号信息，请重新登录')
          return
        }
        
        const response = await this.$http.get(`/api/student/dorm/roommates/${studentNumber}`)
        console.log('室友信息API返回数据：', response.data)
        if (response.data.code === 200) {
          this.roommateList = (response.data.data || []).map(roommate => ({
            name: roommate.name,
            studentNumber: roommate.studentNumber,
            major: roommate.department || '',
            bedNumber: roommate.dormType || '',
            phone: ''
          }))
          console.log('处理后的室友信息：', this.roommateList)
        } else {
          throw new Error(response.data.message || '获取室友信息失败')
        }
      } catch (error) {
        console.error('获取室友信息失败:', error)
        ElMessage.error('获取室友信息失败')
      }
    },
    async handleRepair() {
      try {
        const studentNumber = window.sessionStorage.getItem('studentName')
        if (!studentNumber) {
          ElMessage.error('未找到学号信息，请重新登录')
          return
        }
        
        const response = await this.$http.post('/api/student/dorm/repair', {
          studentNumber,
          dormitory: this.dormInfo.building + '-' + this.dormInfo.roomNumber
        })
        
        if (response.data.code === 200) {
          ElMessage.success('报修申请已提交')
        } else {
          throw new Error(response.data.message || '报修申请提交失败')
        }
      } catch (error) {
        console.error('报修申请提交失败:', error)
        ElMessage.error('报修申请提交失败')
      }
    }
  }
}
</script>

<style scoped>
.dorm-container {
  padding: 20px;
}
.dorm-card {
  max-width: 1200px;
  margin: 0 auto;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.repair-section {
  margin-top: 20px;
  text-align: right;
}
.el-divider {
  margin: 24px 0;
}
</style>