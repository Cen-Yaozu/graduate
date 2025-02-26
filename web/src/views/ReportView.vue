<template>
  <el-header style="padding: 0;height: auto;background-color:#244cb2">
    <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        :ellipsis="false"
        style="background-color: #244cb2;max-width: 90%;border-bottom: none"
    >
      <el-menu-item index="0">
        <img src="../assets/img/logo.png" style="height: auto;max-width: 100%">
      </el-menu-item>
      <el-menu-item index="1"><router-link to="/">首页</router-link> </el-menu-item>
      <el-menu-item index="2"><router-link to="/life">生活指南</router-link> </el-menu-item>
      <el-menu-item index="3"><router-link to="/hot">热点问题</router-link> </el-menu-item>
      <el-menu-item index="4"><router-link to="/report">新生报道</router-link> </el-menu-item>
      <el-menu-item index="5"><router-link to="/admin">后台管理</router-link> </el-menu-item>
    </el-menu>
  </el-header>
  <el-main>
    <table align="center" width="100%">
      <tbody><tr>
        <td align="center">
            <img src="../assets/img/report_p1.png" @click="toreport" style="border: 0;" width="300">
        </td>
      </tr>
      </tbody></table>
    <div align="center" style="font-size:26px;color:red;">提示：点击上方“新生报到”后，请阅读页面左侧“新生须知”，先完成<span style="    font-weight: bold;">账号激活</span>！<br>激活完成后使用系统生成学号重新登录，进行后续信息采集和学费缴纳。</div>
  </el-main>
</template>

<script>
export default {
  name: "ReportView",
  data(){
    return{
      activeIndex:"4"
    }
  },
  methods:{
    toreport(){
      let { href } = this.$router.resolve('/login')
      window.open(href,'_blank')
    },
  }
}
</script>

<style scoped>
/*导航栏*/
.el-menu--horizontal > .el-menu-item:nth-child(1) {
  margin-right: auto;
  height: 80px;
  max-width: 240px;
  align-items: center;
  display: flex;
  position: relative;
  align-items: center;
}
.el-menu--horizontal >.el-menu-item:nth-child(n+1){
  font-weight: bold;
  font-size: 20px;
  color: #fff;
}
.el-menu--horizontal >.el-menu-item:nth-child(1):hover{
  background-color: #244cb2;
}
::v-deep .el-menu--horizontal {
  height: 80px;
  margin: 0 auto;
  border-bottom: none;
}
a{
  text-decoration: none;
}
/*图片*/
.main_con {
  width: 100%;
  margin: 20px auto 0;
  height: auto;
  overflow: hidden;
}
</style>