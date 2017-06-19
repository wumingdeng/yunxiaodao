var utils = {
    onServerInit:onServerInit,
    onAddServer:onAddServer,
    json2base64:json2base64,
    writeback:writeback,
    MD5Sign:MD5Sign,
    Md5:md5,
    VerifyToken:verifyToken,
    sendDataToWxServer:function(data,cb){
        var options = {
            headers: {"Connection": "close"},
            url: cfg.wx_server_address+'/api/sendMsg',
            method: 'POST',
            json:true,
            body: data
        }
        request(options, cb);
    },

    sendPayToWxServer:function(data,cb){
        var options = {
            headers: {"Connection": "close"},
            url: cfg.wx_server_address+'/api/sendPay',
            method: 'POST',
            json:true,
            body: data
        }
        request(options, cb);
    },

    authFromWxServer:function(data,cb){
        var options = {
            headers: {"Connection": "close"},
            url: cfg.wx_server_address+'/api/auth',
            method: 'POST',
            json:true,
            body: data
        }
        request(options, cb);
    }
}

var cfg = require('../config.json')
var request=require('request')
var g = require('../global')
var crypto = require('crypto')
var jwt = require('jsonwebtoken');

function verifyToken(req,res,next){
    var token = req.body.token || req.query.token || req.headers['x-access-token'];
    if (token) {
        // verifies secret and checks exp
        jwt.verify(token, cfg.secret, function(err, decoded) {      
            if (err) {
                res.json({ success: false, message: 'Failed to authenticate token.' }); 
            } else {
                // if everything is good, save to request for use in other routes
                req.decoded = decoded;
                next(req,res)
            }
        });
    } else{
        return res.status(403).send({ 
            success: false, 
            message: 'No token provided.' 
        });
    }
}

function md5(str,key){  
    var md5sum = crypto.createHash('md5',key);
    md5sum.update(str);
    str = md5sum.digest('hex');
    return str;
} 

function signature(obj){
    var keys = Object.keys(obj).sort()
    var resultString=""
    for(k in keys){
        resultString+=keys[k]+'='+obj[keys[k]]+'&'
    }
    resultString=resultString.substr(0,resultString.length-1)
    return resultString
}

function MD5Sign(obj,key){
    var str=signature(obj)
    return md5(str,key).toUpperCase()
}

function onAddServer(gametype,serverid,gameserverip,status,rule){
    var lists=g.roomServers[gametype]
    if(lists===undefined){
        g.roomServers[gametype] = {}
        g.roomServers[gametype][serverid]={ip:gameserverip,status:status,serverid:serverid,rule:rule}
    }else{
        var roominfo = g.roomServers[gametype][serverid]
        if(roominfo===undefined){
            g.roomServers[gametype][serverid]={ip:gameserverip,status:status,serverid:serverid,rule:rule}
        }else{
            g.roomServers[gametype][serverid].ip=gameserverip
            g.roomServers[gametype][serverid].status=status
            g.roomServers[gametype][serverid].rule=rule
        }
    }
}

function json2base64(obj){
    var str = JSON.stringify(obj)
    var b = new Buffer(str,'utf-8');
    return b.toString('base64');
}

function writeback(obj,res){
    res.write(json2base64(obj))
    res.end()
}

function onServerInit(){
    for(var i=0;i<cfg.logicAddress.length;i++){
        var options={
            "url":cfg.logicAddress[i]+'/SyncStatus',
            "method":"post"
        }
        request(options,function(error, response, body){
            if (!error && response.statusCode == 200) {
                var bodyToken=JSON.parse(body);
                onAddServer(bodyToken.gametype,bodyToken.serverid,bodyToken.gameserverip,bodyToken.status,bodyToken.rule)
            }else{
                console.log(error)
            }
        })
    }
    g.LoadAllMemoryConfigs()
}

module.exports = utils