<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<jsp:directive.include file="includes/eucbar.jsp" />
<jsp:directive.include file="includes/eucerr.jsp" />
<div class="content">
  <form action="<c:url value="${requestScope.contextPath}/pass/resetpass${qryString}" />" id="myform" method="post">
    <p>1. 在这里输入您的新密码:</p>
    <input type="password" name="newPwd" id="newPwd" class="input_box" maxlength="18"/>
    <p>请再次输入您的密码:</p>
    <input type="password" name="confirm" id="confirm" class="input_box" maxlength="18"/>
     <p>2. 在这里输入您收到的验证码:</p>
    <input type="text" name="veriCode" id="veriCode" class="input_box" maxlength="6" value="${veriCode}"/><br/>
    <input type="hidden" name="mobile" value="${mobile}"/>
    <p><input type="submit" class="button_1" value="确定并登录"></p>
  </form>
 </div>
<jsp:directive.include file="includes/bottom.jsp" />