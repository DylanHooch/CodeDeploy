package codedeploy.servlet;
import codedeploy.CodeDeploySystem;
import codedeploy.bean.*;
import codedeploy.util.DBOperationUtil;
import org.omg.PortableInterceptor.INACTIVE;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class HostServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    DBOperationUtil dbo=new DBOperationUtil();
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String id = request.getParameter("id");
        String type = request.getParameter("type");
        int ID= Integer.parseInt(id);
        int Type=Integer.parseInt(type);
        List<Host> hostlist;
        if(Type==3)
        {
            hostlist =dbo.queryAllHost();

        }
       else if(Type==6)
          hostlist=dbo.queryHostbyGID(ID);
       else if(Type==4)
        {
            List<PHostGroup> grouplist =dbo.queryGroupbyTID(ID);
            if(grouplist.isEmpty())
             hostlist=new ArrayList<>();

           else
            {
                hostlist=grouplist.get(0).getHosts();
            for(int i=1;i<grouplist.size();i++)
            {
                hostlist.addAll(grouplist.get(i).getHosts());
            }
            }
        }else if(Type==5)
        {
            hostlist=dbo.queryHost(ID,Constants.PRODUCTHOST);
        }
        else {
            hostlist = new ArrayList<>();
        }


        response.setContentType("text/xml;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(" <>");
        request.setAttribute("hostlist", hostlist);
        request.getRequestDispatcher("localHostChart.jsp").forward(request, response);


    }


}
