<template>
	<f7-page>
		<f7-card>
			<f7-card-header>
				订单信息
			</f7-card-header>
			<f7-card-content>
<!-- 				<span style="margin-right:30px;">收货人: {{$store.state.userinfo.contact}}</span>
				<span>电话: {{$store.state.userinfo.tel}}</span>
				<p>
					<span>地址: </span>
					<span style="display:inline-block">{{fullAddress}}</span>
				</p> -->

				<f7-list form>
			    <f7-list-item>
			      <f7-label>联系人</f7-label>
			      <f7-input  readonly type="text" placeholder="" v-model="$store.state.userinfo.contact"></f7-input>
			    </f7-list-item>
			    <f7-list-item>
			      <f7-label>电话</f7-label>
			      <f7-input  readonly type="text" placeholder="" v-model="$store.state.userinfo.tel"></f7-input>
			    </f7-list-item>
			    <f7-list-item>
			      <f7-label>地址</f7-label>
			      <f7-input  readonly type="textarea" placeholder="" v-model="fullAddress"></f7-input>
			    </f7-list-item>
			    <f7-list-item>
			      <f7-label>备注</f7-label>
			      <f7-input  readonly type="textarea" placeholder="" v-model="$store.state.remark"></f7-input>
			    </f7-list-item>
			  </f7-list>
			</f7-card-content>
		</f7-card>

		<f7-card>
			<f7-card-header>
				产品信息
			</f7-card-header>
			<f7-card-content>
				<f7-grid name="baseInfo">
					<f7-col width=40>
						<img class="shoeImg" :src="$store.state.productDetail.smallPic">
					</f7-col>
					<f7-col width=60>
						<h2>{{$store.state.productDetail.name}}</h2>
						<p style="font-size:1.2em;">¥{{$store.state.productDetail.price}}</p>
					</f7-col>
				</f7-grid>
			</f7-card-content>
		</f7-card>
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

					}
				})
			}
		}
	}
</script>

<style scoped>
	.shoeImg {
		width: 100%;
	}
	.pay_btnwrap {
    position: fixed;
    left: 0;
    bottom: 0;
    width: 100%;
    height: 40px;
    z-index: 97;
	}
	.pay_btnwrap .wap {
    position: relative;
    height: 40px;
    line-height: 40px;
    font-size: 14px;
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
    font-size: 16px;
	}
	.pay_btnwrap .wap .pay_btn {
    position: absolute;
    bottom: 0;
    right: 0;
    height: 40px;
    line-height: 40px;
    width: 110px;
    font-size: 18px;
    text-align: center;
    display: block;
    background: #7bd2c9;
    color: #fff;
    overflow: hidden;
    cursor: pointer;
	}
</style>