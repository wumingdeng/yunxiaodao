// const jsSHA = require('jssha');

// const APP_ID = 'wx09f7d37254dfa4d6';

// const createNonceStr = function () {
//   return Math.random().toString(36).substr(2, 15);
// };

// const createTimestamp = function () {
//   return parseInt(new Date().getTime() / 1000) + '';
// };

// const raw = function (args) {
//   let keys = Object.keys(args);
//   keys = keys.sort()
//   let newArgs = {};
//   keys.forEach(function (key) {
//     newArgs[key.toLowerCase()] = args[key];
//   });

//   let string = '';
//   for (let k in newArgs) {
//     string += '&' + k + '=' + newArgs[k];
//   }
//   string = string.substr(1);
//   return string;
// };

// let timestamp = createTimestamp();
// let nonceStr = createNonceStr();

// *
// * @synopsis 签名算法 
// *
// * @param jsapi_ticket 用于签名的 jsapi_ticket
// * @param url 用于签名的 url ，注意必须动态获取，不能 hardcode
// *
// * @returns

// const sign = function (jsapi_ticket, url) {
//   let ret = {
//     jsapi_ticket: jsapi_ticket,
//     nonceStr: nonceStr,
//     timestamp: timestamp,
//     url: url
//   };

//   let shaObj = new jsSHA(raw(ret), 'TEXT');

//   ret.signature = shaObj.getHash('SHA-1', 'HEX');

//   return ret;
// };

// function wxConfig(ticket) {
//   wx.config({
//     appId: APP_ID, // 必填，公众号的唯一标识
//     timestamp: timestamp, // 必填，生成签名的时间戳
//     nonceStr: nonceStr, // 必填，生成签名的随机串
//     signature: sign(ticket, location.href.split('#')[0]),// 必填，签名，见附录1
//     jsApiList: [
//       'chooseImage',
//       'uploadImage',
//       'downloadImage',
//       'openLocation',
//       'getLocation',
//       'chooseWXPay',
//       'addCard',
//       'chooseCard',
//       'openCard',
//       'closeWindow'
//     ] 
//   });
// }

export default {
  init(vue) {
    var vue = vue || window.Global.Vue
    var url = location.href.split('#')[0]
    // var url = 'http://yzxs.sujudao.com/yxd/shoeDetail'
    console.log('发送页面' + url)
    window.Global.s.dispatch('signature',{
      self:vue,
      info:{
        url:url,
      },
      callback(self,res) {
        let data = res.body;
        console.log(data)
        console.log('当前页面：' + location.href.split('#')[0])
        wx.config({
          // debug:true,
          appId: data.appid, // 必填，公众号的唯一标识
          timestamp: data.ts, // 必填，生成签名的时间戳
          nonceStr: data.noncestr, // 必填，生成签名的随机串
          signature: data.sig,// 必填，签名，见附录1
          jsApiList: [
            'onMenuShareAppMessage',
            'onMenuShareTimeline',
            'hideMenuItems',
            'hideOptionMenu',
            'showOptionMenu'
          ] 
        });
      }
    })
  }
}