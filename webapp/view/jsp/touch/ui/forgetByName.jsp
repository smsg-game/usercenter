<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="loginbg">
			<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">忘记密码</div>
			</div>
			<p class="slogan2">问题:${question}?</p>
			<form action="<c:url value="${requestScope.contextPath}/pass/anwques${qryString}" />" id="myform" method="post">			
			<div class="registrationbox">
			<div class="registrationMsg">
			<label class="msgType">
					您的答案是:
				</label>
                <input name="answer" id="answer"  maxlength="50" 
					class="msgContent" placeholder="请输入您的答案" type="text" />  
			</div>
		    </div>
				<!--  <input type="hidden" name="username" value="${username}" />-->
				<div class="registrationBoxBottom">
					<input class="registrationButton" id="loginButton" value="确认并登录"
						type="submit">
				</div>
			</form>
			<div class="registrationnavbox">			
                <jsp:directive.include file="includes/eucerr.jsp" />
			</div>
			<div class="bottomrusbox">
				<div class="gobackbtn" onclick=window.history.go(-1);>
					返回
				</div>
			</div>	
<jsp:directive.include file="includes/bottom.jsp" />

