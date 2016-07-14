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
 * @date 2016��6��12�� ����5:17:07 
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
			System.err.println("UID��ȡʧ��,���������ļ������õİ����Ƿ��Ѱ�װ");
			System.err.println("���ܲɼ���������ʧ��");
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
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * ��ȡ����CPU������
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
	 * ��ȡ����ʹ���ڴ�
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
		String[] array = result.split("\\s+");//���������֮���������ո�
		return Integer.parseInt(array[6])/1024;
	}
	
	
	/**
	 * ��ȡϵͳʣ���ڴ�
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
		String[] array = result.split("\\s+");//���������֮���������ո�
		return Integer.parseInt(array[1])/1024;
	}
	
	
	/**
	 * ��ȡ����ʹ�õ�����
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
	 * ��ȡ����uid
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
		String[] array = result.split("/");//���������֮���������ո�
		return array[1].replace("}", "").trim();
	}
	
	
	/**
	 * ��ȡ��ʼʹ�õ�����
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
