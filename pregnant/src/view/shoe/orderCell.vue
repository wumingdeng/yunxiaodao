<template>
	<f7-card @click.native="onDetail">
		<f7-card-header style="height:50px;width:100%;">
			<p>订单号：{{orderData.id}}</p>
			<p style="float:right;color:#ff0000">{{orderData.price}}元</p>
			<p style="float:right">{{statusName[orderData.status]}}</p>
		</f7-card-header>
		<f7-card-content>
			<f7-grid>
				<f7-col width=35>
					<img class="oc_orderImg" :src="orderData.smallPic"></img>
				</f7-col>
				<f7-col width=65>
					<div class="oc_right">
						<p>产品名称: {{orderData.shoeName}}</p>
						<p>型号内容:</p>
						<p>{{orderData.size}}码 {{orderData.color}} {{orderData.type}}</p>
					</div>
				</f7-col>
			</f7-grid>
			<div v-if="orderData.status==orderstatus.waitPay" style="height:40px;">
				<p class="oc_time" style="float:right;overflow:hidden;">订单时间:{{timeToDate(orderData.createtime,true)}}</p>
			</div>
			<div v-if="orderData.status==orderstatus.waitPay" style="width:100%;height:30px">
				<span class="oc_button" style="right:15px;"><f7-button style="width:80px;" color="green" fill>支付</f7-button></span>
				<span class="oc_button" style="right:105px"><f7-button style="width:80px;" color="green" @click.stop.prevent="onCancel">取消</f7-button></span>
			</div>
			<div v-if="orderData.status==orderstatus.waitReceipt" style="margin-top:10px;height:30px">
				<span class="oc_button" style="right:15px;"><f7-button style="width:80px;" color="green" fill>确认收货</f7-button></span>
				<span class="oc_button" style="right:105px;"><f7-button style="width:80px;" color="green" fill>查看物流</f7-button></span>
			</div>
			<div v-if="orderData.status==orderstatus.waitEvaluate" style="margin-top:10px; height:30px">
				<p class="oc_button" style="right:15px;"><f7-button style="width:80px;" color="green" fill>评价</f7-button></p>
			</div>
			<div v-if="orderData.status==orderstatus.reselection" style="margin-top:10px; height:30px">
				<p class="oc_button"><f7-button style="width:80px;" color="green" fill 
					@click.stop.prevent="onReselection(true)">选择技师</f7-button></p>
				<p class="oc_button"><f7-button style="width:80px;" color="green" fill 
					@click.stop.prevent="onReselection(false)">系统分配</f7-button></p>
				<p class="oc_button"><f7-button style="width:80px;" color="green" @click.stop.prevent="onCancel">取消</f7-button></p>
			</div>
		</f7-card-content>
	</f7-card>
</template>

<script>
	import {timeToDate} from 'src/globals/global'
	export default {
		data(){
			return {
				orderstatus:{
					waitPay:0,	//待支付
					waitDeliver:1,	//待发货
					waitReceipt:2,	//待收货
					waitEvaluate:3,	//待评价
					finish:4	//完成
				},
				statusName:[
					'待付款',
					'待发货',
					'待收货',
					'待评价',
					'交易完成'
				]
			}
		},
		props:{
			orderData:{
				type:Object,
				default:function(){
					return {
						id:123456,
						status:2,
						service:"丰胸",
						scontent:"上门按摩",
						simg:".///static/client/teacher/pro.png",
						cname:"技师甲",
						createtime:"2016-12-14 10:12:50"
					}
				}
			}
		},
		methods:{
			timeToDate:timeToDate,
			onCancel(){
				//取消订单
				this.$f7.confirm("","确定要取消订单？",()=>{
					this.$f7.showPreloader('正在取消订单');
					//服务端接口
					this.$store.dispatch('ordercancel',{
						self:this,
						info:{
							wxid:this.$store.state.user.wxid,
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
			onComment(){
				this.$router.push({
					url:'/userComment',
					query:{
						oid:this.orderData.id
					}
				})
			},
			onDetail(){
				//进入订单明细
				this.$router.push({
					path:'/checkOrder',
					query:{
						oid:this.orderData.id
					}
				});
			},
			onReselection(isSelect) {
				if (isSelect) {
					//进入正常订单流程 根据订单信息 写用户信息和服务信息
					var self = this;
					this.$router.push({
						path:'/selectCulinarian',
						query:{
							oid:this.orderData.id,
							isReselection:true,
							sid:this.orderData.service,
							geo:JSON.stringify({x:self.orderData.geo.coordinates[0],y:self.orderData.geo.coordinates[1]})
						}
					})
					//进入选择技师界面

				} else {
					this.$f7.confirm('','系统将为您找到更合适的技师',()=>{
						this.$f7.showPreloader('系统派单中')
						//系统分配技师
						this.$store.dispatch('orderreset',{
							self:this,
							info:{
								oid:this.orderData.id,
								wid:0
							},
							callback:function(self,res) {
								self.$f7.alert('','成功匹配技师');
								self.orderData.status = self.orderstatus.waitConfirm;
							}
						})
					})
				}
			}
		},
		mounted(){
			this.$f7.init();
    	this.$f7.params.modalButtonCancel = '取消'
    	this.$f7.params.modalButtonOk = '确定'
		}
	}
</script>

<style type="text/css">
	.oc_orderImg {
		width:100%;
		float:left;
		display:inline-block;
	}
	.oc_right{
		display:inline-block;
		height:100px;
		text-align:left;
		overflow: hidden;
	}
	.oc_cname{
		display: inline-block;
		white-space: nowrap;    
		margin-right: 10px;
		width:34%;
	}
	.oc_time{
		display:inline-block;
		white-space: nowrap;
		width:60%;
	}
	.oc_button{
		position:absolute;
		display:inline-block;
		margin:0 0 0 10px;
	}
</style>