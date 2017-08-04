var express=require('express');
var tour_router=express.Router();
var db = require('../models')
var g = require('../global')
var utils = require('../utils')
var cfg = require('../config.json')
var template = require('../template.json');
var jsSHA = require('jssha')
var moment = require('moment')

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
    if (cfg.access_token) {
        res.write(cfg.access_token)
        res.end()
    } else {
        res.json({err:0})
    }
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
    var url = req.body.toUrl
    utils.wechat_f.sendToUser(wxid,tid,data,url)
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


tour_router.route('/footReport').post(function(req,res){
    var wxid = req.body.wxid || ''
    var rid = req.body.rid
    if(wxid === '' || !rid){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    // templateid
    var tid = template.template_message_send_foot_report;
    // data formate: "d":{"first":{"value":"DAMAO"},"service":{"value":"ni h你好s j"}}
    // first ,service is setting as {{first.DATA}} in template,alse can use "color"
    var data = {
        first:{
            value:'您好，您的足部检测报告已完成。',
            color:"#000000"
        },
        keyword1:{
            value:'孕小岛'
        },
        keyword2:{
            value:moment().format("YYYY-MM-DD hh:mm:ss")
        },
        remark:{
            value:'请您点击查看'
        }
    }
    var url = cfg.webAddress + '/?page=foot&rid=' + rid;
    utils.wechat_f.sendToUser(wxid,tid,data,url)
    res.json({ok:1})
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
    utils.wechat_f.setupMenu(cfg.webAddress)
    res.json({"cc":3})
})

// tour_router.route('/auth_check').get(function(req,res){
//     authUser("check",req,res)
// });

// tour_router.route('/auth_foot').get(function(req,res){
//     authUser("foot",req,res)
// });

// tour_router.route('/auth_shoe').get(function(req,res){
//     authUser("shoeDetail",req,res)
// });

tour_router.route('/auth').post(function(req,res){
    var code = req.body.code
    console.log('login:' + code);
    if (!code) {
        res.json({err:g.errorCode.WRONG_PARAM})
    }
    if (code == 'heheda' && cfg.debug && cfg.testUser) {
        db.users.findOne({where:{'wxid':cfg.testUser}}).then(function(user){
            if (user) {
                res.json({ok:user})
            } else {
                res.json({err:g.errorCode.WRONG_WXCHAT_AUTHFAILED});
            }
        })
        return
    }
    utils.wechat_f.getUserOpenid(code,function(openid){
        if(openid){
            console.log('get openid:' + openid)
            utils.wechat_f.getUserBrief(openid,function(data){
                if(data){
                    console.log('get user data:' + data);
                    // res.redirect(301,'http://czw321.ngrok.cc/?wxid='+openid+'&type=1');
                    db.users.findOne({where:{'wxid':openid}}).then(function(user){
                        if(user){
                            // console.log('用户已存在')
                            if (user.isBoss || user.isSaleman) {
                                //推广人员取信息
                                db.salemans.findOne({where:{userid: openid}}).then(function(sdata){
                                    if (sdata) {
                                        //把推广人员表的数据合并到用户数据里
                                        for (key in sdata.dataValues) {
                                            var value = sdata.dataValues[key];
                                            user.dataValues[key] = user.dataValues[key] || value;
                                        }
                                        //更新昵称和头像
                                        db.salemans.update({headUrl:data.headimgurl || '',name:data.nickname || ''},{where:{userid:openid}}).then(function(){
                                            res.json({ok:user})
                                        })
                                    } else {
                                        res.json({ok:user})
                                    }
                                })
                            } else {
                                //一般用户
                                if (data.nickname || data.subscribe == 1) {
                                    db.users.update({headUrl:data.headimgurl || '',name:data.nickname || ''},{where:{wxid:openid}}).then(function(){
                                        // to change redirect url
                                        // res.redirect(301,cfg.webAddress + '/?wxid='+openid+'&type=1&page=' + page);
                                        res.json({ok:user})
                                    })
                                } else {
                                    res.json({ok:user})
                                }
                            }
                        }else{
                            // console.log('创建用户')
                            db.users.create({wxid:openid || '',headUrl:data.headimgurl || '',name:data.nickname || ''}).then(function(newUser){
                                // to change redirect url
                                // res.redirect(301,cfg.webAddress + '/?wxid='+openid+'&type=1$page=' + page);
                                res.json({ok:newUser})
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
    },function(error){
        console.log('auth code:'+code)   
        console.log(error);
        res.json({err:g.errorCode.WRONG_WXCHAT_AUTHFAILED});
    })
})

var createNonceStr = function() {
    return Math.random().toString(36).substr(2, 15);
};

// timestamp
var createTimeStamp = function () {
    return parseInt(new Date().getTime() / 1000) + '';
};

var calcSignature = function (ticket, noncestr, ts, url) {
    var str = 'jsapi_ticket=' + ticket + '&noncestr=' + noncestr + '&timestamp='+ ts +'&url=' + url;
    shaObj = new jsSHA(str, 'TEXT');
    return shaObj.getHash('SHA-1', 'HEX');
}

function getSignature(url){
    var ts = createTimeStamp();
    var noncestr = createNonceStr();
    var signature = calcSignature(cfg.jsticket, noncestr, ts, url);
    return signature
}

tour_router.route('/signature').post(function(req,res){
    var url = req.body.url || ''
    if(url === ''){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    var ts = createTimeStamp();
    var noncestr = createNonceStr();
    var signature = calcSignature(cfg.jsticket, noncestr, ts, url);
    // var sig = getSignature(url)
    res.json({sig:signature,ts:ts,noncestr:noncestr,appid:cfg.appid})
})

tour_router.route('/getqrcode').post(function(req,res) {
    var userid = req.body.userid
    if (!userid) {
        res.json({err:g.errorCode.WRONG_PARAM})
    }
    function onSuccess(resData) {
        // if (resData.ticket) {
        //     res.redirect(301,'https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=' + resData.ticket);
        // }
        //存下ticket
        db.qrcodes.update({ticket:resData.ticket},{where:{userid:userid}})
        res.json({ok:resData.ticket})
    }

    function onError() {
        res.json({err:0})
    }

    //查找用户是否有二维码了
    db.qrcodes.findOne({where:{userid:userid}}).then(function(data) {
        if (data) {
            if (data.ticket) {
                res.json({ok:data.ticket})
            } else {
                utils.wechat_f.getRQCodeTicket(data.id,onSuccess,onError)
            }
        } else {
            db.qrcodes.create({userid:userid}).then(function(newData) {
                utils.wechat_f.getRQCodeTicket(newData.id,onSuccess,onError)
                // res.json({ok:newData.id})
            })
        }
    })
})

module.exports=tour_router;