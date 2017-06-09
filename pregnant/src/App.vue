<template>
  <f7-views style='z-index:1000;' id="app">
    <div v-show="showLoading" class="loading" 
    >
      <span style="height:100%;vertical-align:middle;display:inline-block;"></span>
      <div style="vertical-align:middle;display:inline-block;">
        <img style="vertical-align:middle;" src="static/assets/loading.gif">
        <p style="display:block;margin-top:0">载入中...</p>
      </div>
      </img>
    </div>
    <router-view></router-view>
  </f7-views>
</template>

<script>
    // import navFooter from './views/navFooter'
    import g from './globals/global.js'
    export default {
        data () {
          return {

          }
        },
        mounted () {
            console.log('enter app')
        },
        created () {
          console.log(this)
        },
        computed:{
          showLoading() {
            return Global.s.state.isloading
          }
        },
        methods:{
          sendMessage() {
            WeixinJSBridge.on('menu:share:appmessage', function(argv){

              WeixinJSBridge.invoke('sendAppMessage',{

                "appid":"", //appid 设置空就好了。
                "img_url": 'imgUrl', //分享时所带的图片路径
                "img_width": "120", //图片宽度
                "img_height": "120", //图片高度
                "link":'url', //分享附带链接地址
                "desc":"我是一个介绍", //分享内容介绍
                "title":"标题，再简单不过了。"
                }, function(res){/*** 回调函数，最好设置为空 ***/

              });

            });
          },
          setWxConfig() {
            this.sendMessage();
          }
        },
        mounted() {
          // if (g.isInWeiXin()) {
          //   if (typeof(WeixinJSBridge) == "undefined"){
          //      if( document.addEventListener ){
          //          document.addEventListener('WeixinJSBridgeReady', this.setWxConfig, false);
          //      }else if (document.attachEvent){
          //          document.attachEvent('WeixinJSBridgeReady', this.setWxConfig); 
          //          document.attachEvent('onWeixinJSBridgeReady', this.setWxConfig);
          //      }
          //   }else{
          //      this.setWxConfig();
          //   }
          // }
          // window.setTimeout(this.setWxConfig,1000)

        }
    }
</script>

<style>
  label.label-checkbox input[type=checkbox]:checked+.item-media i.icon-form-checkbox{
    background-color: #fc5475
  }
  i.icon.icon-back {
    background-image: url(data:image/svg+xml;charset=utf-8,%3Csvg%20xmlns%3D'http%3A%2F%2Fwww.w3.org%…-8%2C8l8%2C8l-2%2C2L0%2C10L10%2C0z'%20fill%3D'%23007aff'%2F%3E%3C%2Fsvg%3E);
  }
  .loading {
    text-align:center;
    width:100%;
    height:100%;
    z-index:999;
    position:absolute;
    top:0;
    left:0
  }
</style>
