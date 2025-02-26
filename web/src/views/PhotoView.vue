<template>
  <el-row :gutter="20">
    <el-col :span="4"><el-upload
      class="avatar-uploader"
      action="D:\毕业设计\picture\upload"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload"
      style="display: flex"
  >
    <img v-if="imageUrl" :src="imageUrl" class="avatar" />
    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
    </el-upload></el-col>
    <el-card body-style="margin: 20px auto;padding: 0" style="width: 500px;height: 130px;background-color: beige">
      <p>照片格式要求</p>
      <p>1.上传图片大小不得大于K</p>
      <p>2.上传图片像素为(宽度*高度):200*200</p>
      <p>3.上传图片格式包含:</p>
    </el-card>
  </el-row>
  <el-button type="primary" style="display: flex;margin-top: 30px;margin-left: 46px;width: 100px;">保存</el-button>
</template>

<script>
export default {
  name: "PhotoView",
  data(){
  return{
    imageUrl:''
  };
  },
  methods: {
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    }
  }
}
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style>
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

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
.el-card__body p{
  text-align: justify;
  text-indent: 2em;
  line-height: 20px;
}
</style>