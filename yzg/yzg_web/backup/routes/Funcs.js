var express=require('express');
var tour_router=express.Router();
var db = require('../models')
var mem = require('../memory')
var g = require('../global')
var utils = require('../utils')
var tpl = require('../template.json')
// for quick test
tour_router.route('/build').get(function(req,res){
    db.configs.findAll({
        // attributes: ['cat_id'],
        where:{id:0}
    }).then(function(data){
        // mem.r.pub.publish('memory-channel',"666")
        res.json({d:data});
    })
});

tour_router.route('/test').get(function(req,res){
    db.care_records.findAll({where:{$or:[{status:0},{status:1}]}}).then(function(records){
        // 检查每条是否到了时间
        for(var i = 0;i<records.length;i++){
            var now = Math.floor(Date.now()/1000)
            var d2 = new Date(now*1000)
            var d3 = new Date(d2.toLocaleDateString())
            var nowsec = Math.floor(d3.getTime()/1000)
            if(records[i].status === g.care_status.CARE_COUNTDOWN){
                // 通知阶段
                if(now >= records[i].first_inform_end){
                    // 进入下一阶段
                    // days seconds
                    var next = nowsec + mem.m.configs[0].informUserExamExpire * 86000
                    // scope and async trap for loop here
                    var idx = i
                    db.care_records.update({status:g.care_status.CARE_INFORM_USER_NO_CONFIRM,user_confirm_end:next},{where:{id:records[i].id}}).then(function(){
                        // 通知用户
                        var contentVar={}
                        utils.sendDataToWxServer({wxid:records[idx].user_wxid,tid:tpl.template_inform_user_confirm_exam,d:contentVar},
                            function(){})
                    })
                }
            }else if(records[i].status === g.care_status.CARE_INFORM_USER_NO_CONFIRM){
                // 用户确认阶段
                if(now >= records[i].user_confirm_end){
                    // 进入下一阶段
                    var idx = i
                    db.care_records.update({status:g.care_status.CARE_USER_NO_REPLY},{where:{id:records[i].id}}).then(function(){
                        // 通知陪诊人员联系用户
                        var contentVar={}
                        utils.sendDataToWxWorkerServer({wxid:records[idx].user_wxid,tid:tpl.template_inform_exam_worker_connect,d:contentVar},
                            function(){})
                    })
                }
            }
        }
        res.json({ok:1})
    })
    
});

module.exports=tour_router;