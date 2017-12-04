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
                    var nodeName=$(this).text();
                    var id=$(this).attr("hostID");

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
                            tree.add(nodeId,parentId,nodeName,"javascript:localHost('"+id+"')","","","","",false);
                        else if(nodeId<100)
                            tree.add(nodeId,parentId,nodeName,"javascript:testHost(this.id)","","","","",false);
                        else if(nodeId>200)
                            tree.add(nodeId,parentId,nodeName,"javascript:groups(id)","","","","",false);
                        else tree.add(nodeId,parentId,nodeName,"javascript:pHost(id)","","","","",false);
                });
            }

        });
    document.write(tree);
    function localHost(id) {
        $.ajax({
            url: "LocalHostAction?id=" + id,
            type: 'post', //数据发送方式
            dataType: 'xml', //接受数据格式

            error: function (json) {
                alert("not lived!");
            },
            async: false,
            success: function (xml) {
                var arr = new Array();
                $(xml).find("node").each(function () {
                    var nodeId = $(this).attr("nodeId");
                    var parentId = $(this).attr("parentId");
                    var hrefAddress = $(this).attr("hrefAddress");
                    var nodeName = $(this).text();
                    var id = $(this).attr("hostID");
                    arr.push(id, nodeName)
                });
                var html = "";
                for (var i = 0; i < arr.length; i++) { //遍历data数组
                    var l1 = arr[i];
                    i++;
                    var l2 = arr[i];

                    html += "<tr>"
                        + "<td><input type='radio'checked='checked' value='TRUE'/></td>"
                        + "  <td>"
                        + l1
                        + "</td>"
                        + " <td>"
                        + l2
                        + "</td>"
                        + " <td>无备份</td>"
                        + "  <td>已发布</td>"
                        + "<td>未回滚</td>"
                        + " <td>C12各基佬</td>"
                        + " <td>2017/11/27</td>"
                        + " <td>2017/11/27</td>"
                        + "  <td><a href='1.jpg'>啪啪啪</a></td>"
                        + "</tr>";
                }
                $("#hosts").html(html);
            }
        });
    }

//             $.ajax({
////                    url:"localHostChart.jsp?array="+arr,
//                    url:"\orderChart.jsp",
//                    type:"get",
//                    error:function(json){
//                        alert( "not lived!");
//                    },
//                    async:false,
//                    success:function (data) {
//                        $("#simpleorderlist").html(data);
//                    }
//                })
//            }
//        });
//    }
//                $.ajax({
//                    url:"localHostChart.do?array="+arr,
//                    type:"post",
//                    error:function(json){
//                        alert( "not lived!");
//                    },
//                    async:false,
////                    success:function (data) {
////                        $("#simpleorderlist").html(data);
////                    }
//                })
//				$.ajax({
//					url:"\orderChart.jsp",
//					type:"post",
//					data:'{"id": "uname", "age": 18}'
//					error:function(json){
//						alert( "not lived!");
//					},
//					async:false,
//					success:function (data) {
//						$("#simpleorderlist").html(data);
//					}
//				})


    function groups(id) {

    }
    function   pHost(id) {

    }



    function testHost(id) {

        $.ajax({
            url:"\orderChart.html?id="+id,
            type:"get",
            error:function(json){
                alert( "not lived!");
            },
            async:false,
            success:function (data) {
            }
        })

    }
</script>
</body>
</html>
