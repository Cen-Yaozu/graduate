<template>
  <div class="payment-manage">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>缴费管理</span>
          <el-button type="primary" @click="handleAdd">添加缴费记录</el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="请输入学号/姓名进行搜索"
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

      <!-- 缴费记录列表 -->
      <el-table :data="paymentList" style="width: 100%" v-loading="loading">
        <el-table-column prop="studentNumber" label="学号" width="120" />
        <el-table-column prop="studentName" label="姓名" width="120" />
        <el-table-column prop="paymentType" label="缴费类型" width="120">
          <template #default="scope">
            {{ scope.row.paymentType === 1 ? '学费' : '住宿费' }}
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="scope">
            ¥{{ scope.row.amount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'">
              {{ scope.row.status === 1 ? '已缴费' : '未缴费' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="paymentTime" label="缴费时间" width="180" />
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

    <!-- 添加/编辑缴费记录对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加缴费记录' : '编辑缴费记录'"
      width="500px"
    >
      <el-form :model="paymentForm" :rules="rules" ref="paymentFormRef" label-width="100px">
        <el-form-item label="学号" prop="studentNumber">
          <el-input v-model="paymentForm.studentNumber" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="姓名" prop="studentName">
          <el-input v-model="paymentForm.studentName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="缴费类型" prop="paymentType">
          <el-select v-model="paymentForm.paymentType" placeholder="请选择缴费类型" style="width: 100%">
            <el-option label="学费" :value="1" />
            <el-option label="住宿费" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number v-model="paymentForm.amount" :min="0" :precision="2" :step="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="paymentForm.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="已缴费" :value="1" />
            <el-option label="未缴费" :value="2" />
          </el-select>
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
const paymentList = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const searchKeyword = ref('')

// 对话框相关
const dialogVisible = ref(false)
const dialogType = ref('add')
const paymentFormRef = ref(null)
const paymentForm = ref({
  studentNumber: '',
  studentName: '',
  paymentType: 1,
  amount: 0,
  status: 1
})

// 表单验证规则
const rules = {
  studentNumber: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  studentName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  paymentType: [{ required: true, message: '请选择缴费类型', trigger: 'change' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

// 获取缴费记录列表
const getPaymentList = async () => {
  loading.value = true
  try {
    // TODO: 调用后端API获取缴费记录列表
    // const res = await getPaymentListAPI({
    //   page: currentPage.value,
    //   size: pageSize.value,
    //   keyword: searchKeyword.value
    // })
    // paymentList.value = res.data.list
    // total.value = res.data.total
  } catch (error) {
    console.error('获取缴费记录列表失败：', error)
    ElMessage.error('获取缴费记录列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  getPaymentList()
}

// 分页相关
const handleSizeChange = (val) => {
  pageSize.value = val
  getPaymentList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getPaymentList()
}

// 添加缴费记录
const handleAdd = () => {
  dialogType.value = 'add'
  paymentForm.value = {
    studentNumber: '',
    studentName: '',
    paymentType: 1,
    amount: 0,
    status: 1
  }
  dialogVisible.value = true
}

// 编辑缴费记录
const handleEdit = (row) => {
  dialogType.value = 'edit'
  paymentForm.value = { ...row }
  dialogVisible.value = true
}

// 删除缴费记录
const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除${row.studentName}的缴费记录吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // TODO: 调用后端API删除缴费记录
      // await deletePaymentAPI(row.id)
      ElMessage.success('删除成功')
      getPaymentList()
    } catch (error) {
      console.error('删除缴费记录失败：', error)
      ElMessage.error('删除缴费记录失败')
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!paymentFormRef.value) return
  
  await paymentFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // TODO: 调用后端API提交表单
        // if (dialogType.value === 'add') {
        //   await addPaymentAPI(paymentForm.value)
        // } else {
        //   await updatePaymentAPI(paymentForm.value)
        // }
        ElMessage.success(dialogType.value === 'add' ? '添加成功' : '更新成功')
        dialogVisible.value = false
        getPaymentList()
      } catch (error) {
        console.error('提交表单失败：', error)
        ElMessage.error('提交失败')
      }
    }
  })
}

onMounted(() => {
  getPaymentList()
})
</script>

<style scoped>
.payment-manage {
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