import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'MainPage',
    component: () => import('../views/MainPage.vue')
  },
  {
    path: '/mypage',
    name: 'MyPage',
    component: () => import('../views/MyPage.vue')
  },{
    path: '/payments',
    name: 'PaymentsPage',
    component: () => import('../views/PaymentsPage.vue')
  },
  {
    path: '/search',  
    name: 'SearchPage',
    component: () => import('../views/SearchPage.vue')
  },
  {
    path: "/success",
    name: "Success",
    component: () => import("@/views/SuccessPage.vue")
  },
  {
    path: "/payments/process/:id",
    name: "PaymentProcess",
    component: () => import("@/views/PaymentProcess.vue")
  },
 
  {
    path: '/myticket',
    name: 'MyTicketPage',
    component: () => import('../views/MyTicketPage.vue')
  },
  {
    path: '/login',
    name: 'LoginPage',
    component: () => import('../views/LoginPage.vue')
  },
  {
    path: '/mypage/edit',
    name: 'EditMyInfoPage',
    component: () => import('../views/EditMyInfoPage.vue')
  },
  {
    path: '/myticket/cancel',  
    name: 'Cancel',
    component: () => import('@/views/CancelPage.vue')
  },{
    path: '/signup',
    name: 'SignupPage',
    component: () => import('@/views/SignupPage.vue')
  },{
    path: '/myticket/list',
    name: 'MyTicketListPage',
    component: () => import('@/views/MyTicketListPage.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;