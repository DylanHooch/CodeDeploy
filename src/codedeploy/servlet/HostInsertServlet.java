package codedeploy.servlet;

import codedeploy.bean.*;
import codedeploy.util.DBOperationUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Niko on 2017/11/28.
 */
public class HostInsertServlet extends javax.servlet.http.HttpServlet {

    DBOperationUtil dbo=new DBOperationUtil();

//    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
//        if (request.getParameter("type")!=null) {
//            boolean flg=false;
//            int type = Integer.parseInt(request.getParameter("type"));
//            int gid=0;
//            if(type== Constants.PRODUCTHOST){
//                gid=Integer.parseInt(request.getParameter("gid"));
//            }
//            String address = request.getParameter("address");
//            int id = Integer.parseInt(request.getParameter("id"));
//            //insert
//            Host inshost=new TestHost(id,address);
//            switch (type){
//                case Constants.LOCALHOST:inshost=new LocalHost(id,address,"admin");break;
//                case Constants.TESTHOST:inshost=new TestHost(id,address);break;//不允许插入test主机
//                case  Constants.PRODUCTHOST:inshost=new ProductHost(id,address,gid);break;
//                default:flg=true;break;
//            }
//            if(!flg)
//                dbo.insertHost(inshost);
//        }
//        List<Host> hostList=dbo.queryAllHost();
//        request.setAttribute("result",hostList);
//        request.getRequestDispatcher("hostmanager.jsp").forward(request,response);
//    }
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

//            int type = Integer.parseInt(request.getParameter("type"));
        int GID =Integer.parseInt(request.getParameter("GID"));
        String Name=request.getParameter("Address");
        int ID=dbo.querymaxPid();
        Host inshost=new ProductHost(ID,Name,GID);
        dbo.insertHost(inshost);


    }
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doPost(request,response);
    }
}
