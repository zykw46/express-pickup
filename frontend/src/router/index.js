import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/common/Login.vue'),
    meta: { public: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/common/Register.vue'),
    meta: { public: true }
  },
  {
    path: '/admin',
    name: 'AdminLayout',
    component: () => import('@/layouts/AdminLayout.vue'),
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '首页', icon: 'HomeFilled' }
      },
      {
        path: 'user',
        name: 'UserManage',
        component: () => import('@/views/admin/UserManage.vue'),
        meta: { title: '用户管理', icon: 'UserFilled' }
      },
      {
        path: 'role',
        name: 'RoleManage',
        component: () => import('@/views/admin/RoleManage.vue'),
        meta: { title: '角色管理', icon: 'Key' }
      },
      {
        path: 'station',
        name: 'StationManage',
        component: () => import('@/views/admin/StationManage.vue'),
        meta: { title: '快递站点', icon: 'OfficeBuilding' }
      },
      {
        path: 'spec',
        name: 'SpecManage',
        component: () => import('@/views/admin/SpecManage.vue'),
        meta: { title: '包裹规格', icon: 'Box' }
      },
      {
        path: 'building',
        name: 'BuildingManage',
        component: () => import('@/views/admin/BuildingManage.vue'),
        meta: { title: '宿舍楼', icon: 'School' }
      },
      {
        path: 'order',
        name: 'OrderManage',
        component: () => import('@/views/admin/OrderManage.vue'),
        meta: { title: '订单管理', icon: 'Document' }
      },
      {
        path: 'certification',
        name: 'CertificationManage',
        component: () => import('@/views/admin/CertificationManage.vue'),
        meta: { title: '配送员审核', icon: 'User' }
      },
      {
        path: 'notice',
        name: 'NoticeManage',
        component: () => import('@/views/admin/NoticeManage.vue'),
        meta: { title: '通知公告', icon: 'Bell' }
      }
    ]
  },
  {
    path: '/delivery',
    name: 'DeliveryLayout',
    component: () => import('@/layouts/DeliveryLayout.vue'),
    redirect: '/delivery/hall',
    children: [
      {
        path: 'hall',
        name: 'DeliveryHall',
        component: () => import('@/views/delivery/DeliveryHall.vue'),
        meta: { title: '接单大厅', icon: 'ShoppingBag' }
      },
      {
        path: 'record',
        name: 'DeliveryRecord',
        component: () => import('@/views/delivery/DeliveryRecord.vue'),
        meta: { title: '接单记录', icon: 'DocumentCopy' }
      },
      {
        path: 'income',
        name: 'DeliveryIncome',
        component: () => import('@/views/delivery/DeliveryIncome.vue'),
        meta: { title: '我的收益', icon: 'Money' }
      }
    ]
  },
  {
    path: '/user',
    name: 'UserLayout',
    component: () => import('@/layouts/UserLayout.vue'),
    redirect: '/user/home',
    children: [
      {
        path: 'home',
        name: 'UserHome',
        component: () => import('@/views/user/UserHome.vue'),
        meta: { title: '首页', icon: 'HomeFilled' }
      },
      {
        path: 'order',
        name: 'UserOrder',
        component: () => import('@/views/user/UserOrder.vue'),
        meta: { title: '我的订单', icon: 'Document' }
      },
      {
        path: 'create-order',
        name: 'CreateOrder',
        component: () => import('@/views/user/CreateOrder.vue'),
        meta: { title: '下单', icon: 'Plus' }
      },
      {
        path: 'recharge',
        name: 'UserRecharge',
        component: () => import('@/views/user/UserRecharge.vue'),
        meta: { title: '账户充值', icon: 'Money' }
      },
      {
        path: 'certification',
        name: 'UserCertification',
        component: () => import('@/views/user/UserCertification.vue'),
        meta: { title: '配送员认证', icon: 'User' }
      },
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('@/views/user/UserProfile.vue'),
        meta: { title: '个人中心', icon: 'UserFilled' }
      }
    ]
  },
  {
    path: '/',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.public) {
    next()
    return
  }
  
  if (!userStore.token) {
    next('/login')
    return
  }
  
  const roleCode = userStore.roleCode
  if (to.path.startsWith('/admin') && roleCode !== 'admin') {
    next('/login')
    return
  }
  
  if (to.path.startsWith('/delivery') && roleCode !== 'delivery' && roleCode !== 'admin') {
    next('/login')
    return
  }
  
  next()
})

export default router
