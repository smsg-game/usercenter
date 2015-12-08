<jsp:directive.include file="includes/top.jsp" />
<%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<jsp:directive.include file="includes/eucbar.jsp" />
<div class="content">
  <p>第三步</p>
  <p>重新设置您的密码<p>
  <form:form commandName="formEmail" htmlEscape="true">
  <form:errors path="*" id="msg" cssClass="errors" element="div" />
    <form:password cssClass="input_box" maxlength="18" id="password" tabindex="1" path="password" autocomplete="off" htmlEscape="true" /><br/>
    确认密码<br/>
    <form:password cssClass="input_box" maxlength="18" id="confirm" tabindex="1" path="confirm" autocomplete="off" htmlEscape="true" />
    <input type="hidden" name="secert" value="${secert}" />
    <p><input type="submit" class="button_1" value="重设密码" /></p>
  </form:form>
</div>
<jsp:directive.include file="includes/bottom.jsp" />
