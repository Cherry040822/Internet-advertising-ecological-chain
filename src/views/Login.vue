<template>
  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 30px; text-align: center; margin-bottom: 30px; color: #3f70b9">欢迎登录广告管理系统</div>
      <el-form :model="data.form" ref="formRef" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.name" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item prop="role">
          <el-select style="width: 100%" v-model="data.form.role">
            <el-option value="USER" label="用户"></el-option>
            <el-option value="WEBMASTER" label="互联网站长"></el-option>
            <el-option value="ADMIN" label="管理员"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="large" style="width: 100%;background-color: #3f70b9;color: white" @click="login">登 录</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align: right;">
        还没有账号？请 <a href="/Register">注册</a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { User, Lock } from "@element-plus/icons-vue";
import request from "@/utils/request";
import { ElMessage, ElMessageBox } from "element-plus";
import router from "@/router";

const user = JSON.parse(localStorage.getItem('canteen-user') || '{}');
const data = reactive({
  userData: {},
  dialogVisible: false,
  form: {
    role: 'USER',
  },
  rules: {
    name: [
      { required: true, message: '请输入账号', trigger: 'blur' },
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
    ],
  }
})

const formRef = ref()

// 登录函数
const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request
          .post("/login", data.form)
          .then((res) => {
            if (res.code === "200") {
              // 登录成功
              data.userData = {
                id: res.data.id,  // 用户 ID
                name: res.data.name,  // 用户名
                role: res.data.role,  // 用户角色
              };
              localStorage.setItem("user", JSON.stringify(data.userData));
              ElMessage.success("登录成功");
              if (data.form.role === "USER") {
                router.push("/Index")
              } else if (data.form.role === "ADMIN") {
                router.push("/Index");
              } else if (data.form.role === "WEBMASTER"){
                router.push("/Index")
              }
            } else {
              // 登录失败，显示错误信息
              ElMessage.error("用户名或密码错误");
            }
          })
          .catch((error) => {
            console.error(error);
            ElMessage.error("登录请求失败，请稍后再试");
          });
    }
  });
};

</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: right;
  align-items: center;
  background: url("@/assets/bg.jpg");
  background-size: cover;
}
.login-box {
  width: 350px;
  padding: 50px 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0,.1);
  background-color: rgba(255, 255, 255, .9);
  margin:100px;
}
</style>
