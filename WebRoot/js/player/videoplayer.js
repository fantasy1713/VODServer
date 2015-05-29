VideoPlayer = function(mediajson, div_root){
	"use strict";
	
	var jsoninfo,
		container = div_root,
		//TODO: 保存视频对象的引用，用于调用setTimeout函数时的参数读取
		videoEleArray = [],
		
	addVideosToDiv = function(){
		if(jsoninfo){
			for(var i=0; i<jsoninfo.views.length; i++){
				//根据窗口数进行初始化
				initVideoView.call(this, jsoninfo.views[i]);
			}
		}
	},
	
	initVideoView = function(viewinfo){
		var self = this;
		var orders = viewinfo.order;
		//根据段的顺序进行初始化
		var min = Infinity;
		var firstorder;
		//找到排最前的段进行初始化
		for(var i=0; i<orders.length; i++){
			if(min > orders[i].orderindex){
				firstorder = orders[i];
				break;
			}
		}
		createVideoBySegment.call(this, viewinfo.viewindex, firstorder);
	},
	
	//根据可选的媒体类型选择合适的媒体流进行初始化
	createVideoBySegment = function(viewindex, orderinfo){
		if(!orderinfo)
			return;

		var targettype;
		if(isSupportMediaSourceExtension()){
			targettype = "mpeg-dash";
		}
		else{
			targettype = "mp4";
		}
		
		var seginfo = findSegmentByType(orderinfo, targettype);
		createVideo.call(this, viewindex, orderinfo.orderindex, seginfo);
	},
	
	findSegmentByType = function(orderinfo, type){
		for(var i=0; i<orderinfo.segments.length; i++){
			if(type == orderinfo.segments[i].type){
				return orderinfo.segments[i];
			}
		}
	},
	
	findOrderInfoByIndex = function(viewindex, orderindex){
		var viewinfo, orderinfo;
		for(var i=0; i<jsoninfo.views.length; i++){
			if(jsoninfo.views[i].viewindex == viewindex){
				viewinfo = jsoninfo.views[i];
				break;
			}
		}
		
		if(!viewinfo)
			return;
		
		for(var j=0; j<viewinfo.order.length; i++){
			if(viewinfo.order[j].orderindex == orderindex){
				orderinfo = viewinfo.order[j];
				break;
			}
		}
		
		return orderinfo;
	},
	
	isSupportMediaSourceExtension = function(){
		var hasWebKit = ("WebKitMediaSource" in window),
	    hasMediaSource = ("MediaSource" in window);
		return (hasWebKit || hasMediaSource);
	},
	
	createVideo = function(viewindex, orderindex, seginfo){
		if(!seginfo)
			return;
		
		createVideoDivLayout(viewindex);
		if(seginfo.type == "mpeg-dash"){
			var videoele = createVideoElement(viewindex, orderindex, "file_" + seginfo.id + "/init.mpd", "dash");
			setDashPlayLayout.call(this, videoele);
		}
		else if(seginfo.type == "mp4"){
			var videoele = createVideoElement(viewindex, orderindex, "file_" + seginfo.id, "html5");
			setHtml5PlayLayout.call(this, videoele);
		}
	},
	
	createVideoDivLayout = function(viewindex){
		var videolayout = $(
				"<div class='videopanel'>" +
				  "<div id='div_videocontainer_" + viewindex + "' class='videocontainer'>" +
				  "</div>" +
				  "<div class='videoinfo'>" +
				  	"<table class='tbselect'>" +
				  		"<tr>" + 
				  		  "<td class='tbimgsm'><img id='plimg1_" + viewindex + "'></td>" + 
				  		  "<td class='tbpadding' id='btn_dash_" + viewindex + "'>MPEG-DASH</td>" + 
				  		"</tr>" +
				  		"<tr>" +
				  		  "<td class='tbimgsm'><img id='plimg2_" + viewindex + "'></td>" + 
				  		  "<td class='tbpadding' id='btn_html5_" + viewindex + "'>HTML5/Flash</td>" + 
				  		"</tr>" +
				    "</table>" +
				    
				  	"<fieldset class='fieldinfo' id='div_dashinfo_" + viewindex + "'>" +
				  	  "<legend>DASH媒体控制</legend>" +
				  	  "<table class='tbselect'>" + 
				  	    "<tr>" + 
				  	      "<td><label for='ckb_adapt_" + viewindex + "'>自适应码率:</label></td>" + 
				  	      "<td align='right'><input class='chbox' id='ckb_adapt_" + viewindex + "' type='checkbox' checked/></td>" +
				  	    "</tr>" +
				  	    "<tr>" +
				  	      "<td><label>码率调整:</label><label id='lab_pendingvideo_" + viewindex + "'></label></td>" + 
				  	      "<td align='right'>" + 
				  	        "<a class='bitratebtn' id='btn_dashvideo_btndec_" + viewindex + "'>-</a>"+ 
				  	        "<a class='bitratebtn' id='btn_dashvideo_bitinc_" + viewindex + "'>+</a>" + 
				  	      "</td>" +
				  	    "</tr>" +
				  	    "<tr>" + 
			  	          "<td><label>当前码率:</label></td>" + 
			  	          "<td align='right'><label id='lab_videobirate_" + viewindex + "'></label></td>" +
			  	        "</tr>" +
			  	        "<tr>" + 
		  	              "<td><label>缓冲时长:</label></td>" + 
		  	              "<td align='right'><label id='lab_downrate_" + viewindex + "'></label></td>" +
		  	            "</tr>" +
				  	   "</table>" +
			        "</fieldset>" +
				    
				    "<fieldset class='fieldinfo'>" +
				      "<legend>视频信息</legend>" +
				      "<label>视口序号:</label>" +
				      "<label id='lab_viewindex_" + viewindex + "'></label>" +
				      "<br/>" +
				      "<label>段序号:</label>" +
				      "<label id='lab_orderindex_" + viewindex + "'></label>" +
				      "<br/>" +
				      "<label>视频长度:</label>" +
				      "<label id='lab_mediaduration_" + viewindex + "'></label>" +
				      "<br/>" +
				      "<label>帧率:</label>" +
				      "<label id='lab_mediafps_" + viewindex + "'></label>" +
				      "<br/>" +
				      "<label>视频尺寸:</label>" +
				      "<label id='lab_mediaresolution_" + viewindex + "'></label>" +
				      "<br/>" +
				      "<label>视频格式:</label>" +
				      "<label id='lab_videocodes_" + viewindex + "'></label>" +
				      "<br/>" +
				      "<label>音频格式:</label>" +
				      "<label id='lab_audiocodes_" + viewindex + "'></label>" +
				    "</fieldset>" +
				  "</div>");
		$(div_video).append(videolayout);
		$("#btn_dash_" + viewindex).bind("click", viewindex, showDashVideo);
		$("#btn_html5_" + viewindex).bind("click", viewindex, showHtml5Video);
		$("#btn_dashvideo_bitinc_" + viewindex).bind("click", { type: "video", isinc: true, index: viewindex}, changeDashBitrate);
		$("#btn_dashvideo_btndec_" + viewindex).bind("click", { type: "video", isinc: false, index: viewindex}, changeDashBitrate);
		$("#ckb_adapt_" + viewindex).bind("click", viewindex, setAdaptiveBitrate);
	},
	
	showDashVideo = function(eventdata){
		var viewindex = eventdata.data;
		var videoele = videoEleArray[viewindex];
		if(videoele.type != "dash"){
			var orderinfo = findOrderInfoByIndex(videoele.getViewIndex(), videoele.getOrderIndex());
			var seginfo = findSegmentByType(orderinfo, "mpeg-dash");
			if(!seginfo){
				return;
			}
			
			videoele.reset();
			videoele.setType("dash");
			videoele.setVideoSrc("file_" + seginfo.id + "/init.mpd");
			$("#div_videocontainer_" + viewindex).empty();
			setDashPlayLayout(videoele);
		}
	},
	
	showHtml5Video = function(eventdata){
		var viewindex = eventdata.data;
		var videoele = videoEleArray[viewindex];
		if(videoele.type != "html5"){
			var orderinfo = findOrderInfoByIndex(videoele.getViewIndex(), videoele.getOrderIndex());
			var seginfo = findSegmentByType(orderinfo, "mp4");
			if(!seginfo){
				return;
			}
			
			videoele.reset();
			videoele.setType("html5");
			videoele.setVideoSrc("file_" + seginfo.id);
			$("#div_videocontainer_" + viewindex).empty();
			setHtml5PlayLayout.call(this, videoele);
		}
	},
	
	setAdaptiveBitrate = function(eventdata){
		var viewindex = eventdata.data;
		var videoele = videoEleArray[viewindex];
		videoele.setAbrEnable($("#ckb_adapt_" + viewindex).prop("checked"));
		refreshDashInfo.call(this, videoele);
	},
	
	changeDashBitrate = function(eventdata){
		var datainfo = eventdata.data;
		var videoele = videoEleArray[datainfo.index];
		videoele.changeVideoRepBitrate(datainfo.type, datainfo.isinc);
		refreshDashInfo.call(this, videoele);
	},
	
	createVideoElement = function(viewindex, orderindex, srcurl, type){
		var video = $("<video controls width='100%' height='100%'/>",
				{
					id: "video_" + viewindex
				});
		var ele = new VideoElement(viewindex, orderindex, srcurl, type, video[0]);
		if(videoEleArray[viewindex] !== undefined){
			videoEleArray[viewindex].reset();
		}
		videoEleArray[viewindex] = ele;
		return ele;
	},

	setVideoDetailInfo = function(viewindex, orderindex, type){
		var orderinfo = findOrderInfoByIndex(viewindex, orderindex);
		if(!orderinfo){
			return;
		}
		
		var seginfo;
		if(type == "dash"){
			seginfo = findSegmentByType(orderinfo, "mpeg-dash");
		}
		else if(type == "html5"){
			seginfo = findSegmentByType(orderinfo, "mp4");
		}
		
		if(!seginfo){
			return;
		}
		
		var dashsup = isSupportMediaSourceExtension();
		if(type == "dash" && dashsup){
			$("#plimg1_" + viewindex).attr("src", "img/playing.gif");
			$("#plimg2_" + viewindex).attr("src", "img/available.gif");
			$("#btn_dash_" + viewindex).addClass("selectli");
			$("#btn_html5_" + viewindex).removeClass("selectli");
			$("#div_dashinfo_" + viewindex).show();
		}
		else if(type == "html5"){
			$("#plimg1_" + viewindex).attr("src", "img/available.gif");
			$("#plimg2_" + viewindex).attr("src", "img/playing.gif");
			$("#btn_dash_" + viewindex).removeClass("selectli");
			$("#btn_html5_" + viewindex).addClass("selectli");
			$("#div_dashinfo_" + viewindex).hide();
		}
		
		if(!dashsup){
			$("#plimg1_" + viewindex).attr("src", "img/unavailable.gif");
			$("#btn_dash_" + viewindex).addClass("disableli");
			$("#btn_dash_" + viewindex).unbind("click", showDashVideo);
		}
		
		$("#lab_viewindex_" + viewindex).html(viewindex);
		$("#lab_orderindex_" + viewindex).html(orderindex);
		$("#lab_mediaduration_" + viewindex).html(seginfo.durations.toFixed(2) + "s");
		$("#lab_mediafps_" + viewindex).html(seginfo.framerate.toFixed(2) + "fps");
		$("#lab_mediaresolution_" + viewindex).html(seginfo.width + "x" + seginfo.height);
		$("#lab_videocodes_" + viewindex).html(seginfo.videocodes);
		$("#lab_audiocodes_" + viewindex).html(seginfo.audiocodes);
	},
	
	//创建dash播放器布局
	setDashPlayLayout = function(videoele){
		setVideoDetailInfo.call(this, videoele.getViewIndex(), videoele.getOrderIndex(), videoele.getType());
		$("#div_videocontainer_" + videoele.getViewIndex()).append(videoele.getVideo());
		videoele.dashInitialize();
		setTimeout(refreshDashInfo.bind(this, videoele), 1000);
	},

	//创建html5播放器布局
	setHtml5PlayLayout = function(videoele){
		setVideoDetailInfo.call(this, videoele.getViewIndex(), videoele.getOrderIndex(), videoele.getType());
		
		var jqvideo = $(videoele.getVideo());
		jqvideo.addClass("video-js vjs-default-skin vjs-big-play-centered");
		//需要通过嵌套source才能使videojs正常工作
		var videosrc = $("<source/>",
			{
				src: videoele.getVideoSrc(),
				type: "video/mp4"
			}
		);
		jqvideo.append(videosrc);
		$("#div_videocontainer_" + videoele.getViewIndex()).append(videoele.getVideo());
		videojs(videoele.getVideo());
		
	//	setTimeout("html5PlayInfoUpdate(" + viewindex + ")", 1000);
	},
	
	refreshDashInfo = function(videoele){
		if(videoele.getType() != "dash"){
			return;
		}
		
		var viewindex = videoele.getViewIndex();
		var videomatic = videoele.getCribbedMetricsFor("video");
		
		if(videomatic){
			$("#div_dashvideoinfo_" + viewindex).show();
			$("#lab_videobirate_" + viewindex).html(videomatic.bandwidthValue + "kbps");
			$("#lab_pendingvideo_" + viewindex).html(videomatic.bitrateIndexValue + "/" + videomatic.numBitratesValue);
			$("#lab_downrate_" + viewindex).html(videomatic.bufferLengthValue + "s");
		}
		else{
			$("#div_dashvideoinfo_" + viewindex).hide();
		}

		setTimeout(refreshDashInfo.bind(this, videoele), 1000);
	},

	html5PlayInfoUpdate = function(viewindex){
		setTimeout("html5PlayInfoUpdate(" + viewindex + ")", 1000);
	};
	
	jsoninfo = $.parseJSON(mediajson);
	
	return {
		initialize : function(){
			addVideosToDiv.call(this);
		}
	};
};

VideoPlayer.prototype = {
		constructor : VideoPlayer
};