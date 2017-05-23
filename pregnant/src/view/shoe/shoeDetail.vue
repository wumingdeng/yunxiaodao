<template>
	<f7-page navbar-through>
		<f7-navbar sliding>
      <f7-nav-left>
          <f7-link icon="icon-back" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="产品详情"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>

		<homeSwipe :swipeData="swipeData"></homeSwipe>
		<f7-block inner style="margin-top:0px;">
			<span>{{productData.name}}</span>
			<span style="float:right;color:#ff6ec7;margin-right:20px">价格:{{productData.price}}</span>
		</f7-block>
		
		<f7-card style="margin: 10px 0">
			<f7-card-header>
				详情介绍
			</f7-card-header>
			<f7-card-content style="height:3000px;">
			</f7-card-content>
		</f7-card>

    <buyShoe 
      :pickerOpened="pickerOpened">
     
    </buyShoe>

    <div class="navFooter">
        <!-- <p><f7-button class="pre" @click="$router.push('/buyShoe')">立即购买</f7-button></p> -->
        <p><f7-button class="pre" @click="onClickBuy">立即购买</f7-button></p>
    </div>
	</f7-page>
</template>

<script>
	import homeSwipe from "./homeSwipe";
  import buyShoe from "./buyShoe";
	export default{
		data () {
			return {
        pickerOpened:false,
				productData:{}
			}
		},
    components:{
    	"homeSwipe":homeSwipe,
      'buyShoe':buyShoe
    },     
    computed: {
    	swipeData() {
    		var pdata = this.$store.state.productDetail
    		if (pdata && pdata.swipePic) {
	    		return pdata.swipePic.split(',')
    		} else {
    			return []
    		}
    	}
    },
    methods:{
      onClickBuy() {
        console.log(this.$f7);
        debugger
        this.pickerOpened = true;
      }
    },
		beforeRouteEnter(to,from,next){
			//获取首页的配置信息
    	var sid = to.query.sid;
    	//查询产品信息
    	window.Global.s.dispatch('getProductDetail', {
    		self:window.Global.Vue,
    		info:{
    			sid:sid
    		},
				callback:next
    	})
		},
		mounted() {
			this.productData = this.$store.state.productDetail
		}
    // mounted() {
    // 	var sid = this.$route.query.sid;
    // 	//查询产品信息
    // 	this.$store.dispatch('getProductDetail', {
    // 		self:this,
    // 		info:{
    // 			sid:sid
    // 		},
    // 		callback(self, res) {
    // 			if (res.body.ok == 0) {

    // 			} else {
    // 				self.productData = res.body.ok
    // 				// self.$f7.reinitPageSwiper('#app')
    // 				// self.$f7.init();
    // 			}
    // 		}
    // 	})
    // }


	}
</script>

<style scoped>
  .navFooter {
      height: 60px;
      line-height: 40px;
      background: #fff;
      position: fixed;
      width: 100%;
      min-width: 320px;
      bottom: 0;
      left: 0;
      z-index: 900;
      border-top: 1px solid #ddd;
  }
  .navFooter p {
  	margin:0;
  }
  .pre {
      width: 90%;
      height: 40px;
      line-height: 40px;
      border: none;
      color: #ffffff;
      font-size: 20px;
      margin:0 auto;
      margin-top: 10px;
      background-color:#18ac16;
  }
</style>