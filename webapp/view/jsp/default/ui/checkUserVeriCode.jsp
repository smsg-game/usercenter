<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="header">  梵町个人中心 <span style="float:right"><a href="<%=request.getContextPath()%>/">返回${backName}</a>&nbsp;&nbsp;</span></div>
<div class="content">
  
  <p>为了核实您的身份，请输入验证码并验证</p>
  <font style="color: red;" size="2">提示:如不能正常收到验证码短信请刷新本页重新获取。</font>
  <p/><p/><p/><p/><p/>
  <form action="<%=request.getContextPath()%>/user/check/checkUserIdentified" method="post">
  	<form:errors path="*" id="msg" cssClass="errors" element="div" />
  	
  	<input type="hidden" name="id" value="${id}" />
  	<input type="hidden" name="type" value="${type}" />
  	
  	保密手机: ${mobile}<p/>
  	验证码：<input type="text" name="veriCode"/>
  	<span style="color: red;">
  		${errorUser}
  		${notvaildMobile}
  	</span>
  	<p/>
	<input type="submit" class="button_1" value="提交验证" />
  </form>
  <p/><p/><p/><p/><p/>
  
  <a href="${write}/logout${qryString}">退出登录</a>
  </div>
<jsp:directive.include file="includes/bottom.jsp" />