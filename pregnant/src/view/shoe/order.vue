<template>
	<f7-page id="orderPage" infinite-scroll :infinite-scroll-preloader="isPreloader" @infinite="onInfinite">
<!-- 	  <f7-navbar sliding>
	      <f7-nav-left>
	        <f7-link icon="icon-back" @click="$router.go(-1)"></f7-link>
	      </f7-nav-left>
	      <f7-nav-center sliding style="left:-22px;" title="我的订单"></f7-nav-center>
	      <f7-nav-right></f7-nav-right>
	  </f7-navbar> -->
	<f7-page-content>
    <div v-if="isNoData" style="float:left;width:100%;">
        <p style="margin-top:50px;text-align:center;">暂无订单</p>
    </div>
		<orderCell 
			v-for="(cell,index) in orderArr"
			:orderData="cell"
			:key="index"
		></orderCell>
    <div v-show="!isNoData && !isPreloader" style="width:100%;">
        <p style="margin-bottom:50px;text-align:center;">没有更多订单了</p>
    </div>
    <div style="height:50px;"></div>       
	</f7-page-content>
	<navFooter selectId=2></navFooter>   
	</f7-page>
</template>

<script>
	import navFooter from './navFooter'
	import orderCell from './orderCell'
	export default {
    data () {
			return {
				isNoData:true,
				orderArr:[],
				page:1,
				pageCount:10,
				isPreloader:true,
			}
    },
    components:{
			"navFooter":navFooter,
			'orderCell':orderCell
    },
    methods:{
    	onInfinite(event, done){
    		//获取数据
            console.log('pulldown...')
    		this.getData(done);
    	},
    	getData(callback){
        this.$store.dispatch('orderlistUser',{
          self:this,
          info:{
            uid:this.$store.state.wxid,
            offset:(this.page - 1) * this.pageCount,
            limit:this.pageCount
          },
          callback:(self,res)=>{
            self.orderArr = self.orderArr.concat(res.body.w)
            if (res.body.w.length > 0) {
              self.isNoData = false;
            }
            if (res.body.w.length < self.pageCount) {
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
      }
    },
    mounted() {
    	//取订单数据
    	this.getData();
    }
  }
</script>