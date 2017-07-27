//推广功能
var express = require('express');
var promotion_router = express.Router();
var db = require('../models')
var mem = require('../memory')
var g = require('../global')
var utils = require('../utils')
var cfg = require('../config.json')
var jwt = require('jsonwebtoken')


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
	        	}
	        	res.json({ok:0})
	        })
	    }
	  });
	})
})

module.exports=promotion_router; 
