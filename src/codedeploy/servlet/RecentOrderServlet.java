package codedeploy.servlet;

import codedeploy.bean.DeployOrder;
import codedeploy.util.DBOperationUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;

public class RecentOrderServlet extends HttpServlet {
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
        response.setContentType("text/xml;charset=utf-8");
        int type=Integer.parseInt(request.getParameter("type"));
        PrintWriter out = response.getWriter();
        out.println(" <>");
        if(type==0) {
            List<DeployOrder> dporders = dbo.queryOrder(10);
            request.setAttribute("dporders", dporders);
            request.getRequestDispatcher("recentOrderChart.jsp").forward(request, response);
        }
        else{
            String text=request.getParameter("text");
            System.out.println(text);
            List<DeployOrder> dporders=dbo.queryOrderByBriefName(text,10);
            System.out.println(dporders.size());
            request.setAttribute("dporders", dporders);
            request.getRequestDispatcher("recentOrderChart.jsp").forward(request, response);
        }

    }


}
