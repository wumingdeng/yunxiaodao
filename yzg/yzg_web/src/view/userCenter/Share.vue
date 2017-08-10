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

		
	
    <div id='pitDiv'style="height:100%">
		<img class="clickBuy" src="static/assets/userCenter/share/clickBuy.png" @click="onHooked">
	    <img
			v-for="n in 12" 
			v-lazy="getImgSrc(n)"
			style="width:100%; display:block;" 
	    >
    </div>
	<div id='shareDiv' v-if="popupOpened" style='text-align:center;position:absolute;top:0px;left:0px;background-color:rgba(0,0,0,0.8)' @click="onSharedClick">
		<img style='position: absolute;top:70px;right:20px;width:25%'  src="static/assets/userCenter/share/shareTxt_1.png"></img>
		<img style='position: relative ;top:150px;width:60%' src="static/assets/userCenter/share/shareTxt_2.png"></img>
		<img style='position: relative ;top:300px;width:70%' src="static/assets/userCenter/share/shareTxt_3.png"></img>
	</div>
	</f7-page>
</template>
<script>
	export default {
		data() {
			return {
				popupOpened:true,
				boss:null,
				showNav: false
			}
		},
		methods:{
			onSharedClick(event) {
				if(event == "touchend") event.preventDefault();
				this.popupOpened=false
				document.getElementById('pitDiv').style.overflow=''
				localStorage.shared = "true"
			},
			getImgSrc(index) {
				return 'static/assets/userCenter/share/p1/share' + index + '.jpg'
			},
			onHooked() {
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
			document.getElementById('shareDiv').style.height=document.body.clientHeight+'px';
			document.getElementById('pitDiv').style.overflow='hidden'
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