<template>
  <el-container>
    <el-header style="background-color: #244cb2">
      <el-page-header @back="goBack" style="font-size: 60px;color: white">
        <template #content>
          <span class="text-large font-600 mr-3" style="font-weight: 800;color: white">新生报道</span>
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

export default {
 name: "FreshmenView",
 data(){
  return{
    isCollapse:true,
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
  }
}
</script>

<style scoped>

</style>