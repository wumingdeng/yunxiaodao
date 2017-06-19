var express=require('express');
var common_router=express.Router()
var mem = require('../memory')
var g = require('../global')
var citys = require('../citys.json')
// 内存数据维护

// 获取主页信息
common_router.route('/mainpage').get(function(req,res){
    var product = []
    for(var k in mem.m.products){
        var data = {}
        data.name = mem.m.products[k].name
        data.id = mem.m.products[k].id
        data.smallPic = mem.m.products[k].smallPic
        product.push(data)
    }
    // res.json({cat:mem.m.catalogs,swipe:mem.m.swipe_configs,services:services})
    res.json({swipe:mem.m.swipe_configs,product:product})
});

// 获取服务详情
// input service id
common_router.route('/getProductDetail').post(function(req,res){
    var sid = req.body.sid
    if(sid === undefined){
        res.json({err:g.errorCode.WRONG_PARAM})
    }else{
        if (mem.m.products[sid]) {
            res.json({ok:mem.m.products[sid]})
        } else {
            res.json({ok:0});
        }
    }
});


common_router.route('/freshConfig').get(function(req,res){
    mem.f.InitDbMemory()
    res.json({ok:0});
});

//取体检周期配置
common_router.route('/getCheckCycle').get(function(req,res){
    if (mem.m.check_cycle_configs) {
        res.json({data:mem.m.check_cycle_configs})
    } else {
        res.json({data:[]})
    }
})

module.exports=common_router;