import Vue from 'vue'
import Vuex from 'vuex'
import * as getters from './getters'
import * as actions from './actions'
import mutations from './mutations'

const state = {
	wxid: 'onmcQ08YlxYrpJYJgc7lJVIXAkt4',
	isLogin:false,
	isloading:false,
	userinfo:{
		height:'',
		weight:'',
		lastPeriod:'',
		isSingle:1,
		contact:'',
		province:'福建',
		city: '厦门',
		area: '思明区'
	}
}

Vue.use(Vuex)
export default new Vuex.Store({
  state,
  actions,
  getters,
  mutations
})
