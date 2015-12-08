<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<jsp:directive.include file="includes/eucerr.jsp" />
<div class="content">
  <form action="<c:url value="${requestScope.contextPath}/pass/newpwd${qryString}" />" name="passwdForm" method="post">
    <p>请输入您的新密码:</p>
    <input type="password" name="newpwd" id="newpwd" class="input_box" maxlength="18"/>
    <p>请再次输入您的密码:</p>
    <input type="password" name="confirm" id="confirm" class="input_box" maxlength="18"/>
    <input type="hidden" name="username" value="${username}" />
    <input type="hidden" name="answer" value="${answer}" />
    <p><input type="submit" class="button_1" value="确定并登录"></p>
  </form>
 </div>
<jsp:directive.include file="includes/bottom.jsp" />
