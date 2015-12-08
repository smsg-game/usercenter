<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<jsp:directive.include file="includes/eucbar.jsp" />
<div class="content">
  <form:form commandName="formEmail" htmlEscape="true">
  <form:errors path="*" id="msg" cssClass="errors" element="div" />
    <p>请输入email地址:</p>
    <form:input cssClass="input_box" maxlength="100" id="email" tabindex="1" path="email" autocomplete="off" htmlEscape="true" />
    <p>密码:</p>
    <form:password cssClass="input_box" maxlength="18" id="password" tabindex="2" path="password" autocomplete="off" htmlEscape="true" />
    <p><input type="submit" class="button_1" value="注册" /></p>
  </form:form>
 </div>
<jsp:directive.include file="includes/bottom.jsp" />
