package com.hcb.screensync.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.hcb.screensync.constansts.ConnectModule;
import com.hcb.screensync.utils.Command.CommandResult;

/** 
 * @author  linhong: 
 * @date 2016��6��23�� ����8:21:20 
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		command.executeCommand(START_ADB,null);
		if(module.equals(ConnectModule.USB_MODULE.getCode().toString())){		//USBģʽ
//			CommandResult cr = command.executeCommand(USB_MODULE,null);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
//			if(cr.result!=0){
//				JOptionPane.showMessageDialog(null, "Usbģʽ�л�ʧ��,�������Ӻ�����", "����", JOptionPane.CLOSED_OPTION);
//			}
		}else if(module.equals(ConnectModule.WIFI_MODULE.getCode())){
			CommandResult wifimodule = command.executeCommand(WIFI_MODULE,null);	//�л���Wifiģʽ
			try {
				Thread.sleep(5000);					//�л�ģʽ����Ҫ�ȴ�3����
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(wifimodule.result==0){
				String wifi_address = getWifiAddress();
				if("".equals(wifi_address)){	//wifi��ַ��ȡʧ��
					return;
				}
				String c = String.format(WIFI_CONNECT, wifi_address);
				CommandResult connect = command.executeCommand(c, null);
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				if(connect.result!=0){
					JOptionPane.showMessageDialog(null, "Wifiģʽ����ʧ��", "����", JOptionPane.CLOSED_OPTION);
				}
			}else{
				JOptionPane.showMessageDialog(null, "Wifiģʽ�л�ʧ��,��������", "����", JOptionPane.CLOSED_OPTION);
			}
		}
	}
	
	
	
	/**
	 * ��ȡWIFI��ַ
	 * @return
	 */
	public String getWifiAddress(){
		String result = "";
		CommandResult cr = command.executeCommand(WIFI_ADDRESS,"wlan0");
		if(cr.result==0){
			 result = cr.successMsg;
		}else{
			JOptionPane.showMessageDialog(null, "Wifi��ַ��ȡʧ��", "��ʾ", JOptionPane.CLOSED_OPTION);
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
