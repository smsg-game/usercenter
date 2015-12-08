<jsp:directive.include file="includes/top.jsp" /><%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<div class="content">
  <p>请输入你在手机上收到的验证码</p>
   <!-- <form:form commandName="formVerify" htmlEscape="true">
  	<p><form:errors path="*" id="msg" cssClass="errors" element="div" /></p> -->
    <input cssClass="input_box" maxlength="6" id="veriCode" tabindex="1" name="veriCode" />
    <p><anchor>确定<go href="/user/bindMobile${qryString}" method="post" accept-charset="utf-8">
        <postfield name="mobile" value="${mobile}"/>
        <postfield name="id" value="${id}"/>
        <postfield name="act" value="verify"/>
        <postfield name="veriCode" value="$(veriCode)"/>
    </go></anchor></p>
  <!-- </form:form> -->
 </div>
<jsp:directive.include file="includes/bottom.jsp" />