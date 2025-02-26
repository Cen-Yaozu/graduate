<template>
  <div class="account">
    <el-header style="padding: 0;height: auto;background-color:#244cb2">
      <el-page-header icon="">
        <template #title>
          <img src="../assets/img/logo.png" style="height: 52px;max-width: 100%">
        </template>
        <template #content>
          <span class="text-large font-600 mr-3" style="line-height: 70px;font-weight: 700;font-size: 20px;color: white"> 账号激活 </span>
        </template>
      </el-page-header>

    </el-header>
    <el-main>
      <el-card  style="max-width: 480px">
        <template #header>
          <el-steps :space="200" :active="currentStep" align-center>
            <el-step title="信息校验" />
            <el-step title="绑定邮箱" />
            <el-step title="设置密码" />
            <el-step title="激活完成" />
          </el-steps>
        </template>
        <el-form :model="check" :rules="rules" label-position="top" style="max-width: 600px;margin: 0 auto" label-width="auto">
          <div v-show="currentStep===0">
            <el-form-item label="考生号" prop="admissionTicket">
              <el-input v-model="check.admissionTicket" />
            </el-form-item>
            <el-form-item label="姓名" prop="studentName">
              <el-input v-model="check.studentName" />
            </el-form-item>
            <el-form-item label="证件" prop="idType">
              <el-select v-model="check.idType" placeholder="选择证件">
                <el-option label="居民身份证" value="居民身份证" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
            <el-form-item label="证件号" prop="idCard">
            <el-input v-model="check.idCard" />
          </el-form-item>
          </div>
          <div v-show="currentStep===1">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="check.email" />
            </el-form-item>
            <el-form-item label="短信验证" prop="textcode" style="margin-bottom: 178px">
              <el-space>
              <el-input v-model="check.textcode" style="width: 250px"/>
              <el-button>获取验证码</el-button>
              </el-space>
            </el-form-item>
            <p @click="currentStep=2" style="color: red">如无法收到短信验证码，可点击跳过</p>
          </div>
          <div v-show="currentStep===2">
            <p style="font-weight: bold;margin-bottom: 20px;color: dimgrey;font-size: 15px;">校验完成，请记录你的学号<el-text style="color: deepskyblue">{{check.studentNumber}}</el-text>,并设置密码</p>
            <el-form-item label="密码" prop="pass">
              <el-input v-model="check.pass" type="password" autocomplete="off" />
            </el-form-item>
            <el-form-item label="确认密码" prop="checkPass">
              <el-input
                  v-model="check.checkPass"
                  type="password"
                  autocomplete="off"
                  style="margin-bottom: 146px"
              />
            </el-form-item>
          </div>
          <div v-show="currentStep===3">
            <p style="font-size: 30px;font-weight: 600;color: red;margin: 144px auto 144px;">欢迎你加入广州软件学院</p>
          </div>
        </el-form>
        <el-button v-show="currentStep!==3" style="margin-top: 12px" @click="next">下一步</el-button>
        <el-button v-show="currentStep===3" style="margin-top: 12px" @click="tologin">前往登录</el-button>
      </el-card>
    </el-main>
  </div>
</template>

<script>
import router from "@/router";
export default {
name: "ActivateView",
  data(){
  // 是否为空
  var checknull=(rule,value,callback)=>{
      if (value.trim()===''){
        callback(new Error("不可为空"))
      }else {
        return callback();
      }
    };
  // 密码
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.check.checkPass !== '') {
          this.$refs.check.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.check.pass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
  return{
    currentStep:0,
    check:{
      admissionTicket:'',
      studentName:'',
      idType:'',
      idCard:'',
      email:'',
      textcode:'',
      studentNumber:'123',
      pass:'',
      checkPass:'',
    },
    rules:{
      admissionTicket:[
        { required: true, message: '请填写' },
        { type: 'number', validator: checknull,trigger: 'blur' },
      ],
      studentName:[
        { required: true, message: '请填写', trigger: 'blur' },
        { validator: checknull,trigger: 'blur'},
      ],
      idType:[
        { required: true, message: '选择证件', trigger: 'change' },
      ],
      idCard:[
        { required: true, message: '请填写' },
        { validator: checknull,trigger: 'blur'},
      ],
      email: [
        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
      ],
      textcode:[
        { required: true, message: '请填写' },
      ],
      pass:[
        { required: true, message: '请填写' },
        { validator: validatePass, trigger: 'blur'},
      ],
      checkPass: [
        { required: true, message: '请填写' },
        { validator: validatePass2, trigger: 'blur' }
      ],
    }
    }
  },
  methods:{
  next(){
    if (this.currentStep===3){
      this.
      router.push('/')

    }else {
      this.currentStep++;
    }
    console.log(this.currentStep)
  },
  tologin(){
    router.push('/login')
    },
  }
}
</script>

<style scoped>

.account{
  background:url("../assets/img/navbg.png");
  width: 100%;
  height: 100%;
}
/*导航栏*/
::v-deep .el-page-header__title{
  margin-right: auto;
  height: 80px;
  max-width: 240px;
  align-items: center;
  display: flex;
  position: relative;
  align-items: center;
}
::v-deep .el-page-header__left {
  margin-left: 97px;
}
/*卡片*/
.el-card {
  overflow-y: auto;
  margin: 30px auto;
  padding: 48px 97px;
  min-height: 460px;
  min-width: 750px;
  max-width: 1260px;
  background: hsla(0,0%,100%,.9);
  border-radius: 8px;
  -ms-flex-item-align: start;
  align-self: flex-start;
  -webkit-backdrop-filter: blur(8px);
  backdrop-filter: blur(8px);
}
::v-deep .el-card__header{
  border-bottom: none;
}
::v-deep .el-card__body p {
  text-align: center;
}
/*表单*/
::v-deep .el-form-item__label{
  width: 100%;
}
</style>