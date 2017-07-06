var redis = require("redis");
var cfg = require("../config.json")
var client = redis.createClient(6379,cfg.redisAdress)

client.on('ready',function(res){
    console.log('ready');    
});

var redis = {
    client:client
    // sub:sub
}

module.exports = redis;