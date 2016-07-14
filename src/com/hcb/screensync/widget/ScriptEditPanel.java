package com.hcb.screensync.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.sf.json.JSONObject;

import com.hcb.screensync.bean.RecordResult;
import com.hcb.screensync.bean.SendCommand;
import com.hcb.screensync.constansts.FindBy;
import com.hcb.screensync.constansts.OperationType;
import com.hcb.screensync.main.HcbWindow;
import com.hcb.screensync.observer.ScriptPanelObserver;
import com.hcb.screensync.uiautomator.SocketClient;
import com.hcb.screensync.utils.FileUtil;

/** 
 * @author  linhong: 
 * @date 2016年6月27日 上午11:15:24 
 * @Description: TODO
 * @version 1.0  
 */
public class ScriptEditPanel extends JPanel implements KeyListener{
	private static JTextArea jtextArea = null;
	public static String input_content = "";
	public ScriptEditPanel(int width,int height){
		this.setSize(width, height);
		this.setBackground(Color.WHITE);
		jtextArea = new JTextArea();
		jtextArea.setLineWrap(true);
		jtextArea.setSize(new Dimension(this.getWidth(), this.getHeight()));
		jtextArea.addKeyListener(this);
		add(jtextArea);
	}
	
	
	public void append(String str){
		jtextArea.append(str);
		jtextArea.append("\n");
	}
	
	



	@Override
	public void keyPressed(KeyEvent e) {
		if (e.isControlDown() && e.VK_S == e.getKeyCode()){				//CTRL+S 保存
				if("".equals(jtextArea.getText())){
					return;
				}
				JFileChooser chooser=new JFileChooser();  
			    int retval = chooser.showSaveDialog(this);
			    if(retval == JFileChooser.APPROVE_OPTION) {
			    	File file = chooser.getSelectedFile();
		           try {
		        	   String fileName = file.getAbsolutePath();
		        	   FileUtil.Write(fileName,jtextArea.getText().toString());
		        	   jtextArea.setText("");
				} catch (Exception e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
			} 
			return;
		}
		
		
		if (e.isControlDown() && e.VK_D == e.getKeyCode()){				//CTRL+D  清空
	        jtextArea.setText("");
	        return;
		}
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自动生成的方法存根
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自动生成的方法存根
		
	}
}
