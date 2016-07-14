package com.hcb.screensync.widget;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

/** 
 * @author  linhong: 
 * @date 2016年6月2日 下午6:05:57 
 * @Description: TODO
 * @version 1.0  
 */
public class Test extends JFrame {
	public static void main(String[] args) {
		new Test();
	}
	public Test() {
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
		int pc_bottom_height = screenInsets.bottom;
		Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();
		int width = scrSize.width/3;
		int height = scrSize.height-pc_bottom_height;
		this.setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点击窗口右上角的关闭按钮关闭窗口,退出程序
	    setVisible(true);// 显示窗口
	    setLocationRelativeTo(null);
		Image image = null;
		System.out.println("test");
		try {
			image = ImageIO.read(new File("screen.png"));
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		JPanel panel = new JPanel();
		
		JPanel panel_2 = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 531, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
	}
}
