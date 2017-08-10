<template>
	<f7-page id="infoPage">
	  <f7-navbar sliding v-if="isModify">
      <f7-nav-left>
          <f7-link icon="icon-back color-black color-black" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding style="left:-22px;" title="修改资料"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
	  </f7-navbar>
	  <div v-if="isModify" style="height:43px;"></div>
	  <img v-if="!isModify" style="width:100%" src="static/assets/tgdyr.jpg">
	
	  <f7-list id="userinfoForm" class="infoBg" :style="infoBgStyle" form enctype="multipart/form-data">
      <f7-list-item class="fillInfoItem">
        <f7-label>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</f7-label>
        <f7-input id="nameInput" class="fillInput" type="text" placeholder="" v-model="$store.state.userinfo.realName"></f7-input>
      </f7-list-item>
      <f7-list-item class="fillInfoItem">
        <f7-label>手机号码</f7-label>
        <div class="item-input custom">
          <input id="phoneInput" type="number" placeholder="" v-model="$store.state.userinfo.phone">
        </div>
      </f7-list-item>
      <li class="jobInfoItem">
      	<div class="item-content">
      		<div class="item-inner">
      			<div class="item-title label">职&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业</div> 
      			<div id="jobBorder" class="item-input">
      				<select type="select" id="jobInput">
      					<option value="1">医生</option> 
      					<option value="2">护士</option>
      				</select>
      				<img src="static/assets/userCenter/down_icon.jpg" class="inputDown">
      			</div>
      		</div>
      	</div>
      </li>
     
      <f7-list-item class="fillInfoItem">
        <f7-label>执业医院</f7-label>
        <f7-input id="hospitalInput" type="text" placeholder="" v-model="$store.state.userinfo.hospital"></f7-input>
      </f7-list-item>
      <f7-list-item class="fillInfoItem">
        <f7-label>科&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;室</f7-label>
        <f7-input id="departmentInput" type="text" placeholder=""  v-model="$store.state.userinfo.department"></f7-input>
      </f7-list-item>
      <f7-list-item v-if="isRequest" class="fillInfoItem">
			  <f7-label>执业证书</f7-label>
		    <p v-if="currentPic" class="addPicText">{{currentPic}}</p>
				<vue-core-image-upload id="inputCert" ref="certPic" style="position:relative;left:-10vw;"
					:class="['btn', 'btn-primary']"
					@imagechanged="imagechanged"
					@imageuploaded="imageuploaded"
			    :crop="false"
			    :isXhr="false"
			    :max-file-size="5242880">
			    <div>
				    <img  src="static/assets/userCenter/addPic.jpg" style="width:60px;">
			    </div>
			  </vue-core-image-upload>
			</f7-list-item>
    </f7-list>

		<p v-if="!isRequest"><f7-button big fill color=deepgreen style="width:95%;margin:-10px auto 0 auto;" @click.stop.prevent="onSubmit">提交</f7-button>
		<p v-if="isRequest"><f7-button big fill color=deepgreen style="width:95%;margin:-10px auto 0 auto;" @click.stop.prevent="onSubmit">申请</f7-button></p>
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
				isModify: false,	//是否主动点进来
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
        currentPic:''
      }
		},
    components:{
    	// "upload":upload
    	'vue-core-image-upload': VueCoreImageUpload,
    },
    computed:{
    	infoBgStyle() {
    		if (Global.isTest) {
    			if (this.isRequest)
    				return  "margin:5px;background: url('/static/assets/userCenter/info_bg.png') no-repeat;background-size: 100% 320px;height:320px;"
    			else {
    				return "margin:5px;background: url('/static/assets/userCenter/info_bg.png') no-repeat;background-size: 100% 290px;height:290px;"
    			}
    			
    		} else {
    			if (this.isRequest)
    				return  "margin:5px;background: url('/yzg/static/assets/userCenter/info_bg.png') no-repeat;background-size: 100% 320px;height:320px;"
    			else {
    				return "margin:5px;background: url('/yzg/static/assets/userCenter/info_bg.png') no-repeat;background-size: 100% 290px;height:290px;"
    			}
    		}
    	}
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

     		//结果保存到服务端
  			if (this.isRequest){
  				var cert = this.$refs['certPic'].files[0];
	     		if (this.isRequest && !cert) {
	     			this.$f7.alert('','请上传执业资格证书')
	     			return false;
	     		}
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
		beforeRouteEnter(to,from,next){
			//获取审核状态
    	var isRequest = to.query.request;
    	if (Global.isTest) {
    		// isRequest = true;
    	}
    	if(isRequest) {
    		Global.s.dispatch('getRequestStatus',{
	    		self:window.Global.v,
	    		info:{
	    		},
					callback(self, res) {
						if (res.body.ok) {
							//存在在审核的申请
							self.$f7.alert('','您的申请正在审核中',function(){
								//关闭页面
                if (typeof WeixinJSBridge != "undefined") {
                    WeixinJSBridge.invoke("closeWindow")
                } 
							})
						} else {
							next();
						}
					}
    		})
    	} else {
    		next()
    	}

		},
		mounted() {
			this.isRequest = this.$route.query.request;
			this.isModify = this.$route.query.isModify
			if (Global.isTest) {
				// this.isRequest = true;
			}
			this.upid = this.$route.query.upid;
		}
	}
</script>

<style>
	.infoBg {
    
	}
	#userinfoForm ul {
		background: inherit;
		border: none;
		padding-top:20px;
	}
	.fillInfoItem .item-title.label {
		width:30%;
		text-align: center;
	}
	#userinfoForm ul:after {
		background-color: rgba(0,0,0,0);
	}
	#userinfoForm ul:before {
		background-color: rgba(0,0,0,0);
	}
	.fillInfoItem .item-inner:after {
		background-color: rgba(0,0,0,0);
	}
	.fillInfoItem .item-inner input {
		background-color: #eee;
		border-radius: 5px;
		height: 35px;
		text-align: center;
	}
	.jobInfoItem .item-inner select{
	  width: auto;
    padding: 0 37%;
    margin: 0 auto;
    height: 35px;
	}
	.jobInfoItem .item-title.label {
		width:30%;
		text-align: center;
	}
	.jobInfoItem .item-inner:after {
		background-color: rgba(0,0,0,0);
	}
	.jobInfoItem #jobBorder {
		/*height: 33px;*/
		border:1px solid #eee;
		border-radius: 5px;
	}
	.jobInfoItem #jobBorder .inputDown {
		position: absolute;
		height: 30%;
		right:30px;
		top: 16px;
	}
	.addPicText {
		text-align:left;
		white-space: nowrap;
		position: relative;
		overflow: hidden;;
		width:30vw;
		left: -5vw;
	}

</style>




