package com.synjones.angel;

import com.sun.jna.Structure;

public class IDCardData extends Structure {
	// 注意,不能是char[] name;否则会乱码!转都转不了!
	public byte[] Name; 
	public byte[] Sex;
	public byte[] Nation;
	public byte[] Born;
	public byte[] Address;
	public byte[] IDCardNo;
	public byte[] GrantDept;
	public byte[] UserLifeBegin;
	public byte[] UserLifeEnd;
	public byte[] reserved;
	public byte[] PhotoFileName;

	public IDCardData() {
		Name = new byte[32];
		Sex = new byte[6];
		Nation = new byte[20];
		Born = new byte[18];
		Address = new byte[72];
		IDCardNo = new byte[38];
		GrantDept = new byte[32];
		UserLifeBegin = new byte[18];
		UserLifeEnd = new byte[18];
		reserved = new byte[38];
		PhotoFileName = new byte[255];
	}
}