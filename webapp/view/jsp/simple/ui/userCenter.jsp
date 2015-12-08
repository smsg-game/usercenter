<jsp:directive.include file="includes/top.jsp" />
<%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<p>  梵町个人中心, <a href="<%=request.getAttribute("redirUrl").toString().replaceAll("&", "&amp;")%>">返回${backName}</a></p>
	您好, ${nickName}<br/>
	<a href="<c:url value="${write}${requestScope.contextPath}/user/changeInfo${qryString}" />">修改资料</a><br/>
	绑定手机：
<c:choose>
   <c:when test="${not empty mobile}">
   	${mobile}<br/>
   	<a href="<c:url value="${write}${requestScope.contextPath}/user/bindMobile${qryString}" />">修改绑定</a>|<a href="<c:url value="${write}${requestScope.contextPath}/user/jbMobile${qryString}" />">解除绑定</a><br/>
   </c:when>
   <c:otherwise>
	<br/>
	您还未绑定手机号码 &nbsp;&nbsp; <a href="<c:url value="${write}${requestScope.contextPath}/user/bindMobile${qryString}" />">绑定</a><br/>
   </c:otherwise>
</c:choose>
<!-- 
	安全邮箱：
<c:choose>
   <c:when test="${not empty email}">
   	${email}<br/>
   	<a href="<c:url value="${write}${requestScope.contextPath}/user/bindEmail${qryString}" />">修改邮箱</a>|<a href="<c:url value="${write}${requestScope.contextPath}/user/jbEmail${qryString}" />">解除绑定</a><br/>
   </c:when>
   <c:otherwise>
	<br/>
	您还未绑定邮箱 &nbsp;&nbsp; <a href="<c:url value="${write}${requestScope.contextPath}/user/bindEmail${qryString}" />">绑定</a><br/>
   </c:otherwise>
</c:choose>
-->
	帐号安全：<br/>
	<a href="<c:url value="${write}${requestScope.contextPath}/user/changepass${qryString}" />">修改密码</a><br/>	
 <br/>
<a href="${write}/logout${qryString}">退出登录</a>
<jsp:directive.include file="includes/bottom.jsp" />