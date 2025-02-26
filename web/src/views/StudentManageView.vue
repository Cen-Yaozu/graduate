<template>
  <el-container>
    <el-header style="background-color: #244cb2">
      <el-page-header @back="goBack" style="font-size: 60px;color: white">
        <template #content>
          <span class="text-large font-600 mr-3" style="font-weight: 800;color: white">学生管理系统</span>
        </template>
      </el-page-header>
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
          <el-menu-item index="/student/info">
            <el-icon><User /></el-icon>
            <template #title>个人信息管理</template>
          </el-menu-item>
          <el-menu-item index="/student/dorm">
            <el-icon><House /></el-icon>
            <template #title>宿舍管理</template>
          </el-menu-item>
          <el-menu-item index="/student/payment">
            <el-icon><Wallet /></el-icon>
            <template #title>缴费管理</template>
          </el-menu-item>
          <el-menu-item index="/student/class">
            <el-icon><School /></el-icon>
            <template #title>班级管理</template>
          </el-menu-item>
          <el-menu-item index="/student/arrival">
            <el-icon><Location /></el-icon>
            <template #title>抵校管理</template>
          </el-menu-item>
          <el-menu-item index="/student/home">
            <el-icon><House /></el-icon>
            <template #title>首页</template>
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
import { User, House, Wallet, School, Location } from '@element-plus/icons-vue'

export default {
  name: "StudentManageView",
  components: {
    User,
    House,
    Wallet,
    School,
    Location
  },
  data() {
    return {
      isCollapse: false
    }
  },
  methods: {
    goBack() {
      router.push('/student')
    }
  }
}
</script>

<style scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}
</style>