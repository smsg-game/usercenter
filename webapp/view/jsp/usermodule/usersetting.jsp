<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="header">  梵町个人中心 <span style="float:right"><a href="${redirUrl}">返回${backName}</a>&nbsp;&nbsp;</span></div>
<div class="content">
	您好, ${nickName}<br/>
	<c:choose>
		<c:when test="${not empty nickName}">
			<input type="text" value="${nickName}" name="nickName">
		</c:when>
		<c:otherwise>
			<input type="text" value="设置昵称" name="nickName">
		</c:otherwise>
	</c:choose>
	<br/>
	绑定手机：
<c:choose>
   <c:when test="${not empty mobile}">
   	${mobile}<br/>
   	<a href="<c:url value="${write}${requestScope.contextPath}/user/mbindMobile${qryString}" />">修改绑定</a> |
   	<a href="<c:url value="${write}${requestScope.contextPath}/user/jbMobile${qryString}" />">解除绑定</a><br/>
   </c:when>
   <c:otherwise>
	<br/>
	您还未绑定手机号码 &nbsp;&nbsp;&nbsp;&nbsp;  <a href="<c:url value="${write}${requestScope.contextPath}/user/mbindMobile${qryString}" />">绑定</a><br/>
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
 
<c:choose>
   <c:when test="${not empty sina}">
   	  <form action="${requestScope.contextPath}/user/cancelBind${qryString}" method="POST">
   	     <input type="hidden" name="type" value="${sina}">
		 <input type="hidden" name="page" value="center">	 
	新浪微博&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="取消关联">
	  </form>
   </c:when>
   <c:otherwise>
	新浪微博&nbsp;&nbsp;&nbsp;&nbsp;<a href="${requestScope.contextPath}/oRedirect?t=1&page=bind&uv=module"><input type="submit" value="关联"></a><br/>
   </c:otherwise>
</c:choose>
<c:choose>
   <c:when test="${not empty tqq}">
   	  <form action="${requestScope.contextPath}/user/cancelBind${qryString}" method="POST">
		<input type="hidden" name="type" value="${tqq}">
		<input type="hidden" name="page" value="center">
		 腾讯微博&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="取消关联">
	  </form>
   </c:when>
   <c:otherwise>
	   腾讯微博&nbsp;&nbsp;&nbsp;&nbsp;<a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=2&page=bind"><input type="submit" value="关联"></a><br/>
   </c:otherwise>
</c:choose>
<c:choose>
   <c:when test="${not empty qq}">
   	  <form action="${requestScope.contextPath}/user/cancelBind${qryString}" method="POST">
		<input type="hidden" name="type" value="${qq}">
		<input type="hidden" name="page" value="center">
		腾讯QQ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="取消关联">
	  </form>
   </c:when>
   <c:otherwise>
	  腾讯QQ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=5&page=bind"><input type="submit" value="关联"></a><br/>
   </c:otherwise>
</c:choose>
<c:choose>
   <c:when test="${not empty renren}">
   	  <form action="${requestScope.contextPath}/user/cancelBind${qryString}" method="POST">
		<input type="hidden" name="type" value="${renren}">
		<input type="hidden" name="page" value="center">
		人人网&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="取消关联">
	  </form>
   </c:when>
   <c:otherwise>
	 人人网&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=3&page=bind"><input type="submit" value="关联"></a><br/>
   </c:otherwise>
</c:choose>
<a href="${write}/logout${qryString}">退出登录</a>
</div>
<jsp:directive.include file="includes/bottom.jsp" />