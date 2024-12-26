<template>
  <div style="padding: 20px">
    <el-card style="margin-bottom: 5px;">
      <div style="display: flex; justify-content: space-between; align-items: center;">
    <span>
      <h3>投放管理</h3>
    </span>
        <span>
      <el-button type="primary" round @click="handleChange">保存</el-button>
    </span>
      </div>
    </el-card>
    <el-card style="margin-bottom: 5px">
      <div style="display: flex; align-items: flex-start;height: 100%; width: 100%;">
        <!-- 左侧收益配置部分 -->
        <div style="width: 12px; text-align: center;padding-right:20px;border-right: 1px solid #8c8c8c; height: 100%;">
          <h3>收益配置</h3>
        </div>
        <!-- 右侧三个滑动条部分 -->
        <div style="display: flex; flex-direction: column; padding-left: 30px; width: 1000px; height: 100%;padding-top: 15px">
          <div class="slider-demo-block">
            <span style="width: 120px">广告主收益</span>
            <el-slider v-model="data.userMoney" show-input style="width:500px" @change="syncValues"/>
          </div>
          <div class="slider-demo-block">
            <span style="width: 120px">互联网站长收益</span>
            <el-slider v-model="data.webMoney" show-input style="width:500px" @change="syncValues"/>
          </div>
          <div class="slider-demo-block">
            <span style="width: 120px">管理员收益</span>
            <el-slider v-model="data.admMoney" show-input @change="onAdmMoneyChange" style="width:500px"/>
          </div>
        </div>
      </div>
    </el-card>
    <el-card style="margin-bottom: 5px">
      <span style="width: 600px">
        <span style="width: 100px">广告统一投放时间</span>
        <span class="example-basic" style="width: 300px">
          <el-time-picker v-model="data.time"
                          placeholder="Arbitrary time"
                          format="HH:mm:ss"
                          value-format="HH:mm:ss"
                          style="width: 150px"/>
        </span>
      </span>
      <span style="padding-left: 30px">
        <span style="padding-right:8px">投放间隔天数</span>
        <el-select v-model="data.day"placeholder="Select" style="width: 80px">
          <el-option
              v-for="item in data.range"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
        <span style="padding-left: 8px">天</span>
      </span>
    </el-card>
    <el-card style="margin-bottom: 5px">
      <span style="padding-right:8px">单次点击量收益</span>
      <el-input-number v-model="data.money" :min="1" :max="100" style="width: 120px"/>
      <span style="padding-left: 8px">元</span>
    </el-card>
    <el-card style="margin-bottom: 5px">
      <span style="width: 600px">
        <span style="width: 100px">广告统计点击量时间</span>
        <span class="example-basic" style="width: 300px">
          <el-time-picker v-model="data.moneyTime"
                          placeholder="Arbitrary time"
                          format="HH:mm:ss"
                          value-format="HH:mm:ss"
                          style="width: 150px"/>
        </span>
      </span>
    </el-card>
  </div>
</template>

<script setup>
import { nextTick, reactive, watch } from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

// 定义数据结构
const data = reactive({
  userMoney:0,
  webMoney: 0,
  admMoney:0,
  time: '',
  day: null,
  money: null,
  moneyTime: '',
  range: [
    { value: 1, label: 1 },
    { value: 2, label: 2 },
    { value: 3, label: 3 },
    { value: 4, label: 4 },
    { value: 5, label: 5 },
    { value: 6, label: 6 },
    { value: 7, label: 7 }
  ],
});

// 从后台加载数据
const load = () => {
  request.get("/config/select").then(res => {
    console.log('接口返回的数据:', res); // 打印返回数据
    if (res && res.data) {
          data.userMoney = res.data.userMoney,
          data.webMoney = res.data.webMoney,
          data.admMoney = res.data.admMoney,
          data.time = res.data.time,
          data.day = res.data.day,
          data.money = Number(res.data.money),
          data.moneyTime = res.data.moneyTime
    } else {
      console.error("返回的数据格式不正确");
    }
  }).catch(err => {
    console.error("请求失败", err);
  });
};


load();

let isUpdating = false; // 防止递归更新
let isAdmMoneyChanging=false;
const syncValues = () => {
  const total = data.userMoney + data.webMoney + data.admMoney;
  const target = 100;

  if (isAdmMoneyChanging) {
    // 修改了 admMoney，调整 userMoney 和 webMoney
    if (total > target) {
      const diff = total - target;
      // 如果 admMoney 是最大值，减去差额，确保总和为100
      if (data.webMoney >= diff) {
        data.webMoney -= diff;
      } else {
        data.userMoney -= diff / 2;
        data.admMoney = target - (data.webMoney + data.userMoney);
      }
      console.log('2');  // 此处打印
    }
    if (total < target) {
      const diff = target - total;
      // 如果 admMoney 可用，直接加差额
      if (data.webMoney + diff <= target) {
        data.webMoney += diff;
      } else {
        // 否则根据比例调整
        data.userMoney += diff / 2;
        data.admMoney = target - (data.webMoney + data.userMoney);
      }
      console.log('3');  // 此处打印
    }
  } else {

    if (isUpdating) return; // 防止递归更新
    // 处理其它情况（例如 userMoney 或 webMoney 改动时）
    if (total > target) {
      const diff = total - target;
      isUpdating = true;

      // 如果 admMoney 是最大值，减去差额，确保总和为100
      if (data.admMoney >= diff) {
        data.admMoney -= diff;
      } else {
        // 否则修改 userMoney 和 webMoney
        data.webMoney -= diff / 2;
        data.userMoney = target - (data.webMoney + data.admMoney);
      }

      isUpdating = false;
    }

    if (total < target) {
      const diff = target - total;

      isUpdating = true;

      // 如果 admMoney 可用，直接加差额
      if (data.admMoney + diff <= target) {
        data.admMoney += diff;
      } else {
        // 否则根据比例调整
        data.webMoney += diff / 2;
        data.userMoney = target - (data.webMoney + data.admMoney);
      }

      isUpdating = false;
    }
  }
};

// 监听 userMoney 和 webMoney 的变化
watch(() => data.userMoney, syncValues);
watch(() => data.webMoney, syncValues);

// 修改 admMoney 时，保持总和为100
const onAdmMoneyChange = (value) => {
  if (isUpdating) return;  // 防止递归更新
  isUpdating = true; // 标记正在修改 admMoney
  isAdmMoneyChanging = true;

  data.admMoney = value;

  // 在修改 admMoney 后，手动同步更新 userMoney 和 webMoney
  syncValues();  // 触发同步逻辑，调整 userMoney 和 webMoney

  isUpdating = false; // 恢复标记
  isAdmMoneyChanging = false;
  console.log('1');  // 此处打印
};

const handleChange = () =>{ //更新操作里包含id
  request.put('/config/update', data).then(res =>{
    if(res.code === '200'){
      ElMessage.success('操作成功')
      load() //更新后重新加载最新的数据
    }else{
      ElMessage.error(res.msg)
    }
  })
}
</script>

<style>
.slider-demo-block {
  display: flex;
  align-items: center;
}
.slider-demo-block .el-slider {
  margin-top: 0;
  margin-left: 12px;
}
.example-basic .el-date-editor {
  margin: 8px;
}
</style>
