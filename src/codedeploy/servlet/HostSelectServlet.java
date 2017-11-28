package codedeploy.servlet;

import codedeploy.bean.Host;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Niko on 2017/11/28.
 */
@WebServlet(name = "HostSelectServlet")
public class HostSelectServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<Host> hostList=new ArrayList<>();
        if (request.getParameter("id")!=null) {
            int id = Integer.parseInt(request.getParameter("id"));
            for (Host h : HostInsertServlet.hostList) {
                if (h.getId() == id) {
                    hostList.add(h);
                }
            }
        }
        request.setAttribute("result",hostList);
        request.getRequestDispatcher("hostmanager.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
