package org.liuhe.weixin.qrcode;

public class QRcodeInfo {
	private String mac_id;
	private String scene;
	private String ticket;
	private String requestUrl;
	private String filePath;
	public String getMac_id() {
		return mac_id;
	}
	public void setMac_id(String macId) {
		mac_id = macId;
	}
	public String getScene() {
		return scene;
	}
	public void setScene(String scene) {
		this.scene = scene;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}