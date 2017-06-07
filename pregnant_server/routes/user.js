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
    week = Math.floor(week/(7*24*3600*1000)) + 1
    return week;
}

//取标准体重
function getStandardWeight(week, weight, shape, isSingle) {
    weight = Number(weight);
    var early = g.earlyStage
    if (week <= early) {
        return {
            value: weight + 'kg-' + (weight + g.earlyAdd) + 'kg',
            min: weight,
            max: weight + g.earlyAdd
        }
    }
    week -= early  //减去孕早期
    var config = mem.m.weightRate_configs[shape];
    var minRate, maxRate
    if (isSingle == 1) {
        minRate = config.rateMin
        maxRate = config.rateMax
    } else {
        minRate = config.dRateMin
        maxRate = config.dRateMax
    }
    var minWeight = weight + minRate * week;
    var maxWeight = weight + g.earlyAdd + maxRate * week;
    return  {
        value: minWeight + 'kg-' + maxWeight+ 'kg',
        min: minWeight,
        max: maxWeight
    }
}

//计算体型
function getShape(weight, height) {
    var config = g.weightStandard;
    var result = weight / (height*height/10000);
    console.log(result);
    for (var i = 0; i < config.length; ++i) {
        if (result >= config[i].min && result < config[i].max) {
            return config[i].value;
        }
    }
    return -1
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
                    var currentStandard = getStandardWeight(currentWeek, data.dataValues.weight ,data.dataValues.shape, data.dataValues.isSingle).value;  //取当前标准体重
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
    var hospital_no = req.body.hospital_no || ''
    if (wxid == '' || weight == '' || typeof Number(weight) != 'number') {
        res.json({err:g.errorCode.WRONG_PARAM})
    } else {
        //取出最新的一条数据 如果是当天存的 就覆盖掉
        var newRecord = {hospital:hospital_no,weight:weight,recordDate:new Date()};
        db.users.findOne({where:{'wxid':wxid}}).then(function(udata){
            //计算周数
            if (udata) {
                //根据算法 得出体重数据 存入体重数据表中
                //计算标准体重
                var lastPeriod = udata.dataValues.lastPeriod;
                var currentWeek = getWeek(lastPeriod);  //取当前周数
                newRecord.userid = wxid;
                newRecord.week = currentWeek;
                var standard = getStandardWeight(currentWeek, udata.dataValues.weight ,udata.dataValues.shape, udata.dataValues.isSingle);
                newRecord.standard = standard.value
                var result  
                if (weight < standard.min) {
                    result = g.weightStatus.skinny;
                } else if (weight > standard.max) {
                    result = g.weightStatus.fat
                } else {
                    result = g.weightStatus.normal
                }
                newRecord.result = result
                //取对应提示
                var tipInfo;
                var tip = mem.m.weightAdvice_configs
                for(var i = 0 in tip) {
                    if (currentWeek >= tip[i].minWeek && currentWeek <= tip[i].maxWeek) {
                        if (result ==g.weightStatus.skinny) {
                            tipInfo = tip[i].skinny
                        } else if (result == g.weightStatus.fat) {
                            tipInfo = tip[i].fat
                        } else {
                            tipInfo = tip[i].normal
                        }
                        break
                    }
                }
                newRecord.tip = tipInfo

                console.log(newRecord)
                db.weight_records.update(newRecord, {
                    where:[
                        {userid:wxid},
                        db.sequelize.where(db.sequelize.fn('TO_DAYS', db.sequelize.col('recordDate')),'=',db.sequelize.fn('TO_DAYS',new Date()))
                    ]
                }).then(function(data){
                    console.log(data);
                    if (data[0] != 0) {
                        console.log('更新体重数据')
                        newRecord.recordDate = newRecord.recordDate.toLocaleDateString()
                        res.json({ok:newRecord})
                    } else {
                        db.weight_records.create(newRecord).then(function() {
                            console.log('创建体重数据')
                            res.json({ok:newRecord})
                        }, function(err) {
                            console.log(err)
                            console.log('不能创建数据。。')
                            res.json({ok:0})
                        })
                    }
                });
            } else {
                res.json({ok:0})
            }
        })
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
        //计算体型
        var shape = getShape(weight,height);
        var newInfo = {
            height: height, 
            weight: weight, 
            shape: shape,
            lastPeriod: new Date(lastPeriod), 
            isSingle: isSingle
        };
        db.users.update(
            newInfo,
            {where:{wxid:wxid}}
        ).then(function(data){
            if (data) {
                //更新体重记录
                db.weight_records.findAll({where:{userid: wxid}}).then(function(records){
                    for(var i = 0;i < records.length; i++){
                        var record = records[i];
                        var newRecord = {}
                        var currentWeek = getWeek(new Date(lastPeriod),record.recordDate)
                        newRecord.week = currentWeek
                        var standard = getStandardWeight(currentWeek, weight , shape, isSingle);
                        newRecord.standard = standard.value
                        var result
                        if (record.weight < standard.min) {
                            result = g.weightStatus.skinny;
                        } else if (record.weight > standard.max) {
                            result = g.weightStatus.fat
                        } else {
                            result = g.weightStatus.normal
                        }
                        newRecord.result = result
                        //取对应提示
                        var tipInfo;
                        var tip = mem.m.weightAdvice_configs
                        for(var j = 0 in tip) {
                            if (currentWeek >= tip[j].minWeek && currentWeek <= tip[j].maxWeek) {
                                if (result ==g.weightStatus.skinny) {
                                    tipInfo = tip[j].skinny
                                } else if (result == g.weightStatus.fat) {
                                    tipInfo = tip[j].fat
                                } else {
                                    tipInfo = tip[j].normal
                                }
                                break
                            }
                        }
                        newRecord.tip = tipInfo
                        db.weight_records.update(
                            newRecord,
                            {where:{id: record.id}}
                        ).then(function(){
                            console.log('更新记录')
                        },function(err){
                            console.log(err)
                        })
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