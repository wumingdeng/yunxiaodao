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
var template = require('../template.json')


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
            db.users.update({bossid: boss},{where:{id: data.id}})
            //绑定关系变更
            if (data.bossid != boss) {
                db.qrcodechange_records.create({
                    userid: wxid,
                    time: new Date(),
                    newMaster: boss,
                    oldMaster: data.bossid
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

          db.salemans.findOne({where:{userid: wxid}}).then(function(sdata) {
            //如果已经有上线了 不改变关系
            if (sdata && sdata.upid) {
              res.json({err:99,msg:"已经存在推广关系"})
            } else {
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

//取单个推广人
promotion_router.route('/getSalemanData').post(function(req, res){
  var id = req.body.id;
  db.salemans.findOne({where:{userid:id}}).then(function(data) {
    if (data) {
      res.json({ok:data})
    } else {
      res.json({err:0,msg:'没有推广人数据'})
    }
  })
})


//医护人员填写信息
promotion_router.route('/tgFillInfo').post(function(req, res){
  var wxid = req.body.wxid;
  var name = req.body.name;
  var phone = req.body.phone;
  var job = req.body.job;
  var hospital = req.body.hospital;
  var department = req.body.department;


    console.log(req.body)
  for (key in req.body) {
    console.log(key + ':' + req[key])
  }

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



//审核申请 取申请数据
promotion_router.route('/getRequestList').post(function(req, res){
    var wxid = req.decoded.wxid || ''
    var offset = req.body.offset || 0
    var limit = req.body.limit || 0
    db.join_requests.findAll({ order:  [
        ['status'],
        ['requestDate']
    ], offset: offset, limit: limit}).then(function (records) {
        if (records) {
            for (i in records) {
              var item = records[i];
              item.dataValues.requestDate = moment(item.dataValues.requestDate).format('YYYY-MM-DD hh:mm:ss')
            }
            res.json({ ok: records })
        } else {
            res.json({ ok: 0 })
        }
    })
})

//同意推广申请
promotion_router.route('/acceptRequest').post(function(req, res){
    var wxid = req.decoded.wxid || ''
    var id = req.body.id;
    if (!wxid  || !id) {
      res.json({err:g.errorCode.WRONG_PARAM})
      return
    }

    db.join_requests.findOne({where:{id: id}}).then(function(joinData) {
      if (joinData) {
        // change status
        db.join_requests.update({status: 1, approver: wxid},{where:{id: id}}).then(function(changeData){
          if (changeData[0] != 0) {
            //bandle user
            db.users.findOne({where:{wxid: joinData.userid}}).then(function(user) {
              if (user) {
                db.users.update({bossid: joinData.upid, isSaleman: 1},{where:{id:user.id}})
                //插入到推广人员表
                db.salemans.update({realName:joinData.realName,phone:joinData.phone,job:joinData.job,
                  hospital:joinData.hospital,department:joinData.department,certificate:joinData.certificate,
                  headUrl:user.headUrl,nickName:user.name, upid: joinData.upid,joinDate: new Date()},
                  {where:{userid:joinData.userid}}).then(function(newData) {
                    if (newData[0] != 0) {
                      res.json({ok:0,msg:'修改推广人员数据'})
                    } else {
                      db.salemans.create({realName:joinData.realName,phone:joinData.phone,job:joinData.job,
                      hospital:joinData.hospital,department:joinData.department,certificate:joinData.certificate,
                      userid:joinData.userid,headUrl:user.headUrl,nickName:user.name, upid: joinData.upid,joinDate: new Date()});
                      res.json({ok:0,msg:'创建推广人员数据'})
                    }
                    var tdata = {
                      first:{
                          value:'感谢您为蜜鹊代言！',
                          color:"#000000"
                      },
                      keyword1:{
                          value: '*'
                      },
                      keyword2:{
                          value: '*'
                      },
                      keyword3:{
                          value:'*'
                      },
                      keyword4:{
                          value:'*'
                      },
                      keyword5:{
                          value:moment().format("YYYY-MM-DD hh:mm:ss")
                      },
                      remark:{
                          value:'您的申请已审核通过，点击进入代言推广中心'
                      }
                    }
                    console.log('跳转到:' + cfg.webAddress + "/?page=userHome")
                    //发送模版消息
                    utils.sendDataToWxServer({
                      wxid:joinData.userid,
                      tid:template.template_qrcode_used,
                      d: tdata,
                      toUrl: cfg.webAddress + "/?page=userHome"
                    },function(err) {

                    })
                  });
              } else {
                res.json({ok:0})
              }
            })
          } else {
            res.json({err:99,msg:'accept this request already'})
          }
        })
      } else {
        res.json({err:99,msg:"don't find this join_requests record"})
      }
    })
})

//拒绝申请
promotion_router.route('/refuseRequest').post(function(req, res){
    var wxid = req.decoded.wxid || ''
    var id = req.body.id;
    var advice = req.body.advice || ''
    if (!wxid  || !id) {
      res.json({err:g.errorCode.WRONG_PARAM})
      return
    }
    db.join_requests.findOne({where:{id: id}}).then(function(data) {
      if (data) {
        db.join_requests.update({status: 2,advice: advice,approver: wxid},{where:{id: id}}).then(function(joinData) {
          res.json({ok:0})
          var tdata = {
            first:{
                value:'您的推广申请未通过。',
                color:"#000000"
            },
            keyword1:{
                value: '*'
            },
            keyword2:{
                value: '*'
            },
            keyword3:{
                value:'*'
            },
            keyword4:{
                value:'*'
            },
            keyword5:{
                value:moment().format("YYYY-MM-DD hh:mm:ss")
            },
            remark:{
                value: advice
            }
          }
          //发送模版消息
          utils.sendDataToWxServer({
            wxid:data.userid,
            tid:template.template_qrcode_used,
            d: tdata
          })
        })
      } else {
        res.json({err:99,msg:"don't find this join_requests record"})
      }
    })
})


module.exports=promotion_router; 
