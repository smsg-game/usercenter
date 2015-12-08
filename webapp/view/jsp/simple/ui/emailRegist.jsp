<jsp:directive.include file="includes/top.jsp" />
<%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<c:if test="${empty sessionScope.uinfo}" >
<jsp:directive.include file="includes/eucbar.jsp" />
</c:if>
  <!-- <form:form commandName="formEmail" htmlEscape="true">
  <form:errors path="*" id="msg" cssClass="errors" element="div" /> -->
    <p>请输入email地址:</p>
    <input type="text" cssClass="input_box" maxlength="100" id="email" tabindex="1" name="email" htmlEscape="true" />
    <p>密码:</p>
    <input type="password" cssClass="input_box" maxlength="18" id="password" name="password" tabindex="2"  htmlEscape="true" />
    <p>确认密码</p>
    <input type="password" cssClass="input_box" maxlength="18" id="confirm" name="confirm" tabindex="2"  htmlEscape="true" />
        <p><anchor>注册<go href="/emregist${qryString}" method="post" accept-charset="utf-8">
        <postfield name="email" value="$(email)"/>
        <postfield name="password" value="$(password)"/>
        <postfield name="confirm" value="$(confirm)"/>
    </go></anchor></p>
  <!-- </form:form> -->
<jsp:directive.include file="includes/bottom.jsp" />
