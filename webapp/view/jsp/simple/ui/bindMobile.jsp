<jsp:directive.include file="includes/top.jsp" />
<%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<p>梵町个人中心 <span style="float:right"><a href="<%=request.getAttribute("redirUrl").toString().replaceAll("&", "&amp;")%>">返回${backName}</a>&nbsp;&nbsp;</span></p>
  <!-- <form:form commandName="formUser" htmlEscape="true">
  <form:errors path="*" id="msg" cssClass="errors" element="div" /> -->
  <p>输入您要绑定的手机号码，系统将以短信方式发送验证码到该手机上</p>
    <input cssClass="input_box" maxlength="11" id="mobile" name="mobile" tabindex="1" path="mobile"/>
    <p><anchor>获取验证码<go href="/user/bindMobile${qryString}" method="post" accept-charset="utf-8">
        <postfield name="mobile" value="$(mobile)"/>
        <postfield name="id" value="${id}"/>
    </go></anchor></p>
    <!-- </form:form> -->
  <c:choose>
   <c:when test="${not empty curMobile}">
   <p><anchor>取消绑定<go href="/user/delMobile${qryString}" method="post" accept-charset="utf-8">
        <postfield name="id" value="${id}"/>
    </go></anchor></p>
     </c:when>
  </c:choose>
  
  <a href="${write}/logout${qryString}">退出登录</a>
<jsp:directive.include file="includes/bottom.jsp" />