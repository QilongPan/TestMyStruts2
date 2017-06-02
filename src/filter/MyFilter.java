package filter;

import java.io.UnsupportedEncodingException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import myInterceptor.Interceptor;
import myInterceptor.InterceptorProxy;
import myInterceptor.Invocation;
import config.ActionContext;
import config.Config;

public class MyFilter implements Filter {

	private String language = "utf-8";
	private String className = ""; // 类名
	private String methodName = ""; // 方法名
	private Config config;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws UnsupportedEncodingException {
		className="";
		methodName="";
		request.setCharacterEncoding(language); // 设置编码
		ActionContext.getContext().setRequest(request);
		try {
			// 得到配置文件类
			// Config config = Config.getInit();
			HttpServletRequest servletRequest = (HttpServletRequest) request;
			// 更新session
			ActionContext.getContext().setSession(servletRequest.getSession());
			String uri = servletRequest.getRequestURI();
			String[] sUri = uri.split("/");
			if (sUri.length > 0 && uri.lastIndexOf(".jsp") < 0) {
				className = sUri[sUri.length - 1];
				methodName = config.getAction().get(className).getMethodStr();
			}
			// 不是jsp页面
			if (!className.equals("") || !methodName.equals("")) {
				Map<String, Object> map = getRequestKey_Value(servletRequest); // 获取request中的键值对
				// 加载类
				Class<?> c = Class.forName(config.getAction(className)
						.getClassPath());
				// 实例化类
				Object o = c.newInstance();
				// 获取类中的所有属性
				Field f[] = c.getDeclaredFields();
				for (int m = 0; m < f.length; m++) {
					if (map.containsKey(f[m].getName())) { // 页面和类都有该属性
						String firstChar = f[m].getName().substring(0, 1)
								.toUpperCase();
						String setMethod = "set" + firstChar
								+ f[m].getName().substring(1);
						Method setPro = o.getClass().getMethod(setMethod,
								f[m].getType());
						setPro.invoke(o, map.get(f[m].getName()));
					}

				}
				// 得到拦截器
				Stack<Interceptor> interceptors = getInterceptorInAction(className);
				//执行拦截器
				boolean flag = isPassInterceptor(interceptors);
	
				Object result = null;
				if (flag) {
					result = "false";
				} else {
					// 取得方法
					Method method = o.getClass().getMethod(methodName);
					// 执行方法
					result = method.invoke(o);
				}

				if (result != null && !result.equals("")) {
					// 取得返回路径
					String forwardPath = (String) result;
					// 转发方式
					String urlType = config.getAction(className).getLabel()
							.get(forwardPath).getType();
					// 转发路径
					String urlPaht = config.getAction(className).getLabel()
							.get(forwardPath).getValue();
					if (urlType.equals("redetion")) {// 外部转发
						((HttpServletResponse) response)
								.sendRedirect(servletRequest.getContextPath()
										+ urlPaht);
					} else {// 内部转发
						request.getRequestDispatcher(urlPaht).forward(request,
								response);
					}

				} else {
					chain.doFilter(request, response);
				}
			} else {// ie的url是一个实际页面
				chain.doFilter(request, response);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 取得过滤器初始化参数
		language = filterConfig.getInitParameter("language");
		config = Config.getInit();
	}

	public Map<String, Object> getRequestKey_Value(ServletRequest request) {
		Map<String, Object> map = null; // 页面上的属性和值
		// 获取表单内所有元素
		Enumeration<?> enumeration = request.getParameterNames();
		if (enumeration.hasMoreElements()) {
			map = new HashMap<String, Object>();
			while (enumeration.hasMoreElements()) {
				String inputName = (String) enumeration.nextElement();// 获取元素名
				map.put(inputName, request.getParameter(inputName));// 以表单名作为key
			}
		}
		return map;
	}
	//得到Action的所有拦截器
	public Stack<Interceptor> getInterceptorInAction(String className)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		// 得到拦截器
		Stack<Interceptor> interceptors = new Stack<Interceptor>();
		Stack<String> myInterceptors = config.getAction(className)
				.getMyInterceptors();
		// 实例化所有拦截器
		for (int m = 0; m < myInterceptors.size(); m++) {
			// 加载类
			Class<?> myInterceptorClass = Class.forName(config
					.getInterceptors().get(myInterceptors.get(m)));
			// 实例化类
			Interceptor myInterceptor = (Interceptor) myInterceptorClass
					.newInstance();
			interceptors.add(myInterceptor);
		}
		return interceptors;
	}
	//执行拦截器
	public boolean isPassInterceptor(Stack<Interceptor> interceptors) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		boolean flag = false;
		for (int n = 0; n < interceptors.size(); n++) {
			Interceptor inter = (Interceptor) Proxy.newProxyInstance(
					Interceptor.class.getClassLoader(),
					new Class[] { Interceptor.class },
					new InterceptorProxy(interceptors.get(n)));
			Invocation invocation = new Invocation();
			if ((Boolean) inter.intercept(invocation)) {
				System.out.println("成功登录");
			} else {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
}
