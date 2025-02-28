<template>
  <div class="common-layout">
    <el-container>
      <el-header style="background-color: #244cb2; display: flex; align-items: center; justify-content: space-between;">
        <el-page-header @back="goBack" style="color: white">
          <template #content>
            <span class="text-large font-600 mr-3" style="font-weight: 800;color: white">宿舍类型选择</span>
          </template>
        </el-page-header>
        
        <!-- 添加用户头像和下拉菜单 -->
        <div class="user-avatar">
          <el-dropdown trigger="click">
            <span class="el-dropdown-link">
              <el-avatar :size="40" :icon="UserFilled" />
              <el-icon class="el-icon--right">
                <arrow-down />
              </el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>{{ studentNumber }}</el-dropdown-item>
                <el-dropdown-item divided @click="toStudentHome">个人管理</el-dropdown-item>
                <el-dropdown-item @click="handleLogout">退出系统</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <!-- 已选宿舍信息显示区域 -->
        <el-card v-if="selectedInfo.selected" class="info-card">
          <el-result
            icon="success"
            title="宿舍类型已选择"
            :sub-title="`您已选择: ${getDormTypeName(selectedInfo.dormType)} (${selectedInfo.status})`"
          >
            <template #extra>
              <div v-if="selectedInfo.dormitory">
                <p>已分配宿舍：{{ selectedInfo.dormitory }}</p>
                <p v-if="selectedInfo.dormCard">宿舍卡号：{{ selectedInfo.dormCard }}</p>
              </div>
              <div v-else>
                <p>宿舍类型已选择，等待管理员分配宿舍</p>
              </div>
            </template>
          </el-result>
        </el-card>
        
        <!-- 选择宿舍类型区域，仅在未选择时显示 -->
        <el-card v-else body-style="padding: 40px 0 0 0" style="width: 90%;height: 500px;background: linear-gradient(to top right,rgba(90,149,207,.5),#f5f7fa);margin: 40px auto;">
          <el-card style="max-width: 200px;background: linear-gradient(to top right,#F7EBE1,#f7DFF1);border-radius: 0 30px 30px 0px;">
            <p>填写宿舍意向</p>
          </el-card>
          <p style="margin-top: 50px;font-size: 18px;font-weight: 550;">欢迎您</p>
          <div class="w">
            <p>欢迎新同学加入广州软件学院大家庭!根据我校学生宿舍实际情况，宿舍分空调四人间(住宿费3500元/人/年)、空调六人间(住宿费1500元/人/年)两种类型，为做好学生宿舍入住工作,请同学根据流程进行选择宿舍类型，两种类型的床位数量都固定，造循先选先得原则，如某个类型的宿舍已选完，则只能选择另一个类型的宿舍。 如未在指定时间内进行选择，则由学校统筹分配，谢谢配合!</p>
            <p>请选择意向，确认后不可重新选择</p>
            
            <!-- 宿舍类型选择列表 -->
            <div class="dorm-types-container">
              <div v-for="(dormType, index) in dormTypes" :key="index" class="dorm-type-item">
                <el-radio v-model="selectedDormType" :label="dormType.type" :disabled="dormType.availableCount <= 0">
                  {{ dormType.type }} ({{ dormType.price }}元/年)
                  <el-tag v-if="dormType.availableCount > 0" type="success" size="small">剩余{{ dormType.availableCount }}个床位</el-tag>
                  <el-tag v-else type="danger" size="small">已满</el-tag>
                </el-radio>
                <div class="dorm-description">{{ dormType.description }}</div>
              </div>
            </div>
          </div>
          <el-button style="background: linear-gradient(to top right,#F7EBE1,#f7DFF1)" @click="submitDormSelection" v-loading.fullscreen.lock="fullscreenLoading" :disabled="!selectedDormType">确认选择</el-button>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import router from "@/router";
import { UserFilled, ArrowDown } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import axios from 'axios';

export default {
  name: "SelectDormView",
  data() {
    return {
      selectedDormType: '',
      dormTypes: [],
      selectedInfo: {
        selected: false,
        dormType: '',
        status: '',
        dormitory: null,
        dormCard: null
      },
      fullscreenLoading: false,
      UserFilled,
      ArrowDown,
      studentNumber: window.sessionStorage.getItem('studentNumber') || '未知用户'
    };
  },
  created() {
    this.checkExistingSelection();
    this.fetchDormTypes();
  },
  methods: {
    // 获取宿舍类型数据
    fetchDormTypes() {
      axios.get('/api/student/dorm-selection/types', {
        headers: {
          'Authorization': 'Bearer ' + window.sessionStorage.getItem('token')
        }
      })
      .then(response => {
        if (response.data.code === 200) {
          this.dormTypes = response.data.data;
        } else {
          ElMessage.error('获取宿舍类型失败: ' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('获取宿舍类型异常:', error);
        ElMessage.error('网络异常，请稍后重试');
      });
    },
    
    // 检查学生是否已经选择了宿舍类型
    checkExistingSelection() {
      axios.get(`/api/student/dorm-selection/info/${this.studentNumber}`, {
        headers: {
          'Authorization': 'Bearer ' + window.sessionStorage.getItem('token')
        }
      })
      .then(response => {
        if (response.data.code === 200) {
          this.selectedInfo = response.data.data;
        } else {
          ElMessage.error('获取宿舍选择信息失败: ' + response.data.msg);
        }
      })
      .catch(error => {
        console.error('获取宿舍选择信息异常:', error);
        ElMessage.error('网络异常，请稍后重试');
      });
    },
    
    // 提交宿舍类型选择
    submitDormSelection() {
      if (!this.selectedDormType) {
        ElMessage.warning('请选择宿舍类型');
        return;
      }
      
      ElMessageBox.confirm('您确定要选择 ' + this.getDormTypeName(this.selectedDormType) + ' 吗？选择后将不能更改', '确认选择', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.fullscreenLoading = true;
        
        axios.post('/api/student/dorm-selection/submit', {
          studentNumber: this.studentNumber,
          dormType: this.selectedDormType
        }, {
          headers: {
            'Authorization': 'Bearer ' + window.sessionStorage.getItem('token')
          }
        })
        .then(response => {
          this.fullscreenLoading = false;
          if (response.data.code === 200) {
            ElMessage.success('宿舍类型选择成功');
            // 刷新选择状态
            this.checkExistingSelection();
          } else {
            ElMessage.error('宿舍类型选择失败: ' + response.data.msg);
          }
        })
        .catch(error => {
          this.fullscreenLoading = false;
          console.error('提交宿舍选择异常:', error);
          ElMessage.error('网络异常，请稍后重试');
        });
      }).catch(() => {
        ElMessage.info('已取消选择');
      });
    },
    
    // 获取宿舍类型名称显示
    getDormTypeName(type) {
      switch(type) {
        case '四人间':
          return '空调四人间 (3500元/年)';
        case '六人间':
          return '空调六人间 (1500元/年)';
        default:
          return type;
      }
    },
    
    // 页面导航方法
    goBack() {
      router.push('/freshmanreport')
    },
    toStudentHome() {
      router.push('/shome');
    },
    handleLogout() {
      // 清除会话存储中的所有信息
      window.sessionStorage.removeItem('token');
      window.sessionStorage.removeItem('role');
      window.sessionStorage.removeItem('userRole');
      window.sessionStorage.removeItem('studentNumber');
      window.sessionStorage.removeItem('studentName');
      
      ElMessage.success('已成功退出系统');
      
      // 跳转到登录页
      router.push('/login');
    }
  },
}
</script>

<style scoped>
/*文字*/
.w p{
  margin-bottom: 10px;
  font-size: 15px;
  line-height: 25px;
  text-align: justify;
  color: #333333;
  text-indent: 2em;
  padding: 10px 80px 0 80px;
}

/* 用户头像样式 */
.user-avatar {
  display: flex;
  align-items: center;
  margin-right: 20px;
  height: 60px;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.user-avatar .el-avatar {
  background-color: #fff;
  color: #244cb2;
  margin-right: 5px;
  transition: transform 0.3s;
}

.user-avatar:hover .el-avatar {
  transform: scale(1.1);
}

/* 下拉菜单箭头颜色 */
.el-icon--right {
  color: white;
}

/* 宿舍类型样式 */
.dorm-types-container {
  padding: 0 80px;
  text-align: left;
  margin-bottom: 20px;
}

.dorm-type-item {
  margin-bottom: 15px;
  padding: 10px;
  border-radius: 8px;
  background-color: rgba(255, 255, 255, 0.6);
}

.dorm-description {
  color: #666;
  font-size: 14px;
  margin-top: 5px;
  margin-left: 25px;
}

/* 已选择信息卡片 */
.info-card {
  width: 90%;
  margin: 40px auto;
  background: linear-gradient(to top right, rgba(90,149,207,.5), #f5f7fa);
}
</style>