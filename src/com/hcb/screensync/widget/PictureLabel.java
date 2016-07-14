package com.hcb.screensync.widget;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/** 
 * @author  linhong: 
 * @date 2016年6月2日 下午5:55:12 
 * @Description: 截图保存区域
 * @version 1.0  
 */
public class PictureLabel extends JLabel {
	/**	
	 * 
	 */
	private static final long serialVersionUID = -1729912494785289930L;
	
	public PictureLabel(int width,int height,Image image) {
		this.setSize(width, height);
		BufferedImage newimage = new BufferedImage(width,height,BufferedImage.TYPE_INT_BGR);
		newimage.getGraphics().drawImage(image, 0, 0,this.getWidth(),this.getHeight(), null);
		ImageIcon ic = new ImageIcon();
		ic.setImage(newimage);
		this.setIcon(ic);
		this.setVerticalTextPosition(JLabel.BOTTOM);	//设置文字显示在底部
		this.setHorizontalTextPosition(JLabel.CENTER);
	}
	
}
