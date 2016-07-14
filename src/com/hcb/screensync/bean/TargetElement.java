package com.hcb.screensync.bean;
/** 
 * @author  linhong: 
 * @date 2016Âπ?Êú?1Êó?‰∏ãÂçà8:46:19 
 * @Description: TODO
 * @version 1.0  
 */
public class TargetElement {
	private int index;
	private String text;
	private String className;
	private String pkg;
	private String contentdesc;
	private String resourceId;
	private String unique;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getPkg() {
		return pkg;
	}
	public void setPkg(String pkg) {
		this.pkg = pkg;
	}
	public String getContentdesc() {
		return contentdesc;
	}
	public void setContentdesc(String contentdesc) {
		this.contentdesc = contentdesc;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getUnique() {
		return unique;
	}
	public void setUnique(String unique) {
		this.unique = unique;
	}
	@Override
	public String toString() {
		return "TargetElement [index=" + index + ", text=" + text
				+ ", className=" + className + ", pkg=" + pkg
				+ ", contentdesc=" + contentdesc + ", resourceId=" + resourceId
				+ ", unique=" + unique + "]";
	}
	
}
