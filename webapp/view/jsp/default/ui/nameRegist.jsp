<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<jsp:directive.include file="includes/eucbar.jsp" />
<div class="content">
  <form:form commandName="formUser" htmlEscape="true">
  <form:errors path="*" id="msg" cssClass="errors" element="div" />
    <p>请输入登录名:</p>
    <form:input cssClass="input_box" maxlength="18" id="username" tabindex="1" path="username" autocomplete="off" htmlEscape="true" />
    <p>密码:</p>
    <form:password cssClass="input_box" maxlength="18" id="password" tabindex="2" path="password" autocomplete="off" htmlEscape="true" />
    <p>确认密码</p>
    <form:password cssClass="input_box" maxlength="18" id="confirm" tabindex="2" path="confirm" autocomplete="off" htmlEscape="true" />
    <p><input type="submit" class="button_1" value="注册" /></p>
  </form:form>
 </div>
<jsp:directive.include file="includes/bottom.jsp" />
