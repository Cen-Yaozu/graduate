<template>
  <el-container>
    <el-header style="background-color: #244cb2; display: flex; align-items: center; justify-content: space-between;">
      <el-page-header @back="goBack" style="color: white">
        <template #content>
          <span class="text-large font-600 mr-3" style="font-weight: 800;color: white">费用缴纳</span>
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
    <el-main style="padding: 0;">
      <el-card style="width: 90%;height: 100%;background: linear-gradient(to top right,rgba(90,149,207,.5),#f5f7fa);margin: 40px auto;">
        <el-descriptions
            title="广州软件学院综合收费"
            direction="horizontal"
            :column="1"
            align="center"
            border
        >
          <el-descriptions-item label="缴纳项目">广州软件学院综合收费</el-descriptions-item>
          <el-descriptions-item label="缴纳状态">未缴费</el-descriptions-item>
          <el-descriptions-item label="收费机构">广州软件学院</el-descriptions-item>
          <el-descriptions-item label="联系方式">87818009</el-descriptions-item>
          <el-descriptions-item label="支付方式">微信支付</el-descriptions-item>
          <el-descriptions-item label="缴费编号">E3032200020240821G19570001</el-descriptions-item>
          <el-descriptions-item label="用户名称">梁xx</el-descriptions-item>
          <el-descriptions-item label="学费">10</el-descriptions-item>
          <el-descriptions-item label="宿舍费">10</el-descriptions-item>
          <el-descriptions-item label="医保">10</el-descriptions-item>
          <el-descriptions-item label="缴费总金额">30</el-descriptions-item>
        </el-descriptions>
        <el-button style="background: #98B6EC;margin: 16px auto auto;padding: 0 44px" @click="pay" v-loading.fullscreen.lock="fullscreenLoading">确认支付</el-button>
      </el-card>
    </el-main>
  </el-container>
</template>

<script>
import router from "@/router";
import { UserFilled, ArrowDown } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

export default {

  name: "PayView",
  data(){
    return{
      isPlaying:false,
      activeName:'first',
      activeIndex:"1",
      fullscreenLoading: false,
      UserFilled,
      ArrowDown,
      studentNumber: window.sessionStorage.getItem('studentNumber') || '未知用户'
    }
  },
  methods:{
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
    },
    pay() {
      this.$confirm('是否确认支付?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '支付成功!',
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
          message: '已取消支付'
        });
      });

    },
    jump(){
      router.push('/freshmanreport');
    }
  }
}
</script>

<style scoped>
/*表格*/
::v-deep .el-descriptions__header{
  justify-content: center;
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