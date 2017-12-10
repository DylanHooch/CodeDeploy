<%@ page import="codedeploy.bean.DeployOrder" %>
<%@ page import="java.util.List" %>
<%@ page import="codedeploy.bean.PHostGroup" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: wsz
  Date: 2017/12/2
  Time: 16:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">

<%
    List<DeployOrder> orders = (List<DeployOrder>)request.getAttribute("allorder");
%>

<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/bootstrap.min.css">

</head>
<body>

<main>
    <div>
        <legend class="bg-info text-light">订单创建</legend>
        <p></p>
    </div>

    <form class="form-inline" action="order_select.action" role="form">
        <div class="form-group">
            订单名称:&nbsp &nbsp

            <input type="text" class="form-control" name="order_name"style="margin-right:20px">

            是否发布:&nbsp &nbsp
            <select c class="form-control"style="margin-right:10px">
                <option value="all" style=:display:none">全部</option>
                <option value="yes" style=:display:none">是</option>
                <option value="no" style=:display:none">否</option>
            </select>

            <button class="btn btn-outline-secondary" style="margin-right:10px" >查询</button>
        </div>
    </form>

    <div >
        <button class="btn btn-outline-secondary" data-toggle="modal" data-target="#createOrder">增加</button>
        <button class="btn btn-outline-secondary" type="button" onclick="confirm1(1)">发布</button>
        <button id="rb" class="btn btn-outline-secondary" type="button" onclick="confirm1(2)">回滚</button>
        <button class="btn btn-outline-secondary" type="button" onclick="confirm1(3)">删除</button>
        <button class="btn btn-outline-secondary" type="button" onclick="refreshOrder()">刷新</button>
        <button class="btn btn-outline-secondary" type="button" onclick="alert('检测')">检测</button>
    </div>
    <p></p>
    `
    <table id="table1" class="table table-hover table-bordered table-responsive" >
        <thead >
        <tr>
            <th>选择</th>
            <th>订单名称</th>
            <th>目标机备份</th>
            <th>发布状态</th>
            <th>订单创建日期</th>
        </tr>
        </thead>
        <tbody>
        <% if(orders!=null){
            int row=1;
            for(DeployOrder order : orders ) {%>
        <tr >
            <td><input type="radio" name="selecttr" value="<%=row++ %>" check="checked"/></td>
            <td  style="display: none"><%=order.getOno()%></td>
            <td><%= order.getName()%></td>
            <td >
                <%if(true) {%>
                已备份
                <%}else {%>
                未备份
                <%}%>
            </span></td>
            <td>
                <%if(order.isReleased()==true) {%>
                已发布
                <%}else {%>
                未发布
                <%}%>
            </td>

            <td ><%= order.getDate()%></td>
            <td  style="display: none"><%=order.isReleased()%></td>
        </tr>
        <%}}%>
        </tbody>
    </table>

    <form class="form-inline" role="form">
        <div>
            <p>共1条记录 1/1页
                <select class="form-control">
                    <option value="1">1</option>
                    <option value="5">5</option>
                    <option value="10">10</option>
                    <option value="20">20</option>
                </select>
            </p>
        </div>
    </form>

    <div class="btn-group">
        <button type="button" class="btn btn-outline-secondary"><<</button>
        <button type="button" class="btn btn-outline-secondary disabled">第1页</button>
        <button type="button" class="btn btn-outline-secondary">>></button>
    </div>
    </div>
</main>


<!-- Modal -->
<div class="modal fade" id="createOrder" tabindex="-1" role="dialog" aria-labelledby="createOrderLabel" aria-hidden="true">
    <div class="modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="createOrderLabel">创建新订单</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <!--一个叉 -->
                    &times;
                </button>
                <div class="modal-body" id="createordercontent">
                    <s:action name="order_refresh" executeResult="true" >
                    </s:action>
                </div>
            </div>
        </div>
    </div>
</div>




<script type="text/javascript">
    function checkselectrow(){
        var row=document.getElementById("table1").rows.length;
        for(var i=0;i<row;i++){
            //if(table.rows[i].cells.innerHTML==)
        }
    }
    function confirm1(num){
        var id=table1.rows[$("input[name='selecttr']:checked").val()].cells[1].innerHTML;
        var isrelease=table1.rows[$("input[name='selecttr']:checked").val()].cells[6].innerHTML;


        if(num==1){
            result = confirm("确定发布该订单吗？");
            if (result == true) {
                if(isrelease==true) release(id);
                else alert("该订单已发布!")
            }
        }
        else if(num==2) {
            result = confirm("确定回滚该订单吗？");
            if (result == true) {
                rollback(id);
            }
        }
        else if(num==3){
            reslut=confirm("确认删除订单吗？");
            if(reslut==true){
                deleteOrder(id);
            }
        }
    }
    function refreshOrder(){

            $.ajax({
                url:"/order_refreshOrder.action",
                type:"get",
                success:function(data){
                    //$("#rb_state").text("已回滚");
                    //find(document.getElementsByName("idd").text()));
                    $("#fullorderlist").html(data);

                }
            });
    }
    function rollback(id)
    {
        $.ajax({
            url:"/orderrollback?id="+id,
            type:"get",
            success:function(){
                //$("#rb_state").text("已回滚");
                //find(document.getElementsByName("idd").text()));
                alert("777")

            }
        });
    }
    function release(id)
    {
        $.ajax({
            url:"/orderrelease?id="+id,
            type:"get",
            success:function(data){
                //$("#rb_state").text("已回滚");
                $("#fullorderlist").html(data);
                alert("6666")
            }
        });
    }
    function deleteOrder(id)
    {
        $.ajax({
            url:"/order_delete.action?id="+id,
            type:"get",
            success:function(){
                //$("#rb_state").text("已回滚");

            }
        });
    }
</script>

<script src="js/jquery-3.2.1.js"></script>
<script src="js/popper.js"></script>
<script src="js/bootstrap.min.js"></script>
</body>
</html>
