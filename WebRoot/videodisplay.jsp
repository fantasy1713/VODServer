<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String mediainfo = (String)request.getAttribute("mediainfo");
%>

<!DOCTYPE html>
<html lang="zh-cn">
  <head>
    <base href="<%=basePath%>">
    
    <title>播放页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="css/videodisplay.css">
	<link href="js/videojs/video-js.css" rel="stylesheet" type="text/css">
	<script src="js/videojs/video.dev.js"></script>
	<script src="js/player/videoplayer.js" charset="gbk"></script>
	<script src="js/player/videoelement.js" charset="gbk"></script>
	<script src="js/dash.all.js"></script>
	<script src="js/jquery-1.11.0.min.js"></script>

	<script>
		var mediajson = '<%= mediainfo%>';
		//设置flash播放器路径
       	var idx = document.location.href.lastIndexOf('/');
	    var relpath = document.location.href.substring(0, idx);
	    videojs.options.flash.swf = relpath + "/js/videojs/video-js.swf";
		
		function startVideo(){
			var div_video = $("#div_video");
			var player = new VideoPlayer(mediajson, div_video);
			player.initialize();
		}
	</script>
  </head>
  
  <body onload="startVideo()">
  	<div id="div_video">
  	</div>
  </body>
</html>
