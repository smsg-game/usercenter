<jsp:directive.include file="includes/top.jsp" />
<%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<jsp:directive.include file="includes/eucbar.jsp" />
	<p>一、如果您是梵町用户或小说书柜用户请<a href="<c:url value="${read}${requestScope.contextPath}/login${qryString}" />">直接登录</a></p>
   	<p>二、短信注册</p>
   	<p>设置登录密码,编写6-18位英文或数字(区分大小写)</p>
   	<p>移动用户发送至:<a href="sms:1065755802911">1065755802911</a></p>
   	<p>联通用户发送至:<a href="sms:1065505937511">1065505937511</a></p>
   	<p>电信用户发送到:<a href="sms:1065902020209111">1065902020209111</a></p>
   	<p>系统确认后即可<a href="<c:url value="${read}${requestScope.contextPath}/login${qryString}" />">直接登录</a>,登录名为您发送短信的手机号</p>
  	<!-- <p>三、<a href="<c:url value="${requestScope.contextPath}/emregist${qryString}" />">邮箱注册</a></p>-->
  	<!-- p>三、<a href="<c:url value="${requestScope.contextPath}/nregist${qryString}" />">登录名注册</a></p-->
<jsp:directive.include file="includes/bottom.jsp" />