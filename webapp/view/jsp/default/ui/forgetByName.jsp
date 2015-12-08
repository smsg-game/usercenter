<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<jsp:directive.include file="includes/eucerr.jsp" />
<div class="content">
  <form action="<c:url value="${requestScope.contextPath}/pass/anwques${qryString}" />" id="myform" method="post">
    <p>问题:${question}?</p>
    <p>您的答案是:</p>
    <input type="text" name="answer" id="answer" class="input_box" maxlength="50"/>
    <input type="hidden" name="username" value="${username}" />
    <p><input type="submit" class="button_1" value="确定"></p>
  </form>
 </div>
<jsp:directive.include file="includes/bottom.jsp" />
