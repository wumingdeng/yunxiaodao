<template>
	<f7-page>
		<f7-block inner style="font-size:1.2em;margin:20px 0 0 0;" id="topInfo">
			<p>
				<span style="width:40%;display:inline-block;">
					<span>姓名:</span><span style="color:#888888"> {{$store.state.userinfo.contact}}</span>
				</span>
				<span>手机:</span><span style="color:#888888"> {{$store.state.userinfo.tel}}</span>
			</p>
			<div class="partingLine"></div>
			<p>
				<span>地址:</span><span style="color:#888888"> {{fullAddress}}</span>
			</p>
		</f7-block>

		<f7-block inner style="margin-top:15px;">
			<f7-grid name="baseInfo">
				<f7-col width=30>
					<img class="shoeImg" :src="$store.state.nowPicture">
				</f7-col>
				<f7-col width=70>
					<p style="margin:8px 0;">{{$store.state.productDetail.name}}</p>
					<p style="color:#888888;margin:8px 0;">颜色: {{$store.state.shoeColor}} 尺码: {{$store.state.shoeSize}} 鞋型: {{$store.state.shoeType}}</p>
					<p style="font-size:1.2em;color:#ff0000;margin:8px 0;">¥{{$store.state.productDetail.price}}</p>
				</f7-col>
			</f7-grid>
		</f7-block>

		<f7-list form style="margin-top:-18px;">
			<f7-list-item>
				<f7-label style="width:20%;margin-top:-65px;">留言:</f7-label>
				<div class="item-input">
					<textarea style="padding:2px 5px;margin:12px 0;background-color:#eeeeee" type="textarea" readonly="readonly" v-model="$store.state.remark"></textarea>
				</div>
				<!-- <f7-input readonly type='textarea' v-model="$store.state.remark"></f7-input> -->
			</f7-list-item>
		</f7-list>

  	<div class="pay_btnwrap">
  	  <p class="wap">实付款：<span>￥</span><span id="total_fee">{{$store.state.productDetail.price}}元</span><a @click="onPay" id="payBtn" class="pay_btn">微信支付</a></p>
  	</div>
	</f7-page>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		computed:{
			fullAddress() {
				var info = this.$store.state.userinfo
				return info.province + ' ' + info.city + ' ' + info.area + ' ' + info.address;
			}
		},
		methods:{
			onPay() {
				var state = this.$store.state
				var info = {}
				info.wxid = state.wxid
				info.shoeid = state.productDetail.id
				info.size = state.shoeSize
				info.color = state.shoeColor
				info.type = state.shoeType
				info.remark = state.remark
				info.contact = state.userinfo.contact
				info.gender = state.userinfo.gender
				info.tel = state.userinfo.tel
				info.address = state.userinfo.address
				info.province = state.userinfo.province
				info.city = state.userinfo.city
				info.area = state.userinfo.area


				this.$store.dispatch('ordermake',{
					self:this,
					info:info,
					callback(self, res) {
						if (res.body.w) {
							// self.$f7.alert('','下单成功',function() {
							// 	self.$router.push('/order')
							// })
						}
						var payargs = {
							 "appId":"wx5da59f32f8c2f724",     //公众号名称，由商户传入     
		           "timeStamp":"1395712654",         //时间戳，自1970年以来的秒数     
		           "nonceStr":"e61463f8efa94090b1f366cccfbbb444", //随机串     
		           "package":"prepay_id=u802345jgfjsdfgsdg888",     
		           "signType":"MD5",         //微信签名方式：     
		           "paySign":"70EA570631E4BB79628FBCA90534C63FF7FADD89" //微信签名 
						}
						payargs = res.body;
						WeixinJSBridge.invoke('getBrandWCPayRequest', payargs, function(res){
							console.log(res)
						  if(res.err_msg == "get_brand_wcpay_request:ok"){
						  	self.$router.push('/order');
						    // alert("支付成功");
						    // 这里可以跳转到订单完成页面向用户展示
						  }else{
						    alert('支付失败，请重试');
						  }
						});
					}
				})
			}
		},

		mounted() {

			// function onBridgeReady(){
			//    WeixinJSBridge.invoke(
			//        'getBrandWCPayRequest', {
			//            "appId":"wx5da59f32f8c2f724",     //公众号名称，由商户传入     
			//            "timeStamp":"1395712654",         //时间戳，自1970年以来的秒数     
			//            "nonceStr":"e61463f8efa94090b1f366cccfbbb444", //随机串     
			//            "package":"prepay_id=u802345jgfjsdfgsdg888",     
			//            "signType":"MD5",         //微信签名方式：     
			//            "paySign":"70EA570631E4BB79628FBCA90534C63FF7FADD89" //微信签名 
			//        },
			//        function(res){     
			//        		 console.log(res)
			//            if(res.err_msg == "get_brand_wcpay_request:ok" ) {}     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
			//        }
			//    ); 
			// }
			// if (typeof WeixinJSBridge == "undefined"){
			//    if( document.addEventListener ){
			//        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
			//    }else if (document.attachEvent){
			//        document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
			//        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
			//    }
			// }else{
			//    onBridgeReady();
			// }
		}
	}
</script>

<style scoped>
	#topInfo p {
		margin: 0;
	}
	.partingLine {    
		content: '';
		margin:10px 0;
    left: 0;
    bottom: 0;
    right: auto;
    top: auto;
    height: 1px;
    width: 100%;
    background-color: #e1e1e1;
    display: block;
    z-index: 15;
    -webkit-transform-origin: 50% 100%;
    transform-origin: 50% 100%;
	}
	.shoeImg {
		width: 100%;
	}
	.pay_btnwrap {
    position: fixed;
    left: 0;
    bottom: 0;
    width: 100%;
    height: 50px;
    z-index: 97;
	}
	.pay_btnwrap .wap {
    position: relative;
    height: 50px;
    line-height: 50px;
    font-size: 16px;
    padding-right: 120px;
    text-align: right;
    min-width: 200px;
    display: block;
    background: #fff;
    color: #333;
    border-top: 1px solid #ddd;
    z-index: 97;
    margin: 0;
	}
	.pay_btnwrap .wap span {
    color: #ff0000;
    margin-left: 5px;
    font-size: 18px;
	}
	.pay_btnwrap .wap .pay_btn {
    position: absolute;
    bottom: 0;
    right: 0;
    height: 50px;
    line-height: 50px;
    width: 110px;
    font-size: 18px;
    text-align: center;
    display: block;
    background: #fd7f97;
    color: #fff;
    overflow: hidden;
    cursor: pointer;
	}
	textarea {
		color: #888888;
	}
</style>