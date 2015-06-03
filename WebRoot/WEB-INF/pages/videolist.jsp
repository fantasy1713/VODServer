<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML >
<html>
<head>
<base href="<%=basePath%>">

<title>videolist</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

</head>

<body>
	<div>
		<s:iterator value="result.caches">
			<s:if test="realfilename!=null">
				<s:property value="realfilename" /> &nbsp;
  		</s:if>
			<s:else>
				<s:property value="filename" /> &nbsp;
  		</s:else>
			<a href="vod/playback.action?id=<s:property value='id'/>">play！</a>
			<br>
		</s:iterator>
	</div>
	<div>
		<form action="vod/uploadvideo.action" method="post"
			enctype="multipart/form-data">
			<s:file name="upload" label="选择文件" theme="simple"></s:file>
			<input type="submit" value="上传">
		</form>
	</div>

</body>
<script type="text/javascript" src="js/jquery-1.11.0.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {

	});
</script>
<script src="js/videojs/video.dev.js"></script>
<script src="js/player/videoplayer.js" charset="utf-8"></script>
<script src="js/player/videoelement.js" charset="utf-8"></script>
<script src="js/dash.all.js"></script>
</html>
