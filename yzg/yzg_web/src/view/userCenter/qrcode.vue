<template>
	<f7-page navbar-through>
		<f7-navbar sliding>
      <f7-nav-left>
          <f7-link icon="icon-back color-black" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="我的推广二维码"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
    <canvas id='canvas'></canvas>
    <div v-show="showQRCode"  class="qrcodeBg">
        <img :src="qrcodeUrl" alt="" id='qrcode'>
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
      getBossQRcode() {
      	var self = this
				var QRCode = require('qrcode')

				this.$store.dispatch('getBossQrcode', {
					self:this,
					info:{
						wxid: this.$store.state.wxid
					},
					callback(self, res) {
						if (res.body.ok) {
							var opts = {
							  errorCorrectionLevel: 'H',
							  type: 'image/jpeg',
							  rendererOpts: {
							    quality: 0.3
							  }
							}
							var qrcode = res.body.ok;
							var url = 'http://yzg.sujudao.com/yzg/?page=userHome&qrcode=' + qrcode;
							QRCode.toDataURL(url, opts, function (err, url) {
							  if (err) throw err
							  self.showQRCode = true
							  var img = document.getElementById('qrcode')
							  img.src = url
							})
						}
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
			var bossQrcode = this.$route.query.isBoss;
			var isBoss = this.$store.state.userinfo.isBoss == 1;
			if (isBoss) {
        if (bossQrcode) {
          this.getBossQRcode();
        } else {
          this.getQRCode();
        }
			} else {
        if (bossQrcode) {
          this.$f7.alert('', '权限不足')
          return
        }
				this.getQRCode();
			}
			
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

