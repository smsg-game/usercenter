<jsp:directive.include file="includes/top.jsp" />
<%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<jsp:directive.include file="includes/eucbar.jsp" />
  <p>通过您注册或绑定的手机号，获取验证码重设密码</p>
  <jsp:directive.include file="includes/eucerr.jsp" />
    <input type="text" name="mobile" id="mobile" class="input_box" maxlength="11" value="${mobile}"/>
    <p><anchor>获取验证码<go href="/pass/findbym${qryString}" method="post">
        <postfield name="mobile" value="$(mobile)"/>
    </go></anchor></p>
    <!-- <p>或</p>
    <p>2. <a href="<c:url value="${requestScope.contextPath}/pass/findbyem${qryString}" />">通过邮箱找回</a></p> -->
<jsp:directive.include file="includes/bottom.jsp" />
