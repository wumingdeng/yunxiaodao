// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
// /* global Framework7Vue:false */
import Vue from 'vue'
import App from './App'
import store from './store'

import router from './router.js'
// Import F7
import Framework7 from '../static/framework7/js/framework7.js'
import VueResource from 'vue-resource'
Vue.use(VueResource)

// Import F7 Vue Plugin
import Framework7Vue from '@/framework7-vue/framework7-vue.js'
Vue.use(Framework7Vue)

import Global from './globals/global'
window.Global = Global
Global.s = store
Global.Vue = Vue
Vue.config.productionTip = false

import cfg from '../static/webConfig.json'
Global.verStr = '?version=' + cfg.version

import VueLazyload from 'vue-lazyload'

Vue.use(VueLazyload)

/* eslint-disable no-new */
Global.v = new Vue({
  el: '#app',
  framework7: {
      root: '#app',	//Should be same as app el
      animateNavBackIcon: true
    },
  store,
  router,
  template: '<App/>',
  components: { App }
  // render: h => h(App)
})

console.log(process.env.NODE_ENV)
