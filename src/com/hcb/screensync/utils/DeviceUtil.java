package com.hcb.screensync.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.hcb.screensync.constansts.ConnectModule;
import com.hcb.screensync.utils.Command.CommandResult;

/** 
 * @author  linhong: 
 * @date 2016年6月23日 下午8:21:20 
 * @Description: TODO
 * @version 1.0  
 */
public class DeviceUtil {
	private static Command command = new Command();
	private final static String USB_MODULE = "adb usb";
	private final static String WIFI_MODULE = "adb tcpip 5555";
	private final static String WIFI_ADDRESS = "adb shell netcfg";
	private final static String WIFI_CONNECT = "adb connect %s";
	private final static String KILL_ADB = "adb kill-server";
	private final static String START_ADB = "adb start-server";
	
	
	public void startDeviceModule(String module){
		command.executeCommand(KILL_ADB,null);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		command.executeCommand(START_ADB,null);
		if(module.equals(ConnectModule.USB_MODULE.getCode().toString())){		//USB模式
//			CommandResult cr = command.executeCommand(USB_MODULE,null);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
//			if(cr.result!=0){
//				JOptionPane.showMessageDialog(null, "Usb模式切换失败,请检查连接后重试", "错误", JOptionPane.CLOSED_OPTION);
//			}
		}else if(module.equals(ConnectModule.WIFI_MODULE.getCode())){
			CommandResult wifimodule = command.executeCommand(WIFI_MODULE,null);	//切换到Wifi模式
			try {
				Thread.sleep(5000);					//切换模式后需要等待3秒钟
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(wifimodule.result==0){
				String wifi_address = getWifiAddress();
				if("".equals(wifi_address)){	//wifi地址获取失败
					return;
				}
				String c = String.format(WIFI_CONNECT, wifi_address);
				CommandResult connect = command.executeCommand(c, null);
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				if(connect.result!=0){
					JOptionPane.showMessageDialog(null, "Wifi模式连接失败", "错误", JOptionPane.CLOSED_OPTION);
				}
			}else{
				JOptionPane.showMessageDialog(null, "Wifi模式切换失败,请检查连接", "错误", JOptionPane.CLOSED_OPTION);
			}
		}
	}
	
	
	
	/**
	 * 获取WIFI地址
	 * @return
	 */
	public String getWifiAddress(){
		String result = "";
		CommandResult cr = command.executeCommand(WIFI_ADDRESS,"wlan0");
		if(cr.result==0){
			 result = cr.successMsg;
		}else{
			JOptionPane.showMessageDialog(null, "Wifi地址获取失败", "提示", JOptionPane.CLOSED_OPTION);
			return result;
		}
		String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
	    Pattern p = Pattern.compile(regex);  
	    Matcher m = p.matcher(result);  
	    while (m.find()) {  
	        result = m.group(0); 
	    }
	    Logger.debug("Get Mobile Wifi Address:"+result);
		return result;
	}
	
	
}
