package com.hcb.screensync.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.hcb.screensync.bean.TargetElement;
import com.hcb.screensync.constansts.ConnectModule;

/** 
 * @author  linhong: 
 * @date 2016年6月1日 下午8:53:15 
 * @Description: TODO
 * @version 1.0  
 */
public class Command {
	private final static String FIND_ELEMENT = "adb shell uiautomator runtest UiAutoMatorServer.jar -e element %sx%s -c com.hcb.uiautomator.dump.Recorder";
	public Command(){
		
	}
	
	/**
	 * 根据坐标查找控件,并获取唯一的定位方式
	 * @param x
	 * @param y
	 */
	public TargetElement findElementByCoordinate(int x,int y){
		String element = "";
		TargetElement targetElement = new TargetElement();
		String command = String.format(FIND_ELEMENT, x,y);
		CommandResult commandResult = executeCommand(command, "FINDELEMENT");
		if(!(commandResult.result==0)){
			return null;
		}
		Pattern pattern = Pattern.compile("\\[.*\\]"); 
		String result = commandResult.successMsg;
		Matcher matcher = pattern.matcher(result);
		if(matcher.find()){
			element = matcher.group(0).replace("[", "").replace("]", "").trim();
		}
		String[] attribute= element.split("&");
		for(String e:attribute){
			String[] array = e.trim().split("=");
			String key = array[0];
			switch (key){
				case "index":
					if(array.length>1){
						targetElement.setIndex(Integer.parseInt(array[1]));
					}
					break;
				case "text":
					if(array.length>1){
						targetElement.setText(array[1]);
					}
					break;
				case "className":
					if(array.length>1){
						targetElement.setClassName(array[1]);
					}
					break;
				case "pkg":
					if(array.length>1){
						targetElement.setPkg(array[1]);
					}
					break;
				case "contentdesc":
					if(array.length>1){
						targetElement.setContentdesc(array[1]);
					}
					break;
				case "resourceId":
					if(array.length>1){
						targetElement.setResourceId(array[1]);
					}
					break;
				case "unique":
					if(array.length>1){
						targetElement.setUnique(array[1]);
					}
					break;
			}		
		}
		return targetElement;
	}
	
	
	
	
	
	
	
	public  CommandResult executeCommand(String command,String filter){
		 Logger.debug("EXECUTE COMMAND:"+command);
		 int result = -1;
		 StringBuilder successMsg = new StringBuilder();
		 StringBuilder errorMsg = new StringBuilder();
		 String[] cmd = new String[3];
		 String os = System.getProperty("os.name");  
		 if (os != null && os.startsWith("Windows")){  
			 cmd[0] = "cmd.exe";
			 cmd[1] = "/c";
			 cmd[2] = command;
		 }else{
			 cmd[0] = "bash";
			 cmd[1] = "-c";
			 cmd[2] = command;
		 }
		 ProcessBuilder builder = new ProcessBuilder();
		 builder.command(cmd);
		 InputStream is1 = null;
		 InputStream is2 = null;
		 BufferedReader successResult = null;
		 BufferedReader errorResult = null;
		 Process p = null;
		 try{
			 p = builder.start();
			 is1 = p.getInputStream();
			 is2 = p.getErrorStream();
			 successResult = new  BufferedReader(new  InputStreamReader(is1,"UTF-8"));   
			 errorResult = new  BufferedReader(new  InputStreamReader(is2,"UTF-8"));   
             String line1;  
             while ((line1 = successResult.readLine()) !=  null ) {
            	  if(filter == null){
            		  successMsg.append(line1);
            	  }else{
            		  if(line1.contains(filter)){
            			  successMsg.append(line1);
            		  }
            	  }
            	}
            
             while ((line1 = errorResult.readLine()) !=  null ) {
            	 errorMsg.append(line1);
        	 }
             result = p.waitFor();
	 			} catch (IOException | InterruptedException e) {  
	              e.printStackTrace();  
	 			}finally{
	 				try {
	 					is1.close();
	 					is2.close();
	 					if(successResult != null) {
	 	                    successResult.close();
	 	                }
	 	                if (errorResult != null) {
	 	                    errorResult.close();
	 	                }
	 	              	p.destroy();
	 				} catch (IOException e) {
					e.printStackTrace();
				}
	         }
		 return new CommandResult(result, successMsg == null ? null : successMsg.toString(), errorMsg == null ? null
	                : errorMsg.toString());
	}
	
	
	
	 public  static class CommandResult {

	        /** result of command **/
	        public int  result;
	        /** success message of command result **/
	        public String successMsg;
	        /** error message of command result **/
	        public String errorMsg;
	        
	        public CommandResult(int result){
	        	this.result = result;
	        }
	        
			public CommandResult(int result, String successMsg, String errorMsg) {
	            this.result = result;
	            this.successMsg = successMsg;
	            this.errorMsg = errorMsg;
	        }
	 }
	
}
