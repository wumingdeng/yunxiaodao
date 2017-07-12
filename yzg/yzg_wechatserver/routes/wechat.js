var express=require('express');
var w_router=express.Router();
var wechat = require('wechat');
w_router.use(express.query());
var config = require('../config.json')
var app = express()
var mem = require('../memory')
var db = require('../models')
var utils = require('../utils')
var template = require('../template.json');
var moment = require('moment')
// w_router.use('/', wechat(config.token, function(req, res, next) {
// 	var message = req.weixin;
// 	//文本
// 	if (message.Content === '1') {
// 		res.reply('hehe'+Date.now());
// 	}
// }));

function sendQRcodeTemplateToUser(wxid,user,name) {
  // templateid
  var tid = template.template_qrcode_used;
  // data formate: "d":{"first":{"value":"DAMAO"},"service":{"value":"ni h你好s j"}}
  // first ,service is setting as {{first.DATA}} in template,alse can use "color"
  var data = {
      first:{
          value:'有人扫了你贴在电线杆上的二维码！',
          color:"#000000"
      },
      keyword1:{
          value: user
      },
      keyword2:{
          value: name
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
          value:''
      }
  }
  utils.wechat_f.sendToUser(wxid,tid,data)
}

//创建二维码变更记录
function recordQRCodeChange(userid, newSid, newMaster, oldSid, oldMaster) {
  db.qrcodechange_records.create({
    userid: userid,
    time: new Date(),
    newSid: newSid,
    oldSid: oldSid,
    newMaster: newMaster,
    oldMaster: oldMaster
  })
}

//用户扫描二维码
function userUseQRCode(userid, sid, cb) {
  db.users.findOne({where:{wxid:userid}}).then(function(data) {
    if (data) {
      //如果账号已创建 直接写入上线
      db.qrcodes.findOne({where:{id: sid}}).then(function(qrcodeData){
        db.users.update({qrcode:sid, qrcodeOwner: qrcodeData.userid},{where:{id:data.id}})
        recordQRCodeChange(userid, qrcodeData.id, qrcodeData.userid, data.qrcode, data.qrcodeOwner)
        sendQRcodeTemplateToUser(qrcodeData.userid,data.wxid,data.name)
        //显示扫码的人
        db.users.findOne({where:{wxid: qrcodeData.userid}}).then(function(owner) {
          if (owner) {
            cb(owner.name);
          } else {
            cb()
          }
        })
      })
    } else {
      utils.wechat_f.getUserBrief(userid,function(data) {
        if (data) {
          // console.log('创建用户')
          db.users.create({wxid:userid || '',headUrl:data.headimgurl || '',name:data.nickname || ''}).then(function(newUser){
            console.log('扫码创建用户成功')
            //写入上线
            db.qrcodes.findOne({where:{id: sid}}).then(function(qrcodeData){
              db.users.update({qrcode:sid, qrcodeOwner: qrcodeData.userid},{where:{id:newUser.id}})
              recordQRCodeChange(userid, qrcodeData.id, qrcodeData.userid)
              sendQRcodeTemplateToUser(qrcodeData.userid,newUser.wxid,newUser.name)
              //显示扫码的人
              db.users.findOne({where:{wxid: qrcodeData.userid}}).then(function(owner) {
                if (owner) {
                  cb(owner.name);
                } else {
                  cb()
                }
              })
            })
          })
        } else {
          console.log('扫描创建账号失败。。。')
        }
      })
    }
  })
}

function testGetQRCode(userid, cb) {
    function onSuccess(resData) {
        // if (resData.ticket) {
        //     res.redirect(301,'https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=' + resData.ticket);
        // }
        //存下ticket
        db.qrcodes.update({ticket:resData.ticket},{where:{userid:userid}})
        cb(resData.ticket)
    }

    function onError() {
        return null;
    }

    //查找用户是否有二维码了
    db.qrcodes.findOne({where:{userid:userid}}).then(function(data) {
        if (data) {
            if (data.ticket) {
              cb(data.ticket)
            } else {
                utils.wechat_f.getRQCodeTicket(data.id,onSuccess,onError)
            }
        } else {
            db.qrcodes.create({userid:userid}).then(function(newData) {
                utils.wechat_f.getRQCodeTicket(newData.id,onSuccess,onError)
                // res.json({ok:newData.id})
            })
        }
    })
}


w_router.use('/', wechat(config.token).text(function (message, req, res, next) {
  // 微信输入信息都在message上
  if (message.Content == '推广') {
    var userid = message.FromUserName
    testGetQRCode(userid, function(ticket) {
      res.reply([
        {
          title: '你要的二维码 拿去！',
          description: '打开查看二维码',
          picurl: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495434160&di=011945b146f93281dbd690a59fbcca36&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.cnxmld.com%2Ftupians%2Fbd14908247.jpg',
          url: "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket
        }
      ])
    })
    return;
  }
  res.reply('欢迎来到孕足管！');
}).location(function (message, req, res, next) {
  res.reply('location');
}).image(function (message, req, res, next) {
  res.reply('image');
}).voice(function (message, req, res, next) {
  res.reply('voice');
}).link(function (message, req, res, next) {
  res.reply('link');
}).event(function (message, req, res, next) { 
  if (message.Event == "CLICK") {
  	// res.reply([
   //    {
   //      title: '足部健康报告',
   //      description: '请点击查看您的足部健康报告',
   //      picurl: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1495434160&di=011945b146f93281dbd690a59fbcca36&imgtype=jpg&er=1&src=http%3A%2F%2Fwww.cnxmld.com%2Ftupians%2Fbd14908247.jpg',
   //      url: config.webAddress + '/?wxid=' + message.FromUserName + '&type=1&page=foot'
   //    }
  	// ])
  }else if(message.Event === 'subscribe'){
    for (v in message) {
      console.log("key=" + v + '  value=' + message[v])
    }
     mem.r.client.hmset(message.Ticket, "open_id", message.FromUserName);

     //记录赤裸裸的利益关系
     var user = message.FromUserName
     var sid = message.EventKey.substring(8); //有个"qrscene_"前缀
     userUseQRCode(user,sid, function(master) {
        res.reply('您在' + master + '的带领下加入了孕足管!');
     });
  }else if(message.Event === 'SCAN'){
     for (v in message) {
      console.log("key=" + v + '  value=' + message[v])
     }
    //  res.reply('欢迎扫描测试号！'+message.EventKey+' '+message.FromUserName);
     mem.r.client.hmset(message.Ticket, "open_id", message.FromUserName);

     //记录赤裸裸的利益关系
     var user = message.FromUserName
     var sid = message.EventKey;
     // userUseQRCode(user,sid);
     // res.reply('SCAN');
     userUseQRCode(user,sid, function(master) {
        if (master)
          res.reply('您在' + master + '的带领下加入了孕足管!');
        else {
          res.reply('欢迎来到孕足管！')
        }
     });
  }
}).middlewarify());


module.exports = w_router;