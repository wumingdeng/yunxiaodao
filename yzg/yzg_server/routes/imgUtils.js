var express = require('express');
var promotion_router = express.Router();
var db = require('../models')
var mem = require('../memory')
var g = require('../global')
var utils = require('../utils')
var cfg = require('../config.json')
var jwt = require('jsonwebtoken')
var moment = require('moment')
var template = require('../template.json')
var multer = require('multer');
var mkdirp = require('mkdirp');
var fs = require('fs');


var storage = multer.diskStorage({
  destination: function (req, file, cb) {
    var destDir = __dirname + '/../public/certificate/';
    console.log(destDir)
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

var cpUpload = upload.single('cert')

function doInit(app){
  //申请加入推广人
  app.post('/api/joinRequest', cpUpload, function (req, res, next) {
    var wxid = req.body.wxid;
    var upid = req.body.upid
    var name = req.body.name;
    var phone = req.body.phone;
    var job = req.body.job;
    var hospital = req.body.hospital;
    var department = req.body.department;
    if (!name || !phone || !job || !hospital || !department || !upid) {
      res.json({err: g.errorCode.WRONG_PARAM})
      return
    }

    var cert_path = null;
    if(req.file) {
        cert_path=req.file.path.substr(req.file.path.indexOf('public')+'public'.length+1)
        console.log(cert_path)
    }

    var info = {
      upid: upid,
      realName: name,
      phone: phone,
      job: job,
      hospital: hospital,
      department: department,
      requestDate: new Date(),
      certificate: cert_path
    }

    //先查瞎推荐人
    db.salemans.findOne({where:{userid: upid}}).then(function(upData) {
      //TODO 推荐人判断
      if (upData) {      
      }
      info.upName = upData.realName;
      db.users.findOne({where:{wxid: wxid}}).then(function(user) {
        if (user) {
          info.headUrl = user.headUrl
          info.nickName = user.name
          db.join_requests.findOne({where:{userid: wxid,status: 0}}).then(function(data){
              if (data) {
                db.join_requests.update(info, {where:{userid: wxid,status: 0}}).then(function(newData) {
                    res.json({ok:0});
                })
              } else {      
                info.userid = wxid
                db.join_requests.create(info).then(function(){
                  res.json({ok:0})
                })
              }
          })
        }
      })
    })
  })
}


module.exports = doInit
