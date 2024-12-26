<template>
  <div class="ad-container">
    <!-- 搜索区域 -->
    <el-card style="margin-bottom: 5px">
      <el-button type="primary" plain :icon="CirclePlus" @click="handleAdd">新增类别</el-button>
      <el-button type="danger" plain :icon="Delete" @click="delBatchCategory">批量删除</el-button>
    </el-card>
    <el-card style="margin-bottom: 5px">
        <el-checkbox
            v-model="checkAll"
            :indeterminate="isIndeterminate"
            @change="handleCheckAllChange"
        >
          全选
        </el-checkbox>
        <el-checkbox-group
            v-model="data.checked"
            @change="handleCheckedCitiesChange"
        >
          <el-checkbox v-for="item in data.categories" :key="item.id" :label="item.category" :value="item.id" border>
            {{ item.category }}<el-button link type="danger" :icon="Close" style="margin-left: 10px" @click="deleteCategory(item.id)"></el-button>
          </el-checkbox>
        </el-checkbox-group>
    </el-card>
    <el-card style="margin-bottom: 5px">
      <el-input prefix-icon="Search" v-model="data.searchForm.title" placeholder="请输入广告标题" style="max-width: 300px">
        <template #prepend>
          <el-select v-model="data.searchForm.category" placeholder="类别" clearable @change="load" style="width: 80px">
            <el-option v-for="item in data.categories"
                       :key="item.id"
                       :value-on-clear="null"
                       :label="item.category"
                       :value="item.category"/>
          </el-select>
        </template>
      </el-input>
      <el-button type="plain" @click="load"><el-icon style="vertical-align: middle"><Search/></el-icon></el-button>
      <el-button type="plain" @click="reset" style="margin: 0"><el-icon style="vertical-align: middle"><RefreshRight/></el-icon></el-button>
    </el-card>
    <el-card>
      <!-- 操作按钮区域 -->
      <template #header>
        <el-button type="danger" plain :icon="Delete" @click="handleBatchDelete">批量删除</el-button>
      </template>

      <!-- 广告列表 -->
      <el-table :data="data.tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="title" label="广告标题"/>
        <el-table-column label="广告图片" width="120">
          <template #default="scope">
            <el-image :src="scope.row.image"
                      :zoom-rate="1.2"
                      :max-scale="7"
                      :min-scale="0.2"
                      fit="cover"
                      style="width: 100px; height: 100px;"/>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="详细内容" show-overflow-tooltip/>
        <el-table-column prop="category" label="分类"/>
        <el-table-column prop="keywords" label="关键词"/>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag
                :key="scope.row.status"
                :type="getTypeByStatus(scope.row.status)"
                effect="light"
                round
            >
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300">
          <template #default="scope">
            <el-button link type="danger" :icon="Delete" @click="delByTitle(scope.row.id)">删除</el-button>
            <el-button link type="info" :icon="DataAnalysis" @click="handleData(scope.row)" v-if="scope.row.status==='已投放第三方平台'">数据</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <template #footer>
        <div style="display: flex;justify-content: center">
          <el-pagination
              v-model:current-page="pagination.currentPage"
              v-model:page-size="pagination.pageSize"
              :page-sizes="[10, 20, 50, 100]"
              :size="pagination.size"
              :disabled="pagination.disabled"
              :background="pagination.background"
              :total="pagination.total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
          />
        </div>
      </template>
    </el-card>

    <el-dialog title="添加类别" v-model="data.dialogVisible" width="500">
      <el-input clearable style="width:240px;margin-left: 100px" v-model="data.categoryForm.category" autocomplete="off" placeholder="请输入类别名称"/>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveCategory">保 存</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue'
import {CirclePlus, Close, DataAnalysis, Delete, Plus} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from "element-plus";
import request from "@/utils/request.js";

const checkAll = ref(false)
const isIndeterminate = ref(true)
const data = reactive( {
  checked:[],
  selectedRows:[],
  categories:[],
  category:'',
  id:'',
  searchForm: {
    category: '',
    title: ''
  },
  categoryForm:{},
  categoryIds:[],
  tableData:[],
  dialogVisible: false
})

// 分页相关数据
const pagination = reactive({
  size: 'default',
  currentPage: 1,
  pageSize: 10,
  total: 0,
  background: false,
  disabled: false,
})

// 处理页码改变
const handleCurrentChange = (page) => {
  pagination.currentPage = page
  load()
}

// 处理每页条数改变
const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.currentPage = 1
  load()
}

const handleCheckAllChange = (val) => {
  // 如果全选，data.checked 赋值为所有 seriesId，否则清空 checked 数组
  data.checked = val ? data.categories.map(item => item.id) : [];
  console.log(data.categories.map(item => item.category)); // 如果字段是name
  data.categoryIds=data.categories.map(item => item.id)
  // 确保 isIndeterminate 设为 false
  isIndeterminate.value = false;
};

const handleCheckedCitiesChange = (value) => {
  const checkedCount = value.length;
  checkAll.value = checkedCount === data.categories.length;
  isIndeterminate.value = checkedCount > 0 && checkedCount < data.categories.length;
  data.categoryIds=value
  console.log(data.categoryIds);  // 打印选中的类别id
};

const handleAdd = () =>{
  data.dialogVisible=true
  data.categoryForm={}
}

const saveCategory = () =>{
  request.post('/category/add', data.categoryForm).then(res =>{
    if(res.code === '200'){
      data.dialogVisible=false
      ElMessage.success('操作成功')
      categoriesLoad() //新增后重新加载最新的数据
    }else{
      ElMessage.error(res.msg)
    }
  })
}

const deleteCategory = (id) =>{
  ElMessageBox.confirm('删除数据后无法恢复，您确定要删除吗？','删除确认',{type:'warning'}).then(() =>{
    request.delete('/category/deleteById/' +id).then(res =>{
      if(res.code === '200'){
        ElMessage.success('操作成功')
        categoriesLoad() //删除后重新加载最新的数据
      }else if(res.code==='400'){
        ElMessage.error(res.data)
      }else{
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

const delBatchCategory = () =>{
  if(data.categoryIds.length === 0){
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除数据后无法恢复，您确定要删除吗？','删除确认',{type:'warning'}).then(() =>{
    request.delete('/category/deleteBatch', { data: data.categoryIds }).then(res =>{
      if(res.code === '200'){
        ElMessage.success('操作成功')
        categoriesLoad() //删除后重新加载最新的数据
      }else{
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

// 修改搜索方法
const load = () => {
  request.get('/advertisement/selectPage',{
    params: {
      pageNum:pagination.currentPage,
      pageSize:pagination.pageSize,
      title:data.searchForm.title,
      category: data.searchForm.category
    }
  }).then(res =>{
    data.tableData =res.data.list
    pagination.total = res.data.total
    console.log(pagination.total)
  })
}

const categoriesLoad = () => {
  request.get("/category/selectAll").then(res=>(
      data.categories=res.data
  ))
}

onMounted(() => {
  load()
  categoriesLoad()
})

//获取状态标签格式
const getTypeByStatus = (status) => {
  if (status === '未投放'){
    return 'info'
  }else if (status === '已提交平台'){
    return 'primary'
  }else if (status === '已投放第三方平台'){
    return 'success'
  }
}
// 重置搜索
const reset = () => {
  data.searchForm.category = ''
  data.searchForm.title = ''
  load()
}
//单个数据详情
const handleData = (row) => {
  window.open(`/DataDetails/${row.id}`, '_blank');
}
//单个删除
const delByTitle = (id) => {
  ElMessageBox.confirm('删除数据后无法恢复，您确定要删除吗？','删除确认',{type:'warning'}).then(() =>{
    request.delete('/advertisement/delete/' +id).then(res =>{
      ElMessage.success('删除成功')
      load() //删除后重新加载最新的数据
    }).catch(() => {
      ElMessage.error('删除失败');
    });
  }).catch()
}

// 批量删除
const handleBatchDelete = () => {
  // TODO: 实现批量删除逻辑
  console.log(data.selectedRows)
  if(data.selectedRows.length === 0){
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除数据后无法恢复，您确定要删除吗？','删除确认',{type:'warning'}).then(() =>{
    request.delete('/advertisement/deleteBatch', { data: data.selectedRows }).then(res =>{
      if(res.code === '200'){
        ElMessage.success('操作成功')
        load() //删除后重新加载最新的数据
      }else{
        ElMessage.error(res.msg)
      }
    })
  }).catch()
}

// 表格多选
const handleSelectionChange = (rows) => {
  data.selectedRows = rows.map(row => row.id)
}

</script>

<style scoped>
.ad-container {
  padding: 20px;
}

.search-area {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
}

.operation-area {
  margin-bottom: 20px;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
}

.avatar-uploader:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
  line-height: 178px;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>