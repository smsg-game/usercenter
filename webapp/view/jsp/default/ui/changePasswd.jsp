<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="header">  梵町个人中心 <span style="float:right"><a href="${redirUrl}">返回${backName}</a>&nbsp;&nbsp;</span></div>
<div class="content">
  <form:form commandName="formPass" htmlEscape="true">
  <form:errors path="*" id="msg" cssClass="errors" element="div" />
  <c:if test="${empty nullPass}">
  	<p>原密码:</p>
    <form:password path="passwd" id="passwd" cssClass="input_box" maxlength="20" htmlEscape="true"/>
    </c:if>
    <p>新密码:</p>
    <form:password path="newpwd" id="newpwd" cssClass="input_box" maxlength="20" htmlEscape="true"/>
    <p>确认密码:</p>
    <form:password path="confirm" id="confirm" cssClass="input_box" maxlength="20" htmlEscape="true"/>
    <input type="hidden" name="d" value="${d}" />
    <p><input type="submit" class="button_1" value="确定" /></p>
  </form:form>
 <a href="${write}/logout${qryString}">退出登录</a>
 </div>
<jsp:directive.include file="includes/bottom.jsp" />
