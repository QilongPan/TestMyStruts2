package myInterceptor;

import java.lang.reflect.InvocationTargetException;
/*
 * ������
 */

public interface Interceptor {
	
    public Object intercept(Invocation invocation) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
    
}