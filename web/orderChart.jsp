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
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="css/bootstrap.min.css">

</head>
<body>

<main>
    <div>
        <font size="6" color="blue">订单管理</font>
        <p></p>
    </div>

    <form class="form-inline" role="form">
        <div class="form-group">
            订单名称:&nbsp &nbsp

            <input type="text" class="form-control" name="order_name"style="margin-right:20px">

            是否发布:&nbsp &nbsp
            <select c class="form-control"style="margin-right:10px">
                <option value="all" style=:display:none">全部</option>
                <option value="yes" style=:display:none">是</option>
                <option value="no" style=:display:none">否</option>
            </select>

            <button class="btn btn-outline-secondary" style="margin-right:10px" data-toggle="modal" data-target="#exampleModal">查询</button>
            <button class="btn btn-outline-secondary" type="button" onclick="alert('重置')">重置</button>
        </div>
    </form>

    <div >
        <button class="btn btn-outline-secondary" data-toggle="modal" data-target="#createOrder">增加</button>
        <button class="btn btn-outline-secondary" type="button" onclick="alert('发布')">发布</button>
        <button id="rb" class="btn btn-outline-secondary" type="button" onclick="rollback(1);">回滚</button>
        <button class="btn btn-outline-secondary" type="button" onclick="alert('删除')">删除</button>
        <button class="btn btn-outline-secondary" type="button" onclick="alert('刷新')">刷新</button>
        <button class="btn btn-outline-secondary" type="button" onclick="alert('检测')">检测</button>
    </div>
    <p></p>

    <table class="table table-hover table-bordered table-responsive" >
        <thead >
        <tr>
            <th>选择</th>
            <th>订单名称</th>
            <th>订单描述</th>
            <th>目标机备份</th>
            <th>发布状态</th>
            <th >回滚状态</th>
            <th>创建者</th>
            <th>订单创建日期</th>
            <th>最新执行时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><input type="radio" name="selecttr" checked="checked" /></td>
            <td>订购景栓上门</td>
            <td>VIP订单</td>
            <td>无备份</td>
            <td >已发布</td>
            <td name="rb_state">未回滚</td>
            <td>C12各基佬</td>
            <td>2017/11/27</td>
            <td>2017/11/27</td>
            <td><a href="1.jpg">啪啪啪</a></td>

        </tr>
        <tr>
            <td><input type="radio" name="selecttr"/></td>
            <td>订购景栓上门</td>
            <td>VIP订单</td>
            <td>无备份</td>
            <td>已发布</td>
            <td name="rb_state">未回滚</td>
            <td>C12各基佬</td>
            <td>2017/11/27</td>
            <td>2017/11/27</td>
            <td><a href="1.jpg">啪啪啪</a></td>

        </tr>
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
<div class="modal fade" id="createOrder" tabindex="-1" role="dialog" aria-labelledby="createOrderLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <!--一个叉 -->
                    &times;
                </button>
                <h4 class="modal-title" id="createOrderLabel">创建新订单</h4>
                <div class="modal-body">
                    <s:include value="createOrder.html"/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">



    function rollback(id)
    {
        $.ajax({
            url:"/rollback?id="+id,
            type:"get",
            data:{id:id},
            success:function(data){
                //$("#rb_state").text("已回滚");


                var $c=$("input[name='selecttr']:checked").parents('tr').find(document.getElementsByName("rb_state"));
                $c.text("已回滚")

                alert(id+"订单已回滚");
            }
        });
    }

</script>

<script src="js/jquery-3.2.1.js"></script>
<script src="js/popper.js"></script>
<script src="js/bootstrap.min.js"></script>
</body>
</html>
