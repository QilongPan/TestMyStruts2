package config;

import java.util.Map;
import java.util.Stack;


public class Action {
	
	private String name;	//����
	
	private String classPath;  //·��
	
	private String methodStr; //������
	
	private Map<String, TheLabel> label;  //��ǩ����
	
	private Stack<String> myInterceptors; //����������

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
