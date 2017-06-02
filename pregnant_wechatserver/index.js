/**
 * Created by liuqiang on 16/4/27.
 */

var express = require("express");
var app = express();
var bodyParser = require('body-parser');
var path = require('path');
var cfg = require('./config.json')
var db = require('./models');
var utils = require('./utils')
var crypto = require('crypto');
// api controllers
var route_table = require('./routes/routeTable');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));

// 设置跨域访问
app.all('*', function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");
    res.header("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
    next();
});

// error handler
app.use(function(err, req, res, next) {
  console.error(err.stack);
  res.status(500).send('Something broke!');
  return false;
});
// middleware register
// api router
for(var key in route_table){
    app.use('/api',route_table[key]);
}
// wechat vaild
var wc = require('./routes/wechat')
app.use('/wechat',wc)

utils.wechat_f.getToken()

// app.get('/', function (req, res) {    
//     var signature = req.query.signature;
//     var timestamp = req.query.timestamp;
//     var nonce = req.query.nonce;
//     var echostr = req.query.echostr;
  
//     /*  加密/校验流程如下： */
//     //1. 将token、timestamp、nonce三个参数进行字典序排序
//     var array = new Array(token,timestamp,nonce);
//     array.sort();
//     var str = array.toString().replace(/,/g,"");

//     console.log(str);
  
//     //2. 将三个参数字符串拼接成一个字符串进行sha1加密
//     var sha1Code = crypto.createHash("sha1");
//     var code = sha1Code.update(str,'utf-8').digest("hex");
  
//     //3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
//     if(code===signature){
//         res.send(echostr)
//     }else{
//         res.send("error");
//     }
// });



app.set('port', cfg.listen);
app.use(express.static(path.join(__dirname, './')));
var server = app.listen(app.get('port'), function() {
    console.log('server listening on port ' + server.address().port);
    // 定时任务
    require('./controllers/crontabController')
});