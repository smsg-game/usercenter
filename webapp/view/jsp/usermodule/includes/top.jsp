<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<c:set var="read" value='<%=com.easou.usercenter.config.SSOConfig.getProperty("domain.readonly")%>' />
<c:set var="write" value='<%=com.easou.usercenter.config.SSOConfig.getProperty("domain.write")%>' />

<head>
<meta charset="utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <title>账号管理</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/view/jsp/usermodule/vendor/jquery-mobile/jquery.mobile-1.3.1.min.css" />
    <link href="<%=request.getContextPath()%>/css/default/all.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/view/jsp/usermodule/css/comm.css?t=123" />
    <script src="<%=request.getContextPath()%>/view/jsp/usermodule/vendor/jquery/jquery-1.9.1.min.js"> </script> 
    <script src="<%=request.getContextPath()%>/view/jsp/usermodule/js/comm.js?t=123"></script>
    <script>
	    $(document).bind("mobileinit", function(){
	  	  $.mobile.pageLoadErrorMessage = '网络连接异常，请稍后再试';
	  	});
    </script>
    <script src="<%=request.getContextPath()%>/view/jsp/usermodule/vendor/jquery-mobile/jquery.mobile-1.3.1.min.js"> </script> 
    <script>
      try {

      } catch (error) {
        console.error("Your javascript has an error: " + error);
      }
    </script>
</head>
<c:if test="${not empty pageContext.request.queryString}">
    <c:set var="qryString" value="?${pageContext.request.queryString}" />
</c:if>