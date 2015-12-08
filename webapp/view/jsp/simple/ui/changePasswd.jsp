<jsp:directive.include file="includes/top.jsp" />
<%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<p>  梵町个人中心 <span style="float:right"><a href="<%=request.getAttribute("redirUrl").toString().replaceAll("&", "&amp;")%>">返回${backName}</a>&nbsp;&nbsp;</span></p>
 <!--  <form:form commandName="formPass" htmlEscape="true">
  <form:errors path="*" id="msg" cssClass="errors" element="div" /> -->
  <c:if test="${empty nullPass}">
  	<p>原密码:</p>
    <input type="password" name="passwd" id="passwd" cssClass="input_box" maxlength="20" htmlEscape="true"/>
    </c:if>
    <p>新密码:</p>
    <input type="password" name="newpwd" id="newpwd" cssClass="input_box" maxlength="20" htmlEscape="true"/>
    <p>确认密码:</p>
    <input type="password" name="confirm" id="confirm" cssClass="input_box" maxlength="20" htmlEscape="true"/>
     <p><anchor>确定<go href="/user/changepass${qryString}" method="post" accept-charset="utf-8">
        <postfield name="passwd" value="$(passwd)"/>
        <postfield name="newpwd" value="$(newpwd)"/>
        <postfield name="confirm" value="$(confirm)"/>
        <postfield name="d" value="${d}"/>
    </go></anchor></p>
 <!--  </form:form> -->
 <a href="${write}/logout${qryString}">退出登录</a>
<jsp:directive.include file="includes/bottom.jsp" />
