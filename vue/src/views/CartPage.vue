<template>
  <!--      购物车-->
  <div style="width:250px;background-color: #FAFAFA">
    <span style="text-align: center;display: block;font-size: large">购物车</span>
    <div>
      <el-card
          v-for="(item, index) in data.cartItems"
          :key="item.id"
          style="max-width: 1300px; margin-bottom: 5px;background-color: rgba(232,183,80,0.99)"
      >
        <template #header>{{ item.name }}</template>
<!--        <img :src="item.image" style="width: 100%"/>-->
        <div>单价：{{ item.price }}</div>
        <!-- 加购按钮和数量框 -->
        <div class="card-footer" style="margin-top: 10px; display: flex; align-items: center; justify-content: space-between;">
          <!-- 显示“+”和“-”按钮以及数量 -->
          <div style="display: flex; align-items: center;">
            <!-- 减少数量的按钮 -->
<!--            <el-button @click="decrease(item)" size="small" style="padding: 0 10px;">-->
              <el-button size="small" style="padding: 0 10px;">
              -
            </el-button>

            <!-- 数量显示框 -->
            <span style="width: 40px; text-align: center; font-size: 16px; margin: 0 10px;">
      {{ item.quantity }}
    </span>

            <!-- 增加数量的按钮 -->
<!--            <el-button @click="increase(item)" size="small" style="padding: 0 10px;">-->
              <el-button  size="small" style="padding: 0 10px;">
              +
            </el-button>
          </div>

          <!-- 加入购物车按钮 -->
<!--          <el-button-->
<!--              type="danger"-->
<!--              @click="removeFromCart(item)"-->
<!--              style="margin-left: 10px; padding: 5px 20px; height: 32px;"-->
<!--          >-->
<!--            删除-->
<!--          </el-button>-->
          <el-button
              type="danger"
              style="margin-left: 10px; padding: 5px 20px; height: 32px;"
          >
            删除
          </el-button>
        </div>
      </el-card>
      <div style="font-size: large; display: flex; justify-content: center; align-items: center;">
        <el-button
            type="primary"
        >下单</el-button>
      </div>
    </div>
  </div>
</template>
<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
const data=reactive({
  cartItems:[],
})
const loadcart=()=>{
  request.get("/cart/selectAll").then(res=>{
    data.cartItems=res.data
  })
}
loadcart()
</script>
