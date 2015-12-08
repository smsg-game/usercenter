<jsp:directive.include file="includes/top.jsp" />
<%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<p>梵町个人中心 <span style="float:right"><a href="<%=request.getAttribute("redirUrl").toString().replaceAll("&", "&amp;")%>">返回${backName}</a>&nbsp;&nbsp;</span></p>
  <p>输入您现在的邮箱地址和登录密码</p>
  <!-- <form:form commandName="formEmail" htmlEscape="true">
  <form:errors path="*" id="msg" cssClass="errors" element="div" /> -->
  	现邮箱地址<br/>
    <input cssClass="input_box" maxlength="100" id="email" tabindex="1" name="email"  htmlEscape="true" /><br/>
    登录密码：<br/>
    <input type="password" cssClass="input_box" maxlength="18" id="password" tabindex="2" name="password"  htmlEscape="true"  /><br/>
    <p><anchor>解除绑定<go href="/user/jbEmail${qryString}" method="post" accept-charset="utf-8">
        <postfield name="email" value="$(email)"/>
        <postfield name="password" value="$(password)"/>
        <postfield name="id" value="${id}"/>
    </go></anchor></p>
  <!-- </form:form> -->
  
  <a href="${write}/logout${qryString}">退出登录</a>
<jsp:directive.include file="includes/bottom.jsp" />