<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="weibo4j.util.WeiboConfig"%>
<jsp:directive.include file="includes/top.jsp" />
<!-- 
<jsp:directive.include file="includes/eucbar.jsp" />
 -->
				<c:if test="${not empty qryString}">
    				<c:set var="conSymb" value="&" />
				</c:if>
				<c:if test="${empty qryString}">
    				<c:set var="conSymb" value="?" />
				</c:if>
<div class="loginbg">
			<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">马上登录梵町</div>
			</div>
			<form:form method="post" id="fm1" cssClass="fm-v clearfix" commandName="${commandName}" htmlEscape="true">
				<!--  <form:errors path="*" id="msg" cssClass="errorbox" element="div" />-->
				<div class="loginbox">
					<div class="loginMsg">
	        		<label class="msgType">账号：</label>
					<form:input cssClass="msgContent" placeholder="登录名/手机号"
							id="username" path="username" maxlength="200" tabindex="1" htmlEscape="true" autocomplete="off"/>
					</div>
	        		<div class="loginMsgh2">
					<label class="msgType">密码：</label>
					<form:input cssClass="msgContent" placeholder="请输入密码" type="password"
							id="password" maxlength="200" tabindex="2" path="password"  htmlEscape="true" autocomplete="off" />
					</div>
					<input type="hidden" name="lt" value="${loginTicket}" />
	                <input type="hidden" name="execution" value="${flowExecutionKey}" />
	                <input type="hidden" name="_eventId" value="submit" />
				</div>
				<div class="loginBoxBottom">
					<input id="isCookie" name="isCookie" checked="checked" value="true" tabindex="3"  type="checkbox" />
					<label class="nextAutoLogin">
						下次自动登录 | 
					</label>
					<span class="blue1122cc"><a href="<c:url value="${write}${requestScope.contextPath}/pass/sforget${qryString}" />">忘记密码</a>
					</span>
					<input  class="loginButton" id="loginButton" type="submit" value="登录"/>
				</div>
			<c:if test="${not empty succMsg}">
			<div class="registrationnavbox">
			    <div class="errorbox">${succMsg}</div>
			</div>
			</c:if>	
			<form:errors path="*" id="msg" cssClass="errorbox" element="div" />
			</form:form>
				<div class="loginBoxBottom">			
				<p class="slogan4">还不是梵町用户？
					<span class="blue1122cc"><a href="<c:url value="${write}${requestScope.contextPath}/mregist${qryString}" />">马上注册</a>
					</span></p>
				</div>
				<div class="ouserlogin">
				<p><img alt="weibo" src="images/touch/sina_24.png"/><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=1">新浪微博登录</a></p>
				<p><img alt="tqq" src="images/touch/tqq_24.png"/><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=2">腾讯微博登录</a></p>
				<p><img src="images/default/qq.png" alt="" />&nbsp;<a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=5">QQ登录</a></p>
				<p><img alt="renren" src="images/touch/renren_24.png"/><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=3">人人网登录</a></p>				
			</div>	
			<div class="bottomrusbox">
				<div class="gobackbtn" onclick="window.history.go(-1);">返回	</div>
			</div>
<jsp:directive.include file="includes/bottom.jsp" />

