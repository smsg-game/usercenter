<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="header">  梵町个人中心 <span style="float:right"><a href="${redirUrl}">返回${backName}</a>&nbsp;&nbsp;</span></div>
<div class="content">
<jsp:directive.include file="includes/eucerr.jsp" />
   请在下面输入您要设置的新邮箱,如已设置,请返回<br/>
   <form:form commandName="formEmail" htmlEscape="true">
   	<form:errors path="*" id="msg" cssClass="errors" element="div" />
   	<p>新邮箱</p>
    <form:input cssClass="input_box" maxlength="100" id="email" tabindex="1" path="email" autocomplete="off" htmlEscape="true" />
    <p>登录密码:</p>
    <form:password cssClass="input_box" maxlength="18" id="password" tabindex="2" path="password"  htmlEscape="true" autocomplete="off" />
    <input type="hidden" name="id" value="${id}" /-->
    <p><input type="submit" class="button_1" value="确定"></p>
  </form:form>
  <br />
  <a href="${write}/logout${qryString}">退出登录</a>
  </div>
<jsp:directive.include file="includes/bottom.jsp" />