package com.hcb.screensync.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/** 
 * @author  linhong: 
 * @date 2016年6月6日 上午9:58:39 
 * @Description:Logcat tool
 * @version 1.0  
 */
public class LogcatServer implements Runnable {
	private String device;
	private String filter;
	private String msgpath = "TestResult"+File.separator+"msg.txt";
	private String errorpath = "TestResult"+File.separator+"error.txt";
	public LogcatServer(String device,String filter){
		this.device = device;
		this.filter = filter;
	}
	
	@Override
	public void run() {
		 System.out.println("Thread for record log is Starting");
		 System.out.println("device:"+device);
		 System.out.println("filter:"+filter);
		 File dir = new File("TestResult");
		 if(!dir.exists()){
			 dir.mkdir();
		 }
		 StringBuilder successMsg = new StringBuilder();
		 StringBuilder errorMsg = new StringBuilder();
		 String[] cmd = new String[3];
		 String os = System.getProperty("os.name");
		 if (os != null && os.startsWith("Windows")){  
			 cmd[0] = "cmd.exe";
			 cmd[1] = "/c";
			 if(filter==null){
				 cmd[2] = "adb -s "+device+" logcat -v time 1>"+msgpath+" 2>"+errorpath;
			 }else{
				 cmd[2] = "adb -s "+device+" logcat -v time|findstr "+filter+" 1>"+msgpath+" 2>"+errorpath;
			 }
		 }else{
			 cmd[0] = "bash";
			 cmd[1] = "-c";
			 if(filter==null){
				 cmd[2] = "adb -s "+device+" logcat -v time 1>"+msgpath+" 2>"+errorpath;
			 }else{
				cmd[2] =  "adb -s "+device+" logcat -v time|grep "+filter+" 1>"+msgpath+" 2>"+errorpath;
			 }
		 }
		 InputStream is1 = null; 
		 InputStream is2 = null;
		 BufferedReader successResult = null;
		 BufferedReader errorResult = null;
		 try{
			 Process p = Runtime.getRuntime().exec(cmd);
			 p.waitFor();
			 is1 = p.getInputStream();
			 is2 = p.getErrorStream();
			 successResult = new  BufferedReader(new  InputStreamReader(is1));   
			 errorResult = new  BufferedReader(new  InputStreamReader(is2));   
             String line1;  
             while ((line1 = successResult.readLine()) !=  null ) {
            			  successMsg.append(line1);
            	}
            
             while ((line1 = errorResult.readLine()) !=  null ) {
            	 errorMsg.append(line1);
        	 }
	 			} catch (IOException | InterruptedException e) {  
	              e.printStackTrace();  
	 			}
	}

}
