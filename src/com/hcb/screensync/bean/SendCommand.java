package com.hcb.screensync.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

/** 
 * @author  linhong: 
 * @date 2016年6月23日 下午2:26:03 
 * @Description: TODO
 * @version 1.0  
 */
public class SendCommand {
	private String action;
	private String unique;
	private Map<String,String> params;
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getUnique() {
		return unique;
	}
	public void setUnique(String unique) {
		this.unique = unique;
	}
	public Map<String, String> getParams() {
		return params;
	}
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	@Override
	public String toString() {
		JSONObject json = new JSONObject();
		json.put("action", action);
		json.put("unique", unique);
		json.put("params", params);
		return json.toString();
	}
	
	
	public static void main(String[] args) {
		Map<String,String> map = new HashMap<String, String>();
		map.put("x", "180");
		map.put("y", "520");
		SendCommand s = new SendCommand();
		s.setAction("Find");
		s.setUnique("ID");
		s.setParams(map);
		System.out.println(s.toString());
	}
}
