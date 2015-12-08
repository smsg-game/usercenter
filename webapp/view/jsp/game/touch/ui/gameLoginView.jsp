<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="weibo4j.util.WeiboConfig"%>
<%@ page import="com.easou.usercenter.util.BizLogUtil"%>
<%@ page import="com.easou.usercenter.util.ConditionUtil"%>
<%
response.setDateHeader("Expires", 0);
response.setHeader("Cache-Control","no-cache");
response.setHeader("Cache-Control","no-store");
%>
<jsp:directive.include file="includes/top.jsp" />
<c:if test="${not empty qryString}">
    	<c:set var="conSymb" value="&" />
</c:if>
<c:if test="${empty qryString}">
    	<c:set var="conSymb" value="?" />
</c:if>
<body>

<header>
梵町账号${deploy}
<a class="comeBack icon" onclick="javascript:history.go(-1)"></a>
</header>

<div class="content-button">
<form:form method="post" id="fm1" cssClass="fm-v clearfix"
		commandName="${commandName}" htmlEscape="true">
账号：<form:input id="username" path="username" class="input" type="text" placeholder="登录名/手机号"  style="width:200px"
htmlEscape="true" autocomplete="off" /><br><br>
密码：<form:input  id="password" name="password" path="password" class="input" type="password" placeholder="请输入密码"  style="width:200px"
htmlEscape="true" autocomplete="off" />
</div>
<input type="hidden" name="lt" value="${loginTicket}" /> 
<input type="hidden" name="execution" value="${flowExecutionKey}" /> 
<!-- 自动登录标识 -->
<input type="hidden" id="isCookie" name="isCookie" value="true"/>
<input type="hidden" name="_eventId" value="submit" />
	<div class="content-button">
	 <input class="button-blue" id="loginButton" type="submit" value="登  录"/><br>
	 <a class="text-blue" href="${write}${requestScope.contextPath}/game/touch/sforget${qryString}">忘记密码？</a>
	</div>	
	<form:errors path="*" id="msg" cssClass="login-layer" element="div"  onclick="closeTisi();"/>
</form:form>
<div style="border-bottom:1px #abadae solid; height:20px"></div>

	
<div class="account">
	<h2>使用其他账号登录：</h2>
	<ul class="clearFix">
		<li ><span class="account-sina infoIcon"></span><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=1&source=33">新浪微博</a></li>
		<li ><span class="account-qq infoIcon"></span><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=5&source=33">QQ账号</a></li>
		<li ><span class="account-tencent infoIcon"></span><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=2&source=33">腾讯微博</a></li>
		<li ><span class="account-renren infoIcon"></span><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=3&source=33">人人网</a></li>
	</ul>
</div>
<script>
function closeTisi() {
	document.getElementById("msg").style.display="none";
}
</script>
<jsp:directive.include file="includes/bottom.jsp" />