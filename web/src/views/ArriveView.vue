<template>
  <el-form
      ref="arriveForm"
      style="max-width: 600px"
      :model="arriveForm"
      :rules="rules"
      label-width="auto"
      class="demo-arriveForm"
      status-icon
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
    <el-form-item label="随行人数" prop="familynum">
      <el-input v-model.number="arriveForm.familynum" />
    </el-form-item>
    <el-button type="primary" @click="submitForm('arriveForm')">
      确认
    </el-button>
    <el-button @click="resetForm('arriveForm')">重置</el-button>
  </el-form>
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
      familynum:'',
    },
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
      familynum: [
        { required: true, message: '随行人数不能为空'},
        { type: 'number', message: '随行人数必须为数字'}
      ],
    }
  }
  },
  methods:{
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$http.post('/arrive/update', this.arriveForm).then(res => {
            if(res.data.code === '0') {
              this.$message.success('提交成功');
            } else {
              this.$message.error(res.data.msg);
            }
          });
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style scoped>

</style>