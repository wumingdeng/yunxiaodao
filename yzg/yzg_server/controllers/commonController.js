
var db = require('../models')
var utils = require('../utils')
var tpl = require('../template.json')
var Common = {
    OnRestartServer:function(){
        // 重启时恢复场景
        // 没有触发expire的键值监听还会在redis里
        // 1.如果redis进程挂了,那么程序进程自动挂掉
        // todo 2.涉及到定时器,全局唯一订单的,后面考虑拖出去做成单独的一个进程而不走pm2的多进程管理
        // 清除支付过期的订单，设置为无效
        var mem = require('../memory')
        var nowsec = Math.floor(Date.now()/1000)
        db.orders.update({valid:0},{"where":{payend:{$lte:nowsec}}}).then(function(){
            // 恢复过期的waitend场景,延后为从此刻开始2分钟后过期
            db.orders.findAll({attributes:['id','workerid','assignManully'],where:{status:1,waitend:{$lte:nowsec},workerid:{$ne:-1}}}).then(function(datas){
                for(var i = 0;i< datas.length;i++){
                    if(datas[i].workerid === 0){
                        // 系统分配抢单
                        mem.r.pub.set('aes:'+datas[i].id,1)
                        mem.r.pub.expire('aes:'+datas[i].id,120)
                    }else{
                        // 指定具体技师
                        if(datas[i].assignManully === 1){
                            mem.r.pub.set('aes:'+datas[i].id,1)
                            mem.r.pub.expire('aes:'+datas[i].id,120)
                        }else{
                            mem.r.pub.set('ae:'+datas[i].id,1)
                            mem.r.pub.expire('ae:'+datas[i].id,120)
                        }
                    }
                    // db.care_records.findAll({where:{status:}})
                }
                // 更多需要恢复的场景
                
            })
        })
    },
    OnPayIdExpired:function(id,uid){
        db.orders.update({valid:0},{"where":{id:id}}).then(function(){
            // 通知用户订单失效
            db.users.findOne({attributes:['wxid'],where:{id:uid}}).then(function(user){
                // 额外数据
                var contentVar={}
                utils.sendDataToWxServer({wxid:user.wxid,tid:tpl.template_inform_user_pay_expired,d:contentVar},
                    function(){})
            })
        })
    },
    OnAssignIdExpired:function(id){
        // 重置等待状态
        db.orders.update({workerid:-1},{"where":{id:id}}).then(function(){
            // 通知用户重新选择
            var query = 'select u.wxid,o.price \
                    from users u join orders o on o.userid = u.id where o.id=?'
            db.sequelize.query(query, { replacements: [id], 
                type: db.sequelize.QueryTypes.SELECT }
                ).then(function(records){
                if(records){
                    var contentVar={}
                    utils.sendDataToWxServer({wxid:records[0].wxid,tid:tpl.template_inform_user_repick,d:contentVar},
                        function(){})
                }
            })
        })
    },
    OnAssignIdSystemExpired:function(id){
         // todo 通知管理员派单
        db.orders.update({"assignManully":1,workerid:0},{"where":{id:id}}).then(function(){
            // 设置为手工派单状态
        })
    },
    OnExamConfirmExpired:function(id){

    },
    OnExamReadyConfirm:function(id){
        
    },
    OnExamMake:function(order){
        // 生成所有产检记录
        var mem = require('../memory')
        var service_ = mem.m.services[order.service]
        if(service_ !== undefined){
            if (service_.care_start > 0) {
                var staff = undefined
                for(var k in mem.m.staffs){
                    if(mem.m.staffs[k].hospital_id === order.hospitalid){
                        staff = mem.m.staffs[k]
                        break
                    }
                }
                var care_start = service_.care_start

                db.users.findOne({attributes:['lastPeriod','wxid'],where:{id:order.userid}}).then(function(user){
                    if(user){
                        for (var i in mem.m.care_configs) {
                            if (Number(i) >= care_start) {
                                var care_cfg = mem.m.care_configs[i]
                                if(care_cfg !== undefined && staff !== undefined){
                                    // 找不到了，产检结束了
                                    var weeksec = care_cfg.weeks*604800
                                    var d2 = new Date(order.lastPeriod*1000)
                                    var d3 = new Date(d2.toLocaleDateString())
                                    var nowsec = Math.floor(d3.getTime()/1000)
                                    // 提前几天通知
                                    var waitend =  nowsec + weeksec - mem.m.configs[0].informUserExamDays*86400
                                    // 计划产检时间
                                    var scheduled =  nowsec + weeksec
                                    // var expired = waitend - nowsec
                                    db.care_records.create({orderid:order.id,care_cfg_id:care_cfg.id,
                                    user_wxid:order.userwxid,staff_wxid:staff.wxid,
                                        first_inform_end:waitend,schedule_time:scheduled}).then(function(cr){
                                    })
                                }                    
                            }
                        }
                    }
                })
            }
        }
    }
}

module.exports = Common