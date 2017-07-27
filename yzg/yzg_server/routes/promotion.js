//推广功能
var express = require('express');
var promotion_router = express.Router();
var db = require('../models')
var mem = require('../memory')
var g = require('../global')
var utils = require('../utils')
var cfg = require('../config.json')
var jwt = require('jsonwebtoken')
var moment = require('moment')



//访问分享页 绑定关系
promotion_router.route('/tglink').post(function(req,res){
    var wxid = req.decoded.wxid
    var boss = req.body.bossid
    if (!wxid || !boss) {
        res.json({error:g.errorCode.WRONG_PARAM})
        return
    }
    db.users.findOne({where:{wxid:wxid}}).then(function(data) {
        if (data) {
            //如果账号已创建 直接写入上线
            db.users.update({qrcode:0, qrcodeOwner: boss},{where:{id: data.id}})
            //绑定关系变更
            if (data.qrcodeOwner != boss || data.qrcode != 0) {
                db.qrcodechange_records.create({
                    userid: wxid,
                    time: new Date(),
                    newSid: 0,
                    oldSid: data.qrcode,
                    newMaster: boss,
                    oldMaster: data.qrcodeOwner,
                    linkShare: 1
                })
            }
        }
    })
    res.json({ok:0})
})

//取发展推广的二维码
promotion_router.route('/getBossQrcode').post(function(req,res){ 
	var wxid = req.decoded.wxid;
  db.users.findOne({where:{wxid:wxid}}).then(function(user) {
  	if (user) {
  		if (user.isBoss) {
  			//取得唯一标识
  			jwt.sign({ wxid: wxid}, cfg.secret, { expiresIn: 1800},function(err, token) { 
            //返回标识
            mem.r.pub.set(token,true);
            mem.r.pub.expire(token, 1800)
            res.json({ok:token})
        });
  		} else {
  			res.json({err:g.errorCode.WRONG_NO_RIGHT,errmsg:'没有权限'})
  		}
  	} else {
  		res.json({err:g.errorCode.WRONG_USER_MISSING})
  	}
  })
})

promotion_router.route('/useBossQrcode').post(function(req, res){
	var wxid = req.decoded.wxid;
	var qrcode = req.body.qrcode;
	mem.r.pub.get(qrcode,function(err, reply) {
		console.log(err)
		if (!reply) {
			//过期或者使用过了
			res.json({err:g.errorCode.WRONG_INVALID_QRCODE});
			return
		}
		jwt.verify(qrcode, cfg.secret, function(err, decoded) {      
	    if (err) {
	        res.json({ err: 99, success: false, message: 'Failed to authenticate token.' }); 
	    } else {
	        // if everything is good, save to request for use in other routes
	        mem.r.pub.del(qrcode);
	        var bossid = decoded.wxid;

	        //bandle user
	        db.users.findOne({where:{wxid: wxid}}).then(function(user) {
	        	if (user) {
	        		db.users.update({bossid: bossid, isSaleman: 1},{where:{id:user.id}})
	        		//插入到推广人员表
              db.salemans.update({headUrl:user.headUrl,nickName:user.name, upid: bossid,joinDate: new Date()},
                {where:{userid:wxid}}).then(function(newData) {
                  if (newData[0] != 0) {
                    res.json({ok:0,msg:'修改推广人员数据'})
                  } else {
                    db.salemans.create({userid:wxid,headUrl:user.headUrl,nickName:user.name, upid: bossid,joinDate: new Date()});
                    res.json({ok:0,msg:'创建推广人员数据'})
                  }
                });
	        	} else {
              res.json({ok:0})
            }
	        })
	    }
	  });
	})
})

//工作人员去所属推广人员
promotion_router.route('/getSalemen').post(function(req, res){
  var wxid = req.decoded.wxid;
  db.salemans.findAll({where:{upid: wxid}, order: 'joinDate DESC'}).then(function(data) {
    if (data) {
      for (i in data) {
        var item = data[i]
        item.dataValues.joinDate = moment(item.dataValues.joinDate).format('YYYY-MM-DD hh:mm:ss')
      }
      res.json({ok: data})
    } else {
      res.json({ok:[]})
    }
  })
})

//医护人员填写信息
promotion_router.route('/tgFillInfo').post(function(req, res){
  var wxid = req.decoded.wxid;
  var name = req.body.name;
  var phone = req.body.phone;
  var job = req.body.job;
  var hospital = req.body.hospital;
  var department = req.body.department;

  if (!name || !phone || !job || !hospital || !department) {
    res.json({err: g.errorCode.WRONG_PARAM})
    return
  }

  var info = {
    realName: name,
    phone: phone,
    job: job,
    hospital: hospital,
    department: department
  }
  db.salemans.update(info,{where:{userid: wxid}}).then(function(data) {
    
      res.json({ok:info})
    // if (data[0] != 0) {
    //   res.json({ok:info})
    // } else {
    //   res.json({err:0,msg:"没有推广人数据"})
    // }
  })
})

module.exports=promotion_router; 
