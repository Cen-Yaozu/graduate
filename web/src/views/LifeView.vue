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
        <img class="imged" src="../assets/img/life_p1.jpg" style="background-size: cover;width: 100%">
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
          <el-tab-pane label="生活服务" name="first">
            <h3 style="font-size: 28px;font-weight: bold;font-family: SimHei">生活服务</h3>
            <br>
            <h2 style="display: flex">01 住宿</h2>
            <div class="xxjj" style="margin-top: 25px">
              <div class="cont">
                <p>
                  学校学生公寓套间配置包括休息间、卫生间、阳台组成，宿舍内还配备了电热水器、电风扇、洗衣机、空调等设备。所在专业的宿舍具体楼栋由学校统一安排，具体宿舍房号则在报到后的缴费凭证里，床位由同一宿舍学生内部自行安排。记得按时登录相关网址选择宿舍。
                </p>
              </div>
                <div class="cont">
                  <p>
                    男生宿舍区：黄槐楼（Y1、Y2、Y3）、青枫阁（M1、M2、M3）、绿杨楼（G、P）、红棉楼（R）、碧波楼（B），书新楼（Q）。
                  </p>
                  <p>
                    女生宿舍区：紫薇楼（J1、J2）、橙萱居（T1、T2）、金桂园（K1、K2）、蓝楹楼（F）、尚品书院楼（V）。
                  </p>
                  <p>
                    宿舍管理负责：张老师、欧阳老师，联系电话：020-87818610
                  </p>
                </div>
              <br>
              <h2 style="display: flex">02 金融服务</h2>
              <br>
              <br>
              <div class="cont">
                <p>
                  师生服务大厅有中国工商银行柜员机1台，方便学生存取款。距学校不远的太平镇（或从化经济开发区）有中国银行、中国工商银行、中国农业银行、中国建设银行、中国邮政银行等多家金融机构，方便办理各种金融服务。
                </p>
                <p>
                  校内餐饮、超市均可用微信、校园一卡通，校园一卡通24小时可用微信小程序自助充值
                </p>
              </div>
              <br>
              <h2 style="display: flex">03 报修服务</h2>
              <br>
              <br>
              <div class="cont">
                <h1 style="display: flex;font-weight: 500;font-size: 20px">1) 宿舍报修</h1>
                <br>
                <p>
                  登录学校企业微信—工作台—审批—总务报修填写相关信息提交报修。
                </p>
                <h1 style="display: flex;font-weight: 500;font-size: 20px">2) 紧急报修</h1>
                <br>
                <p>
                  校园紧急报修服务（遇水管爆裂、用电中断等特殊情况），可联系020-87818805 进行紧急处理。
                </p>
                <h1 style="display: flex;font-weight: 500;font-size: 20px">3) 宿舍配匙</h1>
                <br>
                <p>
                  可到行政办公楼一楼师生服务大厅宿舍报修窗口办理缴费。流程：窗口缴费—票据交宿舍楼门岗—门岗通知领取门匙。
                </p>
              </div>
              <br>
              <h2 style="display: flex">04 水电费服务</h2>
              <br>
              <br>
              <div class="cont">
                <p>
                  宿舍使用智能水电系统，可以通过虚拟卡的小程序进行网上预交充值，并且可以在小程序上查看每天水电剩余情况。遇充值后不能正常使用等问题，可咨询020-87818804。可关注【广软校园卡】小程序-水电费充值等服务了解更多信息。
                </p>
              </div>
              <br>
              <h2 style="display: flex">05 商业服务</h2>
              <br>
              <br>
              <div class="cont">
                <p>
                  学校内设超市、印务中心（复印店）和理发店等学生商业服务点，为学生提供购物、印务、理发等多种服务，提供基本生活用品、日常食品、文具、水果等供应。
                </p>
              </div>
              <br>
              <h2 style="display: flex">06 饮食</h2>
              <br>
              <br>
              <div class="cont">
                <p>
                  校内有两个饭堂和一个美食廊，供应早、中、晚餐。消费可用校园一卡通、微信支付。
                </p>
              </div>
              <br>
              <h2 style="display: flex">07 智慧饭堂</h2>
              <br>
              <br>
              <div class="cont">
                <p>
                  免排长队购饭菜的烦恼，手机端自由点餐后，在每栋楼下就可以在智慧保温饭柜取餐。敬请关注【广软总务基建处】公众号。
                </p>
              </div>
              <br>
              <h2 style="display: flex">08 饮用水</h2>
              <br>
              <br>
              <div class="cont">
                <p>
                  学校超市（男生小卖部）设有桶装水，有订水或送水服务，地点：选择到学校超市（男生小卖部）或女生区每栋楼的水票箱投放水票。
                </p>
              </div>
              <br>
              <h2 style="display: flex">09 医务室</h2>
              <br>
              <br>
              <div class="cont">
                <p>
                  学校医务室实行24小时医生值班制度，开诊时间：8：30~22：30，其余时间为急诊。学校与从化120急救中心联网，发生突发事件，直接可进入特殊医疗保障渠道，保障学生的身体健康。学校医务室值班电话：020-87818807
                </p>
              </div>
              <br>
              <h2 style="display: flex">10 快递服务</h2>
              <br>
              <br>
              <div class="cont">
                <p>
                  学校内还设有专门收发快递的服务点——“快递驿站”（开放时间：9点～19点）。
                </p>
                <p>
                  取件操作流程：打开微信扫码关注公众号，获取身份码，按照货架编号取件并放在高拍仪指定区域，同时打开身份码扫描出库，完成取件。每年9月份设有新生快递专区：为了让全体新生轻装到校、轻松报到，校园快递驿站快递服务中心增设“新生快递包裹专窗”进行派件，具体操作如下：
                </p>
                <h1 style="display: flex;font-weight: 500;font-size: 20px">（1）寄出时间:</h1>
                <br>
                <p>
                  新生可根据报到时间和路途远近提前2—7天寄出，避免过早寄出的包裹（行李）因过时无人签领而退件。
                </p>
                <h1 style="display: flex;font-weight: 500;font-size: 20px">（2）包裹备注和保存时间:</h1>
                <br>
                <p>
                  寄包裹（行李）时，在备注栏内写上“新生”二字，校园快递服务中心会将包裹（行李）保留10天。
                </p>
                <h1 style="display: flex;font-weight: 500;font-size: 20px">（3）寄件地址与内容如下:</h1>
                <br>
                <p>
                  1．收件人：新生姓名                
                </p>
                <p>2．收件人电话：新生可正常接收信息的手机号码</p>
                <p>3．收件地址：广州市从化区经济技术开发区广从南路548号，广州软件学院（新生专区）</p>
                <h1 style="display: flex;font-weight: 500;font-size: 20px">（4）如有疑问，欢迎致电:</h1>
                <br>
                <p>
                  李经理13527808282（微电同号)
                </p>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="网络资源" name="second">
            <h3 style="font-size: 28px;font-weight: bold;font-family: SimHei">网络资源</h3>
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
          <el-tab-pane label="心理健康教育" name="third">
            <h3 style="font-size: 28px;font-weight: bold;font-family: SimHei">心理健康教育与咨询服务</h3>
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
          <el-tab-pane label="职业发展教育" name="four">
            <h3 style="font-size: 28px;font-weight: bold;font-family: SimHei">职业发展教育与咨询服务</h3>
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
          <el-tab-pane label="体育活动场所" name="five">
            <h3 style="font-size: 28px;font-weight: bold;font-family: SimHei">体育活动场所</h3>
            <div class="xxjj" style="margin-top: 25px">
              <div class="cont">
                <p>
                  体育活动场所：体育设施完备，拥有各类体育场所和体育器材。运动场地面积达25689平方米。现有标准人造草塑胶跑道运动场(包括足球场和400米环形跑道)1个、篮球12个、网球场2个、室外乒乓球桌4张、室外羽毛球场13个、体育馆（室内羽毛球场地、乒乓球场地)、排球场2个，尚品书院楼（3个羽毛球场、3个室内乒乓球场)。
                </p>
              </div>
                <div class="fl02 flew">
                  <img src="../assets/img/life_p5.jpg"/>
                </div>
              <div class="fl02 flew">
                <img src="../assets/img/life_p6.jpg"/>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="个人安全" name="six">
            <h3 style="font-size: 28px;font-weight: bold;font-family: SimHei">个人安全</h3>
            <div class="xxjj" style="margin-top: 25px">
              <div class="cont">
                <p style="font-weight: bold">安全保卫:</p>
                <p>学校实行严密的安全保卫制度，校内设有自动视频监控系统进行24小时监控，保安人员在校内各处24小时值勤和巡逻，校门实行进出登记制度。当地的派出所民警每日进校园巡逻。学生宿舍实行小区化管理，宿舍管理中心工作人员24小时值班。</p>
                <div class="fl02 flew">
                  <img src="../assets/img/life_p7.jpg"/>
                </div>
                <p style="font-weight: bold">宿舍值班电话：</p>
                <p style="font-weight: bold">男生宿舍区门岗：</p>
                <p>020-87818819</p>
                <p>020-87818131</p>
                <p style="font-weight: bold">女生宿舍区门岗：</p>
                <p>020-87818822</p>
                <p>020-87818612</p>
                <p style="font-weight: bold">校内24小时报警电话：</p>
                <p>020-8781 8110</p>
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
name: "LifeView",
  data(){
    return{
      activeName:'first',
      activeIndex:"2"
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