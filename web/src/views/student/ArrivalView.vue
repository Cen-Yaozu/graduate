<template>
  <div class="arrival-container">
    <el-card class="arrival-card">
      <template #header>
        <div class="card-header">
          <span>抵校登记</span>
        </div>
      </template>

      <el-form :model="arrivalInfo" label-width="120px" :disabled="arrivalInfo.status === '已登记'">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预计到达时间">
              <el-date-picker
                v-model="arrivalInfo.expectedTime"
                type="datetime"
                placeholder="选择日期时间"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到达方式">
              <el-select v-model="arrivalInfo.transportType" placeholder="请选择到达方式">
                <el-option label="火车" value="train" />
                <el-option label="飞机" value="plane" />
                <el-option label="汽车" value="bus" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="车次/航班号">
              <el-input v-model="arrivalInfo.transportNumber" placeholder="请输入车次或航班号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="到达站点">
              <el-input v-model="arrivalInfo.arrivalStation" placeholder="请输入到达站点" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="是否需要接站">
              <el-radio-group v-model="arrivalInfo.needPickup">
                <el-radio :label="true">是</el-radio>
                <el-radio :label="false">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input
                v-model="arrivalInfo.remarks"
                type="textarea"
                placeholder="请输入其他需要说明的信息"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item>
          <el-button type="primary" @click="submitArrival" v-if="arrivalInfo.status !== '已登记'">提交登记</el-button>
          <el-tag type="success" v-else>已完成登记</el-tag>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card class="arrival-guide-card" style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>抵校指南</span>
        </div>
      </template>
      
      <el-collapse>
        <el-collapse-item title="抵校流程" name="1">
          <div>1. 到达学校后，请先前往指定地点进行抵校登记</div>
          <div>2. 登记完成后，前往宿舍办理入住手续</div>
          <div>3. 按照学院安排参加新生入学教育</div>
        </el-collapse-item>
        <el-collapse-item title="注意事项" name="2">
          <div>1. 请按照学校规定的时间抵校，不要提前或延后</div>
          <div>2. 抵校时请携带好个人身份证件、录取通知书等重要文件</div>
          <div>3. 如有特殊情况需要延迟抵校，请提前向学校请假</div>
        </el-collapse-item>
        <el-collapse-item title="联系方式" name="3">
          <div>学校总机：020-12345678</div>
          <div>新生接待处：020-87654321</div>
          <div>紧急联系人：王老师 13800138000</div>
        </el-collapse-item>
      </el-collapse>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ArrivalView',
  data() {
    return {
      arrivalInfo: {
        expectedTime: '',
        transportType: '',
        transportNumber: '',
        arrivalStation: '',
        needPickup: false,
        remarks: '',
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
        
        const response = await this.$http.get(`/api/student/arrival/${studentNumber}`)
        if (response.data.code === 200) {
          this.arrivalInfo = response.data.data || this.arrivalInfo
        }
      } catch (error) {
        console.error('获取抵校信息失败:', error)
        this.$message.error('获取抵校信息失败')
      }
    },
    async submitArrival() {
      try {
        const studentNumber = window.sessionStorage.getItem('studentName')
        if (!studentNumber) {
          this.$message.error('未找到学号信息，请重新登录')
          return
        }
        
        const response = await this.$http.post('/api/student/arrival', {
          ...this.arrivalInfo,
          studentNumber
        })
        
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
.arrival-guide-card {
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