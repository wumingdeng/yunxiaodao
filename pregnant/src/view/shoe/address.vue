<template>
	<f7-page navbar-through>
		<f7-navbar sliding>
      <f7-nav-left>
          <f7-link icon="icon-back" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="邮寄地址"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
		<f7-list form>
	    <f7-list-item>
	      <f7-label>联系人</f7-label>
	      <f7-input id="inputContact" type="text" placeholder="" v-model="$store.state.userinfo.contact"></f7-input>
	    </f7-list-item>
	    <li class="">
	    	<div style="width:60%;margin:0 auto;">
		    	<label class="item-content label-radio customRadio" @click="onGender(0)">
		    		<input value="0" name="my-radio" type="radio" :checked="gender == 0 ? 'checked' : false">
		    		<div class="item-inner">
		    			<div class="item-title">女士</div>
		    		</div>
		    	</label>
		    	<label class="item-content label-radio customRadio" @click="onGender(1)">
		    		<input value="1" name="my-radio" type="radio" :checked="gender == 1 ? 'checked' : false">
		    		<div class="item-inner">
		    			<div class="item-title">先生</div>
		    		</div>
		    	</label>
	    	</div>
	    </li>
<!--       <f7-list-item radio name="my-radio" checked :value="1" title="先生"></f7-list-item>
      <f7-list-item radio name="my-radio" :value="2" title="女士"></f7-list-item> -->
	    <f7-list-item>
	      <f7-label>手机号</f7-label>
	      <f7-input id="inputTel" type="number" placeholder="" v-model='$store.state.userinfo.tel'></f7-input>
	    </f7-list-item>
	    <f7-list-item>
	      <f7-label>所在地区</f7-label>
    		<chinaCity v-model="cityInfo" :initInfo="cityInit" :test='87'></chinaCity>
	    </f7-list-item>
	    <f7-list-item>
	      <f7-label>详细地址</f7-label>
	      <f7-input id="inputAddress" type="textarea" placeholder="" v-model="$store.state.userinfo.address"></f7-input>
	    </f7-list-item>
	    <f7-list-item>
	      <f7-label>备注</f7-label>
	      <f7-input id="inputRemark" type="textarea" placeholder="" v-model="$store.state.remark"></f7-input>
	    </f7-list-item>

	    <li>
    		<p style="width:90%;margin:10px auto;"><f7-button fill color='green' @click="onSure">确定</f7-button></p>
	    </li>
	    <li>
	    	<div style="height:5px;"></div>
	    </li>
    </f7-list>
	</f7-page>
</template>

<script>
	import chinaCity from '../../components/chinaCity'
	export default {
		name: 'address',
		data() {
			return {
				cityInfo:{},
				// contact:null,
				gender:null,
				// tel:null,
				// address:null
			}
		},
		computed: {
			cityInit() {
				return {
					province:this.$store.state.userinfo.province || "福建",
					city:this.$store.state.userinfo.city || '泉州',
					area:this.$store.state.userinfo.area || '丰泽区'
				}
			}
		},
		components:{
			'chinaCity':chinaCity
		},
		methods:{
			onGender(value) {
				this.gender = value;
				var userinfo = {}
				userinfo.gender = value
				this.$store.commit('USERINFO',userinfo)
			},
			checkInfo() {
				var contact = document.getElementById("inputContact").value;
				if (!contact) {
    			this.$f7.alert('','请输入联系人')
    			return false
				}

				var phone = document.getElementById("inputTel");
    		if (!phone.value) {
    			this.$f7.alert('','请输入手机号')
    			return false;
    		}
    		var regPhone = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
     		if(!regPhone.test(phone.value)) {
     			this.$f7.alert('','请输入有效的手机号')
     			phone.focus();
     			return false;
     		} 

     		if (this.cityInfo.province == '' || this.cityInfo.city == '' || this.cityInfo.area == '') {
     			this.$f7.alert('','请选择所在地区')
     			return false
     		}

     		var address = document.getElementById("inputAddress").value;
     		if (!address) {
    			this.$f7.alert('','请输入详细地址')
    			return false
     		}
     		return true
			},
			onSure() {
				if (!this.checkInfo()) {
					return false
				}
				//修改本地省市区信息
				this.$store.commit('USERINFO',this.cityInfo)
				this.$router.push('/pay')
			}
		},
		mounted() {
			//初始化数据
			// this.contact = this.$store.state.userinfo && this.$store.state.userinfo.contact || ''
			this.gender = this.$store.state.userinfo && this.$store.state.userinfo.gender || 0
			// this.tel = this.$store.state.userinfo && this.$store.state.userinfo.tel || ''
			// this.address = this.$store.state.userinfo && this.$store.state.userinfo.address || ''
			// this.cityInit = {
			// 	province: '福建',
			// 	city: '福州'
			// }

		}
	}
</script>

<style scoped>
	.customRadio {
		width:49%;
		/*float:left;*/
		display: inline-block;
	}
	.form-radio input[type=radio]:checked~i, label.label-radio input[type=checkbox]:checked~.item-inner, label.label-radio input[type=radio]:checked~.item-inner {
		color: #111188;
	}
</style>