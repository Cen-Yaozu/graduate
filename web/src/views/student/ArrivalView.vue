<template>
  <div class="arrival-container">
    <el-card class="arrival-card">
      <template #header>
        <div class="card-header">
          <span>抵校管理</span>
        </div>
      </template>

      <el-form :model="arrivalInfo" label-width="120px" :disabled="arrivalInfo.status === '已登记'">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学号">
              <el-input v-model="arrivalInfo.studentNumber" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名">
              <el-input v-model="arrivalInfo.name" disabled />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="抵达日期">
              <el-date-picker
                v-model="arrivalInfo.date"
                type="date"
                placeholder="选择抵达日期"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="抵达时间">
              <el-time-picker
                v-model="arrivalInfo.time"
                placeholder="选择抵达时间"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="交通工具">
              <el-select v-model="arrivalInfo.tool" placeholder="请选择交通工具">
                <el-option label="火车" value="火车" />
                <el-option label="飞机" value="飞机" />
                <el-option label="汽车" value="汽车" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="家人陪同人数">
              <el-input-number v-model="arrivalInfo.familyNum" :min="0" :max="10" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item>
          <el-button type="primary" @click="submitArrival" v-if="arrivalInfo.status !== '已登记'">提交登记</el-button>
          <el-tag type="success" v-else>已完成登记</el-tag>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ArrivalView',
  data() {
    return {
      arrivalInfo: {
        studentNumber: '',
        name: '',
        date: '',
        time: '',
        tool: '',
        familyNum: 0,
        status: '未登记'
      }
    }
  },
  created() {
    this.fetchArrivalInfo()
  },
  methods: {
    async fetchArrivalInfo() {
      try {
        const studentNumber = window.sessionStorage.getItem('studentName')
        if (!studentNumber) {
          this.$message.error('未找到学号信息，请重新登录')
          return
        }
        
        const response = await this.$http.get(`/api/arrive/info/${studentNumber}`)
        if (response.data.code === 200) {
          const data = response.data.data
          if (data) {
            // 初始化日期和时间
            const date = data.date ? new Date(data.date) : null
            let timeDate = null
            if (data.time) {
              const [hours, minutes] = data.time.split(':')
              timeDate = new Date()
              timeDate.setHours(parseInt(hours))
              timeDate.setMinutes(parseInt(minutes))
            }
            
            this.arrivalInfo = {
              ...data,
              date: date,
              time: timeDate,
              status: '已登记'
            }
          } else {
            this.arrivalInfo.studentNumber = studentNumber
            this.arrivalInfo.status = '未登记'
          }
        }
      } catch (error) {
        console.error('获取抵校信息失败:', error)
        this.$message.error('获取抵校信息失败')
      }
    },
    async submitArrival() {
      try {
        if (!this.arrivalInfo.date || !this.arrivalInfo.time || !this.arrivalInfo.tool) {
          this.$message.error('请填写完整信息')
          return
        }

        const response = await this.$http.put('/api/arrive/update', this.arrivalInfo)
        
        if (response.data.code === 200) {
          this.$message.success('登记成功')
          this.arrivalInfo.status = '已登记'
        } else {
          this.$message.error(response.data.msg || '登记失败')
        }
      } catch (error) {
        console.error('登记失败:', error)
        this.$message.error('登记失败')
      }
    }
  }
}
</script>

<style scoped>
.arrival-container {
  padding: 20px;
}
.arrival-card {
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