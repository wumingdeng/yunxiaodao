<template>
	<f7-page navbar-through>
		<f7-navbar sliding>
      <f7-nav-left>
          <f7-link icon="icon-back color-black" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="订单详情"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
		<f7-card>
			<f7-card-header>
				<p>订单号：{{orderData.id}}</p>
				<p style="float:right">{{statusName[orderData.status]}}</p>				
			</f7-card-header>
			<f7-card-content>
				<f7-grid>
					<f7-col width=30>
						<img class="oc_orderImg" :src="orderData.smallPic"></img>
					</f7-col>
					<f7-col width=70>
						<div class="oc_right">
							<span style="">{{orderData.shoeName}}</span>
							<span style="color:#ff0000;font-size:1.5em;position:absolute;top:10px;right:16px;">¥{{orderData.price}}</span>
							<p style="color:#71777f;white-space:nowrap;">尺码:{{orderData.size}}码 颜色:{{orderData.color}} 鞋型:{{orderData.type}}</p>
							<p>订单时间:</p>
						</div>
					</f7-col>
				</f7-grid>
			</f7-card-content>
		</f7-card>
		<f7-card>
			<f7-card-header>
				买家信息		
			</f7-card-header>
			<f7-card-content>
				<p>姓名: <span>{{orderData.contact}}</span></p>
				<p>电话: <span>{{orderData.tel}}</span></p>
				<p>地址: <span>{{orderData.address}}</span></p>
			</f7-card-content>
		</f7-card>
		<f7-card>
			<f7-card-header>
				商家信息		
			</f7-card-header>
			<f7-card-content>
				<p>商家名称: <span>蜜雀科技有限公司</span></p>
				<p>联系方式: <span>400-888888</span></p>
				<p>商家地址: <span></span></p>
				<p>在线客服:</p>
			</f7-card-content>
		</f7-card>

		<f7-grid v-if="orderData.status==orderstatus.waitPay" style="width:100%;height:30px">
			<f7-col><f7-button  style="border-color:#fd7f97;color:#fd7f97" @click.stop.prevent="onCancel">取消</f7-button></f7-col>
			<f7-col><f7-button  style="background-color:#fd7f97" fill @click="onPay">支付</f7-button></f7-col>
		</f7-grid>
		<f7-grid v-if="orderData.status==orderstatus.waitReceipt" style="width:100%;height:30px">
			<f7-col><f7-button  style="border-color:#fd7f97;color:#fd7f97" @click.stop.prevent="openLogistics">查看物流</f7-button></f7-col>
			<f7-col><f7-button  style="background-color:#fd7f97" fill>确认收货</f7-button></f7-col>
		</f7-grid>
		<f7-grid v-if="orderData.status==orderstatus.waitEvaluate" style="width:100%;height:30px">
			<f7-col></f7-col>
			<f7-col><f7-button  style="background-color:#fd7f97" fill>评价</f7-button></f7-col>
		</f7-grid>
		<div style="height:100px;"></div>
<!-- 		<div class="navFooter">
        <span style="width:30%;"><f7-button style="background-color:#fff" class="pre" @click="$router.push('/order')">
          <img src='static/assets/shoe/myOrder.png'></img>
        </f7-button></span>
        <span style="width:70%;"><f7-button class="pre" @click="">立即购买</f7-button></span>
    </div> -->
	</f7-page>
</template>

<script type="text/javascript">
	export default {
		data() {
			return {
				orderData: {},
				orderstatus:{
					waitPay:0,	//待支付
					waitChoice:1, //待备货
					waitDeliver:2,	//待发货
					waitReceipt:3,	//待收货
					waitEvaluate:4,	//待评价
					finish:5,	//完成
					cancel:6
				},
				statusName:[
					'待付款',
					'待备货',
					'待发货',
					'待收货',
					'待评价',
					'交易完成',
					'订单已取消'
				]
			}
		},
		methods:{
			onPay() {
				this.isInPay = true
				this.$store.dispatch('orderpay',{
					self:this,
					info:{
						oid: this.orderData.id
					},
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
						if (payargs.err ) {
							self.$f7.alert('','支付失败');
							self.isInPay = false;
							return
						}
						if (typeof WeixinJSBridge == 'undefined') {
							self.$f7.alert('','支付失败,请在微信中进行支付');
							self.isInPay = false;
							return
						}
						WeixinJSBridge.invoke('getBrandWCPayRequest', payargs, function(res){
							console.log(res)
							self.isInPay = false;
						  if(res.err_msg == "get_brand_wcpay_request:ok"){
								self.orderData.status = self.orderstatus.waitDeliver;
						    // alert("支付成功");
						    // 这里可以跳转到订单完成页面向用户展示
						  }else{
								self.$f7.alert('','支付失败,请重试');
						  }
						});
					}
				})
			},
			onCancel(){
				if (this.isInPay) return;
				//取消订单
				this.$f7.confirm("","确定要取消订单？",()=>{
					this.$f7.showPreloader('正在取消订单');
					//服务端接口
					this.$store.dispatch('ordercancel',{
						self:this,
						info:{
							// wxid:this.$store.state.userinfo.wxid,
							oid:this.orderData.id
						},
						callback:function(self,res) {
							self.orderData.status = self.orderstatus.cancel;
						}
					})
				},()=>{
					console.log("no")
				})
			},
		},
		mounted() {
			this.orderData = this.$store.state.currentOrder;
		}
	}
</script>

<style scoped>
	.navFooter {
      height: 50px;
      line-height: 50px;
      background: #fff;
      position: fixed;
      width: 100%;
      min-width: 320px;
      bottom: 0;
      left: 0;
      z-index: 900;
      border-top:1px solid rgba(170, 170, 170, 0.5);
  }
  .navFooter span {
  	margin:0;
    display:inline-block;
    float:left;
  }
  .navFooter span img {
    height:100%;
  }
  .pre {
      width:100%;
      height: 50px;
      line-height: 50px;
      border: none;
      color: #ffffff;
      font-size: 20px;
      margin:0;
      padding: 0;
      background-color:#fa7699;
      border-radius:0;
  }
</style>