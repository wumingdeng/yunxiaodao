const errorTitle = 'error'
const emptyInput = '输入不能为空'
const webConfig = require('../../static/webConfig')
export const ERROR_EMPTY_INPUT = 1
export const ERROR_MSG = {
  1: emptyInput
}
var browser = {
    versions: function () {
        var u = navigator.userAgent, app = navigator.appVersion;
        return {         //移动终端浏览器版本信息
            trident: u.indexOf('Trident') > -1, //IE内核
            presto: u.indexOf('Presto') > -1, //opera内核
            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
            mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
            iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
            iPad: u.indexOf('iPad') > -1, //是否iPad
            webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
        };
    }(),
    language: (navigator.browserLanguage || navigator.language).toLowerCase()
}

export default {
  // debugServerAddress: 'http://192.168.18.216:8092',
  debugServerAddress: 'http://192.168.18.165:8092',
  // serverAddress: 'http://yzxs.sujudao.com:8092',
  serverAddress: webConfig.serverAddress,
  adminServerAddress: webConfig.adminServerAddress,
  machineServerAddress: webConfig.machineServerAddress,
  wechatServerAddress: webConfig.wechatServerAddress,
  // machineServerAddress: "http://192.168.18.111:8097"
  isInWeiXin:function() {
    if (browser.versions.mobile) {
      var ua = navigator.userAgent.toLowerCase();
      if(ua.match(/MicroMessenger/i)=="micromessenger") {
        return true;
      } else {
        return false;
      }
    }
    return false
  }
}

export function timeToDate(time,showTime) {
  if (String(time).length == 10) {
    time = time * 1000
  }
  var date = new Date(time);
  time = date.toLocaleDateString();
  var year = date.getFullYear();
  var month = ~~date.getMonth() + 1;
  var day = date.getDate();
  if (showTime) {
    var hour =  date.getHours() > 9 ?  date.getHours() : "0" + date.getHours();
    var minute = date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes();
    var second = date.getSeconds() > 9 ? date.getSeconds() : "0" + date.getSeconds();
    time = time + " " + hour + ":" + minute + ":" + second;
  } else {
    time = year + "-" + (month > 9 ? month : "0" + month) + "-" + (day > 9 ? day : "0" + day);
  }
  time = time.split('/').join('-')
  return time
}