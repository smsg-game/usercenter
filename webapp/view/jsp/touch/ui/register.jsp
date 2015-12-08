<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<%String redirectUrl=request.getParameter("service");
	if(null==redirectUrl) {
		redirectUrl = "/user/userCenter?wver=t";
	}
%>
<div class="loginbg">
			<div class="logintopwarp">
				<div class="loginlogo"><img src="../../images/touch/easoulogo.png" style=" width:136px; height:30px;"></div>
				<div class="slogan">注册成为梵町用户</div>
			</div>
			<div class="information1">
			一、已是梵町用户？<span class="blue1122cc"><a href="<c:url value="${read}${requestScope.contextPath}/login${qryString}" />">马上登录</a>
					</span><br/>
			二、<a href="javascript:void(0)" onclick="oneRegister()">一键快速注册</a><br/>
			<div id="tisi" class="login-layer" style="display:none">
				<span id="popDiv"  style="height:240px"></span>
			</div>
			三、短信注册<br/>
   		设置登录密码,编写6-18位英文或数字(区分大小写)<br/>
   		移动用户发送至:<a href="sms:1065755802911">1065755802911</a><br/>
   		联通用户发送至:<a href="sms:1065505937511">1065505937511</a><br/>
   		电信用户发送到:<a href="sms:1065902020209111">1065902020209111</a><br/>
   		系统确认后即可<a href="<c:url value="${read}${requestScope.contextPath}/login${qryString}" />">直接登录</a>,登录名为您发送短信的手机号<br/>
   		<!--  三、<a href="<c:url value="${write}${requestScope.contextPath}/nregist${qryString}" />">登录名注册</a><br/>
   		三、<a href="<c:url value="${write}${requestScope.contextPath}/emregist${qryString}" />">邮箱注册</a-->
			</div>
			<div class="bottomrusbox">
				<div class="gobackbtn" onclick="window.history.go(-1);">返回</div>
			</div>
<jsp:directive.include file="includes/bottom.jsp" />
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
		var str="${qryString}";
		document.getElementById("popDiv").innerHTML="<br/>正在为您分配帐号和密码<br/>";
		document.getElementById("tisi").style.display="";
		ajax({
			url:"/autoReg"+str,
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
	      				window.location.href="<%=redirectUrl%>";
	      			},3000);
      			} else {
      				h.push('<br/><br/>网络繁忙，请稍后再试');
	      			document.getElementById("popDiv").innerHTML=h.join("");
	      			document.getElementById("tisi").style.display="";
      				setTimeout(function(){
	      				document.getElementById("tisi").style.display="none";
	      			},3000);
      				registing = false;
      			}
			},
			error:function(){
				alert("网络繁忙，请稍后再试");
				registing = false;
			}
		})
		}
	}
</script>