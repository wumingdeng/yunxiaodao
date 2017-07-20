<template>
	<f7-page navbar-through>
		<f7-navbar sliding>
      <f7-nav-center sliding title="分享"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
		<p><f7-button style="margin-top:200px;" fill @click="onHooked">一双好鞋</f7-button></p>
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
					this.$store.dispatch('tglink',{
						self:this,
						info:{
							wxid: this.$store.state.wxid,
							bossid:this.boss
						},
						callback(self, res) {

						}
					})
				}
				this.$router.push('/shoeDetail');
			}
		},
		mounted() {
			this.boss = this.$route.query.userid;
		}
	}
</script>

<style scoped>
	
</style>