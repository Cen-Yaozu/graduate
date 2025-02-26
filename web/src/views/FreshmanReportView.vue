<template>
  <div class="bg"></div>
    <el-container>
     <el-header style="padding: 0;height: auto;background-color:#244cb2">
      <el-menu
          router
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"
          :ellipsis="false"
          style="background-color: #244cb2;max-width: 90%;border-bottom: none"
        >
       <el-menu-item index="0" >
        <img src="../assets/img/logo.png" style="height: auto; max-width: 100%">
       </el-menu-item>
        <div class="user-avatar" @click="toAdmin">
          <el-avatar :size="40" :icon="UserFilled" />
        </div>
      </el-menu>
     </el-header>
     <div class="box">
      <div class="left">
        <p>校内资源</p >
      </div>
      <div class="right">
        <div class="title" style="font-size: 17px;font-weight: 550;display: flex">校内资源</div>
        <hr/>
        <div class="list">
          <div class="item" @click="toDorm">
            <div class="item_img">
              <img src="../assets/img/freport_p3.png">
            </div>
            <div class="item_com">
              <p>宿舍选择</p >
              <p>Dorm selection</p >
            </div>
          </div>
          <div class="item" @click="toPay">
            <div class="item_img">
              <img src="../assets/img/freport_p4.png">
            </div>
            <div class="item_com">
              <p>费用缴纳</p >
              <p>Fee payment</p >
            </div>
          </div>
          <div class="item" @click="toreport">
            <div class="item_img">
              <img src="../assets/img/freport_p1.png"/>
            </div>
            <div class="item_com">
              <p>新生报道</p >
              <p>Student report</p >
            </div>
          </div>
        </div>
      </div>
     </div>
    </el-container>
</template>

 <script>
import router from "@/router";
import { UserFilled } from '@element-plus/icons-vue';

export default {
  name: "FreshmanReportView",
  data(){
    return{
      isPlaying:false,
      activeName:'first',
      activeIndex:"1",
      UserFilled
    }
  },
  methods:{
    toDorm(){
      router.push('/selectdorm')
    },
    toPay(){
      router.push('/pay')
    },
    toreport(){
      router.push('/freshman')
    },
    toAdmin(){
      const role = window.sessionStorage.getItem('roles')
      if(role === 'ROLE_STUDENT') {
        router.push('/shome')
      } else {
        router.push('/ahome')
      }
    }
  }
}
</script>

 <style scoped>
 .bg {
   display: block;
   position: fixed;
   background:linear-gradient(to bottom,#4491c6,white);
   background-position-x: 0%;
   background-position-y: 0%;
   background-repeat: repeat;
   background-size: auto;
   background-repeat: no-repeat;
   background-size: cover;
   background-position: center;
   top: 0px;
   height: 100%;
   width: 100%;
   z-index: -10;
   overflow: hidden;
 }
 /*导航栏*/
 .el-menu--horizontal > .el-menu-item:nth-child(1) {
    margin-right: auto;
    height: 80px;
    max-width: 240px;
    align-items: center;
    display: flex;
    position: relative;
   }
 .el-menu--horizontal >.el-menu-item:nth-child(n):hover{
    background-color: #244cb2;
   }
 .el-menu--horizontal >.el-menu-item:nth-child(n):active{
    background-color: #244cb2;
   }
 .el-menu-demo .el-menu-item.is-active,
.el-menu-demo .el-submenu__title.is-active {
   background-color: transparent !important; /* 覆盖默认的背景颜色 */
   color: inherit !important; /* 如果你想保持文字颜色不变 */
}
 ::v-deep .el-menu--horizontal {
    height: 80px;
    margin: 0 auto;
    border-bottom: none;
   }
 a{
    text-decoration: none;
   }
 ::v-deep .el-menu--horizontal>.el-menu-item.is-active{
   border-bottom: none;
 }

/* 用户头像样式 */
.user-avatar {
  display: flex;
  align-items: center;
  margin-right: 20px;
  cursor: pointer;
  height: 80px;
}

.user-avatar .el-avatar {
  background-color: #fff;
  color: #244cb2;
  transition: transform 0.3s;
}

.user-avatar:hover .el-avatar {
  transform: scale(1.1);
}

/*box*/
 .box{
   background: white;
    width: 60%;
    height: 480px;
    margin-left: 20%;
    margin-top: 5%;
    display: flex;
    flex-direction: row;
    border-radius: 10px;
   }
 .left{
    width: 20%;
    padding: 45px 0;
    border-right: rgba(0, 0, 0, 0.151) 1px solid;
   }
 .left p{
   line-height: 40px;
   text-align: center;
   color: #638fff;
   border-left: #6f98ff 3px solid;
   background: #dbf4ff;
   font-weight: 550;
   }
 .right{
    width: 80%;
    padding: 3% 5%;
    display: flex;
    flex-direction: column;
    align-content: center;
   }
 .right hr{
    width: 20px;
    border: 0;
    height: 3px;
    background-color: #0048ff;
    margin-top: 3px;
    margin-left: 1px;
   }
 .list{
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    grid-template-rows: repeat(2,76px);
    grid-gap: 20px;
   margin-top: 15px;
   }
 .item{
    display: flex;
    flex-direction: row;
    border: rgba(0, 0, 0, 0.142) solid 1px;
    padding: 5%;
    cursor: pointer;
    transition: all 0.3s;
   }
 .item:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    transform: translateY(-3px);
 }
 .item_img{
    width: 30%;
    align-content: center;
  margin: 5px;
   }
 .item_img img{
    width: 100%;
   margin: -5px auto;
   }
 .item_com{
    display: flex;
    flex-direction: column;
    margin: 10px auto;
    align-content: center;
   }
 .item_com p:nth-child(1){
    margin: 2px;
   }
 .item_com p:nth-child(2){
    font-size: 13px;
    color: rgba(0, 0, 0, 0.544);
   }
 a{
   text-decoration: none;
 }
 </style>