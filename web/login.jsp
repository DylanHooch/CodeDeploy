<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 2017/10/10
  Time: 19:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    String str=request.getParameter("fxck");
    out.println(str);
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <div style="margin:30px 50px 20px 50px; text-align:center">
        <div style="font-size:14px; font-weight:bold">用户登录</div>
        <div>
            <s:form action="checkLogin" namespace="/login">
            <s:textfield name="fxck" value="%{#request.fxck}" style="font-size:12px;width:120px;"
            label="登录名称" />
            <s:password name="password" style="font-size:12px;width:120px;"
            label="登录密码"/>
            <s:submit value=" 登 录 "/>
            </s:form>
            <ul>
                <% for(int i=0;i<4;i++) {%>
                <li>
                    <%=str%>
                </li>
                <%}%>
            </ul>
        </div>
        </div>
    </div>

</body>
</html>
