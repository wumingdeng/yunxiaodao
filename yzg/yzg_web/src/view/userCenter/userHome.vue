<template>
	<f7-page v-show="isShow" >
        <f7-page-content style="overflow-x: hidden;">
    		<div class="dc_top_per">
    			<img class="face" :src="$store.state.userinfo.headUrl">
    			<span>
                    <p>{{$store.state.userinfo.name}}</p>
                    <p>余额: 0</p>
                </span>
                <p style="height:20px;"></p>
    		</div>
            <div class="jscz">
                <div class="shuxian"></div>
                <div class="jsBtn" style="left:0px;">
                    <img style="width:35px;" src="static/assets/userCenter/income.png">
                    <p style="margin:0">收入明细</p>
                </div>
                <div class="jsBtn" style="">
                    <img style="width:35px;" src="static/assets/userCenter/withdraw.png">
                    <p style="margin:0">提现</p>
                </div>
            </div>

            <div class="menuBg">
                <img src="static/assets/userCenter/yuanhuan.png" style="height:50px; float:left; margin-top:-33px;margin-left:20px;">
                <img src="static/assets/userCenter/yuanhuan.png" style="height:50px; float:right; margin-top:-33px;margin-right:20px;">
                <div @click.prevent.stop="onTouchTG" style="">
                    <img style="width:100%" src="static/assets/userCenter/kapian1.jpg" alt="">
                    <div class="menuContent">
                        <img src="static/assets/userCenter/icon_tgdyr.png">
                        <span>推广代言人</span>
                    </div>
                </div>
                <div @click.prevent.stop='gotoShare' style="position: relative; top: -45px;">
                    <img style="width:100%" src="static/assets/userCenter/kapian2.jpg" alt="">
                    <div class="menuContent">
                        <img src="static/assets/userCenter/icon_tgcp.png">
                        <span>产品推广</span>
                    </div>
                </div>
                <div @click.prevent.stop="$router.push('/tgOrder')" style="position: relative; top: -90px;">
                    <img style="width:100%" src="static/assets/userCenter/kapian3.jpg" alt="">
                    <div class="menuContent">
                        <img src="static/assets/userCenter/icon_myorder.png">
                        <span>推广订单</span>
                    </div>
                </div>
                <div v-if="$store.state.userinfo.isBoss" @click.prevent.stop="$router.push('/mySaleman')" style="position: relative; top: -135px;">
                    <img style="width:100%" src="static/assets/userCenter/kapian2.jpg" alt="">
                    <div class="menuContent">
                        <img src="static/assets/userCenter/icon_mysaleman.png">
                        <span>我推广的代言人</span>
                    </div>
                </div>
                <div v-if="canReview" @click.prevent.stop="$router.push('/requestList')" style="position: relative; top: -180px;">
                    <img style="width:100%" src="static/assets/userCenter/kapian1.jpg" alt="">
                    <div class="menuContent">
                        <img src="static/assets/userCenter/icon_dyrsh.png">
                        <span>代言人审核</span>
                    </div>
                </div>
            </div>
        </f7-page-content>
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
        computed:{
            canReview() {
                if (process.env.NODE_ENV == 'development') {
                    return true
                }
                return this.$store.state.userinfo.isBoss && this.$store.state.userinfo.review;
            }
        },
        methods:{
            onTouchTG() {
                var isBoss = this.$store.state.userinfo.isBoss;
                if (isBoss) {
                    this.$router.push({
                        path: '/qrcode',
                        query: {
                            isBoss: true
                        }
                    })
                } else {
                    this.$router.push({
                        path:'/develop',
                        query:{
                            upid: this.$store.state.wxid
                        }
                    })
                }
            },
            gotoShare() {
                this.$router.push({
                    path:'/share',
                    query:{
                        upid: this.$store.state.wxid
                    }
                })
            }
        },
        components:{
    			// "navFooter":navFooter
        },  
        mounted() {
            if (this.$store.state.userinfo.isBoss) {
                //工作人员直接进入 不做什么有的没的
                this.isShow = true;
                return
            }
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
                        //本地改权限
                        self.$store.commit('USERINFO',{isSaleman: 1});
                        if (!self.$store.state.userinfo.haveTGInfo) {
                            //如果没有填资料 直接进入资料填写
                            self.$router.push('/fillInfo')
                            return
                        }
                        self.$store.state.isLoading = false;
                        self.isShow = true;
                        if (res.body.ok == 0) {
                            self.$f7.alert('','成功成为推广人')
                        } else {
                            self.$f7.alert('',res.body.msg,function() {
                                self.$router.push('userHome')
                            })
                        }
                    }
                })
            } else {
                var isSaleman = this.$store.state.userinfo.isSaleman;
                if (isSaleman && isSaleman == 1){
                    if (!this.$store.state.userinfo.haveTGInfo) {
                        //去填写信息
                        this.$router.push('/fillInfo')
                        return;
                    }
                    this.isShow = true
                } else {
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
        width:100%;
        height:auto;
        /*border: 1px solid #d4d4d4;*/
        min-width: 288px;
        margin-bottom:15px;
	}
	.dc_top_per .face {
        width: 90px;
        height: 90px;
        display: inline-block;
        margin: 10px 0px 10px 30px;
        border: 3px solid rgba(0,220,240,0.3);
        border-radius: 80px;
        -moz-border-radius: 80px;
        -webkit-border-radius: 80px;
        overflow: hidden;
	}
	.dc_top_per span {
        line-height: 20px;
        height: 100px;
        font-size: 16px;
        width:50%;
        color: #fff;
        display: inline-block;
        margin-left: 25px;
        /*text-align: center;*/
        overflow: hidden;
	}
    .jscz {
        background-color: #ffffff;
        width:95%;
        margin:0 auto;
        height: 50px;
        border-radius: 5px;
        box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.3);
        position: relative;
        top:-50px;
        text-align: center;
        padding: 10px 0;
    }
    .shuxian {
        width: 1px;
        height: 100%;
        background-color: #ddd;
        /*position: absolute;*/
        margin:auto;
    }
    .jsBtn {
        width:50%;
        margin-top: -53px;
        position: absolute;
        display: inline;
    }

    .menuBg {
        position: relative;
        top: -35px;
        background-color: #ffffff;
        width:95%;
        height:500px;
        margin:0 auto;
        border-radius: 5px;
        box-shadow: 0px -1px 2px rgba(0, 0, 0, 0.3);
    }
    .menuBg .menuContent {    
        /*float: left;*/
        top: -65px;
        left: 30px;
        position: relative;
        height:35px;
    }
    .menuBg .menuContent img {
        height:30px;
    }
    .menuBg .menuContent span {
        height:30px;
        line-height: 30px;
        font-size: 20px;
        color:#ffffff;
        position: relative;
        top: -6px;
        left: 15px;
    }
</style>