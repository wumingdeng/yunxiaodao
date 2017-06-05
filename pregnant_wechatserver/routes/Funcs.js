var express=require('express');
var tour_router=express.Router();
var db = require('../models')
var g = require('../global')
var utils = require('../utils')
var cfg = require('../config.json')
var jsSHA = require('jssha')

var Payment = require('wechat-pay').Payment;
var payment = new Payment(g.wechatPayInitConfig);


// for quick test
tour_router.route('/test').get(function(req,res){
    // db.configs.findAll({
    //     // attributes: ['cat_id'],
    //     where:{id:0}
    // }).then(function(data){
    //     // mem.r.pub.publish('memory-channel',"666")
    //     res.json({d:data});
    // })
    res.json({ok:1});
});

tour_router.route('/accesstoken').get(function(req,res){
    res.write(cfg.access_token)
    res.end()
})

// server wechatserver :80
// url:api/jsticket
// 返回jsticket
tour_router.route('/jsticket').get(function(req,res){
    res.json({t:cfg.jsticket})
})

tour_router.route('/sendMsg').post(function(req,res){
    var wxid = req.body.wxid || ''
    if(wxid === ''){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    // templateid
    var tid = req.body.tid || ''
    if(tid === ''){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    // data formate: "d":{"first":{"value":"DAMAO"},"service":{"value":"ni h你好s j"}}
    // first ,service is setting as {{first.DATA}} in template,alse can use "color"
    var data = req.body.d
    utils.wechat_f.sendToUser(wxid,tid,data)
})

tour_router.route('/sendPay').post(function(req,res){
    var data = req.body;
    data.spbill_create_ip = data.spbill_create_ip[0];
    console.log(data);

    payment.getBrandWCPayRequestParams(data, function(err, payargs){
        console.log('支付返回');
        console.log(err)
        if (err) {
            res.json({err:err})
        } else {
            console.log(payargs)
            res.json(payargs);
        }
    });
})


//接受微信付款确认请求
var middleware = require('wechat-pay').middleware;
// app.use('/wechat/payResult', middleware(g.wechatPayInitConfig).getNotify().done(function(message, req, res, next) {
tour_router.route('/payResult').post(middleware(g.wechatPayInitConfig).getNotify().done(function(message, req, res, next) {
  var openid = message.openid;
  var order_id = message.out_trade_no;
  console.log('支付确认。。' + order_id);

  /**
   * 查询订单，在自己系统里把订单标为已处理
   * 如果订单之前已经处理过了直接返回成功
   */
  db.orders.findOne({where:{'userid': openid,'id': order_id}}).then(function(order){
    if (!order) {
      res.reply('fail');
    }
    if(order.status == 0) {
      db.orders.update({status: 1},{where:{'id': order_id}}).then(function() {
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

tour_router.route('/sendMoney').get(function(req,res){

})

tour_router.route('/create_menu').get(function(req,res){
    utils.wechat_f.setupMenu(cfg.authCodeAddress)
    res.json({"cc":3})
})

tour_router.route('/auth_check').get(function(req,res){
    authUser("check",req,res)
});

tour_router.route('/auth_foot').get(function(req,res){
    authUser("foot",req,res)
});

tour_router.route('/auth_shoe').get(function(req,res){
    authUser("shoeDetail",req,res)
});

function authUser(page,req,res) {
    utils.wechat_f.getUserOpenid(req.query.code,function(openid){
        if(openid){
            utils.wechat_f.getUserBrief(openid,function(data){
                if(data){
                    console.log(db.users);
                    // res.redirect(301,'http://czw321.ngrok.cc/?wxid='+openid+'&type=1');
                    db.users.findOne({where:{'wxid':openid}}).then(function(user){
                        if(user){
                            console.log('用户已存在')
                            db.users.update({headUrl:data.headimgurl,name:data.nickname},{where:{wxid:openid}}).then(function(){
                                // to change redirect url
                                res.redirect(301,cfg.webAddress + '/?wxid='+openid+'&type=1&page=' + page);
                            })
                        }else{
                            console.log('创建用户')
                            db.users.create({wxid:openid,headUrl:data.headimgurl,name:data.nickname}).then(function(){
                                // to change redirect url
                                res.redirect(301,cfg.webAddress + '/?wxid='+openid+'&type=1$page=' + page);
                            })
                        }
                    })
                    //toremove test
                    // res.json({ok:110});
                }else{
                    res.json({err:g.errorCode.WRONG_WXCHAT_AUTHFAILED});
                }
                
            },function(){
                res.json({err:g.errorCode.WRONG_WXCHAT_AUTHFAILED});
            })
        }
    },function(){
        res.json({err:g.errorCode.WRONG_WXCHAT_AUTHFAILED});
    })
}

module.exports=tour_router;