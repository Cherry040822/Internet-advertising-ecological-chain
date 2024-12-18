<template>
  <div class="product-detail">
    <el-button @click="returnTopage">返回首页</el-button>
    <!-- 商品图片 -->
    <div class="image-section">
      <img :src="data.product.imageUrl" alt="商品图片" class="product-image" />
    </div>

    <!-- 商品信息 -->
    <div class="info-section">
      <h1 class="product-name">{{ data.product.name }}</h1>
      <p class="product-description">描述：{{ data.product.description }}</p>
      <p class="product-price">价格: ￥{{ data.product.price }}</p>

      <!-- 加入购物车按钮 -->
      <el-button
          type="warning"
          size="large"
          @click="addToCart(data.product)">
        加入购物车
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import {useRoute} from "vue-router";
import router from "@/router/index.js";
const data =reactive({
  product:{}

})
const route = useRoute();
const productId = route.query.id; // 从路由参数中获取商品 ID
// // 模拟一个商品数据
// const product = reactive({
//   productName: "示例宝贝",
//   description: "这是一个示例商品，用于展示宝贝详情页面。",
//   price: 199.99,
//   imageUrl: "https://via.placeholder.com/400x300" // 替换为实际图片地址
// });
// 获取商品详情
const loadProductDetails = () => {
  if (!productId) {
    console.error("商品 ID 未传递");
    return;
  }
  request.get(`/product/selectById/${productId}`).then((res) => {
    if (res.code === "200") {
      console.log("返回的商品数据：", res.data); // 打印后端返回数据
      data.product = res.data; // 直接赋值
    } else {
      console.error("加载商品详情失败:", res.msg);
    }
  }).catch((error) => {
    console.error("请求失败:", error);
  });
};

loadProductDetails()
// 加入购物车方法
const addToCart = (product) => {
  request.post("/cart/insert", product).then((res) => {
    if (res.code === "200") {
      ElMessage.success("成功加入购物车！");
    } else {
      ElMessage.error("加入购物车失败，请稍后再试！");
    }
  });
};
const returnTopage =()=>{
  router.push({ path: "/" });
}
</script>

<style scoped>
.product-detail {
  display: flex;
  gap: 20px;
  margin: 20px;
}

.image-section {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.product-image {
  max-width: 100%;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 400px;
  height: 300px;
}

.info-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 10px;
}

.product-name {
  font-size: 24px;
  font-weight: bold;
}

.product-description {
  font-size: 16px;
  color: #666;
}

.product-price {
  font-size: 20px;
  color: #ff5722;
  font-weight: bold;
}

.el-button {
  margin-top: 20px;
}

</style>
