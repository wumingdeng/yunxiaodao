var express=require('express');
var w_router=express.Router();
var wechat = require('wechat');
w_router.use(express.query());
var config = require('../config.json')
// tour_router.route('/').post(function(req,res){
// 	 res.send("<xml> \
// 		<ToUserName><![CDATA[toUser]]></ToUserName> \
// 		<FromUserName><![CDATA[fromUser]]></FromUserName> \ 
// 		<CreateTime>12345678</CreateTime> \
// 		<MsgType><![CDATA[text]]></MsgType> \
// 		<Content><![CDATA[你好]]></Content> \
// 		</xml>")
// });

module.exports = w_router;