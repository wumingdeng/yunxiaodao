var express=require('express');
var order_router=express.Router();
var db = require('../models')
var mem = require('../memory')
var g = require('../global')
var cf = require('../controllers/commonController')
var utils = require('../utils')
var tpl = require('../template.json')


//创建订单
order_router.route('/ordermake').post(function(req,res){
    var wxid = req.decoded.wxid || ''
    var contact = req.body.contact || ''
    var gender = req.body.gender || 0
    var tel = req.body.tel
    var address = req.body.address || ''
    var province = req.body.province || ''
    var city = req.body.city || ''
    var area = req.body.area
    var shoeid = req.body.shoeid
    var size = req.body.size
    var color = req.body.color || ''
    var type = req.body.type
    var remark = req.body.remark
    if(false){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    if(mem.m.products[shoeid]===undefined){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    
    // 计算价格
    var price = mem.m.products[shoeid].price;
    var shoeName = mem.m.products[shoeid].name;
    
    db.users.findOne({where:{'wxid':wxid}}).then(function(data){
        if(data){
            var nowsec = Math.floor(Date.now()/1000)
            console.log(data);
            console.log('下单了:' + data.dataValues.qrcodeOwner)
            db.orders.create({userid:wxid,contact:contact,gender:gender,tel:tel,
                address:address,province:province,city:city,area:area,shoeid:shoeid,
                price:price,shoeName:shoeName,size:size,createtime:nowsec,color:color,
                type:type,remark:remark,status:0,valid:1,reference:data.dataValues.qrcodeOwner}).then(function(order) {
                    // mem.r.pub.set('pe:'+order.id+":"+data.id,1)
                    // mem.r.pub.expire('pe:'+order.id+":"+data.id,payExpireSec)
                    // mem.r.pub.expire('pe:'+order.id,7)
                // res.json({w:price})
                //更新用户资料
                db.users.update({contact:contact,gender: gender,tel: tel,address: address,province: province,city: city,area: area},{where:{wxid:wxid}})
                
                //支付请求
                utils.sendPayToWxServer({
                    spbill_create_ip: req.ip.match(/\d+\.\d+\.\d+\.\d+/),
                    openid: wxid,
                    out_trade_no: order.id,
                    body: order.shoeName,
                    total_fee:price * 100,
                    // total_fee:1,
                    trade_type: 'JSAPI'
                }, function(err, response, payargs){
                    console.log('支付返回');
                    console.log(payargs);
                    res.json(payargs)
                })
            })
        }else{
            res.json({err:g.errorCode.WRONG_USER_MISSING})
        }
    })
});

//支付已经生成的订单
order_router.route('/orderpay').post(function(req,res){
    var oid  = req.body.oid || 0
    if(oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.orders.findOne({where:{'id':oid}}).then(function(order){
        if(order){
            //支付请求
            utils.sendPayToWxServer({
                spbill_create_ip: req.ip.match(/\d+\.\d+\.\d+\.\d+/),
                openid: order.userid,
                out_trade_no: order.id,
                body: order.shoeName,
                total_fee:order.price * 100,
                // total_fee:1,
                trade_type: 'JSAPI'
            }, function(err, response, payargs){
                console.log('支付返回');
                console.log(payargs);
                res.json(payargs)
            })
        }
    })
})
// // 订单支付成功回调
// order_router.route('/orderpay').post(function(req,res){
//     // var wxid = req.decoded.wxid || ''
//     var oid  = req.body.oid || 0
//     if(oid === 0){
//         res.json({err:g.errorCode.WRONG_PARAM})
//         return
//     }
//     db.orders.findOne({where:{'id':oid}}).then(function(order){
//         if(order){
//             mem.r.pub.del('pe:'+order.id+":"+order.userid)
//             // 根据选择进行下一步操作
//             if(order.valid == 0){
//                 res.json({err:g.errorCode.WRONG_ORDER_NOT_EXIST})
//                 return
//             }
//             if(order.status!=0){
//                 res.json({err:g.errorCode.WRONG_ORDER_STATUS})
//                 return
//             }
//             if(order.status==g.orderStatus.PAYED_ACCEPT_NOT_START){
//                 res.json({err:g.errorCode.WRONG_ORDER_STATUS})
//                 return
//             }
//             if(order.workerid == -1){
//                 res.json({w:1})
//                 return
//             }
//             // 更改订单状态
//             var ExpireSec = 0//mem.m.configs[0].orderWaitTime * 3600
//             if(order.workerid === 0){
//                 // 系统派单
//                 ExpireSec = mem.m.configs[0].workerTakeWaitTime * 3600
//             }else{
//                 // 指定技师等待技师响应时间
//                 ExpireSec = mem.m.configs[0].orderWaitTime * 3600
//             }
//             var waitend = Math.floor(Date.now()/1000) + ExpireSec
//             db.orders.update({status:1,payend:0,actualpay:order.needpay,valid:1,waitend:waitend},
//                 {where:{'id':oid}}).then(function(){
//                     var haveServiceWorker = 
//                         getPropertyWorker(order.workerid,ExpireSec,order.id,order.service,order.geo)
//                     res.json({ok:1,data:haveServiceWorker})
//                 }
//             )
//         }else{
//             res.json({err:g.errorCode.WRONG_ORDER_NOT_EXIST})
//         }
//     })
// });

order_router.route('/orderlistUser').post(function(req,res){
    var uid  = req.body.uid || 0
    var offset = req.body.offset || 0
    var limit = req.body.limit || 0
    if(uid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        db.orders.findAll({order:'createtime DESC',offset:offset,limit:limit,where:{'userid':uid,'valid':1}}).then(function(order){
            // for(var i=0;i<orders;i++){
            //     // 剔除不同状态下过期的 防止redis进程挂掉的影响
            //     var nowsec = Math.floor(Date.now()/1000)
            //     if(orders[i].status === 0){
            //         if(orders[i].payend > nowsec){
            //             cf.OnPayIdExpired(orders[i].id)
            //         } 
            //     }else if(orders[i].status === 1){

            //     }
            // }
            //加上需要显示的数据
            order.forEach(function(item,index){ 
                var sinfo = mem.m.products[item.shoeid]
                item.dataValues.shoeName = sinfo.name;
                // item.dataValues.price = sinfo.price;
                // item.dataValues.smallPic = sinfo.smallPic
                if (sinfo.color && item.color) {
                    var colorObj = JSON.parse(sinfo.color);
                    // console.log(colorObj)
                    item.dataValues.smallPic = colorObj[item.color] || '';
                }
            });  
            res.json({w:order})
        })
    }
});


// 用户取消订单
order_router.route('/ordercancel').post(function(req,res){
    var oid  = req.body.oid || 0
    var wxid = req.decoded.wxid || ''
    if(oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    if(wxid === ''){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.orders.findOne({where:{'id':oid,'valid':1}}).then(function(order){
        if(order){
            // 设置为无效订单
            db.orders.update({valid:0,status:g.orderStatus.CANCELED},{where:{id:order.id}}).then(function(){
                res.json({ok:1})
            })
        }else{
            res.json({err:g.errorCode.WRONG_ORDER_NOT_EXIST})
            return
        }
    })
});


//推广的订单
order_router.route('/orderlistReference').post(function(req,res){
    var userid = req.decoded.wxid
    if (!userid) {
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.orders.findAll({order:'createtime DESC', where:{reference:userid}}).then(function(order) {
        if (order) {
            order.forEach(function(item,index){ 
                var sinfo = mem.m.products[item.shoeid]
                item.dataValues.shoeName = sinfo.name;
                if (sinfo.color && item.color) {
                    var colorObj = JSON.parse(sinfo.color);
                    // console.log(colorObj)
                    item.dataValues.smallPic = colorObj[item.color] || '';
                }
            });  
            res.json({ok:order})
        } else {
            res.json({ok:[]})
        }
    })
})


// 获取订单详情
order_router.route('/orderdetail').post(function(req,res){
    var oid = req.body.oid || 0
    if(oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    var query = 'select sr.settled,sr.lastUpdated,o.id,o.userid,o.workerid,o.catalog,o.service,o.address,o.user_idnumber,o.geoaddress\
            ,o.hospitalid,o.createtime,o.status,o.price,o.waitend,o.username,o.tel,c.rate_communicate,c.rate_pro,c.rate_skill\
            ,o.extra,u.lastPeriod\
             from orders o \
             left join settle_records sr on o.id=sr.oid \
             left join comments c on o.id = c.oid \
             left join users u on o.userid = u.id \
             where o.id=?'
    db.sequelize.query(query, { replacements: [oid], 
                type: db.sequelize.QueryTypes.SELECT }
            ).then(function(records){
                if(records){
                    if(records.length>0){
                        var settles = []
                        var totalSettle = 0
                        for(var i=0;i<records.length;i++){
                            if(mem.m.services[records[i].service]){
                                var settle ={}
                                if(records[i].settled!==null){
                                    settle.settled = records[i].settled
                                    settle.lastUpdated = records[i].lastUpdated
                                    settles.push(settle)
                                    totalSettle+=settle.settled 
                                }
                            }
                        }
                        var info = {}
                        info = records[0]
                        info.serviceName=mem.m.services[info.service].name
                        info.serviceDesc=mem.m.services[info.service].desc_content_short
                        info.serviceImgurl=mem.m.services[info.service].desc_icon
                        info.hospital = mem.m.hospitals[info.hospitalid].hospital_name
                        info.cname = mem.m.workers[info.workerid] ? mem.m.workers[info.workerid].name : '待确定'


                        if(totalSettle >= info.price * (mem.m.configs[0].orderSettlePercent / 100)){
                            info.settleOver = 1
                        }else{
                            info.settleOver = 0
                        }
                        settles.sort(function(a,b){return a.lastUpdated<b.lastUpdated})
                        res.json({info:info,settles:settles})
                    }else{
                        res.json({err:g.errorCode.WRONG_NO_SUCH_RECORD})
                    }
                }else{
                    res.json({err:g.errorCode.WRONG_PARAM})
                }
            })
})
module.exports=order_router;