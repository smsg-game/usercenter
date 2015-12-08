<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="header">  梵町个人中心 <span style="float:right"><a href="${redirUrl}">返回${backName}</a>&nbsp;&nbsp;</span></div>
<div class="content">
	您好, ${nickName}<br/>
	<a href="<c:url value="${write}${requestScope.contextPath}/user/changeInfo${qryString}" />">修改资料</a><br/>
	绑定手机：
<c:choose>
   <c:when test="${not empty mobile}">
   	${mobile}<br/>
   	<a href="<c:url value="${write}${requestScope.contextPath}/user/check/show?type=bindMobile" />">修改绑定</a> |
   	<a href="<c:url value="${write}${requestScope.contextPath}/user/check/show?type=jbMobile" />">解除绑定</a><br/>
   </c:when>
   <c:otherwise>
	<br/>
	您还未绑定手机号码 &nbsp;&nbsp;&nbsp;&nbsp;  <a href="<c:url value="${write}${requestScope.contextPath}/user/bindMobile${qryString}" />">绑定</a><br/>
   </c:otherwise>
</c:choose>
	<!-- 安全邮箱：
<c:choose>
   <c:when test="${not empty email}">
   	${email}<br/>
   	<a href="<c:url value="${write}${requestScope.contextPath}/user/bindEmail${qryString}" />">修改邮箱</a>|
   	<a href="<c:url value="${write}${requestScope.contextPath}/user/jbEmail${qryString}" />">解除绑定</a><br/>
   </c:when>
   <c:otherwise>
	<br/>
	您还未绑定邮箱 &nbsp;&nbsp;&nbsp;&nbsp;  <a href="<c:url value="${write}${requestScope.contextPath}/user/bindEmail${qryString}" />">绑定</a><br/>
   </c:otherwise>
</c:choose>
-->
	关联帐号：&nbsp;&nbsp;&nbsp;&nbsp; <a href="<c:url value="${write}${requestScope.contextPath}/user/sbindUser${qryString}" />">设置</a><br/>

	帐号安全：<br/>
	<a href="<c:url value="${write}${requestScope.contextPath}/user/changepass${qryString}" />">修改密码</a><br/>	
 <br/>
<a href="${write}/logout${qryString}">退出登录</a>
</div>
<jsp:directive.include file="includes/bottom.jsp" />