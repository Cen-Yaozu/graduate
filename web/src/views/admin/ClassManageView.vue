<template>
  <div class="class-manage">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>班级管理</span>
        </div>
      </template>
      <el-table :data="classList" style="width: 100%">
        <el-table-column prop="classId" label="班级编号" width="180" />
        <el-table-column prop="className" label="班级名称" width="180" />
        <el-table-column prop="majorName" label="所属专业" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default {
  name: 'ClassManageView',
  setup() {
    const classList = ref([])
    const majorList = ref([])

    const fetchData = async () => {
      try {
        // 获取所有专业信息
        const majorRes = await axios.get('/api/class/majors')
        majorList.value = majorRes.data

        // 获取所有班级信息
        const classRes = await axios.get('/api/class/classes')
        // 处理班级数据，将专业ID映射为专业名称
        classList.value = classRes.data.map(classItem => ({
          ...classItem,
          majorName: majorList.value.find(major => major.majorId === classItem.majorId)?.majorName || '未知专业'
        }))
      } catch (error) {
        console.error('获取数据失败：', error)
        ElMessage.error('获取数据失败')
      }
    }

    onMounted(() => {
      fetchData()
    })

    return {
      classList
    }
  }
}
</script>

<style scoped>
.class-manage {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>