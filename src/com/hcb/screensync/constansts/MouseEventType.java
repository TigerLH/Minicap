package com.hcb.screensync.constansts;
/** 
 * @author  linhong: 
 * @date 2016��6��3�� ����12:47:25 
 * @Description: TODO
 * @version 1.0  
 */
public enum MouseEventType {
	CLICK("click"),
	MOTION("motion");
	String code;
	private MouseEventType(String code){
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
