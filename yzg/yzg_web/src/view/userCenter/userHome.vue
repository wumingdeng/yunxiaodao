<template>
	<f7-page v-show="isShow">
		<div class="dc_top_per">
			<img class="face" :src="$store.state.userinfo.headUrl">
			<span><p>{{$store.state.userinfo.name}}</p></span>
		</div>
        <!-- <div class="dc_m" @click.prevent.stop='getQRCode'> -->
		<router-link to='/qrcode' class="dc_m" >
            <!-- <img class='listIcon' src="/static/client/assets/info_icon.jpg"></img> -->
            <span class="listText">推广二维码</span>
            <span class="up"></span>
        </router-link>
        <router-link v-if="$store.state.userinfo.isBoss" :to="{path:'/qrcode',query:{isBoss:true}}" class="dc_m" >
            <!-- <img class='listIcon' src="/static/client/assets/info_icon.jpg"></img> -->
            <span class="listText">超级二维码</span>
            <span class="up"></span>
        </router-link>
        <div class="dc_m" @click.prevent.stop='gotoShare'>
            <!-- <img class='listIcon' src="/static/client/assets/order_icon.jpg"></img> -->
            <span class='listText'>推广页面</span>
            <span class="up"></span>
        </div>
		<router-link to='/tgOrder' class="dc_m">
            <!-- <img class='listIcon' src="/static/client/assets/order_icon.jpg"></img> -->
            <span class='listText'>我的推广订单</span>
            <span class="up"></span>
        </router-link>
	</f7-page>
</template>

<script>
	// import navFooter from '../navFooter'
	export default {
        data() {
            return {
                qrcode:null,
                showQRCode: false,
                isShow:false
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
            }
        },
        components:{
    			// "navFooter":navFooter
        },  
        mounted() {
            var qrcode = this.$route.query.qrcode;
            console.log(qrcode)
            if (qrcode) {
                if (this.$store.state.isUseQrcode) {
                    this.isShow = true;
                    return;
                }
                this.$store.state.isUseQrcode = true;
                localStorage.removeItem('qrcode')
                this.$store.state.isLoading = true;
                this.$store.dispatch('useBossQrcode',{
                    self: this,
                    info:{
                        qrcode: qrcode
                    },
                    callback(self, res) {
                        console.log(res.body.err)
                        if (res.body.err) {
                        }
                        self.$f7.alert('','成功成为推广人')
                        self.$store.state.isLoading = false;
                        self.isShow = true;
                    }
                })
            } else {
                var isSaleman = this.$store.state.userinfo.isSaleman;
                if (isSaleman && isSaleman == 1)
                    this.isShow = true
                else {
                    this.$f7.alert('','您的权限不足',function() {
                        //关闭页面
                        if (typeof WeixinJSBridge != "undefined") {
                            WeixinJSBridge.invoke("closeWindow")
                        } 
                    })
                }
            }
        }
	}
</script>

<style type="text/css">
	.dc_top_per {
    background: url('/yzg/static/assets/userCenter/user_bg.jpg') no-repeat;
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
        top:3px;
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