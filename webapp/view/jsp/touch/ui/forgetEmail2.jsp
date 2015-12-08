<!-- touch Version by Terryyu 2012.12.13 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<!-- 
<jsp:directive.include file="includes/eucbar.jsp" />
 -->
<div class="loginbg">
			<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">忘记密码</div>
			</div>
			<div class="information1">
					<p>第二步</p>
  					<p>打开邮箱${email}点击验证链接<p>
			</div>			 
			<div class="registrationnavbox">
				<jsp:directive.include file="includes/eucerr.jsp" />
            </div>
			<div class="bottomrusbox">
				<div class="gobackbtn" onclick="window.history.go(-1);">
					返回
				</div>
			</div>			
			
<jsp:directive.include file="includes/bottom.jsp" />
