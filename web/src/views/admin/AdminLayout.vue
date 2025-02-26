<template>
  <div class="admin-layout">
    <el-container>
      <el-aside width="200px">
        <div class="logo">教育管理系统</div>
        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          router
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><el-icon-odometer /></el-icon>
            <span>首页仪表盘</span>
          </el-menu-item>
          <el-menu-item index="/admin/students">
            <el-icon><el-icon-user /></el-icon>
            <span>学生管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/payments">
            <el-icon><el-icon-money /></el-icon>
            <span>缴费管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/classes">
            <el-icon><el-icon-office-building /></el-icon>
            <span>班级管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/dormitories">
            <el-icon><el-icon-house /></el-icon>
            <span>宿舍管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/teachers">
            <el-icon><el-icon-user /></el-icon>
            <span>教师管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/reports">
            <el-icon><el-icon-document /></el-icon>
            <span>报表管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/calendar">
            <el-icon><el-icon-calendar /></el-icon>
            <span>校园日历</span>
          </el-menu-item>
          <el-menu-item index="/admin/password">
            <el-icon><el-icon-key /></el-icon>
            <span>修改密码</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
          <div class="header-right">
            <span>管理员</span>
            <el-dropdown>
              <span class="el-dropdown-link">
                操作<el-icon class="el-icon--right"><el-icon-arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

export default {
  name: 'AdminLayout',
  setup() {
    const router = useRouter()
    const route = useRoute()

    const activeMenu = computed(() => {
      return route.path
    })

    const handleLogout = () => {
      window.sessionStorage.removeItem('token')
      window.sessionStorage.removeItem('userRole')
      router.push('/login')
    }

    return {
      activeMenu,
      handleLogout
    }
  }
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}

.el-header {
  background-color: #fff;
  color: #333;
  line-height: 60px;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.el-aside {
  background-color: #304156;
  color: #bfcbd9;
}

.el-menu {
  border-right: none;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
  background-color: #263445;
}

.header-right {
  display: flex;
  align-items: center;
}

.header-right span {
  margin-right: 15px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}
</style> 