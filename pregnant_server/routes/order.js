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
    var wxid = req.body.wxid || ''
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
            db.orders.create({userid:wxid,contact:contact,gender:gender,tel:tel,
                address:address,province:province,city:city,area:area,shoeid:shoeid,
                price:price,shoeName:shoeName,size:size,createtime:nowsec,color:color,
                type:type,remark:remark,status:0}).then(function(order) {
                    // mem.r.pub.set('pe:'+order.id+":"+data.id,1)
                    // mem.r.pub.expire('pe:'+order.id+":"+data.id,payExpireSec)
                    // mem.r.pub.expire('pe:'+order.id,7)
                res.json({w:price})
                //更新用户资料
                db.users.update({contact:contact,gender: gender,tel: tel,address: address,province: province,city: city,area: area},{where:{wxid:wxid}})
            })
        }else{
            res.json({err:g.errorCode.WRONG_USER_MISSING})
        }
    })
});

// 订单支付成功回调
order_router.route('/orderpay').post(function(req,res){
    // var wxid = req.body.wxid || ''
    var oid  = req.body.oid || 0
    if(oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.orders.findOne({where:{'id':oid}}).then(function(order){
        if(order){
            mem.r.pub.del('pe:'+order.id+":"+order.userid)
            // 根据选择进行下一步操作
            if(order.valid == 0){
                res.json({err:g.errorCode.WRONG_ORDER_NOT_EXIST})
                return
            }
            if(order.status!=0){
                res.json({err:g.errorCode.WRONG_ORDER_STATUS})
                return
            }
            if(order.status==g.orderStatus.PAYED_ACCEPT_NOT_START){
                res.json({err:g.errorCode.WRONG_ORDER_STATUS})
                return
            }
            if(order.workerid == -1){
                res.json({w:1})
                return
            }
            // 更改订单状态
            var ExpireSec = 0//mem.m.configs[0].orderWaitTime * 3600
            if(order.workerid === 0){
                // 系统派单
                ExpireSec = mem.m.configs[0].workerTakeWaitTime * 3600
            }else{
                // 指定技师等待技师响应时间
                ExpireSec = mem.m.configs[0].orderWaitTime * 3600
            }
            var waitend = Math.floor(Date.now()/1000) + ExpireSec
            db.orders.update({status:1,payend:0,actualpay:order.needpay,valid:1,waitend:waitend},
                {where:{'id':oid}}).then(function(){
                    var haveServiceWorker = 
                        getPropertyWorker(order.workerid,ExpireSec,order.id,order.service,order.geo)
                    res.json({ok:1,data:haveServiceWorker})
                }
            )
        }else{
            res.json({err:g.errorCode.WRONG_ORDER_NOT_EXIST})
        }
    })
});

order_router.route('/orderlistUser').post(function(req,res){
    var uid  = req.body.uid || 0
    var offset = req.body.offset || 0
    var limit = req.body.limit || 0
    if(uid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        db.orders.findAll({order:'createtime DESC',offset:offset,limit:limit,where:{'userid':uid}}).then(function(order){
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
                item.dataValues.price = sinfo.price;
                item.dataValues.smallPic = sinfo.smallPic
            });  
            res.json({w:order})
        })
    }
});

// 技师订单
order_router.route('/orderlistWorker').post(function(req,res){
    var wid  = req.body.wid || 0
    var offset = req.body.offset || 0
    var limit = req.body.limit || 0
    var mainpage = req.body.mainpage || 0 // 非0为首页信息，为0是所有信息
    if(wid === 0 || limit === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        var query = '';
        if(mainpage!==0){
            query = 'select u.lastPeriod,o.id,o.userid,o.workerid,o.catalog,o.service,o.address,\
            o.createtime,o.status,o.price,o.waitend,o.username,o.tel\
            from orders o \
            join users u on u.id=o.userid \
            left join comments c on o.id = c.oid \
            where (o.workerid=? and o.status=1 or o.status=2 \
            or o.status=3) or (o.workerid=0 and o.status=1) order by o.createtime desc limit ? offset ?'
        }else{
            query = 'select u.lastPeriod,o.id,o.userid,o.workerid,o.catalog,o.service,o.address,\
            o.createtime,o.status,o.price,o.waitend,o.username,o.tel,c.rate_communicate,c.rate_pro,c.rate_skill\
             from orders o join users u on u.id=o.userid \
            left join comments c on o.id = c.oid where o.workerid=? and o.status != 0 and o.status != 5 order by o.createtime desc limit ? offset ?'
        }
        db.sequelize.query(query, { replacements: [wid,limit,offset], 
                type: db.sequelize.QueryTypes.SELECT }
            ).then(function(records){
                for(var i=0;i<records.length;i++){
                    if(mem.m.services[records[i].service]){
                        records[i].serviceName=mem.m.services[records[i].service].name
                        records[i].serviceDesc=mem.m.services[records[i].service].desc_content_short
                        records[i].serviceImgurl=mem.m.services[records[i].service].desc_icon
                    }
                }
                res.json({w:records})
            })
    }
});

// 订单结算
order_router.route('/workerSettle').post(function(req,res){
    var wid  = req.body.wid || 0
    var oid  = req.body.oid || 0
    // input money by admin
    var money = req.body.money || 0
    if(oid === 0 || wid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    if(money <= 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }

    var query1 = 'select sum(sr.settled) as ts,o.price \
                    from orders o left join settle_records sr on o.id = sr.oid where o.id=?'
    db.sequelize.query(query1, { replacements: [oid], 
                type: db.sequelize.QueryTypes.SELECT }
            ).then(function(records){
        if(records){
            if(records.length <= 0){
                res.json({err:g.errorCode.WRONG_ORDER_NOT_EXIST})
                return
            }
             var payed = records[0].ts
             var price = records[0].price
             var limit = price * (mem.m.configs[0].orderSettlePercent / 100)
             if(payed === null || payed === undefined){
                // 未提现过
                if(money > limit){
                    res.json({err:g.errorCode.WRONG_MAX_SETTLED})
                }else{
                    var lu = Math.floor(Date.now()/1000)
                    db.settle_records.create({oid:oid,wid:wid,settled:money,lastUpdated:lu}).then(function(){})
                    db.workers.update({total_prize:db.sequelize.literal('total_prize + '+money)},{where:{id:wid}}).then(function(){
                        // 这个值不涉及前台用户端的，不通知
                    })
                    res.json({ok:1})
                }
            }else{
                // 已提现过
                if(payed + money > limit){
                    res.json({err:g.errorCode.WRONG_MAX_SETTLED,canpay:limit - payed})
                }else{
                    var lu = Math.floor(Date.now()/1000)
                    db.settle_records.create({oid:oid,wid:wid,settled:money,lastUpdated:lu}).then(function(){})
                    db.workers.update({total_prize:db.sequelize.literal('total_prize + '+money)},{where:{id:wid}}).then(function(){
                        // 这个值不涉及前台用户端的，不通知
                    })
                    res.json({ok:1})
                }
            }
        }else{
            res.json({err:g.errorCode.WRONG_ORDER_NOT_EXIST})
        }
    })
});

// 获取我的提现记录
order_router.route('/workerPayRecords').post(function(req,res){
    var offset = req.body.offset || 0
    var limit = req.body.limit || 0
    var wid = req.body.wid || 0
    if(limit === 0 || wid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        db.pay_records.findAll({offset: offset, limit: limit,where:{uid:wid}}).then(function(data){
            res.json({total:data.length,rows:data})
        })
    }
})

// 获取订单结算记录
order_router.route('/orderSettleRecord').post(function(req,res){
    var oid  = req.body.oid || 0
    if(oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.settle_records.findAll({where:{oid:oid}}).then(function(records){
        res.json({ok:records})
    })
})

// 获取我的收入明细
order_router.route('/workerIncomes').post(function(req,res){
    var wid  = req.body.wid || 0
    var offset = req.body.offset || 0
    var limit = req.body.limit || 0
    if(limit === 0 || wid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.workers.findOne({attributes:['frozen_prize','total_prize'],where:{id:wid}}).then(function(worker){
        if(worker){
            db.pay_records.findAll({ order: [['review_date','DESC']],offset: offset, limit: limit,where:{uid:wid,pass:1}}).then(function(data){
                db.settle_records.findAll({order: [['lastUpdated','DESC']],offset:offset,limit:limit,where:{wid:wid}}).then(function(data2){
                    var data_filtered = [];
                    for(var i=0;i<data2.length;i++){
                        var obj = {type:1}
                        obj.ts = data2[i].lastUpdated
                        obj.money = data2[i].settled
                        obj.oid = data2[i].oid
                        data_filtered.push(obj)
                    }
                    for(var j=0;j<data.length;j++){
                        var obj = {type:2}
                        obj.ts = data[j].review_date
                        obj.money = data[j].money
                        obj.review_string = data[j].review_string
                        data_filtered.push(obj)
                    }
                    data_filtered.sort(function(a,b){return a.ts<b.ts})
                    res.json({frozen_prize:worker.frozen_prize,
                        total_prize:worker.total_prize,
                        total_record:data_filtered.length,records:data_filtered})
                    // res.json({frozen_prize:worker.frozen_prize,
                    //     total_prize:worker.total_prize,
                    //     total_settle:data2.length,settle_rows:data2,total_pay:data.length,pay_rows:data})
                })
            })
        }else{
            res.json({err:g.errorCode.WRONG_WORKER_ID})
        }
    })
})

// 提交提现申请
order_router.route('/workerSubmitPay').post(function(req,res){
    var wid  = req.body.wid || 0
    var money  = req.body.money || 0
    if(wid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    if(money < mem.m.configs[0].minPay || money <= 0){
        res.json({err:g.errorCode.WRONG_MIN_PAY})
        return
    }
    // 判断余额
    db.workers.findOne({attributes:['frozen_prize','total_prize'],where:{id:wid}}).then(function(worker){
        if(worker){
            if(worker.total_prize  >= money){
                var startat = Math.floor(Date.now()/1000)
                db.pay_records.create({uid:wid,money:money,submit_date:startat}).then(function(record){
                    if(record){
                        // 冻结这部分钱
                        db.workers.update({frozen_prize:db.sequelize.literal('frozen_prize + '+money),
                            total_prize:db.sequelize.literal('total_prize - '+money)},
                            {where:{id:wid}}).then(function(){
                        })
                        res.json({ok:{total_prize:worker.total_prize-money,frozen_prize:worker.frozen_prize+money}})
                    }else{
                        res.json({err:g.errorCode.WRONG_COMMON_SQL})
                    }
                })
            }else{
                res.json({err:g.errorCode.WRONG_NOT_ENOUGH_MONEY})
            }
        }else{
            res.json({err:g.errorCode.WRONG_WORKER_ID})
        }
    })
})

// 订单提现
order_router.route('/workerPay').post(function(req,res){
    var wid  = req.body.wid || 0
    var money  = req.body.wid || 0
    var id  = req.body.id || 0
    var pass  = req.body.pass || 0
    if(money < mem.m.configs[0].minPay || money <= 0){
        res.json({err:g.errorCode.WRONG_MIN_PAY})
        return
    }
    db.pay_records.findOne({where:{id:id}}).then(function(record){
        if(record){
            if(pass === 1){
                // todo 通知微信公众号支付
                // 
                db.workers.update({frozen_prize:db.sequelize.literal('frozen_prize - '+money)},{where:{id:wid}}).then(function(){})          
            }else{
                db.workers.update({total_prize:db.sequelize.literal('total_prize + '+money),
                frozen_prize:db.sequelize.literal('frozen_prize - '+money)},{where:{id:wid}}).then(function(){})     
            }
        }else{
            res.json({err:g.errorCode.WRONG_NO_SUCH_RECORD})
        }
    })
});

// 技师接单
order_router.route('/orderget').post(function(req,res){
    var oid = req.body.oid || 0
    var wid = req.body.wid || 0
    if(oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    if(wid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    if(mem.m.workers[wid] === undefined){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.orders.findOne({where:{'id':oid,'valid':1}}).then(function(order){
        if(order){
            if(order.status === g.orderStatus.PAYED_NO_ACCEPT){
                // 删除状态提醒
                mem.r.pub.del("ae:"+order.id)
                mem.r.pub.del("aes:"+order.id)
                db.orders.update({status:g.orderStatus.PAYED_ACCEPT_NOT_START,workerid:wid},{where:{id:oid}}).then(function(){
                    // 通知用户有技师接单
                    var contentVar={}
                    utils.sendDataToWxServer({wxid:order.userwxid,tid:tpl.template_inform_user_beaccepted,d:contentVar},
                        function(){})
                    //生成产检记录
                    cf.OnExamMake(order)
                    res.json({ok:1})
                })
            }else{
                res.json({err:g.errorCode.WRONG_ORDER_STATUS})
            }
        }else{
            res.json({err:g.errorCode.WRONG_ORDER_NOT_EXIST})
        }
    })
});

// 技师开始服务
// 用户点击出院
order_router.route('/orderStart').post(function(req,res){
    var oid = req.body.oid || 0
    var wid = req.body.wid || 0
    if(oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    if(wid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    if(mem.m.workers[wid] === undefined){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.orders.findOne({where:{'id':oid,workerid:wid,'valid':1}}).then(function(order){
        if(order){
            console.log('order.......',order.status)
            if(order.status === g.orderStatus.PAYED_ACCEPT_NOT_START){
                var startat = Math.floor(Date.now()/1000)
                db.orders.update({status:g.orderStatus.PAYED_STARTED,startat:startat},{where:{id:oid}}).then(function(){
                    // 通知技师开始服务
                    var worker = mem.m.workers[order.workerid]
                    if(worker === undefined){
                        res.json({err:g.errorCode.WRONG_WORKER_ID})
                    }else{
                        var contentVar={}
                        utils.sendDataToWxWorkerServer({wxid:worker.wxid,tid:tpl.template_inform_worker_start,d:contentVar},
                            function(){
                                res.json({ok:1})
                            })
                    }
                })
            }else{
                res.json({err:g.errorCode.WRONG_ORDER_STATUS})
            }
        }else{
            res.json({err:g.errorCode.WRONG_ORDER_NOT_EXIST})
        }
    })
})

// 技师服务结束
order_router.route('/orderFinish').post(function(req,res){
    var oid = req.body.oid || 0
    var wid = req.body.wid || 0
    if(oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    // if(wid === 0){
    //     res.json({err:g.errorCode.WRONG_PARAM})
    //     return
    // }
    // if(mem.m.workers[wid] === undefined){
    //     res.json({err:g.errorCode.WRONG_PARAM})
    //     return
    // }
    db.orders.findOne({where:{'id':oid,'valid':1}}).then(function(order){
        if(order){
            if(order.status === g.orderStatus.PAYED_STARTED){
                var endat = Math.floor(Date.now()/1000)
                db.orders.update({status:g.orderStatus.PAYED_OVER,finishat:endat},{where:{id:oid}}).then(function(){
                    // 通知用户评价
                    var contentVar={}
                    utils.sendDataToWxServer({wxid:order.userwxid,tid:tpl.template_inform_user_comment,d:contentVar},
                        function(){})
                })
            }else{
                res.json({err:g.errorCode.WRONG_ORDER_STATUS})
            }
        }else{
            res.json({err:g.errorCode.WRONG_ORDER_NOT_EXIST})
        }
    })
})

// 管理后台人工派单
order_router.route('/orderSetByAdmin').post(function(req,res){
    var oid = req.body.oid || 0
    var wid = req.body.wid || 0
    if(oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    if(wid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    // 设置订单指定worker
    // 重置等待状态
    var ExpireSec = mem.m.configs[0].manualAssignWaitTime * 3600
    var waitend = Math.floor(Date.now()/1000) + ExpireSec
    db.workers.findOne({attributes:['wxid'],where:{id:wid}}).then(function(data){
        if(data){
            var contentVar={}
            utils.sendDataToWxWorkerServer({wxid:data.wxid,tid:tpl.template_inform_worker_assign,d:contentVar},
                function(){})
            db.orders.update({waitend:waitend,workerid:wid},{"where":{id:oid}}).then(function(){
                mem.r.pub.del('aes:'+oid)
                mem.r.pub.set('aes:'+oid,1)
                mem.r.pub.expire('aes:'+oid,ExpireSec)
                res.json({ok:1})
            })
        }else{
            res.json({err:g.errorCode.WRONG_PARAM})
        }
    })
});

// 用户取消订单
order_router.route('/ordercancel').post(function(req,res){
    var oid  = req.body.oid || 0
    var wxid = req.body.wxid || ''
    if(oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    if(wxid === ''){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.orders.findOne({where:{'id':oid,'valid':1}}).then(function(order){
        var moneyBack = 0
            if(order){
                if(order.workerid === -1){
                    // 打回重选的,选择取消,全额退款
                    moneyBack = order.actualpay
                }else{
                    // 订单状态0-未支付 1-已支付未接单 2-已接单未开始服务 3-已开始服务 4-已结束订单 5-已取消
                    if(order.status === g.orderStatus.PAYED_NO_ACCEPT){
                        // 已支付未开始,需要扣除一定比例
                        moneyBack = order.actualpay * (100 - mem.m.configs[0].userCancelFeeA)/100
                    }else if(order.status === g.orderStatus.PAYED_ACCEPT_NOT_START){
                        moneyBack = order.actualpay * (100 - mem.m.configs[0].userCancelFeeB)/100
                    }else if(order.status === g.orderStatus.PAYED_STARTED){
                        // 开始服务不能取消
                        res.json({err:g.errorCode.WRONG_ORDER_CANNOT_CANCEL})
                        return
                    }else if(order.status === g.orderStatus.NOTPAY){
                    
                    } else{
                        res.json({err:g.errorCode.WRONG_ORDER_STATUS})
                        return
                    }
                }
                // 余额先存在用户身上的字段
                db.users.update({money:db.sequelize.literal('money + '+moneyBack)},{where:{wxid:wxid}}).then(function(){})
                // todo 通知发红包
                // 删除订单相关计时
                mem.r.pub.del("ae:"+order.id)
                mem.r.pub.del("aes:"+order.id)
                // 设置为无效订单
                db.orders.update({valid:0,status:g.orderStatus.CANCELED},{where:{id:order.id}}).then(function(){
                    res.json({ok:1})
                })
                // 删除产检记录
                db.care_records.destroy({where:{orderid:order.id}}).then(function(){
        
                })
            }else{
                res.json({err:g.errorCode.WRONG_ORDER_NOT_EXIST})
                return
            }
    })
});

// 技师不接单
order_router.route('/orderrefuse').post(function(req,res){
    var oid  = req.body.oid || 0
    if(oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.orders.update({workerid:-1},{"where":{id:oid}}).then(function(){
        // 通知用户重新选择
        var query = 'select u.wxid,o.price \
                from users u join orders o on o.userid = u.id where o.id=?'
        db.sequelize.query(query, { replacements: [oid], 
            type: db.sequelize.QueryTypes.SELECT }
            ).then(function(records){
            if(records){
                var contentVar={}
                utils.sendDataToWxServer({wxid:records[0].wxid,tid:tpl.template_inform_user_repick,d:contentVar},
                    function(){})

                // 设置为无效订单
                db.orders.update({workerid:-1,status:g.orderStatus.RESELECTION},{where:{id:oid}}).then(function(){
                    res.json({ok:1})
                })
            }
        })
    })
})

// 用户重新选择服务技师
order_router.route('/orderreset').post(function(req,res){
    var oid  = req.body.oid || 0
    // 新选择的技师
    var wid  = req.body.wid
    if(oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    if(wid === -1 || wid === undefined){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.orders.findOne({where:{'id':oid,'valid':1,'workerid':-1}}).then(function(order){
        if(order){
            var ExpireSec = 0//mem.m.configs[0].orderWaitTime * 3600
            var waitend = Math.floor(Date.now()/1000) + ExpireSec
            if(wid === 0){
                // 系统派单
                ExpireSec = mem.m.configs[0].workerTakeWaitTime * 3600
            }else{
                // 指定技师等待技师响应时间
                ExpireSec = mem.m.configs[0].orderWaitTime * 3600
            }
            db.orders.update({valid:1,waitend:waitend,workerid:wid,status:g.orderStatus.PAYED_NO_ACCEPT},
                {where:{'id':oid}}).then(function(){
                    var haveServiceWorker = getPropertyWorker(wid,ExpireSec,oid,order.service,order.geo)
                    res.json({ok:1})
                })
        }else{
            res.json({err:g.errorCode.WRONG_ORDER_CANNOT_RESET})
        }
    })
});

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