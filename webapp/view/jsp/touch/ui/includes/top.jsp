<!-- touch Version by damon modify 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="read" value='<%=com.easou.usercenter.config.SSOConfig.getProperty("domain.readonly")%>' />
<c:set var="write" value='<%=com.easou.usercenter.config.SSOConfig.getProperty("domain.write")%>' />

<html>
<head>
    <meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
    <meta charset="utf-8" />
    <title>梵町</title>
    <meta content="wap.fantingame.com" name="author" />
    <meta content="Copyright &copy;fantingame.com 版权所有" name="copyright" />
    <meta content="1 days" name="revisit-after" />
    <meta content="" name="keywords" />
    <meta content="" name="description" />
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="black" name="apple-mobile-web-app-status-bar-style" />
    <meta content="telephone=no" name="format-detection" />
    <link href="<%=request.getContextPath()%>/css/touch/easoubase.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/css/touch/easou.css" rel="stylesheet" type="text/css" />
</head>
<body>
<c:if test="${not empty pageContext.request.queryString}">
    <c:set var="qryString" value="?${pageContext.request.queryString}" />
</c:if>