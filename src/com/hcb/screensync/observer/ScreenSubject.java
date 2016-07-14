/**
 * 
 */
package com.hcb.screensync.observer;

import java.awt.Image;


public interface ScreenSubject {
	public void registerScreenObserver(AndroidScreenObserver o);

	public void removeScreenObserver(AndroidScreenObserver o);

	public void notifyScreenObservers(Image image);

}
