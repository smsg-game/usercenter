<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<jsp:directive.include file="includes/eucbar.jsp" />
<div class="content">
  <p>请填写绑定邮箱</p>
  <form:form commandName="formEmail" htmlEscape="true">
  <form:errors path="*" id="msg" cssClass="errors" element="div" />
    <form:input cssClass="input_box" maxlength="200" id="email" tabindex="1"  path="email" htmlEscape="true" />
    <input type="submit" class="button_1" value="下一步"/>
  </form:form>
</div>
<jsp:directive.include file="includes/bottom.jsp" />
