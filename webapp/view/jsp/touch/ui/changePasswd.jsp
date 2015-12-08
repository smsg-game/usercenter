<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<div class="loginbg">
			<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">修改密码</div>
			</div>
			<form:form commandName="formPass" htmlEscape="true">
				<div class="passwordbox">
					<c:if test="${empty nullPass}">
					<div class="registrationMsg">
						<label class="msgType">
							 原密码：
						</label>
						<form:password path="passwd" id="passwd"  placeholder="请输入原密码"
						 cssClass="msgContent" maxlength="20" htmlEscape="true" type="password"/>
					</div>
					</c:if>
					<div class="registrationMsg">
						<label class="msgType">
							新密码：
						</label>
						<form:password path="newpwd" id="newpwd"  placeholder="6~18位数字或字母" 
						 class="msgContent" maxlength="20" htmlEscape="true"  type="password"/>
					</div>
					<div class="registrationMsg">
						<label class="msgType">
							确认密码：
						</label>
						<form:password path="confirm" id="confirm" placeholder="6~18位数字或字母"
						 cssClass="msgContent" maxlength="20" htmlEscape="true" type="password"/>
					</div>
				</div>
				<div class="registrationBoxBottom">
					<input class="registrationButton" id="loginButton" value="修改密码"
						type="submit">
				</div>
			<div class="registrationnavbox">
				<form:errors path="*" id="msg" cssClass="errorbox" element="div" />
			</div>
			<input type="hidden" name="d" value="${d}" />
			</form:form>
			<div class="bottomrusbox">
				<div class="gobackbtn" onclick="window.location.href='${redirUrl}'">返回${backName}</div>
			</div>
<jsp:directive.include file="includes/bottom.jsp" />