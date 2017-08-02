<template>
	<f7-page id="infoPage">
<!-- 	  <f7-navbar sliding>
      <f7-nav-center sliding style="left:-22px;" title=""></f7-nav-center>
      <f7-nav-right></f7-nav-right>
	  </f7-navbar> -->
	  <img style="width:100%" src="static/assets/tgdyr.jpg">


	  <f7-block-title style="font-size:19px;margin-top:5px;">
			信息认证
	  </f7-block-title>
	  <f7-block inner>
		  <f7-list id="userinfoForm" form style='margin-top:0px;' enctype="multipart/form-data">
	      <f7-list-item>
	        <f7-label>姓名</f7-label>
	        <f7-input id="nameInput" class="fillInput" type="text" placeholder=""></f7-input>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>手机号码</f7-label>
	        <div class="item-input custom">
	          <input id="phoneInput" type="number" placeholder="" v-model="$store.state.userinfo.phone">
	        </div>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>职业</f7-label>
	        <f7-input id="jobInput" type="select" v-model="selectJob">
	          <option value="1">医生</option>
	          <option value="2">护士</option>
	        </f7-input>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>执业医院</f7-label>
	        <f7-input id="hospitalInput" type="text" placeholder=""></f7-input>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>科室</f7-label>
	        <f7-input id="departmentInput" type="text" placeholder=""></f7-input>
	      </f7-list-item>
	      <f7-list-item v-if="isRequest">
				  <f7-label>执业资格证书</f7-label>

					<vue-core-image-upload id="inputCert" ref="certPic"
  					:class="['btn', 'btn-primary']"
				    text="点击选择图片"
  					@imagechanged="imagechanged"
  					@imageuploaded="imageuploaded"
				    :crop="false"
				    :isXhr="false"
				    :max-file-size="5242880">
				    <p>{{currentPic}}</p>
				  </vue-core-image-upload>
				</f7-list-item>
	    </f7-list>

			<p v-if="!isRequest"><f7-button big fill color=green style="width:90%;margin:-10px auto 0 auto;" @click.stop.prevent="onSubmit">提交</f7-button>
			<p v-if="isRequest"><f7-button big fill color=green style="width:90%;margin:-10px auto 0 auto;" @click.stop.prevent="onSubmit">申请</f7-button></p>
		</f7-block>
	</f7-page>
</template>

<script>
	import VueCoreImageUpload from 'vue-core-image-upload'
	export default {
		data() {
			return {
				upid: null,	//上线
				isTouch: false, //连点控制
				isRequest:false, 	//是否为申请推广人
				selectJob: 1,
				upload:{
            server:"",
            api:"",
            params:{
                token:"test"
            },
            file:"",
            preview:false,
            crop:true,
            width:400,
            height:400,
            cancel:"取消",
            ok:"裁剪",
            success:(data)=>{
                alert(data.length)
            }
        },
        currentPic:'点击选择上传的图片'
      }
		},
    components:{
    	// "upload":upload
    	'vue-core-image-upload': VueCoreImageUpload,
    },
    computed:{
    },
		methods:{
    	onBeginUpload(){
    		console.log('begin..')
    		// this.isScroll = 'hidden';
    	},
    	onEndUpload(){
    		console.log('end..')
    		// this.isScroll = 'auto';
    	},
    	imagechanged(file) {
    		this.currentPic = file.name
    	},
    	imageuploaded() {
    	},
			checkInput() {
				var name = document.getElementById('nameInput').value;
				if (name == '') {
					this.$f7.alert('','请输入姓名');
					return false
				}
				if (name.match(' ')) {
					this.$f7.alert('','名字中不能含有空格')
					return false
				}
				if (name.replace(/[\u0391-\uFFE5]/g,"aa").length > 16) {
					this.$f7.alert('','名字请小于9个字')
					return false
				}

				var phone = document.getElementById('phoneInput').value;
    		if (!phone) {
    			this.$f7.alert('','请输入手机号')
    			return false;
    		}
    		var regPhone = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
     		if(!regPhone.test(phone)) {
     			this.$f7.alert('','请输入有效的手机号')
     			return false;
     		} 

     		var job = document.getElementById('jobInput').value;
     		if (!job) {
     			this.$f7.alert('','请选择职位')
     			return false;
     		}

     		var hospital = document.getElementById('hospitalInput').value;
     		if (!hospital) {
     			this.$f7.alert('','请填写执业医院');
     			return false;
     		}

     		var department = document.getElementById('departmentInput').value;
     		if (!department) {
     			this.$f7.alert('','请填写科室');
     			return false;
     		}
     		var cert = this.$refs['certPic'].files[0];
     		if (this.isRequest && !cert) {
     			this.$f7.alert('','请上传执业资格证书')
     			return false;
     		}
     		//结果保存到服务端
  			if (this.isRequest){
	  			var formData = new FormData()
	  			formData.append('wxid',this.$store.state.wxid);
	  			formData.append('name',name);
	  			formData.append('phone',phone);
	  			formData.append('job',job);
	  			formData.append('hospital',hospital);
	  			formData.append('upid',this.upid);
	  			formData.append('department',department);
	  			formData.append('token',this.$store.state.token);
  				formData.append('cert',cert,'cert.jpg');
     			return formData;
  			} else {
  				var info = {
  					name: name,
  					phone: phone,
  					job: job,
  					hospital: hospital,
  					department: department
  				}
  				return info
  			}
			},
			onSubmit() {
				if (this.isTouch) {
					return;
				}
				this.isTouch = true;
				var info = this.checkInput();
				if (!info) {
					this.isTouch = false;
					return;
				}
				this.$f7.showPreloader('申请中')
				if (this.isRequest) {
					this.$store.dispatch('joinRequest',{
						self: this,
						info: info,
						callback(self, res) {
							self.isTouch = false;
							if (res.body.ok == 0) {
								//写入本地
								self.$f7.alert('','已提交申请,审核中',function(){
									self.$router.push('/finishRequest');
								})
							}
						}
					})
				} else {
					this.$store.dispatch('tgFillInfo',{
						self: this,
						info: info,
						callback(self, res) {
							self.isTouch = false;
							if (res.body.ok) {
								//写入本地
			        	self.$store.commit('USERINFO',res.body.ok);
								self.$f7.alert('','成功填写资料',function(){
									self.$router.push('/userHome');
								})
							}
						}
					})
				}

			}
		},
		mounted() {
			this.isRequest = this.$route.query.request;
			if (Global.isTest) {
				this.isRequest = true;
			}
			this.upid = this.$route.query.upid;
		}
	}
</script>

<style scoped>
/*	.fillInput {
		background-color:#eeeeee;
		margin:5px;
		border-radius:5px;
	}
	.fillInput input {
		text-align:center;
	}*/
</style>




