var express=require('express');
var w_router=express.Router();
var wechat = require('wechat');
w_router.use(express.query());
var config = require('../config.json')
var app = express()
var g = require('../global')
var db = require('../models')
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
  }
}).middlewarify());


//接受微信付款确认请求
var middleware = require('wechat-pay').middleware;
app.use('/payResult', middleware(g.wechatPayInitConfig).getNotify().done(function(message, req, res, next) {
  var openid = message.openid;
  var order_id = message.out_trade_no;
  var attach = {};
  try{
   attach = JSON.parse(message.attach);
  }catch(e){}

  /**
   * 查询订单，在自己系统里把订单标为已处理
   * 如果订单之前已经处理过了直接返回成功
   */
  db.orders.findOne({where:{'userid': openid,'id': order_id}}).then(function(order){
    if (!order) {
      res.reply('fail');
    }
    if(order.status == 0) {
      db.orders.update({status: 1},{whree:{'id': order_id}}).then(function() {
        res.reply('success');
      })
    } else {
      res.reply('success');
    }
  })


  /**
   * 有错误返回错误，不然微信会在一段时间里以一定频次请求你
   * res.reply(new Error('...'))
   */
}));


module.exports = w_router;