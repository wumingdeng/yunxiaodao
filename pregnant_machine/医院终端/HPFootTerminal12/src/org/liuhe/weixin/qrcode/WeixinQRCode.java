package org.liuhe.weixin.qrcode;

/**
 * 临时二维码信息
 * @author
 * @date
 */
/*//返回的json数据包
{
	"ticket":"gQH47joAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2taZ2Z3TVRtNzJXV1Brb3ZhYmJJAAIEZ23sUwMEmm3sUw==",
	"expire_seconds":60,
	"url":"http:\/\/weixin.qq.com\/q\/kZgfwMTm72WWPkovabbI"
}
 * */
public class WeixinQRCode {
	// 获取的二维码ticket
	private String ticket;
	// 二维码的有效时间，单位为秒，最大不超过1800
	private int expireSeconds;
	//二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
	private String url;
	
	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getExpireSeconds() {
		return expireSeconds;
	}

	public void setExpireSeconds(int expireSeconds) {
		this.expireSeconds = expireSeconds;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
