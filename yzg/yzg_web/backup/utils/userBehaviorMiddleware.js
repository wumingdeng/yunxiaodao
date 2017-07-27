var db = require('../models')

function userBehavior(req,res,next) {
	if (req.decoded && req.decoded.wxid) {
		db.user_behaviors.create({
			userid:req.decoded.wxid,
			time:new Date(),
			api: req.originalUrl
		}).then(function() {

		}, function(err) {
			console.log(err)
		})
	}
	next();
}



module.exports = userBehavior;