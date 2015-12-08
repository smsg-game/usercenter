<jsp:directive.include file="includes/top.jsp" />
<%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<div class="header">  梵町个人中心 <span style="float:right"><a href="<%=request.getAttribute("redirUrl").toString().replaceAll("&", "&amp;")%>">返回${backName}</a>&nbsp;&nbsp;</span></div>
<div class="content">
  <p>输入您原来绑定的手机号码和登录密码</p>
  <!-- <form:form commandName="formUser" htmlEscape="true">
  <form:errors path="*" id="msg" cssClass="errors" element="div" /> -->
  	原手机号:<br/>
    <input type="text" cssClass="input_box" maxlength="11" id="mobile" name="mobile" tabindex="1" path="mobile"  htmlEscape="true" /><br/>
    登录密码：<br/>
    <input type="password" cssClass="input_box" maxlength="18" id="password" name="password" tabindex="2" path="password"  htmlEscape="true"  /><br/>
    <p><anchor>解除绑定<go href="/user/jbMobile${qryString}" method="post" accept-charset="utf-8">
        <postfield name="mobile" value="$(mobile)"/>
        <postfield name="password" value="$(password)"/>
        <postfield name="id" value="${id}"/>
    </go></anchor></p>
  <!-- </form:form> -->
  <br/>
  <a href="${write}/logout${qryString}">退出登录</a>
  </div>
<jsp:directive.include file="includes/bottom.jsp" />