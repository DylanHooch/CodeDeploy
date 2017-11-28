<%--
  Created by IntelliJ IDEA.
  User: Dog_o
  Date: 2017/11/28
  Time: 19:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
    <title>订单管理详情页</title>
</head>
<body>
<div style="width:800px;height:100px;color:black">
    <H1>订单管理</H1>
</div>
<div style="width:1000px;height:1000px;">
    <div style="width:300px;height:1000px;float:left;" id="hostlist">
        <s:include value="tree.jsp"/>
    </div>

    <div style="width:700px;height:1000px;float:left;" id="simpleorderlist">
        <s:include value="orderChart.html"/>
    </div>
</div>
</body>
</html>
