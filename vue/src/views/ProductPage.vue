<template>
  <div class="product-page">
    <!-- 广告区域 -->
    <div class="ad-section" ></div>
                                             <!-- 分类菜单栏 -->
    <div class="menu-bar" style="
      margin-bottom: 20px;
      display: flex;
      gap: 10px;
      padding: 10px;
      border: 1px solid #e0e0e0;
      border-radius: 8px;
      box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
      background-color: #ffffff;
    ">
      <el-select v-model="selectedCategory" placeholder="选择分类" @change="selectByCategory" style="width: 200px;">
        <el-option
            v-for="category in data.categories"
            :key="category"
            :label="category"
            :value="category">
        </el-option>
      </el-select>
      <el-button @click="loadproducts" type="warning">显示所有宝贝</el-button>
    </div>


                                            <!--  顶部搜索栏-->
    <div>
      <span style="margin-right: 10px; margin-left: 10px; font-size: xx-large; color: orange; font-family: 'Times New Roman', sans-serif;">购物中心</span>
      <el-input
          v-model="data.searchText"
          placeholder="搜索宝贝名"
          clearable
          style="margin-bottom: 20px;margin-top: 20px;width: 70%;">
      </el-input>
      <el-button
          style="margin-left: 10px;"
          type="warning"
          @click="selectByname"
      >
        搜索
      </el-button>
      <el-button @click="showCart = true" style="font-size: large; margin-left: 10px">购物车</el-button>
    </div>
                                               <!-- 商品列表 -->
    <el-row :gutter="20" class="product-list">
      <el-col :span="6" v-for="product in data.items" :key="product.productId">
        <el-card shadow="hover" class="product-card">
          <div class="product-content" style="display: flex; align-items: center;">
            <div class="product-info" style="flex: 1;">
              <h3 class="product-name">{{ product.name }}</h3>
              <p class="product-price">￥{{ product.price }}</p>
            </div>
            <img
                :src="product.imageUrl"
                alt="商品图片"
                class="product-image"
            />
          </div>
          <div class="product-actions" style="margin-top: 10px;">
            <el-button size="small" @click="viewDetails(product.productId)">查看详情</el-button>
            <el-button type="warning" size="small" @click="insertcart(product)">加入购物车</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!--弹窗内容-->
    <!-- 购物车弹窗 -->
    <el-dialog v-model="showCart" title="购物车" width="30%">
      <!-- 嵌入购物车页面的内容 -->
      <div class="cartItems">
        <el-card
            v-for="(item, index) in data.cartItems"
            :key="item.id"
            style="max-width: 1300px; margin-bottom: 5px; background-color: rgb(255,255,255); border: 1px solid orange; "
        >
          <template #header>{{ item.name }}</template>
          <div>单价：{{ item.price }}</div>
          <!-- 加购按钮和数量框 -->
          <div class="card-footer" style="margin-top: 10px; display: flex; align-items: center; justify-content: space-between;">
            <!-- 显示“+”和“-”按钮以及数量 -->
            <div style="display: flex; align-items: center;">
              <!-- 减少数量的按钮 -->
              <el-button @click="decrease(item)" size="small" style="padding: 0 10px;">
                -
              </el-button>

              <!-- 数量显示框 -->
              <span style="width: 40px; text-align: center; font-size: 16px; margin: 0 10px;">
      {{ item.quantity }}
    </span>

              <!-- 增加数量的按钮 -->
              <el-button @click="increase(item)" size="small" style="padding: 0 10px;">
                +
              </el-button>
            </div>

            <!-- 加入购物车按钮 -->
            <el-button
                type="danger"
                @click="removeFromCart(item)"
                style="margin-left: 10px; padding: 5px 20px; height: 32px;"
            >
              删除
            </el-button>
          </div>
        </el-card>
        <div style="font-size: large; display: flex; justify-content: center; align-items: center;">
          <el-button type="primary">下单</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>


<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
const data=reactive({
  items:[],
  cartItems:[],
  seacherText:'',
  categories:[]
})
// 获取商品列表
const loadproducts =()=>{
  request.get("/product/selectall").then(res=>{
    if (res.code === '200') {
      data.items = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadproducts()

const insertcart =(product)=>
  request.post("/cart/insert",product).then(res=>{
    ElMessage.success("成功加入购物车！");
    loadcart()
  }).catch((error) => {
    ElMessage.error(error.response.data.msg || "加入购物车失败");
  });

const loadcart=()=>{
  request.get("/cart/selectAll").then(res=>{
    if (res.code === "200") {
      data.cartItems = res.data;
    } else {
      ElMessage.error(res.msg);
    }
  })
}
loadcart()

const showCart = ref(false); // 控制购物车弹窗的显示

const increase = (item) => {
  // if (item.quantity == null) {
  //   item.quantity = 0;
  // }
  item.quantity += 1;
  request.post("/cart/increasequantity",item).then((res)=>{
    loadcart()
  })
  console.log('Increasing quantity:', item);  // 调试日志

};

const decrease = (item) => {
  // if (item.quantity == null) {
  //   item.quantity = 0;
  // }
  if (item.quantity > 0) {
    item.quantity -= 1;
    request.post("/cart/decreasequantity",item).then((res)=>{
     loadcart()
    })
  }
};


const removeFromCart = (item) => {
  const productId = item.productId; // 从 item 中获取 dishId

  // 调用后端接口删除菜品
  request.delete(`/cart/delete/${productId}`)

      .then((response) => {
        // 删除成功后从本地数据中删除该菜品
        loadcart()
        console.log("菜品删除成功:", response);
      })
      .catch((error) => {
        console.error("删除菜品失败:", error);
      });
};

const selectByname = () => {
  console.log("触发了搜索");

  // 判断输入是否为空
  if (!data.searchText.trim()) {
    console.log("搜索框为空");
    loadproducts()
    return;
  }

  console.log("搜索的宝贝名称:", data.searchText);

  // 发起 GET 请求，传递查询参数
  request.get('/product/selectByname', {
    params: { name: data.searchText }
  }).then((res) => {
    console.log("返回的宝贝数据:", res);

    // 判断返回数据类型，如果是对象，封装成数组
    if (res.data) {
      if (Array.isArray(res.data)) {
        data.items = res.data; // 如果返回的是数组，直接赋值
      } else {
        data.items = [res.data]; // 如果返回的是单个对象，封装成数组
      }
    } else {
      data.items = []; // 返回空时，清空商品列表
    }
  }).catch((error) => {
    console.error("请求宝贝数据失败:", error);
    data.items = []; // 请求失败时清空数据
  });
};

const selectByCategory =(category)=>{
  request.get("product/selectByCategory",{
    params: { category } // 将 category 作为查询参数传递
  }).then(res=>{
    if (res.code === "200") {
      data.items = res.data;
    } else {
      ElMessage.error("搜索加载失败");
    }
  })
}
// 加载分类名
const loadCategories = () => {
  request.get("/product/categories").then(res => {
    if (res.code === "200") {
      data.categories = res.data; // 例如: ["电子产品", "服装", "家居"]
    } else {
      ElMessage.error("分类加载失败");
    }
  });
};
loadCategories();


const viewDetails = (productId) => {
  // 跳转到详情页面，传递商品ID作为路由参数
  router.push({ path: '/ProductDetail', query: { id: productId } });
};
</script>
<style>
.product-card {
  border: 1px solid orange; /* 设置边框颜色为橙色 */
}

.product-card:hover {
  border-color: darkorange; /* 悬浮时边框变成深橙色 */
}
.product-image{
  width: 100px;
  height: 100px;
  object-fit: cover;
  margin-left: 10px;
}

</style>