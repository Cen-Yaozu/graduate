<template>
  <div class="bg"></div>
  <div class="common-layout">
    <el-container>
      <el-main>
        <el-space wrap>
          <el-card style="width: 800px;height: 400px;background: linear-gradient(to top right,rgba(90,149,207,.5),#f5f7fa);margin-top: 80px;">
            <div class="notice-title">
              使用须知
            </div>
            <div class="notice-break-line"></div>
            <div class="notice-content">
              <div class="ql-snow">
                <div id="ql-editor1" class="ql-editor"><p>首次访问请先点击<strong style="color: black;">“</strong>
                  <a href="http://localhost:8081/activate" style="color: rgb(252 29 29);text-decoration: none"><strong>账号激活</strong></a><strong style="color: black;">”</strong>，设置密码并激活成功后将获取本人学号/工号，之后登录请使用本人学号/工号作为账号，登录密码为账号激活时设置的密码。</p>
                  <p>如遗忘学号/工号，本科生可使用考生号作为账号登录；</p><p>如遗忘密码，可点击<strong style="color: black;">“</strong>
                    <a @click.prevent="findpassword=true" style="color: rgb(252 29 29);"><strong>密码找回</strong></a>
                    <strong style="color: black;">”</strong>,通过信息校验后重置密码。</p><p>平台使用过程中如有任何问题，请致电010-83903297或83906458；</p>
                  <p>本科新生报到其他问题请咨询010-83903097。</p>
                  <p>为了方便您在外网访问内网和图书馆资源，开通了新版VPN，无需安装客户端和插件，支持电脑和手机直接使用，为了获得更好的体验建议使用Chrome、Firefox、IE11、Edge、Safari等浏览器。账号为统一身份认证的用户名和密码。</p></div>
              </div>
            </div>
          </el-card>
          <el-card style="width: 348px;height: 400px;background: linear-gradient(to top right,rgba(90,149,207,.5),#f5f7fa);margin-top: 80px;">
            <div class="wrapper">
              <div class="title">
                <span style="font-size: 30px; color: rgb(121, 64, 191);">M</span>
                <span style="font-size: 30px; color: rgb(104, 74, 198);">Y</span>
                <span style="font-size: 30px; color: rgb(86, 85, 205);">S</span>
                <span style="font-size: 30px; color: rgb(78, 90, 209);">E</span>
                <span style="font-size: 30px; color: rgb(59, 101, 216);">I</span>
                <span style="font-size: 30px; color: rgb(52, 105, 219);">G</span>
              </div>
              <div class="content" style="margin-top: 40px">
                <el-form status-icon ref="loginRef" :model="loginForm" :rules="loginRules"  style="line-height: 38px">
                  <el-space wrap>
                  <el-form-item prop="username">
                    <el-input v-model="loginForm.studentNumber" placeholder="输入学号" prefix-icon="User" clearable/>
                  </el-form-item>
                  <el-form-item prop="password">
                    <el-input v-model="loginForm.password" prefix-icon="Lock" clearable type="password"/>
                  </el-form-item>
                  <!-- <el-form-item prop="code">
                    <el-input v-model="loginForm.code" style="width: 200px" placeholder="输入验证码" clearable/>
                    <div class="login-code">
                      <img src="../assets/img/code.jpg" @click="refreshCode">
                    </div>
                  </el-form-item> -->
                  </el-space>
                  <el-form-item>
                    <el-button type="primary" style="margin-top:34px;width: 100%;height: 40px;background-color: #3a71a8" @click="login">登录</el-button>
                  </el-form-item>
                </el-form>
              </div>
            </div>
          </el-card>
        </el-space>
      </el-main>
    </el-container>
  </div>
  <el-dialog v-model="findpassword" title="重置密码" width="460px" style="text-align: initial;">
    <el-form :model="form"
             :rules="rules"
             label-width="auto" style="max-width: 600px;padding: 20px 30px">
      <el-form-item label="账号" prop="studentNumber">
        <el-input
            v-model="form.studentNumber"
            style="max-width: 600px"
            placeholder="输入学号"

        >
          <template #append>
            <el-button>发送邮箱验证码</el-button>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item label="验证码" prop="textcode">
        <el-input v-model="form.textcode"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="pass">
        <el-input v-model="form.pass" type="password" autocomplete="off" />
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPass">
        <el-input
            v-model="form.checkPass"
            type="password"
            autocomplete="off"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer" style="display: flex;justify-content: center;">
        <el-button type="primary" style="width: 80%" @click="dialogFormVisible = false">
          确认
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script>

export default {
  name: "LoginView",
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
      findpassword:false,
      form:{
        studentNumber: '',
        textcode:'',
        pass:'',
        checkPass:'',
      },

      loginForm:{
        studentNumber: '',
        password:'',
        code: ''
      },
      loginRules:{
        studentNumber:[{required:true,message:'请输入用户名',trigger:'blur'}],
        password:[{required:true,message:'请输入密码',trigger:'blur'}]
        // 验证码相关规则暂时注释
        // code:[{required:true,message:'请输入验证码',trigger:'blur'}]
      },
      rules:{
        studentNumber:[
          { required: true, message: '请填写' },
          { type: 'number', validator: checknull,trigger: 'blur' },
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
  methods: {
    login(){
        this.$refs.loginRef.validate(async v=>{
          if(!v) return
          console.log(this.loginForm)
          const p ={
            "studentNumber": this.loginForm.studentNumber,
            "password": this.loginForm.password
          }
          const {data:res}=await this.$http.post('/login',p)
          console.log(res)
          window.sessionStorage.setItem('token',res.data.token)
          console.log(window.sessionStorage.getItem('token'));
          if (res.code!==200) return this.$message.error(res.data.msg)
          this.$message.success(res.data.msg)
          window.sessionStorage.setItem('roles',res.data.auth)
          window.sessionStorage.setItem('studentName',this.loginForm.studentNumber)
          // console.log(this.$router);
          if(res.data.auth==="ROLE_STUDENT"){
            await this.$router.push('/freshmanreport')
          }else{
            await this.$router.push('/')
          }
        })
    }
  },
}
</script>

<style scoped>
.bg {
  display: block;
  position: fixed;
  background:url("../assets/img/login_p1.jpg");
  background-position-x: 0%;
  background-position-y: 0%;
  background-repeat: repeat;
  background-size: auto;
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  top: 0px;
  height: 100%;
  width: 100%;
  z-index: -10;
  overflow: hidden;
}
/*左边*/
.notice-title {
  font-weight: bold;
  font-size: 21px;
  color: black;
}
.notice-break-line {
  background-color: black;
  width: 100%;
  margin-top: 14px;
  margin-bottom: 20px;
  height: 2px;
}
.notice-content {
  text-align: left;
  text-indent: 2em;
  font-size: 16px;
  color: black;
  line-height: 2;
}
.ql-snow *{
  box-sizing: border-box;
}
.ql-editor {
  box-sizing: border-box;
  line-height: 1.42;
  height: 100%;
  outline: none;
  overflow-y: auto;
  padding: 12px 15px;
  tab-size: 4;
  -moz-tab-size: 4;
  text-align: left;
  white-space: pre-wrap;
  word-wrap: break-word;
}
/*右边*/
.title {
  padding: 10px 0;
  font-size: 17px;
  font-family: fantasy;
  text-align: center;
}
/*表单*/
.el-form-item.my-class >:last-child {
  margin-left: 0px;
}
.el-input{
  width: 307px;
}
/*验证码*/
.login-code img {
  width: 100%;
  height: 100%;
  cursor: pointer;
  vertical-align: middle;
}
</style>