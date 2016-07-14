package com.hcb.screensync.observer;

import java.awt.Image;


/** 
 * @author  linhong: 
 * @date 2016��6��7�� ����3:14:58 
 * @Description: TODO
 * @version 1.0  
 */
public interface PicturePanelSubject {
	public void registerObserver(PicturePanelObserver o);

	public void removeObserver(PicturePanelObserver o);

	public void notifyObservers(String message);
}
