var express=require('express');
var addon_router=express.Router();
var db = require('../models')
var mem = require('../memory')
var g = require('../global')
var cf = require('../controllers/commonController')
var utils = require('../utils')
var tpl = require('../template.json')

// 客服人工派单给三个服务人员
addon_router.route('/assignService').post(function(req,res){
    var type = req.body.type || 0
    var wid = req.body.wid || 0
    var oid = req.body.oid || 0
    if(type === 0 || wid === 0 || oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.staffs.findOne({where:{id:wid}}).then(function(data){
        // 通知这些人
        var contentVar={oid:oid}
        utils.sendDataToWxWorkerServer({wxid:data.wxid,tid:tpl.template_inform_staff_start_service,d:contentVar},
            function(){})
        res.json({ok:1})
    })
});

// 用户确认某次服务签到
addon_router.route('/confirmService').post(function(req,res){
    var id = req.body.id || 0
    var user_confirm = req.body.user_confirm || -1
    if(user_confirm === -1 || id === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
        return
    }
    db.service_records.findOne({where:{id:id}}).then(function(record){
        if(record){
            if(record.user_confirm != 0){
                res.json({err:g.errorCode.WRONG_SERVICE_RECORD_STATUS})
            }else{
                db.service_records.update({user_confirm:user_confirm},
                {where:{id:id}}).then(function(){
                    res.json({ok:1})
                })
            }
        }else{
            res.json({err:g.errorCode.WRONG_SERVICE_RECORD_NOT_EXIST})
        }
    })
});

// 获取签到记录
addon_router.route('/getServiceRecords').post(function(req,res){
    var offset = req.body.offset || 0
    var limit = req.body.limit || 0
    var oid = req.body.oid || 0
    if(limit === 0 || oid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        db.service_records.findAll({offset: offset, limit: limit,where:{orderid:oid}}).then(function(records){
            res.json({total:records.length,rows:records})
        })
    }
});

// 获取与我相关的签到记录
addon_router.route('/getMyServiceRecords').post(function(req,res){
    var offset = req.body.offset || 0
    var limit = req.body.limit || 0
    var wid = req.body.wid || 0
    var wtype = req.body.wtype || 0
    if(limit === 0 || wid === 0 || wtype === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        db.service_records.findAll({offset: offset, limit: limit,where:{service_worker_id:wid,service_type:wtype}}).then(function(records){
            res.json({total:records.length,rows:records})
        })
    }
});

// 获取签到反馈模版
addon_router.route('/getServiceTemplate').post(function(req,res){
    var type = req.body.type || 0
    if(type === 0 ){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        res.json(JSON.parse(mem.m.service_templates[type].names))
    }
});

// 用户确认产检时间
addon_router.route('/userCareConfirm').post(function(req,res){
    var id = req.body.id || 0 // 产检记录id
    var user_time = req.body.user_time || 0
    if(id === 0 || user_time === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        db.care_records.findOne({where:{id:id}}).then(function(record){
            if(record){
                var newStatus = g.care_status.CARE_INFORM_USER_CONFIRMED
                if(record.schedule_time < user_time){
                    newStatus = g.care_status.CARE_INFORM_USER_DELAYED
                }
                db.care_records.update({user_time:user_time,status:newStatus},{where:{id:id}}).then(function(){
                    // 通知产检人员用户时间
                    var d = new Date(user_time*1000)
                    var contentVar={user_time:d.toLocaleString().replace(/:\d{1,2}$/,' ')}
                    utils.sendDataToWxWorkerServer({wxid:record.staff_wxid,tid:tpl.template_inform_exam_worker,d:contentVar},
                        function(){})
                    res.json({ok:1})
                }).catch(function(err){
                    console.log(err)
                    res.json({err:g.errorCode.WRONG_CARE_RECORD_MISSING})
                })
            }else{
                res.json({err:g.errorCode.WRONG_CARE_RECORD_MISSING})
            }
        })
    }
});


function delRecordPicture(tablename,column,id,pic,res){
    db[tablename].findOne({attributes:[column],where:{id:id}}).then(function(data){
            var imgs = data[column].split('|')
            var img_arr = []
            var have = false
            for(var j = 0;j<imgs.length;j++){
                var hit = false
                for(var index = 0; index < pic.length; ++index) {
                    if (imgs[j] == pic[index]) {
                        hit = true;
                        have = true;
                        console.log('找到：' + pic[index])
                        break;
                    }
                }
                if(hit){
                    continue
                }
                img_arr.push(imgs[j])
            }
            console.log(img_arr);
            if(have){
                var left = ''
                if (img_arr.length > 0) {
                    for(var i = 0;i<img_arr.length;i++){
                        left+=img_arr[i]+'|'
                    }
                    left = left.substr(0,left.length-1)
                }
                console.log(tablename,column,left,id)
                var optObj = {}
                optObj[column] = left
                db[tablename].update(optObj,{where:{id:id}});
                // db[tablename].find({where:{id:id}});
            }
            res.json({ok:1})
        })
}

// 删除可用产检记录的图片
addon_router.route('/delExamsPic').post(function(req,res){
    var id = req.body.id || 0
    var pic = req.body.pic || ''    //数组
    if(id === 0 || pic === ''){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        delRecordPicture('care_records','care_images',id,pic,res)
    }
})

// 删除可用服务记录的图片
addon_router.route('/delServicePic').post(function(req,res){
    var id = req.body.id || 0
    var pic = req.body.pic || ''
    if(id === 0 || pic === ''){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        delRecordPicture('service_records','service_imgs',id,pic,res)
    }
})

// 获取可用产检记录
addon_router.route('/getExams').post(function(req,res){
    var id = req.body.id || 0
    if(id === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        db.care_records.findOne({where:{id:id}}).then(function(data){
            //取到服务人员的数据
            if(data.care_images !== null){
                data.care_images = data.care_images.split('|')
            }
            var care_cfg = mem.m.care_configs[data.care_cfg_id]
            data.dataValues.weeks = care_cfg.weeks;
            data.dataValues.content = care_cfg.content;
            data.dataValues.attention = care_cfg.attention;
            res.json({data:data})
        })
    }
})

// 获取可用产检记录
addon_router.route('/getExamsWithStatus').post(function(req,res){
    var offset = req.body.offset || 0
    var limit = req.body.limit || 0
    var status = req.body.status || []
    var user_wxid = req.body.user_wxid || ''
    var kind = req.body.kind || 0
    if(limit === 0 || user_wxid === '' || kind === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        var whereObj = {}
        var orderObj = []
        var query
        var statusQuery = ''
        if(kind === 1){
            // whereObj["user_wxid"] = user_wxid
            query = 'select * from care_records cr where o.id = cr.orderid and cr.user_wxid = ? '
        }else{
            // whereObj["staff_wxid"] = user_wxid
            query = 'select cr.*,o.username,o.tel from care_records cr left join orders o on o.userwxid = cr.user_wxid \
            where o.id = cr.orderid and cr.staff_wxid = ? '


            if(status.length < 2){
                query += 'and cr.status = ' + status[0];
            }else{
                query += 'and ('
                for(var i = 0;i<status.length;i++){
                    if (i == 0) {
                        query += ' cr.status = ' + status[i]
                    } else {
                        query += ' or cr.status = ' + status[i]
                    }
                }
                query += ")"
            }
        }
        query += ' order by cr.schedule_time limit ? offset ?'
        // if(status[0] === g.care_status.CARE_INFORM_USER_NO_CONFIRM){
        //     orderObj.push(['user_confirm_end','desc'])
        // }else if(status[0] === g.care_status.CARE_INFORM_USER_CONFIRMED || 
        //     status[0] === g.care_status.CARE_INFORM_USER_DELAYED){
        //     orderObj.push(['schedule_time','desc'])
        // }else if(status[0] === g.care_status.CARE_FIN){
        //     orderObj.push(['care_time','desc'])
        // }else{
        //     orderObj.push(['schedule_time','desc'])
        // }
        db.sequelize.query(query, { replacements: [user_wxid,limit,offset], 
            type: db.sequelize.QueryTypes.SELECT }).then(function(data){
                if(data){
                    for(var i = 0;i<data.length;i++){
                        if(data[i].care_images !== null){
                            data[i].care_images = data[i].care_images.split('|')
                        }
                        var care_cfg = mem.m.care_configs[data[i].care_cfg_id]
                        data[i].weeks = care_cfg.weeks;
                        data[i].content = care_cfg.content;
                        data[i].attention = care_cfg.attention;
                    }
                    res.json({ok:data})
                }else{
                    res.json({ok:[]})
                }
            })
       
        // db.care_records.findAll({offset:offset,limit:limit,where:whereObj,order:orderType}).then(function(data){
        //     if(data){
        //         for(var i = 0;i<data.length;i++){
        //             if(data[i].care_images !== null){
        //                 data[i].care_images = data[i].care_images.split('|')
        //             }
        //             var care_cfg = mem.m.care_configs[data[i].care_cfg_id]
        //             data[i].dataValues.weeks = care_cfg.weeks;
        //             data[i].dataValues.content = care_cfg.content;
        //             data[i].dataValues.attention = care_cfg.attention;
        //             if (kind != 1) {
        //                 //非用户需要取用户的资料
        //                 var userObj = {}
        //                 if (!userObj[data[i].user_wxid]) {
        //                     db.orders.findOne({where:{'userwxid':data[i].user_wxid}}).then(function(order){
        //                         data[i].dataValues.contact = order.username;
        //                         data[i].dataValues.tel = order.tel;
        //                     })
        //                 }
        //             }
        //         }
        //         res.json({ok:data})
        //     }else{
        //         res.json({ok:[]})
        //     }
        // })
    }
})

// 按区号获取医院
addon_router.route('/getHospitalList').post(function(req,res){
    var city = req.body.city || ''
    if(city === ''){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        var result = []
        for(var k in mem.m.hospitals){
            if(mem.m.hospitals[k].hospital_city.indexOf(city)!==-1){
                result.push(mem.m.hospitals[k])
            }
        }
        res.json({ok:result})
    }
})

module.exports=addon_router;