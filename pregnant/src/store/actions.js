// import g from '../globals/global'
import g from '../globals/global'

var serverAddress
if (process.env.NODE_ENV == 'development') {
  serverAddress = g.debugServerAddress;
} else {
  serverAddress = g.serverAddress;
}

function onErrorRefresh(vue,err) {
  vue.$f7.params.modalButtonOk = '刷新'
  vue.$f7.alert('error',err || '刷新重试',()=>{
    // window.location.reload(); 
  })
}
export function quickloginwxUser ({commit, state}, data) {
  var self = data.self;
  console.log(self)
  // self.$http.post(serverAddress + '/api/quickloginwxUser', data.info)
  self.$http.post(g.wechatServerAddress + '/api/auth', data.info)
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
  console.log(self)
  self.$http.post(serverAddress + '/api/getWeightInfo', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
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
  console.log(self)
  self.$http.post(serverAddress + '/api/fillWeight', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){

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
  console.log(self)
  self.$http.post(serverAddress + '/api/getWeightChart', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){

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
  console.log(self)
  self.$http.post(serverAddress + '/api/getWeightData', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){

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
  console.log(self)
  self.$http.post(serverAddress + '/api/updateInfo', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){

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
  self.$http.post(g.machineServerAddress + '/api/getreport', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){

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
        self.$f7.alert('',response.body.err)
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
  var next = data.callback;
  self.http.post(serverAddress + '/api/getProductDetail', data.info)
    .then((response) => {
      // success callback
      console.log(response)
      if(response.body.err){

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
  self.$http.post(serverAddress + '/api/ordermake', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){

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
  self.$http.post(serverAddress + '/api/orderlistUser', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){

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
  self.$http.post(serverAddress + '/api/ordercancel', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){

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
  self.$http.post(g.adminServerAddress + '/api/getExpInfo', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){

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
