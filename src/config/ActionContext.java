package config;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
/*
 * Action的上下文
 */
public class ActionContext {

	private ServletRequest request;
	private HttpSession session;

    private static ActionContext actionContext;
	
	public synchronized static ActionContext  getContext(){
		if(actionContext==null){
			actionContext = new ActionContext ();
		}
		return actionContext;
	}

	

	public ServletRequest getRequest() {
		return request;
	}



	public void setRequest(ServletRequest request) {
		this.request = request;
	}



	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}
	
	

}
