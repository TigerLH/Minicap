package com.hcb.screensync.minitouch;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IDevice.DeviceUnixSocketNamespace;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;
import com.hcb.screensync.bean.TargetElement;
import com.hcb.screensync.utils.Command;
import com.hcb.screensync.utils.Logger;

/** 
 * @author  linhong: 
 * @date 2016年5月26日 下午1:53:53 
 * @Description: TODO
 * @version 1.0  
 */
public class MiniTouchUtil {
	private Banner banner = new Banner();
	private static final int PORT = 1111;
	private Socket socket;
	private OutputStream outputStream = null;
	private int width;
	private int height;
	private String ABI_COMMAND = "ro.product.cpu.abi";
	private final static String CHECK_SOCKET_SERVER = "adb -s %s shell ps";
	private IDevice device;
	public MiniTouchUtil(IDevice device,int width,int height)
    {	
		this.width = width;
		this.height = height;
		this.device = device;
		try {
			init();
		} catch (SyncException | IOException | AdbCommandRejectedException
				| TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void startMoniTouch() throws Exception{
		new Thread(new Runnable(){
			@Override
			public void run() {
				 executeShellCommand("/data/local/tmp/minitouch");	
				}
	    	}).start();
		try {
			ParseBanner();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "MiniTouch初始化失败", "错误", JOptionPane.CLOSED_OPTION);
			throw new Exception("MiniTouch启动异常");
		}
	}
	
	
	
	private void init() throws SyncException, IOException, AdbCommandRejectedException, TimeoutException{
		String abi = device.getProperty(ABI_COMMAND);
		String MINITOUCH_LOCAL_PATH ="minitouch/"+abi+"/minitouch";
		Logger.debug("MINITOUCH_LOCAL_PATH:"+MINITOUCH_LOCAL_PATH);
		device.pushFile(MINITOUCH_LOCAL_PATH,"/data/local/tmp/minitouch");
		executeShellCommand("chmod 777 /data/local/tmp/minitouch");
		device.createForward(PORT, "minitouch",DeviceUnixSocketNamespace.ABSTRACT);// 端口转发
	}
	
	
    private void ParseBanner() throws IOException{	
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	socket = new Socket("localhost", PORT);
        InputStream stream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        int len = 64;
        byte[] buffer = new byte[len];
        int realLen = stream.read(buffer);
        if (buffer.length != realLen) {
            buffer = subByteArray(buffer, 0, realLen);
        }
        String result = new String(buffer);
        String array[] = result.split(" |\n");
        banner.setVersion(Integer.valueOf(array[1]));// minitouch协议的版本
        banner.setMaxcontacts(Integer.valueOf(array[3]));//contact为触摸点索引，从0开始，可以有多个触摸点
        banner.setMaxx(Integer.valueOf(array[4]));//触摸的x坐标的最大值
        banner.setMaxy(Integer.valueOf(array[5]));//触摸的y坐标的最大值
        banner.setMaxpressure(Integer.valueOf(array[6]));//压力值
    }
    
    private String executeShellCommand(String command) {
		CollectingOutputReceiver output = new CollectingOutputReceiver();
		try {
			device.executeShellCommand(command, output, 0);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AdbCommandRejectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShellCommandUnresponsiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output.getOutput();
	}
    
    
    /**
	 * 提取byte数组
	 * @param byte1
	 * @param start
	 * @param end
	 * @return
	 */
	private byte[] subByteArray(byte[] byte1, int start, int end) {
		byte[] byte2 = new byte[0];
		try {
			byte2 = new byte[end - start];
		} catch (NegativeArraySizeException e) {
			e.printStackTrace();
		}
		System.arraycopy(byte1, start, byte2, 0, end - start);
		return byte2;
	}
    
    public void TouchDown(Point downpoint)
    {
        Point realpoint = PointConvert(downpoint);
        ExecuteTouch(String.format("d 0 %s %s 50\n", (int)realpoint.getX(),  (int)realpoint.getY()));
    }

    public void TouchUp()
    {
        ExecuteTouch("u 0\n");
    }

    public void TouchMove(Point movepoint)
    {
        Point realpoint = PointConvert(movepoint);
        ExecuteTouch(String.format("m 0 %s %s 50\n", (int)realpoint.getX(), (int)realpoint.getY()));
    }

    public void ExecuteTouch(String command){
    	if (outputStream != null) {
            try {
                Logger.debug("ExecuteTouch Event" + command);
                outputStream.write(command.getBytes());
                outputStream.flush();
                String endCommand = "c\n";
                outputStream.write(endCommand.getBytes());
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
    	}
    }
    
    public Point PointConvert(Point point)
    {	
        Point realpoint = new Point((int)(point.getX()/width*banner.getMaxx()), (int)(point.getY()/height*banner.getMaxy()));
        return realpoint;
    }
    
    public void ChangePointConvert(int width,int height){
    	this.width = width;
    	this.height = height;
    }
}
