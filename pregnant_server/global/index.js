
function rad(d){
    return d * Math.PI / 180.0;//经纬度转换成三角函数中度分表形式。
}

/*
    "template_inform_user_pay_expired":"",// 通知用订单超时
    "template_inform_workers":"",// 通知技师抢单
    "template_inform_user_repick":"",// 通知用户重新选择
    "template_inform_worker_assign":"",// 通知技师派单
    "template_inform_user_beaccepted":"",// 通知用户订单被接受
    "template_inform_worker_start":"",// 通知技师服务开始
    "template_inform_user_comment":"",// 通知用户评论
    "template_inform_user_confirm_service":"",// 通知用户确认服务记录
    "template_inform_staff_start_service:":"", // 通知工作人员开始
    "template_inform_user_confirm_exam:":"", // 通知用户体检
    "template_inform_exam_worker":"", // 通知用户陪检人员
    "template_inform_exam_worker_connect":"" // 通知用户陪检人员主动联系用户
*/

module.exports = {
    care_status:{
        CARE_COUNTDOWN:0,
        CARE_INFORM_USER_NO_CONFIRM:1,
        CARE_INFORM_USER_CONFIRMED:2,
        CARE_INFORM_USER_DELAYED:3,
        CARE_USER_NO_REPLY:4,
        CARE_FIN:5
    },
    workerType:{
        staff_worker:1,
        staff_zhongyi:2,
        staff_dietitian:3,
        staff_baby_nurse:4,
        staff_exam_worker:5
    },
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
        WRONG_WRONGPASSWORD:15,// 
        WRONG_EXISTUSERNAME:16,// 
        WRONG_NO_SUCH_RECORD:17,// 
        WRONG_SERVICE_RECORD_ALREADY_MAKE:18,// 
        WRONG_SERVICE_RECORD_NOT_EXIST:19,// 
        WRONG_SERVICE_RECORD_STATUS:20,
        WRONG_CARE_RECORD_MISSING:21,
        WRONG_REQUEST_FAILED:997,// 
        WRONG_COMMON_SQL:998,// 
        WRONG_PARAM:999
    },
    orderStatus:{
        NOTPAY:0,
        PAYED_NO_DELIVER:1, //已支付 未发货
        DELIVER_NO_RECEIPT:2,   //待收货
        RECEIPT_NO_EVALUATE:3,  //待评价
        FINISH:4    //订单完成
    },
    earlyStage: 12,     //孕早期
    earlyAdd: 2,   //早期体重增加
    weightStatus:{
        skinny: '偏轻',
        normal: '正常',
        fat: '偏重'
    },
    weightStandard:[    //孕前BMI（kg/㎡）
        {
            name:"低体重",
            value:1,
            min:0,
            max:18.5
        },
        {
            name:"正常体重",
            value:2,
            min:18.5,
            max:25
        },
        {
            name:"超重",
            value:3,
            min:25,
            max:30
        },
        {
            name:"肥胖",
            value:4,
            min:30,
            max:10000
        }
    ],

    worker_per_servicepage:3,
    worker_per_workerpage:10,
    // 111km -> 1度
    // 附近17km
    getDistance:function(lng1,lat1,lng2,lat2){
        var radLat1 = rad(lat1);
        var radLat2 = rad(lat2);
        var a = radLat1 - radLat2;
        var b = rad(lng1) - rad(lng2);
        var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378.137;
        // EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s
    }
}