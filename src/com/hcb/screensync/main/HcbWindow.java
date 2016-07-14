/**
 * 
 */
package com.hcb.screensync.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.sf.json.JSONObject;

import org.jfree.ui.tabbedui.VerticalLayout;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.hcb.screensync.bean.Configure;
import com.hcb.screensync.bean.RecordResult;
import com.hcb.screensync.bean.SendCommand;
import com.hcb.screensync.constansts.ConnectModule;
import com.hcb.screensync.constansts.FindBy;
import com.hcb.screensync.constansts.ImageIconType;
import com.hcb.screensync.constansts.KeyPanelEventType;
import com.hcb.screensync.constansts.MouseEventType;
import com.hcb.screensync.constansts.OperationType;
import com.hcb.screensync.device.ADB;
import com.hcb.screensync.minicap.MiniCapUtil;
import com.hcb.screensync.minitouch.MiniTouchUtil;
import com.hcb.screensync.observer.AndroidConnectObserver;
import com.hcb.screensync.observer.AndroidScreenObserver;
import com.hcb.screensync.observer.KeyPanelObserver;
import com.hcb.screensync.observer.PicturePanelObserver;
import com.hcb.screensync.observer.ScriptPanelObserver;
import com.hcb.screensync.server.LogcatServer;
import com.hcb.screensync.server.PerformanceServer;
import com.hcb.screensync.uiautomator.SocketClient;
import com.hcb.screensync.uiautomator.UiAutoMatorUtil;
import com.hcb.screensync.utils.ChartUtil;
import com.hcb.screensync.utils.DeviceUtil;
import com.hcb.screensync.utils.DrawUtil;
import com.hcb.screensync.utils.FileUtil;
import com.hcb.screensync.utils.InputMethodUtil;
import com.hcb.screensync.utils.Logger;
import com.hcb.screensync.utils.SystemOutPrintStream;
import com.hcb.screensync.widget.AboutFrame;
import com.hcb.screensync.widget.KeyPanel;
import com.hcb.screensync.widget.PictureLabel;
import com.hcb.screensync.widget.ScriptEditPanel;
import com.hcb.screensync.widget.SetTextDialog;
import com.hcb.screensync.widget.WriteMessageDialog;


public class HcbWindow extends JFrame implements MouseListener,KeyListener,MouseMotionListener,ActionListener,KeyPanelObserver,ScriptPanelObserver{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  static HcbWindow context = null;
	private BufferedImage ScreenImage = null;
	private PicturePanel pp = null;
	private String type = "";
	private IDevice device;
	private int width = 0;
	private int height = 0;
	private int picturePanel_width = 0;
	private int picturePanel_height = 0;
	private int mouse_down_x =0;
	private int mouse_down_y = 0;
	private int picturePanel_click_x = 0;
	private int picturePanel_click_y = 0;
	private JMenuBar menuBar= null;
	public static JMenuItem connectIconItem = null;
	private JMenuItem saveIconItem = null;
	private JMenuItem aboutIconItem = null;
	private JScrollPane js = null;
//	private OperateAndroid oa = null;
	private MiniTouchUtil minitouch = null;
	private boolean isDeviceFound = false;
	private Thread logcatThread = null;
	private Thread performanceThread = null;
	private PerformanceServer ps = null;
	private final String ConfigerFileName = "config.properties";
	private Configure configure = null;
//	public  static boolean isConnect = false;
	public  static boolean isconnect = false;
	public static String input_content = "";
	private final String PIC_PATH = "TestResult"+File.separator+"Screen.png";
	private KeyPanel keyPanel = null; //底部按钮
	private ScreenImagePanel screenImagePanel = null;//图像同步
	private JPanel screenPanel = null;//包含以上两个Panel
	private SocketClient socketClient = null;
	private ScriptEditPanel sep = null;
	private boolean isElementFound = false;
	private boolean isRecordOn = false;
	private String element = "";	//查找到的元素
	public HcbWindow() {
	    initConfigure();                  // 把背景设置为会  ;
	    this.setResizable(false);
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
		final int pc_bottom_height = screenInsets.bottom;
		Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();
		menuBar = new JMenuBar();
		saveIconItem = new JMenuItem();
		aboutIconItem = new JMenuItem();
		connectIconItem = new JMenuItem();
		ImageIcon saveIcon = new ImageIcon(getClass().getClassLoader().getResource("images/save.png"));
		ImageIcon aboutIcon =  new ImageIcon(getClass().getClassLoader().getResource("images/about.png"));
		ImageIcon connectIcon =  new ImageIcon(getClass().getClassLoader().getResource("images/connect.png"));
		saveIconItem.setIcon(saveIcon);
		saveIconItem.setToolTipText("保存");
		saveIconItem.addActionListener(this);
		aboutIconItem.setIcon(aboutIcon);
		aboutIconItem.setToolTipText("关于");
		aboutIconItem.addActionListener(this);
		connectIconItem.setIcon(connectIcon);
		connectIconItem.setToolTipText("连接");
		connectIconItem.addActionListener(this);
		JPanel jpanel = new JPanel();
		jpanel.add(connectIconItem);
		jpanel.add(saveIconItem);
		jpanel.add(aboutIconItem);
		jpanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		menuBar.add(jpanel);
		this.setJMenuBar(menuBar);
		
		
		width = scrSize.width/4;
		height = scrSize.height-pc_bottom_height;
		this.setSize((int)scrSize.getWidth(), height);
		this.setTitle("运力科技Android自助功能测试");
		picturePanel_width = 1*scrSize.width/2;
		picturePanel_height = scrSize.height-pc_bottom_height;
		screenImagePanel = new ScreenImagePanel();
		screenImagePanel.setVisible(true);
		screenImagePanel.setPreferredSize(new Dimension(width, height-150));
		keyPanel = new KeyPanel(width,20);
		screenPanel = new JPanel();
		screenPanel.setSize(width, height-60);
		screenPanel.setLayout(new VerticalLayout());
		screenPanel.add(screenImagePanel);
		screenPanel.add(keyPanel);
		screenImagePanel.addMouseListener(this);
		screenImagePanel.addMouseMotionListener(this);
		screenImagePanel.setDevice(device);
		keyPanel.registerObserver(this);
		keyPanel.addMouseListener(this);
		
		sep = new ScriptEditPanel(width,height);
		JScrollPane jsep = new JScrollPane(sep);
		
		pp = new PicturePanel();
		pp.setLayout(new FlowLayout(FlowLayout.LEFT));
		pp.addMouseListener(this);
		pp.setAutoscrolls(true);


		this.addKeyListener(this); //panel设置KeyListener不起作用
	    js = new JScrollPane(pp,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    GroupLayout groupLayout = new GroupLayout(getContentPane());
	    groupLayout.setHorizontalGroup(
	    	groupLayout.createParallelGroup(Alignment.LEADING)
	    		.addGroup(groupLayout.createSequentialGroup()
	    			.addComponent(screenPanel, GroupLayout.DEFAULT_SIZE, width, Short.MAX_VALUE)
	    			.addPreferredGap(ComponentPlacement.RELATED)
	    			.addComponent(jsep, GroupLayout.DEFAULT_SIZE, width, Short.MAX_VALUE)
	    			.addComponent(js, GroupLayout.DEFAULT_SIZE, width*2, Short.MAX_VALUE)
	    			.addGap(2))
	    );
	    groupLayout.setVerticalGroup(
	    	groupLayout.createParallelGroup(Alignment.LEADING)
	    		.addGroup(groupLayout.createSequentialGroup()
	    			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
	    				.addComponent(screenPanel, GroupLayout.DEFAULT_SIZE, height-60, Short.MAX_VALUE)
	    				.addComponent(jsep, GroupLayout.DEFAULT_SIZE, height-60, Short.MAX_VALUE)
	    				.addComponent(js, GroupLayout.DEFAULT_SIZE, height-60, Short.MAX_VALUE))
	    			.addGap(5))
	    );
	    getContentPane().setLayout(groupLayout);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点击窗口右上角的关闭按钮关闭窗口,退出程序
	    setVisible(true);// 显示窗口
	    setLocationRelativeTo(null);
	    this.addComponentListener(new ComponentAdapter(){			//窗体变化后，要重新计算点击比例
			@Override
			public void componentResized(ComponentEvent arg0){
				try{
					picturePanel_width = pp.getWidth();
					int oldWidth = screenImagePanel.getWidth();
					int oldHeight = screenImagePanel.getHeight();
					screenImagePanel.ChangePointConvert(oldWidth, oldHeight);
					int count = pp.getComponentCount();
					for(int i=0;i<count;i++){					   //窗体变化后,所有子控件大小变化
								//除去底部物理按钮的高度
						int newWidth = picturePanel_width/3-10;
						int newHeight = (int)(((float)oldHeight/oldWidth)*newWidth);
						Component comp = pp.getComponent(i);
						comp.setPreferredSize(new Dimension(picturePanel_width/3-10, newHeight));
						comp.repaint();
					}
				}catch(Exception e){
					
				}
			}
		});            
	}
	
	
	/**
	 * 初始化配置参数
	 */
	public void initConfigure(){
		//startLogforward();  //开启日志重定向
		configure = new Configure();
		String logFilter = FileUtil.getConfigValue(ConfigerFileName, "LogFilter");
		String connectModule = FileUtil.getConfigValue(ConfigerFileName, "Connect");
		String isRecord = FileUtil.getConfigValue(ConfigerFileName, "IsRecord");
		if(("TRUE").equals(isRecord.toUpperCase())){
			configure.setRecord(true);
		}else{
			configure.setRecord(false);
		}
		
		if(ConnectModule.WIFI_MODULE.getCode().equals(connectModule)){			
			configure.setModule(connectModule);
		}else{													//如果配置的不是WIFI则默认是USB模式
			configure.setModule(ConnectModule.USB_MODULE.getCode());
		}
		
		Logger.debug("LOGFILTER:"+logFilter);
		Logger.debug("RECORED STATE IS:"+String.valueOf(configure.isRecord()));
		Logger.debug("THE MODULE YOU SELECTED IS:"+configure.getModule());
		if(configure.isRecord()&&configure.getModule().equals(ConnectModule.USB_MODULE.getCode())){//出于效率考虑,USB模式下才能进行录制
				isRecordOn = true;
		}
		configure.setLogFilter(logFilter);
	}
	
	
	/**
	 * Start
	 */
	public void startRecord(){
		 Thread workThread = new Thread(new Runnable(){
				@Override
				public void run() {
					//screenImagePanel.ShowLog();
					String module = configure.getModule();
					Logger.debug(module+"模式启动中...请稍候");
					if(ConnectModule.WIFI_MODULE.getCode().equals(module)){
						startByModule(ConnectModule.WIFI_MODULE.getCode());
					}else{
						startByModule(ConnectModule.USB_MODULE.getCode());
					}
				}
			});
			workThread.start();
	}
	
	
	/**
	 * System输出重定向
	 */
	public void startLogforward(){
		SystemOutPrintStream mps = new SystemOutPrintStream(System.out);  
		System.setOut(mps);
		System.setErr(mps);
	}
	
	/**
	 * 初始化,启动各种Socket服务
	 * @param module
	 */
	public void startByModule(String module){
		try {
			InputMethodUtil.setDefaultUtf7IME();
			DeviceUtil d = new DeviceUtil();
			d.startDeviceModule(module);
			
			ADB	adb = new ADB(module);
			device = adb.getDevice();
			isDeviceFound = true;
			
			screenImagePanel.setDevice(device);
			Thread.sleep(3000);
			screenImagePanel.startMinitouch();
			Thread.sleep(3000);
			screenImagePanel.startMiniCap();
			startMonitor();
			isconnect = true;
			if(isRecordOn){
				UiAutoMatorUtil ui = new UiAutoMatorUtil();
				ui.startUiAutoMatorServer();
				Thread.sleep(5000);
				socketClient = SocketClient.getInstance();
				socketClient.registerObserver(this);		//注册监听者
				socketClient.Forward();
				socketClient.ConnectServer();
			}
		} catch (Exception e1) {						
			screenImagePanel.setIcon(ImageIconType.CONNECTFAILED.getCode());//启动中出现任何的异常
			connectIconItem.setEnabled(true);
			isconnect = false;
			stopMonitor();
			socketClient.DisConnecterver();
			stopLogcat();
		}
	}
	
	
	/**
	 * 开启日志和性能数据采集服务
	 */
	public void startMonitor(){
		try {
			FileUtil.DeleteFolder("TestResult");//删除文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(logcatThread==null){
			logcatThread = new Thread(new LogcatServer(device.getSerialNumber(), configure.getLogFilter()));
			logcatThread.start();
		}
		
		if(performanceThread==null){
			ps = new PerformanceServer(device, configure.getLogFilter());
			performanceThread = new Thread(ps);
			performanceThread.start();
		}
	}
	
	
	
	/**
	 * 停止logcat服务
	 */
	public void stopLogcat(){
		CollectingOutputReceiver output = new CollectingOutputReceiver();
		String command = "ps|grep logcat";
		String killLogcat = "kill %s";
		try {
			 device.executeShellCommand(command, output,0);
			 String[] array = output.getOutput().split("\\s+");
			 device.executeShellCommand(String.format(killLogcat, array[1]), output,0);
		} catch (TimeoutException | AdbCommandRejectedException
				| ShellCommandUnresponsiveException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 停止数据采集服务
	 */
	public void stopMonitor(){
		if(logcatThread.isAlive()){
			logcatThread.stop();
			logcatThread = null;
		}
		
		if(performanceThread.isAlive()){
			performanceThread.stop();
			performanceThread = null;
			stopLogcat();
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
        String fileName = "TestResult_"+dateFormat.format(new Date())+".zip";
		FileUtil.compress("TestResult",fileName); //压缩文件夹
	}
	
	
	/**
	 * 记录点击的结果,截图&录制的脚本
	 * @param x
	 * @param y
	 */
	public void recordClickResult(int x,int y){
			if(isRecordOn){
				if(type.equals(MouseEventType.CLICK.getCode())){
					Point p = new Point(mouse_down_x,mouse_down_y);
					final Point point = minitouch.PointConvert(p);//Panel的坐标转换为手机的坐标
					try {
						socketClient.FindElement(point.x, point.y);
						while(!isElementFound){		//等待数据返回
							Thread.sleep(100);
						}
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					isElementFound = false;     //重新置为false
				}
			}
	}
	
	public static void main(String[] args) {
//		String lookandfeel="com.jtattoo.plaf.luna.LunaLookAndFeel";	//XP
//		String lookandfeel="com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";	//Mac风格
		String lookandfeel="com.jtattoo.plaf.aluminium.AluminiumLookAndFeel"; //椭圆按钮+翠绿色按钮背景+金属质感 
//		String lookandfeel="com.jtattoo.plaf.hifi.HiFiLookAndFeel";// 黑色风格 
		try {
			UIManager.setLookAndFeel(lookandfeel);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		context = new HcbWindow();
	}

	
	class ScreenImagePanel extends JPanel implements AndroidScreenObserver,AndroidConnectObserver{
		private BufferedImage image = null;
		private MiniCapUtil minicap = null;
		private IDevice device = null;
		private JLabel label = null;
		private ImageIcon bg = null;
		private ImageIcon connect_failed = null;
		private ImageIcon connecting = null;
		public ScreenImagePanel() {
			initImageIcon();
			label = new JLabel();
			label.setSize(new Dimension(width-20,height));
			label.setIcon(bg);
			setLayout(new FlowLayout());
			add(label);
		}				
		
		public void initImageIcon(){
			ImageIcon tmp = new ImageIcon(HcbWindow.class.getResource("/images/bg.png"));
			Image image = tmp.getImage().getScaledInstance(width-20, height-150, Image.SCALE_DEFAULT);
			bg = new ImageIcon(image);
			
			tmp = new ImageIcon(HcbWindow.class.getResource("/images/connect_failed.png"));
		    image = tmp.getImage().getScaledInstance(width-20, height-150, Image.SCALE_DEFAULT);
		    connect_failed = new ImageIcon(image);
		    
		    tmp = new ImageIcon(HcbWindow.class.getResource("/images/connecting.gif"));
		    image = tmp.getImage().getScaledInstance(width-20, height-150, Image.SCALE_DEFAULT);
		    connecting = new ImageIcon(image);
		}
		
		/**
		 * 替换Icon显示的图片
		 * @param fileName
		 */
		public void setIcon(String type){
			if(ImageIconType.WAITFORCONNECT.getCode().equals(type)){
				label.setIcon(bg);
			}else if(ImageIconType.CONNECTING.getCode().equals(type)){
				label.setIcon(connecting);
			}else if(ImageIconType.CONNECTFAILED.getCode().equals(type)){
				label.setIcon(connect_failed);
			}
		}
		
		public void setDevice(IDevice device){
			this.device = device;
		}
		
		public IDevice getDevice(){
			return this.device;
		}
		
		
		/**
		 * 启动MiniCap服务
		 * @throws Exception 
		 */
		public void startMiniCap() throws Exception{
			minicap = new MiniCapUtil(device);
			minicap.registerScreenObserver(this);
			minicap.registerConnectObserver(this);
			minicap.startScreenListener();
		}
		
		public void ChangePointConvert(int widht,int height){
			minitouch.ChangePointConvert(widht, height);
		}
		
		/**
		 * 启动MiniTouch服务
		 * @throws Exception 
		 */
		public void startMinitouch() throws Exception{
			minitouch = new MiniTouchUtil(device,this.getWidth(),this.getHeight());
			minitouch.startMoniTouch();
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if(!isconnect){
				return;
			}
			try {
				if (image == null)
					return;
				g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
				image.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void frameImageChange(Image image) {
			this.image = (BufferedImage) image;
			this.repaint();
//			this.remove(textArea);
		}


		@Override
		public void onDisConnect() {
			isconnect = false;
			image = null;
			setIcon(ImageIconType.WAITFORCONNECT.getCode());
			connectIconItem.setEnabled(true);
			stopMonitor();
			socketClient.DisConnecterver();
			stopLogcat();
			JOptionPane.showMessageDialog(null, "屏幕同步中断,请检查连接后重试", "错误", JOptionPane.CLOSED_OPTION);
		}
	}
	
	
	class PicturePanel extends JPanel implements PicturePanelObserver{
		public PicturePanel(){
			
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
		}

		

		@Override
		public Dimension getPreferredSize() {
			Dimension dimension = null;
			if(this.getComponentCount()>0){
				int height = this.getComponent(0).getHeight(); //子控件的高度
				int total_height = 0;
				int count = getComponentCount();
				if(count%2==0){	//如果能被3整除
					total_height = count/2*(height)+(count/2-1)*5;
				}else{
					total_height = (count/2+1)*(height)+(count/2)*5;
				}
				picturePanel_height = total_height;
				dimension = new Dimension(this.getWidth(),total_height);
				js.getViewport().setViewPosition(new Point(this.getWidth(),total_height));//JscrollPanel自动滑动到底部
			}else{
				return super.getPreferredSize();
			}
			return dimension;
		}

		@Override
		public void onDrawStringMessage(String message) {	
			System.out.println("receive message :"+message);
			Component pl = pp.getComponentAt(new Point(picturePanel_click_x, picturePanel_click_y));
			System.out.println(pl.getClass().getName());
			if(!(pl instanceof PictureLabel)){
				return;
			}
			System.out.println("order:"+getComponentZOrder(pl));
			PictureLabel pic = (PictureLabel) pl;
			ImageIcon icon = (ImageIcon) pic.getIcon();
			BufferedImage image = (BufferedImage)icon.getImage();
			Graphics2D g2 = (Graphics2D)image.getGraphics();
			Font font = new Font("黑体",Font.PLAIN,15);
			g2.setFont(font);
			g2.setColor(Color.BLUE);
			char[] array = message.toCharArray();
			int width = pl.getWidth();
			int show_line = width/15;//每行能显示多少个字符
			System.out.println(array.length);
			System.out.println("show_line:"+show_line);
			if(array.length>show_line){
				int count = 0;
				if(array.length%show_line==0){	//能被整除
					count = array.length/show_line;//要显示多少行才能显示完
				}else{
					count = array.length/show_line+1;
				}
				System.out.println("count:"+count);
				for(int i=0;i<count;i++){
					if(i==count-1){						//最后一行,endIndex为字符串剩余的长度
						String line = message.substring(i*(show_line),array.length);
						System.out.println(line);
						g2.drawString(line,0,pl.getHeight()-20*(count-i-1));
					}else{
						String line = message.substring(i*(show_line),show_line*(i+1));
						System.out.println(line);
						g2.drawString(line,0,pl.getHeight()-20*(count-i-1));
					}
				}
			}else{
				g2.drawString(message,0,pl.getHeight()-25);
			}
			pl.repaint();
		}
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(!isconnect){			//是否已连接
			return;
		}
		
		if(arg0.getSource()==pp&&arg0.getButton()==1){
			picturePanel_click_x = arg0.getX();
			picturePanel_click_y = arg0.getY();
			Component pl = pp.getComponentAt(new Point(picturePanel_click_x, picturePanel_click_y));
			System.out.println(pl.getClass().getName());
			if(!(pl instanceof PictureLabel)){
				return;
			}
			WriteMessageDialog wmd = new WriteMessageDialog(this,true);
			wmd.registerObserver(pp);
			wmd.setLocationRelativeTo(null);	//设置居中显示
			wmd.show();
			return;
		}
		
		if(arg0.getSource()==pp&&arg0.getButton()==3){
			Component comp = null;
			try{
			  	 comp = pp.getComponentAt(new Point(arg0.getX(), arg0.getY()));
			}catch(Exception e){
				e.printStackTrace();
			}
			if(comp!=null){
				pp.remove(comp);
//				for(Note node:note_list){
//					if(comp.contains(node.getX(), node.getY())){
//						note_list.remove(node);
//					}
//				}
				pp.updateUI();
			}
			return;	
		}     
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if(arg0.getSource() == screenImagePanel){
			try{
				BufferedImage image = new BufferedImage(screenPanel.getWidth(), screenPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
				Graphics2D g2 = image.createGraphics();
				screenPanel.paint(g2);
				ScreenImage = image;	//截图ScreenPanel中Image
				int x = arg0.getX();
				int y = arg0.getY();
				mouse_down_x = x;
				mouse_down_y = y;
				type = MouseEventType.CLICK.getCode();
				recordClickResult(x,y);
				minitouch.TouchDown(new Point(arg0.getX(),arg0.getY()));
			}catch(Exception e){
				
			}
		}
	}
	
	
	
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(!isconnect){	
			return;
		}

		if(arg0.getSource() == screenImagePanel){
			int width = screenPanel.getWidth();
			int height = screenPanel.getHeight();
			int x = arg0.getX();
			int y = arg0.getY();
			BufferedImage newimage = new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
			Graphics g = newimage.getGraphics();
			g.drawImage(ScreenImage, 0, 0,width,height , null);
			
			
			
			Graphics2D g2 = (Graphics2D)newimage.getGraphics();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
		    String time = dateFormat.format(new Date());
		    g2.setColor(Color.RED);
		    Font font = new Font("黑体",Font.PLAIN,25);
		    g2.setFont(font);
//			g2.drawString(time, width/2-50,height/2);
			g2.setStroke(new BasicStroke(4));
			if(type.equals(MouseEventType.CLICK.getCode())){
				sep.append(element);
				g2.drawOval(mouse_down_x-25, mouse_down_y-25, 50, 50);
			}else{
				Map<String,String> map = new HashMap<String,String>();	//脚本生成
				map.put("tox", String.valueOf(x));
				map.put("toy", String.valueOf(y));
				map.put("fromx", String.valueOf(mouse_down_x));
				map.put("fromy", String.valueOf(mouse_down_y));
				RecordResult command = new RecordResult();
				command.setUnique("");
				command.setAction(OperationType.DragTo.toString());
				command.setParams(map);
				sep.append(command.toString());
				DrawUtil.drawAL(mouse_down_x,mouse_down_y, x-20, y-20, g2);//带箭头的直线
			}
			int oldWidth = screenPanel.getWidth();
			int oldHeight = screenPanel.getHeight();
			int newWidth = picturePanel_width/2-10;
			int newHeight = (int)(((float)oldHeight/oldWidth)*newWidth);
			PictureLabel pl = new PictureLabel(newWidth,newHeight,newimage);
			pl.setText(time);
			pp.add(pl);
			pp.updateUI();
			minitouch.TouchUp();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
//		switch (code) {
//		case KeyEvent.VK_BACK_SPACE:
//			oa.press("KEYCODE_DEL");
//			break;
//		case KeyEvent.VK_SPACE:
//			oa.press("KEYCODE_SPACE");
//			break;
//		case KeyEvent.VK_DELETE:
//			oa.press("KEYCODE_FORWARD_DEL");
//			break;
//		case KeyEvent.VK_UP:
//			oa.press("KEYCODE_DPAD_UP");
//			break;
//		case KeyEvent.VK_DOWN:
//			oa.press("KEYCODE_DPAD_DOWN");
//			break;
//		case KeyEvent.VK_LEFT:
//			oa.press("KEYCODE_DPAD_LEFT");
//			break;
//		case KeyEvent.VK_RIGHT:
//			oa.press("KEYCODE_DPAD_RIGHT");
//			break;
//		case KeyEvent.VK_ENTER:
//			oa.press("KEYCODE_ENTER");
//			break;
//		case KeyEvent.VK_CONTROL:
//			break;
//		case KeyEvent.VK_ALT:
//			break;
//		case KeyEvent.VK_SHIFT:
//			break;
//		default:
//			oa.type(e.getKeyChar());
//		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(arg0.getSource()==screenImagePanel){
			int x = arg0.getX();
			int y = arg0.getY();
			boolean ismoved = Math.abs(mouse_down_x-x)>100||Math.abs(mouse_down_y-y)>100?true:false;
			if(!ismoved){
				return;
			}
			BufferedImage image = new BufferedImage(screenPanel.getWidth(), screenPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = image.createGraphics();
			screenPanel.paint(g2);
			ScreenImage = image;	//截图ScreenPanel中Image
			type = MouseEventType.MOTION.getCode();
			minitouch.TouchMove(new Point(arg0.getX(),arg0.getY()));
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource()==saveIconItem){
			System.out.println("保存截图...");
			Thread workThread = new Thread(new Runnable(){
				@Override
				public void run() {
					BufferedImage image = new BufferedImage(picturePanel_width, picturePanel_height, BufferedImage.TYPE_INT_RGB);
					Graphics2D g2 = image.createGraphics();
					pp.paint(g2);
					//g2.drawImage(image, 0, 0, picturePanel_width,picturePanel_height , null);
					try {
						 File dir = new File("TestResult");
						 if(!dir.exists()){
							 dir.mkdir();
						 }
						ImageIO.write(image, "png", new File(PIC_PATH));
						pp.removeAll();
						pp.updateUI();
						ChartUtil.ChartToPicture(configure.getLogFilter(),ps.getResultList());
						stopMonitor();
						Thread.sleep(5000);
						startMonitor();	//重启服务获取新的数据
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
			});
			workThread.start();
		}
		
		
		if(arg0.getSource()==aboutIconItem){
			AboutFrame aboutFrame = new AboutFrame();
			aboutFrame.show();
		}
		
		if(arg0.getSource()==connectIconItem){
			screenImagePanel.setIcon(ImageIconType.CONNECTING.getCode());
			connectIconItem.setEnabled(false);
		    startRecord();
		}
		
	}

	
	@Override
	public void onItemClick(String type) {	//keyPanel点击事件
			if(!isconnect){	
				return;
			}
			int x = 0;
			if(type.equals(KeyPanelEventType.BACK.getCode())){
				x = keyPanel.back.getX();
			}else if(type.equals(KeyPanelEventType.HOME.getCode())){
				x = keyPanel.home.getX();
			}else{
				x = keyPanel.menu.getX();
			}		
			int y = screenPanel.getHeight();
			int width = screenPanel.getWidth();
			int height = screenPanel.getHeight();
			BufferedImage image = new BufferedImage(screenPanel.getWidth(), screenPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = image.createGraphics();
			screenPanel.paint(g2);
			BufferedImage newimage = new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
			Graphics g = newimage.getGraphics();
			g.drawImage(image, 0, 0,width,height , null);
			
			Graphics2D g2D = (Graphics2D)newimage.getGraphics();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
		    String time = dateFormat.format(new Date());
		    g2D.setColor(Color.RED);
		    Font font = new Font("黑体",Font.PLAIN,25);
		    g2D.setFont(font);
		    g2D.setStroke(new BasicStroke(4));
		    g2D.drawOval(x, y-60, 50, 50);
			int oldWidth = screenPanel.getWidth();
			int oldHeight = screenPanel.getHeight();
			int newWidth = picturePanel_width/2-10;
			int newHeight = (int)(((float)oldHeight/oldWidth)*newWidth);
			PictureLabel pl = new PictureLabel(newWidth,newHeight,newimage);
			pl.setText(time);
			pp.add(pl);
			pp.updateUI();
		}


	@Override
	public void onElementFound(String result) {
		isElementFound = true;
		String json = result.toString().replace("\\", "");
		RecordResult ac = null;
		try {
			JSONObject tmp = JSONObject.fromObject(json);
			String contentString = tmp.getString("content");
			JSONObject content = JSONObject.fromObject(contentString);
			Map<String,String> map = new HashMap<String,String>();
			String unique = content.get("unique").toString();
			ac = new RecordResult();
			ac.setUnique(unique);
			if(FindBy.ID.toString().equals(unique)){
				ac.setAction(OperationType.ClickById.toString());
				map.put("resourceId", content.get("resourceId").toString());
			}else if(FindBy.TEXT.toString().equals(unique)){
				ac.setAction(OperationType.ClickByText.toString());
				map.put("text", content.get("text").toString());
			}else if(FindBy.CONTENTDESC.toString().equals(unique)){
				ac.setAction(OperationType.ClickByContentdesc.toString());
				map.put("contentdesc", content.get("contentdesc").toString());
			}else if(FindBy.CLASSNAME.toString().equals(unique)){
				ac.setAction(OperationType.ClickByClassName.toString());
				map.put("className", content.get("className").toString());
				map.put("index", content.get("index").toString());
			}
			if("android.widget.EditText".equals(content.get("className").toString())){	//如果点击的是输入框则让用户填写内容
				ac.setAction(OperationType.SetText.toString());
				SetTextDialog dialog = new SetTextDialog(HcbWindow.context,true);
				dialog.setLocationRelativeTo(null);	//设置居中显示
				dialog.show();
				map.put("content", input_content);
				SendCommand command = new SendCommand();
				command.setAction(OperationType.SetText.toString());
				command.setUnique(unique);
				command.setParams(map);
				SocketClient.getInstance().SetText(command);
			}
			ac.setParams(map);
			element = ac.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
