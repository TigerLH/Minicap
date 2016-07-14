/**
 * 
 */
package com.hcb.screensync.observer;



public interface SetTextDialogSubject {
	public void registerScreenObserver(SetTextDialogObserver o);

	public void removeScreenObserver(SetTextDialogObserver o);

	public void notifyScreenObservers(String content);

}
