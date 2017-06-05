import VueRouter from 'vue-router'
import Vue from 'vue'
Vue.use(VueRouter)
import Login from '@/view/login'
import Check from '@/view/check/Check'
import Record from '@/view/check/Record'
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
    meta: { auth: true }
  },
	{
    path: '/check',
    component: Check
	},
	{
    path: '/record',
    component: Record
	},
  {
    path: '/userInfo',
    component: UserInfo
  },
  {
    path: '/foot',
    component: Foot
  },
  {
    path: '/shoeHome',
    component: shoeHome
  },  
  {
    path: '/shoeDetail',
    component: shoeDetail
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
          wxid:localStorage.wxid,
          type:localStorage.type,
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

export default router
