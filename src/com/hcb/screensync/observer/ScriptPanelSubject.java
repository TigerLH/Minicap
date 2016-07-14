package com.hcb.screensync.observer;

import org.json.JSONObject;


/** 
 * @author  linhong: 
 * @date 2016年6月7日 下午3:14:58 
 * @Description: TODO
 * @version 1.0  
 */
public interface ScriptPanelSubject {
	public void registerObserver(ScriptPanelObserver o);

	public void removeObserver(ScriptPanelObserver o);

	public void notifyObservers(String result);
}
