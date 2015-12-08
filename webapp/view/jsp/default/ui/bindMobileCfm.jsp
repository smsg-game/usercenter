<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="content">
  <p>请输入你在手机上收到的验证码</p>
  <form:form commandName="formVerify" htmlEscape="true">
  	<form:errors path="*" id="msg" cssClass="errors" element="div" />
    <form:input cssClass="input_box" maxlength="6" id="veriCode" tabindex="1" path="veriCode" autocomplete="off" htmlEscape="true" />
    <input type="hidden" name="id" value="${id}" />
    <input type="hidden" name="mobile" id="mobile" value="${mobile}" />
    <input type="hidden" name="act" value="verify" />
    <p><input type="submit" class="button_1" value="确定" /></p>
  </form:form>
 </div>
<jsp:directive.include file="includes/bottom.jsp" />