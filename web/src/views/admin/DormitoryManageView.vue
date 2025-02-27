<template>
  <div class="dormitory-manage">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>宿舍管理</span>
          <el-button type="primary" @click="handleAdd">添加宿舍</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="请输入宿舍号/楼栋号进行搜索"
          class="search-input"
          clearable
          @clear="handleSearch"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
      </div>

      <!-- 宿舍列表 -->
      <el-table :data="dormitoryList" style="width: 100%" v-loading="loading">
        <el-table-column prop="buildingNo" label="楼栋号" width="120" />
        <el-table-column prop="roomNo" label="宿舍号" width="120" />
        <el-table-column prop="type" label="宿舍类型" width="120">
          <template #default="scope">
            {{ scope.row.type === 1 ? '男生宿舍' : '女生宿舍' }}
          </template>
        </el-table-column>
        <el-table-column prop="capacity" label="容量" width="120" />
        <el-table-column prop="occupied" label="已住人数" width="120" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '已分配' : '未分配' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button-group>
              <el-button type="primary" @click="handleEdit(scope.row)" size="small">编辑</el-button>
              <el-button type="danger" @click="handleDelete(scope.row)" size="small">删除</el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加/编辑宿舍对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加宿舍' : '编辑宿舍'"
      width="500px"
    >
      <el-form :model="dormitoryForm" :rules="rules" ref="dormitoryFormRef" label-width="100px">
        <el-form-item label="楼栋号" prop="buildingNo">
          <el-input v-model="dormitoryForm.buildingNo" placeholder="请输入楼栋号" />
        </el-form-item>
        <el-form-item label="宿舍号" prop="roomNo">
          <el-input v-model="dormitoryForm.roomNo" placeholder="请输入宿舍号" />
        </el-form-item>
        <el-form-item label="宿舍类型" prop="type">
          <el-select v-model="dormitoryForm.type" placeholder="请选择宿舍类型" style="width: 100%">
            <el-option label="男生宿舍" :value="1" />
            <el-option label="女生宿舍" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="容量" prop="capacity">
          <el-input-number v-model="dormitoryForm.capacity" :min="1" :max="8" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 数据列表
const dormitoryList = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchKeyword = ref('')

// 对话框相关
const dialogVisible = ref(false)
const dialogType = ref('add')
const dormitoryFormRef = ref(null)
const dormitoryForm = ref({
  buildingNo: '',
  roomNo: '',
  type: 1,
  capacity: 4
})

// 表单验证规则
const rules = {
  buildingNo: [{ required: true, message: '请输入楼栋号', trigger: 'blur' }],
  roomNo: [{ required: true, message: '请输入宿舍号', trigger: 'blur' }],
  type: [{ required: true, message: '请选择宿舍类型', trigger: 'change' }],
  capacity: [{ required: true, message: '请输入容量', trigger: 'blur' }]
}

// 获取宿舍列表
const getDormitoryList = async () => {
  loading.value = true
  try {
    // TODO: 调用后端API获取宿舍列表
    // const res = await getDormitoryListAPI({
    //   page: currentPage.value,
    //   size: pageSize.value,
    //   keyword: searchKeyword.value
    // })
    // dormitoryList.value = res.data.list
    // total.value = res.data.total
  } catch (error) {
    console.error('获取宿舍列表失败：', error)
    ElMessage.error('获取宿舍列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  getDormitoryList()
}

// 分页相关
const handleSizeChange = (val) => {
  pageSize.value = val
  getDormitoryList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getDormitoryList()
}

// 添加宿舍
const handleAdd = () => {
  dialogType.value = 'add'
  dormitoryForm.value = {
    buildingNo: '',
    roomNo: '',
    type: 1,
    capacity: 4
  }
  dialogVisible.value = true
}

// 编辑宿舍
const handleEdit = (row) => {
  dialogType.value = 'edit'
  dormitoryForm.value = { ...row }
  dialogVisible.value = true
}

// 删除宿舍
const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除${row.buildingNo}栋${row.roomNo}号宿舍吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // TODO: 调用后端API删除宿舍
      // await deleteDormitoryAPI(row.id)
      ElMessage.success('删除成功')
      getDormitoryList()
    } catch (error) {
      console.error('删除宿舍失败：', error)
      ElMessage.error('删除宿舍失败')
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!dormitoryFormRef.value) return
  
  await dormitoryFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // TODO: 调用后端API提交表单
        // if (dialogType.value === 'add') {
        //   await addDormitoryAPI(dormitoryForm.value)
        // } else {
        //   await updateDormitoryAPI(dormitoryForm.value)
        // }
        ElMessage.success(dialogType.value === 'add' ? '添加成功' : '更新成功')
        dialogVisible.value = false
        getDormitoryList()
      } catch (error) {
        console.error('提交表单失败：', error)
        ElMessage.error('提交失败')
      }
    }
  })
}

onMounted(() => {
  getDormitoryList()
})
</script>

<style scoped>
.dormitory-manage {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>