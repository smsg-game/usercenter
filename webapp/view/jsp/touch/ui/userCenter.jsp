<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page import="com.fantingame.common.api.ClientConfig"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String queryStr = request.getQueryString();
	if(null==queryStr || "".equals(queryStr)) {
		queryStr = "";
	} else {
		queryStr = "?"+queryStr;
	}
	String payUrl = ClientConfig.getProperty("payment.url");
	String param = "/charge.e?backUrl=" + java.net.URLEncoder.encode(basePath + path + queryStr, "utf8");
%>
<c:if test="${not empty pageContext.request.queryString}">
    <c:set var="qryString" value="?${pageContext.request.queryString}" />
</c:if>

<c:set var="read" value='<%=com.easou.usercenter.config.SSOConfig.getProperty("domain.readonly")%>' />
<c:set var="write" value='<%=com.easou.usercenter.config.SSOConfig.getProperty("domain.write")%>' />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<meta charset="utf-8" />
<meta content="wap.fantingame.com" name="author" />
<meta content="Copyright &copy;fantingame.com 版权所有" name="copyright" />
<meta content="1 days" name="revisit-after" />
<meta content="" name="keywords" />
<meta content="" name="description" />

<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title>梵町</title>
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link href="<%=request.getContextPath()%>/css/touch/easoubase.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/touch/easou.css" rel="stylesheet" type="text/css" />
<script src="<%=request.getContextPath()%>/js/zepto.min.js"></script>
<script src="<%=request.getContextPath()%>/js/zepto.play.js"></script>
<script type="text/javascript">
	 function initECurr(){
		$.ajax({
			type : 'GET',
			url : 'user/getECurr${qryString}',
			success : function(data) {
			     document.getElementById('eCurr').innerHTML = data;
			     
			},
			error : function(xhr, type) {
				 document.getElementById("eCurr").innerHTML="未知";
			}
		})
        }
        //历史游戏
        function initHisGames(){
            $.ajax({
			type : 'GET',
			url : 'user/getHisGames${qryString}',
			success : function(data) {
			     document.getElementById('hisGames').innerHTML = data;
			     
			},
			error : function(xhr, type) {
				 document.getElementById("hisGames").innerHTML="获取不到您玩过的游戏";
			}
		})
        }
        function initLoad(){
            initECurr();
            initHisGames();
        }
</script>
</head>

<body onload="initLoad();">
<div class="loginbg">
	<div class="logintopwarp">
		<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
		<div class="slogan">梵町个人中心</div>
	</div>
<div class="information1">
    您好, ${nickName} <a href="<c:url value="${write}${requestScope.contextPath}/user/changeInfo${qryString}" />">修改资料</a><br/><br/> 
<div>
<div style=" float:left;" >e币余额：</div><div id="eCurr" onClick="initECurr();"  style=" float:left;" >0</div>
<div>&nbsp;
<script type="text/javascript">
<!--
	var payLink = "<a href=\"" + "<%=payUrl%>";
	if ('AndroidSmsPayForJavaScript' in window) {
		payLink = payLink + "/mobile";
	} else {
		payLink = payLink + "/wap";
	}
	payLink = payLink + "<%=param%>" +	"\">充值</a>";
	//alert(payLink);
	document.write(payLink);
//-->
</script>
</div>
</div>  
    绑定手机：
  <c:choose>
   <c:when test="${not empty mobile}">
 	${mobile}<br/>
 	<a href="<c:url value="${write}${requestScope.contextPath}/user/bindMobile${qryString}" />">修改绑定</a> |
 	<a href="<c:url value="${write}${requestScope.contextPath}/user/jbMobile${qryString}" />">解除绑定</a><br/>
   </c:when>
   <c:otherwise>
	<br/>您还未绑定手机号码  <a href="<c:url value="${write}${requestScope.contextPath}/user/bindMobile${qryString}" />">绑定</a><br/>
        <!-- （首次绑定手机号，赠送100e币）<br> --> 
   </c:otherwise>
</c:choose>
<!-- 
安全邮箱：
<c:choose>
   <c:when test="${not empty email}">
   	${email}<br/>
   	<a href="<c:url value="${write}${requestScope.contextPath}/user/bindEmail${qryString}" />">修改邮箱</a> |
 	<a href="<c:url value="${write}${requestScope.contextPath}/user/jbEmail${qryString}" />">解除绑定</a><br/>
   </c:when>
   <c:otherwise>
	<br/>
	您还未绑定邮箱 &nbsp;&nbsp;&nbsp;&nbsp;  <a href="<c:url value="${write}${requestScope.contextPath}/user/bindEmail${qryString}" />">绑定</a><br/>
   </c:otherwise>
</c:choose>
 -->
	<div calss="bottomrusbox">
	帐号安全：<br/>
	<a href="<c:url value="${write}${requestScope.contextPath}/user/changepass${qryString}" />">修改密码</a>&nbsp;|&nbsp;<a href="<c:url value="${write}${requestScope.contextPath}/user/sbindUser${qryString}" />">关联账号</a><br/>
	</div>

<div calss="information1">
              我玩过的游戏:
</div>

<div calss="bottomrusbox" id="hisGames">
</div>
 
</div>
<br/>
<div class="bottomrusbox" style="padding-left: 8%;padding-right: 8%;">
	<div class="usergobackbtn" onClick="window.location.href='${redirUrl}'">返回${backName}</div>
	<div class="userlogoutbtn" onClick="window.location.href='${read}/logout${qryString}'">退出登录</div>
</div>
<jsp:directive.include file="includes/bottom.jsp" />