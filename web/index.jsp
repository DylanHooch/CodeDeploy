<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 2017/10/9
  Time: 15:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
  <head>
    <title>T</title>
      <s:head theme="xhtml" />
      <sx:head parseContent="true" />
      <script src="jquery-3.2.1.js"></script>
  </head>
  <body>
  </div>
  <div style="width:800px;height:1000px;">
    <div style="width:300px;height:1000px;float:left;" id="hostlist">
        <s:iterator value="groups" status="st_root">
          <sx:tree label="localhost" id="treeroot" showRootGrid="true" showGrid="true" treeSelectedTopic="treeSelected" >
            <s:iterator value="groups[#st_root.count]" status="st" id="host_in_group">
                <sx:treenode label="groups[st_root].address" >
                    <sx:treenode label=<s:property value="host_in_group"/> >

                    </sx:treenode>
                </sx:treenode>
            </s:iterator>
          </sx:tree>
        </s:iterator>
    </div>
    <div style="width:500px;height:1000px;float:left;" id="simpleorderlist">
        <H3>sx:tree的使用</H3>
  
    </div>
  </div>
  <script>
    $.post("login.jsp");
  </script>
  </body>
</html>
