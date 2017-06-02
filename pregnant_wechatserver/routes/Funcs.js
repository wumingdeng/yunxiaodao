var express=require('express');
var tour_router=express.Router();
var db = require('../models')
var g = require('../global')
var utils = require('../utils')
var cfg = require('../config.json')
var jsSHA = require('jssha')

var Payment = require('wechat-pay').Payment;
var initConfig = {
  partnerKey: "<partnerkey>",
  appId: cfg.appid,
  mchId: cfg.mchid,
  notifyUrl: cfg.wechatServerAddress,
  // pfx: fs.readFileSync("")
};
var payment = new Payment(initConfig);


// for quick test
tour_router.route('/test').get(function(req,res){
    db.configs.findAll({
        // attributes: ['cat_id'],
        where:{id:0}
    }).then(function(data){
        // mem.r.pub.publish('memory-channel',"666")
        res.json({d:data});
    })
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
    console.log(data);

    payment.getBrandWCPayRequestParams(data, function(err, payargs){
        console.log('支付返回');
        console.log(err)
        console.log(payargs)
        if (err) {
            res.json({err:err})
        } else {
            res.json(payargs);
        }
    });
})

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