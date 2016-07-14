package com.hcb.screensync.widget;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.hcb.screensync.observer.PicturePanelObserver;
import com.hcb.screensync.observer.PicturePanelSubject;

/** 
 * @author  linhong: 
 * @date 2016��5��5�� ����10:06:57 
 * @Description: TODO
 * @version 1.0  
 */
public class WriteMessageDialog extends JDialog implements PicturePanelSubject{
	private JTextField textField;
	private List<PicturePanelObserver> observers = new ArrayList<PicturePanelObserver>();
	public WriteMessageDialog(Frame owner, boolean model) {
		super(owner, model);//�Ի�ģʽ
		this.setTitle("���ֱ�ע");	
		this.setAlwaysOnTop(true);
		this.setSize(400,125);	  //���ô�С
		JLabel label = new JLabel("\u5907\u6CE8\u6587\u5B57:");
		
		textField = new JTextField();
		textField.setColumns(10);
		JButton button = new JButton("\u786E\u5B9A");
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!("").equals(arg0)){
					notifyObservers(textField.getText().toString());
				}
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
							.addGap(161)
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
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(button)
					.addContainerGap(63, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
	}
	
	@Override
	public void registerObserver(PicturePanelObserver o) {
			observers.add(o);
	}
	
	@Override
	public void removeObserver(PicturePanelObserver o) {
		int index = observers.indexOf(o);
		if (index != -1) {
			observers.remove(o);
		}
		
	}
	
	@Override
	public void notifyObservers(String message) {
		for (PicturePanelObserver observer : observers) {
			observer.onDrawStringMessage(message);
		}
	}
}
