<%@ page import="codedeploy.bean.DeployOrder" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: He_ee
  Date: 2017/12/4
  Time: 19:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<DeployOrder> orders = (List<DeployOrder>)request.getAttribute("dporders");
%>

<link rel="stylesheet" href="css/bootstrap.min.css">
<script src="js/jquery-3.2.1.js"></script>
<script src="js/popper.js"></script>
<script src="js/bootstrap.min.js"></script>
<div  class="container" >
    <div class="row">
        <div class="col-lg-8"style="float:left;height:60px">
            <font size="6">近期订单</font>
        </div>

        <form class="form-inline col-lg-4" role="form">
            <div class="input-group " >
                <input type="text" class="form-control" id="text" placeholder="订单名称" >
                <span class="input-group-btn">
					<button class="btn btn-outline-secondary" type="button" onclick="javascript:search(document.getElementById('text').value)">
						Search
					</button>
				</span>
            </div><!-- /input-group -->
        </form>
    </div>
</div>
<table  class="table table-hover"  >
    <thead >
    <tr>
        <th>订单名称</th>
        <th>目标机备份</th>
        <th>发布状态</th>
        <th>备份状态</th>
        <th>订单创建日期</th>
    </tr>
    </thead>
    <tbody>
    <% if(orders!=null){
        for(DeployOrder order : orders ) {%>        <tr>
        <td><%= order.getName()%></td>
        <td >
            <%if(order.isReleased()==true) {%>
            <% if(order.IsRollBack()==0){%>
            已备份
            <%} if(order.IsRollBack()==1){%>
            未备份
            <%}}if(order.isReleased()==false) {%>
            未备份
            <%}%>
        </td>
        <td>
            <%if(order.isReleased()==true) {%>
            已发布

            <%}if(order.isReleased()==false) {%>
            未发布
            <%}%>
        </td>
        <td>
            <%if(order.IsRollBack()==0) {%>
            未回滚
            <%}if(order.IsRollBack()==1){%>
            已回滚
            <%}%>
        </td>

        <td ><%= order.getDate()%></td>
    </tr>
    <%}}%>
    </tbody>
</table>
<div align="right" >
    <button type ="button" class="btn btn-outline-secondary" onclick="window.location='select_manageOrder.action';">更多</button>
</div>

<script type="text/javascript" >

function search(text) {
$.ajax({
url: "/recentorder?text="+text+"&type=1",
type: 'get',
error: function (json) {
alert("not lived!");
},
async: false,
success: function (html) {
$("#simpleorderlist").html(html);

}
});
}
</script>