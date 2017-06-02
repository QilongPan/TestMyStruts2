package config;

import java.util.Map;
/*
 * �����ļ�����
 */

public class Config {
	
	private Map<String,Action> action;  //action����

	private Map<String,String> interceptors;  //����������
	
	private static Config  config;
	
	public synchronized static Config  getInit(){	//����ģʽʵ��������
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
