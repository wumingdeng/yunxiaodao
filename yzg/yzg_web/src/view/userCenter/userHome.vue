<template>
	<f7-page v-show="isShow" id="userHome">
        <f7-page-content style="overflow-x: hidden;">
    		<div class="dc_top_per" :style="topBgStyle">
    			<img class="face" :src="$store.state.userinfo.headUrl">
    			<span>
                    <p style="margin-top:5px;">
                        {{$store.state.userinfo.realName}}
                        <img src="static/assets/userCenter/modifyInfo.png" style="height:0.98em;position:relative;top:3px;" @click="gotoFillin">
                    </p>
                    <!-- <p>余额: 0</p> -->
                </span>
                <div class="statusIcon">
                    <img v-if="$store.state.userinfo.isBoss" src="static/assets/userCenter/icon_boss.png">
                    <img v-else src="static/assets/userCenter/icon_saleman.png">
                </div>
                <p style="height:2em;"></p>
    		</div>
            <div class="jscz">
                <div class="shuxian"></div>
                <div class="jsBtn" style="left:0px;" @click.prevent.stop="$router.push('/incomeDetail')">
                    <img style="width:35px;" src="static/assets/userCenter/income.png">
                    <p style="margin:0;font-size:1.2em;">收入明细</p>
                </div>
                <div class="jsBtn" style="" @click="gotoWithdraw">
                    <img style="width:35px;" src="static/assets/userCenter/withdraw.png">
                    <p style="margin:0;font-size:1.2em;">提现</p>
                </div>
            </div>

            <div class="menuBg">
                <img src="static/assets/userCenter/yuanhuan.png" style="height:50px; float:left; margin-top:-33px;margin-left:20px;">
                <img src="static/assets/userCenter/yuanhuan.png" style="height:50px; float:right; margin-top:-33px;margin-right:20px;">
                <div @click.prevent.stop="onTouchTG" style="margin-top:0px;">
                    <img style="width:100%" src="static/assets/userCenter/kapian1.jpg" alt="">
                    <div class="menuContent" style="top:3.6em;">
                        <img src="static/assets/userCenter/icon_tgdyr.png">
                        <span>推广代言人</span>
                    </div>
                </div>
                <div v-if="$store.state.userinfo.isBoss" @click.prevent.stop="$router.push('/mySaleman')" style="position: relative;
        margin-top: -1em;">
                    <img style="width:100%" src="static/assets/userCenter/kapian2.jpg" alt="">
                    <div class="menuContent">
                        <img src="static/assets/userCenter/icon_mysaleman.png">
                        <span>我推广的代言人</span>
                    </div>
                </div>
                <div v-if="canReview" @click.prevent.stop="$router.push('/requestList')" style="position: relative;
        margin-top: -1em;">
                    <img style="width:100%" src="static/assets/userCenter/kapian3.jpg" alt="">
                    <div class="menuContent">
                        <img src="static/assets/userCenter/icon_dyrsh.png">
                        <span>代言人审核</span>
                    </div>
                </div>
                <div @click.prevent.stop='gotoShare' style="position: relative;
        margin-top: -1em;">
                    <img style="width:100%" :src="cptgImg" alt="">
                    <div class="menuContent">
                        <img src="static/assets/userCenter/icon_tgcp.png">
                        <span>产品推广</span>
                    </div>
                </div>
                <div @click.prevent.stop="$router.push('/tgOrder')" style="position: relative;
        margin-top: -1em;">
                    <img style="width:100%" :src="tgddImg" alt="">
                    <div class="menuContent">
                        <img src="static/assets/userCenter/icon_myorder.png">
                        <span>推广订单</span>
                    </div>
                </div>
                <div class="bottomArea"></div>
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
                    // return true
                }
                return this.$store.state.userinfo.isBoss && this.$store.state.userinfo.review;
            },
            topBgStyle() {
                if (Global.isTest) 
                    return "background: url('/static/assets/userCenter/user_bg.jpg') no-repeat;background-size:cover;"
                else 
                    return "background: url('/yzg/static/assets/userCenter/user_bg.jpg') no-repeat;background-size:cover;"
            },
            tgJob() {
                if (this.$store.state.userinfo.isBoss) {
                    return '工作人员'
                } else {
                    return '代言人'
                }
            },
            tgddImg() {
                if (!this.$store.state.userinfo.isBoss) {
                    return "static/assets/userCenter/kapian3.jpg"
                } else {
                    if (this.canReview) {
                        return "static/assets/userCenter/kapian5.jpg"
                    } else {
                        return "static/assets/userCenter/kapian4.jpg"
                    }
                }
            },
            cptgImg() {
                if (this.$store.state.userinfo.isBoss && !this.canReview) {
                    return "static/assets/userCenter/kapian3.jpg"
                } else {
                    return "static/assets/userCenter/kapian4.jpg"
                }
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
                        showNav: true,
                        upid: this.$store.state.wxid
                    }
                })
            },
            gotoWithdraw() {
                this.$router.push('/withdraw')
            },
            gotoFillin() {
                this.$router.push({
                    path:'/fillInfo',
                    query:{
                        isModify: true
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
    #userHome .topShuxian {
        width: 1px;
        height: 2.5em;
        background-color: rgba(0,115,154,1);
        border-right: 1px solid rgba(117,255,249,1);
        display: inline-block;
        padding: 1em 0;
        position: absolute;
        margin:0 0.7em;
        margin-top:2em;
    }
    #userHome .statusIcon {
        background-color: #fee071;
        position: absolute;
        right: 0px;
        top:1em;
        padding: 3px 3px 0px 3px;
        border-radius: 3px 0 0 3px;
    }
    #userHome .statusIcon img{
        position: relative;
        top:2px;
        height: 1.2em;
    }
	.dc_top_per {
        width:100%;
        height:auto;
        /*border: 1px solid #d4d4d4;*/
        min-width: 288px;
        margin-bottom:15px;
        text-align: center;
	}
	.dc_top_per .face {
        width: 17vw;
        height: 17vw;
        /*display: inline-block;*/
        margin: 10px auto 0px auto;
        border: 3px solid rgba(255,255,255,0.3);
        border-radius: 80px;
        -moz-border-radius: 80px;
        -webkit-border-radius: 80px;
        overflow: hidden;

	}
	.dc_top_per span {
        line-height: 16px;
        height: 100px;
        font-size: 18px;
        width: 50%;
        color: #fff;
        /*display: inline-block;*/
        /*margin-left: 30px;*/
        /*text-align: center;*/
        /*overflow: hidden;*/
	}
    .jscz {
        background-color: #ffffff;
        width:95%;
        margin:0 auto;
        height: 60px;
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
        margin-top: -60px;
        position: absolute;
        display: inline;
    }

    .menuBg {
        position: relative;
        top: -35px;
        background-color: #ffffff;
        width:95%;
        /*height:500px;*/
        margin:0 auto;
        border-radius: 5px;
        box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.3);
    }
    .menuBg .menuContent {    
        /*float: left;*/
        top: 2.4em;
        left: 30px;
        position: absolute;
        height:35px;
        line-height: 38px;
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
        top: -8px;
        left: 15px;
    }
    #userHome .bottomArea {
        height: 35px;      
        background: -webkit-linear-gradient(left top, #02d0bd , #0299d8); /* Safari 5.1 - 6.0 */
        background: -o-linear-gradient(bottom right, #02d0bd , #0299d8); /* Opera 11.1 - 12.0 */
        background: -moz-linear-gradient(bottom right, #02d0bd , #0299d8); /* Firefox 3.6 - 15 */
        background: linear-gradient( #ffffff , rgb(222,222,222)); /* 标准的语法 */
    }
</style>