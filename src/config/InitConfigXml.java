package config;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class InitConfigXml extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InitConfigXml() {
		super();
	}

	public void destroy() {
		super.destroy(); 
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public void init() throws ServletException {
		ServletConfig config = getServletConfig();
		//取得服务目录路径
		String path1 = config.getServletContext().getRealPath("/");
		//xml的路径
		String path = config.getInitParameter("configXml");
		System.out.println(path+"enter1");
		getInit(path1+path);
	}
	
	@SuppressWarnings("unchecked")
	public void getInit(String xmlConfigPath){
		//读取文件
		File file = new File(xmlConfigPath);
		SAXReader reader = new SAXReader();
		 try {
			Document document = reader.read(file);
			//标签[根标签]
			Element rootElement = document.getRootElement();
			if(rootElement!=null){
				//初始化容器[1]
				Config dudu = Config.getInit();
				Element interceptors = rootElement.element("interceptors");
				if(interceptors != null){
					//存储拦截器 name,path
					Map<String,String> interceptorMap = 
							new HashMap<String, String>();
					List<Element> interceptor = interceptors.elements("interceptor");
					for(int i = 0 ; i < interceptor.size() ; i++){
						Element eInterceptor = interceptor.get(i);
						Attribute attribute = eInterceptor.attribute("name");
						String name = attribute.getValue();
						Attribute attributeClass = eInterceptor.attribute("class");
						String classPath = attributeClass.getValue();
						interceptorMap.put(name,classPath);
					}
					dudu.setInterceptors(interceptorMap);
				}
				System.out.println("interceptor size:"+dudu.getInterceptors().size());
				//读取actions标签
				Element actions = rootElement.
				element("actions");
				/*******************action******************/
				if(actions!=null){
					//存储action集合【2】
					Map<String,Action> mapAction = new HashMap<String,Action>();
					//读取所有的action标签
					List<Element> action = actions.elements("action");
					for(int i=0;i<action.size();i++){
						Element eAction=action.get(i);//取得action
						//去action下的name属性
						Attribute attribute = 
							eAction.attribute("name");
						//action 名字
						String name = attribute.getValue();
						
						Attribute attributeClass = 
							eAction.attribute("class");
						//类的路径
						String classPath = attributeClass.getValue();
						//执行方法
						Attribute attributeMethod = eAction.attribute("method");
						String methodStr = attributeMethod.getValue();
						
						Action action_ = new Action();
						action_.setName(name);
						action_.setClassPath(classPath);
						action_.setMethodStr(methodStr);
						/*************result**************/
						List<Element> result = 
							action.get(i).elements("result");
						//result 集合
						Map<String,TheLabel> mapResult = new HashMap<String,TheLabel>();
						if(result!=null){
							for(int j=0;j<result.size();j++){
								Attribute aResult = result.get(j).attribute("name");
								//result标签的name属性值
								String resultName = aResult.getValue();
								Attribute aType = result.get(j).attribute("type");
								//如果ressult 中type没有默认为从内部转发
								String resultType = "redirect";//result标签的type属性值
								if(aType!=null){//没有type属性
									//result标签的type属性值
									resultType = aType.getValue();
								}
								TheLabel result_= new TheLabel();
								result_.setName(resultName);
								result_.setType(resultType);
								//result 标签的值
								String resutlText = result.get(j).getText();
								resutlText = resutlText==null?"":resutlText.trim();
								result_.setValue(resutlText);
								//添加result对象到map里
								mapResult.put(resultName, result_);
							}
						}
						//把result集合放到action里
						action_.setLabel(mapResult);
						//拦截器集合
						List<Element> interceptorRefs = 
								action.get(i).elements("interceptor-ref");
						Stack<String> myInterceptorStack = new Stack<String>();
						if(interceptorRefs != null){
							for(int k = 0 ; k < interceptorRefs.size() ; k++){
								Attribute interceptorRef =interceptorRefs.get(k).attribute("name");
								String interceptorRefName = interceptorRef.getValue();
								myInterceptorStack.add(interceptorRefName);
							}
						}
						/********************************/
						action_.setMyInterceptors(myInterceptorStack);
						System.out.println(myInterceptorStack.size()+"enter3");
						mapAction.put(name, action_);
					}
					dudu.setAction(mapAction);//添加action到容器中
				}
				/*******************************************/
			
			}
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
