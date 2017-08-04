<template>
	<f7-page navbar-through infinite-scroll :infinite-scroll-preloader="isPreloader" @infinite="onInfinite">
	  <f7-navbar sliding>
      <f7-nav-left>
        <f7-link icon="icon-back color-black" @click="$router.push('/userHome')"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding style="left:-22px;" title="审核代言人"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
	  </f7-navbar>
		
		<f7-card class='listCard'
			v-for="(item,index) in requestArray"
			:key=index
		>
			<f7-card-header style="white-space: nowrap;font-size:16px;">
				<span>申请时间：{{item.requestDate}}</span>
				<span style="float:right;">{{requestStatus[item.status]}}</span>
			</f7-card-header>
			<f7-card-content style="padding-right:0px;">
				<f7-grid>
					<f7-col width=20>
						<img style="width:100%;margin-top:8px;border-radius: 80px;-moz-border-radius: 80px;-webkit-border-radius: 80px;" :src="item.headUrl" alt="">
					</f7-col>
					<f7-col width=80 style="border-left:1px solid rgba(0,0,0,0.2);padding-left:10px;">
						<p>姓名:{{item.realName}}</p>
						<p>昵称:{{item.nickName}}</p>
						<p>联系方式:{{item.phone}}</p>
						<p>推荐人:{{item.upName}}</p>
					</f7-col>
					<div v-if="item.status==0" style="margin-top:20px;position:absolute;right:10px;width:25%;">
						<f7-button fill color="lightblue" @click="beginReview(index)">开始审核</f7-button>
					</div>
					<div v-else style="margin-top:20px;position:absolute;right:10px;width:25%;">
						<f7-button fill color="lightblue" @click="onCheck(index)">详情</f7-button>
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
        page:1,
        pageCount:10,
        isPreloader:true,
        isNoData:true,
        requestStatus:[
        	'未审核',
        	'审核通过',
        	'审核未通过'
        ],
        requestArray:[]
			}
		},
		computed:{
		},
		methods:{
			//开始审核
			beginReview(index) {
				this.$store.state.currentRequest = this.requestArray[index];
				this.$router.push('/reviewRequest')
			},
			onCheck(index) {
				this.$router.push({
					path: '/checkSaleman',
					query:{
						userid: this.requestArray[index].userid
					}
				})
			},
			getData(callback) {
				this.$store.dispatch('getRequestList',{
          self:this,
          info:{
              wxid:this.$store.state.wxid,
              offset:(this.page - 1) * this.pageCount,
              limit:this.pageCount
          },
          callback:(self,res)=>{
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
      onInfinite(event, done){
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
		margin-right: 0px;
		margin-left: 0px;
	}
	.listCard p {
		margin: 0px;
	}
</style>