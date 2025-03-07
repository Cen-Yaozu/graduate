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
import { ElMessage } from 'element-plus'

export default {
  name: 'AdminLayout',
  setup() {
    const router = useRouter()
    const route = useRoute()

    const activeMenu = computed(() => {
      return route.path
    })

    const handleLogout = () => {
      // 清除会话存储中的所有信息
      window.sessionStorage.removeItem('token')
      window.sessionStorage.removeItem('role')
      window.sessionStorage.removeItem('userRole')
      window.sessionStorage.removeItem('studentNumber')
      window.sessionStorage.removeItem('studentName')
      
      ElMessage.success('已成功退出系统')
      
      // 跳转到登录页
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
  width: 100%;
  overflow: hidden;
}

.el-container {
  height: 100%;
}

.el-header {
  background-color: #fff;
  color: #333;
  line-height: 60px;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0 20px;
}

.el-aside {
  background-color: #304156;
  color: #bfcbd9;
  height: 100vh;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  position: relative;
  z-index: 10;
  overflow-y: auto;
}

.el-menu {
  border-right: none;
  height: calc(100vh - 60px);
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  color: #fff;
  background-color: #263445;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.1);
  position: relative;
  z-index: 1;
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

/* 侧边栏菜单项样式优化 */
:deep(.el-menu-item) {
  height: 56px;
  line-height: 56px;
}

:deep(.el-menu-item.is-active) {
  background-color: #263445 !important;
  border-left: 4px solid #409EFF;
}

:deep(.el-menu-item:hover) {
  background-color: #263445 !important;
}
</style> 