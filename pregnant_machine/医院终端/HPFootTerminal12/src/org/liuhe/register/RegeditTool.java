package org.liuhe.register;

public class RegeditTool {				//注册机代码：根据本机的MAC地址和注册名生成注册码
	
	private static String convert(String s) {
        if (s == null || s.length() == 0)
            return s;
        byte abyte0[] = s.getBytes();
        char ac[] = new char[s.length()];
        int i = 0;
        for (int k = abyte0.length; i < k; i++) {
            int j = abyte0[i];
            if (j >= 48 && j <= 57)
                j = ((j - 48) + 5) % 10 + 48;
            else if (j >= 65 && j <= 90)
                j = ((j - 65) + 13) % 26 + 65;
            else if (j >= 97 && j <= 122)
                j = ((j - 97) + 13) % 26 + 97;
            ac[i] = (char) j;
        }
        return String.valueOf(ac);
    }
	
    private static int hash(String s) {
        int i = 0;
        char ac[] = s.toCharArray();
        int j = 0;
        for (int k = ac.length; j < k; j++)
            i = 31 * i + ac[j];
        return Math.abs(i);
    }

    public static String getRegister(String reg_name){
    	System.out.println("class reg-name is："+reg_name);
    	System.out.println("class get mac address is："+GetMacAddr.getMacAddress());
    	try {
            String licStr = "YE3MP-"+GetMacAddr.getMacAddress()+"-";
            String h = reg_name.substring(0, 1)
                     + licStr
                     + "Decompiling this copyrighted software is a violation of both your license agreement and the Digital Millenium Copyright Act of 1998" 
                     + " (http://www.loc.gov/copyright/legislation/dmca.pdf). Under section 1204 of the DMCA, penalties range up to a $500,000 fine or up" 
                     + " to five years imprisonment for a first offense. Think about it; pay for a license, avoid prosecution, and feel better about yourself."
                     + reg_name;
            int j = hash(h);
            String lic = reg_name.substring(0, 1) + licStr + Integer.toString(j);
            String reg_code= convert(lic);
            System.out.println("License Key : "+reg_name+"\nLicense Code : " +reg_code);
            return reg_code;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

}
