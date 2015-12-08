<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<c:set var="read" value='<%=com.easou.usercenter.config.SSOConfig.getProperty("domain.readonly")%>' />
<c:set var="write" value='<%=com.easou.usercenter.config.SSOConfig.getProperty("domain.write")%>' />

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>梵町</title>
<link href="<%=request.getContextPath()%>/css/default/all.css" rel="stylesheet" type="text/css" />
</head>
<body>
<c:if test="${not empty pageContext.request.queryString}">
    <c:set var="qryString" value=" ?${pageContext.request.queryString}" />
</c:if>