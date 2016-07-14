package com.hcb.screensync.observer;

import java.awt.Image;


/** 
 * @author  linhong: 
 * @date 2016��6��7�� ����3:14:58 
 * @Description: TODO
 * @version 1.0  
 */
public interface KeyPanelSubject {
	public void registerObserver(KeyPanelObserver o);

	public void removeObserver(KeyPanelObserver o);

	public void notifyObservers(String type);
}
