<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="header">  梵町个人中心 <span style="float:right"><a href="${redirUrl}">返回${backName}</a>&nbsp;&nbsp;</span></div>
<div class="content">
  <p>输入您现在的邮箱地址和登录密码</p>
  <form:form commandName="formEmail" htmlEscape="true">
  <form:errors path="*" id="msg" cssClass="errors" element="div" />
  	现邮箱地址<br/>
    <form:input cssClass="input_box" maxlength="100" id="email" tabindex="1" path="email" autocomplete="off" htmlEscape="true" /><br/>
    登录密码：<br/>
    <form:password cssClass="input_box" maxlength="18" id="password" tabindex="2" path="password"  htmlEscape="true" autocomplete="off" /><br/>
    <input type="hidden" name="id" value="${id}" />
    <input type="submit" class="button_1" value="解除绑定" />
  </form:form>
  
  <a href="${write}/logout${qryString}">退出登录</a>
  </div>
<jsp:directive.include file="includes/bottom.jsp" />