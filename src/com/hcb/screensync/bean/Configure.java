package com.hcb.screensync.bean;
/** 
 * @author  linhong: 
 * @date 2016��6��12�� ����11:50:33 
 * @Description: TODO
 * @version 1.0  
 */
public class Configure {
	/**
	 * ��־������
	 */
	private String logFilter;
	
	/**
	 * ����ģʽ
	 */
	private String module;
	
	/**
	 * �Ƿ���¼�ƹ���
	 */
	private boolean record;

	public String getLogFilter() {
		return logFilter;
	}

	public void setLogFilter(String logFilter) {
		this.logFilter = logFilter;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public boolean isRecord() {
		return record;
	}

	public void setRecord(boolean record) {
		this.record = record;
	}

	
	
}
