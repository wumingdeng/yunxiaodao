<template>
	<f7-page navbar-through class="pageBg">
		<f7-navbar sliding>
      <f7-nav-left>
          <f7-link icon="icon-back color-black" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="我的推广二维码"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
    <div v-show="showQRCode"  class="qrcodeBg">
      <div clase="divBorderBg">
        <div></div>
      </div>
      <img :src="qrcodeUrl" id='qrcode'>
      <p>请识别图中二维码</p>
      <p>欢迎加入我们</p>
    </div>
    <div class="refreshBtn" @click="onRefresh"  v-show="showQRCode">
      刷新
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
      onRefresh() {
        this.showQRCode = false;
        this.getBossQRcode()
      },
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
    .pageBg {   
      background: -webkit-linear-gradient(left top, #02d0bd , #0299d8); /* Safari 5.1 - 6.0 */
      background: -o-linear-gradient(bottom right, #02d0bd , #0299d8); /* Opera 11.1 - 12.0 */
      background: -moz-linear-gradient(bottom right, #02d0bd , #0299d8); /* Firefox 3.6 - 15 */
      background: linear-gradient(to bottom right, #02d0bd , #0299d8); /* 标准的语法 */
      text-align: center;
    }
    .qrcodeBg {
      width:70%;
      /*height:21rem;*/
      background-color: #fff;
      margin:0 auto;
      margin-top: 70px;
      z-index: 99;
      vertical-align: center;
      animation: photoBrowserIn .4s forwards;
      transform: translate3d(0,0,0);
      border: 8px solid rgba(255,255,255,0.3);
      background-clip: padding-box;
      text-align: center;
      box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.2);
    }
    .qrcodeBg img {
        width: 92%;
        position: relative;
        top: -7px;
        /*margin-top: 80px;*/
        /*margin:auto 40px;*/
    }
    .qrcodeBg div {
      width: 98%;
      height: 20px;
      margin: 0 auto;
      background-color: rgba(255,255,255,0.2);
      position: relative;
      top:-14px;
      z-index: -1;
      box-shadow: 0px -2px 1px rgba(0, 0, 0, 0.075);
    }
    .qrcodeBg div div {
      width: 90%;
      height: 20px;
      margin: 0 auto;
      background-color: rgba(255,255,255,0.1);
      position: relative;
      top:-6px;
      z-index: -2;
      box-shadow: 0px -2px 1px rgba(0, 0, 0, 0.05);
    }
    .qrcodeBg p {
      margin:0px;
      position: relative;
      top: -10px;
    }
    .refreshBtn {
      background-color: rgba(255,255,255,0.3);
      color: #fff;
      font-size: 1.3rem;
      border-radius:10px;
      border: 1px solid rgba(255,255,255,0.7);
      width: 40%;
      height: 40px;
      margin: 30px auto 0 auto;
      line-height: 40px;
      box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.2);
      position: relative;
      animation: photoBrowserIn .2s forwards;
      transform: translate3d(0,0,0);
    }

</style>

