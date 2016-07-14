package com.hcb.screensync.widget;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/** 
 * @author  linhong: 
 * @date 2016��6��16�� ����9:29:40 
 * @Description: TODO
 * @version 1.0  
 */
public class AboutFrame extends JFrame {
		public AboutFrame(){
			 this.setTitle("关于");
			 this.setResizable(false);
			 this.setSize(350, 200);
			 this.setLocationRelativeTo(null);
			 String lookandfeel="com.jtattoo.plaf.aluminium.AluminiumLookAndFeel"; //��Բ��ť+����ɫ��ť����+�����ʸ� 
			 try {
				UIManager.setLookAndFeel(lookandfeel);
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | UnsupportedLookAndFeelException e) {
				// TODO �Զ���ɵ� catch ��
				e.printStackTrace();
			}
			 JLabel lblNewLabel = new JLabel("Copyright (c) 2016 \u6210\u90FD\u8FD0\u529B\u79D1\u6280. All Rights Reserved ");
			 
			 JLabel lblNewLabel_1 = new JLabel("Android\u81EA\u52A9\u529F\u80FD\u6D4B\u8BD5(V1.0.3)");
			 
			 JLabel lblNewLabel_2 = new JLabel("");
			 lblNewLabel_2.setIcon((new ImageIcon(AboutFrame.class.getResource("/images/logo.png"))));
			 GroupLayout groupLayout = new GroupLayout(getContentPane());
			 groupLayout.setHorizontalGroup(
			 	groupLayout.createParallelGroup(Alignment.LEADING)
			 		.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			 		.addGroup(groupLayout.createSequentialGroup()
			 			.addGap(18)
			 			.addComponent(lblNewLabel)
			 			.addContainerGap(14, Short.MAX_VALUE))
			 		.addGroup(groupLayout.createSequentialGroup()
			 			.addGap(97)
			 			.addComponent(lblNewLabel_1)
			 			.addContainerGap(103, Short.MAX_VALUE))
			 );
			 groupLayout.setVerticalGroup(
			 	groupLayout.createParallelGroup(Alignment.TRAILING)
			 		.addGroup(groupLayout.createSequentialGroup()
			 			.addComponent(lblNewLabel_2)
			 			.addGap(39)
			 			.addComponent(lblNewLabel_1)
			 			.addPreferredGap(ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
			 			.addComponent(lblNewLabel)
			 			.addContainerGap())
			 );
			 getContentPane().setLayout(groupLayout);
		}
}
