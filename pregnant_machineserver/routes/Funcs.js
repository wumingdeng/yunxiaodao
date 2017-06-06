var express=require('express');
var tour_router=express.Router();
var db = require('../models')
var g = require('../global')
var utils = require('../utils')
var request=require('request')
var cfg = require('../config.json')
var mem = require('../memory')
// for quick test
tour_router.route('/test').get(function(req,res){
    var ccc = { mac_id: '0A002700001720170601153508',
  open_id: 'orzDvv0yDNiYzvwT0v7nyUB8Ek3I',
  card_id: '33392023920',
  ticket: 'gQEo8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyX0NpUTlyTE1iTF8xMExsTk5wMTIAAgSvwy9ZAwSAUQEA',
  scene: '33602855',
  from_id: '0A0027000017',
  from_app: 'yxd',
  date_server: 1496302537349,
  date_host: '2017-06-01 15:35:35',
  name: '大名',
  sex: '女',
  birth: '1990-03-20T00:00:00.000Z',
  age: '27',
  district: 'null',
  province: 'null',
  city: 'null',
  county: 'null',
  country: 'null',
  nation: '俄羅斯',
  height: '175.0',
  weight: '70.5',
  date_yunfu: '2017-03-01T00:00:00.000Z',
  hospital_no: 'J180',
  hospital_name: '解放军第180医院',
  clinic_dept: 'null',
  doctor_name: 'null',
  clinic_type: 'null',
  user_id: '14',
  period: '13' }
  ccc.birth=new Date(ccc.birth).toLocaleDateString()
  var now = new Date()
                            ccc.date_server = now.toLocaleDateString()+' '+now.toLocaleTimeString()
                            ccc.date_yunfu=new Date(ccc.date_yunfu).toLocaleDateString()
    db.yxd_basicinfos.create(ccc).then(function(dta){
        res.json({ok:dta})
    }).catch(function(err){
        res.json({"errcode":1,"errmsg":"添加脚型数据basicinfo失败"+err})
    })
});

tour_router.route('/scanverify').post(function(req,res){
    if(req.body.mac_id===undefined){
        res.json({"errcode":3,"errmsg":"插入验证数据错误！"})
    }else{
        if(req.body.ticket !== undefined){
            mem.r.client.hmset(req.body.ticket, "scene", req.body.scene, "mac", req.body.mac_id);
            mem.r.client.expire(req.body.ticket, 25200);
            res.json({"errcode":0,"errmsg":"ok"})
        }else{
            mem.r.client.hgetall(req.body.mac_id, function (err, obj) {
                if(err){
                    res.json({"errcode":3,"errmsg":"查询验证数据失败！"})
                }else{
                    if(obj.open_id===undefined){
                        res.json({"errcode":3,"errmsg":"查询验证数据失败！"})
                    }else{
                        res.json({"errcode":0,"errmsg":"ok","open_id":obj.open_id})
                    }
                }
            });
        }
    }
})

tour_router.route('/userinfo').post(function(req,res){
    if(req.body.sign === undefined){
        res.json({"errcode":1,"errmsg":"非法访问！"})
    }else{
        if(req.body.sign!=='liuhe'){
             res.json({"errcode":1,"errmsg":"非法访问！"})
        }else{
            if(req.body.idType!==undefined && req.body.idValue!==undefined){
                db.users.findOne({where:{wxid:req.body.idValue}}).then(function(data){
                    if(data){
                        var result={id:data.id,open_id:data.wxid,card_id:data.card_id,name:data.name,nation:data.nation,
                            district:data.district,province:data.province,city:data.city,
                            county:data.county,height:data.height,weight:data.weight,
                            "errcode":0,"errmsg":"ok"
                        }
                        if(data.gender===1){
                            result.sex='男'
                        }else{
                            result.sex='女'
                        }
                        result.date_birth = data.date_birth || ''
                        result.date_yunfu = data.lastPeriod || ''
                        var query=`select yxd_basicinfos.id,yxd_parameters.left_length,yxd_parameters.right_length,yxd_parameters.left_width,yxd_parameters.right_width 
                        from yxd_basicinfos,yxd_parameters where yxd_parameters.id = yxd_basicinfos.id and yxd_basicinfos.open_id=? 
                        order by yxd_basicinfos.id desc limit 1;`
                        db.sequelize.query(query, { replacements: [req.body.idValue], 
                            type: db.sequelize.QueryTypes.SELECT }
                        ).then(function(records){
                           if(records.length>0){
                               result.left_length=records[0].left_length
                               result.right_length=records[0].right_length
                               result.left_width=records[0].left_width
                               result.right_width=records[0].right_width
                           }else{
                               result.left_length=''
                               result.right_length=''
                               result.left_width=''
                               result.right_width=''
                           }
                            res.json(result)
                        })
                    }else{
                        db.users.create({wxid:req.body.idValue}).then(function(data){
                            var result={id:data.id,open_id:data.wxid,card_id:'',name:'',nation:'',
                                date_birth:'',district:'',province:'',city:'',
                                county:'',height:0,weight:0,date_yunfu:'',
                                left_length:'',right_length:'',left_width:'',right_width:'',
                                "errcode":0,"errmsg":"ok"
                            }
                            if(data.gender===1){
                                result.sex='男'
                            }else{
                                result.sex='女'
                            }
                            res.json(result)
                        }).catch(function(err){
                            res.json({"errcode":3,"errmsg":"查询用户数据失败或未找到！"})
                        })
                    }
                })
            }else if(req.body.card_id!==undefined && req.body.open_id!==undefined){
                var name = req.body['name'];
                var sex =req.body['sex'];
                var date_birth = req.body['date_birth'];
                var nation = req.body['nation'];
                var address = req.body['address'];
                var sexNumber = 0
                if(sex === '男'){
                    sexNumber=1
                }
                db.users.update({name:req.body.name,date_birth:req.body.date_birth,card_id:req.body.card_id
                    ,nation:req.body.nation,address:req.body.address,gender:sexNumber,},{where:{wxid:req.body.open_id}}).then(function(){
                    res.json({"errcode":0,"errmsg":"ok"})
                }).catch(function(err){
                    res.json({"errcode":3,"errmsg":"更新用户数据失败！"})
                })
            }else if(req.body.open_id!==undefined && req.body.period!==undefined){
                db.users.update({lastPeriod:req.body.period},{where:{wxid:req.body.open_id}}).then(function(){
                    res.json({"errcode":0,"errmsg":"ok"})
                }).catch(function(err){
                    res.json({"errcode":3,"errmsg":"更新末次月经时间失败！"})
                })
            }else if(req.body.open_id!==undefined && req.body.height!==undefined){
                db.users.update({height:req.body.height},{where:{wxid:req.body.open_id}}).then(function(){
                    res.json({"errcode":0,"errmsg":"ok"})
                }).catch(function(err){
                    res.json({"errcode":3,"errmsg":"更新身高信息失败！"})
                })
            }else{
                res.json({"errcode":2,"errmsg":"成功连接接口服务器"})
            }
        }
    }
})

tour_router.route('/serverdata').post(function(req,res){
    if(req.body.sign === undefined){
        res.json({"errcode":1,"errmsg":"非法访问！"})
    }else{
        if(req.body.sign!=='liuhe'){
             res.json({"errcode":1,"errmsg":"非法访问！"})
        }else{
            if((req.body.open_id !== undefined || req.body.card_id !== undefined) && req.body.space_day!==undefined ){
                //判断是否需要扫描脚型
                var query=`select DATE_ADD(date_server, INTERVAL ? DAY) as date from yxd_basicinfos where `
                if(req.body.open_id !== undefined && req.body.card_id !== undefined){
                    query+='(open_id='+req.body.open_id+' or card_id='+req.body.card_id+');'
                }else{
                    if(req.body.open_id){
                        query+='open_id='+req.body.open_id
                    }else{
                        query+='card_id='+req.body.card_id
                    }
                }
                query+= " order by date_server desc limit 1";
                db.sequelize.query(query, { replacements: [req.body.space_day], 
                    type: db.sequelize.QueryTypes.SELECT }
                ).then(function(records){
                    if(records.length>0){
                        var nextdate = new Date(records[0].date)
                        var nowdate = new Date()
                        if(nowdate>nextdate){
                            res.json({"errcode":0,"errmsg":"true"})
                        }else{
                            res.json({"errcode":0,"errmsg":"false"})
                        }
                    }else{
                        res.json({"errcode":0,"errmsg":"true"})
                    }
                })
            }else if(req.body.oper!==undefined){
                if(req.body.oper==='add'){
                    var infos = JSON.parse(req.body.data)
                    // if(infos.mac_id===undefined){
                    //     res.json({"errcode":1,"errmsg":"添加脚型数据basicinfo失败！"})
                    //     return
                    // }
                    db.yxd_references.findOne({where:{mac_id:infos.mac_id}}).then(function(data){
                        if(data){
                            res.json({"errcode":0,"errmsg":"数据已经新增过了"})
                        }else{
                            var birth=new Date(infos.birth).toLocaleDateString()
                            var now = new Date()
                            var date_server = now.toLocaleDateString()+' '+now.toLocaleTimeString()
                            var date_yunfu=new Date(infos.date_yunfu).toLocaleDateString()
                            db.yxd_basicinfos.create({mac_id:infos.mac_id,
                                open_id:infos.open_id,card_id:infos.card_id,ticket:infos.ticket,
                                scene:infos.scene,from_id:infos.from_id,from_app:infos.from_app,date_server:date_server,
                                date_host:infos.date_host,name:infos.name,sex:infos.sex,birth:birth,
                                age:infos.age,district:infos.district,province:infos.province,city:infos.city,
                                county:infos.county,country:infos.country,nation:infos.nation,height:infos.height,
                                weight:infos.weight,date_yunfu:date_yunfu,hospital_no:infos.hospital_no,hospital_name:infos.hospital_name,
                                doctor_name:infos.doctor_name,user_id:infos.user_id,doctor_id:infos.doctor_id,
                                period:infos.period}).then(function(dta){
                                    db.yxd_parameters.create({id:dta.id,mac_id:infos.mac_id,
                                        left_length:infos.left_length,left_width:infos.left_width,right_length:infos.right_length,
                                        right_width:infos.right_width,left_length_725:infos.left_length_725,left_length_635:infos.left_length_635,
                                        left_length_68:infos.left_length_68,left_length_41:infos.left_length_41,left_width_725:infos.left_width_725,
                                        left_width_635:infos.left_width_635,left_width_68:infos.left_width_68,left_width_41:infos.left_width_41,left_width_41full:infos.left_width_41full,
                                        right_length_725:infos.right_length_725,right_length_635:infos.right_length_635,right_length_68:infos.right_length_68,right_length_41:infos.right_length_41,
                                        right_width_725:infos.right_width_725,right_width_635:infos.right_width_635,right_width_68:infos.right_width_68,right_width_41:infos.right_width_41,
                                        right_width_41full:infos.right_width_41full,left_length_90:infos.left_length_90,left_length_825:infos.left_length_825,left_length_78:infos.left_length_78,
                                        left_length_18:infos.left_length_18,left_width_90:infos.left_width_90,left_width_78:infos.left_width_78,left_width_18:infos.left_width_18,
                                        left_width_ratio:infos.left_width_ratio,left_center_angle:infos.left_center_angle,left_incline_angle:infos.left_incline_angle,right_length_90:infos.right_length_90,
                                        right_length_825:infos.right_length_825,right_length_78:infos.right_length_78,right_length_18:infos.right_length_18,right_width_90:infos.right_width_90,
                                        right_width_78:infos.right_width_78,right_width_18:infos.right_width_18,right_width_ratio:infos.right_width_ratio,right_center_angle:infos.right_center_angle,
                                        right_incline_angle:infos.right_incline_angle}).then(function(){
                                            db.yxd_suggestions.create({id:dta.id,mac_id:infos.mac_id,left_foot_size:infos.left_foot_size,
                                                left_foot_width:infos.left_foot_width,left_foot_width2:infos.left_foot_width2,left_foot_status:infos.left_foot_status,left_int_status:infos.left_int_status,
                                                right_foot_size:infos.right_foot_size,right_foot_width:infos.right_foot_width,right_foot_width2:infos.right_foot_width2,right_foot_status:infos.right_foot_status,
                                                right_int_status:infos.right_int_status}).then(function(){
                                                    db.yxd_pictures.create({id:dta.id,mac_id:infos.mac_id,left_url:infos.left_url,
                                                        right_url:infos.right_url,left_dpi:infos.left_dpi,right_dpi:infos.right_dpi,left_urla:infos.left_urla,
                                                        right_urla:infos.right_urla}).then(function(){
                                                            db.yxd_references.create({id:dta.id,mac_id:infos.mac_id,
                                                                lcircle_01_x:infos.lcircle_01_x,lcircle_01_y:infos.lcircle_01_y,lcircle_02_x:infos.lcircle_02_x,lcircle_02_y:infos.lcircle_02_y,
                                                                lcircle_03_x:infos.lcircle_03_x,lcircle_03_y:infos.lcircle_03_y,lfoot_top:infos.lfoot_top,lfoot_bottom:infos.lfoot_bottom,
                                                                lfoot_right:infos.lfoot_right,lfoot_left:infos.lfoot_left,lscale:infos.lscale,rcircle_01_x:infos.rcircle_01_x,
                                                                rcircle_01_y:infos.rcircle_01_y,rcircle_02_x:infos.rcircle_02_x,rcircle_02_y:infos.rcircle_02_y,rcircle_03_x:infos.rcircle_03_x,
                                                                rcircle_03_y:infos.rcircle_03_y,rfoot_top:infos.rfoot_top,rfoot_bottom:infos.rfoot_bottom,rfoot_right:infos.rfoot_right,
                                                                rfoot_left:infos.rfoot_left,rscale:infos.rscale,lcircle_725_x:infos.lcircle_725_x,lcircle_725_y:infos.lcircle_725_y,
                                                                lcircle_635_x:infos.lcircle_635_x,lcircle_635_y:infos.lcircle_635_y,lcircle_4101_x:infos.lcircle_4101_x,lcircle_4101_y:infos.lcircle_4101_y,
                                                                lcircle_4102_x:infos.lcircle_4102_x,lcircle_4102_y:infos.lcircle_4102_y,lbreak_01_x:infos.lbreak_01_x,lbreak_01_y:infos.lbreak_01_y,
                                                                lbreak_02_x:infos.lbreak_02_x,lbreak_02_y:infos.lbreak_02_y,rcircle_725_x:infos.rcircle_725_x,rcircle_725_y:infos.rcircle_725_y,
                                                                rcircle_635_x:infos.rcircle_635_x,rcircle_635_y:infos.rcircle_635_y,rcircle_4101_x:infos.rcircle_4101_x,rcircle_4101_y:infos.rcircle_4101_y,
                                                                rcircle_4102_x:infos.rcircle_4102_x,rcircle_4102_y:infos.rcircle_4102_y,rbreak_01_x:infos.rbreak_01_x,rbreak_01_y:infos.rbreak_01_y,
                                                                rbreak_02_x:infos.rbreak_02_x,rbreak_02_y:infos.rbreak_02_y,lcircle_18_x:infos.lcircle_18_x,lcircle_18_y:infos.lcircle_18_y,
                                                                lcircle_1801_x:infos.lcircle_1801_x,lcircle_1801_y:infos.lcircle_1801_y,lcircle_1802_x:infos.lcircle_1802_x,lcircle_1802_y:infos.lcircle_1802_y,
                                                                lcircle_90_x:infos.lcircle_90_x,lcircle_90_y:infos.lcircle_90_y,lcircle_825_x:infos.lcircle_825_x,lcircle_825_y:infos.lcircle_825_y,
                                                                lcircle_78_x:infos.lcircle_78_x,lcircle_78_y:infos.lcircle_78_y,lcircle_80_x:infos.lcircle_80_x,lcircle_80_y:infos.lcircle_80_y,
                                                                lcircle_65_x:infos.lcircle_65_x,lcircle_65_y:infos.lcircle_65_y,rcircle_18_x:infos.rcircle_18_x,rcircle_18_y:infos.rcircle_18_y,
                                                                rcircle_1801_x:infos.rcircle_1801_x,rcircle_1801_y:infos.rcircle_1801_y,rcircle_1802_x:infos.rcircle_1802_x,rcircle_1802_y:infos.rcircle_1802_y,
                                                                rcircle_90_x:infos.rcircle_90_x,rcircle_90_y:infos.rcircle_90_y,rcircle_825_x:infos.rcircle_825_x,rcircle_825_y:infos.rcircle_825_y,
                                                                rcircle_78_x:infos.rcircle_78_x,rcircle_78_y:infos.rcircle_78_y,rcircle_80_x:infos.rcircle_80_x,rcircle_80_y:infos.rcircle_80_y,
                                                                rcircle_65_x:infos.rcircle_65_x,rcircle_65_y:infos.rcircle_65_y}).then(function(){
                                                                    //同步open_id和card_id
                                                                    if(infos.open_id!=='null'&&infos.card_id!=='null'){

                                                                    }
                                                                    //更新该open_id下的档案总数
                                                                    if(infos.open_id!=='null'){
                                                                        var query=`select count(id) as amount from yxd_basicinfos where open_id=?`
                                                                        db.sequelize.query(query, { replacements: [infos.open_id], 
                                                                            type: db.sequelize.QueryTypes.SELECT }
                                                                        ).then(function(records){
                                                                            if(records.length>0){
                                                                                db.users.update({file_amount:records[0].amount},{where:{wxid:infos.open_id}}).then(function(){

                                                                                })
                                                                            }
                                                                        })
                                                                    }
                                                                    // //如果存在挂号功能，将挂号信息和脚型数据做匹配
                                                                    // if(infos.hospital_no!=='null'){
                                                                    //     db.yxd_details.update({record_id:dta.id},{where:{hospital_no:infos.hospital_no,
                                                                    //         clinic_dept:infos.clinic_dept,doctor_name:infos.doctor_name,clinic_type:infos.clinic_type,
                                                                    //         }}).then(function(){

                                                                    //     })
                                                                    // }
                                                                    res.json({"errcode":0,"errmsg":"新增脚型数据成功"})
                                                            }).catch(function(err){
                                                                db.yxd_pictures.destroy({where:{id:dta.id}})
                                                                db.yxd_basicinfos.destroy({where:{id:dta.id}})
                                                                db.yxd_suggestions.destroy({where:{id:dta.id}})
                                                                db.yxd_parameters.destroy({where:{id:dta.id}})
                                                                res.json({"errcode":1,"errmsg":"添加脚型数据reference失败！"})
                                                            })
                                                    }).catch(function(err){
                                                        db.yxd_basicinfos.destroy({where:{id:dta.id}})
                                                        db.yxd_suggestions.destroy({where:{id:dta.id}})
                                                        db.yxd_parameters.destroy({where:{id:dta.id}})
                                                        res.json({"errcode":1,"errmsg":"添加脚型数据picture失败！"})
                                                    })
                                            }).catch(function(err){
                                                db.yxd_basicinfos.destroy({where:{id:dta.id}})
                                                db.yxd_parameters.destroy({where:{id:dta.id}})
                                                res.json({"errcode":1,"errmsg":"添加脚型数据suggestion失败！"})
                                            })
                                    }).catch(function(err){
                                        db.yxd_basicinfos.destroy({where:{id:dta.id}})
                                        res.json({"errcode":1,"errmsg":"添加脚型数据parameter失败！"})
                                    })
                            }).catch(function(err){
                                res.json({"errcode":1,"errmsg":"添加脚型数据basicinfo失败！"+err})
                            })
                        }
                    })
                }else if(req.body.oper==='edit'){
                    var infos = JSON.parse(req.body.data)
                    db.yxd_parameters.update({left_length:infos.left_length,left_width:infos.left_width,right_length:infos.right_length,
                        right_width:infos.right_width,left_length_725:infos.left_length_725,left_length_635:infos.left_length_635,
                        left_length_68:infos.left_length_68,left_length_41:infos.left_length_41,left_width_725:infos.left_width_725,
                        left_width_635:infos.left_width_635,left_width_68:infos.left_width_68,left_width_41:infos.left_width_41,
                        left_width_41full:infos.left_width_41full,right_length_725:infos.right_length_725,right_length_635:infos.right_length_635,
                        right_length_68:infos.right_length_68,right_length_41:infos.right_length_41,right_width_725:infos.right_width_725,
                        right_width_635:infos.right_width_635,right_width_68:infos.right_width_68,right_width_41:infos.right_width_41,
                        right_width_41full:infos.right_width_41full,left_length_90:infos.left_length_90,left_length_825:infos.left_length_825,
                        left_length_78:infos.left_length_78,left_length_18:infos.left_length_18,left_width_90:infos.left_width_90,
                        left_width_78:infos.left_width_78,left_width_18:infos.left_width_18,left_width_ratio:infos.left_width_ratio,
                        left_center_angle:infos.left_center_angle,left_incline_angle:infos.left_incline_angle,right_length_90:infos.right_length_90,
                        right_length_825:infos.right_length_825,right_length_78:infos.right_length_78,right_length_18:infos.right_length_18,
                        right_width_90:infos.right_width_90,right_width_78:infos.right_width_78,right_width_18:infos.right_width_18,
                        right_width_ratio:infos.right_width_ratio,right_center_angle:infos.right_center_angle,right_incline_angle:infos.right_incline_angle
                        },{where:{mac_id:infos.mac_id}}).then(function(){

                    })
                    db.yxd_suggestions.update({left_foot_size:infos.left_foot_size,left_foot_width:infos.left_foot_width,left_foot_status:infos.left_foot_status,
                        right_foot_size:infos.right_foot_size,right_foot_width:infos.right_foot_width,right_foot_status:infos.right_foot_status,
                        left_foot_width2:infos.left_foot_width2,left_int_status:infos.left_int_status,right_foot_width2:infos.right_foot_width2,
                        right_int_status:infos.right_int_status},{where:{mac_id:infos.mac_id}}).then(function(){

                    })
                    db.yxd_references.update({lcircle_01_x:infos.lcircle_01_x,lcircle_01_y:infos.lcircle_01_y,lcircle_02_x:infos.lcircle_02_x,
                        lcircle_02_y:infos.lcircle_02_y,lcircle_03_x:infos.lcircle_03_x,lcircle_03_y:infos.lcircle_03_y,
                        rcircle_01_x:infos.rcircle_01_x,rcircle_01_y:infos.rcircle_01_y,rcircle_02_x:infos.rcircle_02_x,
                        rcircle_02_y:infos.rcircle_02_y,rcircle_03_x:infos.rcircle_03_x,rcircle_03_y:infos.rcircle_03_y,
                        lcircle_725_x:infos.lcircle_725_x,lcircle_725_y:infos.lcircle_725_y,lcircle_635_x:infos.lcircle_635_x,
                        lcircle_635_y:infos.lcircle_635_y,lcircle_4101_x:infos.lcircle_4101_x,lcircle_4101_y:infos.lcircle_4101_y,
                        lcircle_4102_x:infos.lcircle_4102_x,lcircle_4102_y:infos.lcircle_4102_y,lbreak_01_x:infos.lbreak_01_x,
                        lbreak_01_y:infos.lbreak_01_y,lbreak_02_x:infos.lbreak_02_x,lbreak_02_y:infos.lbreak_02_y,
                        rcircle_725_x:infos.rcircle_725_x,rcircle_725_y:infos.rcircle_725_y,rcircle_635_x:infos.rcircle_635_x,
                        rcircle_635_y:infos.rcircle_635_y,rcircle_4101_x:infos.rcircle_4101_x,rcircle_4101_y:infos.rcircle_4101_y,
                        rcircle_4102_x:infos.rcircle_4102_x,rcircle_4102_y:infos.rcircle_4102_y,rbreak_01_x:infos.rbreak_01_x,
                        rbreak_01_y:infos.rbreak_01_y,rbreak_02_x:infos.rbreak_02_x,rbreak_02_y:infos.rbreak_02_y,
                        lcircle_80_x:infos.lcircle_80_x,lcircle_80_y:infos.lcircle_80_y,lcircle_65_x:infos.lcircle_65_x,
                        lcircle_65_y:infos.lcircle_65_y,rcircle_80_x:infos.rcircle_80_x,rcircle_80_y:infos.rcircle_80_y,
                        rcircle_65_x:infos.rcircle_65_x,rcircle_65_y:infos.rcircle_65_y,lcircle_1801_x:infos.lcircle_1801_x,
                        lcircle_1801_y:infos.lcircle_1801_y,lcircle_1802_x:infos.lcircle_1802_x,lcircle_1802_y:infos.lcircle_1802_y,
                        rcircle_1801_x:infos.rcircle_1801_x,rcircle_1801_y:infos.rcircle_1801_y,rcircle_1802_x:infos.rcircle_1802_x,
                        rcircle_1802_y:infos.rcircle_1802_y
                        },{where:{mac_id:infos.mac_id}}).then(function(){

                    })
                    res.json({"errcode":0,"errmsg":"修改脚型数据成功"})
                }else if(req.body.oper==='delete'){
                    var record_id = req.body.record_id
                    if(req.body.mac_id!==undefined){
                        db.yxd_pictures.destroy({where:{mac_id:req.body.mac_id}})
                        db.yxd_basicinfos.destroy({where:{mac_id:req.body.mac_id}})
                        db.yxd_suggestions.destroy({where:{mac_id:req.body.mac_id}})
                        db.yxd_parameters.destroy({where:{mac_id:req.body.mac_id}})
                        db.yxd_reference.destroy({where:{mac_id:req.body.mac_id}})
                    }else{
                        db.yxd_pictures.destroy({where:{id:record_id}})
                        db.yxd_basicinfos.destroy({where:{id:record_id}})
                        db.yxd_suggestions.destroy({where:{id:record_id}})
                        db.yxd_parameters.destroy({where:{id:record_id}})
                        db.yxd_reference.destroy({where:{id:record_id}})
                    }
                    res.json({"errcode":0,"errmsg":"删除脚型数据成功"})
                }
            }else{
                res.json({"errcode":2,"errmsg":"成功连接接口服务器"})
            }
        }
    }
})

tour_router.route('/serverclinic').post(function(req,res){
    if(req.body.sign!==undefined){
        if(req.body.hospital !== undefined && req.body.worktime != undefined){
            db.doctors.findAll({where:{hospital_no:req.body.hospital}}).then(function(data){
                if(data){
                    var clinic = []
                    for(var i=0;i<data.length;i++){
                        clinic.push({id:data[i].id,clinic_dept:'',doctor_name:data[i].name,clinic_type:'',wait:'0',count_num:0})
                    }
                    res.json({"errcode":0,"errmsg":"",total:data.length,clinic:clinic})
                }else{
                    res.json({"errcode":0,"errmsg":"",total:0,clinic:[]})
                }
            })
        }else if(req.body.hospital !== undefined && req.body.doctor !== undefined && req.body.did !== undefined){
            db.users.update({doctor_id:req.body.did},{where:{wxid:req.body.open_id,card_id:req.body.card_id}}).then(function(){
                res.json({"errcode":0,"errmsg":"队列中","doctor":req.body.doctor})
            }).catch(function(err){
                res.json({"errcode":2,"errmsg":err})
            })
        }else{
            res.json({"errcode":2,"errmsg":"成功连接接口服务器"})
        }
    }else{
        res.json({"errcode":1,"errmsg":"非法访问！"})
    }
})

tour_router.route('/get_user_reportlist').post(function(req,res){
    var openid = req.body.openid || ''
    var page = req.body.p || 1
     if(openid === '' || page < 1){
        res.json({error:g.errorCode.WRONG_PARAM})
    }else{
        db.yxd_basicinfos.findAndCountAll({attributes: ['mac_id','open_id','card_id','user_id','date_server',
            'name','birth','date_yunfu','hospital_name','doctor_name'],
            where:{open_id:openid},order: db.sequelize.literal('ID DESC'),limit:10,offset:10*(page-1)}).then(function(records){
            res.json({r:records})
        }).catch(function(err){
            res.json({error:g.errorCode.WRONG_SQL})
        })
    }
})

tour_router.route('/get_doctor_reportlist').post(function(req,res){
    var doctor_id = req.body.did || 0
    var page = req.body.p || 1
    var status = req.body.s || 0
    if(doctor_id === '' || page < 1){
        res.json({error:g.errorCode.WRONG_PARAM})
    }else{
        db.yxd_basicinfos.findAndCountAll({attributes: ['id','mac_id','open_id','card_id','user_id','date_server',
            'name','birth','date_yunfu','status','age','sex','height','weight'],
            where:{doctor_id:doctor_id,status:status},order: db.sequelize.literal('ID DESC'),limit:10,offset:10*(page-1)}).then(function(records){
            res.json({r:records})
        }).catch(function(err){
            res.json({error:g.errorCode.WRONG_SQL})
        })
    }
})

tour_router.route('/doctor_mark_readed').post(function(req,res){
    var id = req.body.id || 0
    if(id === 0){
        res.json({error:g.errorCode.WRONG_PARAM})
    }else{
        db.yxd_basicinfos.update({status:1},{where:{id:id}}).then(function(){
            res.json({ok:1})
        })
    }
})

tour_router.route('/get_user_latest_report').post(function(req,res){
    var openid = req.body.openid || ''
     if(openid === ''){
        res.json({error:g.errorCode.WRONG_PARAM})
    }else{
        db.yxd_basicinfos.findAndCountAll({attributes: ['mac_id','open_id','card_id','user_id','date_server',
            'name','birth','date_yunfu','hospital_name','doctor_name'],
            where:{open_id:openid},order: db.sequelize.literal('ID DESC'),limit:1}).then(function(records){
            res.json({r:records})
        }).catch(function(err){
            res.json({error:g.errorCode.WRONG_SQL})
        })
    }
})

tour_router.route('/getreport').post(function(req,res){
    var report_id = req.body.rid || 0
    var query = `select yb.mac_id,yb.user_id,yb.open_id,yb.card_id,yb.name,yb.age,yb.sex,
        ypp.left_width,ypp.left_length,ypp.right_length,ypp.right_width,yp.left_urla,yp.right_urla,
	    ys.left_foot_size,ys.left_foot_width,ys.left_foot_width2,left_foot_status,ys.right_foot_size,ys.right_foot_width,
        ys.right_foot_width2,right_foot_status from yxd_basicinfos yb join yxd_pictures yp join yxd_parameters ypp join 
        yxd_suggestions ys ON yb.mac_id=yp.mac_id and yb.mac_id=ypp.mac_id and yb.mac_id=ys.mac_id and yb.mac_id=?`
    db.sequelize.query(query, { replacements: [report_id], 
        type: db.sequelize.QueryTypes.SELECT }
        ).then(function(records){
        if(records){
            res.json({data:records})
        }else{
            res.json({data:[]})
        }
    }).catch(function(err){
        res.json({error:g.errorCode.WRONG_SQL})
    })
})

tour_router.route('/yxd3').post(function(req,res){
    if(req.body.mac_id !== undefined){
        db.yxd_machines.findOne({where:{machine_mac:req.body.mac_id,status:1}}).then(function(data){
            if(data){
                db.hospitals.findOne({where:{id:data.hospital_no}}).then(function(hos){
                    if(hos){
                        res.json({"hospital_no":hos.id,"machine_type":"2",
                            "hospital_name":hos.name,"worktime":"23:00",
                            "db_ip":"","db_port":"",
                            "db_name":"","db_user":"","db_psw":"",
                            "wechat_url":cfg.wechatServerAdress+"/api/accesstoken",
                            "verify_url":cfg.serverAdress+':'+cfg.listen+"/api/scanverify",
                            "userinfo_url":cfg.serverAdress+':'+cfg.listen+"/api/userinfo",
                            "clinic_url":cfg.serverAdress+':'+cfg.listen+"/api/serverclinic",
                            "upload_url":cfg.serverAdress+':'+cfg.listen+"/serverftp4",
                            "data_url":cfg.serverAdress+':'+cfg.listen+"/api/serverdata",
                            "space_day":"0","app_name":"yxd","errcode":0,"errmsg":"ok2"})
                    }else{
                        res.json({"errcode":2,"errmsg":"成功连接数据服务器"})
                    }
                })
            }else{
                // illegal
                db.yxd_wrong_machine_logs.create({ipaddress:req.ip,wrong_mac:req.body.mac_id}).then(function(){
                    res.json({"errcode":3,"errmsg":"非法"})
                })
            }
        })
    }else{
        res.json({"errcode":2,"errmsg":"成功连接数据服务器"})
    }
});

module.exports=tour_router;