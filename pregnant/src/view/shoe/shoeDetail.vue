<template>
	<f7-page>
<!-- 		<f7-navbar sliding>
      <f7-nav-left>
          <f7-link icon="icon-back" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="产品详情"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar> -->
    <f7-page-content  style="">
  		<homeSwipe :swipeData="swipeData"></homeSwipe>
  		<f7-block inner style="margin:-5px 0 0 0;font-size:17px;">
  			<span>{{productData.name}}</span>
  			<span style="float:right;color:#ff6ec7;margin-right:20px">价格:{{productData.price}}</span>
  		</f7-block>
  		
  		<f7-card style="margin: 10px 0">
  			<f7-card-header>
  				详情介绍
  			</f7-card-header>
  			<f7-card-content style="">
          <img 
            style="width:100%; display:block;" 
            v-for="n in 18" 
            :src="getImgSrc(n)">
  			</f7-card-content>
  		</f7-card>

      <div style="height:60px;"></div>

      <buyShoe 
        :pickerOpened="pickerOpened"
        @close="pickerOpened = false"
      ></buyShoe>
    </f7-page-content>
    <div v-if="pickerOpened" class="blackMask"></div>

      <div class="navFooter">
          <!-- <p><f7-button class="pre" @click="$router.push('/buyShoe')">立即购买</f7-button></p> -->
          <span style="width:30%;"><f7-button style="background-color:#fff" class="pre" @click="$router.push('/order')">
            <img src='/static/assets/shoe/myOrder.png'></img>
          </f7-button></span>
          <span style="width:70%;"><f7-button class="pre" @click="onClickBuy">立即购买</f7-button></span>
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
        this.pickerOpened = true;
      },
      getImgSrc(n) {
        return "static/assets/shoe/product/intro_p1/" + n +".jpg"
      }
    },
		beforeRouteEnter(to,from,next){
			//获取首页的配置信息
    	var sid = to.query.sid || 1;
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
		},
    beforeDestroy() {
      console.log('destory...')
      this.pickerOpened = false;
      this.$f7.closeModal()
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
      height: 50px;
      line-height: 50px;
      background: #fff;
      position: fixed;
      width: 100%;
      min-width: 320px;
      bottom: 0;
      left: 0;
      z-index: 900;
      border-top: 1px solid #aaaaaa;
  }
  .navFooter span {
  	margin:0;
    display:inline-block;
    float:left;
  }
  .navFooter span img {
    height:100%;
  }
  .pre {
      width:100%;
      height: 50px;
      line-height: 50px;
      border: none;
      color: #ffffff;
      font-size: 20px;
      margin:0;
      padding: 0;
      background-color:#fa7699;
      border-radius:0;
  }
  .blackMask {
    width:100%;
    height:100%;
    background-color: #000000;
    filter:alpha(Opacity=60);
    -moz-opacity:0.6;
    opacity: 0.6;
    position: absolute;
    top: 0px;
    z-index: 99;
  }
  .swiper-pagination-bullet-active {
    background-color:#ff7777;
  }
</style>