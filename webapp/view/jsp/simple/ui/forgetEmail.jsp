<jsp:directive.include file="includes/top.jsp" />
<%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<jsp:directive.include file="includes/eucbar.jsp" />
  <p>请填写绑定邮箱</p>
  <form:form commandName="formEmail" htmlEscape="true">
  <p><form:errors path="*" id="msg" cssClass="errors" element="div" /></p>
    <input cssClass="input_box" maxlength="200" id="email" tabindex="1"  name="email" htmlEscape="true" />
    <p><anchor>下一步<go href="/pass/findbyem${qryString}" method="post" accept-charset="utf-8">
        <postfield name="email" value="$(email)"/>
    </go></anchor></p>
  </form:form>
<jsp:directive.include file="includes/bottom.jsp" />
