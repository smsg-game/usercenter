<jsp:directive.include file="includes/top.jsp" />
<%@ page contentType="text/vnd.wap.wml; charset=UTF-8" %>
梵町个人中心 <span style="float:right"><a href="<%=request.getAttribute("redirUrl").toString().replaceAll("&", "&amp;")%>">返回${backName}</a>&nbsp;&nbsp;</span>
<jsp:directive.include file="includes/eucerr.jsp" />
<!-- <form:form commandName="formUser" htmlEscape="true">
  <p><form:errors path="*" id="msg" cssClass="errors" element="div" /></p> -->
  	<p>用户名:&nbsp;&nbsp;${name}</p>
  	<p>昵称:(2~16位,中文2位)</p>
    <input id="nickName" name="nickName" cssClass="input_box" maxlength="20" value="${formUser.nickName}"/>
    <p>城市 :(例如 深圳)</p>
    <input id="city" name="city" cssClass="input_box" maxlength="10" value="${formUser.city}" htmlEscape="true"/>
    <p>性别:<c:if test="${formUser.sex==0}">男</c:if><c:if test="${formUser.sex==1}">女</c:if></p>
    <p> <select name="sex">
 <option value="10">--</option>
 <option value="0" >男</option>
 <option value="1">女</option>
</select></p>
     <p><anchor>确定<go href="/user/changeInfo${qryString}" method="post" accept-charset="utf-8">
        <postfield name="nickName" value="$(nickName)"/>
        <postfield name="city" value="$(city)"/>
        <postfield name="sex" value="$(sex)"/>
        <postfield name="d" value="${d}"/>
    </go></anchor></p>
    <!--  </form:form> -->
<a href="${write}/logout${qryString}">退出登录</a>
<jsp:directive.include file="includes/bottom.jsp" />
