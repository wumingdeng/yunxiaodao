var cluster = require('cluster');
var http = require('http');
var numCPUs = require('os').cpus().length;

if (cluster.isMaster) {
    console.log("master start...");

    // Fork workers.
    for (var i = 0; i < numCPUs/2; i++) {
        cluster.fork();
    }

    cluster.on('listening',function(worker,address){
        console.log('listening: worker ' + worker.process.pid +', Address: '+address.address+":"+address.port);
    });

    cluster.on('exit', function(worker, code, signal) {
        console.log('worker ' + worker.process.pid + ' died');
    });
} else {
    var express = require("express");
    var app = express();
    var bodyParser = require('body-parser');
    var path = require('path');
    var cfg = require('./config.json')
    var db = require('./models');
    var mem = require('./memory');
    var utils = require('./utils')
    var middleware = require('./middleware')
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

    var server = app.listen(app.get('port'), function() {
        console.log('server listening on port ' + server.address().port);
    });
}