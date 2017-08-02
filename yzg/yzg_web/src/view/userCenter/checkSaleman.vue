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
	        <f7-input id="nameInput" class="fillInput" type="text" readonly v-model="salemanData.upName"></f7-input>
	        <!-- <f7-button style="float:right; width:30%;" @click="onCheck">详情</f7-button> -->
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>姓名</f7-label>
	        <f7-input id="nameInput" class="fillInput" type="text" readonly v-model="salemanData.realName"></f7-input>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>手机号码</f7-label>
	        <div class="item-input custom">
	          <input id="phoneInput" type="number" readonly v-model="salemanData.phone">
	        </div>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>职业</f7-label>
	        <f7-input id="jobInput" type="text" readonly v-model="job"></f7-input>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>执业医院</f7-label>
	        <f7-input id="hospitalInput" type="text" readonly v-model="salemanData.hospital"></f7-input>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>科室</f7-label>
	        <f7-input id="departmentInput" type="text" readonly v-model="salemanData.department"></f7-input>
	      </f7-list-item>
	      <f7-list-item>
	        <f7-label>审核建议</f7-label>
	        <f7-input id="adviceInput" type="textarea"></f7-input>
	      </f7-list-item>
	    </f7-list>
		</f7-block>
	</f7-page>
</template>

<script>
	export default {
		data() {
			return {
				salemanData:{}
			}
		},
		computed:{
			job() {
				return this.salemanData.job == 1 ? '医生': '护士';
			}
		},
		methods:{
			onCheck() {

			},
			getData(){
				this.$store.dispatch('getSalemanData',{
					self: this,
					info:{
						id: this.$store.state.currentRequest.upid
					},
					callback(self, res){
						if (res.body.ok) {
							self.salemanData = res.body.ok;
						}
					}
				})
			}
		},
		mounted() {
			this.getData();
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




