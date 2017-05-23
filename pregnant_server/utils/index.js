
var cfg = require('../config.json')
var request=require('request')

var utilsf = {
    sendDataToWxServer:function(data,cb){
        var options = {
            headers: {"Connection": "close"},
            url: cfg.wx_server_address+'/api/sendMsg',
            method: 'POST',
            json:true,
            body: data
        }
        request(options, cb);
    }
}

module.exports = utilsf