package com.hcb.screensync.constansts;
/** 
 * @author  linhong: 
 * @date 2016��6��30�� ����2:36:00 
 * @Description: TODO
 * @version 1.0  
 */
public enum ImageIconType {
	CONNECTFAILED("CONNECTFAILED"),
	CONNECTING("CONNECTING"),
	WAITFORCONNECT("WAITFORCONNECT");
	String code;
	private ImageIconType(String code){
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
