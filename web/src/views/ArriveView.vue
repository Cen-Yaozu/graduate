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
      <el-select v-model="arriveForm.tool" placeholder="Activity zone">
        <el-option label="小车" value="shanghai" />
        <el-option label="地铁" value="beijing" />
        <el-option label="公交" value="beijing" />
      </el-select>
    </el-form-item>
    <el-form-item label="到站日期" required>
      <el-col :span="11">
        <el-form-item prop="date">
          <el-date-picker
              v-model="arriveForm.date"
              type="date"
              aria-label="Pick a date"
              placeholder="Pick a date"
              style="width: 100%"
          />
        </el-form-item>
      </el-col>
      <el-col class="text-center" :span="2">
        <span class="text-gray-500">-</span>
      </el-col>
    </el-form-item>
      <el-form-item label="到站时间段" prop="time">
        <el-select v-model="arriveForm.time" placeholder="Activity zone">
          <el-option label="8:30-12:59" value="8:30-12:59" />
          <el-option label="13.00-17:30" value="13.00-17:30" />
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
        { required: true, message: '请选择活动区域', trigger: 'change' }
      ],
      time:[
        { type:'date',required: true, message: '请选择活动区域', trigger: 'change' }
      ],
      date:[
        { type:'date',required: true, message: '请选择活动区域', trigger: 'change' }
      ],
      familynum: [
        { required: true, message: '不能为空'},
        { type: 'number', message: '必须为数字值'}
      ],
    }
  }
  },
  methods:{
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!');
        } else {
          console.log('error submit!!');
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