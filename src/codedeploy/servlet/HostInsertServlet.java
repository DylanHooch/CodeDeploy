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

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        if (request.getParameter("type")!=null) {
            boolean flg=false;
            int type = Integer.parseInt(request.getParameter("type"));
            String address = request.getParameter("Address");
            int id = Integer.parseInt(request.getParameter("id"));
            if(type== Constants.TESTHOST){
                PHostGroup phg=new PHostGroup(-1,address,id);
                dbo.insertGroup(phg);

            }
          else {
                //insert
                Host inshost = new TestHost(id, address);
                switch (type) {
//                case Constants.LOCALHOST:inshost=new LocalHost(id,address,"admin");break;
                    case Constants.LOCALHOST:
                        inshost = new TestHost(-1, address);
                        break;//不允许插入test主机
                    case Constants.PHOSTGROUP:
                        inshost = new ProductHost(-1, address, id);
                        break;
                    default:
                        flg = true;
                        break;
                }
                if (!flg)
                    dbo.insertHost(inshost);
            }
        }


    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doPost(request,response);
    }
}
