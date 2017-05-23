<template>
  <f7-page>
<!--   <f7-picker-modal id="buy-picker" :opened="pickerOpened" style="height:500px;">
    <f7-toolbar>
    <f7-nav-left></f7-nav-left>
      <f7-nav-right>
        <f7-link @click="pickerOpened=false">关闭</f7-link>
      </f7-nav-right>
    </f7-toolbar> -->
		<f7-block inner>
			<f7-grid name="baseInfo">
				<f7-col width=40>
					<img class="shoeImg" src="https://v3.modao.cc/uploads3/images/924/9242422/raw_1493883473.png">
				</f7-col>
				<f7-col width=60>
					<h2>{{productDetail.name}}</h2>
					<p style="font-size:1.2em;">优惠价: {{productDetail.price}}</p>
				</f7-col>
			</f7-grid>
			<div class="partingLine"></div>
			<div>
				<p>尺码</p>
				<ul style="height:70px">
					<li v-for="(item,index) in sizeArray"
						class="selectItem"
						:key='index'
						@click="nowSize = item"
					>
						<a class="normalItem" :class={isSelected:onTouchSize(item)}>
							<span>{{item}}</span>
						</a>
					</li>
				</ul>
			</div>

			<div class="partingLine"></div>

			<div>
				<p>颜色</p>
				<ul style="height:70px">
					<li v-for="(item,index) in colorArray"
						class="selectItem"
						:key='index'
						@click="nowColor = item"
					>
						<a class="normalItem" :class={isSelected:onTouchColor(item)}>
							<span>{{item}}</span>
						</a>
					</li>
				</ul>
			</div>

			<div class="partingLine"></div>

			<div>
				<p>鞋型</p>
				<ul style="height:70px">
					<li v-for="(item,index) in typeArray"
						class="selectItem"
						:key='index'
						@click="nowType = item"
					>
						<a class="normalItem" :class={isSelected:onTouchType(item)}>
							<span>{{item}}</span>
						</a>
					</li>
				</ul>
			</div>			

      <div style="height:100px;"></div>
		</f7-block>
    <div class="navFooter">
        <p><f7-button class="pre" @click="onBuy">立即购买</f7-button></p>
    </div>
  <!-- </f7-picker-modal> -->
  </f7-page>
</template>

<script>
  export default {
  	data() {
  		return {
  			nowSize:null,	//当前选择的尺码
  			nowColor:null,
  			nowType:null,
  			productDetail:{}
  		}
  	},
    props:['pickerOpened'],
  	computed:{
  		sizeArray() {
  			if (this.productDetail && this.productDetail.size) {
  				return this.productDetail.size.split(',')
  			} else {
  				return []
  			}
  		},
  		colorArray() {
  			if (this.productDetail && this.productDetail.color) {
  				return this.productDetail.color.split(',')
  			} else {
  				return []
  			}
  		},
  		typeArray() {
  			if (this.productDetail && this.productDetail.type) {
  				return this.productDetail.type.split(',')
  			} else {
  				return []
  			}
  		}
  	},
  	methods:{
  		onTouchSize(item) {
  			return item == this.nowSize
  		},
  		onTouchColor(item) {
  			return item == this.nowColor
  		},
  		onTouchType(item) {
  			return item == this.nowType
  		},
  		onBuy() {
  			if (!this.nowSize) {
  				this.$f7.alert('',"请选择合适的鞋码")
  				return
  			}
  			if (!this.nowColor) {
  				this.$f7.alert('',"请选择喜欢的颜色")
  				return
  			}
  			if (!this.nowType) {
  				this.$f7.alert('',"请选择合适的鞋型")
  				return
  			}
  			this.$store.commit('SHOE_SIZE',this.nowSize)
  			this.$store.commit('SHOE_COLOR',this.nowColor)
  			this.$store.commit('SHOE_TYPE',this.nowType)
  			this.$router.push('/address')
  		}
  	},
  	mounted() {
  		this.productDetail = this.$store.state.productDetail;
  		this.nowSize = this.$store.state.shoeSize;
  		this.nowColor = this.$store.state.shoeColor;
  		this.nowType = this.$store.state.shoeType;
  	}
  }
</script>

<style scoped>
	.shoeImg {
		width: 100%;
	}
	.partingLine {    
		content: '';
		margin: 10px 0;
    left: 0;
    bottom: 0;
    right: auto;
    top: auto;
    height: 1px;
    width: 100%;
    background-color: #e1e1e1;
    display: block;
    z-index: 15;
    -webkit-transform-origin: 50% 100%;
    transform-origin: 50% 100%;
	}
	.selectItem {    
		float: left;
    position: relative;
    margin: 0 20px 10px 0;
    vertical-align: middle;
    width: 3em;
    line-height: 20px;
    background: #FFF;
	}
	.normalItem {
    color: #3C3C3C;
		display: inline-block;
    white-space: nowrap;
    text-decoration: none;
    padding: 3px 6px;
    min-width: 10px;
    width: 100%;
    text-align: center;
    border: 1px solid #DCDCDC;
    background: #FFF;
    -moz-transition-property: border-color,background;
    -o-transition-property: border-color,background;
    -webkit-transition-property: border-color,background;
    transition-property: border-color,background;
    -moz-transition-duration: 1s;
    -o-transition-duration: 1s;
    -webkit-transition-duration: 1s;
    transition-duration: 1s;
	}
	.isSelected {
		padding: 2px 5px;
    color: #F40;
    border: 2px solid #F40;
	}
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