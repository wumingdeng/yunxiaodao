<template>
	<f7-page>
		<div class="dc_top_per">
			<img class="face" :src="$store.state.userinfo.headUrl">
			<span><p>{{$store.state.userinfo.name}}</p></span>
		</div>
		<div class="dc_m" @click.prevent.stop='getQRCode'>
            <!-- <img class='listIcon' src="/static/client/assets/info_icon.jpg"></img> -->
            <span class="listText">我的二维码</span>
            <span class="up"></span>
        </div>
        <div class="dc_m" @click.prevent.stop='gotoShare'>
            <!-- <img class='listIcon' src="/static/client/assets/order_icon.jpg"></img> -->
            <span class='listText'>推广链接</span>
            <span class="up"></span>
        </div>
		<router-link to='/tgOrder' class="dc_m">
            <!-- <img class='listIcon' src="/static/client/assets/order_icon.jpg"></img> -->
            <span class='listText'>我的推广</span>
            <span class="up"></span>
        </router-link>
        <div v-show="showQRCode" class="qrcodeBg" @click="hideQRCode">
            <img :src="qrcodeUrl" alt="">
        </div>
	</f7-page>
</template>

<script>
	// import navFooter from '../navFooter'
	export default {
        data() {
            return {
                qrcode:null,
                showQRCode: false
            }
        },
        methods:{
            gotoShare() {
                this.$router.push({
                    path:'/share',
                    query:{
                        userid: this.$store.state.wxid
                    }
                })
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
        components:{
    			// "navFooter":navFooter
        },  
	}
</script>

<style type="text/css">
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
        margin-top: 100px;
        /*margin:auto 40px;*/
    }
	.dc_top_per {
    background: url('../../../static/assets/userCenter/user_bg.jpg') no-repeat;
    background-size:cover;
    padding: 10px;
    min-height: 110px;
    border: 1px solid #d4d4d4;
    min-width: 288px;
    overflow: hidden;
    margin-bottom:15px;
	}
	.dc_top_per .face {
    width: 80px;
    height: 80px;
    display: block;
    margin: 10px auto;
    border-radius: 80px;
    -moz-border-radius: 80px;
    -webkit-border-radius: 80px;
    overflow: hidden;
	}
	.dc_top_per span {
    line-height: 20px;
    font-size: 14px;
    color: #333;
    display: block;
    text-align: center;
    overflow: hidden;
    min-width: 288px;
	}
	.dc_m {
    height: 40px;
    display: block;
    background: #fff;
    border: 1px solid #ddd;
    border-top: 0;
    line-height: 30px;
    font-size: 16px;
    color: #555;
    padding: 5px 15px 5px 20px;
    min-width: 273px;
    overflow: hidden;
	}
    .dc_m .listIcon {
    width:18px;
    height:18px;
    margin: 10px 10px 0 0;
    }
    .dc_m .listText {
        position:relative;
        /*top:-3px;*/
        left:10px;
    }
	.dc_m .up {
    float: right;
    display: inline-block;
    vertical-align: middle;
    width: 12px;
    height: 20px;
    background: url('../../../static/assets/userCenter/right_arrow.png') no-repeat;
    background-size: 12px 20px;
    margin-top:10px;
	}
</style>