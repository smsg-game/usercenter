<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="header">  梵町个人中心 <span style="float:right"><a href="<%=request.getContextPath()%>/">返回${backName}</a>&nbsp;&nbsp;</span></div>
<div class="content">
  
  <p>为了核实您的身份，请用保密手机 ${mobile} 获取验证码短信</p>
  <form action="<%=request.getContextPath()%>/user/check/getVeriCode" method="post">
  	<input type="hidden" name="id" value="${id}" />
  	<input type="hidden" name="type" value="${type}" />
	<input type="submit" class="button_1" value="获取验证码" />
  </form>
  <p/><p/><p/><p/><p/>
  
  <a href="${write}/logout${qryString}">退出登录</a>
  </div>
<jsp:directive.include file="includes/bottom.jsp" />