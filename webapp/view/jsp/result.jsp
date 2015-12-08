<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="refresh" content="<c:url value="1.5;url=${redirUrl}" />" />
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<link href="<%=request.getContextPath()%>/css/touch/easoubase.css" rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/touch/easou.css" rel="stylesheet" type="text/css" />
<title>梵町搜索</title>
<link href="${requestScope.contextPath}/css/all.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="loginbg">
<p>${retMessage}</p>
<p>或者直接 <a href="<c:url value="${redirUrl}" />">返回</a></p>
</div>
</body>
</html>