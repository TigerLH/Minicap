package com.hcb.screensync.constansts;
/** 
 * @author  linhong: 
 * @date 2016��6��6�� ����1:43:17 
 * @Description: TODO
 * @version 1.0  
 */
public enum SocketType {
	MINITOUCH("minitouch"),
	MINICAP("minicap");
	String code;
	private SocketType(String code){
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
