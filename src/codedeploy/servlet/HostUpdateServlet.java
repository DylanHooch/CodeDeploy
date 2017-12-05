package codedeploy.servlet;

import codedeploy.bean.*;
import codedeploy.util.DBOperationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Niko on 2017/11/29.
 */
@WebServlet(name = "HostUpdateServlet")
public class HostUpdateServlet extends HttpServlet {
    DBOperationUtil dbo=new DBOperationUtil();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("type")!=null) {
            boolean flg=false;
            int type = Integer.parseInt(request.getParameter("type"));
            int gid=0;
            if(type== Constants.PRODUCTHOST){
                gid=Integer.parseInt(request.getParameter("gid"));
            }
            String address = request.getParameter("address");
            int id = Integer.parseInt(request.getParameter("id"));
            //insert
            Host inshost=new TestHost(id,address);
            switch (type){
                case Constants.LOCALHOST:inshost=new LocalHost(id,address,"admin");break;
                case Constants.TESTHOST:inshost=new TestHost(id,address);break;
                case  Constants.PRODUCTHOST:inshost=new ProductHost(id,address,gid);break;
                default:flg=true;break;
            }
            if(!flg)
                dbo.updateHost(inshost);
        }
        List<Host> hostList=dbo.queryAllHost();
        request.setAttribute("result",hostList);
        request.getRequestDispatcher("hostmanager.jsp").forward(request,response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
