import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/Index',
      name: 'Index',
      component: () => import('@/views/Index.vue'),
      meta: { title: '广告投放与管理' },
      children: [
        { path: '/AdvertisementManage',meta: { title: '广告信息' }, component: () => import('@/views/AdvertisementManage.vue')},
        { path: '/UserManage',meta: { title: '用户信息' }, component: () => import('@/views/UserManage.vue')},
        { path: '/AdvertisingManage', meta: { title: '投放管理' }, component: () => import('@/views/ConfigManage.vue')},
        ]
    },
    { path: '/', name: 'Login', meta:{ title:'登录' }, component: () => import('@/views/Login.vue')},
    { path: '/Register', name: 'Register', meta:{ title:'注册' }, component: () => import('@/views/Register.vue')},
  ],
})

router.beforeEach((to,from,next) => {
  document.title = to.meta.title
  next()
})
export default router
