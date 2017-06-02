const errorTitle = 'error'
const emptyInput = '输入不能为空'
export const ERROR_EMPTY_INPUT = 1
export const ERROR_MSG = {
  1: emptyInput
}
// GLOBAL ROUTER ACCESS DEFAULT
// 1.if you use import xx from thismodule,will use default
// 2.if you use import {xx} from this module,will use the varible have name of xx and will be read only
// 3.if you use import * as xx from thismoudele,will use all as one object name xx
export default {
  r: {},
  // debugUrlPrefix: 'http://czwyxd.ngrok.cc'
  debugServerAddress: 'http://192.168.18.216:8092',
  serverAddress: 'http://yzxs.sujudao.com:8092',
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