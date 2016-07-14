package com.hcb.screensync.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.hcb.screensync.bean.ProfermanceInfo;

/** 
 * @author  linhong: 
 * @date 2016年6月12日 下午5:17:07 
 * @Description: TODO
 * @version 1.0  
 */
public class PerformanceServer implements Runnable {
	private IDevice device = null;
	private String packageName = "";
	private String meminfoUsedCommand = "dumpsys meminfo %s|grep TOTAL";
	private String memFreeCommand = "cat /proc/meminfo|grep MemFree";
	private String getUidCommand = "dumpsys package|grep packageSetting|grep %s/";
	private String netinfoCommand = "cat /proc/uid_stat/%s/tcp_rcv";
	private String cpuInfoCommand = "top -n 1|grep %s";
	private List<ProfermanceInfo> list = new ArrayList<ProfermanceInfo>();
	public PerformanceServer(IDevice device,String packageName){
		this.device = device;
		this.packageName = packageName;
	}
	
	
	public List<ProfermanceInfo> getResultList(){
		return this.list;
	}
	
	@Override
	public void run() {
		String uid = _getUid();
		if(uid==null){
			System.err.println("UID获取失败,请检查配置文件中设置的包名是否已安装");
			System.err.println("性能采集服务启动失败");
			return;
		}
		int begin = _getNetInfoBegin(uid);
		while(true){
			ProfermanceInfo pcinfo = new ProfermanceInfo();
			int netUsed = this.getNetUsed(uid,begin);
			int memUsed = this.getMemUsed();
			int memfree = this.getMemFree();
			int cpuUsed = this.getCpuUsed();
			pcinfo.setMemFree(memfree);
			pcinfo.setNetUsed(netUsed);
			pcinfo.setMemUsed(memUsed);
			pcinfo.setCpuUsed(cpuUsed);
			list.add(pcinfo);
			System.out.println(pcinfo.toString());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * 获取程序CPU利用率
	 */
	public int getCpuUsed(){
		CollectingOutputReceiver output = new CollectingOutputReceiver();
		String command = String.format(cpuInfoCommand, packageName);
		try {
			device.executeShellCommand(command, output,0);
		} catch (TimeoutException | AdbCommandRejectedException
				| ShellCommandUnresponsiveException | IOException e) {
			e.printStackTrace();
		}
		String result = output.getOutput().trim();
		
		if("".equals(result)){
			return 0;
		}
		String[] array = result.split("\\s+");
		return Integer.parseInt(array[2].replace("%", ""));
	}
	
	
	
	/**
	 * 获取程序使用内存
	 * @return
	 */
	public int getMemUsed(){
		CollectingOutputReceiver output = new CollectingOutputReceiver();
		String memUsed = String.format(meminfoUsedCommand, packageName);
		try {
			device.executeShellCommand(memUsed, output,0);
		} catch (TimeoutException | AdbCommandRejectedException
				| ShellCommandUnresponsiveException | IOException e) {
			e.printStackTrace();
		}
		String result = output.getOutput().trim();
		if("".equals(result)){
			return 0;
		}
		String[] array = result.split("\\s+");//结果中数据之间包含多个空格
		return Integer.parseInt(array[6])/1024;
	}
	
	
	/**
	 * 获取系统剩余内存
	 * @return
	 */
	public int getMemFree(){
		CollectingOutputReceiver output = new CollectingOutputReceiver();
		String memFree = String.format(memFreeCommand, packageName);
		try {
			device.executeShellCommand(memFree, output,0);
		} catch (TimeoutException | AdbCommandRejectedException
				| ShellCommandUnresponsiveException | IOException e) {
			e.printStackTrace();
		}
		String result = output.getOutput().trim();
		String[] array = result.split("\\s+");//结果中数据之间包含多个空格
		return Integer.parseInt(array[1])/1024;
	}
	
	
	/**
	 * 获取程序使用的流量
	 * @return
	 */
	public int getNetUsed(String uid,int begin){
		CollectingOutputReceiver output = new CollectingOutputReceiver();
		String command = String.format(netinfoCommand, uid);
		try {
			device.executeShellCommand(command, output,0);
		} catch (TimeoutException | AdbCommandRejectedException
				| ShellCommandUnresponsiveException | IOException e) {
			e.printStackTrace();
		}
		String result = output.getOutput().trim();
		return Integer.parseInt(result)/1024-begin;
	}
	
	
	
	
	
	/**
	 * 获取程序uid
	 * @return
	 * @throws Exception 
	 */
	private String _getUid(){
		CollectingOutputReceiver output = new CollectingOutputReceiver();
		String command = String.format(getUidCommand, packageName);
		try {
			device.executeShellCommand(command, output,0);
		} catch (TimeoutException | AdbCommandRejectedException
				| ShellCommandUnresponsiveException | IOException e) {
			e.printStackTrace();
		}
		String result = output.getOutput().trim();
		if(("").equals(result)){
			return null;
		}
		String[] array = result.split("/");//结果中数据之间包含多个空格
		return array[1].replace("}", "").trim();
	}
	
	
	/**
	 * 获取初始使用的流量
	 * @return
	 */
	private int _getNetInfoBegin(String uid){
		CollectingOutputReceiver output = new CollectingOutputReceiver();
		String command = String.format(netinfoCommand, uid);
		try {
			device.executeShellCommand(command, output,0);
		} catch (TimeoutException | AdbCommandRejectedException
				| ShellCommandUnresponsiveException | IOException e) {
			e.printStackTrace();
		}
		String result = output.getOutput().trim();
		return Integer.parseInt(result)/1024;
	}
	
	
}
