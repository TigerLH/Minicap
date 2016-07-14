package com.hcb.screensync.utils;

import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.hcb.screensync.logger.Log4jConfigurator;

/** 
 * @author  linhong: 
 * @date 2016年6月14日 下午7:26:19 
 * @Description: TODO
 * @version 1.0  
 */
public class SystemOutPrintStream extends PrintStream {
	private Logger logger;
	private String filename = "HcbUiAutoMator.log";
	public SystemOutPrintStream(OutputStream out) {
			super(out);
			Log4jConfigurator log4jConfigurator = new Log4jConfigurator();  
			log4jConfigurator.setFileName(filename);
			log4jConfigurator.configure();
			logger = Logger.getLogger("HCBLOG");
	}
	
	  
    public void write(byte[] buf, int off, int len) {  
         final String message = new String(buf, off, len);   
         SwingUtilities.invokeLater(new Runnable(){
        	 public void run(){
        		 logger.debug(message);
        	 }
         });
    }
	
}
