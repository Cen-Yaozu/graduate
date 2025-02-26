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
      <el-carousel height="650px" :autoplay="false" @change="carouselChange">
        <el-carousel-item :interval="20000">
          <video
              ref="video"
              src="../assets/img/home_v1.mp4"
              width="100%"
              height="100%"
              controls
              autoplay
              volume="0"
              style="object-fit: cover"
              @timeupdate="handleTimeUpdate"
          ></video>
        </el-carousel-item>
        <el-carousel-item>
          <img src="../assets/img/home_p2.jpg">
        </el-carousel-item>
        <el-carousel-item>
          <img src="../assets/img/home_p3.jpg">
        </el-carousel-item>
        <el-carousel-item>
          <img src="../assets/img/home_p4.jpg">
        </el-carousel-item>
      </el-carousel>
    </el-main>
  </el-container>
  <div class="common-layout" style="margin: 0 auto;max-width: 90%">
    <el-container>
      <el-main>
        <el-tabs
            v-model="activeName"
            type="card"
            class="demo-tabs"
            tab-position="left"
            @tab-click="handleClick"
        >
          <el-tab-pane label="学校简介" name="first">
            <h3 style="font-size: 28px;font-weight: bold;font-family: SimHei">学校简介</h3>
            <div class="xxjj" style="margin-top: 25px">
              <div class="cont">
                <p>
                  2002年，广州软件学院诞生在广州的后花园——从化。2006年开始实施本科层次的学历教育，前身为广州大学华软软件学院。2020年12月，经教育部批准，广东省人民政府同意，转设更名为广州软件学院。学校董事长梁冠军先生是美国著名侨领、华人知名实业家、中华海外联谊会副会长、中国侨联顾问、中国和平统一促进会常务理事、美国美东华人社团联合总会主席。
                </p>
                <div class="cont">
                  <p>
                    学校地处广州中新知识城北延区域的广东从化经济开发区，广州地铁14号线太平站停驻校门口，现有在校生16000多人，教职工约980人，是国家级创新创业教育实践基地、国家科学技术部备案“众创空间”、国家自然科学基金依托单位、广东省创新创业教育实践基地、广东省大学生创新创业教育示范学校、广东省和广州市“众创空间”、广东省和广州市两级国际服务外包人才培训机构、广东省博士工作站、广州市创新创业（孵化）示范基地、中国应用技术大学联盟信息技术专业协作会副理事长单位、中国服务贸易协会专家委员会副理事长单位、中国校企协同产学研创新联盟常务理事单位、全国人工智能职业教育联合体发起副理事长单位、广东省民政厅首批5A级社会组织。
                  </p>
                  <p>
                    学校定位科学、理念先进、治学严谨、管理规范，设有软件与人工智能学院、网络空间安全学院、电子信息与控制工程学院、数字传媒学院、创意设计学院、数字经济与管理学院、外国语学院、马克思主义学院、基础与通识教育学院等9个学院，开设广东省一流专业软件工程、物联网工程、物流管理在内的32个本科专业，涵盖工学、艺术学、管理学、经济学、文学等5大学科门类，整体呈现工学（信息技术类）专业为主体，文经管艺多学科协调发展的专业布局。
                  </p>
                  <p>
                    学校秉承“质量立校、特色强校”的发展思路，对接软件与信息技术产业需求，深入开展专业优化设置改革，大力提升内涵建设，着力提高育人质量。经过20多年的建设发展，形成了“特色在IT，优势在软件”“双创引领，专创融合”“侨领榜样，情怀育人”“以美育人，涵养气质”的四大办学特色，现有2个省级实验教学示范中心，1个省级普通高校工程技术中心，1个省级协同育人中心，1个省级重点培育学科，3个省级一流专业建设点，6个省级特色专业。学校注重以社会需求为导向，依托信息产业，以计算机技术在各行各业的应用为立足点，重点发展信息技术类专业群，形成以软件与信息技术类专业为龙头，以信息技术在各行各业的应用为特色的专业群，与华为、奇安信、中软国际、中科智城、京东等知名企业在信息技术、空间安全、智慧物流等领域建立了紧密的校企合作关系，并与网易有道共创广软-网易游戏产业学院，与百度共建软件工程专业（AI大模型创新班），积极探索改革人才培养模式用心培育适应信息技术产业发展需要的创新型、应用型人才，以创新人才培养赋能新质生产力发展。
                  </p>
                </div>
                <div class="fl02 flew">
                  <img src="../assets/img/home_p5.jpg"/>
                </div>
                <div class="cont">
                  <p>学校坚持“人才强校”战略，面向海内外吸纳优秀人才，有专业素质过硬的专家教授，也有海外留学归国的人员，还有来自企业实践经验丰富的“双师型”人才。按照“德业并重，引育并进”的建设思路，大力实施“优才计划”，健全人才引育机制，支持青年教师成长与发展，近百名教师在职攻读博士学位或参与访问学者计划，拥有一支理想信念坚定、师德师风过硬、业务素质精湛、人员结构合理、教育教学充满活力的专业化教师队伍，是学校高质量发展的重要基础。</p>
                  <p>面对时代的快速变迁和技术的迅猛发展，学校将始终坚持以习近平新时代中国特色社会主义思想为指导，坚持社会主义办学方向，秉承“明德日新、知行合一”的校训，落实立德树人根本任务，紧扣内涵建设、质量提升、特色发展三大任务，以更加开放的思想和更加创新的体制、机制，不断深化教学改革，切实加快学校的整体转型与协调发展，提升办学层次，着力提高学校的综合实力和核心竞争力，在高等教育改革的浪潮中乘风破浪，为建成一所在同类院校中具有比较优势和品牌特色的一流应用型本科高校砥砺奋进。</p>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="校长寄语" name="second">
            <h3 style="font-size: 28px;font-weight: bold;font-family: SimHei">校长寄语</h3>
            <div class="article">
              <div id="vsb_content" style="margin-top: 25px">
                <p>南岭磅礴，珠江浩荡。2002年9月，广州软件学院(原广州大学华软软件学院)诞生在广州的后花园——从化。2020年12月，经教育部批准转设为独立设置的全日制普通本科高等学校，并正式更名为广州软件学院。</p>
                <p>建校以来，学校坚持以质量求生存、以创新求发展，以“建成具有品牌特色和比较优势的一流应用型本科高校”目标为引领，重点发展以IT类专业为核心的优势学科群，在学科建设、教学科研、人才培养、创新创业、文化强校等方面齐头并进，培养了一大批适应国家信息产业发展急需的应用型人才，办学特色和水平得到社会认可。</p>
                <p>以生为本，助力成长。学校拥有美丽优雅的环境、充满人文关怀的氛围，囊括大典的学术讲坛、灵活多样的选课机制，宽松自由的创新创业环境，多姿多彩的校园文体生活，这些都是青年学子彰显美德、舒展个性、自由发展、达于至善的基石。我们已经张开有力的臂膀，满怀喜悦地迎接来自五湖四海的优秀学子。我们期待，在人生这条路上，让创新思维、专业一流、技术过硬成为你们最生动、最靓丽的注脚！</p>
                <p>风正扬帆当有为。站在新的历史起点，面对建设粤港澳大湾区这一重大发展机遇，学校将秉承“明德日新，知行合一”的校训，坚持社会主义办学方向，落实立德树人根本任务，紧扣内涵建设、质量提升、特色发展三大要务，积极推进学校的综合改革与持续创新，切实加快学校的整体转型与协调发展，在高等教育改革的浪潮中乘风破浪，为粤港澳大湾区以及广东经济社会发展做出应有贡献。让我们一起携手，把握机遇、继往开来、坚守初心、砥砺奋进！</p>
                <p style="text-align: right;">广州软件学院校长 张强</p>
                <p style="text-align: right;">2023年6月</p>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="校园风光" name="third">
            <h3 style="font-size: 28px;font-weight: bold;font-family: SimHei">校园地图</h3>
            <div class="xyfg" >
              <ul class="flew" style="list-style: none;display: flex;flex-wrap: wrap;margin-top: 25px">
                <li>
                  <el-card style="max-width: 480px">
                    <img
                        class="imges"
                        src="../assets/img/home_p7.jpg"
                        style="width: 100%"
                    />
                    <template #footer><h3 class="l1 txt1">无边湖</h3> </template>
                  </el-card>
                </li>
                <li>
                  <el-card style="max-width: 480px">
                    <img
                        class="imges"
                        src="../assets/img/home_p8.jpg"
                        style="width: 100%"
                    />
                    <template #footer><h3 class="l1 txt1">弘桥广场丝木棉花开</h3> </template>
                  </el-card>
                </li>
                <li>
                  <el-card style="max-width: 480px">
                    <img
                        class="imges"
                        src="../assets/img/home_p9.jpg"
                        style="width: 100%"
                    />
                    <template #footer><h3 class="l1 txt1">学生广场和教学楼</h3> </template>
                  </el-card>
                </li>
                <li>
                  <el-card style="max-width: 480px">
                    <img
                        class="imges"
                        src="../assets/img/home_p9.jpg"
                        style="width: 100%"
                    />
                    <template #footer><h3 class="l1 txt1">无边湖</h3> </template>
                  </el-card>
                </li>
                <li>
                  <el-card style="max-width: 480px">
                    <img
                        class="imges"
                        src="../assets/img/home_p11.jpg"
                        style="width: 100%"
                    />
                    <template #footer><h3 class="l1 txt1">喷泉广场</h3> </template>
                  </el-card>
                </li>
                <li>
                  <el-card style="max-width: 480px">
                    <img
                        class="imges"
                        src="../assets/img/home_p12.jpg"
                        style="width: 100%"
                    />
                    <template #footer><h3 class="l1 txt1">远眺</h3> </template>
                  </el-card>
                </li>
                <li>
                  <el-card style="max-width: 480px">
                    <img
                        class="imges"
                        src="../assets/img/home_p13.jpg"
                        style="width: 100%"
                    />
                    <template #footer><h3 class="l1 txt1">校园航拍</h3> </template>
                  </el-card>
                </li>
              </ul>
            </div>
          </el-tab-pane>
          <el-tab-pane label="校园地图" name="fourth">
            <h3 style="font-size: 28px;font-weight: bold;font-family: SimHei">校园地图</h3>
            <div class="article">
              <div id="vsb_content1" style="margin-top: 25px">
                <p style="text-align: center">
                  <img src="../assets/img/home_p6.jpg" width="900px" height="749px" border="0" vspace="0" hspace="0">
                </p>
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
  name: "HomeView",
  data(){
    return{
      isPlaying:false,
      activeName:'first',
      activeIndex:"1"
    }
  },
  created() {
  },
  mounted() {
    this.video=this.$refs.video;
  },
  methods:{
    playPauseVideo() {
      if (this.isPlaying) {
        this.video.pause();
      } else {
        this.video.play();
      }
      this.isPlaying = !this.isPlaying;
    },
    carouselChange:function(x1,x2){
      if (x1===0){
        this.isPlaying=false;
        this.playPauseVideo();
      }else {
        this.isPlaying=true;
        this.playPauseVideo();

      }
      console.log(x1+",,,"+x2);
    },
    // 标签页
    handleClick:function (tab,event){
      console.log(tab,event);
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
/*走马灯*/
.el-carousel__item h3 {
  display: flex;
  color: #475669;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}
.el-carousel__item img:nth-child(n+1) {
  width: inherit;
  height: inherit;
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
  height: 400px;
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
  margin-bottom: 40px;
  font-size: 20px;
  line-height: 40px;
  text-align: justify;
  color: #333333;
  text-indent: 2em;
}
.article p {
  font-size:20px;
  color: #000;
  line-height: 40px;
  margin-bottom: 0.2rem;
  text-indent: 2em;
  text-align: justify;
  margin-bottom: 40px;
}
.article p img{
  height: auto;
  margin: 0 auto;
  display: block;
}
/*卡片*/
.xyfg ul {
  margin: 0 10px;
}
.xyfg li {
  width: 31.3%;
  padding: 0 10px;
  margin-bottom: 0.4rem;
}
.imges{
  position: relative;
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  overflow: hidden;
  transition: 0.5s;
  background-size: cover !important;
  background-position: center center !important;
}
::v-deep .el-card{
  height: 260px;
}
::v-deep .el-card__body{
  height: 170px;
}
.xyfg li h3[data-v-5c6101e4] {
  font-size: 20px;
  font-weight: 500;
  line-height: 10px;
}
</style>
