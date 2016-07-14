package com.hcb.screensync.device;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.hcb.screensync.constansts.ConnectModule;


public class ADB {
	private String adbLocation = null;
	public static boolean hasInitAdb = false;
	private String module = "";
	
	public ADB(String module){
		this.module = module;
	}
	
	
	
	public IDevice getDevice() throws Exception {
		IDevice targetDevice = null;
		adbLocation = "adb";
		AndroidDebugBridge bridge = null;
		try{
			if(AndroidDebugBridge.getSocketAddress()==null){
				AndroidDebugBridge.init(false);
			}
			bridge = AndroidDebugBridge.createBridge(adbLocation, true);//不能加到socketAddress判断中否则重连不会成功(getSocketAddress不会清空)
		}catch(Exception e){
			throw new Exception("AndroidDebugBridge create failed");
		}
		int count = 0;
		while (!bridge.hasInitialDeviceList()) {
		try {
			Thread.sleep(100L);
			++count;
		  }catch (InterruptedException localInterruptedException) {
			  System.err.println(localInterruptedException.getMessage());
		      }
		  if (count > 500){
		       System.err.println("Timeout getting device list!");
		       return null;
		     }
		  }
		
		IDevice[] devicelist = bridge.getDevices();
		if(devicelist.length<=0){
			JOptionPane.showMessageDialog(null, "设备未找到,请检查连接", "错误", JOptionPane.CLOSED_OPTION);
			throw new Exception("device not found");
		}
		if(module.equals(ConnectModule.WIFI_MODULE.getCode())){
			for(IDevice device:devicelist){
				String serialNumber = getWifiDevice(device.getSerialNumber());
				if(!("").equals(serialNumber)){
					targetDevice = device;
					break;
				}
			}
		}else{
			targetDevice = devicelist[0];
		}
		return targetDevice;
	}
	
	
	/**
	 * 配置wifi设备
	 * @param str
	 * @return
	 */
	public String getWifiDevice(String str){
		String wifiDevice = "";
		String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:5555";
	    Pattern p = Pattern.compile(regex);  
	    Matcher m = p.matcher(str);  
	    while (m.find()) {  
	    	wifiDevice = m.group(0); 
	    }
	    return wifiDevice;
	}
	
	
	/**
	 * 获取手机屏幕宽和高
	 * @param device
	 * @return
	 */
	public int[] getScreenInfo(IDevice device){
		int[] screen = new int[2];
		String MINICAP_WM_SIZE_COMMAND = "wm size";
		CollectingOutputReceiver output = new CollectingOutputReceiver();
		try {
			device.executeShellCommand(MINICAP_WM_SIZE_COMMAND, output, 0);
		} catch (TimeoutException | AdbCommandRejectedException
				| ShellCommandUnresponsiveException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String size = output.getOutput().split(":")[1].trim();
		screen[0]= Integer.parseInt(size.split("x")[0].trim());
		screen[1]= Integer.parseInt(size.split("x")[1].trim());
		return screen;
	}
}

