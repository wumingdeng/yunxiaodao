var request = require('request')
var fs = require('fs')
var cfg = require('../config.json')
var qs = require('qs')
var OAuth = require('wechat-oauth');
var client = new OAuth(cfg.appid, cfg.appsec);

function getJsTicket(cb,err){
    var url='https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token='+cfg.access_token+'&type=jsapi';
        request(url,function(error, response, body){
            if (!error && response.statusCode == 200) {
                var bodyToken=JSON.parse(body);
                console.log(body)
                if(bodyToken&&bodyToken.ticket){
                    cfg.jsticket = bodyToken.ticket
                    cb(bodyToken.ticket)
                }else{
                    err()
                }
            }else{
                err()
            }
        })
}
var f = {
    getToken:function(appid,appsec){
        var self = this;
        var _appid = appid || cfg.appid
        var _appsec = appsec || cfg.appsec
        var url='https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid='+_appid+'&secret='+_appsec;
            request(url,function(error, response, body){
                if (!error && response.statusCode == 200) {
                    var bodyToken=JSON.parse(body);
                    if(bodyToken&&bodyToken.access_token){
                        cfg.access_token = bodyToken.access_token;
                        getJsTicket(function(){},function(){})
                        self.setupMenu(cfg.authCodeAddress)
                        // fs.writeFile('./token', bodyToken.access_token, function (err) {
                        // });
                    }
                }
            })
    },

    getUserOpenid:function(code,cb,err){
        var _appid =  cfg.appid
        var _appsec = cfg.appsec
        var url='https://api.weixin.qq.com/sns/oauth2/access_token?appid='+_appid+'&secret='+_appsec+'&code='+code+'&grant_type=authorization_code';
            request(url,function(error, response, body){
                if (!error && response.statusCode == 200) {
                    var bodyToken=JSON.parse(body);
                    if(bodyToken&&bodyToken.openid){
                        cb(bodyToken.openid)
                    }
                }else{
                    err()
                }
            })
    },
    
    getUserBrief:function(openid,cb,err){
        var reqUrl = 'https://api.weixin.qq.com/cgi-bin/user/info?';
        var params = {
            access_token: cfg.access_token,
            openid: openid,
            lang: 'zh_CN'
        };

        var options = {
            method: 'get',
            url: reqUrl+qs.stringify(params)
        };
        request(options, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                var bodydata=JSON.parse(body);
                cb(bodydata)
            }else{
                err()
            }
        });
    },
    sendToUser:function(userOpenid,templateid,data){
        var url='https://api.weixin.qq.com/cgi-bin/message/template/send?access_token='+cfg.access_token;
        var obj={
            "touser":userOpenid,
            "template_id":templateid,
            "url":"http://weixin.qq.com/download",            
            "data":data
        }
        var options = {
            url: url,
            form: JSON.stringify(obj),
            headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
            }
        };
        
        request.post(options, function (err, res, body) {
            if (err) {
                console.log(err)
            }else {
                //   console.log(body);
            }
        })
    },
    setupMenu:function(codeCallback){
        // note by thonpy
        // 授权回调域名配置规范为全域名，比如需要网页授权的域名为：www.qq.com,
        // 配置以后此域名下面的页面http://www.qq.com/music.html 、 http://www.qq.com/login.html 都可以进行OAuth2.0鉴权。
        // 网页授权网址必须和redirect_uri的全域名要一样。 
        // 比如网页授权网址填的是qq.com， redirect_uri写www.qq.com/callback_uri 这样也会报错
        // for example:
        // 网页授权处填写例如xxvwa.free.natapp.cc的完整域名即可
        // 回调url填写如http://xxvwa.free.natapp.cc/api/auth即可
        var redirect_url_check = client.getAuthorizeURL(codeCallback+ '_check', 1, 'snsapi_userinfo');
        var redirect_url_foot = client.getAuthorizeURL(codeCallback + '_foot', 1, 'snsapi_base');
        var redirect_url_shoe = client.getAuthorizeURL(codeCallback + '_shoe', 1, 'snsapi_base');
        var url='https://api.weixin.qq.com/cgi-bin/menu/create?access_token='+cfg.access_token;
        var menus = {
            "button": [
                {
                    "type": "view",
                    "name": "体重管理",
                    "url": redirect_url_check//"https://open.weixin.qq.com/connect/oauth2/authorize?appid="+cfg.appid+"&redirect_uri="+codeCallback+"&response_type=code&scope=snsapi_base&state=1#wechat_redirect"
                },
                {
                    "type":"click",
                    "name":"足部健康",
                    "key":"FOOT_REPORT"
                },
                {
                    "type": "view",
                    "name": "一双好鞋",
                    "url": redirect_url_shoe//"https://open.weixin.qq.com/connect/oauth2/authorize?appid="+cfg.appid+"&redirect_uri="+codeCallback+"&response_type=code&scope=snsapi_base&state=1#wechat_redirect"
                },
            ]
        };
        var options = {
            url: url,
            form: JSON.stringify(menus),
            headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
            }
        };
    
        request.post(options, function (err, res, body) {
            if (err) {
                console.log("menu err:"+err)
            }else {
                console.log("menu modify success");
                console.log(body);
            }
        })
    }
}

module.exports = f