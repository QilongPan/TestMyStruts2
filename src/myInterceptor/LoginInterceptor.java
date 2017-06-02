package myInterceptor;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import config.ActionContext;

public class LoginInterceptor extends MyInterceptor{
	
	 @Override
    public Object intercept(Invocation invocation) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().getRequest();
		if(request.getParameter("userName") != null
				&& request.getParameter("userName").length()>0){
			 return true;
		}
		return false;
    }

}
