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
		methods:{
			saveParameter(key) {
				var value = this.$route.query[key] || localStorage[key];	//跳转的页面
				if (value && value != 'undefined') {
					localStorage[key] = value;
				}
				return value
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
			console.log('bossid:' + this.$route.query.bossid);
			var code = this.$route.query.code
			var page = this.saveParameter('page');
			var rid = this.saveParameter('rid');	//足部报告
			var bossid = this.saveParameter('bossid')	//用户推广功能
			var qrcode = this.saveParameter('qrcode');	//工作人员给扫的二维码
			var upid = this.saveParameter('upid');	//医护人员给的推广链接
		
			console.log('upid:' + this.$route.query.upid);
			// alert('page:' + page)
			//不需要授权的页面
			if (page.match('share')) {
				this.$router.push({
					path:'/' + page,
					query:{
						upid: upid
					}
				})
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

		        		localStorage.removeItem('bossid')
			        	if (bossid && bossid != null && bossid != self.$store.state.wxid) {
			        		//从推广页点进来的 绑定关系
			        		console.log('boss coming')
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

		        		if (false) {
		        		// if (isTest) {
		        			self.$router.push('/' + page)
		        		} else {
		        			if (page == 'foot' && rid) {
				        		self.$router.push({
				        			path:'/' + page,	//默认跳到卖鞋页
				        			query:{
				        				rid: rid 
				        			}
				        		});
				        		localStorage.removeItem('rid');
				        	} else if (page == 'userHome' && qrcode && qrcode != null) {
				        		self.$router.push({
				        			path:'/' + page,
				        			query:{
				        				qrcode: qrcode
				        			}
				        		})
		        			} else if (page == 'shoeDetail' && res.body.ok.isSaleman) {
		        				//有推广权限的加上id
		        				var showNav = false
		        				if (bossid && bossid == self.$store.state.wxid) {
		        					showNav = true
		        				}
		        				self.$router.push({
				        			path:'/' + page,
				        			query:{
				        				bossid: res.body.ok.wxid,
				        				showNav: showNav
				        			}
				        		})
		        			} else if (page == 'develop' && upid) {
		        				localStorage.removeItem('upid');
		        				self.$router.push({
		        					path:'/' + page,
		        					query:{
		        						upid: upid
		        					}
		        				})
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