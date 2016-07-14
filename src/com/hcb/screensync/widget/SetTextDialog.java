package com.hcb.screensync.widget;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.hcb.screensync.main.HcbWindow;

/** 
 * @author  linhong: 
 * @date 2016年5月5日 上午10:06:57 
 * @Description: TODO
 * @version 1.0  
 */
public class SetTextDialog extends JDialog{
	private JTextField textField;
	public SetTextDialog(Frame owner, boolean model) {
		super(owner, model);//对话模式
		this.setTitle("EnterText");	
		this.setAlwaysOnTop(true);
		this.setSize(400,125);	  //设置大小
		JLabel label = new JLabel("\u8F93\u5165\u6587\u672C:");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JButton button = new JButton("\u786E\u5B9A");
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				HcbWindow.input_content = textField.getText();
				dispose();
			}
			
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(label)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(textField, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(162)
							.addComponent(button)))
					.addContainerGap(36, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(label)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
					.addComponent(button)
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
	}
}
