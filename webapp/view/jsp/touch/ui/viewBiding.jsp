<%@ page contentType="text/html; charset=UTF-8"%>
<jsp:directive.include file="includes/top.jsp" />
<div class="loginbg">
	<div class="logintopwarp">
		<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
		<div class="slogan">欢迎登录梵町！</div>
	</div>
	<div class="information1">
	    <a href="<c:url value="${requestScope.contextPath}/oDefRegist${qryString}" />">请直接进入</a><br/>
		
	</div>
<jsp:directive.include file="includes/bottom.jsp" />

