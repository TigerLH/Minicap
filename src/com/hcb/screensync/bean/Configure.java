package com.hcb.screensync.bean;
/** 
 * @author  linhong: 
 * @date 2016年6月12日 上午11:50:33 
 * @Description: TODO
 * @version 1.0  
 */
public class Configure {
	/**
	 * 日志过滤器
	 */
	private String logFilter;
	
	/**
	 * 连接模式
	 */
	private String module;
	
	/**
	 * 是否开启录制功能
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
