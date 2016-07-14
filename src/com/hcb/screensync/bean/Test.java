package com.hcb.screensync.bean;

import java.util.HashMap;
import java.util.Map;

import com.hcb.screensync.uiautomator.SocketClient;

/** 
 * @author  linhong: 
 * @date 2016年6月23日 下午3:08:47 
 * @Description: TODO
 * @version 1.0  
 */
public class Test {
	public static void main(String[] args) throws Exception {
		Map<String,String> map = new HashMap<String, String>();
		map.put("x", "180");
		map.put("y", "520");
		SendCommand s = new SendCommand();
		s.setAction("Find");
		s.setUnique("ID");
		s.setParams(map);
		
		SocketClient sc = SocketClient.getInstance();
		sc.ConnectServer();
		Thread.sleep(5000);
		try {
			sc.SendCommand(s);
			Thread.sleep(5000);
			sc.SendCommand(s);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
