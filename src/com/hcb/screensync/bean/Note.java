package com.hcb.screensync.bean;
/** 
 * @author  linhong: 
 * @date 2016��6��8�� ����1:48:12 
 * @Description: TODO
 * @version 1.0  
 */
public class Note {
	/**
	 * ��ע��Ϣ��x����
	 */
	private int x;
	/**
	 * ��ע��Ϣ��Y����
	 */
	private int y;
	/**
	 * ��ע��Ϣ
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
