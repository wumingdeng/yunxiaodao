// import g from '../globals/global'
import g from '../globals/global'
function onErrorRefresh(vue,err) {
  vue.$f7.params.modalButtonOk = '刷新'
  vue.$f7.alert('error',err || '刷新重试',()=>{
    window.location.reload(); 
  })
}
export function quickloginwxUser ({commit, state}, data) {
  var self = data.self;
  console.log(self)
  self.$http.post(g.debugUrlPrefix + '/api/quickloginwxUser', data.info)
    .then((response) => {
      // success callback
      self.$f7.hidePreloader()
      console.log(response)
      if(response.body.err){
        self.$router.push('/useWX')
      }else{
        if (data.callback) {
          data.callback(self,response)
        }
      }       
    }, (response) => {
      // error callback
      self.$f7.alert('登录失败') 
      onErrorRefresh(self);
    });
}

export function getWeightInfo ({commit, state}, data) {
  var self = data.self;
  console.log(self)
  self.$http.post(g.debugUrlPrefix + '/api/getWeightInfo', data.info)
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
  self.$http.post(g.debugUrlPrefix + '/api/fillWeight', data.info)
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
  self.$http.post(g.debugUrlPrefix + '/api/getWeightChart', data.info)
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
  self.$http.post(g.debugUrlPrefix + '/api/getWeightData', data.info)
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
  self.$http.post(g.debugUrlPrefix + '/api/updateInfo', data.info)
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
  self.$http.post(g.debugUrlPrefix + '/api/getFootRecord', data.info)
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
  self.http.get(g.debugUrlPrefix+'/api/mainpage')
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
  self.http.post(g.debugUrlPrefix + '/api/getProductDetail', data.info)
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
  self.$http.post(g.debugUrlPrefix + '/api/ordermake', data.info)
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
  self.$http.post(g.debugUrlPrefix + '/api/orderlistUser', data.info)
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
