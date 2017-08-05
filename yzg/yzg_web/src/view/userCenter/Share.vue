<template>
	<f7-page navbar-through>
		<f7-navbar sliding v-if="showNav">
      <f7-nav-left>
        <f7-link icon="icon-back color-black" @click="$router.push('/userHome')"></f7-link>
      </f7-nav-left>
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
				boss:null,
				showNav: false
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
			this.boss = this.$route.query.upid;
			this.showNav = this.$route.query.showNav;
			this.$nextTick(this.$f7.resize)
			console.log('推广人:' + this.boss)
		}
	}
</script>

<style scoped>
	
</style>