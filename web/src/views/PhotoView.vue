<template>
  <div class="photo-page">
    <el-row :gutter="20">
      <el-col :span="4">
        <div class="upload-container">
          <!-- 上传组件，但不自动上传。如果已有照片则禁用上传功能 -->
          <el-upload
            class="avatar-uploader"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="handleFileChange"
            :before-upload="beforeAvatarUpload"
            :disabled="hasUploadedPhoto"
            style="display: flex"
          >
            <img v-if="imageUrl" :src="imageUrl" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
          
          <div v-if="selectedFile" class="preview-info">
            <p>{{ selectedFile.name }}</p>
            <el-button size="small" type="danger" @click="cancelSelection">取消选择</el-button>
          </div>
          
          <!-- 添加已上传照片的提示 -->
          <div v-if="hasUploadedPhoto" class="photo-status-info">
            <el-tag type="success">已完成照片上传</el-tag>
          </div>
        </div>
      </el-col>
      <el-col :span="16">
        <el-card body-style="margin: 20px auto;padding: 10px" style="background-color: #f9f9f9">
          <h3>照片格式要求</h3>
          <p>1. 上传图片大小不得大于2MB</p>
          <p>2. 上传图片像素建议为(宽度*高度):200*200</p>
          <p>3. 上传图片格式为: JPG</p>
          <p>4. 照片要求正面免冠彩色照片，白色或蓝色背景</p>
        </el-card>
      </el-col>
    </el-row>
    <el-button 
      type="primary" 
      @click="savePhoto" 
      :disabled="!selectedFile || hasUploadedPhoto"
      :loading="uploading"
      style="margin-top: 30px;width: 100px;">
      {{ selectedFile ? '上传' : '保存' }}
    </el-button>
  </div>
</template>

<script>
import { Plus } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

export default {
  name: "PhotoView",
  components: {
    Plus
  },
  data() {
    return {
      imageUrl: '', // 本地预览URL或已保存的照片URL
      savedImageUrl: '', // 保存在服务器上的照片URL
      selectedFile: null, // 用户选择的文件对象
      uploading: false, // 上传状态
      studentNumber: window.sessionStorage.getItem('studentNumber') || '',
      headers: {
        'Authorization': 'Bearer ' + window.sessionStorage.getItem('token')
      },
      hasUploadedPhoto: false // 新增：标记是否已上传过照片
    };
  },
  created() {
    // 获取已上传的照片
    this.fetchStudentPhoto();
  },
  methods: {
    // 获取学生已上传的照片
    fetchStudentPhoto() {
      if (!this.studentNumber) {
        ElMessage.warning('无法获取学号信息，请重新登录');
        return;
      }
      
      // 调用API获取学生信息，包括照片URL
      this.$http.get(`/api/freshman-report/get-student-info?studentNumber=${this.studentNumber}`, {
        headers: this.headers
      })
      .then(response => {
        if (response.data.code === 200 && response.data.data && response.data.data.student) {
          const photoUrl = response.data.data.student.studentPicture;
          if (photoUrl) {
            // 使用完整的后端URL：在相对路径前添加后端基础URL
            this.imageUrl = this.$http.defaults.baseURL + photoUrl;
            console.log('图片URL:', this.imageUrl);
            this.savedImageUrl = photoUrl; // 存储原始相对路径，方便后续处理
            this.hasUploadedPhoto = true;
            ElMessage.info('您已完成照片上传，如需更换请联系管理员');
          }
        }
      })
      .catch(error => {
        console.error('获取照片信息失败:', error);
        ElMessage.error('获取照片信息失败，请刷新页面重试');
      });
    },
    
    // 处理文件选择，预览图片
    handleFileChange(file) {
      // 如果已上传照片，则不允许选择
      if (this.hasUploadedPhoto) {
        ElMessage.warning('您已完成照片上传，不能再次上传');
        return;
      }
      
      this.selectedFile = file.raw;
      this.imageUrl = URL.createObjectURL(file.raw);
      console.log('已选择文件:', file.name);
    },
    
    // 取消文件选择
    cancelSelection() {
      this.selectedFile = null;
      // 如果有已保存的图片，恢复显示
      if (this.savedImageUrl) {
        this.imageUrl = this.savedImageUrl;
      } else {
        this.imageUrl = '';
      }
    },
    
    // 检查文件格式和大小
    beforeAvatarUpload(file) {
      // 如果已上传照片，则不允许上传
      if (this.hasUploadedPhoto) {
        ElMessage.warning('您已完成照片上传，不能再次上传');
        return false;
      }
      
      console.log('检查文件:', file.name);
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        ElMessage.error('上传头像图片只能是 JPG 格式!');
        return false;
      }
      if (!isLt2M) {
        ElMessage.error('上传头像图片大小不能超过 2MB!');
        return false;
      }
      
      return true;
    },
    
    // 保存照片到服务器
    savePhoto() {
      // 如果已上传照片，则不允许保存
      if (this.hasUploadedPhoto) {
        ElMessage.warning('您已完成照片上传，不能再次上传');
        return;
      }
      
      // 如果没有选择新文件且已有保存的图片，显示保存成功
      if (!this.selectedFile && this.savedImageUrl) {
        ElMessage.success('照片已保存');
        return;
      }
      
      // 如果没有选择文件，提示用户
      if (!this.selectedFile) {
        ElMessage.warning('请先上传照片');
        return;
      }
      
      // 创建FormData对象上传文件
      const formData = new FormData();
      formData.append('file', this.selectedFile);
      formData.append('studentNumber', this.studentNumber);
      
      this.uploading = true;
      
      // 调用API上传照片
      this.$http.post('/api/freshman-report/upload-photo', formData, {
        headers: {
          'Authorization': 'Bearer ' + window.sessionStorage.getItem('token')
        }
      })
      .then(response => {
        this.uploading = false;
        console.log('上传成功响应:', response.data);
        
        if (response.data.code === 200) {
          // 保存服务器返回的URL (相对路径)
          const photoUrl = response.data.data;
          this.savedImageUrl = photoUrl;
          
          // 使用完整的后端URL
          this.imageUrl = this.$http.defaults.baseURL + photoUrl;
          console.log('完整图片URL:', this.imageUrl);
          
          this.hasUploadedPhoto = true;
          ElMessage.success('照片上传成功');
          this.selectedFile = null;
        } else {
          ElMessage.error(response.data.msg || '照片上传失败');
        }
      })
      .catch(error => {
        this.uploading = false;
        console.error('上传照片错误:', error);
        ElMessage.error('照片上传失败，请检查网络连接');
      });
    }
  }
}
</script>

<style scoped>
.photo-page {
  padding: 20px;
}

.upload-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.preview-info {
  margin-top: 10px;
  text-align: center;
}

.preview-info p {
  margin-bottom: 5px;
  font-size: 12px;
  word-break: break-all;
}

/* 添加已上传状态提示的样式 */
.photo-status-info {
  margin-top: 15px;
  text-align: center;
}

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

/* 添加禁用状态的样式 */
.avatar-uploader .el-upload.is-disabled {
  cursor: not-allowed;
  background-color: #f5f7fa;
  border-color: #e4e7ed;
}

.avatar-uploader .el-upload.is-disabled:hover {
  border-color: #e4e7ed;
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}

.el-card {
  margin-bottom: 20px;
}

.el-card h3 {
  margin-top: 0;
  color: #244cb2;
}

.el-card p {
  line-height: 24px;
  margin: 8px 0;
}
</style>