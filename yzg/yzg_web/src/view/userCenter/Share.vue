<template>
	<f7-page navbar-through>
		<f7-navbar sliding>
      <f7-nav-center sliding title="分享推广测试页"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
		<p><f7-button style="margin:200px 100px 0 100px;" fill big @click="onHooked">选一双专业孕妇鞋</f7-button></p>
	</f7-page>
</template>

<script>
	export default {
		data() {
			return {
				boss:null
			}
		},
		methods:{
			onHooked() {
				// if (this.boss && this.boss != this.$store.state.wxid) {
				if (true) {
					//绑定关系
					console.log(this.boss)
					
				}
				this.$store.state.isLogin = false
				this.$router.push({
					path:'/',
					query:{
						page:'shoeDetail',
						bossid:this.boss
					}
				});
			}
		},
		mounted() {
			this.$f7.resize();
			this.boss = this.$route.query.upid;
			console.log('推广人:' + this.boss)
		}
	}
</script>

<style scoped>
	
</style>