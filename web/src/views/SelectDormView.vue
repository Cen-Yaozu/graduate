<template>
  <div class="common-layout">
    <el-container>
      <el-header style="background-color: #244cb2; display: flex; align-items: center; justify-content: space-between;">
        <el-page-header @back="goBack" style="color: white">
          <template #content>
            <span class="text-large font-600 mr-3" style="font-weight: 800;color: white">宿舍类型选择</span>
          </template>
        </el-page-header>
        
        <!-- 添加用户头像和下拉菜单 -->
        <div class="user-avatar">
          <el-dropdown trigger="click">
            <span class="el-dropdown-link">
              <el-avatar :size="40" :icon="UserFilled" />
              <el-icon class="el-icon--right">
                <arrow-down />
              </el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>{{ studentNumber }}</el-dropdown-item>
                <el-dropdown-item divided @click="toStudentHome">个人管理</el-dropdown-item>
                <el-dropdown-item @click="handleLogout">退出系统</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <el-card body-style="padding: 40px 0 0 0" style="width: 90%;height: 500px;background: linear-gradient(to top right,rgba(90,149,207,.5),#f5f7fa);margin: 40px auto;">
          <el-card style="max-width: 200px;background: linear-gradient(to top right,#F7EBE1,#f7DFF1);border-radius: 0 30px 30px 0px;">
            <p>填写宿舍意向</p>
          </el-card>
          <p style="margin-top: 50px;font-size: 18px;font-weight: 550;">欢迎您</p>
          <div class="w">
          <p>欢迎新同学加入广州软件学院大家庭!根据我校学生宿舍实际情况，宿舍分空调四人间(住宿费3500元/人/年)、空调六人间(住宿费1500元/人/年)两种类型，为做好学生宿舍入住工作,请同学根据流程进行选择宿舍类型，两种类型的床位数量都固定，造循先选先得原则，如某个类型的宿舍已选完，则只能选择另一个类型的宿舍。 如未在指定时间内进行选择，则由学校统筹分配，谢谢配合!</p>
          <p>请选择意向，确认后不可重新选择</p>
          <div class="mb-2 ml-4" style="display: flex;padding: 0 110px 0 110px;width: 107px">
            <el-radio-group v-model="radio">
              <el-radio value="1" size="large">空调四人间</el-radio>
              <el-radio value="2" size="large">空调六人间</el-radio>
            </el-radio-group>
          </div>
          </div>
          <el-button style="background: linear-gradient(to top right,#F7EBE1,#f7DFF1)" @click="jump" v-loading.fullscreen.lock="fullscreenLoading">确认</el-button>
        </el-card>

      </el-main>
    </el-container>
  </div>
</template>

<script>
import router from "@/router";
import { UserFilled, ArrowDown } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

export default {
name: "SelectDormView",
data(){
  return{
    radio:'',
    fullscreenLoading:false,
    UserFilled,
    ArrowDown,
    studentNumber: window.sessionStorage.getItem('studentNumber') || '未知用户'
  };
  },
  methods: {
    jump(){
      this.$confirm('是否选择完成?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '已完成!',
        })
        setTimeout(() => {
          this.fullscreenLoading = true;
        }, 3000,)
        setTimeout(() => {
          this.fullscreenLoading = false;
          router.push('/freshmanreport');
        }, 4000,)
        ;
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消'
        });
      });

    },
    goBack(){
      router.push('/freshmanreport')
    },
    toStudentHome() {
      router.push('/shome');
    },
    handleLogout() {
      // 清除会话存储中的所有信息
      window.sessionStorage.removeItem('token');
      window.sessionStorage.removeItem('role');
      window.sessionStorage.removeItem('userRole');
      window.sessionStorage.removeItem('studentNumber');
      window.sessionStorage.removeItem('studentName');
      
      ElMessage.success('已成功退出系统');
      
      // 跳转到登录页
      router.push('/login');
    }
  },
}
</script>

<style scoped>
/*文字*/
.w p{
  margin-bottom: 10px;
  font-size: 15px;
  line-height: 25px;
  text-align: justify;
  color: #333333;
  text-indent: 2em;
  padding: 10px 80px 0 80px;
}

/* 用户头像样式 */
.user-avatar {
  display: flex;
  align-items: center;
  margin-right: 20px;
  height: 60px;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.user-avatar .el-avatar {
  background-color: #fff;
  color: #244cb2;
  margin-right: 5px;
  transition: transform 0.3s;
}

.user-avatar:hover .el-avatar {
  transform: scale(1.1);
}

/* 下拉菜单箭头颜色 */
.el-icon--right {
  color: white;
}
</style>