package org.liuhe.weixin.qrcode;

/**
 * ��ʱ��ά����Ϣ
 * @author
 * @date
 */
/*//���ص�json���ݰ�
{
	"ticket":"gQH47joAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL2taZ2Z3TVRtNzJXV1Brb3ZhYmJJAAIEZ23sUwMEmm3sUw==",
	"expire_seconds":60,
	"url":"http:\/\/weixin.qq.com\/q\/kZgfwMTm72WWPkovabbI"
}
 * */
public class WeixinQRCode {
	// ��ȡ�Ķ�ά��ticket
	private String ticket;
	// ��ά�����Чʱ�䣬��λΪ�룬��󲻳���1800
	private int expireSeconds;
	//��ά��ͼƬ������ĵ�ַ�������߿ɸ��ݸõ�ַ����������Ҫ�Ķ�ά��ͼƬ
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
