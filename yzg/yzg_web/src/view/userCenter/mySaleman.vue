<template>
	<f7-page navbar-through infinite-scroll id="mySaleman" :infinite-scroll-preloader="isPreloader" @infinite="onInfinite">
	  <f7-navbar sliding>
      <f7-nav-left>
        <f7-link icon="icon-back color-black" @click="$router.push('/userHome')"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding style="left:-22px;" title="我推广的代言人"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
	  </f7-navbar>
		<f7-card class='listCard'
			v-for="(item,index) in salemanArray"
			:key=index
		>
			<f7-card-content>
				<f7-grid>
					<f7-col width=50 style="white-space:nowrap;">
						<img style="width:70px;border-radius:80px;float:left;position:relative;top:-19px;border:3px solid #fff;" :src="item.headUrl">
						<span style="margin-left:15px;font-size:1.2rem;">{{item.realName}}</span>
					</f7-col>
					<f7-col width=50>
						<p style="font-size:1.2rem;text-align:right;margin-bottom:0.4rem;">{{item.hospital}}</p>
						<p style="font-size:1.1rem;color:#68cfc1;text-align:right;">{{item.department}}</p>
					</f7-col>
				</f7-grid>
				<div style="height:1px;background-color:#eee;margin-bottom:10px;"></div>
				<f7-grid>
					<f7-col width=45 style="overflow:hidden;">
						<p style="white-space: nowrap;">昵称:<span style="color:#949494;font-size:.9rem;white-space: nowrap;">{{item.nickName}}</span></p>
						<p style="white-space: nowrap;">身份:<span style="color:#949494;font-size:.9rem;">{{item.job == 1 ? '医生' : '护士'}}</span></p>
					</f7-col>
					<f7-col width=55>
						<p style="white-space: nowrap;">联系方式:<span style="color:#949494;font-size:.9rem;">{{item.phone}}</span></p>
						<p style="white-space: nowrap;overflow:scroll;">加入时间:<span style="color:#949494;font-size:.9rem;">{{item.joinDate}}</span></p>
					</f7-col>
				</f7-grid>
<!-- 				<f7-grid>
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
				</f7-grid> -->
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
      }
		},
		mounted() {
			this.getData();
		}
	}
</script>

<style scoped>
	.userHead {
		width: 60px;
	}
	.listCard {
		margin-right: 5px;
		margin-left: 5px;
	}
	.listCard p {
		margin: 0px;
	}
</style>