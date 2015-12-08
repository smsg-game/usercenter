<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="weibo4j.util.WeiboConfig"%>
<jsp:directive.include file="includes/top.jsp" />
<jsp:directive.include file="includes/eucbar.jsp" />
<div class="content">
  <p id="u1_rtf"><span>欢迎使用梵町</span></p>
  	<form:form method="post" id="fm1" cssClass="fm-v clearfix" commandName="${commandName}" htmlEscape="true">
    <form:errors path="*" id="msg" cssClass="errors" element="div" />
    <p>登录名/手机号:</p>
    <form:input cssClass="input_box" maxlength="200" id="username" tabindex="1"  path="username" htmlEscape="true" />
    <p>密码:</p>
    <form:password cssClass="input_box" maxlength="200" id="password" tabindex="2" path="password"  htmlEscape="true" autocomplete="off" />
    <p><input id="isCookie" name="isCookie" checked="checked" value="true" tabindex="3"  type="checkbox" />记住登录状态。需支持并打开手机cookie功能</p>
    <input type="hidden" name="lt" value="${loginTicket}" />
	<input type="hidden" name="execution" value="${flowExecutionKey}" />
	<input type="hidden" name="_eventId" value="submit" />
    <input type="submit" class="button_1" value="登录"/>
   </form:form>
</div>
<div class="con2">
	<!--p>提示：登录成功后保存任意页面为书签，下次通过书签访问，可免去登录过程</p-->
    <p style="margin-top:10px;">您也可以通过以下方式登录：</p>
    <c:if test="${not empty qryString}">
    	<c:set var="conSymb" value="&" />
	</c:if>
	<c:if test="${empty qryString}">
    	<c:set var="conSymb" value="?" />
	</c:if>
    <p><img src="images/default/sina_24.png" alt="" />&nbsp;<a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=11">新浪微博登录</a></p>
    <p><img src="images/default/tqq_24.png" alt="" />&nbsp;<a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=21">腾讯微博登录</a></p>
    <p><img src="images/default/qq.png" alt="" />&nbsp;<a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=51">QQ登录</a></p>
    <p><img src="images/default/renren_24.png" alt="" />&nbsp;<a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=31">人人网登录</a></p>
</div>
<jsp:directive.include file="includes/bottom.jsp" />