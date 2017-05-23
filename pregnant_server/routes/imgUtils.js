var multer = require('multer');
var mkdirp = require('mkdirp');
var fs = require('fs');
var g = require('../global')
var db = require('../models')
var mem = require('../memory')
var citys = require('../citys.json')
var utils = require('../utils')
var tpl = require('../template.json')
var cf = require('../controllers/commonController')
var storage = multer.diskStorage({
  destination: function (req, file, cb) {
    var destDir = './public/worker/';
     // 判断文件夹是否存在
    fs.stat(destDir, (err) => {
        if (err) {
            // 创建文件夹
            mkdirp(destDir, (err) => {
                if (err) {
                    cb(err);
                } else {
                    cb(null, destDir);
                }
            });
        } else {
            cb(null, destDir);
        }
    });
  },
  filename: function (req, file, cb) {
    var postfix = file.mimetype.substr(file.mimetype.indexOf('/')+1)
    cb(null, file.fieldname + '-' + req.body.wxid+'.'+postfix)
  }
})
var upload = multer({ storage: storage })

var cpUpload = upload.fields([{ name: 'avatar', maxCount: 1 }, 
        { name: 'nc', maxCount: 1 }, { name: 'ec', maxCount: 1 }])

var storage2 = multer.diskStorage({
  destination: function (req, file, cb) {
    var destDir = './public/exams/'+req.body.id+'/';
     // 判断文件夹是否存在
    fs.stat(destDir, (err) => {
        if (err) {
            // 创建文件夹
            mkdirp(destDir, (err) => {
                if (err) {
                    cb(err);
                } else {
                    cb(null, destDir);
                }
            });
        } else {
            cb(null, destDir);
        }
    });
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname)
  }
})
var upload2 = multer({ storage: storage2 })

var cpUpload2 = upload2.array('exams')

var storage3 = multer.diskStorage({
  destination: function (req, file, cb) {
    var destDir = './public/service/'+Date.now()+'/';
     // 判断文件夹是否存在
    fs.stat(destDir, (err) => {
        if (err) {
            // 创建文件夹
            mkdirp(destDir, (err) => {
                if (err) {
                    cb(err);
                } else {
                    cb(null, destDir);
                }
            });
        } else {
            cb(null, destDir);
        }
    });
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname)
  }
})
var upload3 = multer({ storage: storage3 })

var cpUpload3 = upload3.array('services')

function createServiceRecord(oid,type,wid,service_feedback,service_geo,service_geoaddress,res,req){
    db.orders.findOne({attributes:['userwxid'],where:{id:oid,valid:1,status:g.orderStatus.PAYED_STARTED}}).then(function(order){
        if(order){
            var startat = Math.floor(Date.now()/1000)
            var createObj = {service_type:type,service_time:startat,
                orderid:oid,service_worker_id:wid,service_feedback:service_feedback}
            if(service_geo !== undefined){
                var point = { type: 'Point', coordinates: [service_geo.x,service_geo.y]};
                createObj['service_geo']=point
            }
            createObj['service_geoaddress'] = service_geoaddress || '';
            var already = ''
            if(already.length!=0){
                already+='|'
            }
            for(var i = 0;i<req.files.length;i++){
                var name = req.files[i].path.substr(req.files[i].path.indexOf('public')+'public'.length+1)
                if(already.indexOf(name)===-1){
                    already +=name+'|'
                }
                // filenames+=name+'|'
            }
            already = already.substr(0,already.length-1)
            createObj['service_imgs'] = already
            db.service_records.create(createObj).then(function(data){
                // 通知用户确认
                var contentVar={}
                utils.sendDataToWxServer({wxid:order.userwxid,tid:tpl.template_inform_user_confirm_service,d:contentVar},
                    function(){})
                res.json({ok:1})
            })
        }else{
            res.json({err:g.errorCode.WRONG_ORDER_NOT_EXIST})
            return
        }
    })
}

function doInit(app){
    // 带图片提交维护信息
    app.post('/fillWorkerProfile', cpUpload, function (req, res, next) {
        // req.files is array of `` files
        // req.body will contain the text fields, if there were any
        var nowSec = Math.floor(Date.now()/1000)
        var id = parseInt(req.body.id) || 0
        var wxid = req.body.wxid || ''
        var name = req.body.name || ''
        var sex = parseInt(req.body.sex) || 1
        var hospital = req.body.hospital || ''
        var address = req.body.address || ''
        var address2 = req.body.address2 || ''
        var geoaddress = req.body.geoaddress || ''
        var geoaddress2 = req.body.geoaddress2 || ''
        var geox = req.body.geox
        var geox2 = req.body.geox2
        var geoy = req.body.geoy
        var geoy2 = req.body.geoy2
        var age = req.body.age || 18
        var keshi = req.body.keshi || ''
        var position = req.body.position || ''
        var tel = req.body.tel || ''
        var goodat = req.body.goodat || ''
        var code = req.body.code || ''
        var from = req.body.from || ''
        var expyears = req.body.expyears || ''
        var content = req.body.content || ''
        var id_number = req.body.id_number || ''
        var city = req.body.city || ''
        if(id === 0 || wxid === '' || code === '' ||
            name === '' || hospital === ''  || address === '' || keshi === '' ||
            position === '' || tel === '' || goodat === '' || id_number === ''){
            res.json({err:g.errorCode.WRONG_PARAM})
        }else{
            if(goodat.length > 64){
                res.json({err:g.errorCode.WRONG_PARAM})
                return
            }
            if(mem.m.phone_valids.workers[wxid] === undefined){
                res.json({err:g.errorCode.WRONG_VALID_CODE})
                return
            }
            if(mem.m.phone_valids.workers[wxid].code !== code){
                res.json({err:g.errorCode.WRONG_VALID_CODE})
                return
            }
            if(mem.m.phone_valids.workers[wxid].created + 600 < nowSec){
                delete mem.m.phone_valids.workers[wxid]
                res.json({err:g.errorCode.WRONG_VALID_CODE})
                return
            }



            var avatar_path = null;
            if(req.files.avatar) {
                avatar_path=req.files.avatar[0].path.substr(req.files.avatar[0].path.indexOf('public')+'public'.length+1)
            }
            var normal_cert_path = null;
            if(req.files.nc) {
                normal_cert_path=req.files.nc[0].path.substr(req.files.nc[0].path.indexOf('public')+'public'.length+1)
            }
            var expert_cert_path = null;
            if(req.files.ec) {
                expert_cert_path=req.files.ec[0].path.substr(req.files.ec[0].path.indexOf('public')+'public'.length+1)
            }

            var updateObj = { name: name,sex:sex,
                hospital:hospital,address:address,address2:address2,geoaddress2:geoaddress2,geoaddress:geoaddress,
                age:age,keshi:keshi,position:position,tel:tel,goodat:goodat,
                photourl:avatar_path,
                normal_cert:normal_cert_path,
                expert_cert:expert_cert_path,review_status:1,
                from:from,expyears:expyears,content:content,id_number:id_number}
            if(geoy && geox){
                var point = { type: 'Point', coordinates: [geox,geoy]};
                updateObj['geo'] = point
            }
            if(geoy2 && geox2){
                var point2 = { type: 'Point', coordinates: [geox2,geoy2]};
                updateObj['geo2'] = point2
            }


            if(city !== ''){
                for(var k in citys){
                    if(citys[k].indexOf(city)!=-1){
                        city = citys[k]
                        break
                    }
                }
                updateObj['city'] = city
            }

            

            var obj = {}    //把为空的过滤掉

            for(var key in updateObj) {
                if (updateObj[key] != undefined) {
                    obj[key] = updateObj[key];
                }
            }
            
            db.workers.update(obj,
                {where:{'id':id}}).then(function(){
                    // 通知内存更新
                    mem.r.pub.publish('memory-channel',"worker:"+id)
                    delete mem.m.phone_valids.workers[wxid]
                    res.json({w:1})
            })
        }
    })

    // 提交产检报告
    app.post('/staffSubmitExam',cpUpload2,function(req,res,next){
        var id = req.body.id || 0
        var care_extra = req.body.care_extra || ''
        var care_time = req.body.care_time || ''
        var after_feedback = req.body.after_feedback || ''
        var staff_name = req.body.staff_name || ''
        if(id === 0 || care_extra ==='' || care_time === 0 || after_feedback === '' || staff_name === ''){
            res.json({err:g.errorCode.WRONG_PARAM})
        }else{
            db.care_records.findOne({where:{id:id}}).then(function(record){
                if(record){
                    db.orders.findOne({where:{id:record.orderid}}).then(function(order){
                        if(order){
                            var already = record.care_images || ''
                            if(already.length!=0){
                                already+='|'
                            }
                            for(var i = 0;i<req.files.length;i++){
                                var name = req.files[i].path.substr(req.files[i].path.indexOf('public')+'public'.length+1)
                                if(already.indexOf(name)===-1){
                                    already +=name+'|'
                                }
                                // filenames+=name+'|'
                            }
                            already = already.substr(0,already.length-1)
                            var timestamp = 0
                            if(isNaN(care_time)){
                                var d = new Date(care_time)
                                var sec = Math.floor(d.getTime()/1000)
                                if(isNaN(sec)){
                                    res.json({err:g.errorCode.WRONG_VALID_CODE})
                                    return
                                }
                                timestamp = sec
                            }else{
                                timestamp = lastPeriod
                            }
                            var originStatus = order.status
                            db.care_records.update({status:g.care_status.CARE_FIN,care_time:timestamp,
                                care_extra:care_extra,care_images:already,staff_name:staff_name,after_feedback:after_feedback},{where:{id:id}}).then(function(){
                                // if(originStatus !== g.care_status.CARE_FIN){
                                //     cf.OnExamMake(order,record.care_cfg_id)
                                // }
                                res.json({ok:1})
                            })
                        }else{
                            res.json({err:g.errorCode.WRONG_CARE_RECORD_MISSING})
                        }
                    })
                }else{
                    res.json({err:g.errorCode.WRONG_CARE_RECORD_MISSING})
                }
            })
        }
    })

    // 创建某次服务签到
    app.post('/makeService',cpUpload3,function(req,res,next){
        var type = req.body.type || 0
        var oid = req.body.oid || 0
        var wid = req.body.wid || 0
        var service_geo = JSON.parse(req.body.service_geo)
        var service_geoaddress = req.body.service_geoaddress || ''
        var service_feedback = req.body.service_feedback || ''
        if(type === 0 || oid === 0 || wid === 0 ){
            res.json({err:g.errorCode.WRONG_PARAM})
            return
        }
        if(type < g.workerType.staff_worker&&
            type > g.workerType.staff_baby_nurse){
            res.json({err:g.errorCode.WRONG_PARAM})
            return
        }
        if(type !== g.workerType.staff_worker){
            // 其他类型只能创建1次签到
            db.service_records.findOne({where:{service_type:type,service_worker_id:wid,orderid:oid}}).then(function(record){
                if(record){
                    res.json({err:g.errorCode.WRONG_SERVICE_RECORD_ALREADY_MAKE})
                    return
                }else{
                    createServiceRecord(oid,type,wid,service_feedback,service_geo,service_geoaddress,res,req)
                }
            })
        }else{
            createServiceRecord(oid,type,wid,service_feedback,service_geo,service_geoaddress,res,req)
        }
    })
}

module.exports = doInit
