<template>
  <el-descriptions title="学生基本信息" border>
    <el-descriptions-item label="姓名">梁XX</el-descriptions-item>
    <el-descriptions-item label="性别">女</el-descriptions-item>
    <el-descriptions-item label="出生年月">2003年8月19日</el-descriptions-item>
    <el-descriptions-item label="籍贯">
      <el-input
        v-model="hometown"
        style="width: 240px"
        clearable
    />
    </el-descriptions-item>
    <el-descriptions-item label="民族">
      <el-input
          v-model="nation"
          style="width: 240px"
          clearable
      />
    </el-descriptions-item>
    <el-descriptions-item label="政治面貌">
      <el-input
          v-model="politeAspect"
          style="width: 240px"
          clearable
      />
    </el-descriptions-item>
    <el-descriptions-item label="证件">身份证</el-descriptions-item>
    <el-descriptions-item label="身份证号">442000200609103879</el-descriptions-item>
    <el-descriptions-item label="电话">
      <el-input
          v-model.number="phone"
          style="width: 240px"
          clearable
      />
    </el-descriptions-item>
    <el-descriptions-item label="详细通讯地址">
      <el-input
          v-model='address'
          style="width: 400px"
          clearable
      />
    </el-descriptions-item>
  </el-descriptions>
  <br>
  <el-descriptions title="学生简历" :column="3" border></el-descriptions>
  <el-table :data="resumeData" style="width: 100%" max-height="250">
    <el-table-column fixed prop="startTime" label="开始时间" width="200" />
    <el-table-column prop="endTime" label="结束时间" width="200" />
    <el-table-column prop="organization" label="单位学习、工作" width="300" />
    <el-table-column prop="hats" label="职务" width="120" />
    <el-table-column align="right">
      <template #header>
        <el-button class="mt-4" style="width: 20%" @click="onAddItem">
          添加
        </el-button>
      </template>
      <template #default="scope">
        <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.$index,resumeData)"
        >
          删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
  <br>
  <el-descriptions title="家庭主要成员" :column="3" border></el-descriptions>
  <el-table :data="familyData" style="width: 100%" max-height="250">
    <el-table-column fixed prop="familyPart" label="称谓" width="200" />
    <el-table-column prop="familyName" label="姓名" width="200" />
    <el-table-column prop="familyAge" label="年龄" width="200" />
    <el-table-column prop="familyPoliteAspect" label="政治面貌" width="200" />
    <el-table-column prop="familyOrganization" label="单位学习、工作" width="300" />
    <el-table-column align="right">
      <template #header>
        <el-button class="mt-4" style="width: 55%" @click="onAddfamilyItem">
          添加
        </el-button>
      </template>
      <template #default="scope">
        <el-button
            size="small"
            type="danger"
            @click="familyDelete(scope.$index,familyData)"
        >
          删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
export default {
  name: "InformationView",
  data(){
  return{
    hometown:'',
    politeAspect:'',
    nation:'',
    phone:'',
    address:'',
    rules:{

    },
    resumeData:[
      {
        startTime:'123',
        endTime:'456',
        organization:'789',
        hats:'753'
      },
      {
        startTime:'1234',
        endTime:'4564',
        organization:'7894',
        hats:'7534'
      }
    ]
  }
  },
  methods:{
    handleDelete(index,rows){
      rows.splice(index, 1);
    },
    familyDelete(index,rows){
      rows.splice(index, 1);
    },
  }
}
</script>

<style scoped>

</style>