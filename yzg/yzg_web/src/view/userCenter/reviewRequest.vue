<template>
	<f7-page navbar-through>
	  <f7-navbar sliding>
      <f7-nav-left>
        <f7-link icon="icon-back color-black" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding style="left:-22px;" title=""></f7-nav-center>
      <f7-nav-right></f7-nav-right>
	  </f7-navbar>
	  <f7-block inner>
		  <f7-list id="userinfoForm" form style='margin-top:0px;'>
	      <f7-list-item>
	        <f7-label>推荐人</f7-label>
	        <f7-input id="nameInput" class="fillInput" type="text" readonly v-model="$store.state.currentRequest.upName"></f7-input>
	        <f7-button style="float:right; width:30%;" @click="onCheck">详情</f7-button>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>姓名</f7-label>
	        <f7-input id="nameInput" class="fillInput" type="text" readonly v-model="$store.state.currentRequest.realName"></f7-input>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>手机号码</f7-label>
	        <div class="item-input custom">
	          <input id="phoneInput" type="number" readonly v-model="$store.state.currentRequest.phone">
	        </div>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>职业</f7-label>
	        <f7-input id="jobInput" type="text" readonly v-model="job"></f7-input>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>执业医院</f7-label>
	        <f7-input id="hospitalInput" type="text" readonly v-model="$store.state.currentRequest.hospital"></f7-input>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>科室</f7-label>
	        <f7-input id="departmentInput" type="text" readonly v-model="$store.state.currentRequest.department"></f7-input>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>审核建议</f7-label>
	        <f7-input id="adviceInput" type="textarea"></f7-input>
	      </f7-list-item>
	    </f7-list>
	    <img :src="cert" style="width:100%;">
			<f7-grid>
				<f7-col width=50>
					<f7-button fill big color="red" @click="onRefuse">审核不通过</f7-button>
				</f7-col>
				<f7-col width=50>
					<f7-button fill big color="green" @click="onAccept">审核通过</f7-button>
				</f7-col>
			</f7-grid>
		</f7-block>
	</f7-page>
</template>

<script>
	import g from '../../globals/global'
	export default {
		data() {
			return {
				upid: null,	//上线
				isTouch: false, //连点控制
				isRequest:false 	//是否为申请推广人
			}
		},
		computed:{
			job() {
				return this.$store.state.currentRequest.job == 1 ? '医生': '护士';
			},
			cert() {
				var cert = this.$store.state.currentRequest.certificate;
				if (cert) {
					return g.serverAddress + '/' + cert;
				} else {
					return ''
				}
			}
		},
		methods:{
			onCheck() {
				this.$router.push('/checkSaleman')
			},
			onAccept() {
				var self = this
				this.$f7.confirm('您将通过申请',function() {
					self.$store.dispatch('acceptRequest',{
						self: self,
						info:{
							id: self.$store.state.currentRequest.id
						},
						callback(self, res){
							if (res.body.ok == 0) {
								self.$f7.alert('','已经通过推广申请',function() {
									self.$router.go(-1);
								})
							}
						}
					})
				})
			},
			onRefuse(){
				var adviceInput = document.getElementById('adviceInput').value
				if (!adviceInput) {
					this.$f7.alert('','请填写审核建议');
					return false
				}
				this.$store.dispatch('refuseRequest',{
					self: this,
					info:{
						id: this.$store.state.currentRequest.id,
						advice: adviceInput
					},
					callback(self, res) {
						if (res.body.ok == 0) {
							self.$f7.alert('','已经拒绝此推广申请',function() {
								self.$router.go(-1);
							})
						}
					}
				})
			}
		},
		mounted() {
			this.isRequest = this.$route.query.request;
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




