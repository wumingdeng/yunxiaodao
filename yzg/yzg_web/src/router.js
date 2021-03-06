import VueRouter from 'vue-router'
import Vue from 'vue'
Vue.use(VueRouter)
import Login from '@/view/login'
// import Check from '@/view/check/Check'
// const Check = resolve => require(['@/view/check/Check'], resolve)
const Check = r => require.ensure([], () => r(require('@/view/check/Check')), 'moment')
const Record = resolve => require(['@/view/check/Record'], resolve)
// import Record from '@/view/check/Record'
import Cycle from '@/view/check/Cycle'
// import UserInfo from '@/view/UserInfo'
// const UserInfo = resolve => require(['@/view/UserInfo'], resolve)
const UserInfo = r => require.ensure([], () => r(require('@/view/UserInfo')), 'moment')
import Foot from '@/view/foot/Foot'
import shoeHome from '@/view/shoe/shoeHome'
import shoeDetail from '@/view/shoe/shoeDetail'
import Order from '@/view/shoe/order'
import OrderDetail from '@/view/shoe/orderDetail'
import buyShoe from '@/view/shoe/buyShoe'
// const Address = resolve => require(['@/view/shoe/address'], resolve)
import Address from '@/view/shoe/address'
import Pay from '@/view/shoe/pay'

import UserHome from '@/view/userCenter/userHome'
import QRCode from '@/view/userCenter/qrcode'
import TgOrder from '@/view/userCenter/tgOrder'
import Share from '@/view/userCenter/Share'
import mySaleman from '@/view/userCenter/mySaleman'
// import fillInfo from '@/view/userCenter/fillInfo'
const fillInfo = resolve => require(['@/view/userCenter/fillInfo'], resolve)
import finishRequest from '@/view/userCenter/finishRequest'
import develop from '@/view/userCenter/develop'
import requestList from '@/view/userCenter/requestList'
import reviewRequest from '@/view/userCenter/reviewRequest'
import checkSaleman from '@/view/userCenter/checkSaleman'
import withdraw from '@/view/userCenter/withdraw'
import incomeDetail from '@/view/userCenter/incomeDetail'

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
      share: false
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
      // auth: true,
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
    path: '/orderDetail',
    component: OrderDetail
  },
  {
    path: '/address',
    component: Address
  },
  {
    path: '/pay',
    component: Pay
  },
  {
    path: '/userHome',
    component: UserHome
  },
  {
    path: '/qrcode',
    component: QRCode
  },
  {
    path: '/tgOrder',
    component: TgOrder
  },
  {
    path: '/share',
    component: Share,
    meta: { 
      auth: true,
      share: true
    }
  },
  {
    path: '/mySaleman',
    component: mySaleman
  },
  {
    path: '/fillInfo',
    component: fillInfo
  },
  {
    path: '/finishRequest',
    component: finishRequest
  },
  {
    path: '/requestList',
    component: requestList
  },
  {
    path: '/incomeDetail',
    component: incomeDetail
  },
  {
    path: '/reviewRequest',
    component: reviewRequest
  },
  {
    path: '/develop',
    component: develop,
    meta: { 
      share: true
    }
  },
  {
    path: '/checkSaleman',
    component: checkSaleman
  },
  {
    path: '/withdraw',
    component: withdraw
  },
].map(route => {
  route.path = rootPath + route.path
  return route
})

const router = new VueRouter({
  mode: 'history',
  base: process.env.NODE_ENV == 'development' ? '': '/yzg/',
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
      var query = to.query;
      query.page = to.path.substring(1)
      next({
        path: '/',
        query: query
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
    var currentPage = route.fullPath.substring(1)
    // if (process.env.NODE_ENV == 'development') return;
    //当跳转到根路径时不记录
    if (currentPage && currentPage.substring(0,1) != '?')  {
      localStorage.page = route.fullPath.substring(1); //保存当前路由 刷新的时候用
      window.setTimeout(wxApi.init.bind(this,route.meta.share),150)  //加个延时 要不location.href 还是旧的路由
    }
})


export default router
