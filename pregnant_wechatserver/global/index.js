var redis = require("redis");
// var pub = redis.createClient();
var cfg = require('../config.json')
module.exports = {
    errorCode:{
        WRONG_WORKER_ID:1,
        WRONG_SERVICE_ID:2,
        WRONG_CATALOG_ID:3,
        WRONG_USER_MISSING:4,
        WRONG_VALID_CODE:5,
        WRONG_ORDER_NOT_EXIST:6,
        WRONG_ORDER_STATUS:7,
        WRONG_ORDER_CANNOT_RESET:8,
        WRONG_ORDER_CANNOT_CANCEL:9,
        WRONG_CANNOT_COMMENT:10,
        WRONG_MIN_PAY:11,
        WRONG_MAX_SETTLED:12,// 超过最大结算额度
        WRONG_NOT_ENOUGH_MONEY:13,// 没有足够提现额度
        WRONG_WXCHAT_AUTHFAILED:14,// 没有足够提现额度
        WRONG_PARAM:999
    },
    // pub:pub
    wechatPayInitConfig : {
      partnerKey: cfg.partnerKey,
      appId: cfg.appid,
      mchId: cfg.mchid,
      notifyUrl: cfg.wechatServerAddress,
      // pfx: fs.readFileSync("")
    }
}