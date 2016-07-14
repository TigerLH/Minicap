package com.hcb.screensync.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hcb.screensync.utils.Command.CommandResult;

/** 
 * @author  linhong: 
 * @date 2016年6月23日 下午8:17:34 
 * @Description: TODO
 * @version 1.0  
 */
public class InputMethodUtil {
	private static Command command = new Command();
	private final static String DEFAULT_IME_FILTER = "default_input_method";
	private final static String ENABLED_IME_FILTER = "enabled_input_methods";
	private final static String UTF7_IME_NAME="jp.jun_nama.test.utf7ime/.Utf7ImeService";
	private final static String UPDATE_IME_COMMAND ="adb shell content update --uri content://settings/secure --bind value:s:%s --where _id=%s";
	private final static String QUERY_IME_COMMAND = "adb shell content query --uri content://settings/secure";
	/**
	 * 根据类型获取输入法enabled和default的ID
	 * @param type
	 * @return
	 */
	private static String _getInputMethodIdByType(String type){
		String id = "";
		CommandResult commandResult = command.executeCommand(QUERY_IME_COMMAND, type);
		Pattern p = Pattern.compile("id=\\d{1,5}+");
		Matcher m = p.matcher(commandResult.successMsg.trim());
		if(m.find()){
			id = m.group(0).split("=")[1];
		}
		return id;
	}
	
	
	/**
	 * 设置默认的输入法为Utf7IME
	 * 输入中文时依赖此输入法
	 */
	public static void setDefaultUtf7IME(){
		String enabled_id = _getInputMethodIdByType(ENABLED_IME_FILTER);
		String default_id = _getInputMethodIdByType(DEFAULT_IME_FILTER);
		String settoDedfault = String.format(UPDATE_IME_COMMAND, UTF7_IME_NAME,default_id);
		List<String> list = _getEnabledInputMethod();
		if(list.contains(UTF7_IME_NAME)){
			command.executeCommand(settoDedfault, null);
		}else{
			String str = "";
			for(String ime:list){
				str+= ime+":";
			}
			String addtoEnabled = String.format(UPDATE_IME_COMMAND, str+UTF7_IME_NAME,enabled_id);
			command.executeCommand(addtoEnabled, null);
			command.executeCommand(settoDedfault, null);
		}
	}
	
	
	/**
	 * 获取当前设备内的输入法
	 * @return
	 */
	private static List<String> _getEnabledInputMethod(){
		List<String> list = new ArrayList<String>();
		CommandResult commandResult = command.executeCommand(QUERY_IME_COMMAND, ENABLED_IME_FILTER);
		String enabledInputMethod = commandResult.successMsg;
		String[] array = enabledInputMethod.split("value=");
		String ime = array[1];
		String[] imes = ime.split(":");
		for(int i=0;i<imes.length;i++){
			list.add(imes[i]);
		}
		return list;
	}
}
