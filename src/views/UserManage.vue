<template>
  <div style="padding: 20px">
    <el-card style="margin-bottom: 5px">
      <div>
        <el-input clearable style="width: 240px;margin-right: 5px" v-model="data.name" placeholder="请输入用户名称查询" prefix-icon="Search"></el-input>
        <el-button type="primary" @click="Load">查 询</el-button>
        <el-button type="warning" @click="reset">重 置</el-button>
      </div>
    </el-card>

    <el-card>
      <el-table :data="data.List" stripe  @selection-change="handleSelectionChange">
        <el-table-column type="selection" />
        <el-table-column prop="id" label="用户号" />
        <el-table-column prop="name" label="用户名" />
        <el-table-column prop="role" label="角色" />
      </el-table>

      <div style="margin-top: 10px">
        <el-pagination
            @size-change="Load"
            @current-change="Load"
            v-model:current-page="data.pageNum"
            v-model:page-size="data.pageSize"
            :page-sizes="[5,10,15,20]"
            background
            layout="total,sizes,prev,pager,next,jumper"
            :total="data.total"
            style="padding-left: 250px"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {reactive, ref, onMounted, nextTick} from "vue";
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

const data = reactive({
  List:[],
  name:null,
  pageNum:1,
  pageSize:10,
  total:0,
  form:{},
  dishIds:[],
})

const Load = () =>{
  request.get('/user/selectPage',{
    params: {
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      name:data.name,
    }
  }).then(res =>{
    data.List=res.data.list
    data.total=res.data.total
  })
}
Load()
const reset = () =>{
  data.name=null;
  Load()
}

const handleSelectionChange = (rows) =>{
  // 从选中的行数组里取出所有行的id组成一个新的数组
  data.dishIds=rows.map(row => row.dishId)
}

</script>

<style scoped>

</style>