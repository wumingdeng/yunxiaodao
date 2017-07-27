<template>
	<f7-page navbar-through infinite-scroll :infinite-scroll-preloader="isPreloader" @infinite="onInfinite">
	  <f7-navbar sliding>
      <f7-nav-left>
        <f7-link icon="icon-back color-black" @click="$router.push('/userHome')"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding style="left:-22px;" title="我推广的代言人"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
	  </f7-navbar>
		
		<f7-card class='listCard'
			v-for="(item,index) in salemanArray"
		>
			<f7-card-content>
				<f7-grid>
					<f7-col width=20>
						<img style="width:100%" :src="item.headUrl" alt="">
					</f7-col>
					<f7-col width=40>
						<p>姓名:{{item.realName}}</p>
						<p>联系方式:{{item.tel}}</p>
						<p>医院:{{item.hospital}}</p>
						<p>加入时间:{{item.joinDate}}</p>
					</f7-col>
					<f7-col width=40>
						<p>昵称:{{item.nickName}}</p>
						<p>身份:{{item.job}}</p>
						<p>科室:{{item.department}}</p>
					</f7-col>
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
        salemanArray:[]
			}
		},
		methods:{
			getData(callback) {
				this.$store.dispatch('getSalemen',{
          self:this,
          info:{
              wxid:this.$store.state.wxid,
              offset:(this.page - 1) * this.pageCount,
              limit:this.pageCount
          },
          callback:(self,res)=>{
              self.salemanArray = self.salemanArray.concat(res.body.ok)
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