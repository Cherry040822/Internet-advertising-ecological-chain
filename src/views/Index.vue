<template>
  <div>
    <div style="height: 60px;display: flex; align-items: center; border-bottom: 1px solid #ddd; background-color: #1d66a8 ">
      <div style="flex: 1;">
        <div style="padding-left: 20px; display: flex; align-items: center;">
          <img src="@/assets/header.png" alt="" style="width: 40px">
          <div style="font-weight: bold; font-size: 30px; margin-left: 5px; color: rgba(255,255,255,0.87);">广告投放与管理系统</div>
        </div>
      </div>
      <div style="width: fit-content; padding-right: 10px; display: flex; align-items: center;" >
        <span>
            <img :src="'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="" style="width: 40px; height: 40px; border-radius: 50%">
          </span>
        <span style="color: rgba(255,255,255,0.87);margin-left: 5px">{{data.user.name}}</span>
      </div>
    </div>

    <div style="display: flex">
      <div style="width: 200px; border-right: 1px solid #ddd;min-height: calc(100vh - 60px);background-color: #acc2dc;">
        <el-menu
            router
            style="border: none;"
            :default-active="$route.path"
            :default-openeds="['/home', '2']"

        >
          <el-menu-item index="/AdvertisementManage" v-if="data.user.role === 'ADMIN'">
            <el-icon><Dish /></el-icon>
            <span>广告信息</span>
          </el-menu-item>
          <el-menu-item index="/UserManage" v-if="data.user.role === 'ADMIN'">
            <el-icon><Bowl /></el-icon>
            <span>用户信息</span>
          </el-menu-item>
          <el-menu-item index="/AdvertisingManage" v-if="data.user.role === 'ADMIN'">
            <el-icon><Bowl /></el-icon>
            <span>投放管理</span>
          </el-menu-item>
          <el-sub-menu v-if="data.user.role === 'ADMIN'" >
            <template #title>
              <el-icon><Bowl /></el-icon>
              <span>收益管理</span>
            </template>
            <el-menu-item index="/daily">
              <span style="margin-left: 20px">每日报表</span>
            </el-menu-item>
            <el-menu-item index="/monthly">
              <span style="margin-left: 20px">每月报表</span>
            </el-menu-item>
            <el-menu-item index="/annual">
              <span style="margin-left: 20px">每年报表</span>
            </el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/person">
            <el-icon><User /></el-icon>
            <span>个人资料</span>
          </el-menu-item>
          <el-menu-item index="login" @click="logout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出系统</span>
          </el-menu-item>
        </el-menu>
      </div>

      <div style="flex: 1; height: 0; background-color: rgba(241,240,240,0.69)">
        <router-view @updateUser="updateUser" />
      </div>
    </div>

  </div>
</template>

<script setup>
import {reactive} from "vue";
import { useRoute } from 'vue-router'
import {DataAnalysis, Dish, EditPen, List, SwitchButton, User, UserFilled, ZoomIn} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import Cookies from "js-cookie"
const $route = useRoute()

const data = reactive({
  user: JSON.parse(localStorage.getItem('user') || '{}')
})

const logout = () => {
  localStorage.removeItem('canteen-user')
  router.push('/')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem("user"))
}
Cookies.set('adb',123),{
  path:'/',
  domain:'',
  sameSite:'Lax'
}
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: #e0e4ff !important;
}
.el-menu-item:hover {
  background-color: #e9eeff !important;
  color: #1450aa;
}
.el-sub-menu-item:hover {
  background-color: #e9eeff !important;
  color: #1450aa;
}
:deep(th)  {
  color: #333;
}

:deep(.el-menu) {
  background-color: transparent !important;
  border-right: none !important;
}

:deep(.el-menu-item),
:deep(.el-sub-menu__title) {
  color: #031c38 !important;  /* 浅色文字 */
}

:deep(.el-menu-item:hover),
:deep(.el-sub-menu__title:hover) {
  background-color: #263445 !important;
  color: #fff !important;
}

:deep(.el-menu-item.is-active) {
  background-color: #263445 !important;  /* 选中项的背景色 */
  color: #fff !important;
}

/* 子菜单弹出层样式 */
:deep(.el-menu--popup) {
  background-color: #304156 !important;
}

:deep(.el-menu--popup .el-menu-item) {
  color: #bfcbd9 !important;
}

:deep(.el-menu--popup .el-menu-item:hover) {
  background-color: #263445 !important;
  color: #fff !important;
}
.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
  border:none !important;
}
/* 去掉el-dropdown-menu的边框 */
.el-dropdown-menu {
  border: none !important;
}

/* 去掉el-dropdown-item的边框和背景 */
.el-dropdown-menu .el-dropdown-item:hover {
  background-color: transparent !important;
  border: none !important;
}
</style>