import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/ProductPage.vue'),
    },
    {
      path: '/ProductDetail',
      name: 'ProductDetail',
      component:()=>import('../views/ProductDetail.vue')
    },
  ],
})

export default router
