<!-- touch Version by jay modify 2013.03.07 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="read" value='<%=com.easou.usercenter.config.SSOConfig.getProperty("domain.readonly")%>' />
<c:set var="write" value='<%=com.easou.usercenter.config.SSOConfig.getProperty("domain.write")%>' />
<c:set var="deploy" value='<%=com.easou.usercenter.config.SSOConfig.getProperty("server.deploy", "")%>' />
<c:set var="esid" value='<%=request.getParameter("esid") %>'/>
<c:set var="qn" value='<%=request.getParameter("qn") %>'/>
<c:if test="${not empty pageContext.request.queryString}">
    <c:set var="qryString" value="?${pageContext.request.queryString}" />
</c:if>

<html>
<head>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection">
<title>游戏平台</title>
<c:choose>
<c:when test="${appAgent=='AndroidGameHall'}">
 <link type="text/css" rel="stylesheet"  href="/css/common_android.css">
</c:when>
<c:otherwise>
 <link type="text/css" rel="stylesheet"  href="/css/common.css">
</c:otherwise>
</c:choose>
 <script src="/js/zepto.min.js"></script>
 <script src="/js/zepto.play.js"></script>
</head>
<style>
/*error */
.login-layer{ width:220px; background:#001021;opacity:0.8; color:#fff; position:absolute; left:50%; top:50%; margin-left:-110px; margin-top:-50px; border-radius:8px; 
text-align: center;  padding:10px 0px;z-index:99999;}
.errorbox{
	width:240px;  margin-left:22px;  border:#cdc655 1px solid; background:#fffddc;
	color:#ff4400; line-height:28px; margin:0px auto; margin-top:15px; padding:5px
}
</style>
<div id="tisi" class="login-layer" style="display:none;">
	<span id="popDiv"  style="height:240px;text-align: center;">
	</span>
</div>