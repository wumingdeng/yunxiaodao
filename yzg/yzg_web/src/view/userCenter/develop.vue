<template>
	<f7-page id="developPage" navbar-through>
		<f7-navbar sliding>
      <f7-nav-left>
          <f7-link icon="icon-back color-black color-black" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="推广代言人测试页"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
		<p style="margin-top:200px">请将该页面发送给你的朋友或分享到朋友圈</p>
		<p>邀请你的朋友一起加入蜜鹊代言</p>
	</f7-page>
</template>

<script>
	export default {
		data() {
			return {
				upid:null
			}
		},
		methods:{
			
		},
		beforeRouteEnter(to,from,next){
			var upid = to.query.upid;
			if (upid) {
				if (upid != Global.s.state.userinfo.wxid) {
					//通过链接进来的
					if (Global.s.state.userinfo.isSaleman) {
						//如果已经是推广人了
						var bossid = Global.s.state.userinfo.bossid
						if (bossid != upid) {
							//变更上限
							Global.s.dispatch('changeBoss',{
								self: Global.v,
								info:{
									upid: upid
								},
								callback(self, res) {
									//进入个人中心
						      next({
						        path: '/userHome'
						      })
								}
							})
						} else {
				      next({
				        path: '/userHome'
				      })
						}
						
					} else {
						//进入申请推广人页面
			      next({
			        path: '/fillInfo',
							query:{
								request: true,
								upid: upid
							}
			      })
					}
				} else {
					next()
				}
			} else {
				next()
			}

		},
		mounted() {
			this.$f7.resize();
			// this.upid = this.$route.query.upid;
			// if (this.upid) {
			// 	if (this.upid != this.$store.state.userinfo.wxid) {
			// 		//通过链接进来的
			// 		if (this.$store.state.userinfo.isSaleman) {
			// 			//如果已经是推广人了
			// 			var bossid = this.$store.state.userinfo.bossid
			// 			if (bossid != this.upid) {
			// 				//变更上限
			// 				this.$store.dispatch('changeBoss',{
			// 					self: this,
			// 					info:{
			// 						upid: this.upid
			// 					},
			// 					callback(self, res) {
			// 						//进入个人中心
			// 						self.$router.push('/userHome')
			// 					}
			// 				})
			// 			} else {
			// 				this.$router.push('/userHome')
			// 			}
						
			// 		} else {
			// 			//进入申请推广人页面
			// 			this.$router.push({
			// 				path:'/fillInfo',
			// 				query:{
			// 					request: true,
			// 					upid: this.upid
			// 				}
			// 			})
			// 		}
			// 	}
			// }

		}
	}
</script>

<style scoped>
	#developPage p {
		text-align: center;
		font-size: 18px;
	}
</style>