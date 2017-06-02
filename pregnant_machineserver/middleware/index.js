module.exports={
    decryptMiddle:function(req,res,next){
        if(req.method === 'POST'){
            if(req.body !== undefined){
                var str=new Buffer(req.body, 'base64').toString()
                req.body = JSON.parse(str)
            }
        }
        next();
    },
    encryptMiddle:function(req,res,next){
        console.log(res)
        next();
    }
}