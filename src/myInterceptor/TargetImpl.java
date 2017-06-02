package myInterceptor;

public class TargetImpl implements Target {
	
	
    @Override
    public String execute() {
        return "success";
    }
}