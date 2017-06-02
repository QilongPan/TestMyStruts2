<%@ page language="java" import="java.util.*,entity.User" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'watchPersonalInfor.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <div style="height:400px; width:400px; margin:0 auto;">
    	<%User user =(User)session.getAttribute("user"); %>
    	姓名:<input type="text" value="<%=user.getUserName()%>"/><br/>
    	性别:<input type="text" value="<%=user.getSex() %>"/><br/>
    	年龄:<input type="text" value="<%=user.getAge() %>"/><br/>
    	<a href="operate.jsp">返回</a>
    </div>
  </body>
</html>
