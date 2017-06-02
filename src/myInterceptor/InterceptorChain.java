package myInterceptor;

import java.util.Collections;
import java.util.Stack;

public class InterceptorChain {
    private Stack<Interceptor> interceptors;
     
    public InterceptorChain(Stack<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }
    
    public Object registerAll(Object target){
    	System.out.println("½øÈëÀ¹½ØÆ÷Á´");
        for(Interceptor interceptor:interceptors){
            target = TargetProxy.bind(target, interceptor);
        }
        return target;
    }
    
    public void addInterceptor(Interceptor interceptor){
        interceptors.add(interceptor);
    }
    
    public Stack<Interceptor> getInterceptor(){
        return (Stack<Interceptor>) Collections.unmodifiableCollection(interceptors);
    }
}