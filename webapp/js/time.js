// JavaScript Document
(function( $ ){
		  
	/*imglazyload
	*延时加载
	*/
	$.fn.imglazyload = function( options ){
		var o = $.extend({
			attr : 'lazy-src',
			container : window,
			event : 'scroll',
			fadeIn : false,
			threshold : 0,
			vertical : true
		}, options ),
	event = o.event,
	vertical = o.vertical,
	container = $( o.container ),
	threshold = o.threshold,
	
	// 将jQuery对象转换成DOM数组便于操作
	
	dataName = 'imglazyload_offset',
	OFFSET = vertical ? 'top' : 'left',
	SCROLL = vertical ? 'scrollTop' : 'scrollLeft',
	winSize = vertical ? container.height() : container.width(),
	scrollCoord = document.documentElement[ SCROLL ]||document.body[ SCROLL ], 
	docSize = winSize + scrollCoord;
	
	var elems = [];
		for(var i=0;i< $(this).length;i++){
			elems.push($(this).get(i))
		}
	// 延迟加载的触发器
	var trigger = {
		init : function( coord ){
			return coord >= scrollCoord &&coord <= ( docSize + threshold );
		},
		scroll : function( coord ){
			
			var scrollCoord =  document.documentElement[ SCROLL ]||document.body[ SCROLL ]; 
			return coord >= scrollCoord && coord <= ( winSize + scrollCoord + threshold );
		},
		resize : function( coord ){
			
			var scrollCoord =  document.documentElement[ SCROLL ]||document.body[ SCROLL ],winSize = vertical ? container.height() :container.width();
			return coord >= scrollCoord && coord <= ( winSize + scrollCoord + threshold );
		}
	};
	var loader = function( triggerElem, event ){
		var i = 0, isCustom = false,isTrigger, coord, elem, $elem, lazySrc;
		// 自定义事件只要触发即可，无需再判断
		if( event ){
			if( event !== 'scroll' && event !== 'resize' ){
				isCustom = true;
			}
		}else{
			event = 'init';
		}
		
		for( ; i < elems.length; i++ ){
			isTrigger = false;
			elem = elems[i];
			$elem = $( elem );
			lazySrc = $elem.attr( o.attr );
			if( !lazySrc || elem.src === lazySrc ){
				continue;
			}
			// 先从缓存获取offset值，缓存中没有才获取计算值,
			// 将计算值缓存，避免重复获取引起的reflow
			coord = $elem.data( dataName );
			if( coord === undefined ){
				coord = $elem.offset()[ OFFSET ];
				$elem.attr( dataName, coord );
			}
			
			isTrigger = isCustom || trigger[ event ]( coord );
			
			if( isTrigger ){
				// 加载图片
				
				elem.src = lazySrc;
				if( o.fadeIn ){
					$elem.hide().fadeIn();
				}
				// 移除缓存
				$elem.removeAttr( dataName );
				// 从DOM数组中移除该DOM
				elems.splice( i--, 1 );
			}
		}
		// 所有的图片加载完后卸载触发事件
		if( !elems.length ){
			if( triggerElem ){
				triggerElem.unbind( event, fire );
			}else{
				container.unbind( o.event, fire );
			}
			$( window ).unbind( 'resize', fire );
			elems = null;
		}
	};
	var fire = function( e ){
		loader( $(this), e.type );
	};
	// 绑定事件
	container = event === 'scroll' ? container : $( this );
	container.bind( event, fire );
	$( window ).bind( 'resize', fire );
	// 初始化
	loader();
	return this;
	};
	
	
	/*selectBg
	*滑动选择中改变背景颜色，500毫秒之后恢复
	*@wolfer
	*/
	$.fn.selectBg = function( options ){
		var o = $.extend({
			bgColor : '#dbdbdb',
			parentNode:"li"
		}, options ),
		oTarget,t=this;
		
		t.live("touchstart",function(e){
			oTarget = $(e.target).parents(o.parentNode);
			oTarget.css("background",o.bgColor);
			setTimeout(function(){
				oTarget.css("background","");
			}, 500);
		}).live("touchend",function(e){	
			oTarget.css("background","");
		});
	}
	
	
	
	

 /**
 * slider.js for Mobile
 * @version 1.0.2
 * @required Zepto.js
 * @author 7405321@gmail.com
 */

$.fn.slider = function(config) {
        config = $.extend({
            el: this,
            navBarCt: '',
            remoteUrl: '',
            cycle: true,
            autoPlay: true,
            playTime: 3000,
            leftMove: '',
            rightMove: '',
            onchange: function() {},
            onsuccess: function() {}
        }, config || {});

      
		
		 var isIphone = /i(Phone|P(o|a)d)/.test(navigator.userAgent) && !! window.ontouchstart,
			isAndroid = /Android/.test(navigator.userAgent),
			has3d = isIphone && 'WebKitCSSMatrix' in window && 'm11' in new WebKitCSSMatrix(),
			translateStart = has3d ? 'translate3d(' : 'translate(',
			translateEnd = has3d ? ',0)' : ')';
	
		//private methods
		var settings = {
			//init data
			initData: function(config) {
				var chld = config.el.children();
				this.data = {
					_items: chld,
					_width: chld[0].scrollWidth,
					_trans: config.initPos || 0,
					_pageWith: config.pageWith || 300,
					_config: config,
					_navBarCt: config.navBarCt,
					_onchange: config.onchange
				};
			},
			//load data
			loader: function(config) {
				var _this = this,
					_el = config.el,
					_fn = function() {
						_this.cloneItem(_el, config.cycle);
						_this.bindEvents(_el, config);
						_this.initData(config);
						actions.autoMove(config);
					};
				//ajax request    
				if(config.remoteUrl) {
					$.ajax({
						url: config.remoteUrl,
						success: function(data) {
							config.onsuccess.call(_el, data);
							_fn();
						}
					});
				} else {
					_fn();
				}
			},
			//clone node
			cloneItem: function(el, cycle) {
				cycle && el.append(el.children()[0].cloneNode(true));
				el.children().css({
					"display": 'inline-block',
					"-webkit-perspective": 1000,
					"-webkit-backface-visibility": 'hidden'
				});
			},
			//bind events
			bindEvents: function(el, config) {
				el.bind({
					"touchstart": actions.touchstart,
					"touchmove": actions.touchmove,
					"touchend": actions.touchend
				});
				$(config.leftMove).click(function() {
					actions.customMove('left');
				});
				$(config.rightMove).click(function() {
					actions.customMove('right');
				});
			}
		};
	
		//event list
		var actions = {
			//auto play timer
			autoTimer: -1,
			touchstart: function(e) {
				clearInterval(actions.autoTimer);
				actions.startX = actions.lastX = e.touches[0].clientX;
				actions.startY = e.touches[0].clientY;
			},
			touchmove: function(e) {
				var _this = actions,
				curX = e.touches[0].clientX,
				curY = e.touches[0].clientY;
				//scroll left/right position
				if(Math.abs(curX - _this.startX) > Math.abs(curY - _this.startY)) {
					e.preventDefault();
					//calc current position
					var trans = settings.data._trans + curX - _this.lastX,
					width = settings.data._width,
					pageWith = settings.data._pageWith;
					_this.lastX = curX;
					//move layer position
					if(settings.data._config.cycle) {
						if(-trans < width) {
							trans -= width;
						}
						if(-trans >= width) {
							trans += width;
						}
					} else {
						if(-trans < 0) {
							trans = settings.data._trans;
						} else if(-trans > width - pageWith) {
							trans = -(width - pageWith);
						}
					}
					//touchmove position without animation
					_this.transition(trans, false);
					settings.data._trans = trans;
				}
			},
			touchend: function(e) {
				var _this = actions,
					trans = settings.data._trans,
					width = settings.data._width,
					gap = settings.data._pageWith,
					ot = Math.abs((trans) % gap),
					flag = trans > 0 ? 1 : -1;
				//move time difference
				trans = _this.lastX - _this.startX < 0 ? (trans + flag * (gap - ot)) : (trans - flag * ot);
				if(-trans < 0) {
					trans = settings.data._trans;
				} else if(-trans > width - gap && !settings.data._config.cycle) {
					trans = -(width - gap);
				}
				_this.transition(trans, true);
				actions.autoMove(settings.data._config);
			},
			//auto play
			autoMove: function(config) {
				if(config.autoPlay) {
					var _this = this;
	
					function _move() {
						var trans = settings.data._trans;
						var pageWith = settings.data._pageWith;
						trans += -pageWith;
						_this.transition(trans, true);
					};
					_this.autoTimer = setInterval(function() {
						_move();
					}, config.playTime);
				}
			},
			//transition 
			transition: function(trans, animation) {
				var _this = this,
				items = settings.data._items;
				items.css({
					"-webkit-transform": translateStart + trans + "px,0" + translateEnd,
					"-webkit-transition": animation ? "-webkit-transform 0.2s cubic-bezier(0,0,0.25,1)" : 'none'
				});
				
				/*加载时换图片*/
				var n = -settings.data._trans/settings.data._pageWith;
				var img = $(settings.data._items).find("img").eq(n+1);
				if(img){
					img.attr("src",img.attr("lazy-src")||img.attr("src")).removeAttr("lazy-src");
				}
				
				if(animation) {
					items.one('webkitTransitionEnd', function() {
						var width = settings.data._width;
						if(-trans >= width) {
							trans += width;
							_this.transition(trans, false);
						}
						settings.data._trans = trans;
						_this.onchange();
					});
				}
			},
			//page change event
			onchange: function() {
				var cur = parseInt((settings.data._trans) / settings.data._pageWith);
				settings.data._navBarCt.removeClass('selected').eq(-cur).addClass('selected');
				settings.data._onchange(this, -cur);
			},
			//custom move page
			customMove: function(pos) {
				clearInterval(actions.autoTimer);
				var width = settings.data._width,
				pageWith = settings.data._pageWith,
				trans = settings.data._trans + (pos == 'left' ? 1 : -1) * pageWith;
				if(!settings.data._config.cycle){
					if(trans > 0 || -trans > width - pageWith) return;
				}else{
					if(trans > 0) {
						this.transition(settings.data._trans = -width, false);
						trans = -width + pageWith;
					}   
				}
				//fixed the animation bug
				setTimeout(function(){actions.transition(trans, true);},0)
				actions.autoMove(settings.data._config);
			}
		};
		
		
 		settings.loader(config);
        return this;
    };

 



/**
 * slider.js for Mobile
 * @version 1.0.2
 * @required Zepto.js
 * @author 7405321@gmail.com
 */

$.fn.allSlider = function(config) {
        config = $.extend({
            el: this,
            navBarCt: '',
            remoteUrl: '',
            cycle: true,
            autoPlay: true,
            playTime: 3000,
            leftMove: '',
            rightMove: '',
            onchange: function() {},
            onsuccess: function() {}
        }, config || {});

      
		
		 var isIphone = /i(Phone|P(o|a)d)/.test(navigator.userAgent) && !! window.ontouchstart,
			isAndroid = /Android/.test(navigator.userAgent),
			has3d = isIphone && 'WebKitCSSMatrix' in window && 'm11' in new WebKitCSSMatrix(),
			translateStart = has3d ? 'translate3d(' : 'translate(',
			translateEnd = has3d ? ',0)' : ')';
	
		//private methods
		var settings = {
			//init data
			initData: function(config) {
				var chld = config.el.children();
				
				this.data = {
					_items: chld,
					//_width: chld[0].scrollWidth,
					_trans: config.initPos || 0,
					_pageWith: config.pageWidth || 300,
					_config: config,
					_navBarCt: config.navBarCt,
					_onchange: config.onchange
				};
				/*动态设置图片的宽度*/
				config.el.find("img").css({"width":this.data._pageWith});
				this.data._width = chld[0].scrollWidth;
				
			},
			//load data
			loader: function(config) {
				var _this = this,
					_el = config.el,
					_fn = function() {
						_this.cloneItem(_el, config.cycle);
						_this.bindEvents(_el, config);
						_this.initData(config);
						actions.autoMove(config);
					};
				//ajax request    
				if(config.remoteUrl) {
					$.ajax({
						url: config.remoteUrl,
						success: function(data) {
							config.onsuccess.call(_el, data);
							_fn();
						}
					});
				} else {
					_fn();
				}
			},
			//clone node
			cloneItem: function(el, cycle) {
				cycle && el.append(el.children()[0].cloneNode(true));
				el.children().css({
					"display": 'inline-block',
					"-webkit-perspective": 1000,
					"-webkit-backface-visibility": 'hidden'
				});
			},
			//bind events
			bindEvents: function(el, config) {
				el.bind({
					"touchstart": actions.touchstart,
					"touchmove": actions.touchmove,
					"touchend": actions.touchend
				});
				$(config.leftMove).click(function() {
					actions.customMove('left');
				});
				$(config.rightMove).click(function() {
					actions.customMove('right');
				});
			}
		};
	
		//event list
		var actions = {
			//auto play timer
			autoTimer: -1,
			touchstart: function(e) {
				clearInterval(actions.autoTimer);
				actions.startX = actions.lastX = e.touches[0].clientX;
				actions.startY = e.touches[0].clientY;
			},
			touchmove: function(e) {
				var _this = actions,
				curX = e.touches[0].clientX,
				curY = e.touches[0].clientY;
				//scroll left/right position
				if(Math.abs(curX - _this.startX) > Math.abs(curY - _this.startY)) {
					e.preventDefault();
					//calc current position
					var trans = settings.data._trans + curX - _this.lastX,
					width = settings.data._width,
					pageWith = settings.data._pageWith;
					_this.lastX = curX;
					//move layer position
					if(settings.data._config.cycle) {
						if(-trans < width) {
							trans -= width;
						}
						if(-trans >= width) {
							trans += width;
						}
					} else {
						if(-trans < 0) {
							trans = settings.data._trans;
						} else if(-trans > width - pageWith) {
							trans = -(width - pageWith);
						}
					}
					//touchmove position without animation
					_this.transition(trans, false);
					settings.data._trans = trans;
				}
			},
			touchend: function(e) {
				var _this = actions,
					trans = settings.data._trans,
					width = settings.data._width,
					gap = settings.data._pageWith,
					ot = Math.abs((trans) % gap),
					flag = trans > 0 ? 1 : -1;
				//move time difference
				trans = _this.lastX - _this.startX < 0 ? (trans + flag * (gap - ot)) : (trans - flag * ot);
				if(-trans < 0) {
					trans = settings.data._trans;
				} else if(-trans > width - gap && !settings.data._config.cycle) {
					trans = -(width - gap);
				}
				_this.transition(trans, true);
				actions.autoMove(settings.data._config);
			},
			//auto play
			autoMove: function(config) {
				if(config.autoPlay) {
					var _this = this;
	
					function _move() {
						var trans = settings.data._trans;
						var pageWith = settings.data._pageWith;
						
						trans += -pageWith;
						_this.transition(trans, true);
					};
					_this.autoTimer = setInterval(function() {
						_move();
					}, config.playTime);
				}
			},
			//transition 
			transition: function(trans, animation) {
				var _this = this,
				items = settings.data._items;
				items.css({
					"-webkit-transform": translateStart + trans + "px,0" + translateEnd,
					"-webkit-transition": animation ? "-webkit-transform 0.2s cubic-bezier(0,0,0.25,1)" : 'none'
				});
				
				/*加载时换图片*/
				var n = -settings.data._trans/settings.data._pageWith;
				var img = $(settings.data._items).find("img").eq(n+1);
				if(img){
					img.attr("src",img.attr("lazy-src")||img.attr("src")).removeAttr("lazy-src");
				}
				
				if(animation) {
					items.one('webkitTransitionEnd', function() {
						var width = settings.data._width;
						if(-trans >= width) {
							trans += width;
							_this.transition(trans, false);
						}
						settings.data._trans = trans;
						_this.onchange();
					});
				}
				
				window.addEventListener("orientationchange", function(){
					setTimeout(function(){
						 var cur = parseInt((settings.data._trans) / settings.data._pageWith);
						
				 		settings.data._pageWith =$(".picScroll").width();
						config.el.find("img").css({"width":settings.data._pageWith});
						settings.data._width = config.el.children()[0].scrollWidth; 
						settings.data._trans = cur*settings.data._pageWith;
						console.log(cur*settings.data._pageWith);
						settings.data._items.css({
							"-webkit-transform": "translate("+cur*settings.data._pageWith+"px, 0px)"
						});
					},200)
																  
				}, false); 
				/*
				$(window).resize(function(){
						 var cur = parseInt((settings.data._trans) / settings.data._pageWith);
						
				 		settings.data._pageWith =$(".picScroll").width();
						config.el.find("img").css({"width":settings.data._pageWith});
						settings.data._width = config.el.children()[0].scrollWidth; 
						settings.data._trans = cur*settings.data._pageWith;
						console.log(cur*settings.data._pageWith);
						settings.data._items.css({
							"-webkit-transform": "translate("+cur*settings.data._pageWith+"px, 0px)"
						});
						
				});*/
			},
			//page change event
			onchange: function() {
				var cur = parseInt((settings.data._trans) / settings.data._pageWith);
				settings.data._navBarCt.removeClass('selected').eq(-cur).addClass('selected');
				settings.data._onchange(this, -cur);
			},
			//custom move page
			customMove: function(pos) {
				clearInterval(actions.autoTimer);
				var width = settings.data._width,
				pageWith = settings.data._pageWith,
				trans = settings.data._trans + (pos == 'left' ? 1 : -1) * pageWith;
				if(!settings.data._config.cycle){
					if(trans > 0 || -trans > width - pageWith) return;
				}else{
					if(trans > 0) {
						this.transition(settings.data._trans = -width, false);
						trans = -width + pageWith;
					}   
				}
				//fixed the animation bug
				setTimeout(function(){actions.transition(trans, true);},0)
				actions.autoMove(settings.data._config);
			}
		};
		
		
 		settings.loader(config);
        return this;
    };
	
	
	
	
	/*弹出层*/
$.fn.alert = function( options ){ 
    var o = $.extend({ 
    		"width":"30%",
    		"content":"",
    		"closeTime":3000
			}, options);
    
    var a = {
    		init:function(){
    			clearTimeout(this.timer);
    			this.creatHtml();
    		},
    		
    		/*创建HTML*/
    		creatHtml:function(){
				if($(".alertClass").length !== 0){
					$(".alertClass").remove();
					}
    			var html = "<div class='alertClass'>"+o.content+"</div>";
    			$("body").append(html);
    			this.target = $(".alertClass");
    			this.setPost();
    			this.bindEvent();
    			
    		},
    		
    		/*定位*/
    		setPost:function(){
    			
    			this.target.css({
    				"background":"#000000",
    				"color":"#fff",
					 "width":o.width,
					 "position":"absolute",
					 "zIndex":"101",
					 "padding":"20px",
					"textAlign":"center"
					
				});
    			
    			var height = $(window).height();
    			var width = $(window).width();
    			var maskHeight = this.target.height();
    			var maskWidth = this.target.width();
    			
    			this.target.css({
      				 "left":(width-maskWidth)/2,
   					 "top":(height-maskHeight)/2
   					
   				});
    			
    			this.anim();
    			
    			
    		},
    		
    		/*事件绑定*/
    		bindEvent:function(){
    			var _t = this;
    			
    			/*如果层显示出来 就隐藏*/
    			_t.target.click(function(){
					_t.anmiClose();
    			});
    			
    			/*规定时间后消失*/
    			this.timer = setTimeout(function(){
    				_t.anmiClose();
    			},o.closeTime);
    		},
    		
    		anim:function(){
    			var _t = this;
    			this.target.css({
    				 "-webkit-transform":"scale(0,0)",
    				 "opacity":0
					
  				});
    			
    			this.target.animate({
    				"-webkit-transform":"scale(1.1,1.1)",
    				"opacity":0.9
    			},300,"ease-in-out");
    			
    			setTimeout(function(){
    				_t.target.animate({
        				"-webkit-transform":"scale(1.0,1.0)"
        			},100);
    			},300)
    		},
    		
    		anmiClose:function(){
    			var _t = this;
    			this.target.animate({
    				"-webkit-transform":"scale(0,0)",
    				"opacity":0
    			},300,"ease-in-out");
    			
    			setTimeout(function(){
    				_t.target.remove();
    			},300)
    		}
    }
    
    a.init();
}




	/*弹出层
 * 带确认和取消*/
$.fn.confirm = function( options ){ 
    var o = $.extend({ 
    		"width":"30%",
    		"content":"",
    		"yesButton":"确认",
    		"noButton":"取消",
    		"yesFun":function(){},
    		"noFun":function(){a.anmiClose();}
			}, options);
	
		var c=options.yesFun;
		o.yesFun=function(){
			c(a.anmiClose);
		}
    var a = {
    		init:function(){
    			this.creatHtml();
    		},
    		
    		/*创建HTML*/
    		creatHtml:function(){
    			var html = "<div class='confirmClass'><h5>提 示</h5><div class='confirmContent'>"+o.content+"</div><div style='text-align:center'>" +
    					"<input name='' type='button' value='"+o.yesButton+"'  class='button button-blue button-margin' id='confirm-yes'/>" +
    					"<input name='' type='button' value='"+o.noButton+"'  class='button button-blue button-margin' id='confirm-no'/>" +
    					"</div></div>";
    			$("body").append(html);
    			this.target = $(".confirmClass");
    			this.content = $(".confirmContent");
    			this.yesButton = $("#confirm-yes");
    			this.noButton = $("#confirm-no");
    			this.setPost();
    			this.bindEvent();
    			
    		},
    		
    		/*定位*/
    		setPost:function(){
    			
    			this.target.css({
    				"background":"#ffffff",
    				 "width":o.width,
					 "position":"absolute",
					 "zIndex":"101",
					 "border-radius":"5px",
					 "padding-bottom":"10px"
				});
				
				this.target.find("h5").css({
    				"height":"34px",
					"textAlign":"center",
					"color":"#fff",
					"line-height":"34px",
					 "border-radius":"5px 5px 0px 0px",
					 "font-size":"16px",
					"background": "url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAAiCAYAAACeLbMRAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAACHSURBVBhXFcRZCoFhAAXQ+2AP5nlmLzaFSH6SpCRJSpJEIqxFmWdevKrrfufhIPP9EenNh0itTas3kVyaFioxf6mZik9VbPJU4wcRHanI8K4GKtxXod6NCHavqqMC7Qvhb50JX9PUUN76ifDUTNUj4bYOhKtsKilnca8KypE35XaEPatslS3/vt1aa9YsBhgAAAAASUVORK5CYII=')  repeat-x;"
    				
				});
				
				
    			
    			this.content.css({
    				"color":"#333333",
					"padding":"10px",
					"marginBottom":"10px",
					"textAlign":"center"
				});
    			
    			var height = $(window).height();
    			var width = $(window).width();
    			var maskHeight = this.target.height();
    			var maskWidth = this.target.width();
    			
    			this.target.css({
      				 "left":(width-maskWidth)/2,
   					 "top":(height-maskHeight)/2
   				});
    			
    			this.anim();
    		},
    		
    		/*事件绑定*/
    		bindEvent:function(){
    			var _t = this;
    			
    			this.yesButton.click(function(){
    				o.yesFun();
    			});
    			
    			this.noButton.live("click",function(){
    				o.noFun();
    			});
    		},
    		
    		anmiClose:function(){
    				var _t = a;
        			a.target.animate({
        				"-webkit-transform":"scale(0,0)",
        				"opacity":0
        			},300,"ease-in-out");
        			
        			setTimeout(function(){
        				_t.target.remove();
        				_t.noButton.die("click");
        			},300)
        		
    		},
    		
    		anim:function(){
    			var _t = this;
    			this.target.css({
    				 "-webkit-transform":"scale(0,0)",
    				 "opacity":0
  				});
    			
    			this.target.animate({
    				"-webkit-transform":"scale(1.1,1.1)",
    				"opacity":1
    			},300,"ease-in-out");
    			
    			setTimeout(function(){
    				_t.target.animate({
        				"-webkit-transform":"scale(1.0,1.0)"
        			},100);
    			},300)
    		}
    }
    
    a.init();
}


	/*弹出层*/
$.fn.box = function( options ){ 
    var o = $.extend({ 
    		"width":"30%",
    		"content":"",
			"boxClass":"boxClass"
			}, options);
    
    var a = {
    		init:function(){
    			clearTimeout(this.timer);
    			this.creatHtml();
    		},
    		
    		/*创建HTML*/
    		creatHtml:function(){
				if($(".alertClass").length !== 0){
					$(".alertClass").remove();
					}
    			var html = "<div class='"+o.boxClass+"'>"+o.content+"</div>";
    			$("body").append(html);
    			this.target = $("."+o.boxClass);
    			this.setPost();
    			
    			
    		},
    		
    		/*定位*/
    		setPost:function(){
    			
    			this.target.css({
    				"background":"#000000",
    				"color":"#fff",
					 "width":o.width,
					 "position":"absolute",
					 "zIndex":"101",
					 "padding":"20px",
					"textAlign":"center"
					
				});
    			
    			var height = $(window).height();
    			var width = $(window).width();
    			var maskHeight = this.target.height();
    			var maskWidth = this.target.width();
    			
    			this.target.css({
      				 "left":(width-maskWidth)/2,
   					 "top":(height-maskHeight)/2
   					
   				});
    			
    			this.anim();
    			
    			
    		},
    		
    		
    		
    		anim:function(){
    			var _t = this;
    			this.target.css({
    				 "-webkit-transform":"scale(0,0)",
    				 "opacity":0
					
  				});
    			
    			this.target.animate({
    				"-webkit-transform":"scale(1.1,1.1)",
    				"opacity":0.9
    			},300,"ease-in-out");
    			
    			setTimeout(function(){
    				_t.target.animate({
        				"-webkit-transform":"scale(1.0,1.0)"
        			},100);
    			},300)
    		},
    		
    		anmiClose:function(){
    			var _t = this;
    			this.target.animate({
    				"-webkit-transform":"scale(0,0)",
    				"opacity":0
    			},300,"ease-in-out");
    			
    			setTimeout(function(){
    				_t.target.remove();
    			},300)
    		}
    }
    
    a.init();
}

/*showClose
	*滑动选择中改变背景颜色，500毫秒之后恢复
	*@wolfer
	*/
	$.fn.showClose = function( options ){
		var o = $.extend({
			box:".listMode",	
			bigImg:"images/bigClose.jpg",
			smallImg:"images/smallClose.jpg",
			closeFun : function(){}
		}, options );
		
		
		var a = {
			init:function(){
				
				this.bindEvent();
				},
				
			createHtml:function(){
				
				if($("#closeImg").length !== 0){
					$("#closeImg").remove();
				}
				
				var html = "<span class='closeImg close icon'></span>";
				$(o.box).append(html);
				this.closeButton = $(".closeImg");
				this.closeButton.click(function(e){
					e.stopPropagation();
					var box = $(this).parents(o.box);
					var isBigImg = box.hasClass("lists");
					$(this).remove();
					if(isBigImg){
						box.html("<img src='"+o.bigImg+"'   width='100%' />");
						}else{
						box.html("<img src='"+o.smallImg+"'   width='100%' />");
						}
					});
				
				
				},
			
			bindEvent:function(){
				var _t = this;
				
				
				$(o.box).live("touchstart",function(){
					clearTimeout(_t.timer);
					_t.timer= setTimeout(function(){
						_t.createHtml();
					},1000);
				});
				
				$(o.box).live("touchend touchmove",function(){
					clearTimeout(_t.timer);
				});
				
				$(o.box).live("click",function(){
					var img = $(".closeImg");
					if(img.length !== 0){
						$(".closeImg").remove();
					}else{
						var href = $(this).attr("_href");
						location.href=href;
					}
				});
				
				}
			}
			a.init();
	}

})( Zepto ); 




