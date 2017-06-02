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
	private String className = ""; // ����
	private String methodName = ""; // ������
	private Config config;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws UnsupportedEncodingException {
		className="";
		methodName="";
		request.setCharacterEncoding(language); // ���ñ���
		ActionContext.getContext().setRequest(request);
		try {
			// �õ������ļ���
			// Config config = Config.getInit();
			HttpServletRequest servletRequest = (HttpServletRequest) request;
			// ����session
			ActionContext.getContext().setSession(servletRequest.getSession());
			String uri = servletRequest.getRequestURI();
			String[] sUri = uri.split("/");
			if (sUri.length > 0 && uri.lastIndexOf(".jsp") < 0) {
				className = sUri[sUri.length - 1];
				methodName = config.getAction().get(className).getMethodStr();
			}
			// ����jspҳ��
			if (!className.equals("") || !methodName.equals("")) {
				Map<String, Object> map = getRequestKey_Value(servletRequest); // ��ȡrequest�еļ�ֵ��
				// ������
				Class<?> c = Class.forName(config.getAction(className)
						.getClassPath());
				// ʵ������
				Object o = c.newInstance();
				// ��ȡ���е���������
				Field f[] = c.getDeclaredFields();
				for (int m = 0; m < f.length; m++) {
					if (map.containsKey(f[m].getName())) { // ҳ����඼�и�����
						String firstChar = f[m].getName().substring(0, 1)
								.toUpperCase();
						String setMethod = "set" + firstChar
								+ f[m].getName().substring(1);
						Method setPro = o.getClass().getMethod(setMethod,
								f[m].getType());
						setPro.invoke(o, map.get(f[m].getName()));
					}

				}
				// �õ�������
				Stack<Interceptor> interceptors = getInterceptorInAction(className);
				//ִ��������
				boolean flag = isPassInterceptor(interceptors);
	
				Object result = null;
				if (flag) {
					result = "false";
				} else {
					// ȡ�÷���
					Method method = o.getClass().getMethod(methodName);
					// ִ�з���
					result = method.invoke(o);
				}

				if (result != null && !result.equals("")) {
					// ȡ�÷���·��
					String forwardPath = (String) result;
					// ת����ʽ
					String urlType = config.getAction(className).getLabel()
							.get(forwardPath).getType();
					// ת��·��
					String urlPaht = config.getAction(className).getLabel()
							.get(forwardPath).getValue();
					if (urlType.equals("redetion")) {// �ⲿת��
						((HttpServletResponse) response)
								.sendRedirect(servletRequest.getContextPath()
										+ urlPaht);
					} else {// �ڲ�ת��
						request.getRequestDispatcher(urlPaht).forward(request,
								response);
					}

				} else {
					chain.doFilter(request, response);
				}
			} else {// ie��url��һ��ʵ��ҳ��
				chain.doFilter(request, response);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// ȡ�ù�������ʼ������
		language = filterConfig.getInitParameter("language");
		config = Config.getInit();
	}

	public Map<String, Object> getRequestKey_Value(ServletRequest request) {
		Map<String, Object> map = null; // ҳ���ϵ����Ժ�ֵ
		// ��ȡ��������Ԫ��
		Enumeration<?> enumeration = request.getParameterNames();
		if (enumeration.hasMoreElements()) {
			map = new HashMap<String, Object>();
			while (enumeration.hasMoreElements()) {
				String inputName = (String) enumeration.nextElement();// ��ȡԪ����
				map.put(inputName, request.getParameter(inputName));// �Ա�����Ϊkey
			}
		}
		return map;
	}
	//�õ�Action������������
	public Stack<Interceptor> getInterceptorInAction(String className)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		// �õ�������
		Stack<Interceptor> interceptors = new Stack<Interceptor>();
		Stack<String> myInterceptors = config.getAction(className)
				.getMyInterceptors();
		// ʵ��������������
		for (int m = 0; m < myInterceptors.size(); m++) {
			// ������
			Class<?> myInterceptorClass = Class.forName(config
					.getInterceptors().get(myInterceptors.get(m)));
			// ʵ������
			Interceptor myInterceptor = (Interceptor) myInterceptorClass
					.newInstance();
			interceptors.add(myInterceptor);
		}
		return interceptors;
	}
	//ִ��������
	public boolean isPassInterceptor(Stack<Interceptor> interceptors) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		boolean flag = false;
		for (int n = 0; n < interceptors.size(); n++) {
			Interceptor inter = (Interceptor) Proxy.newProxyInstance(
					Interceptor.class.getClassLoader(),
					new Class[] { Interceptor.class },
					new InterceptorProxy(interceptors.get(n)));
			Invocation invocation = new Invocation();
			if ((Boolean) inter.intercept(invocation)) {
				System.out.println("�ɹ���¼");
			} else {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
}
