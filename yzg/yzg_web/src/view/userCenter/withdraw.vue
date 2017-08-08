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
          <p style="margin:0; color:rgb(254,192,195);font-size:16px">可用余额</p>
          <p style="margin:5px;color:#393939;">0元</p>
      </div>
      <div class="txBtn" style="">
          <img style="width:12vw;" src="static/assets/userCenter/icon_djye.png">
          <p style="margin:0; color:rgb(111,216,218);font-size:16px;">冻结余额</p>
          <p style="margin:5px;color:#393939;">0元</p>
      </div>
    </div>
    <div class="middle">
      <img src="static/assets/userCenter/yuanhuan.png" style="height:50px; float:left; margin-top:-33px;margin-left:20px;">
      <img src="static/assets/userCenter/yuanhuan.png" style="height:50px; float:right; margin-top:-33px;margin-right:20px;">
    	<f7-grid class="withdrawArea">
    		<f7-col width=20 style="font-size:16px;;color:#515151;">
    			提现
    		</f7-col>
    		<f7-col width=45>
    			<input type="text">
    		</f7-col>
    		<f7-col width=35>
    			<f7-button style="margin-top:19px;width:100%;font-size:16px;line-height:30px;height:32px;"  fill color="deepgreen">确定</f7-button>
    		</f7-col>
    	</f7-grid>
    </div>
    <div style="position:absolute;top:24em;text-align:center;width:100%;">
    	暂无提现记录
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
			this.$f7.resize()
		}
	}
</script>

<style>
	#withdraw {
		text-align: center;
	}
	#withdraw .topInfo {
		width:95%;
		margin-left: 2.5vw;
		height: 4.8em;
		background-color: #fff;
		position: absolute;
		top: 4.8em;
		border-radius: 7px;
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
		width:95%;
		margin-left: 2.5vw;
		height: 7.35em;
		background-color: #fff;
		position: absolute;
		top: 14.5em;
		border-radius: 7px;
    box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.3);
    white-space: nowrap;
  }
  #withdraw .withdrawArea {
  	height:5em;
  	line-height: 5em;
  	margin-top: 2.3em;
  	background-color: rgb(243,245,245);
  	vertical-align: middle;
  	padding: 0 10px;
  }
  #withdraw .withdrawArea input {
  	background-color: rgb(213,213,213);
  	width: 100%;
  	height: 1.8em;
  	box-shadow: none;
  	border: none;
  	text-align: center;
  	border-radius: 5px;
  	font-size:17px;
  }
</style>


