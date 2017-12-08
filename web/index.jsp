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
    <script src="js/popper.js"></script>
    <script src="js/jquery-3.2.1.js"></script>
    <script src="js/bootstrap.min.js"></script>
</head>
<body>
<s:include value="nav.jsp"></s:include>
<div style="width:800px;height:30px">
</div>
<div style="width:1200px;height:1000px;">
    <div style="width:300px;height:1000px;float:left;">
        <div style="width:300px;height:400px;float:left;" id="hostlist">
            <s:include value="tree_good.jsp">
                <s:param name="param" value="'unclickable'"/>
            </s:include>
        </div>
    </div>
    <div style="width:900px;height:200px;float:left;" id="simpleorderlist">
        <%--<s:include value="recentOrderChart.jsp">--%>
            <%--<s:param name="orderList" value="#request.recentOrders"/>--%>
        <%--</s:include>--%>
    </div>
</div>
</body>
</html>
<script>
    $.ajax({
        url: "/recentorder?type=0",
        type: 'get',
        error: function (json) {
            alert("Not Recent Orders");
        },
        async: false,
        success: function (html) {
            $("#simpleorderlist").html(html);

        }
    });
</script>