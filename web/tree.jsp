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
    <script type="text/javascript" src="dtree.js"></script>
    <script type="text/javascript" src="jquery-3.2.1.js"></script>
    <link rel="stylesheet" href="dtree.css" type="text/css"></link>

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
                var nodeName=$(this).text();
                tree.add(nodeId,parentId,nodeName,hrefAddress,"","","","",false);

            });
        }

    });
    document.write(tree);
</script>
</body>
</html>
