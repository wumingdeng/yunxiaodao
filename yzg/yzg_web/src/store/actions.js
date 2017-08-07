// import g from '../globals/global'
import g from '../globals/global'

var serverAddress
if (process.env.NODE_ENV == 'development') {
  serverAddress = g.debugServerAddress;
} else {
  serverAddress = g.serverAddress;
}

function onErrorRefresh(vue,err) {
  // vue.$f7.params.modalButtonOk = '刷新'
  alert(err || '刷新重试',()=>{
    // window.location.reload(); 
  })
}

function onErrorHandler(errCode) {
  if (errCode == 99) {
    //token过期了
    Global.s.state.isLogin = false; //重新登录
    Global.v.$router.push('/')
  } else if (errCode == 23) {
    Global.v.$f7.alert('','无效的二维码',function() {
      //关闭页面
      if (typeof WeixinJSBridge != "undefined") {
          WeixinJSBridge.invoke("closeWindow")
      } 
    })
  }
}

export function quickloginwxUser ({commit, state}, data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  console.log(self)
  self.$http.post(serverAddress + '/api/quickloginwxUser', data.info)
  // self.$http.post(g.wechatServerAddress + '/api/auth', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      self.$store.state.isloading = false;
      console.log(response)
      data.callback(self,response)
      // if(response.body.err){
      //   self.$router.push('/useWX')
      // }else{
      //   if (data.callback) {
      //     data.callback(self,response)
      //   }
      // }       
    }, (response) => {
      // error callback
      self.$f7.alert('登录失败') 
      onErrorRefresh(self);
    });
}

export function getWeightInfo ({commit, state}, data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  console.log(self)
  self.$http.post(serverAddress + '/api/getWeightInfo', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
        // self.$router.push('/useWX')
      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      onErrorRefresh(self);
    });
}

export function fillWeight ({commit, state}, data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  console.log(self)
  self.$http.post(serverAddress + '/api/userFillWeight', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)

      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      onErrorRefresh(self);
    });
}

//取体重图表数据
export function getWeightChart ({commit, state}, data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  console.log(self)
  self.$http.post(serverAddress + '/api/getWeightChart', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)

      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      onErrorRefresh(self);
    });
}

//取体重记录
export function getWeightData ({commit, state}, data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  console.log(self)
  self.$http.post(serverAddress + '/api/getWeightData', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)

      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      onErrorRefresh(self);
    });
}

//保存用户资料
export function updateInfo ({commit, state}, data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  console.log(self)
  self.$http.post(serverAddress + '/api/updateInfo', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)

      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      onErrorRefresh(self);
    });
}

//取用户足部报告
export function getFootRecord ({commit, state}, data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(serverAddress + '/api/getreport', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)

      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      onErrorRefresh(self);
    });
}

export function getHomeData ({commit, state},data) {
  var self = data.self;
  var next = data.callback;
  self.http.get(serverAddress+'/api/mainpage')
    .then((response) => {
      // success callback
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
      } else {
        if (next) {
          commit("SWIPEDATA",response.body.swipe);
          commit("PRODUCTDATA",response.body.product);
          next();
        }
      }
    }, (response) => {
      // error callback
      onErrorRefresh(self);
      // self.$f7.alert("error occur","error")
    });
}

export function getProductDetail ({commit, state},data) {
  var self = data.self;
  // data.info.token = Global.s.state.token  //带上token
  var next = data.callback;
  console.log(serverAddress);
  self.http.post(serverAddress + '/api/getProductDetail', data.info)
    .then((response) => {
      // success callback
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)

      }else{
        if (next) {
          commit("PRODUCTDETAIL",response.body.ok);
          next();
        }
      }       
    }, (response) => {
      // error callback
      onErrorRefresh(self);
    });
}

export function ordermake ({commit, state}, data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(serverAddress + '/api/ordermake', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)

      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      onErrorRefresh(self);
    });
}

export function orderpay ({commit, state}, data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(serverAddress + '/api/orderpay', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)

      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      onErrorRefresh(self);
    });
}



//取订单
export function orderlistUser ({commit, state}, data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(serverAddress + '/api/orderlistUser', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)

      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      onErrorRefresh(self);
    });
}

//取消订单
export function ordercancel ({commit, state}, data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(serverAddress + '/api/ordercancel', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)

      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      onErrorRefresh(self);
    });
}

//取物流信息
export function getLogistics ({commit, state}, data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(g.adminServerAddress + '/api/getExpInfo', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)

      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      onErrorRefresh(self);
    });
}
//微信sdk认证
export function signature ({commit, state},data) {
  var self = data.self;
  self.http.post(g.wechatServerAddress+'/api/signature',data.info)
    .then((response) => {
      // success callback
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
        // self.$f7.alert('',response.body.err)
      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}

//取体检日期配置
export function getCheckCycle ({commit, state},data) {
  var self = data.self;
  self.$http.get(g.serverAddress+'/api/getCheckCycle')
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
        // self.$f7.alert('',response.body.err)
      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}

//取二维码
export function getQRCode ({commit, state},data) {
  var self = data.self;
  self.$http.post(g.wechatServerAddress+'/api/getqrcode', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
        // self.$f7.alert('',response.body.err)
      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}

//取推广的订单
export function orderlistReference({commit, state},data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(g.serverAddress+'/api/orderlistReference', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
        // self.$f7.alert('',response.body.err)
      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}

//绑定推广关系
export function tglink({commit, state},data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(g.serverAddress+'/api/tglink', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
        // self.$f7.alert('',response.body.err)
      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}

//取超级验证吗
export function getBossQrcode({commit, state},data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(g.serverAddress+'/api/getBossQrcode', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
        // self.$f7.alert('',response.body.err)
      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}
//超级验证吗
export function useBossQrcode({commit, state},data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(g.serverAddress+'/api/useBossQrcode', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
        // self.$f7.alert('',response.body.err)
      }
        if (data.callback) {
          data.callback(self,response)
        }
             
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}

//取我推广的代言人
export function getSalemen({commit, state},data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(g.serverAddress+'/api/getSalemen', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
        // self.$f7.alert('',response.body.err)
      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}


//填写推广人资料
export function getRequestStatus({commit, state},data) {
  var self = data.self;
  data.info.token = Global.s.state.token  //带上token
  self.$http.post(g.serverAddress+'/api/getRequestStatus', data.info)
    .then((response) => {
      // success callback
      Global.v.$f7.hidePreloader()
      console.log(response)
      if (data.callback) {
        data.callback(self,response)
      }
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}

//填写推广人资料
export function tgFillInfo({commit, state},data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(g.serverAddress+'/api/tgFillInfo', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if (data.callback) {
        data.callback(self,response)
      }
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}

//变更上线
export function changeBoss({commit, state},data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(g.serverAddress+'/api/changeBoss', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if (data.callback) {
        data.callback(self,response)
      }
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}

//申请成为推广人
export function joinRequest({commit, state},data) {
  var self = data.self;
  self.$http.post(g.serverAddress+'/api/joinRequest', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if (data.callback) {
        data.callback(self,response)
      } 
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}

//申请成为推广人
export function getRequestList({commit, state},data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(g.serverAddress+'/api/getRequestList', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
        // self.$f7.alert('',response.body.err)
      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}


//
export function acceptRequest({commit, state},data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(g.serverAddress+'/api/acceptRequest', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
        // self.$f7.alert('',response.body.err)
      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}

//拒绝申请
export function refuseRequest({commit, state},data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(g.serverAddress+'/api/refuseRequest', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
        // self.$f7.alert('',response.body.err)
      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}

//取推广人数据
export function getSalemanData({commit, state},data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(g.serverAddress+'/api/getSalemanData', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
        // self.$f7.alert('',response.body.err)
      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}


//取收入数据
export function getIncomeDetails({commit, state},data) {
  var self = data.self;
  data.info.token = self.$store.state.token  //带上token
  self.$http.post(g.serverAddress+'/api/getIncomeDetails', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        onErrorHandler(response.body.err)
        // self.$f7.alert('',response.body.err)
      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      // onErrorRefresh(self);
    });
}