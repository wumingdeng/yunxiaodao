/**
 * Created by liuqiang on 16/4/27.
 */
var express = require("express");
var app = express();
var bodyParser = require('body-parser');
var path = require('path');
var cfg = require('./config.json')
var db = require('./models');
var mem = require('./memory');
var utils = require('./utils')
var middleware = require('./middleware')
var process = require('process')
// api controllers
var route_table = require('./routes/routeTable');

// middleware added by text
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
// cross domain
app.all('*', function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");
    res.header("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
    next();
});

// upload
require('./routes/imgUtils')(app)

// error handler
app.use(function(err, req, res, next) {
  console.error(err.stack);
  res.status(500).send('Something broke!');
});

// api router
for(var key in route_table){
    app.use('/api',route_table[key]);
}

utils.onServerInit();

app.set('port', cfg.listen);
app.use(express.static(path.join(__dirname, './uploads')));

process.on('uncaughtException', function (err) {
　　console.log('Caught exception: ' + err);
});

var server = app.listen(app.get('port'), function() {
    console.log('server listening on port ' + server.address().port);
});