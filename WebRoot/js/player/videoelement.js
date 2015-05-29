VideoElement = function(viewindex, orderindex, srcurl, newtype, videotag){
	"use strict";
	var video = videotag,
		viewindex = viewindex,
		orderindex = orderindex,
		videosrc = srcurl,
		player,
		type = newtype,
		context;
	
	if(newtype == "dash")
	{
		context = new Dash.di.DashContext();
		player = new MediaPlayer(context);
		player.startup();
	    //video必须是DOM对象，传入Jquery对象将无法识别
		player.attachView(video);
		player.setAutoSwitchQuality(true);
	}
	
	return {
		setType : function(newtype){
			type = newtype;
		},
		
		getType : function(){
			return type;
		},
		
		getVideo : function(){
			return video;
		},
		
		getVideoId : function(){
			return video.id;
		},
		
		getViewIndex : function(){
			return viewindex;
		},
		
		getOrderIndex : function(){
			return orderindex;
		},
		
		setOrderIndex : function(idx){
			orderindex = idx;
		},
		
		getVideoSrc : function(){
			return videosrc;
		},
		
		setVideoSrc : function(srcurl){
			videosrc = srcurl;
		},
		
		setAbrEnable : function(enable){
			player.setAutoSwitchQuality(enable);
		},
		
		getCribbedMetricsFor : function(type){
	        var metrics = player.getMetricsFor(type),
	            metricsExt = player.getMetricsExt(),
	            repSwitch,
	            bufferLevel,
	            httpRequest,
	            droppedFramesMetrics,
	            bitrateIndexValue,
	            bandwidthValue,
	            pendingValue,
	            numBitratesValue,
	            bufferLengthValue = 0,
	            point,
	            lastFragmentDuration,
	            lastFragmentDownloadTime,
	            droppedFramesValue = 0;

	        if (metrics && metricsExt) {
	            repSwitch = metricsExt.getCurrentRepresentationSwitch(metrics);
	            bufferLevel = metricsExt.getCurrentBufferLevel(metrics);
	            httpRequest = metricsExt.getCurrentHttpRequest(metrics);
	            droppedFramesMetrics = metricsExt.getCurrentDroppedFrames(metrics);

	            if (repSwitch !== null) {
	                bitrateIndexValue = metricsExt.getIndexForRepresentation(repSwitch.to);
	                bandwidthValue = metricsExt.getBandwidthForRepresentation(repSwitch.to);
	                bandwidthValue = bandwidthValue / 1000;
	                bandwidthValue = Math.round(bandwidthValue);
	            }

	            numBitratesValue = metricsExt.getMaxIndexForBufferType(type);

	            if (bufferLevel !== null) {
	                bufferLengthValue = bufferLevel.level.toPrecision(4);
	            }

	            if (httpRequest !== null) {
	                lastFragmentDuration = httpRequest.mediaduration;
	                lastFragmentDownloadTime = httpRequest.tresponse.getTime() - httpRequest.trequest.getTime();

	                // convert milliseconds to seconds
	                lastFragmentDownloadTime = lastFragmentDownloadTime / 1000;
	                lastFragmentDuration = lastFragmentDuration.toPrecision(4);
	            }

	            if (droppedFramesMetrics !== null) {
	                droppedFramesValue = droppedFramesMetrics.droppedFrames;
	            }

	            if (isNaN(bandwidthValue) || bandwidthValue === undefined) {
	                bandwidthValue = 0;
	            }

	            if (isNaN(bitrateIndexValue) || bitrateIndexValue === undefined) {
	                bitrateIndexValue = 0;
	            }

	            if (isNaN(numBitratesValue) || numBitratesValue === undefined) {
	                numBitratesValue = 0;
	            }

	            if (isNaN(bufferLengthValue) || bufferLengthValue === undefined) {
	                bufferLengthValue = 0;
	            }

	            pendingValue = player.getQualityFor(type);

	            return {
	                bandwidthValue: bandwidthValue,
	                bitrateIndexValue: (pendingValue !== bitrateIndexValue) ? pendingValue + 1 : bitrateIndexValue + 1,
	                pendingIndex: (pendingValue !== bitrateIndexValue) ? "(-> " + (pendingValue + 1) + ")" : "",
	                numBitratesValue: numBitratesValue,
	                bufferLengthValue: bufferLengthValue,
	                droppedFramesValue: droppedFramesValue
	            };
	        }
	        else {
	            return null;
	        }
	    },
		
		//只能上调或下降
		changeVideoRepBitrate : function(type, isincrease){
			 var newQuality,
	            metricsExt = player.getMetricsExt(),
	            max = metricsExt.getMaxIndexForBufferType(type);

			 if(isincrease){
				 newQuality = player.getQualityFor(type) + 1;
			 }
			 else{
				 newQuality = player.getQualityFor(type) - 1;
			 }
	        
	        if (newQuality >= max) {
	            newQuality = max - 1;
	        }
	        if(newQuality < 0){
	        	newQuality = 0;
	        }
	        
	        player.setQualityFor(type, newQuality);
		},
		
		dashInitialize : function(){
			player.setAutoPlay(false);
			player.attachSource(videosrc);
		},
		
		reset : function(){
			var jvideo = $(video);
			//dash clean
			if(player != null)
			{
				player.attachSource(null);
			}
			//html5 clean
			video["player"] = undefined;
			jvideo.removeAttr("class");
			jvideo.removeAttr("src");
			jvideo.empty();
			
			//reset video
			jvideo.attr({id: "video_" + viewindex, width: "100%", height:"100%", controls : true});
		}
	};
};

VideoElement.prototype = {
		constructor : VideoElement
};