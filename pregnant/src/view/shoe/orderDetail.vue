<template>
	<f7-page navbar-through>
		<f7-navbar sliding>
      <f7-nav-left>
          <f7-link icon="icon-back color-black" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="订单详情"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
		<f7-card class="orderCard">
			<f7-card-header>
				<p>订单号：{{orderData.id}}</p>
				<p style="float:right;color:#ff0000;">{{statusName[orderData.status]}}</p>				
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
							<p>订单时间: {{timeToDate(orderData.createtime,true)}}</p>
						</div>
					</f7-col>
				</f7-grid>
			</f7-card-content>
		</f7-card>
		<f7-card class="orderCard">
			<f7-card-header>
				收货信息		
			</f7-card-header>
			<f7-card-content>
				<p>姓名: <span>{{orderData.contact}}</span></p>
				<p>电话: <span>{{orderData.tel}}</span></p>
				<p>地址: <span>{{orderData.address}}</span></p>
			</f7-card-content>
		</f7-card>
		<f7-card class="orderCard">
			<f7-card-header>
				商家信息		
			</f7-card-header>
			<f7-card-content>
				<p>商家名称: <span>蜜鹊智能科技有限公司</span></p>
				<!-- <p>联系方式: <span>18060060203</span></p> -->
				<!-- <p>商家地址: <span></span></p> -->
				<f7-grid>
					<f7-col width=25>
						<span>在线客服:</span>
					</f7-col>
					<f7-col width=75>
						<img style="width:50%;margin-left:-15px;margin-top:-5px;" src="static/assets/serverQrcode.jpg" alt="">
					</f7-col>
				</f7-grid>
			</f7-card-content>
		</f7-card class="orderCard">

		
		<f7-grid v-if="orderData.status==orderstatus.waitPay" style="width:95%;height:30px;margin:0 auto;">
			<f7-col width=50><f7-button  style="background-color:#ffffff;border-color:#fd7f97;color:#fd7f97" big @click.stop.prevent="onCancel">取消</f7-button></f7-col>
			<f7-col width=50><f7-button  style="background-color:#fd7f97" fill big @click="onPay">支付</f7-button></f7-col>
		</f7-grid>
		<f7-grid v-if="orderData.status==orderstatus.waitReceipt" style="width:95%;height:30px;margin:0 auto;">
			<f7-col><f7-button  style="border-color:#fd7f97;color:#fd7f97" big @click.stop.prevent="openLogistics">查看物流</f7-button></f7-col>
			<f7-col><f7-button  style="background-color:#fd7f97" fill big>确认收货</f7-button></f7-col>
		</f7-grid>
		<f7-grid v-if="orderData.status==orderstatus.waitEvaluate" style="width:95%;height:30px;margin:0 auto;">
			<f7-col></f7-col>
			<f7-col><f7-button  style="background-color:#fd7f97" fill big>评价</f7-button></f7-col>
		</f7-grid>
		<div style="height:100px;"></div>
		<f7-popup id="demo-popup" :opened="popupOpened" @popup:closed="popupOpened=false">
      <f7-view>
        <f7-pages>
          <f7-page navbar-fixed style="background-color:#ffffff">
            <f7-navbar title="物流信息">
              <f7-nav-right>
                <!-- Using state: -->
                <img src="static/assets/shoe/guanbi.png" class="closeBtn" @click="popupOpened=false">
                <!-- <f7-link @click="popupOpened=false">关闭</f7-link> -->
                <!--
                Or using F7 API:
                <f7-link close-popup>Close</f7-link>
                -->
              </f7-nav-right>
            </f7-navbar>
            <div v-show="!haveData" style="margin-top:100px;text-align:center;">尚无物流跟踪数据，请稍后重试</div>
            <f7-block v-show="haveData" inner style="margin-top:0">
              <f7-timeline-item inner 
                v-for="(item,index) in logisticsData.Traces"
                :text="item.AcceptStation"
                :date="getLogisticsDate(item.AcceptTime)"
                :time="getLogisticsTime(item.AcceptTime)"
                :key="index"
              ></f7-timeline-item>
            </f7-block>
          </f7-page>
        </f7-pages>
      </f7-view>
    </f7-popup>
<!-- 		<div class="navFooter">
        <span style="width:30%;"><f7-button style="background-color:#fff" class="pre" @click="$router.push('/order')">
          <img src='static/assets/shoe/myOrder.png'></img>
        </f7-button></span>
        <span style="width:70%;"><f7-button class="pre" @click="">立即购买</f7-button></span>
    </div> -->
	</f7-page>
</template>

<script type="text/javascript">
	import {timeToDate} from 'src/globals/global'
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
					'待发货',
					'待发货',
					'待收货',
					'待评价',
					'交易完成',
					'订单已取消'
				],
				popupOpened:false,
        dateObj:{}, //存物流已有的日期
        haveData:false,
        logisticsData:{
          "EBusinessID": "1287326",
          "ShipperCode": "YTO",
          "Success": true,
          "LogisticCode": "12345678",
          "State": "2",
          "Traces": [
            {
              "AcceptTime": "2017-05-18 10:12:38",
              "AcceptStation": "圆通合作点【指尖快递】快件已到达绿地蓝海国际大厦B座负一层驿站,如有疑问请联系055163520604"
            },
            {
              "AcceptTime": "2017-05-19 15:16:13",
              "AcceptStation": "圆通合作点【指尖快递】快件已到达港汇广场A座负一层驿站,如有疑问请联系13515644171"
            },
            {
              "AcceptTime": "2017-05-19 15:16:13",
              "AcceptStation": "圆通合作点【指尖快递】快件已到达港汇广场A座负一层驿站,如有疑问请联系13515644171"
            },
            {
              "AcceptTime": "2017-05-19 15:16:13",
              "AcceptStation": "圆通合作点【指尖快递】快件已到达港汇广场A座负一层驿站,如有疑问请联系13515644171"
            },
          ]
        }
			}
		},
		methods:{
			timeToDate:timeToDate,
			openLogistics() {
        console.log('get logistics')
        this.dateObj = {}
        this.logisticsData = {}
        this.popupOpened = true
      	this.haveData = false
        //取物流信息
        this.$store.dispatch('getLogistics',{
          self:this,
          info:{
            expCode:data.exp_com_no,
            expNo:data.exp_no,
            orderCode:this.orderData.id
          },
          callback(self, res) {
            if (res.body.ok == 1) {
              self.logisticsData = res.body.d;
              if (self.logisticsData.Traces && self.logisticsData.Traces.length > 0) {
              	self.haveData = true
              }
            }
          }
        })
      },
      getLogisticsDate(str) {
        var date = str.substring(0,10)
        date = date.replace(/\-/,'  ')
        //同一天只显示一次
        if (this.dateObj[date]) {
          return ''
        } else {
          this.dateObj[date] = true
          return date
        }
      },
      getLogisticsTime(str) {
        return str.substring(11)
      },
			onPay() {
				if (this.isInPay) {
					return
				}
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
								self.orderData.status = self.orderstatus.waitChoice;
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
	.orderCard {
		margin-right: 0px;
		margin-left: 0px;
	}
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