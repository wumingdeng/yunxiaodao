var express=require('express');
var w_router=express.Router();
var wechat = require('wechat');
w_router.use(express.query());
var config = require('../config.json')
var app = express()
var mem = require('../memory')
// w_router.use('/', wechat(config.token, function(req, res, next) {
// 	var message = req.weixin;
// 	//文本
// 	if (message.Content === '1') {
// 		res.reply('hehe'+Date.now());
// 	}
// }));


w_router.use('/', wechat(config.token).text(function (message, req, res, next) {
  // 微信输入信息都在message上
  res.reply('欢迎来到测试号！');
}).location(function (message, req, res, next) {
  res.reply('location');
}).image(function (message, req, res, next) {
  res.reply('image');
}).voice(function (message, req, res, next) {
  res.reply('voice');
}).link(function (message, req, res, next) {
  res.reply('link');
}).event(function (message, req, res, next) { 
  if (message.Event == "CLICK") {
  	res.reply([
      {
        title: '足部健康报告',
        description: '请点击查看您的足部健康报告',
        picurl: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495434160&di=011945b146f93281dbd690a59fbcca36&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.cnxmld.com%2Ftupians%2Fbd14908247.jpg',
        url: config.webAddress + '/?wxid=' + message.FromUserName + '&type=1&page=foot'
      }
  	])
  }else if(message.Event === 'subscribe'){
    //  res.reply('欢迎关注测试号！');
     console.log(message.Ticket)
     mem.r.client.hmset(message.Ticket, "open_id", message.FromUserName);
  }else if(message.Event === 'SCAN'){
    //  res.reply('欢迎扫描测试号！'+message.EventKey+' '+message.FromUserName);
     mem.r.client.hmset(message.Ticket, "open_id", message.FromUserName);
  }
}).middlewarify());


module.exports = w_router;