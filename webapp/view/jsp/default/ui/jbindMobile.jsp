<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="header">  梵町个人中心 <span style="float:right"><a href="${redirUrl}">返回${backName}</a>&nbsp;&nbsp;</span></div>
<div class="content">
  <p>输入您原来绑定的手机号码和登录密码</p>
  <form:form commandName="formUser" htmlEscape="true">
  <form:errors path="*" id="msg" cssClass="errors" element="div" />
  	原手机号:<br/>
    <form:input cssClass="input_box" maxlength="11" id="mobile" tabindex="1" path="mobile" autocomplete="off" htmlEscape="true" /><br/>
    登录密码：<br/>
    <form:password cssClass="input_box" maxlength="18" id="password" tabindex="2" path="password"  htmlEscape="true" autocomplete="off" /><br/>
    <input type="hidden" name="id" value="${id}" />
    <input type="submit" class="button_1" value="解除绑定" />
  </form:form>
  <br/>
  <a href="${write}/logout${qryString}">退出登录</a>
  </div>
<jsp:directive.include file="includes/bottom.jsp" />