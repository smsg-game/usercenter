<!-- touch Version by damon 2012.06.01 -->
<%@ page contentType="text/html; charset=UTF-8"%>
<jsp:directive.include file="includes/top.jsp" />
<body>

<header>
绑定手机号
<a class="comeBack icon" href="${requestScope.contextPath}/game/touch/bindAccount${qryString}"></a>
</header>
<ul class="forgotPass-lists">
	<li style="width:34%" class="on" id="oneLi">1.输入手机号</li>
	<li style="width:33%" id="twoLi">2.输入验证码</li>
	<li style="width:33%" id="thirdLi">3.绑定成功</li>
</ul>
<form action="<c:url value="${requestScope.contextPath}/game/touch/resetSucc${qryString}" />" id="myform" method="post">
<input type="hidden" name="id" id="id" value="${id}" />
<div class="forgotPass-moblie">
	<div class="content-button"><input type="text" name="mobile" id="mobile" class="input" type="text" placeholder="输入您的手机号"  style="width:250px" maxlength="11" value="${mobile}"></div>	
	<div class="content-button"><a href="javascript:next();" class="button-blue">下一步</a></div>
</div>
<div  class="forgotPass-pass" style="display:none">	
	<div class="content-button"><input name="veriCode" id="veriCode" class="input" type="text" placeholder="输入验证码"  style="width:250px"></div>	
	<div class="content-button"><a href="javascript:submitVeriCode();" class="button-blue">提 交</a></div>
</div>
<div  class="forgotPass-succ" style="display:none">
    <div class="content-button">
                    手机号绑定成功，系统将在3秒后自动跳转到游戏个人中心。
    </div>
</div>
</form>
<script>
//$(document).ready(function(){
//	var li = $(".forgotPass-lists li");
//	li.live("click",function(){
//		var n = li.index($(this));
//		if(n === 0){
//			$(".forgotPass-moblie").show();
//			$(".forgotPass-pass").hide();
//			$(".forgotPass-succ").hide();
//		}else if(n === 1){
//			$(".forgotPass-moblie").hide();
//			$(".forgotPass-pass").show();
//			$(".forgotPass-succ").hide();
//		}else{
//			$(".forgotPass-moblie").hide();
//			$(".forgotPass-pass").hide();
//			$(".forgotPass-succ").show();
//		}
//		
//		$(this).addClass("on").siblings().removeClass("on")
//	})
//})
function next(){
    var mobile = document.getElementById("mobile").value;
    var id = document.getElementById("id").value;
    //alert(document.getElementById("one").style.background);
     $.ajax({
			type : 'POST',
			url : '${requestScope.contextPath}/game/touch/bindMobileNext${qryString}',
			data: {"id":id,"mobile":mobile},
			success : function(data) {
			     $(".forgotPass-moblie").hide();
			     $(".forgotPass-pass").show();
			     $(".forgotPass-succ").hide();
			     $("#twoLi").addClass("on");
			     $("#oneLi").removeClass("on");
			     document.getElementById("popDiv").innerHTML=data;
	      		 setTimeout(function(){
	      				document.getElementById("tisi").style.display="none";
	      			},3000);
			},
			error : function(xhr, type) {
			     var msg = xhr.responseText;
			     document.getElementById("popDiv").innerHTML=msg;
	      		 document.getElementById("tisi").style.display="";
				 setTimeout(function(){
	      				document.getElementById("tisi").style.display="none";
	      				if(msg=="验证码已发送，请在手机上查看"){
	      				    $(".forgotPass-moblie").hide();
			                $(".forgotPass-pass").show();
			                $(".forgotPass-succ").hide();
			                //document.getElementById("two").removeClass("on");
			                //document.getElementById("one").addClass("on");
			                 //$(".forgotPass-lists li").addClass("on").siblings().removeClass("on");
			                 $("#twoLi").addClass("on");
			                 $("#oneLi").removeClass("on");
	      				}
	      			},3000);
			}
		})
}

function submitVeriCode(){
    var mobile = document.getElementById("mobile").value;
    var veriCode = document.getElementById("veriCode").value;
    var id = document.getElementById("id").value;
     $.ajax({
			type : 'POST',
			url : '${requestScope.contextPath}/game/touch/bindcfm${qryString}',
			data: {"id":id,"mobile":mobile,"veriCode":veriCode},
			success : function(data) {
			     $(".forgotPass-moblie").hide();
			     $(".forgotPass-pass").hide();
			     $(".forgotPass-succ").show();
			     $("#twoLi").removeClass("on");
			     $("#thirdLi").addClass("on");
			     setTimeout(function(){
	      				//document.getElementById("tisi").style.display="none";
	      				window.location.href='${requestScope.contextPath}/game/touch/userCenter${qryString}';	      				
	      				//document.getElementById("myform").submit();
	      			},3000);
			},
			error : function(xhr, type) {
			     document.getElementById("popDiv").innerHTML=xhr.responseText;
	      		 document.getElementById("tisi").style.display="";
				 setTimeout(function(){
	      				document.getElementById("tisi").style.display="none";
	      			},3000);
			}
		})
}

</script>
<jsp:directive.include file="includes/bottom.jsp" />