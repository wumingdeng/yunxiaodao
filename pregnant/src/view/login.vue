<template>
	<f7-page>

	</f7-page>
</template>

<script>
	export default{
		data() {
			return {

			}
		},
		mounted() {
			//验证登录
			var wxid = this.$route.query.wxid;
			var type = this.$route.query.type;
			var page = this.$route.query.page;	//跳转的页面
			// var oid = this.$route.query.oid;
			console.log(wxid)
			localStorage.wxid = wxid;
			localStorage.type = type;
			localStorage.page = page;
			// localStorage.oid = oid;
			// this.$store.commit('user/GET_WXID',wxid);
			// this.$f7.showPreloader('登录中')	
			// this.$store.state.isloading = true;
			// debugger
			this.$store.commit('GET_WXID',wxid)


			if (!this.$store.state.isLogin) {
				this.$store.commit("LOADING",true)
				this.$store.dispatch('quickloginwxUser',{
					self:this,
					info:{
						wxid: wxid
					},
					callback:function(self,res) {
	        	self.$store.commit('USERINFO',res.body.ok);
	        	self.$router.push('/' + page);
					}
				})
			} else {
      	this.$router.push('/' + page);
			}
		}
	}

</script>