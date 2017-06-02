package myInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InterceptorProxy implements InvocationHandler {

	private Object proxied;
	
	public InterceptorProxy (Object proxied){
		this.proxied = proxied;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
	//	method.invoke(this.proxied, args);
		return method.invoke(this.proxied, args);
	}

	
}
