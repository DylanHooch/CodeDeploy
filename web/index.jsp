<%--
  Created by IntelliJ IDEA.
  User: Lenovo
  Date: 2017/10/9
  Time: 15:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <div class="content" style="width:800px;height:1000px;">
    <div class="dtree" style="width:200px;float:left;">

      <p><a href="javascript: d.openAll();">open all</a> | <a href="javascript: d.closeAll();">close all</a></p>

      <script type="text/javascript">
          <!--

          d = new dTree('d');

          d.add(0,-1,'My example tree');
          d.add(1,0,'Node 1','');
          d.add(2,0,'Node 2','example01.html');
          d.add(3,1,'Node 1.1','example01.html');
          d.add(4,0,'Node 3','example01.html');
          d.add(5,3,'Node 1.1.1','example01.html');
          d.add(6,5,'Node 1.1.1.1','example01.html');
          d.add(7,0,'Node 4','example01.html');
          d.add(8,1,'Node 1.2','');
          d.add(90,0,'My Pictures','example01.html','Pictures I\'ve taken over the years','','','img/imgfolder.gif');
          d.add(10,90,'The trip to Iceland','example01.html','Pictures of Gullfoss and Geysir');
          d.add(11,90,'Mom\'s birthday','example01.html');
          d.add(12,0,'Recycle Bin','example01.html','','','img/trash.gif');

          document.write(d);

          //-->
      </script>

    </div>
    <div class="aaa" style="width:600px;float:left;">
      <h1>hahaha</h1>
    </div></br></br>
  </div>
  <div id=""></div>
  </body>
</html>
