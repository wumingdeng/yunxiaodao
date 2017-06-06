var express = require('express');
var app = express();
var cfg = require('./config.json')
var xml=require('node-xml');
var bodyParser = require('body-parser');

var db = require('./models');
var mem = require('./memory');
mem.f.InitDbMemory()

app.set('views',__dirname);    // 设置视图 
app.set('view engine', 'html'); 
var token = cfg.token;
var crypto = require('crypto');

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
});
// middleware register
// api router
for(var key in route_table){
    app.use('/api',route_table[key]);
}

var server = app.listen(8092, function () {
  var host = server.address().address;
  var port = server.address().port;

  console.log('Example app listening at http://%s:%s', host, port);
});