

<%--
  Created by IntelliJ IDEA.
  User: 世勋
  Date: 2017-12-02
  Time: 2:26
  To change this template use File | Settings | File Templates.
--%>

<html>
<head>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="codedeploy.bean.Host" %>
    <%@ page import="codedeploy.bean.Constants" %>
    <%@ page import="java.util.ArrayList" %>
    <%@ page import="java.io.PrintWriter" %>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <script src="js/jquery-3.2.1.js"></script>
    <script src="js/popper.js"></script>
    <script src="js/bootstrap.min.js"></script>
</head>
<body>
<main>

    <%  ArrayList<Host> list=(ArrayList)request.getAttribute("hostlist");
        PrintWriter out1 = response.getWriter();
        out1.println("   ");
    %>

    <div>
        <font charset="utf-8" size="5" color="black">名称: <%=request.getParameter("name")%></font>

        <a  href="javascript:update('<%=request.getParameter("id")%>','<%=request.getParameter("name")%>','<%=request.getParameter("type")%>')"  class="btn btn-outline-secondary" >编辑</a>

        <%--<button class="btn btn-outline-secondary" data-toggle="modal" data-target="#createOrder">增加</button>--%>
        <%   int type=Integer.parseInt(request.getParameter("type"));
            if(type!=6)
        {%>
        <a  href="javascript:insert ('<%=request.getParameter("id")%>','<%=request.getParameter("type")%>')"  class="btn btn-outline-secondary" >增加</a>
        <% } else%>
        <a  href="javascript:insertPhost ('<%=request.getParameter("id")%>')"  class="btn btn-outline-secondary" >增加</a>
    </div>

    <table class="table table-hover table-bordered">
        <thead>
        <tr>

            <th>主机名称</th>
            <th>主机ID</th>
            <th>主机类型</th>
            <th></th>
            <th></th>
        </tr>
        </thead>

        <tbody id="hosts">

        <%
            for(int i=0;i<list.size();i++)
            {
        %>
        <tr>
        <td><%=list.get(i).getAddress()%></td>
        <td><%=list.get(i).getId()%></td>
        <td><%=Constants.typename[list.get(i).getType()]%></td>
        <td><a href="javascript:update('<%=list.get(i).getId()%>','<%=list.get(i).getAddress()%>','<%=list.get(i).getType()%>')">编辑</a></td>
        <td><a href="javascript:Delete('<%=list.get(i).getId()%>','<%=list.get(i).getAddress()%>','<%=list.get(i).getType()%>')">删除</a></td>
        </tr>
             <%
            }
              %>
        </tbody>
    </table>



    <div class="btn-group">
        <button type="button" class="btn btn-outline-secondary"><<</button>
        <button type="button" class="btn btn-outline-secondary disabled">1</button>
        <button type="button" class="btn btn-outline-secondary">>></button>
    </div>


</main>



<script type="text/javascript">
function update(id,name,type)
{

    result = prompt("请输入新名称", name)
    if (result == null)//you click cancel
    {

    }

    else if (result == "")//you click ok, but input nothing
    {
        alert("输入不能为空");
    }
    else//input something and clikc ok
    {   $.ajax({
        url: "/HostUpdateServlet?id=" +id+"&address="+result+"&type="+type,
        type: 'get', //数据发送方
        error: function (json) {
            alert("not lived!");
        },
        async: false,
        success: function (html) {

         reslut=confirm("数据更新成功");
         location.reload();


        }
    });

    }
}

function Delete(id,name,type) {

    result = confirm("请确认是否删除")
    if (result == false)//you click ok, but input nothing
    {

    }
    else//input something and clikc ok
    {
        $.ajax({
            url: "/hostdel?id=" + id + "&name=" + name + "&type=" + type,
            type: 'get', //数据发送方
            error: function (json) {
                alert("not lived!");
            },
            async: false,
            success: function (html) {

                reslut = confirm("数据删除成功");
                location.reload();

            }
        });

    }
}
    function insert(ID,type) {

        result = prompt("请输入新的生产主机地址")
        if (result == null)//you click ok, but input nothing
        {

        }
        else//input something and clikc ok
        {
            $.ajax({
                url: "/hostins?ID=" + ID+ "&Address="+ result+"&type="+type,
                type: 'get', //数据发送方
                error: function (json) {
                    alert("not lived!");
                },
                async: false,
                success: function (html) {

                    reslut = confirm("数据增加成功");
                    location.reload();


                }
            });

        }
    }
function insertPhost(GID) {

    result = prompt("请输入新的生产主机地址")
    if (result == null)//you click ok, but input nothing
    {

    }
    else//input something and clikc ok
    {
        $.ajax({
            url: "/hostinsPh?ID=" + GID+ "&Address="+ result,
            type: 'get', //数据发送方
            error: function (json) {
                alert("not lived!");
            },
            async: false,
            success: function (html) {

                reslut = confirm("数据增加成功");
                location.reload();


            }
        });

    }
}
</script>


</body>
</html>