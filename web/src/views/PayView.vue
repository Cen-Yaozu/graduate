<template>
  <el-container>
    <el-header style="background-color: #244cb2">
      <el-page-header @back="goBack" style="font-size: 60px;color: white">
        <template #content>
          <span class="text-large font-600 mr-3" style="font-weight: 800;color: white">费用缴纳</span>
        </template>
      </el-page-header>
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

export default {

  name: "PayView",
  data(){
    return{
      isPlaying:false,
      activeName:'first',
      activeIndex:"1",
      fullscreenLoading: false
    }
  },
  methods:{
    goBack(){
      router.push('/freshmanreport')
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
</style>