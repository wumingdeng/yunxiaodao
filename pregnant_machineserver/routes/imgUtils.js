var multer = require('multer');
var mkdirp = require('mkdirp');
var fs = require('fs');
var g = require('../global')
var db = require('../models')
var mem = require('../memory')
var utils = require('../utils')
var cfg = require('../config.json')

function getDateStr(cc){
    var m = cc.getMonth()+1
    var d = cc.getDate()
    var y = cc.getFullYear()
    if(m<10){
        m='0'+m
    }
    if(d<10){
        d='0'+d
    }
    return y+m+d
}

var storage3 = multer.diskStorage({
  destination: function (req, file, cb) {
    var ym = getDateStr(new Date())
    var destDir = './uploads/'+ym+'/';
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

var cpUpload3 = upload3.fields([{ name: 'file1', maxCount: 1 }, 
        { name: 'file2', maxCount: 1 }])

function doInit(app){
    // 创建某次服务签到
    app.post('/serverftp4',cpUpload3,function(req,res,next){
        if(req.body.sign === undefined){
            res.json({"errcode":1,"errmsg":"非法访问！"})
        }else{
            if(req.body.sign!=='liuhe'){
                res.json({"errcode":1,"errmsg":"非法访问！"})
            }else{
                if(req.body.oper!==undefined){
                     if(req.body.oper==='upload'){
                        var ym = getDateStr(new Date())
                        var p = cfg.serverAdress+':'+cfg.listen+'/upload/'+ym+'/'
                        res.json({"errcode":0,"errmsg":p})
                     }else{
                        res.json({"errcode":2,"errmsg":"成功访问图片上传接口"})
                     }
                }else{
                    res.json({"errcode":2,"errmsg":"成功访问图片上传接口"})
                }
            }
        }
    })
}

module.exports = doInit
