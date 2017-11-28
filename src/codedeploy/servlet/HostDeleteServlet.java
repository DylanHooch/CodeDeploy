package codedeploy.servlet;

import codedeploy.bean.Host;
import codedeploy.bean.TestHost;
import codedeploy.util.DBOperationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Niko on 2017/11/28.
 */
@WebServlet(name = "HostDeleteServlet")
public class HostDeleteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("id")!=null) {
            String s = request.getParameter("id");
            int id = Integer.parseInt(s);
            ArrayList<Host> del=new ArrayList<>();
            LinkedList<Host> hostlist=(LinkedList)HostInsertServlet.hostList;
            for(int i=0;i<hostlist.size();i++){
                if(hostlist.get(i).getId()==id){
                    del.add(hostlist.get(i));
                }
            }
            for(int i=0;i<del.size();i++){
                hostlist.remove(del.get(i));
            }
            for (Host h : HostInsertServlet.hostList) {
                if (h.getId() == id) {
                    HostInsertServlet.hostList.remove(h);
                }
            }
        }
        request.setAttribute("result",HostInsertServlet.hostList);
        request.getRequestDispatcher("hostmanager.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
