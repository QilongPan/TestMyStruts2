package myInterceptor;

import java.lang.reflect.InvocationTargetException;
/*
 * À¹½ØÆ÷
 */

public interface Interceptor {
	
    public Object intercept(Invocation invocation) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
    
}