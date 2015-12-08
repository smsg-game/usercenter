<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:directive.include file="includes/top.jsp" />
<c:set var="gameHall" value='<%=com.easou.game.GameConfig.getProperty("game.hall")%>' />
<script src="http://tjs.sjs.sinajs.cn/open/api/js/wb.js?appkey=" type="text/javascript" charset="utf-8"></script>
<script src="http://mat1.gtimg.com/app/opent/js/qshare_min.js"></script>

<body>
<header>
分享
<a class="comeBack icon" href="${requestScope.contextPath}/game/touch/userCenter${qryString}"></a>
</header>
<div>
	<ul class="info-account">
	<c:choose>
		<c:when test="${appAgent=='AndroidGameHall'}">
			<!-- e游戏apk分享 -->
			<li >
				<span class="info-account-text">
					<img class="fl info-account-logo"  src="<%=request.getContextPath()%>/images/play-logo.png">
					<ol  class="fl">
						<dt>我的e游戏</dt>
						<ds>如果觉得不错，请告诉你的朋友~ </ds>
					</ol>
				</span>
				<a href="javascript:shareDiv(0);" class="info-account-button button-blue">分享</a>
				<input type="hidden" id = "shareContent0" value="我正在用一个好玩意，《我的e游戏》——玩游戏的利器。在这可以个性定制喜欢的游戏，而且不用下载就可以玩到最热门的页游喔。你要不要试玩一下呢？">
				<input type="hidden" id = "shareUrl0" value="http://183.232.10.67:82/gameResource/13641203255685413.apk">
				<input type="hidden" id = "0" value="">
			</li>
		</c:when>
		<c:otherwise>
			<!-- 大厅网页版分享 -->
			<li >
				<span class="info-account-text">
					<a href="${gameHall}"><img class="fl info-account-logo"  src="<%=request.getContextPath()%>/images/play-logo-4.png"></a>
					<ol  class="fl">
						<dt>梵町游戏大厅</dt>
						<ds>如果觉得不错，请告诉你的朋友~ </ds>
					</ol>
				</span>
				<a href="javascript:shareDiv(0);" class="info-account-button button-blue">分享</a>
				<input type="hidden" id = "shareContent0" value="这是一个神奇的游戏大厅!上面玩啥米游戏居然都不用下载，一个账号可以通玩全部的游戏，重点是，还都是免费玩滴~，实在是无聊时必备利器：">
				<input type="hidden" id = "shareUrl0" value="${gameHall}">
				<input type="hidden" id = "0" value="">
			</li>
			</c:otherwise>
		</c:choose>
			
		<li class="info-content"  >
				把你玩过不错的游戏分享给大家
		</li>
			<span id="hisGames">
			</span>
	</ul>
</div>	
<!-- 待分享信息,通过js将内容赋于对应id中 -->
<input type="hidden" id = "shareContent">
<input type="hidden" id = "shareUrl">
<input type="hidden" id = "gameId">
<jsp:directive.include file="includes/bottom.jsp" />
<script type="text/javascript">
var appAgent = "${appAgent}";

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
            //initECurr();
            initHisGames();
        }
        
        function shareDiv(gameId){
           var shareContent =  document.getElementById("shareContent"+gameId).value;  
           var shareUrl =  document.getElementById("shareUrl"+gameId).value;  
           document.getElementById("shareContent").value=shareContent;
           document.getElementById("shareUrl").value=shareUrl;
           document.getElementById("gameId").value=gameId;
           if(appAgent=="AppEasouHall") {
           		if("AndroidShareForJS" in window) {
                        var shareLink = [postToSinaWb(1), postToQQWb(1)];
                        AndroidShareForJS.startAndroidShare(shareLink, shareContent, shareUrl);
                }
           }
        }
        
        function postToQQWb(t){
            var gameId=document.getElementById("gameId").value;
            var shareContent=document.getElementById("shareContent").value;
            var shareUrl=document.getElementById("shareUrl").value;
            
            var _t = encodeURI(shareContent);
            var _url = encodeURI(shareUrl);
            var _appkey = encodeURI("c6ccc15ca91c420fb603faf3f0161e08");//你从腾讯获得的appkey
            var _pic = encodeURI('');//（列如：var _pic='图片url1|图片url2|图片url3....）
            var _site = '';//你的网站地址
            var _u = 'http://v.t.qq.com/share/share.php?title='+_t+'&url='+_url+'&appkey='+_appkey+'&site='+_site+'&pic='+_pic;
            if(t==0) {
            	window.open( _u,'转播到腾讯微博', 'width=700, height=680, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, location=yes, resizable=no, status=no' );
            } else {
            	return _u;
            }
       }
       function postToSinaWb(t){
       var gameId=document.getElementById("gameId").value;
            var shareContent=document.getElementById("shareContent").value;
            var shareUrl=document.getElementById("shareUrl").value;
           var _w = 72 , _h = 16;     
           
  var param = {
    url:shareUrl,
    type:'3',
    count:'1', /**是否显示分享数，1显示(可选)*/
    appkey:'', /**您申请的应用appkey,显示分享来源(可选)*/
    title:shareContent, /**分享的文字内容(可选，默认为所在页面的title)*/
    pic:'', /**分享图片的路径(可选)*/
    ralateUid:'', /**关联用户的UID，分享微博会@该用户(可选)*/
	language:'zh_cn', /**设置语言，zh_cn|zh_tw(可选)*/
    rnd:new Date().valueOf()
  }
  var temp = [];
  for( var p in param ){
    temp.push(p + '=' + encodeURIComponent( param[p] || '' ) )
  }
  var _u = "http://service.weibo.com/share/share.php?" + temp.join('&');
  if(t==0) {
   window.open( _u,'转播到腾讯微博', 'width=_w, height=_h, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, location=yes, resizable=no, status=no' );
    //window.open( _u,'转播到腾讯微博', 'width=700, height=680, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, location=yes, resizable=no, status=no' );
      } else {
      return _u
     }
}
</script>
<script>
$(document).ready(
function(){
	initLoad();
	if(appAgent!="AppEasouHall") {
		// 不是大厅app
		var loadHtml = "<a  href='javascript:void(0);' onclick='postToSinaWb(0);' class='info-account-sina infoIcon'></a><a href='javascript:void();' onclick='postToQQWb(0);' class='info-account-tencent infoIcon'></a>";
		$(".info-account-button").live("click",function(){
			$().box({"width":"50%","content":loadHtml,"isCloseButton":true})
		})
	}
})
</script>