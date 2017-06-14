import cfg from '../../static/webConfig.json'

var authPath = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=' + cfg.appId + '&redirect_uri='+encodeURIComponent(window.location.href)+ '?page=' + localStorage.page + '&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect'

var wxAuth = {    
	isweixin: function() {
    var ua = window.navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i) == 'micromessenger'){
        return true;
    } else {
        return false;
    }
  },
	auth:function() {
		window.location.href = authPath
	}
}

export default wxAuth;