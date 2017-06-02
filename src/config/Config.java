package config;

import java.util.Map;
/*
 * 配置文件对象
 */

public class Config {
	
	private Map<String,Action> action;  //action集合

	private Map<String,String> interceptors;  //拦截器集合
	
	private static Config  config;
	
	public synchronized static Config  getInit(){	//单例模式实例化对象
		if(config==null){
			config = new Config ();
		}
		return config;
	}
	
	public Action getAction(String actionName) {
		return action.get(actionName);
	}
	
	public Map<String, Action> getAction() {
		return action;
	}

	public void setAction(Map<String, Action> action) {
		this.action = action;
	}

	public Map<String, String> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(Map<String, String> interceptors) {
		this.interceptors = interceptors;
	}

	
}
