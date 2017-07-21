<template>
	<f7-page>

	</f7-page>
</template>

<script>
	import wxAuth from '@/utils/wxAuth.js'
	export default{
		data() {
			return {

			}
		},
		mounted() {
			//验证登录
			// var wxid = this.$route.query.wxid;
			// console.log('授权code:' + this.$route.query.code)
			// if (this.$route.query.code) {
			// 	alert(this.$route.query.code)
			// 	return;
			// }
			// console.log('page:' + this.$route.query.page)
			// console.log('rid:' + this.$route.query.rid

			var code = this.$route.query.code
			var page = this.$route.query.page || localStorage.page;	//跳转的页面
			if (page == "undefined") page = 'shoeDetail'
			var rid = this.$route.query.rid || localStorage.rid	//足部报告id
			if (rid == "undefined") rid = null;
			var bossid = this.$route.query.bossid || localStorage.bossid
			if (bossid == "undefined") bossid = null;
			// var oid = this.$route.query.oid;
			// localStorage.code = code;
			localStorage.page = page;
			localStorage.rid = rid;
			localStorage.bossid = bossid;
			// alert('page:' + page)
			//不需要授权的页面
			if (page.match('share')) {
				this.$router.push('/' + page)
				return
			} else {
				// alert('wtf?')
				if (!this.$store.state.href) {
					this.$store.state.href = location.href.split('#')[0]
					// alert(this.$store.state.href)
				} else {
					// alert('初始路径:' + this.$store.state.href)
				}
			}

			// this.$store.commit('GET_WXID',wxid)
			var isTest = process.env.NODE_ENV == 'development'
			if (isTest) {
				code = 'heheda'
			}
			if (!this.$store.state.isLogin) {
				if (code && code != 'undefined') {
					//登录
					this.$store.commit("LOADING",true)
					this.$store.dispatch('quickloginwxUser',{
						self:this,
						info:{
							code: code
						},
						callback:function(self,res) {
							if (res.body.ok) {
			        	self.$store.commit('USERINFO',res.body.ok);
			        	self.$store.commit('GET_WXID',res.body.ok.wxid);
			        	self.$store.state.token = res.body.token;

			        	if (bossid && bossid != self.$store.state.wxid) {
			        		//从推广页点进来的 绑定关系
			        		console.log('boss coming')
			        		localStorage.bossid = null
			        		self.$store.dispatch('tglink',{
										self:self,
										info:{
											wxid: self.$store.state.wxid,
											bossid:bossid
										},
										callback(self, res) {

										}
									})
			        	}

		        		if (isTest) {
		        			self.$router.push('/' + page)
		        		} else {
		        			if (page == 'foot' && rid) {
				        		self.$router.push({
				        			path:'/' + page,	//默认跳到卖鞋页
				        			query:{
				        				rid: rid 
				        			}
				        		});
				        		localStorage.rid = null;
		        			} else {
		        				self.$router.push("/" + page);
		        			}
		        		}
			        } else if (res.body.err == 14) {
			        	//授权失败
			        	console.log(res.body)
			        	// alert(res.body)
			        	// return
								wxAuth.auth();
			        }
						}
					})
				} else {
					wxAuth.auth();
					return
				}
			} else {
				//关闭页面
				if (WeixinJSBridge) {
					WeixinJSBridge.invoke("closeWindow")
				} else {
      		this.$router.push('/' + page);
				}
			}
		}
	}

</script>