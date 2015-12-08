(function($, window) {

  $(function() {
    showHeaderIfUserAgentIsNotMobile(); // First page action.
    $(document).bind('pageinit', showHeaderIfUserAgentIsNotMobile); // AJAX action.
  });

  function showHeaderIfUserAgentIsNotMobile() {
    if (!/Android|webOS|iPhone|iPad|iPod|Phone|BlackBerry|Nokia/i.test(navigator.userAgent)) {
      $('div[data-role=header]').css('display', 'block');
    }
  }
})(jQuery, window);

function binded(msg) {
	showPrompt.init(msg);
}
var showPrompt={
		init:function(title,isTimer){
			if($("#mask").length !== 0){
				return;
				}
			this.isTimer = isTimer;
			this.creatHtml();
			this.setValue(title);
			this.setPost();
			this.addEvent();
			
		},
		
		//添加事件
		addEvent:function(){
			var _t = this,tMask;
			var time = 2000;
			
			if(!this.isTimer){
				this.mask.on("touchstart click",function(e){
					clearTimeout(tMask);								 
					$("#mask").remove();
				});
				
				clearTimeout(tMask);
				tMask = setTimeout(function(){
					$("#mask").remove();
				},time);
			}
				window.addEventListener("orientationchange", function(){
				//$(".number").val(_t.bodyWidth);
					setTimeout(function(){
						_t.setPost();	 
					},200);
																  
				}, false); 
		},
		
		//创建一个HTML代码 插入在BODY中
		creatHtml:function(){
			var html  = "<div id='mask'><div class='maskBg'></div><div class='maskContent'></div><span class='maskText'></span></div>";
			$("body").append(html);
			this.mask = $("#mask");
			
		},
		
		//设置里面的图片和文字
		setValue:function(title){
			$(".maskText").html(title);
		},
		
		//设置位置
		setPost:function(){
			var top = document.documentElement.scrollTop || document.body.scrollTop;
			var height = $(window).height();
			var width = $(window).width();
			var maskHeight = $(".maskContent").height();
			var maskWidth = $(".maskContent").width();
			var maskTextHeight = $(".maskText").height();
			var maskTextWidth = $(".maskText").width();
			
			this.mask.find(".maskContent").css({
				left:(width-maskWidth)/2,
				height:maskTextHeight+70,
				top: (height-(maskTextHeight+70))/2
			});
			
			this.mask.find(".maskText").css({
				top:(height-maskTextHeight)/2+top,
				left:(width-maskTextWidth)/2
			});
			
			this.mask.find(".maskContent").css({
				top:(height-$(".maskContent").height())/2+top
			});
			
		}
	};
function changeNickName() {
	var nickName = $("#nickNameValue").val();
	$.ajax({
		type : 'POST',
		dataType:"json",
		data:{nickName:nickName},
		url : "/user/changeNickName",
		success : function(data) {
			if(data.code == 0) {
				showPrompt.init("设置昵称成功");
			} else {
				showPrompt.init(data.desc);
			}
			$("#nickNameDiv").show();
			$("#nickNameA").html('<span id="nickname-label">昵称</span>' + nickName);
        	$("#nickNameText").hide();
		},
		error : function(xhr, type) {
			showPrompt.init("设置失败");
		}
	});
}
var focusNickName = null;
function setNickName() {
	$("#nickNameDiv").hide();
	$("#nickNameText").show();
	$("#nickNameValue").keydown(function(e) {
        if (e.which == 13) { 
        	changeNickName();
          }
        });
	$("#nickNameValue").focus(function(e) {
		focusNickName = $("#nickNameValue").val();
	});
	$("#nickNameValue").blur(function(e) {
		if(focusNickName != $("#nickNameValue").val()) {
			changeNickName();
		}
		$("#nickNameDiv").show();
		$("#nickNameText").hide();
	});

}
function displayPwd(id,curr) {
	var text = $(curr).parent().find('.ui-btn-text').text();
	if(text == "显示密码") {
		var pwdObj = $("#" + id);
		pwdObj.attr("type","text");
		$(curr).text("隐藏密码");
		$(curr).parent().find('.ui-btn-text').text("隐藏密码");
	} else {
		var pwdObj = $("#" + id);
		pwdObj.attr("type","password");
		$(curr).text("显示密码");
		$(curr).parent().find('.ui-btn-text').text("显示密码");
	}
	
}
function changePwd(url) {
	var newpwd = $("#newpwd").val();
	$.ajax({
		type : 'POST',
		dataType:"json",
		data:{newpwd:newpwd},
		url : url,
		success : function(data) {
			if(data.code == 0) {
				window.location = "/user/usersetting.html?m=2";
			}else {
				showPrompt.init(data.desc);
			}
		},
		error : function(xhr, type) {
			showPrompt.init("Cookie或者Session过期，请刷新重新登录再操作");
		}
	});
}

function sendSms(url,id,mobile) {
	$.ajax({
		type : 'POST',
		dataType:"json",
		data:{id:id,mobile:mobile},
		url : url,
		success : function(data) {
			if(data.code == 0){
				showPrompt.init("重新下发成功");
			}else {
				showPrompt.init(data.desc);
			}
		},
		error : function(xhr, type) {
			showPrompt.init("你无权操作此功能，请刷新后重新登录再操作");
		}
	});
}





