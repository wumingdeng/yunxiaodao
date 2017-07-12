var express=require('express');
var tour_router=express.Router();
var db = require('../models')
var mem = require('../memory')
var g = require('../global')
var utils = require('../utils')
var tpl = require('../template.json')
// for quick test
tour_router.route('/build').get(function(req,res){
    db.configs.findAll({
        // attributes: ['cat_id'],
        where:{id:0}
    }).then(function(data){
        // mem.r.pub.publish('memory-channel',"666")
        res.json({d:data});
    })
});

tour_router.route('/test').get(function(req,res){

    db.weight_records.findOne({where:{id:104}}).then(function(data){
        var time = data.recordDate;
        console.log(time);
        var newTime = new Date(time)
        console.log(new Date(newTime))
        db.weight_records.update({recordDate: new Date(time)},{where:{id:data.id}}).then(function(){
            res.json({time:time, newTime:newTime})
        })
    })
    
});

module.exports=tour_router;