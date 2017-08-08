<template>
	<f7-page navbar-through id="withdraw">
		<f7-navbar sliding>
      <f7-nav-left>
          <f7-link icon="icon-back color-black color-black" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="提现"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
    <img src="static/assets/userCenter/user_bg.jpg" style="height:7em;width:100%;">
    <div class="topInfo">
      <div class="shuxian"></div>
      <div class="txBtn" style="left:0px;">
          <img style="width:12vw;" src="static/assets/userCenter/icon_kyye.png">
          <p style="margin:0; color:rgb(254,192,195);">可用余额</p>
          <p style="margin:5px;;">0元</p>
      </div>
      <div class="txBtn" style="">
          <img style="width:12vw;" src="static/assets/userCenter/icon_djye.png">
          <p style="margin:0; color:rgb(111,216,218);">冻结余额</p>
          <p style="margin:5px;;">0元</p>
      </div>
    </div>
    <div class="middle">
      <img src="static/assets/userCenter/yuanhuan.png" style="height:50px; float:left; margin-top:-33px;margin-left:20px;">
      <img src="static/assets/userCenter/yuanhuan.png" style="height:50px; float:right; margin-top:-33px;margin-right:20px;">
    	<f7-grid class="withdrawArea">
    		<f7-col width=15 style="font-size:1.5em;">
    			提现
    		</f7-col>
    		<f7-col width=50>
    			<input type="text">
    		</f7-col>
    		<f7-col width=35>
    			<f7-button style="margin-top:0.7em;width:100%;" fill color="deepgreen">确定</f7-button>
    		</f7-col>
    	</f7-grid>
    </div>
	</f7-page>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		mounted() {
      this.$nextTick(this.$f7.resize)
		}
	}
</script>

<style>
	#withdraw {
		text-align: center;
	}
	#withdraw .topInfo {
		width:92%;
		margin-left: 4vw;
		height: 4.8em;
		background-color: #fff;
		position: absolute;
		top: 4.8em;
		border-radius: 3px;
    box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.3);
    white-space: nowrap;
    padding: 2em 0;
	}
  #withdraw .txBtn {
    width:50%;
    margin-top: -5.6em;
    position: absolute;
    display: inline;
  }
  #withdraw .middle {
		width:92%;
		margin-left: 4vw;
		height: 5.6em;
		background-color: #fff;
		position: absolute;
		top: 14.5em;
		border-radius: 3px;
    box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.3);
    white-space: nowrap;
  }
  #withdraw .withdrawArea {
  	height:3.3em;
  	line-height: 3.3em;
  	margin-top: 2.3em;
  	background-color: rgb(243,245,245);
  	vertical-align: middle;
  	padding: 0 10px;
  }
  #withdraw .withdrawArea input {
  	background-color: rgb(213,213,213);
  	width: 100%;
  	height: 2.2em;
  	box-shadow: none;
  	border: none;
  	text-align: center;
  	border-radius: 5px;
  }
</style>


