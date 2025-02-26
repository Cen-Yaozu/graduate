<template>
  <el-container>
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
      </el-menu>
    </el-header>
    <el-main style="padding: 0;">
      <div class="ban" style="position: relative">
        <img class="imged" src="../assets/img/hot_p1.jpg" style="background-size: cover;width: 100%">
      </div>
    </el-main>
  </el-container>
  <div class="common-layout" style="margin: 0 auto;max-width: 75%">
    <el-container>
      <el-main>
        <el-tabs
            v-model="activeName"
            type="card"
            class="demo-tabs"
            tab-position="left"
            @tab-click="handleClick"
        >
          <el-tab-pane label="学习生活篇" name="first">
            <h3 style="font-size: 28px;font-weight: bold;font-family: SimHei">学习生活篇</h3>
            <div class="xxjj" style="margin-top: 25px">
              <h2 style="display: flex">1、什么时候开学报到？什么时候入学教育？</h2>
              <br>
              <div class="cont">
                <p>24级专升本新生报到注册时间为：8月23日</p>
                <p>24级普高本科新生报到注册时间为：9月7日</p>
                <p>新生入学教育：9月8日-9月10日</p>
                <p>军训时间：9月11日-9月24日</p>
                <p>正式上课时间：9月25日开始</p>
              </div>
              <br>
              <h2 style="display: flex">2、学校有哪些院系？各专业在哪些院系？</h2>
              <br>
              <div class="cont">
                <p>软件工程系、网络技术系、电子系、计算机系、游戏系、数码媒体系、外语系和数字经济与管理学院</p>
                <div class="fl02 flew">
                  <img src="../assets/img/hot_p2.jpg" width="80%"/>
                </div>
              </div>
              <br>
              <h2 style="display: flex">3、请问新生报到时需要带什么？</h2>
              <br>
              <div class="cont">
                <p>专升本新生：录取通知书、身份证、普通专科(离职)毕业证书、密封纸质档案(应届专科生凭调档函调档)、免冠红底小一寸彩色近照10张、大一寸彩色近照5张。</p>
                <p>专升本新生：录取通知书、身份证、普通专科(离职)毕业证书、密封纸质档案(应届专科生凭调档函调档)、免冠红底小一寸彩色近照10张、大一寸彩色近照5张。</p>
                <br>
                <p style="font-size: initial;color: red">注: 请各位新生提前准备好个人电子证件照(纯色背景，不化浓妆着装避免过于鲜艳或复杂的图案，照片不得修饰或PS处理)，尺寸为: 宽480px、高640px，大小不超过100KB，以jpg格式保存。开学后将由教务处统一收取。</p>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="校园生活篇" name="second">
            <h3 style="font-size: 28px;font-weight: bold;font-family: SimHei">校园生活篇</h3>
            <div class="wlzy" style="margin-top: 25px">
              <br>
              <h2 style="display: flex">1.广软办事大厅</h2>
              <br>
              <h2 style="display: flex">http://ehall.seig.edu.cn</h2>
              <br>
              <br>
              <div class="cont">
                <p>
                  新生经统一身份认证后，能通过办事大厅直达学院各类应用系统。校园学习生活的众多事务（如请假、出入校园、证件补办、盖章、课程代替和修改密码等)都在网上一键办理。
                </p>
              </div>
              <br>
              <h2 style="display: flex">2.广软导航网</h2>
              <br>
              <br>
              <h2 style="display: flex">http://home.seig.edu.cn/</h2>
              <br>
              <div class="cont">
                <p>
                  收录了广软所有网址，一网导航，方便查找。当然，下面的常用网址都可以在里面找到。
                </p>
              </div>
              <br>
              <h2 style="display: flex">3.Myseig系统</h2>
              <br>
              <br>
              <h2 style="display: flex">http://stu.seig.edu.cn/</h2>
              <br>
              <div class="cont">
                <p>
                  系统登陆账号为学号，初始密码待定。点击“信息管理系统”，输入同样的账号密码，即可进入信息管理系统。信息管理系统可查看课程表、考试成绩、选课等等。
                </p>
              </div>
              <br>
              <h2 style="display: flex">4.广软邮箱</h2>
              <br>
              <br>
              <h2 style="display: flex">https://smail.seig.edu.cn</h2>
              <br>
              <div class="cont">
                <p>
                  学校的各种通知，会通过广软邮箱发送。邮箱账号在信息管理系统里面可以查到，密码是身份证的后8位，登录后尽快更改密码。
                </p>
              </div>
              <br>
              <h2 style="display: flex">5.广软企业微信</h2>
              <br>
              <br>
              <div class="cont">
                <p>
                  学生登入学校企业微信后能访问广软办事大厅移动端、健康上报、宿舍设施（非网络类）报修、宿舍网络报修、班级群通知等应用。
                </p>
              </div>
              <br>
              <h2 style="display: flex">6.其它常用内网还有</h2>
              <br>
              <br>
              <div class="cont">
                <p>网协首页：</p>
                <p>http://na.seig.edu.cn/</p>
                <p>计协主页</p>
                <p>http://ca.seig.edu.cn/ 等等。</p>
              </div>
              <br>
              <h2 style="display: flex">7.广软常用FTP地址及账号密码：</h2>
              <br>
              <br>
              <div class="cont">
                <p>
                  课件下载ftp://kj.seig.edu.cn、ftp://kj1.seig.edu.cn(kj1为数码系、计算机系、游戏系及学生处、教保处等教辅用)和ftp://kj2.seig.edu.cn(k2为软件系和网络系使用)（该服务器存放课件作业等资料)
                </p>
                <p>下载账号/密码：kjdown/kjdown</p>
                <p>上传账号/密码：upload_****/upload_****（“****”为老师工号)</p>
                <p>软件应用下载172.16.3.230（该服务器存放共享软件/电子/教程/图书等资料）</p>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="个人发展" name="third">
            <h3 style="font-size: 28px;font-weight: bold;font-family: SimHei">个人发展</h3>
            <div class="xljk" style="margin-top: 25px">
              <div class="cont">
                <p>我校心理健康教育与素质拓展中心（简称“心理中心”)成立于2005年，隶属于学生处。心理中心兼备心理健康教育及心理咨询服务功能，专门维护和促进学生心理健康，提升学生心理素质。学校心理中学紧密围绕“立德树人”根本任务，坚持“以生为本”的教育理念，积极打造富有学校特色的大学生心理健康教育工作体系，切实促进学生的健康成长。在心理活动的开展上，我们构建了“3+1”心理活动体系("3"是5·25心理健康月、大学生综合素质拓展月和心理知识竞赛，"1"是指心灵成长坊，含心理沙龙、心理电影赏析、心灵关怀活动、专题心理讲座等)；我们构建“五位一体”心理育人格局，注重个案心理辅导和心理健康普及教育相结合，活动与专业教育和校园文化建设结合，为广大师生提供心理健康科普、合理宣泄情绪、扩宽人际交往圈子等机会。此外，近年来心理中心积极组织我校师生参加省级心理健康类活动大赛，屡获佳绩。</p>
                <div class="fl02 flew">
                  <img src="../assets/img/life_p3.jpg"/>
                </div>
                <div class="cont">
                  <p>特色活动：5·25大学生心理健康月、大学生综合素质拓展训练营、心理知识竞赛、专题心理沙龙</p>
                  <p>心理中心网站：</p>
                  <p>https://xl.psyedu.cn/seig/default</p>
                  <p>心理咨询预约方式（现场/热线电话/公众号/微信/QQ)如下：</p>
                  <p>电话预约：24小时热线电话020-87818381</p>
                  <p>微信预约：搜“grzxyy580”,实名添加微信，提出预约需求</p>
                  <p>公众号预约：搜“广州软件学院心理中心”并关注，点击“咨询预约”并填写信息</p>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="其他常见问题" name="four">
            <h3 style="font-size: 28px;font-weight: bold;font-family: SimHei">其他常见问题</h3>
            <div class="xxjj" style="margin-top: 25px">
              <div class="cont">
                <p>
                  负责高考考生、本校考生、创新创业团队的咨询服务工作。咨询团队由志愿填报咨询师、生涯规划师、职业指导师、创业咨询师等组成，专兼职相结合，校内外不同社会层次人士共同参与，各有侧重，优势互补，旨在为学生提供个性化的咨询服务，引导学生根据兴趣、性格、能力、社会现实、家庭情况等在求学、就业、创业、职业生涯规划等方面做出合理的选择，帮助学生实现理想的求学、就业、创业梦想，实现更好的个人发展。
                </p>
                <div class="fl02 flew">
                  <img src="../assets/img/life_p4.png"/>
                </div>
                <div class="cont">
                  <p>办公地点：行政办公楼101室</p>
                  <p>就业网：</p>
                  <p>http://job.seig.edu.cn/</p>
                  <p>创新创业学院网：</p>
                  <p>http://cie.seig.edu.cn/</p>
                  <p>服务理念：让每一位来访者绽放更自信的笑容！</p>
                  <p>特色活动：生涯体验周、职前礼仪培训训练营、职业生涯规划比赛、简历制作比赛、模拟面试大赛、就业创业服务月、"书博职涯"咨询活动</p>
                  <p>咨询预约方式（现场/热线电话/公众号/微信/QQ)如下：</p>
                  <p>现场预约：前往图书馆一楼111书博职涯咨询室（周一至五工作时间均可）</p>
                  <p>热线电话预约：020-87818718 87818012</p>
                  <p>公众号预约：搜“广州软件学院就业服务”并关注，点击“毕业生”，点击“预约辅导”并填写信息</p>
                  <p>微信预约：搜“19886338718”，实名添加微信，提出预约需求</p>
                  <p>QQ预约：3213964059</p>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "HotView",
  data(){
    return{
      activeName:'first',
      activeIndex:"3"
    }
  },
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
/*标签页*/
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
::v-deep .el-tabs--left .el-tabs__nav-wrap.is-left{
  box-shadow: 0px 5px 10px 2px rgba(0, 0, 0, 0.1);
}
::v-deep .el-tabs--left.el-tabs--card .el-tabs__nav{
  height: 480px;
  background: url("../assets/img/navbg.png") no-repeat bottom #fff;
  width: 150px;
}
::v-deep .el-tabs__item:nth-child(n){
  height: 52px;
  /*border-bottom: 1px dashed rgba(57, 79, 135, 0.4);*/
  font-size: 15px;
  line-height: 1.2;
  font-weight: 600;
}
::v-deep .el-tabs--left .el-tabs__item.is-left, .el-tabs--right .el-tabs__item.is-left{
  justify-content: center;
}
::v-deep .el-tabs--left.el-tabs--card .el-tabs__item.is-left:nth-child(n){
  border-bottom:1px dashed rgba(57, 79, 135, 0.4);
}
::v-deep .el-tabs__content{
  padding: 15px 0 0 30px;
}
.xxjj .cont p{
  margin-bottom: 10px;
  font-size: 21px;
  line-height: 25px;
  text-align: justify;
  color: #333333;
  text-indent: 2em;
}
.wlzy .cont p{
  margin-bottom: 10px;
  font-size: 21px;
  line-height: 25px;
  text-align: justify;
  color: #333333;
  text-indent: 2em;
}
.xljk .cont p{
  margin-bottom: 10px;
  font-size: 21px;
  line-height: 25px;
  text-align: justify;
  color: #333333;
  text-indent: 2em;
}
</style>