package com.hcb.screensync.main;

import java.io.IOException;

import javax.swing.JLabel;

import com.android.chimpchat.ChimpManager;
import com.android.chimpchat.adb.AdbChimpDevice;
import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.IChimpImage;
import com.android.ddmlib.IDevice;

public class OperateAndroid {
	private AdbChimpDevice device;
	private ChimpManager manager;
	public OperateAndroid(IDevice dev) {
		try{
			device = new AdbChimpDevice(dev);
			manager = device.getManager();
		}catch(Exception e){
			
		}
	}
	
	
	
	public IChimpImage getImage(){
		return device.takeSnapshot();
	}
	

	
	public void drag(int startX, int startY, int endX, int endY, int time,
			int step) {
		device.drag(startX, startY, endX, endY, time, step);
	}

	public void type(char c) {
		device.type(Character.toString(c));
	}

	public void touchDown(int x, int y) throws Exception {
		manager.touchDown(x, y);
	}

	public void touchUp(int x, int y) throws Exception {
		manager.touchUp(x, y);
	}

	public void touchMove(int x, int y) throws Exception {
		manager.touchMove(x, y);
	}

	public void press(String arg0){
		try {
			manager.press(arg0);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
	public int getScreenWidth() {
		System.out.println("width:"+device.getProperty("display.width"));
		return Integer.parseInt(device.getProperty("display.width"));
	}

	public int getScreenHeight() {
		System.out.println("height:"+device.getProperty("display.height"));
		return Integer.parseInt(device.getProperty("display.height"));
	}
	
	/**
	 * 唤醒屏幕
	 */
	public void wake(){
		device.wake();
	}
	

}
