<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<c:set var="gameHall" value='<%=com.easou.game.GameConfig.getProperty("game.hall")%>' />
<c:set var="payUrl" value='<%=com.fantingame.common.api.ClientConfig.getProperty("payment.url")%>' />

<body>
<header>
个人中心
<c:if test="${appAgent!='AndroidGameHall'}">
<a class="comeBack icon" href="${gameHall}?esid=${esid}&qn=${qn}&v=2.1"></a>
</c:if>
<a class="change icon" href="${read}${requestScope.contextPath}/game/touch/switchUser${qryString}"></a>
</header>

<div>
	<h1>昵称：${nickName}<span class="fr">财富：<span id="eCurr" onClick="initECurr();">0</span>&nbsp;e币</span></h1>
	<div class="info-content">
		<ul class="info-lists">
			<li class="info-modifyName"><a href="${write}${requestScope.contextPath}/game/touch/changeNName${qryString}"><span class="info-logo infoIcon"></span><span class="info-name">修改昵称</span></a></li>
			<li class="info-recharge"><a href="${payUrl}/wap/charge.e?backUrl=${backUrl}"><span class="info-logo infoIcon"></span><span class="info-name">e币充值</span></a></li>
			<li class="info-account"><a href="${write}${requestScope.contextPath}/game/touch/bindAccount${qryString}"><span class="info-logo infoIcon"></span><span class="info-name">账号绑定</span></a></li>
			<li class="info-share"><a href="${write}${requestScope.contextPath}/game/touch/gameShare${qryString}"><span class="info-logo infoIcon"></span><span class="info-name">分 享</span></a></li>
			<li class="info-feedback"><a href="${gameHall}/subMsg.e?esid=${esid}&v=2.1"><span class="info-logo infoIcon"></span><span class="info-name">意见反馈</span></a></li>
			<c:if test="${appAgent!='AndroidGameHall'}">
			<li class="info-easou"><a href="${gameHall}/stc.e?bk=aboutey3&esid=${esid}&v=2.1"><span class="info-logo infoIcon"></span><span class="info-name">关于梵町</span></a></li>
			</c:if>
		</ul>
	</div>
</div>	
</body>
</html>
<script>
$(document).ready(function(){
	initLoad();
})
</script>
<script type="text/javascript">
	 function initECurr(){
		$.ajax({
			type : 'GET',
			url : '/game/touch/getECurr${qryString}',
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
			url : '/game/touch/getHisGames${qryString}',
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
            //initHisGames();
        }
</script>