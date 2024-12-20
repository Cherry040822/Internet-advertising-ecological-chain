<template>
  <div class="ad-container">
    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="广告分类">
          <el-select v-model="searchForm.category" placeholder="请选择分类" clearable>
            <el-option v-for="item in categories"
                       :key="item.value"
                       :label="item.label"
                       :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="广告标题">
          <el-input v-model="searchForm.title" placeholder="请输入广告标题"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="operation-area">
      <el-button type="danger" @click="handleBatchDelete">批量删除</el-button>
    </div>

    <!-- 广告列表 -->
    <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="title" label="广告标题"/>
      <el-table-column label="广告图片" width="120">
        <template #default="scope">
          <el-image :src="scope.row.image"
                    :preview-src-list="[scope.row.image]"
                    style="width: 100px; height: 100px"/>
        </template>
      </el-table-column>
      <el-table-column prop="content" label="详细内容" show-overflow-tooltip/>
      <el-table-column prop="category" label="分类"/>
      <el-table-column prop="keywords" label="关键词"/>
      <el-table-column prop="username" label="发布人"/>
      <el-table-column label="操作" width="300">
        <template #default="scope">
          <el-button link type="danger">删除</el-button>
          <el-button link type="success">投放</el-button>
          <el-button link type="info">数据</el-button>
        </template>
      </el-table-column>
      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
            v-model:current-page="pagination.currentPage"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </el-table>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue'
import { Plus } from '@element-plus/icons-vue'
import {ElMessage} from "element-plus";
import request from "@/utils/request.js";

// 广告分类选项
const categories = [
  { value: '1', label: '商业广告' },
  { value: '2', label: '品牌推广' },
  { value: '3', label: '活动宣传' },
  { value: '4', label: '其他' }
]

// 搜索表单数据
const searchForm = reactive({
  category: '',
  title: ''
})

// 表格数据
const tableData = ref([])
const selectedRows = ref([])

// 弹窗相关
const dialogVisible = ref(false)
const dialogType = ref('add')
const adForm = reactive({
  title: '',
  image: '',
  content: '',
  category: '',
  keywords: ''
})

// 重置搜索
const resetSearch = () => {
  searchForm.category = ''
  searchForm.title = ''
  load()
}

// 新增广告
const handleAdd = () => {
  dialogType.value = 'add'
  dialogVisible.value = true

}

// 批量删除
const handleBatchDelete = () => {
  // TODO: 实现批量删除逻辑
}

// 表格多选
const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

// 图片上传成功回调
const handleUploadSuccess = (res) => {
  console.log(res.data.image)
  adForm.image = res.data
}
// 分页相关数据
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
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

// 修改搜索方法
const load = () => {
  request.get('/advertisementList/selectPage',{
    params: {
      pageNum:pagination.currentPage,
      pageSize:pagination.pageSize,
    }
  }).then(res =>{
    tableData.value =res.data.list
  })
}

onMounted(() => {
  load()
})
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