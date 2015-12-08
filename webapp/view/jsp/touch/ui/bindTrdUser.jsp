<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8"%>
<jsp:directive.include file="includes/top.jsp" />
<c:if test="${not empty qryString}">
    	<c:set var="conSymb" value="&" />
	</c:if>
	<c:if test="${empty qryString}">
    	<c:set var="conSymb" value="?" />
	</c:if>
<div class="loginbg">
	<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">关联账号</div>
			</div>
	
<div style="line-height:35px;text-align:center">
<c:choose>
   <c:when test="${not empty sina}">
   	  <form action="${requestScope.contextPath}/user/cancelBind${qryString}" method="POST">
   	     <input type="hidden" name="type" value="${sina}">
		 <input type="hidden" name="page" value="center">	 
	                新浪微博&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="取消关联">
	  </form>
   </c:when>
   <c:otherwise>
	新浪微博&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=1&page=bind"><input type="submit" value="关联"></a><br/>
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
	   腾讯微博&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=2&page=bind"><input type="submit" value="关联"></a><br/>
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
	  腾讯QQ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=5&page=bind"><input type="submit" value="关联"></a><br/>
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
	 人人网&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=3&page=bind"><input type="submit" value="关联"></a><br/>
   </c:otherwise>
</c:choose>
	
	<div class="bottomrusbox">
		<div class="gobackbtn" onclick="window.location.href='${redirUrl}'">返回${backName}</div>
	</div>
<jsp:directive.include file="includes/bottom.jsp" />