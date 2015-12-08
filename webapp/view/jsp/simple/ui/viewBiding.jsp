<jsp:directive.include file="includes/top.jsp" />
<%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<!-- jsp:directive.include file="includes/eucbar.jsp" /-->
	 <br />
	 	 欢迎登录梵町！<br>
	    <a href="<c:url value="${requestScope.contextPath}/oDefRegist${qryString}" />">请直接进入</a><br/>
		<!-- br />
		2、我还没有梵町账号，现在<a href="<c:url value="${requestScope.contextPath}/oregist${qryString}" />">申请</a>
		<br /-->
<jsp:directive.include file="includes/bottom.jsp" />

