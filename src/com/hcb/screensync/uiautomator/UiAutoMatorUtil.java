package com.hcb.screensync.uiautomator;

import com.hcb.screensync.utils.Command;
import com.hcb.screensync.utils.Command.CommandResult;
import com.hcb.screensync.utils.Logger;

/** 
 * @author  linhong: 
 * @date 2016年6月23日 下午8:10:47 
 * @Description: TODO
 * @version 1.0  
 */
public class UiAutoMatorUtil {
	private final static String UIAUTOMATOR_START_COMMAND = "adb shell uiautomator runtest HCBUiAutoMatorServer.jar -c com.hcb.uiautomator.server.TestAgent";
	private final static String UIAUTOMATOR_PUSH_COMMAND = "adb push server/HCBUiAutoMatorServer.jar data/local/tmp/HCBUiAutoMatorServer.jar";
	public UiAutoMatorUtil(){
	}
	
	/**
	 * 启动手机端UiAutoMator服务
	 * @throws Exception 
	 */
	public void startUiAutoMatorServer() throws Exception{
		Logger.debug("PUSH UIAUTOMATOR SERVER");
		final Command command = new Command();
		CommandResult cr = command.executeCommand(UIAUTOMATOR_PUSH_COMMAND, null);
		if(cr.result!=0){
			throw new Exception("UIAUTOMATOR SERVER PUSH FAILED");
		}
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				command.executeCommand(UIAUTOMATOR_START_COMMAND, null);
				
			}
		});
		thread.start();
	}
}
