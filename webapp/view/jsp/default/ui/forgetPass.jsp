<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<jsp:directive.include file="includes/eucbar.jsp" />
<jsp:directive.include file="includes/eucerr.jsp" />
<div class="content">
  <p>通过您注册或绑定的手机号，获取验证码重设密码</p>
  <form action="<c:url value="${requestScope.contextPath}/pass/findbym${qryString}" />" id="FindMForm" method="post">
    <input type="text" name="mobile" id="mobile" class="input_box" maxlength="11" value="${mobile}"/>
    <input type="submit" class="button_1" value="获取验证码"/>
  </form>
   <!--  <p>或</p>
    <p>2. <a href="<c:url value="${requestScope.contextPath}/pass/findbyem${qryString}" />">通过邮箱找回</a> -->
</div>
<jsp:directive.include file="includes/bottom.jsp" />
