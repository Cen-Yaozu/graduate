<template>
  <el-container>
    <el-header style="background-color: #244cb2; display: flex; align-items: center; justify-content: space-between;">
      <el-page-header @back="goBack" style="color: white">
        <template #content>
          <span class="text-large font-600 mr-3" style="font-weight: 800;color: white">新生报道</span>
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
    <el-container>
      <el-aside width="200px" style="height: 640px;">
        <el-menu
            router
            default-active="/"
            class="el-menu-vertical-demo"
            :collapse="isCollapse=false"
            style="height: 100%"
        >
          <el-menu-item index="/photo">
            <el-icon><Picture /></el-icon>
            <template #title>照片采集</template>
          </el-menu-item>
          <el-menu-item index="/information">
            <el-icon><User /></el-icon>
            <template #title>信息采集</template>
          </el-menu-item>
          <el-menu-item index="/arrive">
            <el-icon><Location /></el-icon>
            <template #title>到站登记</template>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
import router from "@/router";
import { UserFilled, ArrowDown, Picture, User, Location } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

export default {
 name: "FreshmenView",
 data(){
  return{
    isCollapse:true,
    UserFilled,
    ArrowDown,
    Picture,
    User,
    Location,
    studentNumber: window.sessionStorage.getItem('studentNumber') || '未知用户'
    }
  },
  created() {

  },
  mounted() {

  },
  methods: {
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
  }
}
</script>

<style scoped>
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