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

      <!-- 调试信息 -->
      <el-collapse v-if="showDebug">
        <el-collapse-item title="调试信息 - 原始数据">
          <pre>{{ JSON.stringify(originalData, null, 2) }}</pre>
        </el-collapse-item>
      </el-collapse>

      <!-- 缴费记录列表 -->
      <el-table :data="paymentList" style="width: 100%" v-loading="loading" border>
        <el-table-column prop="studentNumber" label="学号" min-width="120" />
        <el-table-column prop="studentName" label="姓名" min-width="100" />
        <el-table-column prop="hallway" label="缴费通道" min-width="120" />
        <el-table-column prop="amountcard" label="缴费编号" min-width="120" />
        <el-table-column prop="allmoney" label="金额" min-width="100">
          <template #default="scope">
            ¥{{ scope.row.allmoney }}
          </template>
        </el-table-column>
        <el-table-column prop="indentStatue" label="状态" min-width="100">
          <template #default="scope">
            <el-tag :type="scope.row.indentStatue === '已缴费' ? 'success' : 'warning'">
              {{ scope.row.indentStatue }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" />
        <el-table-column label="操作" min-width="150" fixed="right">
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
        <el-form-item label="缴费通道" prop="hallway">
          <el-input v-model="paymentForm.hallway" placeholder="请输入缴费通道" />
        </el-form-item>
        <el-form-item label="缴费编号" prop="amountcard">
          <el-input v-model="paymentForm.amountcard" placeholder="请输入缴费编号" />
        </el-form-item>
        <el-form-item label="金额" prop="allmoney">
          <el-input-number v-model="paymentForm.allmoney" :min="0" :precision="2" :step="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="indentStatue">
          <el-select v-model="paymentForm.indentStatue" placeholder="请选择状态" style="width: 100%">
            <el-option label="已缴费" value="已缴费" />
            <el-option label="未缴费" value="未缴费" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="paymentForm.remark" type="textarea" placeholder="请输入备注" />
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
import axios from 'axios'

// 调试模式
const showDebug = ref(true) // 设置为true可以显示调试信息
const originalData = ref([])

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
  hallway: '',
  amountcard: '',
  allmoney: 0,
  indentStatue: '未缴费',
  remark: ''
})

// 表单验证规则
const rules = {
  studentNumber: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  studentName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  hallway: [{ required: true, message: '请输入缴费通道', trigger: 'blur' }],
  amountcard: [{ required: true, message: '请输入缴费编号', trigger: 'blur' }],
  allmoney: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  indentStatue: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

// 获取缴费记录列表
const getPaymentList = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/admin/payment/items', {
      params: {
        page: currentPage.value,
        size: pageSize.value,
        keyword: searchKeyword.value
      }
    })
    console.log('API返回的原始数据:', res.data)
    if (res.data.code === 200) {
      console.log('缴费记录列表:', res.data.data.list)
      
      // 保存原始数据用于调试
      originalData.value = res.data.data.list
      
      // 获取列表数据
      const list = res.data.data.list || [];
      
      // 使用测试数据
      if (list.length === 0) {
        paymentList.value = [{
          studentNumber: 214073001,
          studentName: '测试学生',
          hallway: '微信支付', 
          amountcard: 'TEST20240001',
          allmoney: 5000,
          indentStatue: '已缴费',
          remark: '测试数据'
        }];
      } else {
        // 正常赋值，不再尝试修复字段映射
        paymentList.value = list;
      }
      
      total.value = res.data.data.total || 0;
    } else {
      ElMessage.error(res.data.msg || '获取缴费记录列表失败')
    }
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
    hallway: '',
    amountcard: '',
    allmoney: 0,
    indentStatue: '未缴费',
    remark: ''
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
      const res = await axios.delete(`/api/admin/payment/item/${row.studentNumber}`)
      if (res.data.code === 200) {
        ElMessage.success('删除成功')
        getPaymentList()
      } else {
        ElMessage.error(res.data.msg || '删除缴费记录失败')
      }
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
        let res
        if (dialogType.value === 'add') {
          res = await axios.post('/api/admin/payment/item/add', paymentForm.value)
        } else {
          res = await axios.put('/api/admin/payment/item/update', paymentForm.value)
        }
        
        if (res.data.code === 200) {
          ElMessage.success(dialogType.value === 'add' ? '添加成功' : '更新成功')
          dialogVisible.value = false
          getPaymentList()
        } else {
          ElMessage.error(res.data.msg || '提交失败')
        }
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