var utils = require('./index.js')
const filterList = ['/api/quickloginwxUser','/api/getProductDetail','/api/fillWeight']
function checkExcept(originalUrl){
    for(var idx = 0;idx < filterList.length;idx++){
        if(filterList[idx] === originalUrl){
            return true;
        }
    }
    return false;
}
function authToken(req,res,next){
	if(req.method === 'GET' || checkExcept(req.originalUrl)){
    next();
	}else if(req.method == 'POST' || req.method == 'OPTIONS'){
	  if(req.body.token != null){
		    utils.VerifyToken(req,res,function(req,res){
		      next();
		    })
	  } else {
      res.json({err:16});
	  }
	}
}


module.exports = authToken;