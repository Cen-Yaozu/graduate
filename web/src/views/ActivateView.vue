<template>
  <div class="account">
    <el-header style="padding: 0;height: auto;background-color:#244cb2">
      <el-page-header @back="goBack">
        <template #title>
          <img src="../assets/img/logo.png" style="height: 52px;max-width: 100%">
        </template>
        <template #content>
          <span class="text-large font-600 mr-3" style="line-height: 70px;font-weight: 700;font-size: 20px;color: white"> 账号激活 </span>
        </template>
      </el-page-header>
    </el-header>
    <el-main>
      <el-card class="activate-card">
        <template #header>
          <el-steps :active="currentStep" align-center finish-status="success">
            <el-step title="信息校验" />
            <el-step title="绑定邮箱" />
            <el-step title="设置密码" />
            <el-step title="激活完成" />
          </el-steps>
        </template>
        
        <el-form 
          :model="check" 
          :rules="rules" 
          ref="checkForm" 
          label-position="top" 
          class="activate-form">
          
          <!-- 步骤1：信息校验 -->
          <div v-show="currentStep === 0">
            <el-form-item label="考生号" prop="admissionTicket">
              <el-input v-model="check.admissionTicket" placeholder="请输入考生号" />
            </el-form-item>
            
            <el-form-item label="姓名" prop="studentName">
              <el-input v-model="check.studentName" placeholder="请输入姓名" />
            </el-form-item>
            
            <el-form-item label="证件" prop="idType">
              <el-select v-model="check.idType" placeholder="选择证件类型" style="width: 100%">
                <el-option label="身份证" value="身份证" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
            
            <el-form-item label="证件号" prop="idCard">
              <el-input v-model="check.idCard" placeholder="请输入证件号码" />
            </el-form-item>
          </div>
          
          <!-- 步骤2：绑定邮箱 -->
          <div v-show="currentStep === 1">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="check.email" placeholder="请输入邮箱地址" />
            </el-form-item>
            
            <el-form-item label="验证码" prop="textcode">
              <el-input v-model="check.textcode" placeholder="请输入验证码" style="width: 70%" />
              <el-button 
                type="primary" 
                style="width: 28%; margin-left: 2%" 
                @click="sendVerificationCode" 
                :disabled="codeSent">
                {{ codeButtonText }}
              </el-button>
            </el-form-item>
            
            <div class="skip-link">
              <span @click="skipVerification">无法收到验证码？点击跳过</span>
            </div>
          </div>
          
          <!-- 步骤3：设置密码 -->
          <div v-show="currentStep === 2">
            <div class="notice-text">
              校验完成，请记录你的学号 <span class="student-number">{{ check.studentNumber }}</span>，并设置密码
            </div>
            
            <el-form-item label="密码" prop="pass">
              <el-input 
                v-model="check.pass" 
                type="password" 
                placeholder="请设置密码，不少于6位" 
                show-password />
            </el-form-item>
            
            <el-form-item label="确认密码" prop="checkPass">
              <el-input 
                v-model="check.checkPass" 
                type="password" 
                placeholder="请再次输入密码" 
                show-password />
            </el-form-item>
          </div>
          
          <!-- 步骤4：激活完成 -->
          <div v-show="currentStep === 3" class="completion-step">
            <el-result
              icon="success"
              title="账号激活成功"
              sub-title="欢迎你加入广州软件学院">
            </el-result>
          </div>
        </el-form>
        
        <div class="form-actions">
          <el-button 
            v-show="currentStep !== 3" 
            type="primary" 
            @click="next">
            下一步
          </el-button>
          <el-button 
            v-show="currentStep === 3" 
            type="success" 
            @click="toLogin">
            前往登录
          </el-button>
        </div>
      </el-card>
    </el-main>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'

export default {
  name: "ActivateView",
  data() {
    // 验证是否为空
    const checknull = (rule, value, callback) => {
      if (value.trim() === '') {
        callback(new Error("不可为空"))
      } else {
        callback();
      }
    };
    
    // 密码验证
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.check.checkPass !== '') {
          this.$refs.checkForm.validateField('checkPass');
        }
        callback();
      }
    };
    
    // 确认密码验证
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.check.pass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    
    return {
      currentStep: 0,
      codeSent: false,
      codeButtonText: '获取验证码',
      codeCountdown: 60,
      timerInterval: null,
      check: {
        admissionTicket: '',
        studentName: '',
        idType: '身份证',
        idCard: '',
        email: '',
        textcode: '',
        studentNumber: '',
        pass: '',
        checkPass: '',
      },
      rules: {
        admissionTicket: [
          { required: true, message: '请填写考生号' },
          { validator: checknull, trigger: 'blur' },
        ],
        studentName: [
          { required: true, message: '请填写姓名', trigger: 'blur' },
          { validator: checknull, trigger: 'blur' },
        ],
        idType: [
          { required: true, message: '请选择证件类型', trigger: 'change' },
        ],
        idCard: [
          { required: true, message: '请填写证件号码' },
          { validator: checknull, trigger: 'blur' },
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ],
        textcode: [
          { required: false, message: '请填写验证码' },
        ],
        pass: [
          { required: true, message: '请填写密码' },
          { validator: validatePass, trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
        ],
        checkPass: [
          { required: true, message: '请确认密码' },
          { validator: validatePass2, trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    goBack() {
      this.$router.push('/login');
    },
    next() {
      if (this.currentStep === 0) {
        // 非常简单的验证，只要有内容就通过
        if (this.check.admissionTicket && 
            this.check.studentName && 
            this.check.idType && 
            this.check.idCard) {
          
          // 所有必填字段都有值，直接调用API
          ElMessage.info('正在验证考生信息...');
          this.verifyStudentInfo();
        } else {
          // 提示用户填写完整信息
          if (!this.check.admissionTicket) {
            ElMessage.error('请填写考生号');
          } else if (!this.check.studentName) {
            ElMessage.error('请填写姓名');
          } else if (!this.check.idType) {
            ElMessage.error('请选择证件类型');
          } else if (!this.check.idCard) {
            ElMessage.error('请填写证件号码');
          } else {
            ElMessage.error('请填写完整信息');
          }
        }
      } else if (this.currentStep === 1) {
        // 首先检查邮箱格式
        if (!this.check.email || !/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/.test(this.check.email)) {
          ElMessage.error('请填写正确的邮箱地址');
          return;
        }
        
        // 如果用户没有输入验证码，提示填写
        if (!this.check.textcode) {
          ElMessage.error('请填写验证码');
          return;
        }
        
        // 调用后端API验证验证码
        this.$http.post('/api/verify-code', {
          studentNumber: this.check.studentNumber,
          verificationCode: this.check.textcode
        }).then(response => {
          if (response.data.code === 200) {
            // 验证码正确，进入下一步
            ElMessage.success('验证通过');
            this.currentStep++;
          } else {
            // 验证码错误，提示用户
            ElMessage.error('验证码输入错误');
          }
        }).catch(error => {
          console.error('验证失败:', error);
          ElMessage.error('验证失败: ' + (error.response?.data?.msg || '服务器错误'));
        });
      } else if (this.currentStep === 2) {
        if (this.check.pass && this.check.pass.length >= 6 && this.check.pass === this.check.checkPass) {
          // 密码设置正确，调用API
          this.activateAccount();
        } else {
          if (!this.check.pass) {
            ElMessage.error('请设置密码');
          } else if (this.check.pass.length < 6) {
            ElMessage.error('密码长度不能少于6位');
          } else if (this.check.pass !== this.check.checkPass) {
            ElMessage.error('两次输入密码不一致');
          }
        }
      } else {
        this.currentStep++;
      }
    },
    verifyStudentInfo() {
      // 直接调用API，不再进行表单验证
      this.$http.post('/api/verify-student', {
        admissionTicket: this.check.admissionTicket,
        studentName: this.check.studentName,
        idType: this.check.idType,
        idCard: this.check.idCard
      }).then(response => {
        console.log('验证响应:', response.data);
        
        if (response.data.code === 200) {
          // 验证成功，设置学号并进入下一步
          this.check.studentNumber = response.data.data.studentNumber;
          ElMessage.success('考生信息验证成功');
          this.currentStep++;
        } else {
          // 验证失败，显示错误消息
          ElMessage.error(response.data.msg || '信息不正确，请重新输入');
        }
      }).catch(error => {
        console.error('验证失败:', error);
        ElMessage.error('验证失败: ' + (error.response?.data?.msg || '信息不正确，请重新输入'));
      });
    },
    sendVerificationCode() {
      if (!this.check.email) {
        ElMessage.warning('请先填写邮箱');
        return;
      }
      
      // 验证邮箱格式
      const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
      if (!emailPattern.test(this.check.email)) {
        ElMessage.warning('请输入有效的邮箱地址');
        return;
      }
      
      ElMessage.info('正在发送验证码...');
      
      // 调用后端API发送验证码
      this.$http.post('/api/send-verification', {
        email: this.check.email,
        studentNumber: this.check.studentNumber
      }).then(response => {
        console.log('发送验证码响应:', response.data);
        
        if (response.data.code === 200) {
          // 发送成功，开始倒计时
          this.codeSent = true;
          this.codeCountdown = 60;
          this.codeButtonText = `${this.codeCountdown}秒`;
          this.timerInterval = setInterval(() => {
            this.codeCountdown--;
            this.codeButtonText = `${this.codeCountdown}秒`;
            if (this.codeCountdown <= 0) {
              clearInterval(this.timerInterval);
              this.codeSent = false;
              this.codeButtonText = '获取验证码';
            }
          }, 1000);
          
          ElMessage.success('验证码已发送，请查收');
        } else {
          ElMessage.error(response.data.msg || '验证码发送失败');
        }
      }).catch(error => {
        console.error('发送验证码失败:', error);
        ElMessage.error('发送失败: ' + (error.response?.data?.msg || '服务器错误'));
      });
    },
    skipVerification() {
      ElMessage.warning('已跳过验证码验证');
      this.currentStep = 2;
    },
    activateAccount() {
      // 直接验证密码，不再使用表单验证API
      if (!this.check.pass || this.check.pass.length < 6) {
        ElMessage.error('密码长度不能少于6位');
        return;
      }
      
      if (this.check.pass !== this.check.checkPass) {
        ElMessage.error('两次输入密码不一致');
        return;
      }
      
      ElMessage.info('正在激活账号...');
      
      // 调用后端API激活账号
      this.$http.post('/api/activate-account', {
        studentNumber: this.check.studentNumber,
        password: this.check.pass,
        email: this.check.email,
        verificationCode: this.check.textcode
      }).then(response => {
        console.log('激活账号响应:', response.data);
        
        if (response.data.code === 200) {
          // 激活成功
          ElMessage.success('账号激活成功');
          this.currentStep++;
        } else {
          // 激活失败
          ElMessage.error(response.data.msg || '账号激活失败');
        }
      }).catch(error => {
        console.error('激活失败:', error);
        ElMessage.error('激活失败: ' + (error.response?.data?.msg || '服务器错误'));
      });
    },
    toLogin() {
      this.$router.push('/login');
    }
  },
  beforeUnmount() {
    // 清除可能的定时器
    if (this.timerInterval) {
      clearInterval(this.timerInterval);
    }
  }
}
</script>

<style scoped>
.account {
  background: url("../assets/img/navbg.png");
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 卡片样式 */
.activate-card {
  max-width: 500px;
  margin: 30px auto;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

/* 表单样式 */
.activate-form {
  padding: 20px 0;
}

/* 表单字段样式 */
:deep(.el-form-item__label) {
  font-weight: 500;
  padding-bottom: 5px;
}

/* 学号样式 */
.student-number {
  color: #409EFF;
  font-weight: bold;
}

/* 通知文本样式 */
.notice-text {
  margin-bottom: 20px;
  text-align: center;
  color: #606266;
  font-size: 15px;
}

/* 表单操作区域 */
.form-actions {
  display: flex;
  justify-content: center;
  margin-top: 10px;
  padding: 10px 0;
}

/* 跳过链接样式 */
.skip-link {
  text-align: center;
  margin-top: 20px;
  color: #F56C6C;
  cursor: pointer;
  font-size: 14px;
}

.skip-link span:hover {
  text-decoration: underline;
}

/* 完成步骤样式 */
.completion-step {
  padding: 20px 0;
}

/* 导航栏样式 */
:deep(.el-page-header__title) {
  margin-right: auto;
  height: 80px;
  max-width: 240px;
  display: flex;
  align-items: center;
}

:deep(.el-page-header__left) {
  margin-left: 97px;
}
</style>