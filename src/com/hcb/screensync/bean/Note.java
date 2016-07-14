package com.hcb.screensync.bean;
/** 
 * @author  linhong: 
 * @date 2016年6月8日 下午1:48:12 
 * @Description: TODO
 * @version 1.0  
 */
public class Note {
	/**
	 * 备注信息的x坐标
	 */
	private int x;
	/**
	 * 备注信息的Y坐标
	 */
	private int y;
	/**
	 * 备注信息
	 */
	private String message;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "note [x=" + x + ", y=" + y + ", message=" + message + "]";
	}
	
}
