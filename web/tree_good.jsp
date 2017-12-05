<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 2017/11/28
  Time: 14:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>树形结构___ajax请求方式</title>
    <script type="text/javascript" src="js/dtree.js"></script>
    <script type="text/javascript" src="js/jquery-3.2.1.js"></script>
    <link rel="stylesheet" href="css/dtree.css" type="text/css">
    <link rel="bootstrap" href="css/bootstrap.min.css" type="text/css">
    <script src="js/popper.js"></script>
</head>
<body>
<script type="text/javascript">
    tree = new dTree('tree');//创建一个对象.
        $.ajax({
            url:"dynamicTreeAction.action",
            type:'post', //数据发送方式
            dataType:'xml', //接受数据格式
            error:function(json){
                alert( "not lived!");
            },
            async: false ,//同步方式
            success: function(xml){
                $(xml).find("node").each(function(){
                    var nodeId=$(this).attr("nodeId");
                    var parentId=$(this).attr("parentId");
                    var hrefAddress=$(this).attr("hrefAddress");
                    var id=$(this).attr("hostID");
                    var nodeName=$(this).text();

                    if( '<%=request.getParameter("param")%>'=="unclickable")
                    {
                        if(nodeId==0)
                            tree.add(nodeId,parentId,nodeName,"","","","","",false);
                        else if(nodeId<100)
                            tree.add(nodeId,parentId,nodeName,"","","","","",false);
                        else if(nodeId>200)
                            tree.add(nodeId,parentId,nodeName,"","","","","",false);
                        else tree.add(nodeId,parentId,nodeName,"","","","","",false);
                    }
                    else if('<%=request.getParameter("param")%>'=="clickable")
                        if(nodeId==0)
                            tree.add(nodeId,parentId,nodeName,"javascript:localHost('"+id+"','"+nodeName+"')","","","","",false);
                        else if(nodeId<100)
                            tree.add(nodeId,parentId,nodeName,"javascript:testHost('"+id+"','"+nodeName+"')","","","","","",false);
                        else if(nodeId<200)
                            tree.add(nodeId,parentId,nodeName,"javascript:groups('"+id+"','"+nodeName+"')","","","","",false);
                        else tree.add(nodeId,parentId,nodeName,"javascript:pHost('"+id+"','"+nodeName+"')","","","","",false);
                });
            }

        });
    document.write(tree);


    function localHost(id,name) {
        $.ajax({
            url: "/HostServlet?id="+id+"&type=3&name="+name,  //type为主机类型 这里应该对应Constants.LOCALHOST
            type: 'get',
            error: function (json) {
                alert("not lived!");
            },
            async: false,
            success: function (html) {
                $("#simplehostlist").html(html);

            }
        });
    }



    function groups(id,name) {
        $.ajax({
            url: "/HostServlet?id=" + id+"&type=6&name="+name,  //type为主机类型 这里应该对应Constants.PRODUCTHOST
            type: 'get', //数据发送方
            error: function (json) {
                alert("not lived!");
            },
            async: false,
            success: function (html) {
                $("#simplehostlist").html(html);

            }
        });
    }
    function   pHost(id,name) {

        $.ajax({
            url: "/HostServlet?id="+id+"&type=5&name="+name,
            type: 'get', //数据发送方
            error: function (json) {
                alert("not lived!");
            },
            async: false,
            success: function (html) {
                $("#simplehostlist").html(html);

            }
        });
    }


    function testHost(id,name) {

        $.ajax({
            url: "/HostServlet?id=" + id+"&type=4&name="+name,  //type为主机类型 这里应该对应Constants.TESTHOST
            type: 'get', //数据发送方

            error: function (json) {
                alert("not lived!");
            },
            async: false,
            success: function (html) {
                $("#simplehostlist").html(html);

            }
        });

    }
</script>
</body>
</html>
