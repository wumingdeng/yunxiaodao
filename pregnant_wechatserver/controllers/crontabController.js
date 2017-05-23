var schedule = require('node-schedule');

var utils = require('../utils')

// var rule = new schedule.RecurrenceRule();
// rule.minute = 42;

var j = schedule.scheduleJob('0 0 */1 * * *', function(){
    console.log("get token now:"+Date.now())
    utils.wechat_f.getToken()
});

module.exports = schedule