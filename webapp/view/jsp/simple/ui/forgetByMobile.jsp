<jsp:directive.include file="includes/top.jsp" />
<%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
<jsp:directive.include file="includes/eucbar.jsp" />
<jsp:directive.include file="includes/eucerr.jsp" />
    <p>1. 在这里输入您的新密码:</p>
    <input name="newPwd" type="password" id="newPwd" class="input_box" maxlength="11" value="${newPwd}"/>
    <p>请再次输入您的密码:</p>
    <input type="password" name="confirm" id="confirm" class="input_box" maxlength="18"/>
     <p>2. 在这里输入您收到的验证码:</p>
    <input type="text" name="veriCode" id="veriCode" class="input_box" maxlength="6" value="${veriCode}"/>
    <p><anchor>确定并登录<go href="/pass/resetpass${qryString}" method="post" accept-charset="utf-8">
        <postfield name="newPwd" value="$(newPwd)"/>
        <postfield name="confirm" value="$(confirm)"/>
        <postfield name="veriCode" value="$(veriCode)"/>
        <postfield name="mobile" value="${mobile}"/>
    </go></anchor></p>
<jsp:directive.include file="includes/bottom.jsp" />