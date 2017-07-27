<template>
	<div v-show="isShow" class="qrcodeBg" @click='onClose'>
		<div>
			<img id="qrcode" style="width:100%" :src="qrcodeImg"  @click.stop='onTouch'>
			<span id="closeBtn" style="width:50px;height:50px;position:absolute;top:0px;right:0px;"  @click='onClose'></span>
		</div>
	</div>
</template>


<script>
	export default {
		data() {
			return {

			}
		},
		props:['isShow'],
		methods:{
			onClose(evt) {
				this.$emit('close')
			},
			onTouch() {

			}
		},
		computed:{
			qrcodeImg() {
				return "static/assets/serverQrcode_full.png" + Global.verStr;
			}
		},
		mounted() {
			// var qrcode = document.getElementById('qrcode');
			// var scale = qrcode.naturalWidth / qrcode.offsetWidth;
			// var closeBtn = document.getElementById('closeBtn')
			// closeBtn.style.width = closeBtn.offsetWidth / scale + 'px'
			// closeBtn.style.top = -closeBtn.offsetWidth / 3 + 'px'
			// closeBtn.style.right = -closeBtn.offsetWidth / 3 + 'px'
		}
	}
</script>

<style scoped>
  .qrcodeBg {
    width:100%;
    height:100%;
    background-color: rgba(0,0,0,0.7);
   /* filter:alpha(Opacity=30);
    -moz-opacity:0.3;
    opacity: 0.3;*/
    text-align: center;
    position: absolute;
  	vertical-align: center;
    top: 0px;
    z-index: 1000;
    animation: pageFromRightToCenterShadow .4s forwards;
  }
  .qrcodeBg div {
    width: 70%; 
    height: 60%;
	  margin: auto; 
	  position: absolute; 
	  top: 0; left: 0; bottom: 0; right: 0; 
    margin: auto;
	  animation: photoBrowserIn .4s forwards;
	  transform: translate3d(0,0,0);
    /*margin:auto 40px;*/
  }


  .qrcodeBg-out {
    width:100%;
    height:100%;
    background-color: rgba(0,0,0,0.7);
   /* filter:alpha(Opacity=30);
    -moz-opacity:0.3;
    opacity: 0.3;*/
    position: absolute;
    top: 0px;
    z-index: 1000;
    vertical-align: center;
    animation: pageFromRightToCenterShadow .4s forwards;
  }
  .qrcodeBg-out img {
      width: 100%;
      margin-top: 80px;
		  animation: photoBrowserOut .4s forwards;
		  transform: translate3d(0,0,0);
      /*margin:auto 40px;*/
  }
</style>