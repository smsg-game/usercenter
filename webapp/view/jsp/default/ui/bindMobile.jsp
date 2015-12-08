<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="header">  梵町个人中心 <span style="float:right"><a href="${redirUrl}">返回${backName}</a>&nbsp;&nbsp;</span></div>
<div class="content">
  <p>输入您要绑定的手机号码，系统将以短信方式发送验证码到该手机上</p>
  <form:form commandName="formUser" htmlEscape="true">
  <form:errors path="*" id="msg" cssClass="errors" element="div" />
    <form:input cssClass="input_box" maxlength="11" id="mobile" tabindex="1" path="mobile" autocomplete="off" htmlEscape="true" />
    <input type="hidden" name="id" value="${id}" />
    <p><input type="submit" class="button_1" value="获取验证码" /></p>
  </form:form>
  
  <c:choose>
   <c:when test="${not empty curMobile}">
   <form action="<c:url value="${requestScope.contextPath}/user/delMobile${qryString}" />" id="myform" method="post">
   <input type="hidden" name="id" value="${id}" />
       <p>您也可以<input type="submit" class="button_1" value="取消绑定" /></p>
       </form>
     </c:when>
  </c:choose>
  
  <a href="${write}/logout${qryString}">退出登录</a>
  </div>
<jsp:directive.include file="includes/bottom.jsp" />