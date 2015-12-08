<jsp:directive.include file="includes/top.jsp" /><%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<p>梵町个人中心 <span style="float:right"><a href="<%=request.getAttribute("redirUrl").toString().replaceAll("&", "&amp;")%>">返回${backName}</a>&nbsp;&nbsp;</span></p>
<jsp:directive.include file="includes/eucerr.jsp" />
   请在下面输入您要设置的新邮箱,如已设置,请返回<br/><!-- 
   <form:form commandName="formEmail" htmlEscape="true">
   	<form:errors path="*" id="msg" cssClass="errors" element="div" /> -->
   	<p>新邮箱</p>
    <input cssClass="input_box" maxlength="100" id="email" tabindex="1" name="email"  htmlEscape="true" />
    <p>登录密码:</p>
    <input type="password" cssClass="input_box" maxlength="18" id="password" tabindex="2" name="password" />
    <p><anchor>确定<go href="/user/bindEmail${qryString}" method="post" accept-charset="utf-8">
        <postfield name="email" value="$(email)"/>
        <postfield name="password" value="$(password)"/>
        <postfield name="id" value="${id}"/>
    </go></anchor></p>
  <!-- </form:form> -->
  <br />
  <a href="${write}/logout${qryString}">退出登录</a>
<jsp:directive.include file="includes/bottom.jsp" />