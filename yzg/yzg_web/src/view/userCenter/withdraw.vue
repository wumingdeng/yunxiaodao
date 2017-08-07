<template>
	<f7-page navbar-through id="withdraw">
		<f7-navbar sliding>
      <f7-nav-left>
          <f7-link icon="icon-back color-black color-black" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title=""></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
    <img src="static/assets/userCenter/user_bg.jpg" style="height:10vh;width:100%;">
    <div class="topInfo">
      <div class="shuxian"></div>
      <div class="txBtn" style="left:0px;">
          <img style="width:12vw;" src="static/assets/userCenter/icon_kyye.png">
          <p style="margin:0; color:rgb(254,192,195);">可用余额</p>
          <p style="margin:5px;;">113元</p>
      </div>
      <div class="txBtn" style="" @click="gotoWithdraw">
          <img style="width:12vw;" src="static/assets/userCenter/icon_djye.png">
          <p style="margin:0; color:rgb(111,216,218);">冻结余额</p>
          <p style="margin:5px;;">255元</p>
      </div>
    </div>
    <div class="middle">
      <img src="static/assets/userCenter/yuanhuan.png" style="height:50px; float:left; margin-top:-33px;margin-left:20px;">
      <img src="static/assets/userCenter/yuanhuan.png" style="height:50px; float:right; margin-top:-33px;margin-right:20px;">
    	<div class="withdrawArea"></div>
    </div>
	</f7-page>
</template>

<script>
	
</script>

<style>
	#withdraw {
		text-align: center;
	}
	#withdraw .topInfo {
		width:92%;
		margin-left: 4vw;
		height: 10vh;
		background-color: #fff;
		position: absolute;
		top: 10vh;
		border-radius: 3px;
    box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.3);
    white-space: nowrap;
    padding: 4vh 0;
	}
	#withdraw .topInfo .topCell {
		height: 100%;
		width: 50%;
		display: inline-block;
    box-sizing: border-box;
    margin:2vh 0;
	}
  #withdraw .txBtn {
    width:50%;
    margin-top: -12vh;
    position: absolute;
    display: inline;
  }
  #withdraw .middle {
		width:92%;
		margin-left: 4vw;
		height: 12vh;
		background-color: #fff;
		position: absolute;
		top: 30vh;
		border-radius: 3px;
    box-shadow: 0px 1px 2px rgba(0, 0, 0, 0.3);
    white-space: nowrap;
		
  }
</style>


