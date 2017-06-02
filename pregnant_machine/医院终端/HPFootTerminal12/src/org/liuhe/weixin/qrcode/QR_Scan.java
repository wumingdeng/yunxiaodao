package org.liuhe.weixin.qrcode;

//临时的二维码请求说明{"expire_seconds": 604800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
public class QR_Scan {
	private int expire_seconds;
	private String action_name = "QR_SCENE";
	private Action_info action_info;
	public int getExpire_seconds() {
		return expire_seconds;
	}
	public void setExpire_seconds(int expire_seconds) {
		this.expire_seconds = expire_seconds;
	}
	public String getAction_name() {
		return action_name;
	}
	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}
	public Action_info getAction_info() {
		return action_info;
	}
	public void setAction_info(Action_info action_info) {
		this.action_info = action_info;
	}
	
}
