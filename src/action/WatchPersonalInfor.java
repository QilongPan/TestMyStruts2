package action;

import config.ActionContext;
import entity.User;
import myInterceptor.Target;

public class WatchPersonalInfor implements Target{

	@Override
	public String execute() {
		User user = new User();
		user.setAge(21);
		user.setSex("��");
		user.setUserName("С��");
		ActionContext.getContext().getSession().setAttribute("user", user);
		return "success";
	}

}
