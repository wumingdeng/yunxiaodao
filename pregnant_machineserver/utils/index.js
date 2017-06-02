var utils = {
    onServerInit:onServerInit,
    onAddServer:onAddServer,
    json2base64:json2base64,
    writeback:writeback,
    MD5Sign:MD5Sign
}

var request = require('request')
var config = require('../config.json')
var g = require('../global')
var crypto = require('crypto')

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
    
    g.LoadAllMemoryConfigs()
}

module.exports = utils