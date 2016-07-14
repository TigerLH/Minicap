package com.hcb.screensync.constansts;
/** 
 * @author  linhong: 
 * @date 2016年6月2日 下午1:35:51 
 * @Description: TODO
 * @version 1.0  
 */
public enum ConnectModule {
	USB_MODULE("USB"),
	WIFI_MODULE("WIFI");
	private String code;
	private ConnectModule(String code){
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
