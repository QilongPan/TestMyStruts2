package myInterceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
 * 为了在Interceptor中得到被拦截对象的信息，需要定义一种数据结构来表示被拦截的方法
 */
public class Invocation {
	
	private Object target;
	private Method method;
	private Object[] args;

	public Invocation(){
		
	}
	
	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Invocation(Object target, Method method, Object[] args) {
		this.target = target;
		this.method = method;
		this.args = args;
	}

	/**
	 * 调用代理类的方法
	 * 
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public Object process() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		return method.invoke(target, args);
	}
	
}
