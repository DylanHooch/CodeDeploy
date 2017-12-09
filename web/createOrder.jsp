<%@ page import="codedeploy.bean.Host" %>
<%@ page import="java.util.List" %>
<%@ page import="codedeploy.bean.PHostGroup" %><%--
  Created by IntelliJ IDEA.
  User: He_ee
  Date: 2017/12/4
  Time: 20:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="css/bootstrap.css" rel="stylesheet">
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="js/popper.js"></script>
<script src="js/jquery-3.2.1.js"></script>
<script src="js/bootstrap.js"></script>
<body>
<%
List<Host> hostList=(List<Host>)session.getAttribute("hostList");
List<PHostGroup> groupList=(List<PHostGroup>)request.getAttribute("groupListToShow");
int tid=(int)request.getAttribute("tid");
%>

<main>
    <form role="form" action="order_create.action" id="OrdercreateForm">
        <fieldset class="form-group">
            <legend class="bg-info text-light">订单创建</legend>
            <div class="form-group row">
                <label class="col-lg-1 col-md-1 col-xs-1 col-sm-1 col-form-label text-muted" for="Datum_application">基准应用：</label>
                <div class="col-lg-2 col-md-1 col-xs-1 col-sm-1">
                    <select name="tid" id="datum_application" class="form-control">
                        <% for( Host host : hostList) {%>
                        <option  name="tidop" value="<%=host.getId()%>" onclick="refresh(this.value)"><%= host.getAddress()%></option>
                        <%}%>
                    </select>
                    <script type="text/javascript">
                        $("option[name='tidop']").each(function(){
                            if($(this).val()==<%=tid%>){
                                $(this).attr("selected","true");
                            }
                        });
                    </script>
                </div>
                <label class="col-lg-1 col-md-1 col-xs-1 col-sm-1 col-form-label" for="order_name">订单名称： *</label>

                <div class="col-lg-2 col-md-2 col-xs-2 col-sm-2">
                    <input name="oname" class="form-control" id="order_name" type="text" placeholder="example:部署订单"/>
                </div>
            </div>
            <%--<div class="form-group row">--%>
                <%--<label class="col-lg-1 col-md-1 col-xs-1 col-sm-1 col-form-label" for="backups_type">备份类型： *</label>--%>

                <%--<div class="col-lg-2 col-md-2 col-xs-2 col-sm-2">--%>
                    <%--<select id="backups_type" class="form-control">--%>
                        <%--<option>增量备份</option>--%>
                        <%--<option>完全备份</option>--%>
                    <%--</select>--%>
                <%--</div>--%>
                <%--<label class="col-lg-2 col-md-2 col-xs-2 col-sm-2 col-form-label " for="aim_backupsType">目标机备份状态： *</label>--%>

                <%--<div class="clearfix col-lg-2 col-md-2 col-xs-2 col-sm-2">--%>
                    <%--<select id="aim_backupsType" class="form-control">--%>
                        <%--<option>目标机无备份</option>--%>
                        <%--<option>目标机有备份</option>--%>

                    <%--</select>--%>

                <%--</div>--%>
                <%--<div class="col-lg-5 col-md-5 col-xs-5 col-sm-5">--%>
                    <%--<small id="BackupHelp" class="form-text text-danger col-form-label">注意：动态库上线必须选择目标机有备份</small>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<div class="form-group row">--%>
                <%--<label class="col-lg-1 col-md-1 col-xs-1 col-sm-1 col-form-label" for="order_type">订单类型： *</label>--%>
                <%--<div class="clearfix col-lg-2 col-md-2 col-xs-2 col-sm-2">--%>
                    <%--<select id="order_type" class="form-control">--%>
                        <%--<option>升级订单</option>--%>
                        <%--<option>回滚订单</option>--%>

                    <%--</select>--%>

                <%--</div>--%>
            <%--</div>--%>
            <div class="form -group row ">
                <label class="col-lg-1 col-md-1 col-xs-1 col-sm-1 col-form-label" for="Datum_path">基准路径：</label>
                <div class="col-11">
                    <p class="form-control-static" id="Datum_path">/code</p>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-lg-1 col-md-1 col-xs-1 col-sm-1 col-form-label" for="File_list" >文件清单： *</label>
                <div class="col-5">
                    <textarea name="filenames" id="File_list" class="form-control" rows="6"></textarea>
                    <small id="FileHelp" class="form-text text-danger col-form-label">注意：文件清单贴相对于基准路径的相对路径，并且文件清单前不需要加“/”</small>
                </div>
                <div>
                    <button type="button" class="btn btn-default btn-sm">
                        <span class="glyphicon glyphicon-plus"></span>选择
                    </button>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-lg-1 col-md-1 col-xs-1 col-sm-1 col-form-label" >订单目标： *</label>
                <div class="col-5" name="target">
                    <% for( PHostGroup group : groupList) {%>
                    <label class="checkbox-inline">
                        <input type="radio" name="gid" value="<%= group.getId()%>"><%= group.getName()%>
                    </label>
                    <%}%>
                </div>
            </div>
            <%--<div class="form-group row">
                <label class="col-lg-1 col-md-1 col-xs-1 col-sm-1 col-form-label" for="File_list">备注信息： </label>
                <div class="col-5">
                    <textarea class="form-control" rows="4"></textarea>
                </div>
            </div>--%>
            <div class="form-group row">
                <div class="col-4">
                    <button type="button" class="btn btn-default btn-block" data-toggle="modal" data-target="#confirm_dialog">确定</button>

                </div>
                <div class="col-4">
                    <button type="button" class="btn btn-warning btn-block" data-dismiss="modal">取消</button>
                </div>
            </div>
        </fieldset>
    </form>
</main>
<!-- Modal -->
<div class="modal fade" id="confirm_dialog" tabindex="-1" role="dialog" aria-labelledby="confirmLabel"
           aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <!--一个叉 -->
                    <span aria-hidden="true">&times;</span>
                </button>

                <div class="modal-body">
                    <h5 class="modal-title" id="confirm">确认提交订单?</h5>
                </div>
                <div class="modal-footer">
                    <button id="_confirm" type="button" class="btn btn-default btn-sm" >确定</button>
                    <button type="button" class="btn btn-warning btn-sm" data-dismiss="modal" >取消</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    function refresh(tid){
        $.ajax({
            url:"order_refresh.action",
            type:"post",
            data:{tid: tid},
            success:function (data) {
                $("#createordercontent").html(data);
            }
        })
    }
    $("#_confirm").click(function(){
            document.getElementById("OrdercreateForm").submit();
    })
</script>
