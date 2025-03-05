<template>
  <div class="arrive-container">
    <!-- 信息已提交提示 -->
    <el-alert
      v-if="isInfoCompleted"
      title="到站信息已登记完成，不可再次修改"
      type="info"
      :closable="false"
      show-icon
      style="margin-bottom: 20px;"
    />
    
    <el-form
        ref="arriveForm"
        style="max-width: 600px"
        :model="arriveForm"
        :rules="rules"
        label-width="auto"
        class="demo-arriveForm"
        status-icon
        :disabled="isInfoCompleted"
    >
      <el-form-item label="交通方式" prop="tool">
        <el-select v-model="arriveForm.tool" placeholder="请选择交通方式">
          <el-option label="小车" value="小车" />
          <el-option label="地铁" value="地铁" />
          <el-option label="公交" value="公交" />
        </el-select>
      </el-form-item>
      <el-form-item label="到站日期" required>
        <el-col :span="11">
          <el-form-item prop="date">
            <el-date-picker
                v-model="arriveForm.date"
                type="date"
                aria-label="Pick a date"
                placeholder="请选择到站日期"
                style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col class="text-center" :span="2">
          <span class="text-gray-500">-</span>
        </el-col>
      </el-form-item>
        <el-form-item label="到站时间段" prop="time">
          <el-select v-model="arriveForm.time" placeholder="请选择到站时间段">
            <el-option label="8:30-12:59" value="8:30-12:59" />
            <el-option label="13:00-17:30" value="13:00-17:30" />
          </el-select>
        </el-form-item>
      <el-form-item label="随行人数" prop="familyNum">
        <el-input v-model.number="arriveForm.familyNum" />
      </el-form-item>
      <el-button type="primary" @click="submitForm('arriveForm')" :disabled="isInfoCompleted">
        确认
      </el-button>
      <el-button @click="resetForm('arriveForm')" :disabled="isInfoCompleted">重置</el-button>
    </el-form>
  </div>
</template>

<script>
export default {
name: "ArriveView",
  data(){
  return{
    arriveForm:{
      time:'',
      date:'',
      tool:'',
      familyNum:'',
      studentNumber: ''
    },
    isInfoCompleted: false, // 标记是否已经完成信息采集
    rules:{
      tool:[
        { required: true, message: '请选择交通方式', trigger: 'change' }
      ],
      time:[
        { required: true, message: '请选择到站时间段', trigger: 'change' }
      ],
      date:[
        { required: true, message: '请选择到站日期', trigger: 'change' }
      ],
      familyNum: [
        { required: true, message: '随行人数不能为空'},
        { type: 'number', message: '随行人数必须为数字'}
      ],
    }
  }
  },
  created() {
    // 从session storage获取学号
    this.arriveForm.studentNumber = window.sessionStorage.getItem('studentNumber') || '';
    
    // 加载学生已有的到站信息
    this.fetchArriveInfo();
  },
  methods:{
    // 获取学生已有的到站信息
    fetchArriveInfo() {
      if (!this.arriveForm.studentNumber) {
        this.$message.warning('未获取到学号信息，请重新登录');
        return;
      }
      
      this.$http.get(`/arrive/info?studentNumber=${this.arriveForm.studentNumber}`)
        .then(response => {
          if (response.data.code === 200 && response.data.data) {
            const data = response.data.data;
            
            // 设置表单数据
            this.arriveForm.time = data.time || '';
            // 处理日期格式
            if (data.date) {
              this.arriveForm.date = new Date(data.date);
            }
            this.arriveForm.tool = data.tool || '';
            this.arriveForm.familyNum = data.familyNum || 0;
            
            // 标记为已完成
            this.isInfoCompleted = true;
            this.$message.info('您已登记到站信息，不可再次修改');
          }
        })
        .catch(error => {
          console.log('获取到站信息失败或尚未提交:', error);
          // 404错误是正常的，表示尚未提交信息
          if (error.response && error.response.status !== 404) {
            this.$message.error('获取到站信息失败: ' + (error.response?.data?.msg || '服务器错误'));
          }
        });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 确保学号存在
          if (!this.arriveForm.studentNumber) {
            this.arriveForm.studentNumber = window.sessionStorage.getItem('studentNumber') || '';
            if (!this.arriveForm.studentNumber) {
              this.$message.error('无法获取学号信息，请重新登录');
              return;
            }
          }
          
          this.$http.post('/arrive/update', this.arriveForm).then(res => {
            if(res.data.code === 200) {
              this.$message.success('提交成功');
              // 标记为已完成，禁用编辑
              this.isInfoCompleted = true;
            } else {
              this.$message.error(res.data.msg);
            }
          }).catch(error => {
            if (error.response && error.response.status === 403) {
              this.$message.warning('信息已经提交，不允许再次修改');
              // 标记为已完成，禁用编辑
              this.isInfoCompleted = true;
              // 刷新数据
              this.fetchArriveInfo();
            } else {
              this.$message.error('提交失败：' + (error.response?.data?.msg || '服务器错误'));
            }
          });
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      // 重置表单时保留学号
      this.arriveForm.studentNumber = window.sessionStorage.getItem('studentNumber') || '';
    }
  }
}
</script>

<style scoped>
.arrive-container {
  padding: 20px;
}
</style>