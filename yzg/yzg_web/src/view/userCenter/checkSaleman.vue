<template>
	<f7-page>
<!-- 	  <f7-navbar sliding>
      <f7-nav-left>
        <f7-link icon="icon-back color-black" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding style="left:-22px;" title=""></f7-nav-center>
      <f7-nav-right></f7-nav-right>
	  </f7-navbar> -->
	  <div class="checkSalemanTop">
			<img class="face" :src="salemanData.headUrl">
			<div class="topShuxian"></div>
			<span class='cus_span'>
          <p style='font-size:16px;color:rgba(255,255,255,1)'>代言人: {{salemanData.realName}}</p>
          <p>已加入代言计划{{10}}天</p>
          <p>累计推广订单{{10}}单 </p>
					<p>累计推广代言人{{10}}个</p>
      </span>
      <p style="height:20px;"></p>
		</div>
	  <f7-list id="checkSalemanForm" form>
      <f7-list-item id="upmanItem">
        <f7-label>推荐人</f7-label>
        <f7-input id="nameInput" class="fillInput" type="text" readonly v-model="salemanData.upName"></f7-input>
        <!-- <f7-button style="float:right; width:30%;" @click="onCheck">详情</f7-button> -->
      </f7-list-item>
      <f7-list-item>
        <f7-label>手机号码</f7-label>
        <div class="item-input custom">
          <input id="phoneInput" type="number" readonly v-model="salemanData.phone">
        </div>
      </f7-list-item>
      <f7-list-item>
        <f7-label>职&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业</f7-label>
        <f7-input id="jobInput" type="text" readonly v-model="job"></f7-input>
      </f7-list-item>
      <f7-list-item>
        <f7-label>执业医院</f7-label>
        <f7-input id="hospitalInput" type="text" readonly v-model="salemanData.hospital"></f7-input>
      </f7-list-item>
      <f7-list-item>
        <f7-label>科&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;室</f7-label>
        <f7-input id="departmentInput" type="text" readonly v-model="salemanData.department"></f7-input>
      </f7-list-item>
      <f7-list-item id="certItem">
        <f7-label>执业证书</f7-label>
        <div style="height:5em;"></div>
				<img :src="cert[0]" style="height:5em;position:absolute;left:6em;" @click.stop.prevent="openPhotoBrowser">
      </f7-list-item>
    </f7-list>
    <p style="width:88%; margin:1em auto;"><f7-button big fill color="deepgreen" @click.stop.prevent="$router.go(-1)">返回</f7-button></p>
    <f7-photo-browser
      ref="pb"
      :photos="cert"
      @click="onClickPB"
    ></f7-photo-browser>
	</f7-page>
</template>

<script>
	import g from '../../globals/global'
	export default {
		data() {
			return {
				cert:[],
				salemanData:{},
				pbParams: {
					photos: [],
					backLinkText: '',
					ofText: '/',
					loop: false,
					exposition: false,
					expositionHideCaptions: false
				}
			}
		},
		computed:{
			job() {
				return this.salemanData.job == 1 ? '医生': '护士';
			}
		},
		methods:{
      openPhotoBrowser(index) {
        index = 0;
        this.$refs.pb.open(0)
        this.$refs.pb.enableExposition()
      },
      onClickPB(swiper,event) {
        this.$nextTick(this.$refs.pb.close)
        // setTimeout(this.$refs.pb.close,0)
      },
			onCheck() {

			},
			getData(userid){
				this.$store.dispatch('getSalemanData',{
					self: this,
					info:{
						id: userid
					},
					callback(self, res){
						if (res.body.ok) {
							self.salemanData = res.body.ok;
							var certpic = self.salemanData.certificate;
							if (certpic) {
								if (Global.isTest) {
									self.cert =  ['http://yzg.sujudao.com:8192' + '/' + certpic]
								} else {
									self.cert = [g.serverAddress + '/' + certpic];
								}
							} else {
								self.cert = [''];
							}
							self.pbParams.photos = self.cert;
							self.pbParams.onClick = self.onClickPB;
							self.$refs.pb = self.$f7.photoBrowser(self.pbParams)
						}
					}
				})
			}
		},
		mounted() {
      this.$nextTick(this.$f7.resize)
			var userid = this.$route.query.userid;
			this.getData(userid);
		},
    beforeDestroy() {
      console.log('destory...')
      this.$refs.pb.close();
    }
	}
</script>

<style>
	.checkSalemanTop {
    background: url('/yzg/static/assets/userCenter/user_bg.jpg') no-repeat;
    background-size:cover;
    width:100%;
    height:auto;
    /*border: 1px solid #d4d4d4;*/
    min-width: 288px;
    margin-bottom:15px;
	}
	.checkSalemanTop .topShuxian {
        width: 1px;
        height: 2.5em;
        background-color: rgba(0,115,154,1);
        border-right: 1px solid rgba(117,255,249,1);
        display: inline-block;
        padding: 1em 0;
        position: absolute;
        margin:2.5em 0em 0 0.6em;
    }
	.checkSalemanTop .face {
    width: 20vw;
    height: 20vw;
    display: inline-block;
    margin: 20px 0px 10px 10px;
    border: 3px solid rgba(0,220,240,0.3);
    border-radius: 20vw;
    -moz-border-radius: 80px;
    -webkit-border-radius: 80px;
    overflow: hidden;
	}
	.checkSalemanTop span {
    line-height: 20px;
    height: 100px;
    font-size: 16px;
    width:50%;
    color: #fff;
    display: inline-block;
    margin: 15px 0 0 15px;
    position: relative;
    top:-8px;
		left:10px;
    /*text-align: center;*/
    /*overflow: hidden;*/
	}
	.checkSalemanTop span p {
		margin: 9px 0px;
		white-space: nowrap;
		font-size:14px;
		line-height:14px;
		color:rgba(255,255,255,0.8);
	}
	#checkSalemanForm {
		margin: -50px 5px 0 5px;
	}
	#checkSalemanForm > ul {
		background-color: rgb(248,248,248);
		border-radius: 10px;
	}
	#checkSalemanForm > .list-block .item-inner:after {
		background-color: rgba(0,0,0,0)
	}
	#checkSalemanForm .item-content:after {
		content: '';
    position: absolute;
    left: 0;
    bottom: 0;
    right: auto;
    top: auto;
    height: 1px;
    width: 100%;
    background-color: #c8c7cc;
    display: block;
    z-index: 15;
    -webkit-transform-origin: 50% 100%;
    transform-origin: 50% 100%;
	}
	
	#certItem .item-content:after {
		background-color: rgba(0,0,0,0);
	}
	#checkSalemanForm ul:before {
		background-color: rgba(0,0,0,0);
	}
	#checkSalemanForm ul:after {
		background-color: rgba(0,0,0,0);
	}

	#checkSalemanForm #upmanItem {
		background-color: #fff;
		border-radius: 10px 10px 0 0;

	}
	#checkSalemanForm #upmanItem .item-content:after {
		background-color: rgba(0,0,0,0)
	}	
	#checkSalemanForm #upmanItem .item-inner:after {
		background-color: rgba(0,0,0,0)
	}
	#checkSalemanForm .item-title.label {
		width:5em;
	}
	#checkSalemanForm #upmanItem .item-title.label {
		width:5em;
		letter-spacing: 0.5em;
		color: #68cfc1;
	}
	#checkSalemanForm input {
		text-align: right;
		color: #a7a7a7;
	}
	#checkSalemanForm #upmanItem input {
		text-align: left;
		color: #949494;
	}
</style>




