<jsp:directive.include file="includes/top.jsp" /><%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<jsp:directive.include file="includes/eucbar.jsp" />
欢迎使用梵町<br/>
    <p>登录名/手机号:</p>
    <!-- <form:form method="post" commandName="${commandName}">
	<form:errors path="*" id="msg" element="p" /> -->
    <input type="text" maxlength="200" name="username" /><br/>
    <p>密码:</p>
    <input type="password" maxlength="200" name="password" /><br/>
    <p><anchor>登录<go href="/login${qryString}" method="post">
	<postfield name="username" value="$(username)"/>
	<postfield name="password" value="$(password)"/>
	<postfield name="isCookie" value="true"/>
	<postfield name="lt" value="${loginTicket}"/>
	<postfield name="execution" value="${flowExecutionKey}"/>
	<postfield name="_eventId" value="submit"/>
	</go></anchor></p>
	<!-- </form:form> -->
<jsp:directive.include file="includes/bottom.jsp" />