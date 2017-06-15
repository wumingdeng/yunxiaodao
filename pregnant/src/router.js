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
    meta: { 
      auth: true
    }
  },
	{
    path: '/check',
    component: Check, 
    name: 'check',
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

import wxApi from './utils/wxApi.js'
import cfg from '../static/webConfig.json'
router.afterEach(route => {
    if (process.env.NODE_ENV == 'development') return;

    window.setTimeout(wxApi.init,10)  //加个延时 要不location.href 还是旧的路由
    // wxApi.init();
    wx.ready(function(){
      console.log('app.vue wxapi ready')
      if (route.meta.share) { //页面是否可分享
        //设置分享功能
        console.log('set share..')
        setWxConfig()
      } else {
        // hideMenu(['menuItem:share:appMessage','menuItem:share:timeline'])
        console.log('hide all...')
        wx.hideOptionMenu();
      }
    });
})

function shareAppMessage(link) {
  var url = cfg.webAddress
  var route = location.href.split('#')[0].substring(url.length + 1)
  url = url + '/?page=' + route;
  console.log('link:' + url)
  wx.onMenuShareAppMessage({
    title: '快来孕小岛看看～！', // 分享标题
    desc: '这是一个分享页', // 分享描述
    link: url, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
    imgUrl: '', // 分享图标
    type: '', // 分享类型,music、video或link，不填默认为link
    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
    success: function () { 
        // 用户确认分享后执行的回调函数
    },
    cancel: function () { 
        // 用户取消分享后执行的回调函数
    }
  });
}
function shareTimeline(link) { 
  var url = cfg.webAddress
  var route = location.href.split('#')[0].substring(url.length + 1)
  url = url + '/?page=' + route;
  console.log('link timeline:' + url)
  wx.onMenuShareTimeline({
      title: '快来孕小岛看看～！', // 分享标题
      link: url, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
      imgUrl: '', // 分享图标
      success: function () { 
          // 用户确认分享后执行的回调函数
      },
      cancel: function () { 
          // 用户取消分享后执行的回调函数
      }
  });
}
function hideMenu(arr) {
  console.log('隐藏部分菜单')
  arr = arr || []
  arr = arr.concat(["menuItem:share:qq","menuItem:share:QZone","menuItem:readMode"]);
  wx.hideMenuItems({
      menuList: arr // 要隐藏的菜单项，只能隐藏“传播类”和“保护类”按钮，所有menu项见附录3
  });
}
function setWxConfig() {
  wx.showOptionMenu();
  shareAppMessage();
  shareTimeline();
  hideMenu()
}

export default router
