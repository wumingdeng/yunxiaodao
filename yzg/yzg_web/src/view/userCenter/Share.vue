<template>
	<f7-page navbar-through id="sharePage">
		<f7-navbar sliding v-if="showNav">
      <f7-nav-left>
        <f7-link icon="icon-back color-black" @click="$router.push('/userHome')"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="分享推广"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
    <div v-if="!showNav" style="height:45px;"></div>
		<img class="clickBuy" src="static/assets/userCenter/share/clickBuy.png" @click="onHooked">
    <div style="height:100%">
	    <img
				v-for="n in 12" 
				v-lazy="getImgSrc(n)"
				style="width:100%; display:block;" 
	    >
    </div>
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
			getImgSrc(index) {
				return 'static/assets/userCenter/share/p1/share' + index + '.jpg'
			},
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
	#sharePage .clickBuy {
		position: absolute;
		width: 40%;
		top: 150px;
	}
</style>