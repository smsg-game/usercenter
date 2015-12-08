<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="header">  梵町个人中心 <span style="float:right"><a href="${redirUrl}">返回${backName}</a>&nbsp;&nbsp;</span></div>
<div class="content">
  <form:form commandName="formUser" htmlEscape="true">
  <form:errors path="*" id="msg" cssClass="errors" element="div" />
  	<p>用户名:&nbsp;&nbsp;${name}</p>
  	<p>昵称:(2~16位,中文2位)</p>
    <form:input path="nickName" id="nickName" cssClass="input_box" maxlength="20" htmlEscape="true"/>
    <p>城市 :(例如 深圳)</p>
    <form:input path="city" id="city" cssClass="input_box" maxlength="10" htmlEscape="true"/>
    <p>性别:</p>
    <c:forEach var="ps" items="p">
  <tr>
<form:select path="sex" name="sex" id="sex">
<option value="10" <c:if test="${formUser.sex==10}">selected</c:if>>-</option>
<option value="0" <c:if test="${formUser.sex==0}">selected</c:if>>男</option>
<option value="1" <c:if test="${formUser.sex==1}">selected</c:if>>女</option>
</form:select>
</tr>
</c:forEach>
    <input type="hidden" name="d" value="${d}" />
    <p><input type="submit" class="button_1" value="确定" /></p>
  </form:form>
<a href="${write}/logout${qryString}">退出登录</a>
 </div>
<jsp:directive.include file="includes/bottom.jsp" />
