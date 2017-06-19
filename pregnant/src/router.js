import VueRouter from 'vue-router'
import Vue from 'vue'
Vue.use(VueRouter)
import Login from '@/view/login'
import Check from '@/view/check/Check'
import Record from '@/view/check/Record'
import Cycle from '@/view/check/Cycle'
import UserInfo from '@/view/UserInfo'
import Foot from '@/view/foot/Foot'
import shoeHome from '@/view/shoe/shoeHome'
import shoeDetail from '@/view/shoe/shoeDetail'
import Order from '@/view/shoe/order'
import buyShoe from '@/view/shoe/buyShoe'
import Address from '@/view/shoe/address'
import Pay from '@/view/shoe/pay'

// 根目录
const rootPath = ''

var routes = [
  {
    path: '/login', alias: '/', 
    component: Login, 
    name: 'login',
    meta: { 
      auth: true
    }
  },
	{
    path: '/check',
    component: Check, 
    name: 'check',
    meta: { 
      // auth: true,
      share: true
    }
	},
  {
    path: '/cycle',
    component: Cycle, 
    name: 'Cycle',
    meta: { 
      share: true
    }
  },
	{
    path: '/record',
    component: Record,
    meta: { 
      share: false
    }
	},
  {
    path: '/userInfo',
    component: UserInfo,
    meta: { 
      // auth: true,
      share: false
    }
  },
  {
    path: '/foot',
    component: Foot,
    meta: { 
      // auth: true
    }
  },
  {
    path: '/shoeHome',
    component: shoeHome,
    meta: { 
      share: true
    }
  },  
  {
    path: '/shoeDetail',
    component: shoeDetail,
    meta: { 
      auth: true,
      share: true
    }
  },
  {
    path: '/buyShoe',
    component: buyShoe
  },
  {
    path: '/order',
    component: Order
  },
  {
    path: '/address',
    component: Address
  },
  {
    path: '/pay',
    component: Pay
  }
].map(route => {
  route.path = rootPath + route.path
  return route
})

const router = new VueRouter({
  mode: 'history',
  base: process.env.NODE_ENV == 'development' ? '': '/yxd/',
  routes
})

router.beforeEach((to, from, next) => {
  //只能微信上访问
  // if (!to.matched.some(record => record.meta.notNeedWX)) {  //没有配置notNeedWX的路由都要判断是否在微信上登录
  //   if(!g.isInWeiXin()) {
  //     console.log('plase use wx....')
  //     next('/useWX')
  //     return
  //   }
  // }
  if (to.matched.some(record => !record.meta.auth)) {
    // this route requires auth, check if logged in
    // if not, redirect to login page.
    var isLogin = window.Global.s.state.isLogin
    if (!isLogin) {
      next({
        path: '/',
        query: { 
          redirect: to.fullPath,
          oid:localStorage.oid
        }
      })
    } else {
      next()
    }
  } else {
    next() // 确保一定要调用 next()
  }
})

import wxApi from './utils/wxApi.js'
router.afterEach(route => {
    if (process.env.NODE_ENV == 'development') return;

    localStorage.page = route.fullPath.substring(1); //保存当前路由 刷新的时候用
    window.setTimeout(wxApi.init.bind(this,route.meta.share),50)  //加个延时 要不location.href 还是旧的路由
    
})


export default router
