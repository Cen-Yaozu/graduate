<template>
  <div class="common-layout">
    <el-container>
      <el-aside>
        <el-menu
            router
            class="el-menu-vertical-demo"
            :collapse="isCollapse"
            default-active="/calendar"
        >
          <router-link to="/calendar" class="logo">
            <img src="../../assets/img/sh-logo1.png"/>
            <h1 v-show="!isCollapse">广州软件学院</h1>
          </router-link>
          <el-menu-item index="/calendar">
            <el-icon><Calendar /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/sinformation">
            <el-icon><User /></el-icon>
            <span>个人信息</span>
          </el-menu-item>
          <el-menu-item index="/dorm">
            <el-icon><House /></el-icon>
            <span>宿舍管理</span>
          </el-menu-item>
          <el-menu-item index="/payment">
            <el-icon><Money /></el-icon>
            <span>缴费管理</span>
          </el-menu-item>
          <el-menu-item index="/class">
            <el-icon><School /></el-icon>
            <span>班级管理</span>
          </el-menu-item>
          <el-menu-item index="/arrival">
            <el-icon><Location /></el-icon>
            <span>抵校管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
          <el-icon @click="isCollapse=!isCollapse">
            <Expand v-show="isCollapse"/>
            <Fold v-show="!isCollapse"/>
          </el-icon>

          <el-breadcrumb separator="/" style="margin-left: 17px;">
            <el-breadcrumb-item v-for="(item,index) in $route.matched"
            :key="index"
            :to="{path:item.path}"
            >{{ item.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
          <el-dropdown>
    <span class="el-dropdown-link">
      <el-avatar :size="30" :src="circleUrl" />
      <el-icon class="el-icon--right">
        <arrow-down />
      </el-icon>
    </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>{{ studentNumber }}</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-header>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus';
import router from '@/router';
import { Calendar, User, House, Money, School, Location, Expand, Fold, ArrowDown } from '@element-plus/icons-vue';

export default {
name: 'StudentSHomeView',
  data(){
  return{
    isCollapse:false,
    circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
    studentNumber: window.sessionStorage.getItem('studentNumber') || '未知用户'
  }
  },
  components: {
    Calendar, User, House, Money, School, Location, Expand, Fold, ArrowDown
  },
  methods: {
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
.el-aside{
  background: #eee;
  height: 100vh;
  width: auto;
}
.el-menu{
  width: 60px;
  min-height: 400px;
  background: #eee;
}
.el-menu-vertical-demo:not(.el-menu--collapse){
  background: #eee;
  border-right: none;
  min-height: 400px;
  width: 200px;
}
.el-menu-vertical-demo+h1:not(.el-menu--collapse){
  display: initial;
}

.logo{
  display: flex;
  justify-content: center;
  align-items: center;
  text-decoration: none;
  color: black;
}
  .logo img{
    display: flex;
    width: 50px;
  }
  .el-header{
    display: flex;
    align-items: center;
    background-color: #eee;
  }
.el-dropdown{
    margin-left: auto;
  }

</style>