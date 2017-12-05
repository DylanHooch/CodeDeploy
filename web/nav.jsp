<%--
  Created by IntelliJ IDEA.
  User: 世勋
  Date: 2017-11-28
  Time: 20:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <html>
    <head>
        <meta charset="utf-8">
        <title>Bootstrap 实例 - 默认的导航栏</title>
        <!-- <link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
        <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        -->
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <script src="jquery-3.2.1.js"></script>
        <script src="popper.js"></script>
        <script src="js/bootstrap.min.js"></script>
    </head>
    <style scoped> body{padding:50px;} </style>
        <nav class="navbar fixed-top navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="index.jsp">智能部署上线系统</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
            <ul class="navbar-nav">
                <li class="nav-item active">
                    <a class="nav-link" href="index.jsp">主页 <span class="sr-only">(current)</span></a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                       订单管理
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                        <a class="dropdown-item" href="#">发布订单</a>
                        <a class="dropdown-item" href="#">增加订单</a>
                        <a class="dropdown-item" href="#">订单列表</a>
                    </div>
                </li>
                <li class="nav-item active">
                    <a class="nav-link"  href="hostmanager_good.jsp">主机管理 <span class="sr-only">(current)</span></a>
                </li>
            </ul>
        </div>

    </nav>

    </html>
