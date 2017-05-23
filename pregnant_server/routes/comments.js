var express=require('express');
var comm_router=express.Router();
var db = require('../models')
var mem = require('../memory')
var g = require('../global')
comm_router.route('/userCommentWorker').post(function(req,res){
    var oid  = req.body.oid || 0
    var content  = req.body.content || ''
    var extra  = req.body.extra || ''
    var rate_pro  = req.body.rate_pro || 5.0
    var rate_communicate  = req.body.extra || 5.0
    var rate_skill  = req.body.extra || 5.0
    if(oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    if(content === ''){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    var uid  = req.body.uid || 0
    if(uid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    // 检查用户是否参与订单
    db.orders.findOne({attributes:["workerid"],where:{'id':oid,'status':g.orderStatus.PAYED_OVER,'userid':uid,'valid':1}}).then(function(order){
        if(order){
            var startat = Math.floor(Date.now()/1000)
            db.comments.create({oid:oid,to_wid:order.workerid,rate_skill:rate_skill,
                rate_pro:rate_pro,rate_communicate:rate_communicate,content:content,extra:extra,date:startat}).then(function(){
                // 更新技师得分
                db.comments.findAll({
                    attributes: [[db.sequelize.fn('avg', db.sequelize.col('rate_pro')), 'pro'],
                    [db.sequelize.fn('avg', db.sequelize.col('rate_communicate')), 'communicate'],
                    [db.sequelize.fn('avg', db.sequelize.col('rate_skill')), 'skill']],group:['to_wid'],
                    where:{to_wid:order.workerid}
                }).then(function(data){
                    var pro = data[0].dataValues.pro
                    var communicate = data[0].dataValues.communicate
                    var skill = data[0].dataValues.skill
                    db.workers.update({rate_pro:pro,rate_communicate:communicate,rate_skill:skill},
                        {where:{id:order.workerid}}).then(function(){
                            // 通知内存更新评分
                            mem.r.pub.publish('memory-channel',"worker:"+order.workerid)
                        })
                    // mem.m.workers[order.workerid].rate_pro = pro
                    // mem.m.workers[order.workerid].rate_communicate = communicate
                    // mem.m.workers[order.workerid].rate_skill = skill
                    res.json({ok:data})
                })
            })
        }else{
            res.json({err:g.errorCode.WRONG_CANNOT_COMMENT})
            return
        }
    })
});

comm_router.route('/workerCommentUser').post(function(req,res){
    // 修改feedback
    var id  = req.body.id || 0
    var feedback  = req.body.feedback || ''
    if(id === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    if(feedback === ''){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.comments.update({feedback:feedback},{where:{id:id}}).then(function(data){
        res.json({ok:data})
    })
});

comm_router.route('/getComment').post(function(req,res){
    var oid  = req.body.oid || 0
    if(oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.comments.findAll({where:{oid:oid}}).then(function(){
        res.json({ok:1})
    })
});

module.exports=comm_router;