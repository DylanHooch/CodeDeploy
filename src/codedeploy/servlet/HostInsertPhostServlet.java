package codedeploy.servlet;

import codedeploy.bean.*;
import codedeploy.util.DBOperationUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by Niko on 2017/11/28.
 */
public class HostInsertPhostServlet extends javax.servlet.http.HttpServlet {

    DBOperationUtil dbo=new DBOperationUtil();


    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

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
