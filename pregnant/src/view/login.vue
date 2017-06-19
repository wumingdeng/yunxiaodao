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
			console.log('授权code:' + this.$route.query.code)
			var code = this.$route.query.code || localStorage.code;
			var page = this.$route.query.page || localStorage.page;	//跳转的页面
			if (page == "undefined") page = 'shoeDetail'
			var rid = this.$route.query.rid	//足部报告id
			// var oid = this.$route.query.oid;
			localStorage.code = code;
			localStorage.page = page;


			// this.$store.commit('GET_WXID',wxid)
			var isTest = process.env.NODE_ENV == 'development'
			if (isTest) {
				code = 'heheda'
			}
			if (!this.$store.state.isLogin) {
				if (code && code != 'undefined') {
					//登录
					console.log('yanzheg...')
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
		        		if (isTest) {
		        			self.$router.go(-1)
		        		} else {
			        		self.$router.push({
			        			path:'/' + page,	//默认跳到卖鞋页
			        			query:{
			        				rid: rid
			        			}
			        		});
		        		}
			        } else if (res.body.err == 14) {
			        	//授权失败
								wxAuth.auth();
			        }
						}
					})
				} else {
					wxAuth.auth();
					return
				}
			} else {
      	this.$router.push('/' + page);
			}
		}
	}

</script>