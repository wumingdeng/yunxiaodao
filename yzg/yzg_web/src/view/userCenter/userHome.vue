<template>
	<f7-page v-show="isShow">
		<div class="dc_top_per">
            <img src="/static/assets/userCenter/user_bg.jpg" style="position:obsolute;float:left;width:100%;z-index:0;">
			<img class="face" :src="$store.state.userinfo.headUrl">
			<span>
                <p>{{$store.state.userinfo.name}}</p>
                <p>余额: 0</p>
            </span>
		</div>
        <div @click="onTouchTG" class="dc_m" >
            <!-- <img class='listIcon' src="/static/client/assets/info_icon.jpg"></img> -->
            <span class="listText">推广代言人</span>
            <span class="up"></span>
        </div>
        <router-link v-if="$store.state.userinfo.isBoss" :to="{path:'/mySaleman'}" class="dc_m" >
            <!-- <img class='listIcon' src="/static/client/assets/info_icon.jpg"></img> -->
            <span class="listText">我推广的代言人</span>
            <span class="up"></span>
        </router-link>
        <router-link v-if="canReview" :to="{path:'/requestList'}" class="dc_m" >
            <!-- <img class='listIcon' src="/static/client/assets/info_icon.jpg"></img> -->
            <span class="listText">代言人审核</span>
            <span class="up"></span>
        </router-link>
        <!-- <div class="dc_m" @click.prevent.stop='getQRCode'> -->
        <div class="dc_m" @click.prevent.stop='gotoShare'>
            <!-- <img class='listIcon' src="/static/client/assets/order_icon.jpg"></img> -->
            <span class='listText'>产品推广</span>
            <span class="up"></span>
        </div>
		<router-link to='/tgOrder' class="dc_m">
            <!-- <img class='listIcon' src="/static/client/assets/order_icon.jpg"></img> -->
            <span class='listText'>推广订单</span>
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
                        userid: this.$store.state.wxid
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
                        var realName = self.$store.state.userinfo.realName;
                        if (!realName) {
                            //如果没有填资料 直接进入资料填写
                            self.$router.push('/fillInfo')
                            return
                        }
                        if (res.body.ok == 0) {
                            self.$f7.alert('','成功成为推广人')
                        } else {
                            self.$f7.alert('',res.body.msg)
                        }
                        self.$store.state.isLoading = false;
                        self.isShow = true;
                    }
                })
            } else {
                var isSaleman = this.$store.state.userinfo.isSaleman;
                if (isSaleman && isSaleman == 1){
                    if (!this.$store.state.userinfo.nickName || ! this.$store.state.userinfo.job) {
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
/*        background: url('/static/assets/userCenter/user_bg.jpg') no-repeat;
        background-size:cover;*/
        padding: 10px;
        width:100%;
        height:270px;
        border: 1px solid #d4d4d4;
        min-width: 288px;
        /*overflow: hidden;*/
        margin-bottom:15px;
	}
	.dc_top_per .face {
        width: 90px;
        height: 90px;
        display: inline-block;
        margin: 10px 0px 10px 30px;
        border-radius: 80px;
        -moz-border-radius: 80px;
        -webkit-border-radius: 80px;
        overflow: hidden;
	}
	.dc_top_per span {
        line-height: 20px;
        height: 90px;
        font-size: 16px;
        width:60%;
        color: #fff;
        display: inline-block;
        margin-left: 25px;
        /*text-align: center;*/
        overflow: hidden;
	}
	.dc_m {
    border-top:#ACD8F5 5px dotted;
    height: 40px;
    display: block;
    background: #fff;
    border: 1px solid #ddd;
    /*border-top: 0;*/
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