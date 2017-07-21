<template>
	<f7-page navbar-through>
		<f7-navbar sliding>
      <f7-nav-left>
          <f7-link icon="icon-back color-black" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="我的推广二维码"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
    <div v-show="showQRCode" class="qrcodeBg">
        <img :src="qrcodeUrl" alt="">
    </div>
	</f7-page>
</template>

<script>
	export default {
		data() {
			return {
				showQRCode: false,
				qrcode: null
			}
		},
		methods:{
      getQRCode() {
        this.showQRCode = true;
        if (this.qrcode) {
            return
        }
        this.$store.dispatch('getQRCode',{
          self:this,
          info:{
              userid: this.$store.state.wxid
          },
          callback(self,res) {
              if (res.body.ok)
                  self.qrcode = res.body.ok;
          }
        })
      },
      hideQRCode() {
          console.log('hide..')
          this.showQRCode = false;
      }
		},
    computed:{
        qrcodeUrl() {
            if (this.qrcode) {
                return 'https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=' + this.qrcode
            } else {
                return ''
            }
        }
    },
		mounted() {
			this.getQRCode();
		}
	}

</script>

<style scoped>
    .qrcodeBg {
      width:100%;
      height:100%;
      background-color: #000000;
      filter:alpha(Opacity=60);
      -moz-opacity:0.6;
      /*opacity: 0.6;*/
      position: absolute;
      top: 0px;
      z-index: 99;
      vertical-align: center;
      animation: photoBrowserIn .4s forwards;
      transform: translate3d(0,0,0);
    }
    .qrcodeBg img {
        width: 100%;
        margin-top: 80px;
        /*margin:auto 40px;*/
    }

</style>

