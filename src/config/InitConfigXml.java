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
		//ȡ�÷���Ŀ¼·��
		String path1 = config.getServletContext().getRealPath("/");
		//xml��·��
		String path = config.getInitParameter("configXml");
		System.out.println(path+"enter1");
		getInit(path1+path);
	}
	
	@SuppressWarnings("unchecked")
	public void getInit(String xmlConfigPath){
		//��ȡ�ļ�
		File file = new File(xmlConfigPath);
		SAXReader reader = new SAXReader();
		 try {
			Document document = reader.read(file);
			//��ǩ[����ǩ]
			Element rootElement = document.getRootElement();
			if(rootElement!=null){
				//��ʼ������[1]
				Config dudu = Config.getInit();
				Element interceptors = rootElement.element("interceptors");
				if(interceptors != null){
					//�洢������ name,path
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
				//��ȡactions��ǩ
				Element actions = rootElement.
				element("actions");
				/*******************action******************/
				if(actions!=null){
					//�洢action���ϡ�2��
					Map<String,Action> mapAction = new HashMap<String,Action>();
					//��ȡ���е�action��ǩ
					List<Element> action = actions.elements("action");
					for(int i=0;i<action.size();i++){
						Element eAction=action.get(i);//ȡ��action
						//ȥaction�µ�name����
						Attribute attribute = 
							eAction.attribute("name");
						//action ����
						String name = attribute.getValue();
						
						Attribute attributeClass = 
							eAction.attribute("class");
						//���·��
						String classPath = attributeClass.getValue();
						//ִ�з���
						Attribute attributeMethod = eAction.attribute("method");
						String methodStr = attributeMethod.getValue();
						
						Action action_ = new Action();
						action_.setName(name);
						action_.setClassPath(classPath);
						action_.setMethodStr(methodStr);
						/*************result**************/
						List<Element> result = 
							action.get(i).elements("result");
						//result ����
						Map<String,TheLabel> mapResult = new HashMap<String,TheLabel>();
						if(result!=null){
							for(int j=0;j<result.size();j++){
								Attribute aResult = result.get(j).attribute("name");
								//result��ǩ��name����ֵ
								String resultName = aResult.getValue();
								Attribute aType = result.get(j).attribute("type");
								//���ressult ��typeû��Ĭ��Ϊ���ڲ�ת��
								String resultType = "redirect";//result��ǩ��type����ֵ
								if(aType!=null){//û��type����
									//result��ǩ��type����ֵ
									resultType = aType.getValue();
								}
								TheLabel result_= new TheLabel();
								result_.setName(resultName);
								result_.setType(resultType);
								//result ��ǩ��ֵ
								String resutlText = result.get(j).getText();
								resutlText = resutlText==null?"":resutlText.trim();
								result_.setValue(resutlText);
								//���result����map��
								mapResult.put(resultName, result_);
							}
						}
						//��result���Ϸŵ�action��
						action_.setLabel(mapResult);
						//����������
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
					dudu.setAction(mapAction);//���action��������
				}
				/*******************************************/
			
			}
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
