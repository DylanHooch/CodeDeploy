package codedeploy.servlet;

import codedeploy.bean.Host;
import codedeploy.bean.TestHost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Niko on 2017/11/28.
 */
public class HostInsertServlet extends javax.servlet.http.HttpServlet {
    static List<Host> hostList = new LinkedList<>();
    static {
        hostList.add(new TestHost(1,"test1"));
        hostList.add(new TestHost(1,"test2"));
    }
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        if (request.getParameter("type")!=null) {

            String type = (request.getParameter("type"));

            String address = request.getParameter("address");
            String id = request.getParameter("id");
            System.out.println(type + "\n" + address + "\n" + id);
            //insert


            hostList.add(new TestHost(Integer.parseInt(id), address));
        }
        request.setAttribute("result",hostList);
        request.getRequestDispatcher("hostmanager.jsp").forward(request,response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doPost(request,response);
    }
}
