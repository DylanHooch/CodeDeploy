<%--
  Created by IntelliJ IDEA.
  User: 世勋
  Date: 2017-12-04
  Time: 21:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>主机管理</title>
    <s:head theme="xhtml"/>
    <sx:head parseContent="true"/>
    <!-- include compliled bootstrap plugins-->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/popper.js"></script>
    <script src="js/jquery-3.2.1.js"></script>
    <script src="js/bootstrap.min.js"></script>
</head>
<body>
<s:include value="nav.jsp"></s:include>
<div style="width:800px;height:30px">
    <H1> </H1>
</div>
<div style="width:800px;height:1000px;">
    <div style="width:300px;height:1000px;float:left;">
        <div style="width:300px;height:400px;float:left;" id="hostlist">
            <s:include value="tree_good.jsp">
                <s:param name="param" value="'clickable'"/>
            </s:include>
        </div>
    </div>
    <div style="width:500px;height:200px;float:left;" id="simplehostlist">
    </div>
</div>
</body>
</html>