package com.hcb.screensync.constansts;
/** 
 * @author  linhong: 
 * @date 2016��6��15�� ����6:13:12 
 * @Description: TODO
 * @version 1.0  
 */
public enum KeyPanelEventType {
	BACK("BACK"),
	HOME("HOME"),
	MENU("MENU");
	String code;
	private KeyPanelEventType(String code){
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
