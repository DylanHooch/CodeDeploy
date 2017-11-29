<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 2017/10/9
  Time: 15:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>智能部署上线系统</title>
    <s:head theme="xhtml"/>
    <sx:head parseContent="true"/>
    <!-- include compliled bootstrap plugins-->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/bootstrap.min.js"></script>
    <s:head theme="simple"/>
</head>
<body>
<div style="width:800px;height:100px">
    <H1>智能部署上线系统</H1>
</div>
<div style="width:800px;height:1000px;">
    <div style="width:300px;height:1000px;float:left;">
        <div style="width:300px;height:400px;float:left;" id="hostlist">
            <s:include value="tree.jsp"/>
        </div>
        <div style="width:500px;height:50px;float:left">
            <a href="/hostins">主机管理</a>
        </div>
    </div>
    <div style="width:500px;height:200px;float:left;" id="simpleorderlist">
        <s:include value="recentOrderChart.html"/>
    </div>
</div>
</body>
</html>
