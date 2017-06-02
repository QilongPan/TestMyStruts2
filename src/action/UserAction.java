package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import myInterceptor.Target;
import config.ActionContext;

public class UserAction implements Target{
	
	private String userName; 
	private String password; 
	
	public 	String login(){
		System.out.println("baocun"+userName);
		HttpServletRequest request= (HttpServletRequest) ActionContext.getContext().getRequest();
		request.setAttribute("my", "hello world!");
		System.out.println(request.getParameter("userName")+"enter1");
		return "success";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String execute() {
		if(userName.equals("admin")
				&&password.equals("admin")){
			HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().getRequest();
			request.setAttribute("userName",userName);
			HttpSession session = ActionContext.getContext().getSession();
			session.setAttribute("userName", userName);
			return "success";
		}
		return "false";
	}
	
	

}
