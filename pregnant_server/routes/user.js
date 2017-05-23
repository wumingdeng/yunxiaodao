var express=require('express');
var user_router=express.Router();
var db = require('../models')
var mem = require('../memory')
var g = require('../global')
var citys = require('../citys.json')

function getWeek(lastPeriod, now) {
    if (now) {
        now = now.getTime()
    } else {
        now =  new Date().getTime();
    }
    var week = now - lastPeriod.getTime();
    week = Math.floor(week/(7*24*3600*1000))
    return week;
}
function getStandardWeight() {
    return '50kg-60kg';
}

user_router.route('/quickloginwxUser').post(function(req,res){
    var wxid = req.body.wxid || ''
    if(wxid === ''){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        db.users.findOne({where:{'wxid':wxid}}).then(function(data){
            if(data){
                if(citys[data.dataValues['city']]!==undefined){
                    // data.dataValues['city'] = citys[data.dataValues['city']]
                }
                console.log('login success')
                res.json({ok:data.dataValues})
            }else{
                console.log('login fail')
                res.json({err:g.errorCode.WRONG_USER_MISSING})
            }
        })
    }
});

user_router.route('/getWeightInfo').post(function(req, res) {
    var wxid = req.body.wxid || ''
    if (wxid == '') {
        res.json({err:g.errorCode.WRONG_PARAM})
    } else {
        db.users.findOne({where:{'wxid':wxid}}).then(function(data){
            if (data) {
                db.weight_records.findOne({where:{'userid': wxid},order:'recordDate DESC'}).then(function(wdata){
                    var lastPeriod = data.dataValues.lastPeriod;
                    var currentWeek = getWeek(lastPeriod);  //取当前周数
                    var currentStandard = getStandardWeight();  //取当前标准体重
                    if (wdata) {
                        wdata.dataValues.currentWeek = currentWeek;
                        wdata.dataValues.currentStandard = currentStandard;
                        console.log('get weightRecord');
                        res.json({ok:wdata.dataValues})
                    } else {
                        var resData = {
                            currentWeek: currentWeek,
                            currentStandard: currentStandard
                        }
                        console.log('no weightRecord');
                        res.json({ok:resData})
                    }
                })               
            } else {
                //没数据
                res.json({ok:0})
            }
        })

    }
})

user_router.route('/fillWeight').post(function(req, res) {
    var wxid = req.body.wxid || ''
    var weight = req.body.weight || ''
    if (wxid == '' || weight == '' || typeof Number(weight) != 'number') {
        res.json({err:g.errorCode.WRONG_PARAM})
    } else {
        //取出最新的一条数据 如果是当天存的 就覆盖掉
        //根据算法 得出体重数据 存入体重数据表中
        var newRecord = {weight:weight,recordDate:new Date().toLocaleDateString()};
        db.weight_records.update(newRecord, {
            where:[
                {userid:wxid},
                db.sequelize.where(db.sequelize.fn('TO_DAYS', db.sequelize.col('recordDate')),'=',db.sequelize.fn('TO_DAYS',new Date().toLocaleString()))
            ]
        }).then(function(data){
            console.log(data);
            if (data[0] != 0) {
                console.log('更新体重数据')
                res.json({ok:newRecord})
            } else {
                //计算周数
                db.users.findOne({where:{'wxid':wxid}}).then(function(udata){
                    if (udata) {
                        var lastPeriod = udata.dataValues.lastPeriod;
                        var currentWeek = getWeek(lastPeriod);  //取当前周数
                        var currentStandard = getStandardWeight();  //取当前标准体重
                        newRecord.userid = wxid;
                        newRecord.week = currentWeek;
                        db.weight_records.create(newRecord).then(function() {
                            console.log('创建体重数据')
                            res.json({ok:newRecord})
                        }, function() {
                            console.log('不能创建数据。。')
                        })
                    } else {
                        res.json({ok:0})
                    }
                })
            }
        });
    }
})

//取用户体重图表的数据
user_router.route('/getWeightChart').post(function(req, res) {
    var wxid = req.body.wxid || ''
    if (wxid == '') {
        res.json({err:g.errorCode.WRONG_PARAM})
    } else {
        db.weight_records.findAll({
            where:{userid: wxid}
        }).then(function(records) {
            if (records) {
                //每周取一条体重最大的
                var result = [];
                var temp = {}
                for(var i = 0; i<records.length; i++){
                    var rec = records[i];
                    var nowWeight
                    if (temp[rec.week]) {
                        nowWeight = temp[rec.week].weight
                    } else {
                        nowWeight = 0;
                    }
                    if (rec.weight > nowWeight) {
                        temp[rec.week] = rec;
                    }
                }
                for (var item in temp) {
                    //只取体重和周数
                    var data = {
                        week:temp[item].week,
                        weight:temp[item].weight
                    }
                    if (data.week >= 0) {
                        result.push(data)
                    }
                }
                res.json({ok:result})
            } else {
                res.json({ok:0})
            }
        })
    }
})

//取用户体重记录
user_router.route('/getWeightData').post(function(req, res) {
    var wxid  = req.body.wxid || 0
    var offset = req.body.offset || 0
    var limit = req.body.limit || 0
    if(wxid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        db.weight_records.findAll({order:'recordDate DESC',offset:offset,limit:limit,where:{'userid':wxid, week:{$gte:0}}}).then(function(records){
            if (records) {
                res.json({ok:records})
            } else {
                res.json({ok:0})
            }
        })
    }
})

//保存用户资料
user_router.route('/updateInfo').post(function(req, res) {
    var wxid  = req.body.wxid || 0
    var height = req.body.height || 0
    var weight = req.body.weight || 0
    var lastPeriod = req.body.lastPeriod || 0
    var isSingle = req.body.isSingle || 0
    if(wxid === 0 || height == 0 || weight == 0 || lastPeriod == 0){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        //TODO 验证参数
        var newInfo = {
            height: height, 
            weight: weight, 
            lastPeriod: new Date(lastPeriod), 
            isSingle: isSingle
        };
        db.users.update(
            newInfo,
            {where:{wxid:wxid}}
        ).then(function(data){
            if (data) {
                //更新体重记录里的周数
                db.weight_records.findAll({where:{userid: wxid}}).then(function(records){
                    for(var i = 0;i<records.length;i++){
                        var record = records[i];
                        db.weight_records.update(
                            {week:getWeek(new Date(lastPeriod),record.recordDate)},
                            {where:{id: record.id}}
                        )
                    }
                })
                res.json({ok: newInfo});
            }
        })
    }
})

//获取用户最新的一次足部扫描报告
user_router.route('/getFootRecord').post(function(req, res) {
    var wxid  = req.body.wxid || 0
    if(wxid === 0){
        res.json({err:g.errorCode.WRONG_PARAM})
    } else {
        db.foot_records.findOne({
            where:{'userid': wxid},
            order:'recordDate DESC'
        }).then(function(record) {
            if (record) {
                res.json({ok:record})
            } else {
                res.json({ok: 0})
            }
        })
    }
})



module.exports=user_router;