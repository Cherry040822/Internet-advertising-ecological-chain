<template>
  <h1>1111</h1>
</template>


<script setup>
import {reactive} from "vue";
import {v4 as uuidv4} from "uuid";
import Cookies from "js-cookie";
import request from "@/utils/request.js";
import { onMounted } from 'vue';

const data = (reactive({
  web_user:''
}))

data.web_user=uuidv4()
const selectUser = () =>{
  const web_user=Cookies.get('web_user')
  console.log(web_user)
  request.get(`webUser/selectByUser/${web_user}`).then(res=>{

    if(res.code === '500'){
      Cookies.set('web_user', data.web_user, {
        path: '/',
        domain: '',  // 可以设置为父页面的域名
        sameSite: 'None',
        secure: true   // 必须设置为 true
      });

      request.get(`webUser/add/${data.web_user}`).then(res=>{})
    }
    // 将 cookie 通过 postMessage 发送到父页面
    window.parent.postMessage({ web_user: Cookies.get('web_user') }, "*");
  })
}
onMounted(() => {
  selectUser();
});
</script>