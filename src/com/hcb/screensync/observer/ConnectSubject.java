/**
 * 
 */
package com.hcb.screensync.observer;



public interface ConnectSubject {
	public void registerConnectObserver(AndroidConnectObserver o);

	public void removeConnectObserver(AndroidConnectObserver o);

	public void notifyConnectObservers();

}
