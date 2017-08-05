<template>
	<f7-page navbar-through infinite-scroll :infinite-scroll-preloader="isPreloader" @infinite="onInfinite">
		<f7-navbar sliding>
			<f7-nav-left>
				<f7-link icon="icon-back color-black" @click="$router.push('/userHome')"></f7-link>
			</f7-nav-left>
			<f7-nav-center sliding style="left:-22px;" title="审核代言人"></f7-nav-center>
			<f7-nav-right></f7-nav-right>
		</f7-navbar>
	
		<f7-card class='listCard' v-for="(item,index) in requestArray" :key='index'>
			<f7-card-header style="white-space: nowrap;font-size:16px;">
				<span>{{item.requestDate.substr(0,10)}}</span>
				<span>
					<span style='color:#68cfc1;font-size:15px'>推荐人 : </span>{{item.upName}}</span>
				<span :style="setItemStatusColor(item.status)">{{requestStatus[item.status]}}</span>
			</f7-card-header>
			<f7-card-content>
				<f7-grid>
					<f7-col width=20>
						<img style="width:100%;margin-top:8px;border-radius: 80px;-moz-border-radius: 80px;-webkit-border-radius: 80px;overflow: hidden;border:3px solid rgba(0,220,240,0.3);" :src="item.headUrl" alt="">
					</f7-col>
					<f7-col width=5>
						<div style='width:3px;border-right: 2px solid rgba(164,164,164,0.3);  padding-bottom:30px; margin-bottom:0px;margin-top:20px'>
						</div>
					</f7-col>
					<f7-col width=75>
						<p>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：<span style='color:rgb(164,164,164)'>{{item.realName}}</span></p>
						<p>昵&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称：<span style='color:rgb(164,164,164)'>{{item.nickName}}</span></p>
						<p>联系方式：<span style='color:rgb(164,164,164)'>{{item.phone}}</span></p>
					</f7-col>
					<div v-if="item.status==0" style="margin-top:35px;position:absolute;right:-10px;width:25%;">
						<f7-button fill class='cus_btn' @click="beginReview(index)">开始审核</f7-button>
					</div>
					<div v-else style="margin-top:35px;position:absolute;right:-10px;width:25%;">
						<f7-button fill class='cus_btn' @click="onCheck(index)">详情</f7-button>
					</div>
				</f7-grid>
			</f7-card-content>
		</f7-card>
	
	</f7-page>
</template>

<script>
export default {
	data() {
		return {
			page: 1,
			pageCount: 10,
			isPreloader: true,
			isNoData: true,
			requestStatus: [
				'未审核',
				'审核通过',
				'审核未通过'
			],
			requestArray: []
		}
	},
	computed: {
		// setItemStatusColor() {
		// 	return 'float:right;color:#35c9ff'
		// }
	},
	methods: {
		//不同状态显示不同颜色
		setItemStatusColor(status) {
			switch(status){
				case 0:
				return 'float:right;color:#fd8f86';
				case 1:
				return 'float:right;color:#35c9ff';
				case 2:
				return 'float:right;color:#515151';
				default:
				return 'float:right'
			}
		},
		beginReview(index) {
			this.$store.state.currentRequest = this.requestArray[index];
			this.$router.push('/reviewRequest')
		},
		onCheck(index) {
			this.$router.push({
				path: '/checkSaleman',
				query: {
					userid: this.requestArray[index].userid
				}
			})
		},
		getData(callback) {
			this.$store.dispatch('getRequestList', {
				self: this,
				info: {
					wxid: this.$store.state.wxid,
					offset: (this.page - 1) * this.pageCount,
					limit: this.pageCount
				},
				callback: (self, res) => {
					if (res.body.ok) {
						self.requestArray = self.requestArray.concat(res.body.ok);
						if (res.body.ok.length > 0) {
							self.isNoData = false;
						}
						if (res.body.ok.length < self.pageCount) {
							//停止加载
							self.$f7.detachInfiniteScroll('.infinite-scroll')
							self.isPreloader = false;
							console.log('stoppredown...')
						}
						if (callback) {
							callback();
						}
					}
				}
			})
			this.page++;
		},
		onInfinite(event, done) {
			//获取数据
			this.getData(done);
		},
	},
	mounted() {
		this.getData();
	}
}
</script>

<style scoped>
.listCard {
	margin-right: 7px;
	margin-left: 7px;
	border-radius: 10px;
}

.head {
	width: 90px;
        height: 90px;
        display: inline-block;
        margin: 10px 0px 10px 30px;
        border: 3px solid rgba(0,220,240,0.3);
        border-radius: 80px;
		
        -moz-border-radius: 80px;
        -webkit-border-radius: 80px;
        overflow: hidden;
}

.listCard p {
	margin: 2px 0px;
}

.button{
	background:#68cfc1;
	width:80%;
	padding:0px 0px;
}

</style>