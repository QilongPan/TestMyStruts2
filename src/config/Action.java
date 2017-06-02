package config;

import java.util.Map;
import java.util.Stack;


public class Action {
	
	private String name;	//名字
	
	private String classPath;  //路径
	
	private String methodStr; //方法名
	
	private Map<String, TheLabel> label;  //标签集合
	
	private Stack<String> myInterceptors; //拦截器集合

	public Stack<String> getMyInterceptors() {
		return myInterceptors;
	}

	public void setMyInterceptors(Stack<String> myInterceptors) {
		this.myInterceptors = myInterceptors;
	}

	public String getMethodStr() {
		return methodStr;
	}

	public void setMethodStr(String methodStr) {
		this.methodStr = methodStr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public Map<String, TheLabel> getLabel() {
		return label;
	}

	public void setLabel(Map<String, TheLabel> label) {
		this.label = label;
	}
	
}
