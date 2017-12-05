<%@ page import="codedeploy.bean.Host" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="codedeploy.bean.TestHost" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Niko
  Date: 2017/11/28
  Time: 19:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<html>
<head>
    <title>主机管理</title>
</head>
<body>

<%
    List<Host>hostList = (List<Host>)request.getAttribute("result");
%>


<h1 style="text-align: center">主机管理界面</h1>
<hr/>
<table align="center" border="1">
    <tr>
        <td>GID</td>
        <td>ID</td>
        <td>TYPE</td>
        <td>ADDRESS</td>
    </tr>
    <%
        for (Host host:hostList) {
    %>
        <tr>
            <td>1</td>
            <td><%=host.getId()%></td>
            <td>2</td>
            <td><%=host.getAddress()%></td>
        </tr>
    <%
        }
    %>
</table >
<form action="hostins">
    GID:
    <input type="text" name="gid" value="11111">
    ID:
    <input type="text" name="id" value="14285">
    TYPE:
    <input type="text" name="type" value="1">
    ADDRESS:
    <input type="text" name="address" value="123456">
    <input type="submit" value="插入">
</form>
<form action="hostdel">
    ID:
    <input type="text" name="id" value="111">
    <input type="submit" value="删除">
</form>

<form action="hostsel">
    ID:
    <input type="text" name="id" value="112">
    <input type="submit" value="查询">
</form>

</body>
</html>
