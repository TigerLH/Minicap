package com.hcb.screensync.uiautomator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import android.util.Log;

import com.hcb.screensync.bean.SendCommand;
import com.hcb.screensync.observer.ScriptPanelObserver;
import com.hcb.screensync.observer.ScriptPanelSubject;
import com.hcb.screensync.utils.Command;
import com.hcb.screensync.utils.Command.CommandResult;
import com.hcb.screensync.utils.Logger;

/**
 * 读取手机端的返回
 * @author Linhong
 *
 */
public class SocketClient  implements ScriptPanelSubject{
	private BufferedReader in = null;
	private BufferedWriter out = null;
	private WorkerThread m_ClientThread = null;
	private Socket m_ClientSocket = null;
	private StringBuilder result = new StringBuilder();
	private int server_port;
	private String FORWARD_COMMAND = "adb forward tcp:6666 tcp:6666";
	private static SocketClient socketClient = null;
	private List<ScriptPanelObserver> observers = new ArrayList<ScriptPanelObserver>();
	private SocketClient(int server_port){
		this.server_port = server_port;
	}
	


	public static SocketClient getInstance(){
		if(socketClient == null) {    
			synchronized (SocketClient.class) {    
				if (socketClient == null) {
					socketClient = new SocketClient(6666);   
				}    
			}    
		}    
		return socketClient;  
	}
	
	
	public static void main(String[] args) throws Exception {
		UiAutoMatorUtil ui = new UiAutoMatorUtil();
		ui.startUiAutoMatorServer();
		SocketClient socketClient = new SocketClient(6666);
		try {
			socketClient.Forward();
			socketClient.ConnectServer();
			socketClient.FindElement(300, 200);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	/**
	 * 端口转发
	 * @throws Exception
	 */
	public void Forward() throws Exception{
		Command command = new Command();
		CommandResult cr = command.executeCommand(FORWARD_COMMAND, null);
		if(cr.result!=0){
			throw new Exception("socket server forward failed");
		}
	}
	
	
	/**
	 * 连接服务
	 * @throws Exception 
	 */
	public void ConnectServer() throws Exception{
		try {
			m_ClientSocket = new Socket("localhost",server_port);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "SocketClient Connect Failed", "错误", JOptionPane.CLOSED_OPTION);
			throw new Exception("Connect Failed");
		}
		m_ClientThread = new WorkerThread(m_ClientSocket);
		m_ClientThread.start();
		Logger.debug("SocketClient Start Sucess:"+server_port);
	}
	
	
	/**
	 * 断开连接
	 */
	public void DisConnecterver(){
		if(m_ClientThread!=null){
			m_ClientThread.cancle();
		}
	}
	
	
	/**
	 * 找到控件
	 * @param x
	 * @param y
	 * @throws Exception 
	 */
	public synchronized void FindElement(int x,int y) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		map.put("x", String.valueOf(x));
		map.put("y", String.valueOf(y));
		SendCommand command = new SendCommand();
		command.setAction("Find");
		command.setParams(map);
		SendCommand(command);
	}
	
	
	/**
	 * 找到控件
	 * @param x
	 * @param y
	 * @throws Exception 
	 */
	public synchronized void SetText(SendCommand command) throws Exception{
		SendCommand(command);
	}
	
	
	
	
	/**
	 * 发送指令
	 * @param command
	 * @throws Exception
	 */
	public void SendCommand(SendCommand command) throws Exception {
		  String json = command.toString();
	      Logger.debug("Send Command:" + json);
	      try {
	        out.write(json);
	        out.flush();
	      } catch (final IOException e) {
	    	  throw new Exception("Error processing send command("
	          + e.toString() + ")");  
	      }
	}
	
	
	private class WorkerThread extends Thread{
		private Socket ClientSocket ;
		
		public WorkerThread(Socket socket){
			this.ClientSocket = socket;
		}
		
		@Override
		public State getState() {
			return super.getState();
		}

		@Override
		public void run() {
			super.run();
				if(ClientSocket.isConnected()){
					try {
						in = new BufferedReader(new InputStreamReader( ClientSocket.getInputStream(), "UTF-8"));
						out = new BufferedWriter(new OutputStreamWriter(ClientSocket.getOutputStream(), "UTF-8"));
						while(true){
							result.setLength(0);//清空
							int a;
							if(in!=null){
							    while ((a = in .read()) != -1 && in.ready()) {
							    	result.append((char) a);
							      }
							}
							
							if(("").equals(result.toString())){
								   continue;
							  }else{
								  notifyObservers(result.toString()+"}");
							  }
						}
					}
					 catch (IOException e) {
						Logger.debug("Remote SocketServer is Closed");
						e.printStackTrace();
					}
				}
		}
				

		@Override
		public synchronized void start() {
			super.start();
		}
		
		public void cancle(){
			if(m_ClientSocket != null)
				try {
					m_ClientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.v("SocketServer","Close client socket error " + e.getLocalizedMessage());
					e.printStackTrace();
				}
		}
	}


	@Override
	public void registerObserver(ScriptPanelObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(ScriptPanelObserver o) {
		int index = observers.indexOf(o);
		if (index != -1) {
			observers.remove(o);
		}
	}

	@Override
	public void notifyObservers(String result) {
		for (ScriptPanelObserver observer : observers) {
			observer.onElementFound(result);
		}
	}

}
