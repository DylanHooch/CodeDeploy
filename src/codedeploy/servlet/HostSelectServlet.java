package codedeploy.servlet;

import codedeploy.bean.Host;
import codedeploy.util.DBOperationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niko on 2017/11/28.
 */
@WebServlet(name = "HostSelectServlet")
public class HostSelectServlet extends HttpServlet {
    DBOperationUtil dbo=new DBOperationUtil();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Host> hostList=new ArrayList<>();
        if (request.getParameter("id")!=null) {
            int id = Integer.parseInt(request.getParameter("id"));
            int type = Integer.parseInt(request.getParameter("type"));
            hostList=dbo.queryHost(id,type);
        }
        request.setAttribute("result",hostList);
        request.getRequestDispatcher("hostmanager.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
