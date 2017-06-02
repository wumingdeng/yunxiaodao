package org.liuhe.register;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GetMacAddr {

	//获取本机的MAC物理地址  unused!!!
	/*public static String getMACAddr() {
		NetworkInterface netInterface;
		byte[] macAddr = null;
		try {
			netInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
			macAddr = netInterface.getHardwareAddress();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < macAddr.length; i++) {
			//if(i!=0){ sb.append("-"); }
			String s = Integer.toHexString(macAddr[i] & 0xFF);
			sb.append(s.length() == 1 ? "0" + s : s);
		}
		return sb.toString().toUpperCase();			// format：002421FEA0B5
	}*/
	public static String getMacAddress() {
		String mac = "";
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Windows")) {
			try {
				String command = "cmd.exe /c ipconfig /all";
				Process p = Runtime.getRuntime().exec(command);
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line = "";
				while ((line = br.readLine()) != null) {
					if ((line.indexOf("Physical Address") > 0)
							|| (line.indexOf("物理地址") > 0)) {
						int index = line.indexOf(":") + 2;
						mac = line.substring(index);
						break;
					}
				}
				br.close();
			} catch (IOException e) {
			}
		}
		mac = mac.trim();
		mac = mac.toUpperCase();
		mac = mac.replace("-", "");
		return mac;
	}

	//获取主机名称
	public static void getConfig() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String ip01 = addr.toString(); 					// 获取ip
			String ip02 = addr.getHostAddress();
			String hostName = addr.getHostName(); 			// 获取主机名称
			System.out.println("本机IP01：" + ip01); 		// PC--20140114JJA/192.168.0.108
			System.out.println("本机IP02：" + ip02); 		// 192.168.0.108
			System.out.println("主机名称：" + hostName); 	// PC--20140114JJA
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}