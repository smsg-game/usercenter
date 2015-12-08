<%@ page contentType="text/html; charset=UTF-8" %>
<!-- login page modify by jay 2013.3.5 -->
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="read" value='<%=com.easou.usercenter.config.SSOConfig.getProperty("domain.readonly")%>' />
<c:set var="write" value='<%=com.easou.usercenter.config.SSOConfig.getProperty("domain.write")%>' />
<c:if test="${not empty pageContext.request.queryString}">
    <c:set var="qryString" value="?${pageContext.request.queryString}" />
</c:if>
<c:if test="${not empty qryString}">
    	<c:set var="conSymb" value="&" />
</c:if>
<c:if test="${empty qryString}">
    	<c:set var="conSymb" value="?" />
</c:if>
<c:if test="${autoReg==true}">
	<c:set var="oload" value=" onLoad='oneRegister()'" />
</c:if>

<html>
<head>
    <title>梵町</title>
    <meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="black" name="apple-mobile-web-app-status-bar-style" />
    <meta content="telephone=no" name="format-detection" />
	<link href="<%=request.getContextPath()%>/css/touch/common.css" rel="stylesheet" type="text/css" />
	<link href="<%=request.getContextPath()%>/css/touch/login.css" rel="stylesheet" type="text/css" />
</head>
<body${oload}>
<div class="loginbg">
	
	<!-- 没有登录,需要先注册 -->		
	<div class="login line-bottom">
		<img class="login-img" src="../../images/touch/login-logo.png" />
		<c:if test="${autoReg==false }">
			梵町登录系统，安全快捷
			<a class="login-button"  href="javascript:void(0)"><input class="button-blue button"  id="regbutton" type="button" value="一键注册" onclick="oneRegister()" /></a>
		</c:if>
		<c:if test="${autoReg==true }">
		    梵町游戏 精心每一款
		    <a class="login-button"  href="javascript:void(0)"><input class="button-blue button"  id="regbutton" type="button" value="一键注册"  onclick="oneRegister()"/></a>
		</c:if>
	</div>
	<div id="tisi" class="login-layer" style="display:none">
		<span id="popDiv"  style="height:240px"></span>
	</div>
	<div class="account">
	<h1>使用其他账号登录：</h1>
	<ul class="clearFix">
		<li ><span class="account-sina icon"></span><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=1">新浪微博</a></li>
		<li ><span class="account-qq icon"></span><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=5">QQ账号</a></li>
		<li ><span class="account-tencent icon"></span><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=2">腾讯微博</a></li>
		<li ><span class="account-renren icon"></span><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=3">人人网</a></li>
		<li ><span class="account-easou icon"></span><a href="${requestScope.contextPath}/login${qryString}${conSymb}wver=t">梵町账号</a></li>
	</ul>
	</div>
</div>
<jsp:directive.include file="includes/bottom.jsp" />

<script type="text/javascript">
	var win = window;
	function xhr(){
		try {
			return win.XMLHttpRequest && (win.location.protocol !== 'file:' || !win.ActiveXObject) ? new win.XMLHttpRequest() : new win.ActiveXObject('Microsoft.XMLHTTP')
		} catch(e) {}			
	}
	function ajax(conf) {
		var s = conf,
			t = "json",
			h = xhr(),
			pid, ttl = s.timeout,
			su = s.success,
			se = s.error,
			v;
		h.onreadystatechange = function() {
			if(4 == h.readyState) {
				if(200 == h.status) {
					clearTimeout(pid);
					var v =  JSON.parse(h.responseText);
					su(v);
				} else {
					se && se(h.xhr, h.type)
				}
			}
		};
		h.open("post", s.url, !0);
		h.setRequestHeader("Cache-Control", "no-cache");
		h.send(s.data);
		if(ttl > 0) 
			pid = setTimeout(function() {
				h.abort();
				se && se(h.xhr, h.type)
			},ttl)
	}
	function oneRegister(){
		document.getElementById("regbutton").disabled = true;
		var esid="<%=request.getAttribute("esid")%>",
		uid="<%=request.getAttribute("uid")%>",
		source="<%=request.getAttribute("source")%>",
		qn="<%=request.getAttribute("qn")%>";
		document.getElementById("popDiv").innerHTML="<br/>正在为您分配帐号和密码<br/>";
		document.getElementById("tisi").style.display="";
		ajax({
			url:"/autoReg?esid="+esid+"&source="+source+"&uid="+uid+"&qn="+qn,
			success:function(data){
				var h=[];
				if("true"==data.success) {
					h.push('注册成功<br/>');
					h.push('用户名:'+data.name+'<br/>');
	      			h.push('密码:'+data.passwd+'<br/>');
	      			h.push('系统会帮您自动登录<br/>');
	      			h.push('呢称可在个人中心修改<br/>');
	      			document.getElementById("popDiv").innerHTML=h.join("");
	      			document.getElementById("tisi").style.display="";
	      			setTimeout(function(){
	      				document.getElementById("popDiv").innerHTML="<img class=\"login-load\" src=\"../../images/touch/login-load.png\" />努力跳转中...";
	      				//window.location.href=data.service;
	      				window.location.reload();
	      			},1500);
      			} else {
      				h.push('<br/><br/>网络繁忙，请稍后再试');
	      			document.getElementById("popDiv").innerHTML=h.join("");
	      			document.getElementById("tisi").style.display="";
      				setTimeout(function(){
	      				document.getElementById("tisi").style.display="none";
	      				document.getElementById("regbutton").disabled = false;
	      			},1500);
      			}
			},
			error:function(){
				var h=[];
				h.push('<br/><br/>网络繁忙! 请稍后再试');
	      		document.getElementById("popDiv").innerHTML=h.join("");
	      		document.getElementById("tisi").style.display="";
      			setTimeout(function(){
	      			document.getElementById("tisi").style.display="none";
	      			document.getElementById("regbutton").disabled = false;
	      		},1500);
			}
		})
	}
</script>
