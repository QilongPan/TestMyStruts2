package myInterceptor;

import java.lang.reflect.InvocationTargetException;

public class MyInterceptor implements Interceptor {
	// interceptorÃû×Ö
	private String name;
	// classÂ·¾¶
	private String classPath;

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

	@Override
	public Object intercept(Invocation invocation)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		System.out.println("intercept");
		return invocation.process();
	}

}
