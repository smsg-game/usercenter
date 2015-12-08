<%@ page contentType="text/html; charset=UTF-8" %>
<!-- login page modify by jay 2013.3.5 -->
<%
response.setDateHeader("Expires", 0);
response.setHeader("Cache-Control","no-cache");
response.setHeader("Cache-Control","no-store");
%>
<jsp:directive.include file="includes/top.jsp" />
<c:if test="${not empty qryString}">
    	<c:set var="conSymb" value="&" />
</c:if>
<c:if test="${empty qryString}">
    	<c:set var="conSymb" value="?" />
</c:if>
<c:if test="${autoReg==true}">
	<c:set var="oload" value=" onLoad='oneRegister()'" />
</c:if>
<c:set var="gameHall" value='<%=com.easou.game.GameConfig.getProperty("game.hall")%>' />
<body${oload}>
<header>
登录|注册${deploy}
<c:if test="${appAgent!='AndroidGameHall'}">
<a class="comeBack icon" onclick="javascript:history.go(-1)"></a>
</c:if>
</header>
	<div class="content-text">
    <c:if test="${autoReg==false }">
		还没有账号？
	</c:if>
	<c:if test="${autoReg==true }">
		  梵町游戏 精心每一款
	</c:if>
	</div>
	<div class="content-button">
	 	<span class="button-blue" onclick="oneRegister()">一键注册</span>
	</div>
<div style="border-bottom:1px #abadae solid; height:20px"></div>
<div class="account">
	<h2>使用其他账号登录：</h2>
	<ul class="clearFix">
		<li ><span class="account-sina infoIcon"></span><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=1&uv=gt&source=33">新浪微博</a></li>
		<li ><span class="account-qq infoIcon"></span><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=5&uv=gt&source=33">QQ账号</a></li>
		<li ><span class="account-tencent infoIcon"></span><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=2&uv=gt&source=33">腾讯微博</a></li>
		<li ><span class="account-renren infoIcon"></span><a href="${requestScope.contextPath}/oRedirect${qryString}${conSymb}t=3&uv=gt&source=33">人人网</a></li>
		<li ><span class="account-easou infoIcon"></span><a href="${requestScope.contextPath}/game/touch/gtLogin${qryString}">梵町账号</a></li>
	</ul>
</div>
<div id="tisi" class="login-layer" style="display:none">
		<span id="popDiv"  style="height:240px"></span>
	</div>
</body>
</html>
<script type="text/javascript">
	var win = window;
	var registing = false;
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
		if(registing) {
			//do nothing...
		} else {
			registing=true;
			var esid="<%=request.getAttribute("esid")%>",
			uid="<%=request.getAttribute("uid")%>",
			source="<%=request.getAttribute("source")%>",
			qn="<%=request.getAttribute("qn")%>",
			gameId="<%=request.getParameter("gameId")%>";
			document.getElementById("popDiv").innerHTML="<br/>正在为您分配帐号和密码<br/>";
			document.getElementById("tisi").style.display="";
			ajax({
				url:"/autoReg?esid="+esid+"&source="+source+"&uid="+uid+"&qn="+qn + "&gameId=" + gameId,
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
		      				registing=false;
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
		      			registing=false;
		      		},1500);
				}
			})
		}
	}
</script>
